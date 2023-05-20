package jeuDeDames;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Represents a black square (case) on the game board in a game of Checkers (Jeu de Dames). 
 * Extends the abstract Case class and implements the MouseListener interface.
 * In Checkers, pieces can only move on black squares, so these squares can contain pieces and handle mouse events.
 */
public class CaseNoire extends Case implements MouseListener {
    
    /**
     * The piece that is currently on the square. 
     * Null if there is no piece.
     */
    private Piece piece;
    
    /**
     * Copy constructor. Creates a new black square as a copy of the provided one.
     * If the copied square has a piece, the new square also gets a copy of the piece.
     *
     * @param c the CaseNoire object to be copied
     * @param p the Plateau object representing the game board
     * @see Case#Case(Case, Plateau)
     */
    public CaseNoire(CaseNoire c,Plateau p) {
        super(c,p);
        if(c.getPiece() != null) {
            piece = c.getPiece().copie(this);
        }
        else piece = null;
    }
    
    /**
     * Parameterized constructor. Creates a new black square at the specified position.
     * Initially, there is no piece on the square.
     *
     * @param l the row of the square on the game board
     * @param c the column of the square on the game board
     * @param p the Plateau object representing the game board
     */
    public CaseNoire(int c, int l,Plateau p) {
        super(c,l,p);
        piece = null;
        addMouseListener(this);
    }
    
    /**
     * Adds a Component (specifically a Piece) to the square.
     * Also updates the position and dimensions of the piece on the square.
     *
     * @param comp the Component (Piece) to be added
     * @return the Component (Piece) that was added
     */
    public Component add(Component comp) {
        piece = (Piece) comp;
        if(comp != null) {
            super.add(comp);
            piece.setLocation(Case.TAILLE/2-5,Case.TAILLE/2-5);
            piece.setBounds(5,5,Case.TAILLE,Case.TAILLE);
        }
        return comp;  
    }
    
    /**
     * Creates a copy of this black square on the provided game board.
     * If this square has a piece, the copy will also get a copy of the piece.
     *
     * @param p the Plateau object representing the game board
     * @return the copy of this CaseNoire object
     * @see Case#copie(Plateau)
     */
    public Case copie(Plateau p) {
        return new CaseNoire(this,p);
    }
    
    /**
     * Returns the piece that is currently on the square.
     *
     * @return the Piece object on the square, null if there is no piece
     */
    public Piece getPiece() {
        return piece;
    }

    /**
     * Handles the mouseClicked event. 
     * Depending on whether there is a piece on the square and whether a piece has been selected, 
     * it executes various game logic like moving a piece or selecting/deselecting a piece.
     *
     * @param e the MouseEvent object
     * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
     */
    public void mouseClicked(MouseEvent e) {
        // Il y a un pi ce sur la case
        if(piece!=null) {
            
            //Le joueur d selectionne la case.		
            if(this == plateau.getCaseSelect()) {
                plateau.caseSelect(this,null);
            }
            else {
                //Aucune case n'a encore  t  selectionn e
                if(plateau.getCaseSelect()==null) {
                    //Si la case fait partie d'un coup obligatoire
                    if(obligatoire) {
                        plateau.caseSelect(this,piece);	
                    }
                    else {
                        // La case ne fait donc pas partie d'un coup obligatoire,
                        // on v rifie qu'il n'y a pas de coup obligatoire possible
                        // si ce n'est pas la case, on ne s lectionne pas la case.
                        if(!plateau.isObligatoire())
                            plateau.caseSelect(this,piece);
                    }
                }
            }
            
            
        }
        
        // Il n'y a pas de piece sur la case
        else {
            
            //Si il n'y a pas de piece sur la case, et qu'une piece a  t  s lectionn 
            if(plateau.getCaseSelect() != null) {
                //La case fait partie d'un coup obligatoire.
                if(obligatoire) {
                    plateau.caseSelect(this,null);
                }
                else {
                    //Sinon on autorise le d placement que s'il n'y a pas de coup
                    // obligatoire.
                    if(!plateau.isObligatoire()) {
                        CaseNoire caseDepart = plateau.getCaseSelect();
                        Piece p = plateau.getPieceSelec();
                        // On v rifie que le coup est valide
                        // si oui, on envoie au plateau un "message" pour avertir que le coup est valide
                        Rafle rafle = new Rafle(caseDepart,null,null);
                        rafle.addCasesSuivantes(this);
                        if(p.isCoupValide(rafle)) {
                            plateau.caseSelect(this,null);
                        }
                    }
                    
                }
            }
            
        }
    }
    
    /**
     * Handles the mouseEntered event. If a piece has been selected, it can highlight the square 
     * if a potential move is valid.
     *
     * @param e the MouseEvent object
     * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
     * @see Case#select
     */
    public void mouseEntered(MouseEvent e) {
        //Si le joueur a s lectionn e une piece
        
        if(plateau.getCaseSelect()!=null) {
            
            if(obligatoire && piece == null) {
                select = true; 
                repaint();
                return;
            }
            
            if(plateau.isObligatoire())
                return;
            
            //Si le coup potentiel est valable, on met la case en surbrillance
            Rafle rafle = new Rafle(plateau.getCaseSelect(),null,null);
            rafle.addCasesSuivantes(this);
            
            if(plateau.getPieceSelec().isCoupValide(rafle)) {
                select=true;
                repaint();
            }
            
        }
    }
    
    /**
     * Handles the mouseExited event. If the square was highlighted, it removes the highlight.
     *
     * @param e the MouseEvent object
     * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
     * @see Case#select
     */
    public void mouseExited(MouseEvent e) {
        //Si la case  tait en surbrillance, on la rend "normale"
        if(select) {
            select=false;
            repaint();
        } 
    }
    
    /**
     * Handles the mousePressed event. In this implementation, this method does nothing.
     *
     * @param e the MouseEvent object
     * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
     */
    public void mousePressed(MouseEvent e) {      
    }

    /**
     * Handles the mouseReleased event. In this implementation, this method does nothing.
     *
     * @param e the MouseEvent object
     * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
     */
    public void mouseReleased(MouseEvent e) {
    }
    
    /**
     * Paints the component. Colors the square depending on its current state 
     * (normal, highlighted, or mandatory).
     *
     * @param g the Graphics object
     * @see Case#TAILLE
     * @see javax.swing.JComponent#paintComponent(Graphics)
     */
    public void paintComponent(Graphics g) {
        // Si la case est en surbrillance on dessine un petit carre cyan
        if(select)  {
            g.setColor(Color.CYAN);
            g.fillRect(0,0,Case.TAILLE,Case.TAILLE);
            g.setColor(Color.BLACK);
            g.fillRect(5,5,Case.TAILLE-10,Case.TAILLE-10);
        }
        else {
            //Si la case est obligatoire on dessine un carre rouge
            if(obligatoire) {
                g.setColor(Color.RED);
                g.fillRect(0,0,Case.TAILLE,Case.TAILLE);
                g.setColor(Color.BLACK);
                g.fillRect(5,5,Case.TAILLE-10,Case.TAILLE-10);
            }
            //			 Sinon on dessin un carre  noir
            else {
                g.setColor(Color.BLACK);
                g.fillRect(0,0,Case.TAILLE,Case.TAILLE);
            }
        }
    }

    /**
     * Removes the piece that is currently on the square.
     */
    public void remove() {
        if(piece!=null) {
            remove(piece);
            piece = null;
        }
    }
    
    /**
     * Sets whether the square is mandatory or not. If the state changes, it triggers a repaint.
     *
     * @param b true if the square is mandatory, false otherwise
     * @see Arbitre#calculCoupObligatoire()
     */
    public void setObligatoire(boolean b) {
        obligatoire = b;
        repaint();
    }
    
    /**
     * Sets whether the square is selected or not. If the state changes, it triggers a repaint.
     *
     * @param b true if the square is selected, false otherwise
     * @see CaseNoire#select
     */
    public void setSelect(boolean b) {
        select = b;
        repaint();
    }
}