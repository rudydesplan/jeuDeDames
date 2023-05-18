/*
 *****************************************************************************
 *                         Plateau.java  -  description                  
 *                            -------------------                        
 *   begin                : 18 mai. 2023                                      
 *   copyright            : (C) 2023 by Rudy Desplan
 *   email                : rudy.desplan@etud.univ-paris8.fr                
 *****************************************************************************
 
 ***************************************************************************
 *                                                                         *
 *   This program is free software; you can redistribute it and/or modify  *
 *   it under the terms of the GNU General Public License as published by  *
 *   the Free Software Foundation; either version 2 of the License, or     *
 *   (at your option) any later version.                                   *
 *                                                                         *
 ***************************************************************************
 * 
 */

package jeuDeDames;


import java.awt.Dimension;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

/**
 * Classe d�crivant le plateau du jeu de dames.
 * @author rudy
 */
public class Plateau extends JComponent {
    
    /**
     * Hauteur des Jlabel contenant le num�ro de colonnes
     */
    private static int hauteur = 20;
    /**
     * Largeur des JLabel contentant le num�ro des lignes
     */
    private static int largeur = 20;
    
    /**
     * Cette variable vaut true si le plateau est actif, false sinon
     */
    private boolean actif;
    
    /**
     * Arbitre controllant le jeu.
     */
    private transient Arbitre arbitre;	
    
    /**
     * Case s�lectionn�e par le joueur. Une case est s�lectionn� lorsque le joueur a cliqu� sur la pi�ce
     * qui est dessus
     */
    private CaseNoire caseSelectionnee;
    
    /**
     * Coup obligatoire. Cette variable vaut true s'il y a un coup obligatoire, false sinon.
     * 
     */
    private boolean obligatoire;
    
    /**
     * Piece selectionn�e par le joueur. Il s'agit de la pi�ce positionn� sur la case selectionn�e par le joueur
     * @see Plateau#caseSelectionnee
     */
    private Piece pieceSelec;
    
    /**
     * Tableau de Cases symbolisant la plateau.
     */
    private Case[][] plateau;
    
    /**
     * Constructeur de la classe Plateau.
     */
    public Plateau() {
        super();
        this.setPreferredSize(new Dimension(11*Case.TAILLE,11*Case.TAILLE));
        plateau = new Case[10][10];
        caseSelectionnee = null;
        obligatoire = false;
        /* Initialisation des cases blanches */
        for(int i=0;i<10;i++) {
            JLabel ligne = new JLabel(Integer.toString(i+1));
            ligne.setBounds(0,hauteur+i*Case.TAILLE,largeur,Case.TAILLE);
            ligne.setHorizontalAlignment(SwingConstants.CENTER);
            add(ligne);
            for(int j=(i%2);j<10;j+=2) {
                JLabel colonne = new JLabel(Character.toString((char) ('A' + j)));
                colonne.setBounds(j*Case.TAILLE+largeur,0,Case.TAILLE,hauteur);
                colonne.setHorizontalAlignment(SwingConstants.CENTER);
                add(colonne);
                plateau[i][j] = new CaseBlanche(i,j,this);
                get(i,j).setBounds(largeur+j*Case.TAILLE,hauteur + i*Case.TAILLE,Case.TAILLE,Case.TAILLE);
                add(get(i,j));
            }
        }
        
        /* Initialisation des cases noires */
        for(int i=0;i<10;i++) {
            for(int j=((i+1)%2);j<10;j+=2) {
                plateau[i][j] = new CaseNoire(i,j,this);
                get(i,j).setBounds(largeur+j*Case.TAILLE,hauteur + i*Case.TAILLE,Case.TAILLE,Case.TAILLE);
                add(get(i,j));
            }
        }
    }
    
    /**
     * Constructeur qui r�alise une copie du plateau p. M�thode apell� pour calculer les coups obligatoires.
     * En effet, pour ne pas perdre les donn�es de la partie, on effectue les simulation sur des copies du plateau
     * du jeu.
     * @param p plateau a copier.
     */
    public Plateau(Plateau p) {
        super();
        plateau = new Case[10][10];
        caseSelectionnee = null;
        obligatoire = p.isObligatoire();
        /* Copie des Cases Blanches */
        for(int i=0;i<10;i++) {
            for(int j=0;j<10;j++) {
                plateau[i][j] = p.get(i,j).copie(this);
            }
        }
        
    }
    
    /**
     * M�thode qui g�re la s�lection des cases.
     * @param c Case sur laquelle le joueur a cliqu�
     * @param p Piece se situant sur la case.
     */
    public void caseSelect(CaseNoire c,Piece p) {
        if(!actif)
            return;
        //Couleur des pieces du joueur actif
        int couleur = arbitre.getJoueurActif().getCouleur();
        
        // Il n'y a pas de case selectionn�e
        if(caseSelectionnee==null) {
            // Existe un coup obligatoire
            if(p.getCouleur() == couleur) {
                // Selectionne la case
                caseSelectionnee = c;
                c.setSelect(true);
                // On met a jour les coups obligatoire.
                // On ne met en surbrillance que les cases pouvant �tre atteinte
                if(obligatoire)
                    arbitre.misAJourCoupsObligatoire(c);
                pieceSelec = p;
                
            }
        }
        
        // Il y a une case selectionnee
        else{
            //Deselection case.
            if(c == caseSelectionnee) {
                pieceSelec = null;
                //On redessine tous les coups obligatoires
                if(obligatoire)
                    arbitre.dessineCoupObligatoire();
                initialise();
            }
            else {
                // Coup Obligatoire
                if(obligatoire) {
                    Rafle r1;
                    Rafle r2;
                    // On recupere la rafle passant par la case selectionn� par le joueur.
                    r1 = arbitre.getRafle(caseSelectionnee);
                    
                    if(r1!=null) {
                        // On recupere la rafle passant par la case c.
                        r2 = r1.getRaflesSuivantes(c);
                        r1.toString();
                        if( r2 != null) {
                            // Recupere la case prise
                            
                            Case prise = get(r2.getPrise().getLigne(),r2.getPrise().getColonne());
                            
                            // On initialise un coup
                            Rafle coup = new Rafle(caseSelectionnee,(CaseNoire) prise,arbitre.getJoueurActif());
                            coup.addCasesSuivantes(c);
                            
                            // Ajoute la piece deplac� sur se nouvelle case.
                            pieceSelec.deplacer(c);
                            initialise();
                            // On avertie l'arbitre qu'un coup est jou�.
                            arbitre.finCoup(coup,r2.getNbPrises());
                        }
                    }
                }
                // Pas de coup obligatoire.
                else {
                    Rafle coup = new Rafle(caseSelectionnee,null,arbitre.getJoueurActif());
                    coup.addCasesSuivantes(c);
                    pieceSelec.deplacer(c);
                    initialise();
                    arbitre.finCoup(coup,0);
                }
            }
        }
        repaint();
        
    }
    
    
    /**
     * Retourne la case situ� � la m�me position, c'est-�-dire m�me ligne et m�me colonne. 
     * @param c Case qui est retourn�e
     * @return Retourne la case c.
     */
    public Case get(Case c) {
        if(c!=null) {
            return plateau[c.getLigne()][c.getColonne()];
        }
        return null;
    }
    
    /**
     * Retourne la case se situant � la ligne l,et � la colonne c.
     * @param l ligne de la case.
     * @param c colonne de la case.
     * @return Case situ� en position l,c
     */
    public Case get(int l, int c) {
        //Arguments sont valides
        if(l>=0 && l<10 && c>=0 && c<10)
            return plateau[l][c];
        
        return null;
        
    }
    
    /**
     * Retourne l'arbitre controllant le jeu
     * @return arbitre controllant le jeu
     */
    public Arbitre getArbitre() {
        return arbitre;
    }
    
    /**
     * Retourne la case qui est selectionn�e
     * @return La case qui est selectionn�e, null s'il n'y a pas de case.
     * @see Plateau#caseSelectionnee
     */
    public CaseNoire getCaseSelect() {
        return caseSelectionnee;
    }
    
    /**
     * M�thode retournant la hauteur du plateau
     * @return hauteur du plateau
     */
    @Override
    public int getHeight() {
        return 10*Case.TAILLE + hauteur;
    }
    
    /**
     * Retourne la piece selectionn�e
     * @return Piece selectionn�e, null s'il n'y a pas de pi�ce.
     * @see Plateau#pieceSelec
     */
    public Piece getPieceSelec() {
        return pieceSelec;
    }
    
    /**
     * Retourne la largeur du tableau
     * @return largeur du tableau
     */
    @Override
    public int getWidth() {
        return 10*Case.TAILLE + largeur;
    }
    
    
    /**
     * M�thode qui initialise le plateau. C'est a dire d�selectionne la pi�ce et la case selectionn�e.
     * @see Plateau#caseSelectionnee
     * @see Plateau#pieceSelec
     *
     */
    public void initialise() {
        caseSelectionnee = null;
        pieceSelec = null;
        
    }
    
    /**
     * Retourne un bool�en si il y a un coup obligatoire. 
     * @return true s'il y a un coup obligatoire, false sinon
     * @see Plateau#obligatoire
     */
    public boolean isObligatoire() {
        return obligatoire;
    }
    
    /**
     *  M�thode qui permet d'activer ou non le plateau
     * 
     * @param b true pour activer le plateau, false pour le d�sactiver
     */
    public void setActif(boolean b) {
        actif = b;
    }
    
    /**
     * Cette m�thode affecte un arbitre au plateau
     * @param a Arbitre affect� au plateau
     */
    public void setArbitre(Arbitre a) {
        arbitre = a;
    }
    
    /**
     * Permet de mettre a jour la variable obligatoire.
     * @param b indique s'il y a un coup obligatoire ou non.
     */
    public void setObligatoire(boolean b) {
        obligatoire = b;
    }
    /**
     * Supprime les pieces se situant sur la plateau.
     *
     */
    public void vider() {
        for(int i=0;i<10;i++) {
            for(int j=0;j<10;j++) {
                get(i,j).remove();
            }
        }
        repaint();
    }
}
