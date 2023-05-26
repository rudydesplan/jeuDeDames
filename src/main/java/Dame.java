package jeuDeDames;

import java.awt.Color;
import java.awt.Graphics;

/**
 * Class Dame extends Piece to represent a Queen in a game.
 */
public class Dame extends Piece {

    /**
     * Constructor that takes a Queen and a black case as parameters.
     *
     * @param d the Queen object
     * @param c the black case
     */
    public Dame(Dame d,CaseNoire c) {
        super(d,c);
    }

    /**
     * Constructor that initializes the queen with its color, position, plateau, player, and direction.
     *
     * @param c the color of the queen
     * @param pos the position of the queen
     * @param p the plateau of the game
     * @param j the player
     */
    public Dame(int c,CaseNoire pos,Plateau p,Joueur j) {
        super(c,pos,p,j);
        direction = 0;
    }

    /**
     * Overrides the casePossible method from Piece. 
     * Checks possible case in all four directions (up, down, left, right).
     *
     * @return the first possible case found or null if no case is possible
     */
    @Override
    public Case casePossible() {
        Case c = null;
        int[] directions = {1, -1};
    
        for (int d1 : directions) {
            for (int d2 : directions) {
                c = casePossibleDirection(d1, d2);
                if (c != null) {
                    return c;
                }
            }
        }
        return null;
    }

    /**
     * Private helper method for checking possible case in a given direction.
     *
     * @param dLig change in line (direction)
     * @param dCol change in column (direction)
     * @return the case if it is possible or null otherwise
     */
    private Case casePossibleDirection(int dLig, int dCol) {
        int ligne = position.getLigne();
        int colonne = position.getColonne();    
        while(true) {
            ligne += dLig;
            colonne += dCol;   
            if(ligne < 0 || ligne > 9 || colonne < 0 || colonne > 9 
               || plateau.get(ligne, colonne).getPiece() != null) {
                return null;
            }
            return plateau.get(ligne, colonne);
        }
    }

    /**
     * Overrides the copie method from Piece. 
     * Makes a copy of this queen at the given black case.
     *
     * @param c the black case
     * @return the new Queen
     */
    @Override
    public Piece copie(CaseNoire c) {
        return new Dame(this,c);
    }

    /**
     * Overrides the coupObligatoire method from Piece.
     * Performs a forced capture, and returns a Rafle object.
     *
     * @param prise the case where the capture will occur
     * @return a Rafle object describing the forced capture
     */
    @Override
    public Rafle coupObligatoire(CaseNoire prise) {
        
        Rafle rafle = new Rafle(position,prise,null);
        int nbPrises = 0;
        
        int ligne = position.getLigne();
        int colonne = position.getColonne();
        
        int iLigne = ligne;
        int iColonne = colonne;
        
        int dLig;
        int dCol;
        
        // On recherche dans les 4 directions une prise possible.
        // Ceci correspond au premier coup de la rafle
        if(prise == null) {
            
            // Direction 1,1
            //nbPrises = rechercherPremierePrise(iLigne,iColonne,1,1,rafle,nbPrises);
            dLig = 1;
            dCol = 1;
            boolean isBreak = false;
            while(!isBreak) {
                Piece p = null;
                
                ligne += dLig;
                colonne += dCol;
                
                if(ligne < 1 || ligne > 8 || colonne < 1 || colonne > 8)
                    isBreak = true;
                else {
                    if( ( p = plateau.get(ligne, colonne).getPiece() )!= null) {
                        if(p.getCouleur() != couleur) {
                            if(plateau.get(ligne+dLig,colonne+dCol).getPiece() == null) {
                                // Prise possible
                                deplacer( (CaseNoire) plateau.get(ligne-dLig, colonne-dCol) );
                                Rafle r = coupObligatoire(p.getPosition());
                                r.setCaseDebut( (CaseNoire) plateau.get(iLigne, iColonne));
                                r.setCasePrise(null);
                                // La rafle trouvee est "meilleure"
                                if(r.getNbPrises() > nbPrises) {
                                    rafle = r;
                                    nbPrises = r.getNbPrises();
                                }
                                else {
                                    // la rafle trouvee est equivalente
                                    if(nbPrises > 0 && r.getNbPrises() == nbPrises) {
                                        for(int i = 0; i<r.getNbCasesSuivantes();i++) {
                                            rafle.addRaflesSuivantes(r.getRaflesSuivantes(i));
                                        }
                                    }
                                }
                                
                            }
                        }
                        isBreak = true;
                    }
                }
            }
            
            // Direction -1,1
            //nbPrises = rechercherPremierePrise(iLigne,iColonne,-1,1,rafle,nbPrises);
            dLig = -1;
            dCol = 1;
            ligne = iLigne;
            colonne = iColonne;
            isBreak = false;
            while(!isBreak) {
                Piece p = null;
                
                ligne += dLig;
                colonne += dCol;
                
                if(ligne < 1 || ligne > 8 || colonne < 1 || colonne > 8)
                    isBreak = true;
                else {
                    if( ( p = plateau.get(ligne, colonne).getPiece() )!= null) {
                        if(p.getCouleur() != couleur) {
                            if(plateau.get(ligne+dLig,colonne+dCol).getPiece() == null) {
                                // Prise possible
                                deplacer( (CaseNoire) plateau.get(ligne-dLig,colonne-dCol) );
                                Rafle r = coupObligatoire(p.getPosition());
                                r.setCaseDebut( (CaseNoire) plateau.get(iLigne,iColonne));
                                r.setCasePrise(null);
                                // La rafle trouvee est "meilleure"
                                if(r.getNbPrises() > nbPrises) {
                                    rafle = r;
                                    nbPrises = r.getNbPrises();
                                }
                                else {
                                    // la rafle trouvee est equivalente
                                    if(nbPrises > 0 && r.getNbPrises() == nbPrises) {
                                        for(int i = 0; i<r.getNbCasesSuivantes();i++) {
                                            rafle.addRaflesSuivantes(r.getRaflesSuivantes(i));
                                        }
                                    }
                                }
                            }
                        }
                        isBreak = true;
                    }
                }
            }
            
            // Direction -1,-1
            dLig = -1;
            dCol = -1;
            ligne = iLigne;
            colonne = iColonne;
            isBreak = false;
            while(!isBreak) {
                Piece p = null;
                
                ligne += dLig;
                colonne += dCol;
                
                if(ligne < 1 || ligne > 8 || colonne < 1 || colonne > 8)
                    isBreak = true;
                else {
                    if( ( p = plateau.get(ligne, colonne).getPiece() )!= null) {
                        if(p.getCouleur() != couleur) {
                            if(plateau.get(ligne+dLig,colonne+dCol).getPiece() == null) {
                                // Prise possible
                                deplacer( (CaseNoire) plateau.get(ligne-dLig, colonne-dCol) );
                                Rafle r = coupObligatoire(p.getPosition());
                                r.setCaseDebut( (CaseNoire) plateau.get(iLigne, iColonne));
                                r.setCasePrise(null);
                                // La rafle trouvee est "meilleure"
                                if(r.getNbPrises() > nbPrises) {
                                    rafle = r;
                                    nbPrises = r.getNbPrises();
                                }
                                else {
                                    // la rafle trouvee est equivalente
                                    if(nbPrises > 0 && r.getNbPrises() == nbPrises) {
                                        for(int i = 0; i<r.getNbCasesSuivantes();i++) {
                                            rafle.addRaflesSuivantes(r.getRaflesSuivantes(i));
                                        }
                                    }
                                }
                            }
                        }
                        isBreak = true;
                    }
                }
            }

            //nbPrises = rechercherPremierePrise(iLigne,iColonne,1,-1,rafle,nbPrises);
            // Direction 1,-1
            dLig = 1;
            dCol = -1;
            isBreak = false;
            while(!isBreak) {
                Piece p = null;
                            
                ligne += dLig;
                colonne += dCol;
                            
                if(ligne < 1 || ligne > 8 || colonne < 1 || colonne > 8)
                    isBreak = true;
                else {
                    if( ( p = plateau.get(ligne, colonne).getPiece() )!= null) {
                        if(p.getCouleur() != couleur) {
                            if(plateau.get(ligne+dLig,colonne+dCol).getPiece() == null) {
                                // Prise possible
                                deplacer( (CaseNoire) plateau.get(ligne-dLig, colonne-dCol) );
                                Rafle r = coupObligatoire(p.getPosition());
                                r.setCaseDebut(prise);
                                r.setCasePrise( (CaseNoire) plateau.get(ligne, colonne) );
                                if(r.getNbPrises() > nbPrises) {
                                    rafle = r;
                                    nbPrises = r.getNbPrises();
                                }
                                else {
                                    if(nbPrises > 0 && r.getNbPrises() == nbPrises) {
                                        for(int i = 0; i<r.getNbCasesSuivantes();i++) {
                                            rafle.addRaflesSuivantes(r.getRaflesSuivantes(i));
                                        }
                                    }
                                }
                            }
                        }
                        isBreak = true;
                    }
                }
            }
        }
        
        // On connait la premiere prise a effectue
        else {
            dLig = prise.getLigne() - position.getLigne();
            dCol = prise.getColonne() - position.getColonne();
            
            ligne += dLig;
            colonne += dCol;

            // Define a new boolean variable "continueLoop"
            boolean continueLoop = true;
            
            while(continueLoop) {
                Piece p = null;
                Case c = null;
                Case c2 = null;
                
                ligne += dLig;
                colonne += dCol;
                
                // Replace "break" with the condition to stop the loop
                if(ligne<0 || ligne > 9 || colonne <0 || colonne > 9)
                    continueLoop = false;
                else {
                    deplacer( (CaseNoire) plateau.get(ligne,colonne));
                    
                    // Piece prenable sur le cote
                    nbPrises = prisePossible(ligne,colonne,dLig,-dCol,rafle,nbPrises,prise);
                    
                    // Piece prenable sur l'autre cote
                    deplacer( (CaseNoire) plateau.get(ligne,colonne));
                    nbPrises = prisePossible(ligne,colonne,-dLig,dCol,rafle,nbPrises,prise);
                    
                    
                    // Piece prenable dans la direction ou on avance
                    if((c=plateau.get(ligne+dLig,colonne+dCol)) != null) {
                        if((p = c.getPiece())!= null && p.getCouleur() != couleur) {
                            if((c2 = plateau.get(ligne+2*dLig,colonne+2*dCol)) != null && c2.getPiece() == null) {
                                Rafle r = coupObligatoire((CaseNoire) c);
                                r.setCasePrise(prise);
                                if(r.getNbPrises() > nbPrises) {
                                    nbPrises = r.getNbPrises();
                                    rafle.clearCasesSuivantes();
                                    rafle.addRaflesSuivantes(r);
                                }
                                else {
                                    if(nbPrises>0 && nbPrises == r.getNbPrises()) {
                                        // On doit verifier s'il n'y a pas une rafle equivalente qui commence par la meme case
                                        Rafle r1 = rafle.getRaflesSuivantes(r.getCaseDebut());
                                        if(r1==null)
                                            // Si il n'y a pas on ajoute simplement la nouvelle rafle trouvee
                                            rafle.addRaflesSuivantes(r);
                                        else {
                                            //dans le cas ou une rafle commencant par la meme case existe deja
                                            // on ajoute a cette raflen, les nouvelles rafles suivantes trouves.
                                            for(int i=0;i<r.getNbCasesSuivantes();i++)
                                                r1.addRaflesSuivantes(r.getRaflesSuivantes(i));
                                        }
                                    }
                                }
                            }
                        }
                        // Replace the last "break" statement with the condition to stop the loop
                        if(p != null && p.getCouleur() != couleur) {
                            continueLoop = false;
                        }
                    }
                }
            }
            //Si il n'y a pas de prises possibles dans toutes la diagonales, on ajoute
            // aux cases suivantes, toutes les cases de la diagonale.
            if(nbPrises == 0) {
                position.remove();
                iLigne += 2*dLig;
                iColonne += 2*dCol;
                
                // Define a new boolean variable "continueLoop"
                boolean continueLoop_ = true;
                
                while(continueLoop_) {
                    Rafle r = new Rafle( (CaseNoire) plateau.get(iLigne,iColonne),null,null);
                    r.setCasePrise(prise);
                    r.setNbPrises(1);
                    rafle.addRaflesSuivantes(r);
                    
                    iLigne += dLig;
                    iColonne += dCol;
                    
                    // Replace "break" with the condition to stop the loop
                    if(iLigne <0 || iLigne > 9 || iColonne <0 || iColonne >9 || plateau.get(iLigne,iColonne).getPiece()!=null)
                        continueLoop_ = false;
                }
                nbPrises=1;
            }
            
        }
        
        if(prise != null) 
            nbPrises++;
        //On doit diminuer de un le nombre de prises totales, car dans l'algorithme, on en ajoute un en plus.
        else nbPrises--;
        
        rafle.setNbPrises(nbPrises);
        return rafle;
    }

    /**
     * This method checks if a move is possible in a given direction.
     * It iterates over all possible directions to see if any moves are possible.
     * @return true if a move is possible in any direction, false otherwise
     */
    @Override
    public boolean coupPossible() {
        int[] directions = {1, -1};
        for (int dLig : directions) {
            for (int dCol : directions) {
                if (coupPossibleDirection(dLig, dCol)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Checks if a move is possible in a certain direction.
     *
     * @param dLig change in line (direction)
     * @param dCol change in column (direction)
     * @return true if move is possible, false otherwise
     */
    public boolean coupPossibleDirection(int dLig, int dCol) {
        int ligne = position.getLigne();
        int colonne = position.getColonne();
        
        ligne += dLig;
        colonne += dCol;
        
        if(ligne<0 || ligne >9 || colonne<0 || colonne >9)
            return false;
        
        return plateau.get(ligne, colonne).getPiece() == null;
    }
    
    /**
     * This method validates the provided move.
     * It compares the starting and ending positions and checks if the path is clear of obstacles.
     * @param coup the move to validate
     * @return true if the move is valid, false otherwise
     */
    @Override
    public boolean isCoupValide(Rafle coup) {
        CaseNoire depart = coup.getCaseDebut();
        CaseNoire arrivee = coup.getCasesSuivantes(0);
        
        // Les deux cases sont identiques, c bon
        if(depart == arrivee)
            return true;
        
        int dlig = depart.getLigne();
        int dcol = depart.getColonne();
        
        int alig = arrivee.getLigne();
        int acol = arrivee.getColonne();
        
        int dirCol = (depart.getColonne() > arrivee.getColonne()) ? -1 : 1;
        int dirLig = (depart.getLigne() > arrivee.getLigne()) ? -1 : 1;
    
        while( (dlig != alig) || (dcol != acol) ) {
            dlig += dirLig;
            dcol += dirCol;
            
            if( dlig < 0 || dlig > 9 || dcol <0 || dcol > 9 || plateau.get(dlig,dcol).getPiece()!=null) 
                return false;
        }
        
        return true;
    }
    
    /**
     * Overrides the paintComponent method of the parent class. 
     * It sets the color and fills the oval based on the color and size parameters.
     * @param g the graphics context to use for painting
     */
    @Override
    public void paintComponent(Graphics g) {
        int color = couleur*75+150;
        int cote = Case.TAILLE-10;
        g.setColor(new Color(color,color,color));
        g.fillOval(x-cote/2,y-cote/2,cote,cote);	
        
        g.setColor(Color.RED);
        g.fillOval(cote/2-5,cote/2-5,10,10);  
    }

    /**
     * This method checks if a capture is possible from a given position in a given direction.
     * It verifies if the piece on the given square is of the opposite color and if the next square in the same direction is empty.
     * @param ligne the row of the current position
     * @param colonne the column of the current position
     * @param dLig the row direction
     * @param dCol the column direction
     * @param rafle the move to check
     * @param nbPrises the number of captures made
     * @param prise the square of the captured piece
     * @return the updated number of captures made
     */
    private int prisePossible(int ligne, int colonne, int dLig, int dCol,Rafle rafle, int nbPrises,CaseNoire prise) {
        Piece p;
        Case c;
        Case c2;
        if((c = plateau.get(ligne+dLig,colonne+dCol)) != null && 
            (p = c.getPiece())!= null && 
            p.getCouleur() != couleur &&
            (c2 = plateau.get(ligne+2*dLig,colonne+2*dCol)) != null && 
            c2.getPiece() == null) {
                
            Rafle r = coupObligatoire( (CaseNoire) c);
            r.setCasePrise(prise);
            if(r.getNbPrises() > nbPrises) {
                nbPrises = r.getNbPrises();
                rafle.clearCasesSuivantes();
                rafle.addRaflesSuivantes(r);
                return nbPrises;
            }
            if(nbPrises>0 && nbPrises == r.getNbPrises()) {
                Rafle r1 = rafle.getRaflesSuivantes(r.getCaseDebut());
                if(r1==null)
                    rafle.addRaflesSuivantes(r);
                else {
                    for(int i=0;i<r.getNbCasesSuivantes();i++)
                        r1.addRaflesSuivantes(r.getRaflesSuivantes(i));
                }
                return nbPrises;
            }
        }
        return nbPrises;
    }

    /**
     * Overrides the promotion method of the parent class.
     * It always returns false because Queens (Dames in French) cannot be promoted.
     * @return false, as Queens cannot be promoted
     */
    @Override
    public boolean promotion() {
        return false;
    }
    
    /**
     * Overrides the toString method of the parent class. 
     * It returns a string that represents the piece.
     * @return a string that represents the piece
     */
    @Override
    public String toString() {
        return "D("+couleur+"):" + position.toString();
    }
}