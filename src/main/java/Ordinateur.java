package jeuDeDames;
import java.util.Vector;

/**
 * The Ordinateur class represents a computer-controlled player in the game of checkers (jeu de dames).
 * This class provides concrete implementations for the abstract methods in the superclass Joueur.
 * @see Joueur
 */
public class Ordinateur extends Joueur {
    
    /**
     * Constructor for the Ordinateur class.
     * Initializes the computer-controlled player with a name, color, and the game board.
     * @param n String: The name of the player.
     * @param c int: The color of the player.
     * @param p Plateau: The game board.
     */
    public Ordinateur(String n,int c,Plateau p) {
        super(n,c,p);
    }

    /**
     * Method that handles the opponent's surrender.
     * As this is a computer player, no action is performed.
     */
    @Override
    public void abandonAdversaire() {
    }
    
    /**
     * Method to handle undoing a move.
     * The computer player always accepts the undo request.
     * @return boolean: Always returns true.
     */
    @Override
    public boolean annulerCoup() {
        return true;
    }
    
    /**
     * Method to handle the player's turn.
     * The computer player makes a move following the game rules.
     * @return boolean: Returns true if the computer player can make a move, false otherwise.
     */
    @Override
    public boolean jouer() {
        startTimer();
        setActif(true);
        adversaire.setActif(false);
        Arbitre arbitre = plateau.getArbitre();
        if(plateau.isObligatoire()) {
            Vector<Rafle> coup = arbitre.getCoupObligatoire();
            
            Rafle r1 = (coup.elementAt(0) );
            Rafle r2 = r1.getRaflesSuivantes(0);
            
            Rafle c = new Rafle((CaseNoire) plateau.get(r1.getCaseDebut()),(CaseNoire)plateau.get(r2.getPrise()),this);
            c.addCasesSuivantes((CaseNoire)plateau.get(r2.getCaseDebut()));
            
            // Ajoute la piece deplace sur se nouvelle case.
            plateau.get(r1.getCaseDebut()).getPiece().deplacer((CaseNoire) plateau.get(r2.getCaseDebut()));
            
            // On avertie l'arbitre qu'un coup est joue.
            arbitre.finCoup(c,r2.getNbPrises());
            return true;
        }
        
        for(int i=0; i<pieces.size(); i++) {
            Piece p = pieces.elementAt(i);
            Case c = null;
            if( (c=p.casePossible())!=null) {
                Rafle r = new Rafle(p.getPosition(),null,this);
                r.addCasesSuivantes((CaseNoire) c);
                p.deplacer((CaseNoire) c);
                arbitre.finCoup(r,0);
                return true;
            }
        }
        return false;
    }
    
    /**
     * Method to handle a draw condition proposed by the adversary.
     * The computer player always accepts the draw proposal.
     * @return boolean: Always returns true.
     */
    @Override
    public boolean nulAdversaire() {
        return true;
    }

    /**
     * Method to handle a refusal of a draw.
     * As this is a computer player, no action is performed.
     */
    @Override
    public void nulRefuse() {
    }
    
    /**
     * Sets the player's panel and hides the panel's button.
     * @param p JoueurPanel: The panel to be set for the player.
     */
    @Override
    public void setPanel(JoueurPanel p) {
        super.setPanel(p);
        panel.hideButton();
    }
    
    /**
     * Returns a string representation of the computer player.
     * @return String: A string representing the computer player.
     */
    @Override
    public String toString() {
        return "Ordinateur:"+ nom;
    }
}
