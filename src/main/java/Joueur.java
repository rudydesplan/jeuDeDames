package jeuDeDames;

import java.awt.event.ActionEvent;
import java.util.Vector;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Timer;

/**
 * The Joueur class represents a player in the game of checkers (jeu de dames). 
 * This class contains information about the player's name, color, remaining time, and pieces.
 * This is an abstract class which means it can't be instantiated directly. It is meant to be subclassed.
 * @see Humain
 * @see Ordinateur
 */
public abstract class Joueur {
    
    /**
     * Indicates whether the player is active.
     */
    protected boolean actif = false;

    /**
     * The player's adversary.
     */
    protected Joueur adversaire = null;

    /**
     * The color of the player. Use constants defined in Piece for clarity.
     * @see Piece#NOIR
     * @see Piece#BLANC
     */
    protected int couleur;

    /**
     * The name of the player.
     */
    protected String nom;

    /**
     * The panel object associated with the player.
     */
    protected JoueurPanel panel = null;

    /**
     * A list of pieces belonging to the player.
     */
    protected Vector<Piece> pieces = null;

    /**
     * The game board that the player plays on.
     */
    protected Plateau plateau = null;

    /**
     * The remaining time for the player.
     */
    protected int tempsRestant;

    /**
     * The timer used for tracking the player's time.
     */
    protected Timer timer;

    /**
     * An action that is triggered to update the player's remaining time.
     */
    protected Action updateTemps;


    /**
     * Constructor for the Joueur class.
     * Initializes the player with a name, color and the game board.
     * @param n Name of the player.
     * @param c Color of the player.
     * @param p The game board.
     * @see Plateau
     */
    public Joueur(String n,int c,Plateau p) {
        nom = n;
        couleur = c;
        plateau = p;
        pieces = new Vector<>();
        updateTemps = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                updateTempsLabel();
            }
        };
        timer = new Timer(1000,updateTemps);
    }

    /**
     * Method for a player to abandon the game.
     */
    public void abandon() {
        plateau.getArbitre().abandon(this);
    }

    /**
     * Method that handles the opponent's surrender.
     * This method should be implemented in the subclasses.
     */
    public abstract void abandonAdversaire();
    
    /**
     * Method to update the player's name display.
     */
    public void actualiserNom() {
        panel.setActif(actif);
    }
    
    /**
     * Adds a piece to the player's collection.
     * @param p The piece to be added.
     */
    public void addPiece(Piece p) {
        pieces.add(p);
    }
    
    /**
     * Handles the event of a player canceling their move.
     */
    public void annule() {
        if(actif)
            stopTimer();
        else
            adversaire.stopTimer();
        plateau.getArbitre().annulerCoup(this);
    }
    
    /**
     * Abstract method to handle undoing a move.
     * This method should be implemented in the subclasses.
     * @return true if the move was successfully undone, false otherwise
     */
    public abstract boolean annulerCoup();
    
    /**
     * This method initializes the pieces for the player at the start of the game.
     */
    public void debut() {
        for(int i=(couleur*6);i<(couleur*6)+4;i++) {
            for(int j=((i+1)%2);j<10;j+=2) {
                Piece pion = new Pion(couleur,(CaseNoire) plateau.get(i,j),plateau,this);
                pieces.add(pion);
            }        
        }
    }

    /**
     * Returns the active state of the player.
     * @return true if the player is active, false otherwise
     */
    public boolean getActif() {
        return actif;
    }
    
    /**
     * Returns the adversary player.
     * @return the adversary player
     */
    public Joueur getAdversaire() {
        return adversaire;
    }
    
    /**
     * Returns the color of the player.
     * @see Piece#NOIR
     * @see Piece#BLANC
     * @return the color of the player
     */
    public int getCouleur() {
        return couleur;
    }
    
    /**
     * Returns the name of the player.
     * @return the name of the player
     */
    public String getNom() {
        return nom;
    }
    
    /**
     * Returns the number of pieces that the player has.
     * @return the number of pieces that the player has
     */
    public int getNombrePieces() {
        return pieces.size();
    }

    /**
     * Returns the player's panel.
     * @return the player's panel
     */
    public JoueurPanel getPanel() {
        return panel;
    }

    /**
     * Returns a specific piece from the player's collection.
     * @param i Index of the piece to be returned.
     * @return a specific piece from the player's collection
     */
    public Piece getPiece(int i) {
        return pieces.elementAt(i);
    }

    /**
     * Returns the remaining time for the player.
     * @return the remaining time for the player
     */
    public int getTempsRestant() {
        return tempsRestant;
    }

    /**
     * Hides the player's panel.
     */
    public void hidePanel() {
        panel.setVisible(false);
    }

    /**
     * Abstract method to handle the player's turn.
     * This method should be implemented in the subclasses.
     * @return true if the player's turn was successfully handled, false otherwise
     */
    public abstract boolean jouer();

    /**
     * Handles a draw condition.
     */
    public void nul() {
        if(actif)
            stopTimer();
        else
            adversaire.stopTimer();
        plateau.getArbitre().nul(this);
    }
    
    /**
     * Abstract method to handle a draw condition proposed by the adversary.
     * This method should be implemented in the subclasses.
     * @return true if the draw condition was successfully handled, false otherwise
     */
    public abstract boolean nulAdversaire();
    
    /**
     * Abstract method to handle a refusal of a draw.
     * This method should be implemented in the subclasses.
     */
    public abstract void nulRefuse();
    
    /**
     * Promotes a piece to a "Dame" .
     * @param p The piece (a pawn) to be promoted.
     */
    public void promotion(Pion p) {
        pieces.remove(p);
        p.getPosition().remove();
        Dame d = new Dame(couleur,p.getPosition(),plateau,this);
        d.deplacer(p.getPosition());
        pieces.add(d);
    }

    /**
     * Depromotes a "Dame" (Queen) back to a piece (pawn).
     * It removes the Queen from the collection of pieces, removes it from its position, 
     * then creates a new pawn in its place, moves it to the Queen's position, 
     * and adds it to the collection of pieces.
     * @param d the Queen to be depromoted
     */
    public void depromotion(Dame d) {
        pieces.remove(d);
        d.getPosition().remove();
        Pion p = new Pion(couleur, d.getPosition(), plateau, this);
        p.deplacer(d.getPosition());
        pieces.add(p);
    }

    /**
     * Removes a piece from the player's collection.
     * @param p The piece to be removed.
     */
    public void removePiece(Piece p) {
        pieces.remove(p);
        actualiserNom();
    }

    /**
     * Sets the active state of the player.
     * @param b The active state to be set.
     * @see Joueur#actif
     */
    public void setActif(boolean b) {
        actif = b;
        actualiserNom();
    }

    /**
     * Sets the adversary player.
     * @param j The adversary player.
     */
    public void setAdversaire(Joueur j) {
        adversaire = j;
    }

    /**
     * Sets the player's panel.
     * @param p The player's panel.
     */
    public void setPanel(JoueurPanel p) {
        panel = p;
    }

    /**
     * Sets the remaining time for the player.
     * @param temps The remaining time.
     */
    public void setTemps(int temps) {
        tempsRestant = temps;
        setTimer(temps);      
    }

    /**
     * Sets the timer display on the player's panel.
     * @param t The timer display value.
     */
    public void setTimer(int t) {
        panel.setTimer(t);
    }

    /**
     * Starts the player's timer.
     */
    public void startTimer() {
        if(tempsRestant > 0)
            timer.start();
    }
    
    /**
     * Stops the player's timer.
     * @return the remaining time for the player after stopping the timer
     */
    public int stopTimer() {
        timer.stop();
        return tempsRestant;
    }

    /**
     * Abstract method to return a string representation of the player.
     * This method should be implemented in the subclasses.
     */
    public abstract String toString();

    /**
     * Updates the display of the remaining time for the player.
     */
    public void updateTempsLabel() {
        tempsRestant--;
        panel.setTimer(tempsRestant);
        if(tempsRestant<=0) 
            plateau.getArbitre().finTemps();
    }
}