/*
 *****************************************************************************
 *                         rafle.java  -  description                  
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

import java.util.Vector;

/**
 * <p>La classe Rafle symbolise un coup d'une rafle effectu�e au jeu de dames. Une rafle
 * est donc symbolis� par une liste de Rafle. Chaque �l�ment (objet) contient la case du
 * d�but du coup, la case prise lors du coup pr�c�dent et un vecteur contenant la listes des cases suivantes possibles.
 * En effet il se peut que pour une case donn�e il y ait plusieurs autres coups possibles.</p>
 * 
 * <p>On trouve �galement le nombres de pions pris a partir de la case "debut".</p>
 *
 * <p> Il est important de noter que l'on retient que les coups <i>maximums</i>, c'est � dire ceux
 * qui permettent de prendre le maximum de pions.</p> 
 * @author rudy
 * 
 */
public class Rafle {
    
    /**
     * Case du d�but du coup.
     */
    private CaseNoire caseDebut;
    /**
     * Case prise lors du coup pr�c�dent.
     */
    private CaseNoire casePrise;
    
    /**
     * Vecteur contenant la listes des cases suivantes potentiels.
     */
    private Vector<Rafle> casesSuivantes;
    
    /**
     * Joueur effectuant la rafle;
     */
    private Joueur joueur = null;
    /**
     * Nombre prises maximum par la rafle commen�ant � la case caseDebut.
     */
    private int nbPrises;
    /**
     * Piece prise lors du coup pr�c�dent
     */
    private Piece piecePrise;
    
    /**
     * Constructeur par d�faut
     * @param d case 
     * @param p case ou la pi�ce a �t� prise.
     * @param j Joueur qui a effectu� le coup
     */
    public Rafle(CaseNoire d, CaseNoire p,Joueur j) {
        caseDebut = d;
        casePrise = p;
        if(casePrise != null) 
            piecePrise = casePrise.getPiece();
        else piecePrise=null;
        
        nbPrises=0;
        casesSuivantes = new Vector<>();
        joueur = j;
    }
    /**
     * Ajoute une case a la liste des cases suivantes.
     * @param c
     */
    public void addCasesSuivantes(CaseNoire c) {
        casesSuivantes.add(new Rafle(c,null,joueur));
    }
    
    /**
     * Ajoute une rafle a la liste des cases suivantes
     * @param r Rafle a ajouter.
     */
    public void addRaflesSuivantes(Rafle r) {
        casesSuivantes.add(r);
    }
    
    /**
     * M�thode qui vide le vecteur qui contient la liste des cases suivantes.
     *
     */
    public void clearCasesSuivantes() {
        casesSuivantes.clear();
    }
    
    /**
     * Permet d'effacer les coups obligatoires du Plateau p.
     * @param p Instance du plateau p sur lequel on efface les <i>"cases obligatoires"</i>.
     * @see Plateau#isObligatoire()
     */
    public void effaceCoups(Plateau p) {
        CaseNoire c = null;
        int dirLig;
        int dirCol;
        int dlig;
        int dcol;
        int alig;
        int acol;
        
        p.get(caseDebut).setObligatoire(false);
        if(casePrise != null)
            p.get(casePrise).setObligatoire(false);
        for(int i=0;i<casesSuivantes.size();i++) {
            
            c = (casesSuivantes.elementAt(i)).getCaseDebut();
            dlig = caseDebut.getLigne();
            dcol = caseDebut.getColonne();
            alig = c.getLigne();
            acol = c.getColonne();
            
            dirLig = dlig - alig;
            dirCol = dcol - acol;
            while((dlig != alig) && (dcol != acol) && (dlig >= 0) && (dlig <= 9) && (dcol >= 0) && (dcol <= 9)) {
                dlig += dirLig;
                dcol += dirCol;
                
                // Guard against stepping out of bounds due to the last increment
                if((dlig >= 0) && (dlig <= 9) && (dcol >= 0) && (dcol <= 9)) {
                    p.get(dlig,dcol).setObligatoire(false);
                }
            }
            
            (casesSuivantes.elementAt(i)).effaceCoups(p);
        }
    }
    
    /**
     * 
     * @return case du d�but de la rafle
     */
    public CaseNoire getCaseDebut() {
        return caseDebut;
    }
    
    /**
     * Retourne la i�me case suivante.
     * @param i i�me case suivante a retourner.
     * @return Retourne la i�me case suivante, null sinon.
     */
    public CaseNoire getCasesSuivantes(int i) {
        if(i<casesSuivantes.size())
            return (casesSuivantes.get(i)).getCaseDebut();
        return null;
    }
    
    /**
     * Retourne le joueur ayant effectu� la rafle.
     * @return Joueur ayant effectu� la rafle.
     */
    public Joueur getJoueur() {
        return joueur;
    }
    /**
     * Retourne le nombre de cases suivantes
     * @return nombre de cases suivantes
     */
    public int getNbCasesSuivantes() {
        return casesSuivantes.size();
    }
    
    /**
     * @return nombre de prises effectu� par cette rafle.
     */
    public int getNbPrises() {
        return nbPrises;
    }
    
    /**
     * Retourne la piece prise lors de la rafle
     * @return piece prise lors de la rafle
     */
    public Piece getPiecePrise() {
        return piecePrise;
    }
    
    /**
     * Retourne la case noire qui a �t� prise.
     * @return La case noire ou une pi�ce a �t� prise.
     */
    public CaseNoire getPrise() {
        return casePrise;
    }
    
    /**
     * Cette m�thode recherche dans la listes des cases suivantes, la case <i>c</i>. 
     * Si on la trouve dans la liste des cases suivantes, on renvoie la Rafle qui lui est affect�.
     * @param c Case recherch�.
     * @return Retourne la rafle passant par la case c.
     */
    public Rafle getRaflesSuivantes(CaseNoire c) {
        for(int i=0;i<casesSuivantes.size();i++) {
            if((casesSuivantes.elementAt(i)).getCaseDebut().compare(c)) {
                return casesSuivantes.elementAt(i); 
            }
        }
        return null;
    }
    
    /**
     * Retourne le i�me rafle suivante
     * @param i i�me rafle que l'on souhaite retourner
     * @return i�me rafle suivante
     */
    public Rafle getRaflesSuivantes(int i) {
        return casesSuivantes.elementAt(i);
    }
    /**
     * 
     * @return Vecteur contenant la liste des cases suivantes. C'est � dire la liste des
     * cases ou le joueur peut continuer son coup.
     */
    public Vector<Rafle> getVector() {
        return casesSuivantes;
    }
    
    /**
     * Permet de mettre a jour les cases <i>"obligatoires"</i> du Plateau p.
     * @param p Instance du Plateau sur lequel on souhaite mette � jour les coups.
     * @see Plateau#isObligatoire()
     */
    public void misAJourCoups(Plateau p) {
        
        p.get(caseDebut).setObligatoire(true);
        if(casePrise != null)
            p.get(casePrise).setObligatoire(true);
        for(int i=0;i<casesSuivantes.size();i++) {
            (casesSuivantes.elementAt(i)).misAJourCoups(p);
        }
    }
    
    /**
     * Permet de d�finir la case du d�but de la rafle.
     * @param c CaseNoire qui va devenir la case du d�but de la rafle
     */
    public void setCaseDebut(CaseNoire c) {
        caseDebut = c;
    }
    
    /**
     * M�thode permettant de d�finir ou la case a �t� prise.
     * @param prise Case ou il y a un prise.
     */
    public void setCasePrise(CaseNoire prise) {
        if(prise == null)
            piecePrise=null;
        else
            piecePrise = prise.getPiece();
        casePrise = prise;
    }
    
    /**
     * M�thode qui permet d'attribuer un joueur a la rafle.
     * @param j joueur auteur de la rafle.
     */
    public void setJoueur(Joueur j) {
        joueur = j;
    }
    
    /**
     * Permet de d�finir le nombre de prises de la rafle.
     * @param n Nombre de prises maximales
     */
    public void setNbPrises(int n) {
        nbPrises = n;
    }
    
    /**
     * M�thode qui d�finit la piece prie.
     * @param prise piece qui est prise lors de la rafle.
     */
    public void setPiecePrise(Piece prise) {
        piecePrise = prise;
    }
    
    /**
     * Attribue la i�me rafle des cases suivantes
     * @param i i�me case suivante a modifier
     * @param coup nouvelle r�gle suivante
     */
    public void setRaflesSuivantes(int i,Rafle coup) {
        casesSuivantes.setElementAt(coup,i);
    }
    
    /**
     * Retourn une cha�ne d�crivant la rafle
     * @return Retourne un cha�ne de caract�re d�crivant la Rafle.
     */
    public String toString() {
        StringBuilder s = new StringBuilder();
        if(joueur != null) {
            s.append(joueur.getNom());
        }
        s.append("|").append(caseDebut).append("|").append(casePrise).append("|").append(piecePrise).append("|");
        
        for(int i = 0; i < casesSuivantes.size(); i++) {
            s.append("[").append(casesSuivantes.elementAt(i).toString()).append("]");
        }
        
        return s.append(";").toString();
    }
    
    
    
}
