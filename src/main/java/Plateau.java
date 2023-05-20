package jeuDeDames;

import java.awt.Dimension;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

/**
 * The Plateau class represents the game board for a game of checkers. It extends the JComponent class 
 * from Swing and is used as a graphical component in the game's user interface.
 */
public class Plateau extends JComponent {
    
    private static int hauteur = 20;

    private static int largeur = 20;

    private boolean actif;

    private transient Arbitre arbitre;	

    private CaseNoire caseSelectionnee;

    private boolean obligatoire;

    private Piece pieceSelec;

    private Case[][] plateau;

    /**
     * Default constructor for the Plateau class. It initializes the board with white and black squares.
     */
    public Plateau() {
        super();
        this.setPreferredSize(new Dimension(11*Case.TAILLE,11*Case.TAILLE));
        plateau = new Case[10][10];
        caseSelectionnee = null;
        obligatoire = false;
        /* Initialisation des cases blanches */
        for(int i=0;i<10;i++) {
            JLabel ligne = new JLabel(Integer.toString(i+1));
            ligne.setBounds(0,hauteur+i*Case.TAILLE,largeur,Case.TAILLE);
            ligne.setHorizontalAlignment(SwingConstants.CENTER);
            add(ligne);
            for(int j=(i%2);j<10;j+=2) {
                JLabel colonne = new JLabel(Character.toString((char) ('A' + j)));
                colonne.setBounds(j*Case.TAILLE+largeur,0,Case.TAILLE,hauteur);
                colonne.setHorizontalAlignment(SwingConstants.CENTER);
                add(colonne);
                plateau[i][j] = new CaseBlanche(i,j,this);
                get(i,j).setBounds(largeur+j*Case.TAILLE,hauteur + i*Case.TAILLE,Case.TAILLE,Case.TAILLE);
                add(get(i,j));
            }
        }
        
        /* Initialisation des cases noires */
        for(int i=0;i<10;i++) {
            for(int j=((i+1)%2);j<10;j+=2) {
                plateau[i][j] = new CaseNoire(i,j,this);
                get(i,j).setBounds(largeur+j*Case.TAILLE,hauteur + i*Case.TAILLE,Case.TAILLE,Case.TAILLE);
                add(get(i,j));
            }
        }
    }

    /**
     * Copy constructor for the Plateau class. It copies the given Plateau object.
     * @param p Plateau: The Plateau object to be copied.
     */
    public Plateau(Plateau p) {
        super();
        plateau = new Case[10][10];
        caseSelectionnee = null;
        obligatoire = p.isObligatoire();
        /* Copie des Cases Blanches */
        for(int i=0;i<10;i++) {
            for(int j=0;j<10;j++) {
                plateau[i][j] = p.get(i,j).copie(this);
            }
        }  
    }
    
    /**
     * Method to handle the selection of a case on the game board.
     * @param c CaseNoire: The case selected by the player.
     * @param p Piece: The piece in the selected case.
     */
    public void caseSelect(CaseNoire c,Piece p) {
        if(!actif)
            return;
        //Couleur des pieces du joueur actif
        int couleur = arbitre.getJoueurActif().getCouleur();
        
        // Il n'y a pas de case selectionn�e
        if(caseSelectionnee==null) {
            // Existe un coup obligatoire
            if(p.getCouleur() == couleur) {
                // Selectionne la case
                caseSelectionnee = c;
                c.setSelect(true);
                // On met a jour les coups obligatoire.
                // On ne met en surbrillance que les cases pouvant �tre atteinte
                if(obligatoire)
                    arbitre.misAJourCoupsObligatoire(c);
                pieceSelec = p;
                
            }
        }
        
        // Il y a une case selectionnee
        else{
            //Deselection case.
            if(c == caseSelectionnee) {
                pieceSelec = null;
                //On redessine tous les coups obligatoires
                if(obligatoire)
                    arbitre.dessineCoupObligatoire();
                initialise();
            }
            else {
                // Coup Obligatoire
                if(obligatoire) {
                    Rafle r1;
                    Rafle r2;
                    // On recupere la rafle passant par la case selectionn� par le joueur.
                    r1 = arbitre.getRafle(caseSelectionnee);
                    
                    if(r1!=null) {
                        // On recupere la rafle passant par la case c.
                        r2 = r1.getRaflesSuivantes(c);
                        r1.toString();
                        if( r2 != null) {
                            // Recupere la case prise
                            
                            Case prise = get(r2.getPrise().getLigne(),r2.getPrise().getColonne());
                            
                            // On initialise un coup
                            Rafle coup = new Rafle(caseSelectionnee,(CaseNoire) prise,arbitre.getJoueurActif());
                            coup.addCasesSuivantes(c);
                            
                            // Ajoute la piece deplac� sur se nouvelle case.
                            pieceSelec.deplacer(c);
                            initialise();
                            // On avertie l'arbitre qu'un coup est jou�.
                            arbitre.finCoup(coup,r2.getNbPrises());
                        }
                    }
                }
                // Pas de coup obligatoire.
                else {
                    Rafle coup = new Rafle(caseSelectionnee,null,arbitre.getJoueurActif());
                    coup.addCasesSuivantes(c);
                    pieceSelec.deplacer(c);
                    initialise();
                    arbitre.finCoup(coup,0);
                }
            }
        }
        repaint(); 
    }

    /**
     * Method to get the case for the given Case object.
     * @param c Case: The Case object to be looked up.
     * @return Case: The Case object on the game board.
     */
    public Case get(Case c) {
        if(c!=null) {
            return plateau[c.getLigne()][c.getColonne()];
        }
        return null;
    }

    /**
     * Method to get the case at the specified coordinates.
     * @param l int: The row index.
     * @param c int: The column index.
     * @return Case: The case at the specified coordinates.
     */
    public Case get(int l, int c) {
        //Arguments sont valides
        if(l>=0 && l<10 && c>=0 && c<10)
            return plateau[l][c];
        
        return null;
    }

    /**
     * Method to get the Arbitre object associated with the game board.
     * @return Arbitre: The Arbitre object associated with the game board.
     */
    public Arbitre getArbitre() {
        return arbitre;
    }
    
    /**
     * Method to get the currently selected case.
     * @return CaseNoire: The currently selected case.
     * @see Plateau#caseSelectionnee
     */
    public CaseNoire getCaseSelect() {
        return caseSelectionnee;
    }
    
    /**
     * Method to get the height of the game board.
     * @return int: The height of the game board.
     */
    @Override
    public int getHeight() {
        return 10*Case.TAILLE + hauteur;
    }
    
    /**
     * Method to get the currently selected piece.
     * @return Piece: The currently selected piece.
     * @see Plateau#pieceSelec
     */
    public Piece getPieceSelec() {
        return pieceSelec;
    }

    /**
     * Method to get the width of the game board.
     * @return int: The width of the game board.
     */
    @Override
    public int getWidth() {
        return 10*Case.TAILLE + largeur;
    }

    /**
     * Method to reset the selection of a case and a piece.
     * @see Plateau#caseSelectionnee
     * @see Plateau#pieceSelec
     */
    public void initialise() {
        caseSelectionnee = null;
        pieceSelec = null; 
    }
    
    /**
     * Method to check if there's an obligatory move.
     * @return boolean: True if there's an obligatory move, false otherwise.
     * @see Plateau#obligatoire
     */
    public boolean isObligatoire() {
        return obligatoire;
    }

    /**
     * Method to set whether the game board is active or not.
     * @param b boolean: True to set the game board as active, false otherwise.
     */
    public void setActif(boolean b) {
        actif = b;
    }
    
    /**
     * Method to set the Arbitre object for the game board.
     * @param a Arbitre: The Arbitre object to set for the game board.
     */
    public void setArbitre(Arbitre a) {
        arbitre = a;
    }
    
    /**
     * Method to set whether there is an obligatory move or not.
     * @param b boolean: True to set an obligatory move, false otherwise.
     */
    public void setObligatoire(boolean b) {
        obligatoire = b;
    }

    /**
     * Method to empty all squares on the game board.
     */
    public void vider() {
        for(int i=0;i<10;i++) {
            for(int j=0;j<10;j++) {
                get(i,j).remove();
            }
        }
        repaint();
    }
}