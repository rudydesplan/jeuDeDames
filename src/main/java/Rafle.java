package jeuDeDames;

import java.util.Vector;

/**
 * The Rafle class represents a sequence of captures in a game of checkers. 
 * This includes details about the starting square, the capture square, 
 * further captures in the sequence, the player executing the sequence,
 * the number of captures and the piece being captured.
 */
public class Rafle {

    private CaseNoire caseDebut;

    private CaseNoire casePrise;

    private Vector<Rafle> casesSuivantes;

    private Joueur joueur = null;

    private int nbPrises;

    private Piece piecePrise;

    /**
     * Constructor of the Rafle class.
     * Initializes a Rafle with a starting square, a capture square, and a player.
     * @param d The starting square
     * @param p The capture square
     * @param j The player executing the Rafle
     */
    public Rafle(CaseNoire d, CaseNoire p,Joueur j) {
        caseDebut = d;
        casePrise = p;
        if(casePrise != null) 
            piecePrise = casePrise.getPiece();
        else piecePrise=null;
        
        nbPrises=0;
        casesSuivantes = new Vector<>();
        joueur = j;
    }

    /**
     * Adds a capture to the sequence.
     * @param c The square to be added to the sequence
     */
    public void addCasesSuivantes(CaseNoire c) {
        casesSuivantes.add(new Rafle(c,null,joueur));
    }

    /**
     * Adds a Rafle to the sequence.
     * @param r The Rafle to be added to the sequence
     */
    public void addRaflesSuivantes(Rafle r) {
        casesSuivantes.add(r);
    }

    /**
     * Clears the sequence of captures.
     */
    public void clearCasesSuivantes() {
        casesSuivantes.clear();
    }
    
    /**
     * Erases moves from the board.
     * @param p The game board
     * @see Plateau#isObligatoire()
     */
    public void effaceCoups(Plateau p) {
        CaseNoire c = null;
        int dirLig;
        int dirCol;
        int dlig;
        int dcol;
        int alig;
        int acol;
        
        p.get(caseDebut).setObligatoire(false);
        if(casePrise != null)
            p.get(casePrise).setObligatoire(false);
        for(int i=0;i<casesSuivantes.size();i++) {
            
            c = (casesSuivantes.elementAt(i)).getCaseDebut();
            dlig = caseDebut.getLigne();
            dcol = caseDebut.getColonne();
            alig = c.getLigne();
            acol = c.getColonne();
            
            dirLig = dlig - alig;
            dirCol = dcol - acol;
            while((dlig != alig) && (dcol != acol) && (dlig >= 0) && (dlig <= 9) && (dcol >= 0) && (dcol <= 9)) {
                dlig += dirLig;
                dcol += dirCol;
                
                // Guard against stepping out of bounds due to the last increment
                if((dlig >= 0) && (dlig <= 9) && (dcol >= 0) && (dcol <= 9)) {
                    p.get(dlig,dcol).setObligatoire(false);
                }
            }
            
            (casesSuivantes.elementAt(i)).effaceCoups(p);
        }
    }
    
    /**
     * Gets the starting square of the Rafle.
     * @return The starting square of the Rafle
     */
    public CaseNoire getCaseDebut() {
        return caseDebut;
    }
    
    /**
     * Gets a square from the sequence.
     * @param i The index of the square in the sequence
     * @return The square at the specified index in the sequence
     */
    public CaseNoire getCasesSuivantes(int i) {
        if(i<casesSuivantes.size())
            return (casesSuivantes.get(i)).getCaseDebut();
        return null;
    }

    /**
     * Gets the player executing the Rafle.
     * @return The player executing the Rafle
     */
    public Joueur getJoueur() {
        return joueur;
    }

    /**
     * Gets the number of squares in the sequence.
     * @return The number of squares in the sequence
     */
    public int getNbCasesSuivantes() {
        return casesSuivantes.size();
    }

    /**
     * Gets the number of captures in the Rafle.
     * @return The number of captures in the Rafle
     */
    public int getNbPrises() {
        return nbPrises;
    }
    
    /**
     * Gets the piece being captured in the Rafle.
     * @return The piece being captured in the Rafle
     */
    public Piece getPiecePrise() {
        return piecePrise;
    }
    
    /**
     * Returns the captured square in the Rafle.
     * @return The captured square in the Rafle
     */
    public CaseNoire getPrise() {
        return casePrise;
    }
    
    /**
     * Returns the Rafle object that starts at the given square.
     * @param c The starting square of the Rafle
     * @return The Rafle that starts at the given square. If no such Rafle exists, returns null.
     */
    public Rafle getRaflesSuivantes(CaseNoire c) {
        for(int i=0;i<casesSuivantes.size();i++) {
            if((casesSuivantes.elementAt(i)).getCaseDebut().compare(c)) {
                return casesSuivantes.elementAt(i); 
            }
        }
        return null;
    }

    /**
     * Returns the Rafle object at the given index.
     * @param i The index of the Rafle
     * @return The Rafle at the given index
     */
    public Rafle getRaflesSuivantes(int i) {
        return casesSuivantes.elementAt(i);
    }

    /**
     * Returns a Vector of the sequence of captures.
     * @return A Vector of Rafle objects representing the sequence of captures
     */
    public Vector<Rafle> getVector() {
        return casesSuivantes;
    }
    
    /**
     * Updates the moves on the game board.
     * @param p The game board
     * @see Plateau#isObligatoire()
     */
    public void misAJourCoups(Plateau p) {
        p.get(caseDebut).setObligatoire(true);
        if(casePrise != null)
            p.get(casePrise).setObligatoire(true);
        for(int i=0;i<casesSuivantes.size();i++) {
            (casesSuivantes.elementAt(i)).misAJourCoups(p);
        }
    }

    /**
     * Sets the starting square of the Rafle.
     * @param c The starting square
     */
    public void setCaseDebut(CaseNoire c) {
        caseDebut = c;
    }

    /**
     * Sets the captured square of the Rafle.
     * If the input is null, the captured piece is also set to null.
     * If not null, the captured piece is updated to the piece at the input square.
     * @param prise The captured square
     */
    public void setCasePrise(CaseNoire prise) {
        if(prise == null)
            piecePrise=null;
        else
            piecePrise = prise.getPiece();
        casePrise = prise;
    }
    
    /**
     * Sets the player executing the Rafle.
     * @param j The player
     */
    public void setJoueur(Joueur j) {
        joueur = j;
    }

    /**
     * Sets the number of captures in the Rafle.
     * @param n The number of captures
     */
    public void setNbPrises(int n) {
        nbPrises = n;
    }

    /**
     * Sets the piece being captured in the Rafle.
     * @param prise The piece being captured
     */
    public void setPiecePrise(Piece prise) {
        piecePrise = prise;
    }

    /**
     * Replaces the Rafle at the given index with the given Rafle.
     * @param i The index of the Rafle to be replaced
     * @param coup The Rafle to replace the existing Rafle at the given index
     */
    public void setRaflesSuivantes(int i,Rafle coup) {
        casesSuivantes.setElementAt(coup,i);
    }
    
    /**
     * Returns a string representation of the Rafle.
     * @return A string representation of the Rafle
     */
    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        if(joueur != null) {
            s.append(joueur.getNom());
        }
        s.append("|").append(caseDebut).append("|").append(casePrise).append("|").append(piecePrise).append("|");
        
        for(int i = 0; i < casesSuivantes.size(); i++) {
            s.append("[").append(casesSuivantes.elementAt(i).toString()).append("]");
        }
        
        return s.append(";").toString();
    }
}