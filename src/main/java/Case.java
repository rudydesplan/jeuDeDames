/*
 *****************************************************************************
 *                         Case.java  -  description                  
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

import java.awt.Component;
import java.awt.Graphics;

import javax.swing.JComponent;


/**
 * Classe abstraite d crivant une case du plateau du Jeu de Dames. Cette classe h rite de la 
 * classe JLabel puisqu'on souhaite l'ajouter   un JPanel. On red finit donc les m thodes d'affichages.
 * On impl mente  galement l'interface MouseListener puisqu'on souhaite g rer les cliques et les d placement de souris.
 * 
 * @author rudy
 * @see CaseNoire
 * @see CaseBlanche
 * 
 */
public abstract class Case extends JComponent {
    
    /**
     * Constante enti re qui d finit la taille des cases du damier en pixel.
     */
    public static final int TAILLE = 50;  
    
    /**
     * Entier contenant la colonne o  se trouve la case.
     */
    protected int col;
    
    /**
     * Entier contenant la ligne o  se trouve la case.
     */
    protected int lig;
    
    /**
     * Cette variable prot g e permet de savoir si la case fait partie d'un coup obligatoire. 
     * Si c'est oui, on la dessine d'une mani re particuli re afin que le joueur puisse
     * distinguer les cases faisant partie d'un coup obligatoire des autres. 
     */
    protected boolean obligatoire;
    
    /**
     * Contient la plateau sur laquelle se trouve la case.
     */
    protected Plateau plateau;
    
    /**
     * Variable prot g e positionn  a vrai si la case est en surbrillance. La case est en surbrillance
     * si le joueur a s lectionn e la pi ce qui est dessus ou si le joueur positionne la pi ce qu'il 
     * veut d placer sur cette case. La case est en surbrillance uniquement si le coup potentiel est
     * valide. 
     * 
     * @see CaseNoire#setSelect(boolean)
     */
    protected boolean select;
    
    /**
     * Ce constructeur r alise une copie de la case <code>c</code>, mais  galement de la pi ce qui s'y
     * trouve. Ce constructeur doit uniquement  tre utilis  pour la recherche des coups obligatoires.
     * En effet, dans l'algorithme, comme on doit simuler des coups, on les effectue dans une copie
     * du plateau du jeu r el afin de ne pas perdre les donn es de celui-ci.
     * @param c Case dont on d sire effectuer la copie
     * @param p Plateau ou se situe la case.
     */
    
    // On doit  galement passer l'instance de la classe plateau. Car sinon on prendrait le plateau
    // de la case que l'on souhaite copier, ce qui aurait pour cons quence de modifier le contenu
    // de ce plateau.
    
    public Case(Case c,Plateau p) {
        super();
        lig = c.getLigne();
        col = c.getColonne();
        obligatoire = false;
        select = false;
        plateau = p;
    }
    
    
    /**
     * Constructeur de base de la classe Case.
     * @param l entier d signant la ligne.
     * @param c entier d signant la colonne.
     * @param damier sur lequel se trouve la case.
     */
    
    public Case(int l, int c, Plateau damier) {
        super();
        lig = l;
        col = c;
        obligatoire=false;
        select = false;
        plateau = damier;
    }
    
    
    /**
     * M thode qui permet d'ajouter un <code>Component</code>   la case. Ici on ajoute une <code>piece</code>.
     * @see  java.awt.Container#add(java.awt.Component)
     */
    public Component add(Component comp) {
        return super.add(comp);
    }
    
    /**
     * M thode qui permet de comparer deux cases. On ne compare que les lignes et les colonnes.
     * @param c Case avec laquel on souhaite r aliser la comparaison.
     * @return Retourne true si les cades sont semblables, false sinon
     */
    public boolean compare(Case c) {
        if(c.getLigne() == lig && c.getColonne() == col)
            return true;
        return false;
    }
    
    /**
     * Retourne un copie de la <code>case</code>.
     * @return la copie de la <code>case</code>.
     * @param p Plateau o  se trouve la case
     * @see Case#Case(Case, Plateau)
     */
    public abstract Case copie(Plateau p);
    
    /**
     * Retourne le num ro de la colonne de la <code>case</code>.
     * @return Colonne de la <code>case</code>.
     */
    public int getColonne() {
        return col;
    }
    
    /**
     * Retourne le num ro de la ligne de la <code>case</code>.
     * @return Ligne de la <code>case</code>.
     */
    public int getLigne() {
        return lig;
    }
    
    /**
     * M thode abstraite renvoyant la <code>pi ce se trouvant sur la <code>case</code>.
     * @return <code>Pi ce</code> se trouvant sur la <code>case</code>, null s'il n'y a pas de <code>pi ce</code> dessus
     */
    public abstract Piece getPiece();
    
    /**
     * Retourne le <code>plateau</code> sur lequel se trouve la <code>case</code>.
     * @return <code>Plateau</code> sur lequel se trouve la <code>case</code>.
     */
    public Plateau getPlateau() {
        return plateau;
    }
    
    /**
     * M thode abstraite red finissant la m thode paintComponent de la classe <code>JLabel</code>
     * permettant de personnaliser l'affichage de la <code>case</code>.
     * @see JComponent#paintComponent(java.awt.Graphics)
     */
    public abstract void paintComponent(Graphics g);
    
    /**
     * M thode qui enl ve la pi ce se trouvant sur la case.
     *
     */
    public abstract void remove();
    
    /**
     * M thode permettant d'indiquer a ma <code>case</code> si elle fait partie
     * d'un coup obligatoire.
     * @param b true si la case est obligatoire, false sinon
     * @see Case#obligatoire
     */
    public abstract void setObligatoire(boolean b);
    
    
    /**
     * M thode permettant de s lectionner un <code>case</code>.
     * @see Case#select
     * @param b true si la case est selectionn , false sinon
     **/
    public abstract void setSelect(boolean b);
    
    /**
     * M thode renvoyant une cha ne caract risant la case : "ligne,colonne".
     * @return cha ne d crivant la case.
     */
    public String toString() {
        return lig + "," + col;
    }
    
}
