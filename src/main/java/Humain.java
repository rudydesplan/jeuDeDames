package jeuDeDames;

import javax.swing.JOptionPane;

/**
 * This class extends the abstract class Joueur and represents a human player in the game of checkers.
 * It overrides several abstract methods from the Joueur class, implementing functionality specific to a human player.
 * @see Joueur
 */
public class Humain extends Joueur {

    /**
     * Constructor for the Humain class.
     * Initializes the player with a name, color and the game board.
     * @param n Name of the player.
     * @param c Color of the player.
     * @param p The game board.
     */
    public Humain(String n, int c, Plateau p) {
        super(n,c,p);
    }

    /**
     * Displays a message notifying the human player of their victory when the opponent abandons the game.
     */
    @Override
    public void abandonAdversaire() {
        JOptionPane.showMessageDialog(null, adversaire.getNom() +" a abandonne. Vous avez gagne !", "Jeu De Dames", JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Asks the human player if they accept the adversary's proposal to cancel the last move.
     * @return boolean indicating the acceptance or refusal of the proposal.
     */
    @Override
    public boolean annulerCoup() {
        switch(JOptionPane.showConfirmDialog(null, adversaire.getNom() + " veut annuler son dernier coup. Acceptez-vous ?","Jeu De Dames",JOptionPane.YES_NO_OPTION)) {
        case JOptionPane.YES_OPTION:
            return true;
        case JOptionPane.NO_OPTION:
            return false;
        default:
            return false;
        }  
    }

    /**
     * Checks if a valid move is possible for the human player.
     * @return boolean indicating if a move is possible.
     */
    public boolean coupPossible() {
        if(plateau.isObligatoire())
            return true;
        
        for(int i=0; i<pieces.size(); i++) {
            Piece p = pieces.elementAt(i);
            if(p.coupPossible())
                return true;
        }
        return false;
    }
    
    /**
     * Sets the active status for the human player and begins their turn if a move is possible.
     * @return boolean indicating if a move is possible and thus, the turn could start.
     */
    @Override
    public boolean jouer() {
        setActif(true);
        adversaire.setActif(false);
        if(!coupPossible()) 
            return false;        
        startTimer();
        return true;
    }
    
    /**
     * Asks the human player if they accept the adversary's proposal for a draw.
     * @return boolean indicating the acceptance or refusal of the proposal.
     */
    @Override
    public boolean nulAdversaire() {
        switch(JOptionPane.showConfirmDialog(null, adversaire.getNom() + " vous propose une partie nulle. Acceptez-vous ?","Jeu De Dames",JOptionPane.YES_NO_OPTION)) {
        case JOptionPane.YES_OPTION:
            return true;
        case JOptionPane.NO_OPTION:
            return false;
        default:
            return false;
        }
    }
    
    /**
     * Displays a message notifying the human player that the opponent refused their draw proposal and restarts the timer.
     */
    @Override
    public void nulRefuse() {
        JOptionPane.showMessageDialog(null,adversaire.getNom() + " a refuse votre proposition de partie nulle.", "Jeu De Dames", JOptionPane.INFORMATION_MESSAGE);
        if(actif)
            startTimer();
        else
            adversaire.startTimer();
    }
    
    /**
     * Returns a string representation of the human player.
     * @return A string that includes the type of player (Human) and their name.
     */
    @Override
    public String toString() {
        return "Humain:"+ nom;
    }
}