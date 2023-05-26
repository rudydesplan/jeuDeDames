package jeuDeDames;

import java.util.Vector;

/**
 * The HistoriqueCoup class is part of the jeuDeDames package. 
 * This class is responsible for managing the historical moves 
 * (represented by instances of the Rafle class) in a game of checkers (jeu de dames).
 */
public class HistoriqueCoup {
    
    private Vector<Rafle> historique = null;
    
    /**
     * Constructor for the HistoriqueCoup class.
     * Initializes an empty list for storing the history of moves.
     */
    public HistoriqueCoup() {
        historique = new Vector<>();
    }
    
    /**
     * Adds a move to the history.
     *
     * @param r The move to be added.
     */
    public void addCoup(Rafle r) {
        historique.add(r);
    }
    
    /**
     * Clears the history of moves.
     */
    public void clear() {
        historique.clear();
    }
    
    /**
     * Returns the move at the specified index in the history.
     *
     * @param i The index of the move to be returned.
     * @return The move at the specified index or null if the index is out of bounds.
     */
    public Rafle get(int i) {
        if(i >=0 && i < historique.size())
            return historique.get(i);
        return null;
    }
    
    /**
     * Returns the index of the last move made by the specified player.
     *
     * @param j The player whose last move is to be returned.
     * @return The index of the last move made by the player or -1 if no move has been made.
     */
    public int getDernierCoup(Joueur j) {
        int nbCoup = historique.size();
        if( nbCoup>0 && (historique.get(nbCoup-1)).getJoueur() == j) {
            return nbCoup-1;
        }
        if(nbCoup < 1) 
            return -1;
        return nbCoup-2;
    }
    
    /**
     * Returns the total number of moves in the history.
     *
     * @return The total number of moves.
     */
    public int getNbCoups() {
        return historique.size();
    }
    
    /**
     * Removes the move at the specified index from the history.
     *
     * @param i The index of the move to be removed.
     * @return The removed move.
     */
    public Rafle remove(int i) {
        return historique.remove(i);
    }
    
    /**
     * Returns a string representation of the history of moves.
     *
     * @return A string representation of the history of moves.
     * @see Rafle#toString()
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < historique.size(); i++) {
            sb.append(historique.elementAt(i).toString()).append("\n");
        }
        return sb.toString();
    }
}