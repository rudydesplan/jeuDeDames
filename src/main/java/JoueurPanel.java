/*
 *****************************************************************************
 *                         JoueurPanel.java -  description                  
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


import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * JPanel contenant les boutons des joueurs
 * @author Rudy
 *
 */
public class JoueurPanel extends JPanel {
    
    private static int heightButton = 20;
    
    private static int widthButton = 250;
    /**
     * Bouton qui permet au joueur 1 d'abandonner
     */
    private JButton abandon = null;
    /**
     * Bouton qui permet au joueur 1 d'annuler un coup.
     */
    private JButton annul = null;
    
    /**
     * Joueur "proprietaire" de panel
     */
    private transient Joueur joueur;
    /**
     * JLabel affichant le nom du joueur
     */
    private JLabel nom = null;
    /**
     * Bouton qui permet au joueur 1 de proposer un nul.
     */
    private JButton nul = null;
    /**
     * Label contenant le temps restant au joueur 1 pour jouer
     */
    private JLabel temps = null;
    
    /**
     * Constructeur par defaut
     * @param j Joueur proprietaire du JPanel
     */
    public JoueurPanel(Joueur j) {
        super();
        setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
        setPreferredSize(new Dimension(150,150));
        setMinimumSize(new Dimension(150,150));
        setMaximumSize(new Dimension(150,150));
        joueur = j;
        initialise();
        joueur.setPanel(this);
        
    }
    
    /**
     * Methode qui retourne une chaine de caractere sous le forme minute:seconde du temps
     * <code>t</code>.
     * @param t temps en secondes a transforme en <code>String</code>
     * @return une chaine de caractere de la forme minute:seconde de <code>temps</code>
     */
    public String getTempsString(int t) {
        if(t<0)
            return " ";
        int minutes = t / 60;
        int seconds = t - 60 * minutes;
        
        String minute = Integer.toString(minutes);
        String seconde = Integer.toString(seconds);
        
        if(minute.length() == 1) 
            minute = "0"+ minute;
        if(seconde.length() == 1)
            seconde = "0" + seconde;
        return minute + ":" + seconde; 
    }
    
    
    /**
     * Methode qui cache les boutons du panneau.
     *
     */
    public void hideButton() {
        abandon.setVisible(false);
        nul.setVisible(false);
        annul.setVisible(false);
    }
    
    /**
     * Methode qui initialise le panneau
     *
     */
    private void initialise() {
        
        abandon = new JButton("Abandonner");
        abandon.setPreferredSize(new Dimension(widthButton,heightButton));
        abandon.setAlignmentX(CENTER_ALIGNMENT);
        abandon.setMinimumSize(new Dimension(widthButton,heightButton));
        abandon.setMaximumSize(new Dimension(widthButton,heightButton));
        abandon.addActionListener(e -> joueur.abandon());

        annul = new JButton("Annuler coup");
        annul.setPreferredSize(new Dimension(widthButton,heightButton));
        annul.setMinimumSize(new Dimension(widthButton,heightButton));
        annul.setMaximumSize(new Dimension(widthButton,heightButton));
        annul.setAlignmentX(CENTER_ALIGNMENT);
        annul.addActionListener(e -> joueur.annule());

        nul = new JButton("Proposer nul");
        nul.setPreferredSize(new Dimension(widthButton,heightButton));
        nul.setAlignmentX(CENTER_ALIGNMENT);
        nul.setMinimumSize(new Dimension(widthButton,heightButton));
        nul.setMaximumSize(new Dimension(widthButton,heightButton));
        nul.addActionListener(e -> joueur.nul());
        
        temps = new JLabel();
        temps.setAlignmentX(CENTER_ALIGNMENT);
        nom = new JLabel(joueur.getNom());
        nom.setAlignmentX(CENTER_ALIGNMENT);
        add(Box.createVerticalGlue());
        add(nom);
        add(temps);
        add(Box.createRigidArea(new Dimension(0, 10)));
        add(abandon);
        add(Box.createRigidArea(new Dimension(0, 10)));
        add(nul);
        add(Box.createRigidArea(new Dimension(0, 10)));
        add(annul);
        add(Box.createVerticalGlue());
    }
    
    /**
     * Met a jour le nom du joueur.
     * @param b true si le joueur est actif, false sinon
     */
    public void setActif(boolean b) {
        if(b)
            nom.setText("->" + joueur.getNom() + " (" + joueur.getNombrePieces() + ")");
        else 
            nom.setText(joueur.getNom() + " (" + joueur.getNombrePieces() + ")");
    }
    
    /**
     * Methode qui met a jour le label affichant le temps au temps <code>t</code>
     * @param t temps affiche
     */
    public void setTimer(int t) {
        temps.setText(getTempsString(t));
    }
}
