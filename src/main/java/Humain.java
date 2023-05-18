/*
 *****************************************************************************
 *                         Humain.java  -  description                  
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

import javax.swing.JOptionPane;

/**
 * Classe symbolisant un joueur humain.
 * @author rudy
 */
public class Humain extends Joueur {
    
    /**
     * Constructeur de la classe Humain
     * @param n Nom du joueur.
     * @param c Couleur du joueur.
     * @param p Plateau sur lequel le joueur joue.
     */
    public Humain(String n, int c, Plateau p) {
        super(n,c,p);
    }
    
    /**
     * Methode qui annonce au <code>joueur</code> que son adversaire abandonne.
     */
    public void abandonAdversaire() {
        JOptionPane.showMessageDialog(null, adversaire.getNom() +" a abandonne. Vous avez gagne !", "Jeu De Dames", JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Methode qui demande au <code>joueur</code> s'il accepte que son 
     * adversaire annule son dernier coup.
     * @return Retourne true si le joueur accepte d'annuler le coup, false sinon
     */
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
     * Methode qui verifie qu'un coup est possible
     * @return true si un coup est possible, false sinon
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
     * Methode donnant la main au joueur
     * @return true
     */
    public boolean jouer() {
        setActif(true);
        adversaire.setActif(false);
        
        if(!coupPossible()) 
            return false;
        
        startTimer();
        return true;
    }
    
    /**
     * Methode qui demande au <code>joueur</code> s'il veut que la partie soit nulle.
     * @return Retourne un booleen, true si la partie est accepte comme nulle, false sinon
     */
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
     * Methode qui annonce au <code>joueur</code> que son adversaire a refuse sa proposition
     * de partie nulle.
     */
    public void nulRefuse() {
        JOptionPane.showMessageDialog(null,adversaire.getNom() + " a refuse votre proposition de partie nulle.", "Jeu De Dames", JOptionPane.INFORMATION_MESSAGE);
        if(actif)
            startTimer();
        else
            adversaire.startTimer();
    }
    
    /**
     * Renvoie une chaine decrivant le joueur : H:NomDuJoueur
     * @return chaine de la forme : H:NomDuJoueur
     */
    public String toString() {
        return "Humain:"+ nom;
    }
}
