package jeuDeDames;

import java.awt.Graphics;

import javax.swing.JComponent;

/**
 * Abstract class representing a generic piece in a game. This class provides the basic
 * attributes and actions a piece can perform. It should be extended by other classes
 * that provide a specific implementation for each kind of game piece.
 *
 * The piece knows its own color, direction, the player that controls it, its position
 * on the game board, and its screen coordinates.
 */
public abstract class Piece extends JComponent {
    
    /**
     * Constant representing a white piece.
     */
    public static final int BLANC = 1;

    /**
     * Constant representing a black piece.
     */
    public static final int NOIR = 0;

    protected int couleur;

    protected int direction;
    
    protected transient Joueur joueur=null;
    
    protected Plateau plateau;

    protected CaseNoire position;

    protected int x;

    protected int y;

    /**
     * Constructor for a Piece object. Initializes the color, position, player, and dimensions of the piece.
     * @param c     color of the piece.
     * @param pos   initial position of the piece.
     * @param p     the game board.
     * @param j     the player that controls the piece.
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
     * Copy constructor for a Piece object. Creates a new piece with the same color and position as the provided piece.
     * @param p     the piece to copy.
     * @param c     the position for the new piece.
     */
    public Piece(Piece p, CaseNoire c) {
        super();
        couleur = p.getCouleur();
        position = c;
        plateau = c.getPlateau();
    }

    /**
     * Abstract method to determine the next possible position for the piece. 
     * Should be implemented in a subclass.
     * @return  the next possible case for the piece.
     */
    public abstract Case casePossible();

    /**
     * Creates a copy of this piece at a new position.
     * @param c     the position for the new piece.
     * @return      a new piece identical to this piece at the specified position.
     */
    public abstract Piece copie(CaseNoire c);

    /**
     * Determines if there is a mandatory capture for the piece. 
     * Should be implemented in a subclass.
     * @param prise     the position of the piece to be captured.
     * @return          the capture action that must be performed.
     */
    public abstract Rafle coupObligatoire(CaseNoire prise);

    /**
     * Determines if the piece has any valid moves.
     * Should be implemented in a subclass.
     * @return  true if the piece has any valid moves, false otherwise.
     */
    public abstract boolean coupPossible();

    /**
     * Moves the piece to a new position on the board.
     * @param c     the new position for the piece.
     */
    public void deplacer(CaseNoire c) {
        if(position != null)
            position.remove();
        position = c;
        c.add(this);
    }

    /**
     * Gets the color of the piece.
     * @return  the color of the piece.
     */
    public int getCouleur() {
        return couleur;
    }

    /**
     * Gets the direction of the piece.
     * @return  the direction of the piece.
     */
    public int getDirection() {
        return direction;
    }

    /**
     * Gets the player that controls the piece.
     * @return  the player that controls the piece.
     */
    public Joueur getJoueur() {
        return joueur;
    }

    /**
     * Gets the current position of the piece.
     *
     * @return  the current position of the piece.
     */
    public CaseNoire getPosition() {
        return position;
    }

    /**
     * Determines if the proposed capture action is valid for this piece. 
     * Should be implemented in a subclass.
     *
     * @param r     the proposed capture action.
     * @return      true if the proposed capture is valid, false otherwise.
     */
    public abstract boolean isCoupValide(Rafle r);

    /**
     * Paints the piece on the board. Overrides the method from JComponent.
     *
     * @param g     the Graphics object to paint on.
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
    }

    /**
     * Changes the piece to a more powerful piece (if applicable). 
     * Should be implemented in a subclass.
     */
    public abstract boolean promotion();

    /**
     * Sets the player that controls the piece.
     *
     * @param j     the player that will control the piece.
     */
    public void setJoueur(Joueur j) {
        joueur = j;
    }

    /**
     * Changes the screen position of the piece. Overrides the method from JComponent.
     *
     * @param w     the new x-coordinate for the piece.
     * @param h     the new y-coordinate for the piece.
     */
    @Override
    public void setLocation(int w,int h) {
        x = w;
        y = h;
        repaint();
    }

    /**
     * Sets the position of the piece on the game board.
     *
     * @param c     the new position for the piece.
     */
    public void setPosition(CaseNoire c) {}

    /**
     * Provides a string representation of the piece. 
     * A typical output could be "D:ligne,colonne" for a dame (queen),
     * and "P:ligne,colonne" for a pion (pawn).
     * This method must be overridden in the subclass.
     *
     * @return  a string representation of the piece.
     */
    public abstract String toString();
}