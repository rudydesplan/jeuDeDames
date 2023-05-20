package jeuDeDames;

import java.awt.Color;
import java.awt.Graphics;

/**
 * Represents a white square (case) on the game board in a game of Checkers (Jeu de Dames). 
 * Extends the abstract Case class.
 * In Checkers, pieces can only move on black squares, so white squares do not contain any pieces.
 */
public class CaseBlanche extends Case {
    
    /**
     * Copy constructor. Creates a new white square as a copy of the provided one.
     *
     * @param c the CaseBlanche object to be copied
     * @param p the Plateau object representing the game board
     * @see Case#Case(Case, Plateau)
     */
    public CaseBlanche(CaseBlanche c,Plateau p) {
        super(c,p);
    }
    
    /**
     * Parameterized constructor. Creates a new white square at the specified position.
     *
     * @param l the row of the square on the game board
     * @param c the column of the square on the game board
     * @param p the Plateau object representing the game board
     */
    public CaseBlanche(int l,int c,Plateau p) {
        super(l,c,p);
    }
    
    /**
     * Creates a copy of the current white square.
     *
     * @param p the Plateau object representing the game board
     * @return a new CaseBlanche object that is a copy of the current one
     * @see Case#copie(Plateau)
     */
    public Case copie(Plateau p) {
        return new CaseBlanche(this,p);
    }
    
    /**
     * Always returns null, as white squares do not contain any pieces.
     *
     * @return null
     */
    public Piece getPiece() {
        return null;
    }
    
    /**
     * Paints the square white.
     *
     * @param g the Graphics object used for painting
     * @see Case#TAILLE
     * @see javax.swing.JComponent#paintComponent(Graphics)
     */
    public void paintComponent(Graphics g) {
        g.setColor(Color.WHITE);
        g.fillRect(0,0,Case.TAILLE,Case.TAILLE);
    }

    /**
     * Does nothing, as there are no pieces to remove from white squares.
     */
    public void remove() {
    }
    
    /**
     * Does nothing, as white squares cannot be mandatory.
     *
     * @param b a boolean value (ignored)
     */
    public void setObligatoire(boolean b) {
    }
    
    /**
     * Does nothing, as white squares cannot be selected.
     *
     * @param b a boolean value (ignored)
     */
    public void setSelect(boolean b) {
    }
}