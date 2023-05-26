package jeuDeDames;
import java.awt.Component;
import java.awt.Graphics;

import javax.swing.JComponent;

/**
 * This class represents a generic case on a board. It is abstract because it has several methods that 
 * are to be implemented by its subclasses. It extends JComponent because it is a graphical component
 * that can be added to a JFrame or JPanel.
 * @see CaseNoire
 * @see CaseBlanche
 */
public abstract class Case extends JComponent {
    
    /**
     * Size of the case.
     */
    public static final int TAILLE = 50;  
    
    /**
     * Column position of the case.
     */
    protected int col;

    /**
     * Row position of the case.
     */
    protected int lig;
 
    /**
     * This protected variable lets you know if the square is part of an obligatory move.
     * If yes, we draw it in a particular way so that the player can
     * distinguish squares forming part of an obligatory move from others.
     */
    protected boolean obligatoire;

    /**
     * The board to which the case belongs.
     */
    protected Plateau plateau;

    /**
     * Protected variable set to true if box is highlighted. The box is highlighted
     * if the player has selected the piece that is on it or if the player positions the piece that he
     * wants to move to this square. The box is highlighted only if the potential move is valid.
     * @see CaseNoire#setSelect(boolean)
     */
    protected boolean select;
    
    /**
     * Constructor that copies a case but also the piece on it.
     *
     * @param c Case to copy.
     * @param p The board to which the case will belong.
     */
    public Case(Case c,Plateau p) {
        super();
        lig = c.getLigne();
        col = c.getColonne();
        obligatoire = false;
        select = false;
        plateau = p;
    }
    
    /**
     * Constructor that creates a case at the given row and column.
     *
     * @param l Row position.
     * @param c Column position.
     * @param damier The board to which the case will belong.
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
     * Overrides the add() method from the Component class. 
     * Adds a child component.
     *
     * @param comp the component to be added
     * @return the component argument
     * @see java.awt.Container#add(java.awt.Component)
     */
    @Override
    public Component add(Component comp) {
        return super.add(comp);
    }
    
    /**
     * Compares this case with another case.
     *
     * @param c The case to compare with.
     * @return true if the cases have the same row and column, false otherwise.
     */
    public boolean compare(Case c) {
        return c.getLigne() == lig && c.getColonne() == col;
    }
    
    /**
     * Creates a copy of this case.
     *
     * @param p The board to which the new case will belong.
     * @return A new case that is a copy of this case.
     * @see Case#Case(Case, Plateau)
     */
    public abstract Case copie(Plateau p);

    /**
     * Getter for the column position.
     *
     * @return Column position.
     */
    public int getColonne() {
        return col;
    }
    
    /**
     * Getter for the row position.
     *
     * @return Row position.
     */
    public int getLigne() {
        return lig;
    }
    
    /**
     * Getter for the piece on the case.
     *
     * @return Piece on the case.
     */
    public abstract Piece getPiece();

    /**
     * Getter for the board.
     *
     * @return The board to which the case belongs.
     */
    public Plateau getPlateau() {
        return plateau;
    }
    
    /**
     * Method to paint the component, to be implemented by subclasses.
     *
     * @param g The graphics context.
     * @see JComponent#paintComponent(java.awt.Graphics)
     */
    @Override
    public abstract void paintComponent(Graphics g);

    /**
     * Removes the piece from the case.
     */
    public abstract void remove();
    
    /**
     * Sets the mandatory flag.
     *
     * @param b The new value of the flag.
     * @see Case#obligatoire
     */
    public abstract void setObligatoire(boolean b);
    
    /**
     * Sets the selected flag.
     *
     * @param b The new value of the flag.
     * @see Case#select
     */
    public abstract void setSelect(boolean b);

    public abstract boolean isObligatoire();

    public abstract boolean isSelect();


    /**
     * Provides a String representation of the Case object,
     * indicating its row and column coordinates.
     *
     * @return a string representation of the Case in the format "row,column"
     */
    @Override
    public String toString() {
        return lig + "," + col;
    }
}