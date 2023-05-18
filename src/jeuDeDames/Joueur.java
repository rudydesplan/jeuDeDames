/*
 *****************************************************************************
 *                         Joueur.java  -  description                  
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

import java.awt.event.ActionEvent;
import java.util.Vector;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Timer;

/**
 * 
 * Classe abstraite d�crivant un joueur du Jeu de Dames. Ce joueur peut aussi bien �tre un humain 
 * que l'ordinateur.
 * 
 * @author rudy
 * @see Humain
 * @see Ordinateur
 * 
 */
public abstract class Joueur {
    
    /**
     * Bool�en qui permet de savoir si le joueur est actif ou pas, c'est a dire, si c'est � lui de jouer.
     */
    protected boolean actif = false;
    
    /**
     * Adversaire
     */
    protected Joueur adversaire = null;
    
    /**
     * Couleur du pions du joueur.
     * @see Piece#couleur
     */
    protected int couleur;
    
    /**
     * Nom du joueur.
     */
    protected String nom;
    
    /**
     * Panel associ� au joueur affich� dans le fen�tre principale
     */
    protected JoueurPanel panel = null;
    
    /**
     * Pi�ces du joueur.
     */
    protected Vector<Piece> pieces = null;
    
    /**
     * Damier sur lequel le joueur joue.
     */
    protected Plateau plateau = null;
    
    /**
     * Nombre de secondes de r�flexion restantes
     */
    protected int tempsRestant;
    
    /**
     * Timer g�rant la limite en temps
     */
    protected Timer timer;
    
    /**
     * Action appel�e toutes les secondes par le timer
     */
    protected Action updateTemps;
    
    /**
     * Constructeur de base
     * 
     * @param n Nom du joueur.
     * @param c Couleur des pions du joueur.
     * @param p Damier sur lequel le joueur joue.
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
     * M�thode appel� lorsque le joueur clique sur le bouton abandonner
     *
     */
    public void abandon() {
        plateau.getArbitre().abandon(this);
    }
    
    /**
     * M�thode qui annonce au <code>joueur</code> que son adversaire abandonne.
     */
    public abstract void abandonAdversaire();
    
    /**
     * Actualise le nom du joueur. On met a jour le nombres de pi�ces restantes sur la plateau,
     * on affiche �galement une �toile si le joueur est actif.
     *
     */
    public void actualiserNom() {
        panel.setActif(actif);
    }
    
    /**
     * Ajoute une piece a la liste des pieces que le joueur possede
     * @param p la piece que l'on veut ajouter
     */
    public void addPiece(Piece p) {
        pieces.add(p);
    }
    
    /**
     * M�thode appel� lorsque le joueur clique sur le bouton Annuler Coup
     *
     */
    public void annule() {
        if(actif)
            stopTimer();
        else
            adversaire.stopTimer();
        plateau.getArbitre().annulerCoup(this);
    }
    
    /**
     * M�thode qui demande au <code>joueur</code> s'il accepte que son 
     * adversaire annule son dernier coup.
     * @return true si le joueur accepte que son adversaire annule son coup, false sinon
     */
    public abstract boolean annulerCoup();
    
    /**
     * M�thode qui cr�e les pions du joueur.
     * Cette m�thode doit �tre apell� uniquement en d�but de partie.
     * 
     */
    public void debut() {
        /*
         * Cr�ation des pi�ces + position des pi�ces sur le damier 
         */
        
        // Suivant la couleur, on commence � la ligne "0" (pour les blancs)
        // ou � la ligne "6" pour les noirs 
        
        for(int i=(couleur*6);i<(couleur*6)+4;i++) {
            // On parcoure toutes les case noir de la ligne.
            // Si i est pair, la premi�re case est noir, sinon elle est blanche
            for(int j=((i+1)%2);j<10;j+=2) {
                Piece pion = new Pion(couleur,(CaseNoire) plateau.get(i,j),plateau,this);
                pieces.add(pion);
            }
            
        }
    }
    
    /**
     * Retourne true si c'est au joueur de jouer, false sinon.
     * @return true si c'est au joueur de jouer, false sinon.
     */
    public boolean getActif() {
        return actif;
    }
    
    /**
     * Retourne l'adversaire.
     * @return Adversaire
     */
    public Joueur getAdversaire() {
        return adversaire;
    }
    
    /**
     * M�thode qui renvoit la couleur des pions du joueur.
     * <ul>
     * <li>BLANC pour les blancs</li>
     * <li>NOIR pour les noirs</li>
     * </ul>
     * @return Couleur des pions du joueur.
     * @see Piece#NOIR
     * @see Piece#BLANC
     */
    public int getCouleur() {
        return couleur;
    }
    
    /**
     * Retourne le nom du joueur.
     * @return Cha�ne contenant le nom du joueur.
     */
    public String getNom() {
        return nom;
    }
    
    /**
     * Retourne le nombre de pi�ce que poss�de la joueur.
     * @return Nombre de pieces du joueur
     */
    public int getNombrePieces() {
        return pieces.size();
    }
    
    /**
     * Retourne le <code>JoueurPanel</code> du joueur
     * @return Le panel du joueur
     */
    public JoueurPanel getPanel() {
        return panel;
    }
    /**
     * M�thode qui retourne la pi�ce "i" du joueur.
     * @param i i�me pi�ce du joueur.
     * @return i�me pi�ce du joueur.
     */
    public Piece getPiece(int i) {
        return pieces.elementAt(i);
    }
    
    /**
     * Retourne le temps restant au joueur pour jouer
     * @return Temps restant pour jouer
     */
    public int getTempsRestant() {
        return tempsRestant;
    }
    
    /**
     * M�thode qui cache le panneau
     *
     */
    public void hidePanel() {
        panel.setVisible(false);
    }
    
    /**
     * M�thode faisant joueur le joueur.
     * @return true si le joueur peut jouer, false sinon.
     */
    public abstract boolean jouer();
    
    /**
     * M�thode appel� lorsque le joueur clique sur le bouton Proposer Nul
     *
     */
    public void nul() {
        if(actif)
            stopTimer();
        else
            adversaire.stopTimer();
        plateau.getArbitre().nul(this);
    }
    
    /**
     * M�thode qui demande au <code>joueur</code> s'il veut que la partie soit nulle.
     * @return true si le joueur accepte que la partie est nulle, false sinon 
     */
    public abstract boolean nulAdversaire();
    
    
    
    /**
     * M�thode qui annonce au <code>joueur</code> que son adversaire a refus� sa proposition
     * de partie nulle.
     */
    public abstract void nulRefuse();
    
    /**
     * M�thode qui permet de faire la promotion du pion p
     * @param p Pion promu.
     */
    public void promotion(Pion p) {
        pieces.remove(p);
        p.getPosition().remove();
        Dame d = new Dame(couleur,p.getPosition(),plateau,this);
        d.deplacer(p.getPosition());
        pieces.add(d);
    }
    
    
    /**
     * Supprime la Piece <code>p</code> du joueur.
     * @param p
     */	
    public void removePiece(Piece p) {
        pieces.remove(p);
        actualiserNom();
    }
    
    /**
     * Permet de position la variable actif.
     * @param b true si c'est le tour du joueur, false sinon
     * @see Joueur#actif
     */
    public void setActif(boolean b) {
        actif = b;
        actualiserNom();
    }
    
    /**
     * Permet d'attribuer un adversaire au joueur.
     * @param j Adversaire
     */
    public void setAdversaire(Joueur j) {
        adversaire = j;
    }
    
    /**
     * Attribue un <code>JoueurPanel</code> au joueur
     * @param p JoueurPanel associ� au joueur
     */
    public void setPanel(JoueurPanel p) {
        panel = p;
    }
    
    /**
     * M�thode qui fixe la limite en temps
     * @param temps Limite en minute
     */
    public void setTemps(int temps) {
        tempsRestant = temps;
        setTimer(temps);
        
    }
    
    /**
     * Met � jour le contenu du label <code>timer</code> au temps t
     * @param t Temps � afficher dans le label
     */
    public void setTimer(int t) {
        panel.setTimer(t);
    }
    
    /**
     * M�thode qui d�marre le timer charg� de controll� le temps de r�flexion 
     */
    public void startTimer() {
        if(tempsRestant > 0)
            timer.start();
    }
    
    /**
     * M�thode qui arr�te le timer charg� de controll� le temps de r�flexion 
     * limite
     * @return retourne le nombre de seconde restante au moment de l'arret du timer
     */
    public int stopTimer() {
        timer.stop();
        return tempsRestant;
    }
    /**
     * M�thode renvoyant une cha�ne d�crivant le joueur
     * @return cha�ne d�crivant le joueur
     */
    public abstract String toString();
    /**
     * M�thode qui met � jour le label affichant le temps 
     * restant
     */
    public void updateTempsLabel() {
        tempsRestant--;
        panel.setTimer(tempsRestant);
        if(tempsRestant<=0) 
            plateau.getArbitre().finTemps();
    }
}
