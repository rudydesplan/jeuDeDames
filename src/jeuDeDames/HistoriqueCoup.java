/*
 *****************************************************************************
 *                         HistoriqueCoup.java  -  description                  
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
 * Classe qui gere l'historique des coups. 
 * @author rudy
 */
public class HistoriqueCoup {
    
    /**
     * Vecteur contenant la liste des rafle effectue par les joueurs
     */
    private Vector<Rafle> historique = null;
    
    /**
     * Constructeur par defaut
     *
     */
    public HistoriqueCoup() {
        historique = new Vector<>();
    }
    
    /**
     * Ajout une rafle jouer par un joueur
     * @param r rafle jouee
     */
    public void addCoup(Rafle r) {
        historique.add(r);
    }
    
    /**
     * Efface tous les coups enregistres
     *
     */
    public void clear() {
        historique.clear();
    }
    
    /**
     * Retourne la rafle a la position <code>i</code>
     * @param i ieme rafle.
     * @return rafle a la position <code>i</code>
     */
    public Rafle get(int i) {
        if(i <=0 && i < historique.size())
            return historique.get(i);
        
        return null;
    }
    
    /**
     * Renvoit la position dans la liste des rafles de la derniere rafle effectue par le joueur <code>j</code>
     * @param j joueur dont on recherche le dernier coup
     * @return position de la derniere rafle jouee par le joueur <code>j</code>
     */
    public int getDernierCoup(Joueur j) {
        int nbCoup = historique.size();
        if( nbCoup>0 && (historique.get(nbCoup-1)).getJoueur() == j) {
            return nbCoup-1;
        }
        if(nbCoup < 1) 
            return -1;
        return nbCoup-2;
    }
    
    /**
     * Retourne le nombre de coups enregistres
     * @return Nombre de coups enregistres
     */
    public int getNbCoups() {
        return historique.size();
    }
    
    /**
     * Supprime la rafle a la position <code>i</code>
     * @param i Numero de la rafle que l'on souhaite supprimer
     * @return Rafle supprimee
     */
    public Rafle remove(int i) {
        return historique.remove(i);
    }
    
    /**
     * Retourne une chaine decrivant l'historique de la partie.
     * La chaine renvoyee contient une rafle par ligne.
     * @see Rafle#toString()
     * @return chaine decrivant l'historique.
     */
    public String toString() {
        String s = "";
        for(int i=0;i<historique.size();i++) {
            s+=historique.elementAt(i).toString() + "\n";
        }
        return s;
    }
}