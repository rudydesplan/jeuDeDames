/*
 *****************************************************************************
 *                         Ordinateur.java  -  description                  
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
import java.util.Vector;

/**
 * 
 * Classe decrvant un joueur du Jeu de Dames controle par l'ordinateur.
 * @todo Gestion dames.
 * @author rudy
 */

public class Ordinateur extends Joueur {
    
    /**
     * Constructeur de la classe Ordinateur.
     * @param n Nom du joueur.
     * @param c Couleur du joueur.
     * @param p Plateau sur lequel le joueur joue.
     */
    public Ordinateur(String n,int c,Plateau p) {
        super(n,c,p);
    }
    
    /**
     * Methode appelee lorsque l'adversaire abandonne
     */
    public void abandonAdversaire() {
        // L'ordinateur ne gere pas ca
    }
    
    /**
     * Methode qui demande au <code>joueur</code> s'il accepte que son 
     * adversaire annule son dernier coup.
     * @return toujours true, l'ordinateur est sympa !
     */
    public boolean annulerCoup() {
        return true;
    }
    
    /**
     * Methode faisant jouer le joueur.
     * @return true, si un coup a ete jouer, false sinon.
     */
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
            Piece p = (Piece) pieces.elementAt(i);
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
     * Methode qui demande au <code>joueur</code> s'il veut que la partie soit nulle.
     * @return On renvoit toujours true, l'ordinateur est sympa ! 
     */
    public boolean nulAdversaire() {
        return true;
    }
    
    /**
     * Methode appelee lorsque l'ordinateur a demande le nul
     */
    public void nulRefuse() {
        // L'ordinateur ne gere pas ca
    }
    
    /**
     * Associe un <code>JoueurPanel</code> au joueur
     * @param p la panel associe
     */
    public void setPanel(JoueurPanel p) {
        super.setPanel(p);
        panel.hideButton();
    }
    
    /**
     * Methode renvoyant un chaine decrivant le joueur. La chaine est de la forme : O:NomDuJoueur
     * @return chaine decrivant le joueur, elle est de la forme :O:NomDuJoueur
     */
    public String toString() {
        return "O:"+ nom;
    }
}
