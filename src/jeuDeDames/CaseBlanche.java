/*
 *****************************************************************************
 *                         CaseBlanche.java  -  description                  
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
 * Classe d crivant une case blanche du Jeu de Dames. Cette case est <i>"inactive"</i>. En effet, 
 * au Jeu de Dames, on joue uniquement sur les case noires.
 *  @author rudy
 * 
 */
public class CaseBlanche extends Case {
    
    /**
     * Constructeur qui r alise un copie de la <code>CaseBlanche</code>.
     * @param c CaseBlanche que l'on souhaite copier.
     * @param p Plateau sur lequel se trouve la case.
     * 
     * @see Case#Case(Case, Plateau)
     */
    public CaseBlanche(CaseBlanche c,Plateau p) {
        super(c,p);
    }
    
    /**
     * Constructeur de CaseBlanche.
     * @param l ligne de la case.
     * @param c colonne de la case.
     * @param p Plateau sur lequel se trouve la case.
     */
    public CaseBlanche(int l,int c,Plateau p) {
        super(l,c,p);
    }
    
    /**
     * Retourne un copie de la case.
     * @return la copie de la case.
     * @see Case#copie(Plateau)
     */
    public Case copie(Plateau p) {
        return new CaseBlanche(this,p);
    }
    
    /**
     * M thode renvoyant la piece situ  sur la case.
     * Dans le jeu de Dames on ne joue pas sur les cases blanches, elle
     * renvoit donc toujours nulle.
     * 
     * @return null;
     */
    public Piece getPiece() {
        return null;
    }
    
    /**
     * M thode red finit de la classe JPanel, permettant de personnaliser 
     * l'affichage d'une case blanche, ici on dessine un carre de c t  TAILLE de couleur blanche.
     * @see Case#TAILLE
     * @see javax.swing.JComponent#paintComponent(Graphics)
     */
    public void paintComponent(Graphics g) {
        g.setColor(Color.WHITE);
        g.fillRect(0,0,Case.TAILLE,Case.TAILLE);
    }
    
    /**
     * M thode qui supprimer la pi ce de la case
     */
    public void remove() {
        //On n'enleve rien puisqu'il n'y a pas de pi ces sur les cases blanches
    }
    
    /**
     * M%m thode permettant de rendre la case "obligatoire". Comme aucun coup obligatoire
     * ne peut avoir lieu, cette m thode ne fait rien.
     * @param b true si la case est obligatoire, false sinon
     */
    public void setObligatoire(boolean b) {
        // les cases blanches en peuvent  tre obligatoire
    }
    
    /**
     * M thode permettant de s lectionner une case. On ne peut pas s lectionn  de case, ici donc
     * la m thode ne fait rien.
     * @param b true si la case est selectionn , false sinon
     */
    public void setSelect(boolean b) {
        //les cases blanches ne peuvent  tre s lectionn es
    }
}


