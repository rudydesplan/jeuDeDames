package jeuDeDames;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Vector;

import javax.swing.JOptionPane;

/**
 * This class represents a game controller for a game similar to Checkers or Draughts ("Jeu De Dames" in French).
 * It manages the game state and controls game actions including determining the end of the game, 
 * updating obligatory moves, starting a new game, and saving the game state to a file.
 *
 * It includes methods for actions such as:
 * - finishing a turn,
 * - ending a game,
 * - ending the game when time runs out,
 * - getting the current obligatory move,
 * - getting the current active player,
 * - getting the game board,
 * - updating obligatory moves,
 * - starting a new game,
 * - saving the game state to a file,
 * - setting active player, and more.
 *
 * The class has a relationship with several other classes, including Rafle, CaseNoire, Plateau, Joueur and Piece, 
 * which represent various components of the game.
 */
public class Arbitre {
    
    /**
     * Constant value representing a null move.
     */
    public static final int NUL = 1;

    /**
     * Constant value representing a lost game.
     */
    public static final int PERDU = 0;

    /**
     * Current move in the game.
     */
    private Rafle coupCourant = null;

    /**
     * List of moves in the game.
     */
    private Vector<Rafle> coups = null;

    /**
     * Next move in the game.
     */
    private Rafle coupSuivant = null;

    /**
     * The game window instance.
     */
    private JeuDeDamesWindow fenetre = null;

    /**
     * The history of moves made in the game.
     */
    private HistoriqueCoup historique = null;

    /**
     * Current player in the game.
     */
    private Joueur j = null;

    /**
     * First player in the game.
     */
    private Joueur j1;

    /**
     * Second player in the game.
     */
    private Joueur j2;

    /**
     * The pause time between moves.
     */
    private int pause;

    /**
     * The game board instance.
     */
    private Plateau plateau = null;

    /**
     * The total time for the game.
     */
    private int temps = 0;

    /**
     * Constructor for the Arbitre class.
     *
     * @param p The game board instance.
     * @param parent The game window instance.
     */
    public Arbitre(Plateau p, JeuDeDamesWindow parent) {
        plateau = p;
        fenetre = parent;
        coups = new Vector<>();
        historique = new HistoriqueCoup();
    }
    
    /**
     * Handle player surrender event.
     *
     * @param joueur The player that is surrendering.
     */
    public void abandon(Joueur joueur) {
        
        joueur.getAdversaire().abandonAdversaire();
        j = joueur;
        finDePartie(PERDU);
    }
    
    /**
     * Undo a player's move.
     *
     * @param joueur The player who wishes to undo his move.
     */
    public void annulerCoup(Joueur joueur) {
        if (coupCourant != null) {
            JOptionPane.showMessageDialog(null, "Avant d'annuler un coup, "
                    + j.getNom() + " doit terminer son coup.", "Jeu De Dames",
                    JOptionPane.ERROR_MESSAGE);
            j.startTimer();
            return;
        }
        if (joueur.getAdversaire().annulerCoup()) {

            int nbCoup = historique.getNbCoups();
            int ret = historique.getDernierCoup(joueur);
            
            if (ret == -1) {
                JOptionPane.showMessageDialog(null,
                        "Impossible d'annuler le coup", "Jeu De Dames",
                        JOptionPane.ERROR_MESSAGE);
                j.startTimer();
                return;
            }
            
            for (int i = nbCoup - 1; i >= ret; i--) {
                Rafle r = historique.remove(i);
                if (r != null) {

                    CaseNoire caseDebut = r.getCaseDebut();
                    CaseNoire caseArrivee = null;
                    while (r.getCasesSuivantes(0) != null) {
                        if (r.getPiecePrise() != null) {
                            r.getPiecePrise().deplacer(r.getPrise());
                            r.getJoueur().getAdversaire().addPiece(r.getPiecePrise());
                            r.getJoueur().actualiserNom();
                        }
                        caseArrivee = r.getCasesSuivantes(0);
                        r = r.getRaflesSuivantes(0);
                    }
                    //Verification qu'il n'y a pas eu de promotion
                    if (caseArrivee != null) {
                        if (caseArrivee.getPiece() instanceof Dame) {  // Ajoute une vérification pour voir si la pièce est une Dame
                            Dame d = new Dame(caseArrivee.getPiece().getCouleur(), caseDebut, plateau, r.getJoueur());
                            d.deplacer(caseDebut);
                            r.getJoueur().removePiece(caseArrivee.getPiece());
                            caseArrivee.remove();
                        } else if (caseArrivee.getLigne() == 0 && caseArrivee.getPiece().getCouleur() == Piece.BLANC) {
                            Pion p = new Pion(Piece.BLANC, caseDebut, plateau, r.getJoueur());
                            p.deplacer(caseDebut);
                            r.getJoueur().removePiece(caseArrivee.getPiece());
                            caseArrivee.remove();
                        } else if (caseArrivee.getLigne() == 9 && caseArrivee.getPiece().getCouleur() == Piece.NOIR) {
                            Pion p = new Pion(Piece.NOIR, caseDebut, plateau, r.getJoueur());
                            p.deplacer(caseDebut);
                            r.getJoueur().removePiece(caseArrivee.getPiece());
                            caseArrivee.remove();
                        } else
                            caseArrivee.getPiece().deplacer(caseDebut);
                    }
                }
            }
            
            if (joueur != j) {
                j = joueur;
                j.jouer();
            }
            else
                j.startTimer();
            plateau.repaint();
            effaceCoupObligatoire();
            calculCoupObligatoire();
        }
        
    }
    
    /**
     * Calculate the obligatory move.
     * @see Rafle
     * @see Arbitre#coups
     * @see CaseNoire#setObligatoire(boolean)
     */
    public void calculCoupObligatoire() {
        coups = new Vector<>();
        Piece pi;
        int nbPrisesMax = 0;
        // pour toutes les cases du plateau
        for (int i = 0; i < 10; i++) {
            for (int k = 0; k < 10; k++) {
                //Copie du plateau qui permet d'effectuer les calculs
                Plateau p = new Plateau(plateau);
                //On verifie que sur cette case, il y a une piece et si elle
                // appartient
                //au joueur qui possede le tour
                if ((pi = p.get(i, k).getPiece()) != null
                        && pi.getCouleur() == j.getCouleur()) {
                    /*
                     * Calcul du coup obligatoire pour cette piece.
                     */
                    
                    Rafle rafle;
                    rafle = pi.coupObligatoire(null);
                    //Si elle peut effectuer un coup, on l'ajoute
                    if (rafle.getNbPrises() > nbPrisesMax) {
                        coups.clear();
                        nbPrisesMax = rafle.getNbPrises();
                        coups.add(rafle);
                    } else {
                        if (rafle.getNbPrises() == nbPrisesMax
                                && nbPrisesMax > 0) {
                            coups.add(rafle);
                        }
                    }
                    
                }
            }
        }
        dessineCoupObligatoire();
    }
    
    /**
     * Change the current player.
     */
    public void changerJoueur() {
        j.stopTimer();
        j = j.getAdversaire();
    }
    
    /**
     * This method is responsible for loading a game from a specified file.
     * The game is set up by reading the game's state from the file, including player types, 
     * their pieces, and the game's history.
     * 
     * @param fichier The file from which to load the game. The file is expected to follow a 
     * specific format containing information about the game's state.
     * @throws IOException if an I/O error occurs while reading from the file.
     * @throws IndexOutOfBoundsException if the contents of the file are malformed or contain 
     * invalid data (e.g., missing ";" separators, invalid coordinates, etc.).
     * 
     * @see Arbitre#sauver(File)
     */
    public void charger(File fichier) {
        BufferedReader in = null;
        try {
            
            //Chaine permettant de stocker la liste des pieces.
            String s;
            //Chaine contenant la piece
            String p;
            // Position de ";" dans la chaine"
            int pos1 = 0;
            int pos2 = 0;
            Piece piece;
            
            // Tampon permettant de lire dans le fichier.
            in = new BufferedReader(new FileReader(fichier));
            
            // Vide le plateau des pieces qu'il s'y trouvait
            plateau.vider();
            
            // Creation du nouveau joueur
            s = in.readLine();
            if (s.charAt(0) == 'H')
                j1 = new Humain(s.substring(2), Piece.BLANC, plateau);
            else
                j1 = new Ordinateur(s.substring(2), Piece.BLANC, plateau);
            
            fenetre.addJ1Panel(new JoueurPanel(j1));

            s = in.readLine();
            // Lecture des pieces du premier joueur
            while (pos2 != -1) {
                //Recherche la position du ";" suivant
                pos2 = s.indexOf(";", pos1);
                //Si on en a trouve un
                if (pos2 != -1) {
                    //Recupere la piece
                    p = s.substring(pos1, pos2);
                    piece = lirePiece(p, j1);
                    piece.deplacer((CaseNoire) piece.getPosition());
                    j1.addPiece(piece);
                    // Actualise la position de recherche
                    pos1 = pos2 + 1;
                }
                
            }
            
            // Lecture du nom du deuxieme joueur
            s = in.readLine();
            if (s.charAt(0) == 'H')
                j2 = new Humain(s.substring(2), Piece.NOIR, plateau);
            else
                j2 = new Ordinateur(s.substring(2), Piece.NOIR, plateau);
            
            fenetre.addJ2Panel(new JoueurPanel(j2));
            
            // Lecture des pieces du deuxieme joueur
            s = in.readLine();
            
            //Reinitialisation des offsets
            pos2 = 0;
            pos1 = 0;
            
            while (pos2 != -1) {
                // Recherche du ";" suivant
                pos2 = s.indexOf(";", pos1);
                // Si on en a trouve un
                if (pos2 != -1) {
                    //Recupere les "coordonnees" de la pieces
                    p = s.substring(pos1, pos2);
                    piece = lirePiece(p, j2);
                    piece.deplacer((CaseNoire) piece.getPosition());
                    j2.addPiece(piece);
                }
                //Actualise la position de la recherche
                pos1 = pos2 + 1;
            }
            
            //Mis a jour du joueur actif
            if (j2.getNom().compareTo(in.readLine()) == 0) {
                j = j2;
                j2.setActif(true);
            } else {
                j = j1;
                j1.setActif(true);
            }
            
            //Lecture du temps
            try {
                int temps = Integer.parseInt(in.readLine());
                j1.setTemps(temps);
                temps = Integer.parseInt(in.readLine());
                j2.setTemps(temps);
            } catch (NumberFormatException e) {
                j1.setTemps(-1);
                j2.setTemps(-1);
            }
            
            //Lecture de l'historique des coups
            historique.clear();
            
            try{
                while ((s = in.readLine()) != null) {
                    historique.addCoup(lireRafle(s));
                }
            }
            catch(IndexOutOfBoundsException e) {
                historique.clear();
            }
            
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null,
                    "Erreur lors de la lecture du fichier : "
                    + fichier.getName() + "\nDétails: " + e.getMessage(), "Jeu De Dames",
                    JOptionPane.ERROR_MESSAGE);
            cleanupGame();
        } catch (IndexOutOfBoundsException e) {
            JOptionPane.showMessageDialog(null,
                    "Erreur dans le contenu du fichier : "
                    + fichier.getName() + "\nLe fichier est mal formé ou contient des données invalides.", "Jeu De Dames",
                    JOptionPane.ERROR_MESSAGE);
            cleanupGame();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(null,
                        "Erreur lors de la fermeture du fichier : "
                        + fichier.getName(), "Jeu De Dames",
                        JOptionPane.ERROR_MESSAGE);
                }
            }
        }
        
        //Attribution des adversaires
        j1.setAdversaire(j2);
        j2.setAdversaire(j1);
        
        effaceCoupObligatoire();
        calculCoupObligatoire();
        plateau.setActif(true);
    }

    /**
     * This method is responsible for cleaning up the game when an error occurs during loading.
     * It clears the game's history, empties the game board, and hides the panels of the players.
     * This method is typically called when an exception is caught during the game loading process.
     */
    private void cleanupGame() {
        historique.clear();
        plateau.vider();
        if (j1 != null) {
            j1.hidePanel();
        }
        if (j2 != null) {
            j2.hidePanel();
        }
    }
    
    /**
     * Draw the obligatory move.
     * @see Arbitre#calculCoupObligatoire()
     */
    public void dessineCoupObligatoire() {
        for (int i = 0; i < coups.size(); i++) {
            (coups.elementAt(i)).misAJourCoups(plateau);
        }
        if (!coups.isEmpty())
            plateau.setObligatoire(true);
    }
    
    /**
     * Erase the obligatory move.
     * @see Arbitre#calculCoupObligatoire()
     */
    public void effaceCoupObligatoire() {
        for (int i = 0; i < coups.size(); i++) {
            (coups.elementAt(i)).effaceCoups(plateau);
        }
        plateau.setObligatoire(false);
    }
    
    /**
     * Handles the end of a move. Removes captured pieces, updates the game state, and switches the active player if necessary.
     *
     * @param coup  The move just played.
     * @param prise The number of captures made in the move.
     */
    public void finCoup(Rafle coup, int prise) {
        
        CaseNoire c = null;
        
        effaceCoupObligatoire();
        
        if ((c = coup.getPrise()) != null) {
            j.getAdversaire().removePiece(c.getPiece());
            c.remove();
        }
        
        if (coupCourant == null) {
            coupCourant = coup;
            coupSuivant = coup;
        } else {
            coupSuivant.setRaflesSuivantes(0, coup);
            coupSuivant = coupSuivant.getRaflesSuivantes(0);
        }
        
        //Plus de prises possibles, on change de joueur
        if (prise <= 1) {
            coup.getCasesSuivantes(0).getPiece().promotion();
            changerJoueur();
            calculCoupObligatoire();
            historique.addCoup(coupCourant);
            coupCourant = null;
        }
        
        // Il reste des coups possibles, on met a jour la liste des coups
        else {
            for (int i = 0; i < coups.size(); i++) {
                Vector<Rafle> v = (coups.elementAt(i)).getVector();
                for (int k = 0; k < v.size(); k++) {
                    if ((v.elementAt(k)).getCaseDebut().compare(
                            coup.getCasesSuivantes(0))) {
                        coups.clear();
                        (v.elementAt(k)).setCasePrise(null);
                        coups.add(v.elementAt(k));
                        dessineCoupObligatoire();
                        j.jouer();
                        return;
                    }
                }
            }
        }
        
        // Le joueur n'a plus de pieces
        if (j.getNombrePieces() == 0) {
            finDePartie(PERDU);
            return;
        }
        if (!j.jouer()) {
            JOptionPane.showMessageDialog(null, j.getNom()
                    + " ne peut plus jouer !", "Jeu De Dames",
                    JOptionPane.INFORMATION_MESSAGE);
            finDePartie(PERDU);
        }
        
    }
    
    /**
     * Handles the end of the game based on the given reason. Displays the winner or a draw message, and deactivates the game board.
     *
     * @param raison The reason for ending the game.
     * @see Arbitre#PERDU
     * @see Arbitre#NUL
     */
    public void finDePartie(int raison) {
        if (raison == PERDU) {
            if (j == j1) {
                JOptionPane.showMessageDialog(null, j2.getNom() + " a gagne !",
                        "Jeu De Dames", JOptionPane.INFORMATION_MESSAGE);
                
            } else {
                JOptionPane.showMessageDialog(null, j1.getNom() + " a gagne !",
                        "Jeu De Dames", JOptionPane.INFORMATION_MESSAGE);
            }
        }
        
        if (raison == NUL) {
            JOptionPane.showMessageDialog(null, "La partie est nulle !",
                    "Jeu De Dames", JOptionPane.INFORMATION_MESSAGE);
        }
        
        plateau.setActif(false);
        j1.getPanel().hideButton();
        j2.getPanel().hideButton();
        
        j1 = null;
        j2 = null;
    }
    
    /**
     * Handles the end of the game when the time runs out for the active player.
     */
    public void finTemps() {
        j.stopTimer();
        JOptionPane.showMessageDialog(null, j.getNom()
                + "a depasse le temps de jeu !", "Jeu De Dames",
                JOptionPane.INFORMATION_MESSAGE);
        finDePartie(PERDU);
    }
    
    /**
     * Returns the obligatory moves for the active player.
     *
     * @return A vector containing all the obligatory moves.
     * @see Rafle
     */
    public Vector<Rafle> getCoupObligatoire() {
        return coups;
    }
    
    /**
     * Returns the player with the given name.
     *
     * @param nom The name of the player.
     * @return The player with the given name.
     */
    public Joueur getJoueur(String nom) {
        if (j1.getNom().compareTo(nom) == 0)
            return j1;
        
        return j2;
    }

    /**
     * Returns the first player.
     *
     * @return The first player.
     */
    public Joueur getJoueur1() {
        return j1;
    }

    /**
     * Returns the second player.
     *
     * @return The second player.
     */
    public Joueur getJoueur2() {
        return j2;
    }

    /**
     * Returns the currently active player.
     *
     * @return The active player.
     */
    public Joueur getJoueurActif() {
        return j;
    }

    /**
     * Returns the game board.
     *
     * @return The game board.
     */
    public Plateau getPlateau() {
        return plateau;
    }
    
    /**
     * Returns the Rafle starting at the given CaseNoire.
     *
     * @param c The CaseNoire where the Rafle starts.
     * @return The Rafle starting at the given CaseNoire.
     */
    public Rafle getRafle(CaseNoire c) {
        for (int i = 0; i < coups.size(); i++) {
            if ((coups.elementAt(i)).getCaseDebut().compare(c))
                return coups.elementAt(i);
        }
        return null;
    }
    
    /**
     * Returns the remaining game time.
     *
     * @return The remaining game time.
     */
    public int getTemps() {
        return temps;
    }
    
    /**
     * Converts a string representation of a Piece into an actual Piece object. 
     * The string should follow the format: [Piece type][Index number] [Line number][Column number]
     * For example, "D1 5,7" creates a 'Dame' at position 5,7.
     *
     * @param s the string representation of the Piece to create
     * @param j the Joueur (player) to whom the piece belongs
     * @return a Piece object represented by the string input
     * @throws IndexOutOfBoundsException if the provided line or column is outside the range (0-9)
     */
    private Piece lirePiece(String s, Joueur j) throws IndexOutOfBoundsException {
        Piece p = null;
        try {
            int ligne = s.charAt(5) - '0';
            int colonne = s.charAt(7) - '0';
            
            //La ligne ou la colonne n'est pas valide
            if(ligne < 0 || ligne > 9 || colonne < 0 || colonne > 9)
                throw new IndexOutOfBoundsException();
            
            if (s.charAt(0) == 'D')
                p = new Dame(s.charAt(2) - '0', (CaseNoire) plateau.get(ligne,
                        colonne), plateau, j);
            else
                p = new Pion(s.charAt(2) - '0', (CaseNoire) plateau.get(ligne,
                        colonne), plateau, j);
        }
        catch(IndexOutOfBoundsException e) {
            throw new IndexOutOfBoundsException(); 
        }
        return p;
    }
    
    /**
     * Converts a string representation of a Rafle (sequence of capturing moves) into an actual Rafle object. 
     * The string should follow the format: [Player's name]|[Start square]|[Captured square]|[Captured piece's description][Subsequent rafles]
     * For example, "John|1,1|2,2|D1 5,7[3,3|4,4|D2 6,8]" represents a rafle by player 'John' starting from position 1,1, capturing a piece at position 2,2 and subsequent rafles.
     * If a square or a piece is null, it should be represented as "null".
     *
     * @param s the string representation of the Rafle to create
     * @return a Rafle object represented by the string input
     * @throws IndexOutOfBoundsException if the provided line or column is outside the range (0-9)
     */
    private Rafle lireRafle(String s) throws IndexOutOfBoundsException {
        
        try {
            //Nom du joueur
            int pos = s.indexOf('|');
            String nom = s.substring(0, pos);
            
            //Case debut
            CaseNoire caseDebut = null;
            int pos2 = s.indexOf('|', pos + 1);
            String c = s.substring(pos + 1, pos2);
            if (c.compareTo("null") != 0) {
                int caseLigne = c.charAt(0) - '0';
                int caseColonne = c.charAt(2) - '0';
                caseDebut = (CaseNoire) plateau.get(caseLigne, caseColonne);
            }
            
            pos = pos2;
            // Case Prise
            CaseNoire casePrise = null;
            pos2 = s.indexOf('|', pos + 1);
            c = s.substring(pos + 1, pos2);
            if (c.compareTo("null") != 0) {
                int caseLigne = c.charAt(0) - '0';
                int caseColonne = c.charAt(2) - '0';
                casePrise = (CaseNoire) plateau.get(caseLigne, caseColonne);
            }
            
            //Postionne avant le descriptif de la piece
            pos = pos2;
            Piece piecePrise = null;
            pos2 = s.indexOf('|', pos + 1);
            if (pos2 == -1)
                pos2 = s.indexOf(';', pos + 1);
            String prise = s.substring(pos + 1, pos2);
            if (prise.compareTo("null") != 0) {
                piecePrise = lirePiece(prise, getJoueur(nom));
            }
            
            Rafle r = new Rafle(caseDebut, casePrise, getJoueur(nom));
            r.setPiecePrise(piecePrise);
            
            //Lit les rafles suivantes
            pos = pos2;
            pos = s.indexOf('[', pos);
            while (pos != -1) {
                pos2 = s.indexOf(']', pos);
                //On fait la rafle qui correspondat a la rafle lu
                String rafle = s.substring(pos + 1, pos2 + 1);
                r.addRaflesSuivantes(lireRafle(rafle));
                pos = pos2;
                pos = s.indexOf('[', pos);
            }
            return r;
        }
        catch(IndexOutOfBoundsException e) {
            throw new IndexOutOfBoundsException(); 
        }
    }
    
    /**
     * Updates the obligatory moves after a move is made.
     *
     * @param c The CaseNoire where the move was made.
     * @see Arbitre#calculCoupObligatoire()
     */
    public void misAJourCoupsObligatoire(CaseNoire c) {
        int r = 0;
        for (int i = 0; i < coups.size(); i++) {
            if (!(coups.elementAt(i)).getCaseDebut().compare(c))
                (coups.elementAt(i)).effaceCoups(plateau);
            else
                r = i;
        }
        (coups.elementAt(r)).misAJourCoups(plateau);
        
    }
    
    /**
     * Resets the game state to start a new game.
     */
    public void nouvellePartie() {
        
        effaceCoupObligatoire();
        plateau.vider();
        
        j1.debut();
        j2.debut();
        
        j = j1;
        
        //Attribution des adversaires
        j1.setAdversaire(j2);
        j2.setAdversaire(j1);
        
        // Attribution des label
        j.jouer();
        
        // Positionnement des pieces du joueur 1
        for (int i = 0; i < 20; i++) {
            Piece piece = j1.getPiece(i);
            Case c = piece.getPosition();
            c.add(piece);
        }
        
        // Positionnement des pieces du Joueur 2
        for (int i = 0; i < 20; i++) {
            Piece piece = j2.getPiece(i);
            Case c = piece.getPosition();
            c.add(piece);
        }
        historique.clear();
        plateau.setActif(true);
        
    }
    
    /**
     * Handles the draw offer from a player. Ends the game in a draw if the other player agrees.
     *
     * @param joueur The player offering the draw.
     */
    public void nul(Joueur joueur) {
        
        if (joueur.getAdversaire().nulAdversaire()) {
            finDePartie(NUL);
        } else {
            joueur.nulRefuse();
        }
    }
    
    /**
     * Save the game to a file.
     *
     * @param fichier The file to which to save the game.
     * @see Arbitre#charger(File)
     */
    public void sauver(File fichier) {
        BufferedWriter out = null;
        try {
            
            out = new BufferedWriter(new FileWriter(fichier));
            
            //Sauvegarde Joueur 1
            out.write(j1.toString());
            out.write("\n");
            for (int i = 0; i < j1.getNombrePieces(); i++) {
                out.write(j1.getPiece(i).toString() + ";");
            }
            out.write("\n");
            
            //Sauvegarde Joueur 2
            out.write(j2.toString());
            out.write("\n");
            for (int i = 0; i < j2.getNombrePieces(); i++) {
                out.write(j2.getPiece(i).toString() + ";");
            }
            
            //Sauvegarde du Joueur actif.
            out.write("\n");
            out.write(j.getNom());
            
            //Sauvegarde du temps
            out.write("\n");
            out.write(Integer.toString(j1.getTempsRestant()));
            out.write("\n");
            out.write(Integer.toString(j2.getTempsRestant()));
            //Sauvegarde de l'historique.
            out.write("\n");
            out.write(historique.toString());
            out.close();
            
        } catch (IOException ioe) {
            JOptionPane.showMessageDialog(null,
                    "Erreur lors de la sauvegarde de la partie dans le fichier : "
                    + fichier.getName() + "\nDétails: " + ioe.getMessage(), "Jeu De Dames",
                    JOptionPane.ERROR_MESSAGE);
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(null,
                        "Erreur lors de la fermeture du fichier : "
                        + fichier.getName(), "Jeu De Dames",
                        JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }
    
    /**
     * Set the first player.
     *
     * @param j The player to be set as the first player.
     */
    public void setJoueur1(Joueur j) {
        j1 = j;
        j1.setTimer(temps);
    }
    
    /**
     * Set the second player.
     *
     * @param j The player to be set as the second player.
     */
    public void setJoueur2(Joueur j) {
        j2 = j;
        j2.setTimer(temps);
    }
    
    /**
     * Set the active player.
     *
     * @param joueur The player to be set as the active player.
     */
    public void setJoueurActif(Joueur joueur) {
        j = joueur;
    }
}