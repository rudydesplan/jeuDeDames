package jeuDeDames;
import java.awt.Color;
import java.awt.Graphics;

/**
 * The Pion class extends the Piece class and represents a pawn in a game of checkers. 
 * This class handles the pawn's movements, captures, and promotions.
 */
public class Pion extends Piece{

    /**
     * Primary constructor for the Pion class. 
     * Establishes the color, position, direction, and associated player of the pawn.
     * @param c Color of the pawn
     * @param pos Position of the pawn
     * @param p The game board
     * @param j The player owning the pawn
     */
    public Pion(int c, CaseNoire pos,Plateau p,Joueur j) {
        super(c,pos,p,j);
        if(c == BLANC) direction = -1;
        else direction =1;
    }
    
    /**
     * Copy constructor for the Pion class. 
     * Creates a new Pion object identical to the provided Pion, but on a new case.
     * @param p The pawn to be copied
     * @param c The new case for the pawn
     */
    public Pion(Pion p, CaseNoire c) {
        super(p,c);
        direction = p.getDirection();
    }
    
    /**
     * Calculates the possible cases for this pawn to move to.
     * @return  the Case that this pawn can move to. If there are no possible moves, returns null.
     */
    @Override
    public Case casePossible() {
        Case c = null;
        Case possibleCase = null;
        
        if ((c = plateau.get(position.getLigne() + direction, position.getColonne() + 1)) != null && c.getPiece() == null) {
            possibleCase = c;
        } else if ((c = plateau.get(position.getLigne() + direction, position.getColonne() - 1)) != null && c.getPiece() == null) {
            possibleCase = c;
        }
        return possibleCase;
    }
    
    /**
     * Creates a copy of this pawn on a new case.
     * @param c The case where the copied pawn will be placed
     * @return  a new Pion object on the provided CaseNoire.
     */
    @Override
    public Piece copie(CaseNoire c) {
        return new Pion(this,c);
    }

    /**
     * Determines whether there is a compulsory capture for this pawn.
     * @param prise The case of the potential capture
     * @return  the Rafle object representing the compulsory capture. If there is no compulsory capture, returns null.
     */
    @Override
    public Rafle coupObligatoire(CaseNoire prise) {

        int lig = position.getLigne();
        int col = position.getColonne();

        Rafle rafle=new Rafle(position,prise,null);
        int nbPrises=0;
        
        // Calcul effectu  pour la case situ  en bas   droite
        nbPrises = coupPossible(lig,col,1,1,rafle,nbPrises);
        
        // Calcul effectu  pour la case situ  en bas   gauche
        nbPrises = coupPossible(lig,col,-1,1,rafle,nbPrises);
        
        //Calcul effectu  pour la case situ  en haut   gauche
        nbPrises = coupPossible(lig,col,1,-1,rafle,nbPrises);
        
        // Calcul effectu  pour la case situ  en haut   droite
        nbPrises = coupPossible(lig,col,-1,-1,rafle,nbPrises);
        
        if(prise!=null) {
            nbPrises++;
        }
        rafle.setNbPrises(nbPrises);
        return rafle;
    }
    
    /**
     * Checks whether this pawn can make a move.
     * @return  true if the pawn can make a move, false otherwise.
     */
    @Override
    public boolean coupPossible() {
        Case c = null;
        return ((c = plateau.get(position.getLigne() + direction, position.getColonne() + 1)) != null && c.getPiece() == null) ||
               ((c = plateau.get(position.getLigne() + direction, position.getColonne() - 1)) != null && c.getPiece() == null);
    }

    /**
     * Checks if there are possible captures for this pawn from a specific position on the game board.
     *
     * @param lig Initial row coordinate.
     * @param col Initial column coordinate.
     * @param dLig Row direction.
     * @param dCol Column direction.
     * @param rafle The Rafle object to track the captures.
     * @param nbPrises The current number of captures.
     * @return The updated number of captures.
     */
    private int coupPossible(int lig, int col,int dLig, int dCol,Rafle rafle, int nbPrises) {     
        Case c = null;
        Case c2 = null;
        Piece piece = null;
        
        if((c = plateau.get(lig+dLig,col+dCol)) != null && 
           (piece = c.getPiece()) != null &&
           piece.getCouleur() != couleur && 
           (c2 = plateau.get(lig+2*dLig,col+2*dCol)) != null && 
           plateau.get(lig+2*dLig,col+2*dCol).getPiece() == null) {
            /*  Prise possible */
            //Suppression du pion pris
            c.remove();
            //Deplacement de la piece
            deplacer((CaseNoire) c2);
            
            Rafle r = coupObligatoire(piece.getPosition());
            if(r.getNbPrises()>nbPrises) {
                nbPrises=r.getNbPrises();
                rafle.clearCasesSuivantes();
                rafle.addRaflesSuivantes(r);
            }
            else {
                if(r.getNbPrises()==nbPrises) {
                    rafle.addRaflesSuivantes(r);  
                }
            }
        }
        return nbPrises;
    }

    /**
     * Returns the direction of this pawn.
     * @return  the integer representing the direction of this pawn.
     * @see Pion#direction
     */
    @Override
    public int getDirection() {
        return direction;
    }
    
    /**
     * Checks if a given move (Rafle) is valid.
     * @param coup The move to be validated.
     * @return true if the move is valid, false otherwise.
     */
    @Override
    public boolean isCoupValide(Rafle coup) {
        CaseNoire depart = coup.getCaseDebut();
        CaseNoire arrivee = coup.getCasesSuivantes(0);
    
        int diff = arrivee.getColonne() - depart.getColonne();
        
        return depart == arrivee || 
               (arrivee.getLigne() == depart.getLigne() + direction && (diff == 1 || diff == -1));
    }
    
    /**
     * Paints this pawn on the game board.
     *
     * @param g The Graphics object for rendering the pawn.
     * @see javax.swing.JComponent#paintComponent(Graphics)
     */
    @Override
    public void paintComponent(Graphics g) {
        int color = couleur*75+150;
        int cote = Case.TAILLE-10;
        g.setColor(new Color(color,color,color));
        g.fillOval(x-cote/2,y-cote/2,cote,cote);	
    }
    
    /**
     * Promotes this pawn if it has reached the end of the game board.
     */
    @Override
    public boolean promotion() {
        boolean bResult =false;
        if (position.getLigne() == (couleur - 1) * (-9)) {
            joueur.promotion(this);
            bResult = true;
        }
        return bResult;
    }
    
    /**
     * Returns a string representation of this pawn.
     * @return The string representation of this pawn.
     */
    @Override
    public String toString() {
        return "P("+couleur+"):" + position.toString();
    } 
}
