/*
 *****************************************************************************
 *                         Pion.java  -  description                  
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
import java.awt.Color;
import java.awt.Graphics;

/**
 * Classe symbolisant un pion du jeu de dames.
 * @author rudy
 */
public class Pion extends Piece{
    
    /**
     * Constructeur de la classe Pion
     * @param c Couleur de la pi ce
     * @param pos Case (CaseNoire) sur laquelle se trouve la pi ce.
     * @param j Joueur qui poss de la pi ce
     * @param p Plateau sur lequel la pi ce se trouve
     */
    public Pion(int c, CaseNoire pos,Plateau p,Joueur j) {
        super(c,pos,p,j);
        if(c == BLANC) direction = -1;
        else direction =1;
        
    }
    
    /**
     * Constructeur r alisant une copie du Pion p
     * @param p Pion que l'on d sire copier.
     * @param c Case ou se trouve la pi ce
     */
    public Pion(Pion p, CaseNoire c) {
        super(p,c);
        direction = p.getDirection();
    }
    
    /**
     * Retourne la premi re case vide ou peut se d placer le pion.
     * @return premi re case possible ou le pion peut se d placer.
     */
    public Case casePossible() {
        Case c = null;
        if(( c = plateau.get(position.getLigne()+direction,position.getColonne()+1) )!=null) {
            if(c.getPiece()==null)
                return c;
        }
        if( (c=plateau.get(position.getLigne()+direction,position.getColonne()-1)) !=null) {
            if(c.getPiece()==null)
                return c;
        }
        return null;
    }
    
    /**
     * Retourne un copie du pion
     * @return la copie de la pi ce
     * @param c Case o  se trouve la pi ce
     */
    public Piece copie(CaseNoire c) {
        return new Pion(this,c);
    }
    
    /**
     * Calcul et retourne le coup obligatoire pour le pion.
     * @param prise la case ou le pion pr c dent est pris
     * @return le coup obligatoire pour le pion
     */
    public Rafle coupObligatoire(CaseNoire prise) {
        
        /*
         * On recherche dans les quatre directions s'il y a des
         * prises possibles. on stocke le r sultat dans rafle.
         * 
         */
        int lig = position.getLigne();
        int col = position.getColonne();
        //Case c,c2;
        //Piece piece;
        Rafle rafle=new Rafle(position,prise,null);
        int nbPrises=0;
        
        // Calcul effectu  pour la case situ  en bas   droite
        nbPrises = coupPossible(lig,col,1,1,rafle,nbPrises);
        
        // Calcul effectu  pour la case situ  en bas   gauche
        nbPrises = coupPossible(lig,col,-1,1,rafle,nbPrises);
        
        //Calcul effectu  pour la case situ  en haut   gauche
        nbPrises = coupPossible(lig,col,1,-1,rafle,nbPrises);
        
        // Calcul effectu  pour la case situ  en haut   droite
        nbPrises = coupPossible(lig,col,-1,-1,rafle,nbPrises);
        
        if(prise!=null) {
            nbPrises++;
        }
        rafle.setNbPrises(nbPrises);
        return rafle;
    }
    
    /**
     * M thode qui recherche si la pion peut bouger.
     * @return true si la pion peut bouger, false sinon.
     */
    public boolean coupPossible() {
        Case c = null;
        if(( c = plateau.get(position.getLigne()+direction,position.getColonne()+1) )!=null) {
            if(c.getPiece()==null)
                return true;
        }
        if( (c=plateau.get(position.getLigne()+direction,position.getColonne()-1)) !=null) {
            if(c.getPiece()==null)
                return true;
        }
        return false;
    }
    
    /**
     * <p>Cette m thode calcul le coup obligatoire pour le pion dans la direction <code>dLig</code>
     * ,<code>dCol</code>. Le r sultat est stock  dans <code>rafle</code>. On retourne le nombre 
     * de prises maximales que l'on peut faire avec le pion.</p>
     * 
     * @param lig Ligne de la pi ce o  l'on souhaite effectu e la recherche.
     * @param col Colonne de la pi ce o  l'on souhaite effectu e la recherche.
     * @param dLig Direction ligne de recherche.
     * @param dCol Direction colonne de recherche.
     * @param rafle Rafle obligatoire. 
     * @param nbPrises Nombre de prises obligatoire avant le calcul.
     * @return Nombres de prises maximales que l'on peut faire apr s le calcul.
     */
    private int coupPossible(int lig, int col,int dLig, int dCol,Rafle rafle, int nbPrises) {
        
        Case c = null;
        Case c2 = null;
        Piece piece = null;
        
        
        if((c = plateau.get(lig+dLig,col+dCol)) != null) {
            if((piece = c.getPiece()) != null) {
                if(piece.getCouleur() != couleur) {
                    if((c2 = plateau.get(lig+2*dLig,col+2*dCol)) != null) {
                        if(plateau.get(lig+2*dLig,col+2*dCol).getPiece() == null) {
                            /*  Prise possible */
                            //Suppression du pion pris
                            c.remove();
                            //D placement de la pi ce
                            deplacer((CaseNoire) c2);
                            
                            Rafle r = coupObligatoire(piece.getPosition());
                            if(r.getNbPrises()>nbPrises) {
                                nbPrises=r.getNbPrises();
                                rafle.clearCasesSuivantes();
                                rafle.addRaflesSuivantes(r);
                            }
                            else {
                                if(r.getNbPrises()==nbPrises) {
                                    rafle.addRaflesSuivantes(r);	
                                }
                            }
                            
                        }
                    }
                }
            }
        }
        return nbPrises;
    }
    
    /**
     * Retourne la direction de d placement du pion.
     * @return Renvoit la direction de la piece.
     * @see Pion#direction
     * 
     */
    public int getDirection() {
        return direction;
    }
    
    /**
     * M thode validant un coup.
     * @param coup Coup a valider
     * @return true si le coup est valide, false sinon
     */
    public boolean isCoupValide(Rafle coup) {
        CaseNoire depart = coup.getCaseDebut();
        CaseNoire arrivee = coup.getCasesSuivantes(0);
        
        // Les deux cases sont identiques, c bon
        if(depart == arrivee)
            return true;
        
        // Si la pion ne se d place pas d'une ligne dans la bonne direction, mauvais d placement
        if(arrivee.getLigne() != depart.getLigne() + direction)
            return false;
        
        //Calcul de la diff rence de colonne
        int diff = arrivee.getColonne() - depart.getColonne();
        
        // Si cette diff rence est diff rente de 1 ou -1, erreur.
        if(diff != 1 && diff != -1)
            return false;
        return true;
    }
    
    /**
     * M thode red finit de la classe JLabel, permettant de personnaliser 
     * l'affichage d'un pion, ici on dessine un cercle.
     * @see javax.swing.JComponent#paintComponent(Graphics)
     */
    public void paintComponent(Graphics g) {
        int color = couleur*75+150;
        int cote = Case.TAILLE-10;
        g.setColor(new Color(color,color,color));
        g.fillOval(x-cote/2,y-cote/2,cote,cote);	
    }
    
    /**
     * M thode qui g re la promotion du pion.
     */
    public void promotion() {
        if(position.getLigne() == (couleur-1)*(-9) )
            joueur.promotion(this);
    }
    
    /**
     * Renvoit un cha ne de la forme "P:ligne,colonne" pour d crire la position du pion.
     * @return retourne une description de la piece sous forme de cha ne
     */
    public String toString() {
        return "P("+couleur+"):" + position.toString();
    }
    
}
