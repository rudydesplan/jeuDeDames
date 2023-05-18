/*
 *****************************************************************************
 *                         Piece.java  -  description                  
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

import java.awt.Graphics;

import javax.swing.JComponent;




/**
 * 
 * Classe abstraite symbolisant une pi�ce du Jeu de Dames, c'est-�-dire les pions
 * et les dames.
 * @author rudy
 * @see Pion
 * @see Dame
 * 
 */
public abstract class Piece extends JComponent {
    
    /**
     * Constante enti�re d�signant la couleur Blanche pour une pi�ce.
     */
    public static final int BLANC = 1;
    
    /**
     * Constante enti�re d�signant le couleur Noir pour une pi�ce.
     */
    public static final int NOIR = 0;
    
    /**
     * Contient la couleur de la pi�ce :  soit BLANC, soit NOIR.
     * @see Piece#NOIR
     * @see Piece#BLANC
     */
    protected int couleur;
    
    /**
     * Direction dans laquelle se d�place le pion. Direction vaut -1 pour les pions blancs, en effet,
     * dans le jeu les pions blancs se trouvent dans la partie basse du plateau, elle doivent donc monter,
     * c'est a dire que les lignes doivent diminuer. Pour les pions noires, c'est l'inverse, direction vaut donc +1.
     */
    protected int direction;
    
    /**
     * Joueur qui poss�de la pi�ce.
     */
    protected transient Joueur joueur=null;
    
    /**
     * Plateau sur lequel se trouve la piece.
     */
    protected Plateau plateau;
    
    /**
     * Position de la pi�ce.
     */
    protected CaseNoire position;
    
    /**
     * Position de la pi�ce sur la case, il s'agit du coin en haut � gauche.
     */
    protected int x;
    
    /**
     * Position de la pi�ce sur la case, il s'agit du coin en haut � gauche.
     */
    protected int y;
    
    /**
     * Construcuteur de la classe Piece.
     * @param c Couleur de la pi�ce.
     * @param pos Position de la pi�ce.
     * @param j Joueur qui poss�de la pi�ce
     * @param p Plateau sur lequel la pi�ce est dispos�e
     */
    public Piece(int c, CaseNoire pos, Plateau p, Joueur j) {
        super();
        couleur = c;
        position = pos;
        joueur = j;
        setBounds(5,5,Case.TAILLE-10,Case.TAILLE-10);
        
        //Initialisation de la position des pi�ces sur la case.
        y=(Case.TAILLE-10)/2;
        x=y;
        
        plateau = p;
    }
    
    
    /**
     * Constructeur prenant en argument une piece. Ceci permet de cr�er une copie de la pi�ce p, positionn� 
     * sur la case <code>c</code>. Il est important de passer en argument la case <code>c</code>, en effet, si on
     * ne faisait pas �� la piece <code>p</code> et se copie se trouverait sur la m�me case !
     * @param p Piece que l'on veut "copier"
     * @param c Case sur laquelle se trouve la pi�ce
     */
    public Piece(Piece p, CaseNoire c) {
        super();
        couleur = p.getCouleur();
        position = c;
        plateau = c.getPlateau();
        
    }
    
    /**
     * M�thode qui retourne une case o� la pi�ce peut se d�placer.	
     * @return une case ou la piece peut se d�placer.
     */
    public abstract Case casePossible();
    
    /**
     * Retourne une copie de la pi�ce. Cette m�thode est utilis� lorsque l'on souhaite r�alis� une copie enti�re 
     * du damier. On fait ceci lorsque qu'on souhaite simuler des coups afin de d�terminer le coups obligatoires. 
     * @return une copie de la pi�ce
     * @param c Case sur laquelle se trouve la pi�ce
     */
    public abstract Piece copie(CaseNoire c);
    
    /**
     * Calcul le coup obligatoire pour la pi�ce
     * @param prise Case ou le pion pr�c�dent a �t� pris
     * @return Rafle maximale pour la pi�ce
     */
    public abstract Rafle coupObligatoire(CaseNoire prise);
    
    /**
     * M�thode qui recherche si la dame peut bouger.
     * @return true si la dame peut bouger, false sinon.
     */
    public abstract boolean coupPossible();
    
    /**
     * M�thode qui d�place la pi�ce sur la case <code>c</code>.
     * @param c Case que laquelle le pion est d�plac�.
     */
    public void deplacer(CaseNoire c) {
        if(position != null)
            position.remove();
        position = c;
        c.add(this);
    }
    
    /**
     * M�thode qui renvoit le couleur de la pi�ce
     * @return la couleur de la piece
     * @see Piece#NOIR
     * @see Piece#BLANC
     */
    public int getCouleur() {
        return couleur;
    }
    
    /**
     * Retourne la direction dans laquelle se d�place le pion.
     * @return la direction dans laquelle se d�place le pion.
     */
    public int getDirection() {
        return direction;
    }
    
    /**
     * Retourne la <code>joueur</code> qui poss�de la pi�ce
     * @return Joueur qui poss�de la pi�ce.
     */
    public Joueur getJoueur() {
        return joueur;
    }
    
    /**
     * M�thode qui retourne la position (Case) sur laquelle se trouve la pi�ce
     * @return Case sur laquelle se trouve la pi�ce.
     */
    public CaseNoire getPosition() {
        return position;
    }
    
    /**
     * M�thode qui v�rifie que le Coup c est valide.
     * @param r la rafle que l'on souhaite v�rifi�.
     * @return un bool�en, vrai si le coup est valide, faux sinon.
     */
    public abstract boolean isCoupValide(Rafle r);
    
    /**
     * M�thode qui dessine la piece.
     * @see javax.swing.JComponent#paintComponent(Graphics)
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
    }
    
    /**
     * M�thode qui g�re la promotion des pi�ces
     */
    public abstract void promotion();
    
    /**
     * Permet de d�finir la joueur qui poss�de la pi�ce.
     * @param j
     */
    public void setJoueur(Joueur j) {
        joueur = j;
    }
    
    /**
     * M�thode qui permet de modifier l'endroit ou la pi�ce se dessine.
     * @param w Abscisse de la fen�tre
     * @param h ordonn�e de la fen�tre
     */
    @Override
    public void setLocation(int w,int h) {
        x = w;
        y = h;
        repaint();
    }
    
    /**
     * M�thode qui permet de changer la position de la pi�ce
     * @param c nouvelle position de la pi�ce
     * @see Piece#position
     */
    public void setPosition(CaseNoire c) {
        position = c;
    }
    
    /**
     * Retourne une cha�ne d�crivant la piece. On a une cha�ne de la forme "D:ligne,colonne" pour une dame,
     * et "P:ligne,colonne" pour un pion.
     * 
     * @return retourne une description de la piece sous forme de cha�ne
     */
    @Override
    public abstract String toString();
    
}
