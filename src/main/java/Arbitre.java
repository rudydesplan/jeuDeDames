/*
 *****************************************************************************
 *                         Arbitre.java  -  description                  
 *                            -------------------                        
 *   begin                : 18 mai. 2023                                      
 *   copyright            : (C) 2023 by Rudy Desplan
 *   email                : rudy.desplan@etud.univ-paris8.fr                    
 *****************************************************************************
 
 ***************************************************************************
 *                                                                         *
 *   This program is free software; you can redistribute it and/or modify  *
 *   it under the terms of the GNU General Public License as published by  *
 *   the Free Software Foundation; either version 2 of the License, or     *
 *   (at your option) any later version.                                   *
 *                                                                         *
 ***************************************************************************
 * 
 */

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
 * 
 * La classe arbitre est la classe qui controle le deroulement du jeu. C'est
 * l'Arbitre qui verifie que la partie n'est pas termine, si le coup est
 * valable, determine les coups obligatoires, etc.
 * 
 * @author rudy
 */
public class Arbitre {
    
    /**
     * Constante indiquant une partie nulle.
     */
    public static final int NUL = 1;
    
    /**
     * Constante indiquant une partie perdue.
     */
    public static final int PERDU = 0;
    
    /**
     * Coup en cours.
     */
    private Rafle coupCourant = null;
    
    /**
     * Vecteur contenant toutes les coups obligatoires possibles. Chaque element
     * de ce vecteur est une instance de la classe Rafle.
     * 
     * @see Arbitre#calculCoupObligatoire()
     * @see Rafle
     */
    private Vector<Rafle> coups = null;
    
    /**
     * Coup Suivant.
     */
    private Rafle coupSuivant = null;
    
    /**
     * Instance de la fenetre ou se deroule le jeu.
     */
    private JeuDeDamesWindow fenetre = null;
    
    /**
     * Historique des coups
     */
    private HistoriqueCoup historique = null;
    
    /**
     * Joueur actif du Jeu.
     */
    private Joueur j = null;
    
    /**
     * Premier joueur du Jeu de Dames
     */
    private Joueur j1;
    
    /**
     * Deuxieme joueur du Jeu de Dames
     */
    private Joueur j2;
    
    /**
     * Stocke le nombre de secondes restantes le timer est mis en pause
     */
    private int pause;
    
    /**
     * Contient la disposition des pions sur le plateau.
     */
    private Plateau plateau = null;
    
    /**
     * Limte de temps de reflexion en secondes.
     */
    private int temps = 0;
    
    /**
     * Constructeur de base d'Arbitre. Il prend en argument qu'une instance de
     * la classe Plateau ou se deroule le jeu.
     * 
     * @param p
     *            Plateau du jeu.
     * @param parent
     *            Fenetre ou se deroule le jeu
     */
    public Arbitre(Plateau p, JeuDeDamesWindow parent) {
        plateau = p;
        fenetre = parent;
        coups = new Vector<>();
        historique = new HistoriqueCoup();
    }
    
    /**
     * Methode appelee lorsque le <code>joueur</code> abandonne. A la suite de
     * l'appel a cet fonction, la partie est donc termine.
     * 
     * @param joueur
     *            joueur qui abandonne.
     */
    public void abandon(Joueur joueur) {
        
        joueur.getAdversaire().abandonAdversaire();
        j = joueur;
        finDePartie(PERDU);
    }
    
    /**
     * Methode qui annule le dernier coup de <code>joueur</code>. On demande
     * au preablable a son adversaire s'il est d'accord, si oui on annule son
     * dernier coup, sinon on ne fait rien.
     * 
     * @param joueur
     *            Joueur qui veut annuler son dernier coup.
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
     * <p>
     * Cette methode calcul les coups obligatoires ou rafle que le joueur peut
     * effectuer dans le tour. La liste de ces coups se trouve dans le vecteur
     * <code>coups</code>.
     * </p>
     * <p>
     * Dans un premier temps on calcul les coups obligatoires, si on en trouve,
     * on met en surbrillance les cases en question.
     * </p>
     * 
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
     * Methode qui change le joueur qui detient le tour.
     *  
     */
    public void changerJoueur() {
        j.stopTimer();
        j = j.getAdversaire();
    }
    
    /**
     * Methode qui charge une partie enregistrer dans un fichier.
     * 
     * @param fichier
     *            Fichier contenant le sauvegarde de la partie.
     * @see Arbitre#sauver(File)
     * @todo Gestion des exceptions
     */
    public void charger(File fichier) {
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
            BufferedReader in = new BufferedReader(new FileReader(fichier));
            
            // Vide le plateau des pieces qu'il s'y trouvait
            plateau.vider();
            
            // Creation du nouveau joueur
            s = in.readLine();
            if (s.charAt(0) == 'H')
                j1 = new Humain(s.substring(2), Piece.BLANC, plateau);
            else
                j1 = new Ordinateur(s.substring(2), Piece.BLANC, plateau);
            
            fenetre.addJ1Panel(new JoueurPanel(j1));
            // On met la position des pieces dans s, pour pouvoir travailler
            // dessus
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
            
            /*
             * Lecture du temps
             */
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
            
        } catch (IOException | IndexOutOfBoundsException e) {
            JOptionPane.showMessageDialog(null,
                    "Erreur lors de la lecture du fichier : "
                    + fichier.getName(), "Jeu De Dames",
                    JOptionPane.ERROR_MESSAGE);
            historique.clear();
            plateau.vider();
            if(j1!=null) {
                j1.hidePanel();
            }
            if(j2!=null) {
                j2.hidePanel();
            }
            return;
        }
        
        //Attribution des adversaires
        j1.setAdversaire(j2);
        j2.setAdversaire(j1);
        
        effaceCoupObligatoire();
        calculCoupObligatoire();
        plateau.setActif(true);
    }
    
    /**
     * Dessine sur le plateau les coups obligatoires.
     * 
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
     * Methode qui efface les coups obligatoires dessine sur le plateau
     * 
     * @see Arbitre#calculCoupObligatoire()
     */
    public void effaceCoupObligatoire() {
        for (int i = 0; i < coups.size(); i++) {
            (coups.elementAt(i)).effaceCoups(plateau);
        }
        plateau.setObligatoire(false);
    }
    
    /**
     * <p>
     * Methode apelle lorsque le joueur a effectue un coup. Dans cette methode,
     * on enregistre le coup effectue, on verifie que la partie n'est pas
     * termine, et on "prepare" le coup suivant; c'est-a-dire que soit on calcul
     * le(s) coup(s) obligatoire(s) de l'autre joueur si le joueur ayant
     * effectue ce coup ne peut plus jouer, soit le joueur peut encore jouer
     * dans ce cas on met a jour la liste des coups obligatoires.
     * </p>
     * 
     * @param prise
     *            Nombre de prise possible restantes
     * @param coup
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
     * Methode apelle lorsque la partie est termine. On affiche le gagnant, et
     * on reinitialise la fenetre.
     * 
     * @param raison
     *            Entier indiquant la raison de la fin de partie.
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
     * Methode appele lorsqu'un joueur depasse le temps indique.
     *  
     */
    public void finTemps() {
        j.stopTimer();
        JOptionPane.showMessageDialog(null, j.getNom()
                + "a depasse le temps de jeu !", "Jeu De Dames",
                JOptionPane.INFORMATION_MESSAGE);
        finDePartie(PERDU);
    }
    
    /**
     * Retourne sous forme d'un vecteur la liste des coups obligatoires. Chaque
     * coup obligatoire est stocke sous forme de <code>Rafle</code>.
     * 
     * @return Un vecteur contenant tous les coups obligatoires.
     * @see Rafle
     */
    public Vector<Rafle> getCoupObligatoire() {
        return coups;
    }
    
    /**
     * Methode qui renvoit le joueur ayant le nom <code>nom</code>
     * 
     * @param nom
     *            nom du joueur que l'on recherche
     * @return joueur ayant le nom <code>nom</code>
     */
    public Joueur getJoueur(String nom) {
        if (j1.getNom().compareTo(nom) == 0)
            return j1;
        
        return j2;
    }
    
    /**
     * Retourne le joueur 1, celui qui controle les pions blancs.
     * 
     * @return le joueur qui possede les blancs.
     */
    public Joueur getJoueur1() {
        return j1;
    }
    
    /**
     * Retourne le joueur 2, celui qui controle les pions noirs.
     * 
     * @return le joueur qui possede les noirs.
     */
    public Joueur getJoueur2() {
        return j2;
    }
    
    /**
     * Retourne le joueur qui joue.
     * 
     * @return Renvois le joueur a qui c'est le tour de jouer.
     */
    public Joueur getJoueurActif() {
        return j;
    }
    
    /**
     * Retourne la plateau ou se deroule le jeu.
     * 
     * @return plateau ou se deroule le jeu.
     */
    public Plateau getPlateau() {
        return plateau;
    }
    
    /**
     * Retourne la rafle parmi la liste des coups obligatoires contenu dans
     * <code>coups</code> qui commence par la case <code>c</code>.
     * 
     * @param c
     *            Case recherche.
     * @return La rafle dont la premiere case est <code>c</code>, null si
     *         elle n'existe pas.
     */
    public Rafle getRafle(CaseNoire c) {
        for (int i = 0; i < coups.size(); i++) {
            if ((coups.elementAt(i)).getCaseDebut().compare(c))
                return coups.elementAt(i);
        }
        return null;
    }
    
    /**
     * Methode retournant la limite de temps en secondes
     * 
     * @return Limite de temps en secondes
     */
    public int getTemps() {
        return temps;
    }
    
    /**
     * Methode qui lit une piece a partir de la chaine s
     * 
     * @param s
     *            chaine a partir de laquelle la piece est cree
     * @param j
     *            Joueur a qui la piece va appartenir
     * @throws IndexOutOfBoundsException Lancee lorsqu'il y a un probleme de lecture
     * @return La piece cree a partir de s
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
     * Methode qui genere une rafle a partir de la String s.
     * @param s Chaine a partir de laquelle la rafle est generee
     * @return la rafle generee a partir de s.
     * @throws IndexOutOfBoundsException Lancee lorsqu'il y a un probleme de lecture
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
     * Met a jour la liste des coups obligatoires lorsque un joueur a
     * selectionnee une piece.
     * 
     * @param c
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
     * Methode qui initialise une nouvelle partie. Dans cette methode, on cree
     * les deux Joueurs, on positionne les pieces de ces joueurs sur le damier.
     *  
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
     * Methode appelee lorsque le <code>joueur</code> propose une partie
     * nulle. Un message est envoye a son adversaire, si celui ci accepte la
     * partie nulle, la partie est declare comme nulle, et le jeu s'arrete.
     * Sinon la partie continue.
     * 
     * @param joueur
     *            joueur qui propose le nul.
     */
    public void nul(Joueur joueur) {
        
        if (joueur.getAdversaire().nulAdversaire()) {
            finDePartie(NUL);
        } else {
            joueur.nulRefuse();
        }
    }
    
    /**
     * <p>
     * Methode qui sauvegarde la partie.
     * </p>
     * <p>
     * En premier on trouve le nom du premier joueur (les blancs). Ensuite on
     * trouve la liste des pieces avec les positions. Chaque piece a cette forme :
     * "T:l,c" ou T vaut P pour un pion, D pour une dame; <code>c</code> est
     * la colonne ou se trouve la piece, <code>l</code> la ligne. Les pieces
     * sont separees par ";" A la suite, on trouve de la meme maniere le nom du
     * deuxieme joueur, ainsi que la liste de ces pieces. Pour finir le nom du
     * joueur actif.
     * </p>
     * 
     * @param fichier
     *            Fichier dans lequel la partie est sauvegarder.
     * @see Arbitre#charger(File)
     * @todo Gestion des execeptions
     */
    public void sauver(File fichier) {
        try {
            
            BufferedWriter out = new BufferedWriter(new FileWriter(fichier));
            
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
                    + fichier.getName(), "Jeu De Dames",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Permet de definir le joueur 1
     * 
     * @param j
     *            joueur 1
     */
    public void setJoueur1(Joueur j) {
        j1 = j;
        j1.setTimer(temps);
    }
    
    /**
     * Permet de definir le joueur 2
     * 
     * @param j
     *            joueur 2
     */
    public void setJoueur2(Joueur j) {
        j2 = j;
        j2.setTimer(temps);
    }
    
    /**
     * Permet de definir le joueur actif
     * 
     * @param joueur
     *            joueur actif
     */
    public void setJoueurActif(Joueur joueur) {
        j = joueur;
    }
    
}
