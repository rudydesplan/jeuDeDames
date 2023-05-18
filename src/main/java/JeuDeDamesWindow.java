/*
 *****************************************************************************
 *                         JeuDeDamesWindow.java  -  description             *
 *                            -------------------                            *
 *   begin                : 18 mai. 2023                                      
 *   copyright            : (C) 2023 by Rudy Desplan
 *   email                : rudy.desplan@etud.univ-paris8.fr                      *
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
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.WindowConstants;

/**
 * 
 * Classe heritant de la classe <code>JFrame</code>. Cette classe gere la fenetre du jeu de dames,
 * l'affichage des composants,...
 * 
 * @author rudy
 * 
 */
public class JeuDeDamesWindow extends JFrame {
    
    /**
     * Hauteur de la fenetre
     */
    private static final float hauteur = (float) 2/3;
    /**
     * Largeur de la fenetre
     */
    private static final float largeur = (float) 2/3;
    /**
     * Position X de la fenetre
     */
    private static final float posX = (float) 1/6;
    /**
     * Position Y de la fenetre
     */
    private static final float posY = (float) 1/6;
    /**
     * Methode principal lancant le Jeu de Dames.
     * @param args
     */
    public static void main(String[] args) {
        JeuDeDamesWindow jeuDeDamesWindow = new JeuDeDamesWindow();
        jeuDeDamesWindow.setVisible(true);
    }
    /**
     * Arbitre controlant le jeu
     */
    private transient Arbitre arbitre;
    
    /**
     * JoueurPanel du joueur 1
     */
    private JoueurPanel joueur1Panel = null;
    /**
     * JoueurPanel du joueur2
     */
    private JoueurPanel joueur2Panel = null;
    /**
     * JPanel ou est dessine les composants de la fenetre
     */
    private JPanel pane = null;
    /**
     * Plateau representant la damier
     * 
     */
    private Plateau plateau;
    
    
    
    /**
     * Constructeur de base.
     */
    public JeuDeDamesWindow() {
        //Initialisation de la fen�tre
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("Jeu de Dames");
        
        //Taille et position de la fenetre
        Toolkit k = Toolkit.getDefaultToolkit();
        Dimension tailleEcran = k.getScreenSize();
        int largeurEcran = tailleEcran.width;
        int hauteurEcran = tailleEcran.height;
        setSize((int) (largeurEcran*hauteur), (int) (hauteurEcran*largeur));
        setLocation((int) (largeurEcran*posX), (int) (hauteurEcran*posY));
        
        //Menu
        JMenuBar menuBar = new JMenuBar();
        JMenu mFichier = new JMenu("Fichier");
        mFichier.setMnemonic(KeyEvent.VK_F);
        
        //Item Nouveau
        JMenuItem iNouveau = new JMenuItem("Nouvelle Partie",KeyEvent.VK_N);
        iNouveau.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N,java.awt.event.InputEvent.CTRL_DOWN_MASK));
        iNouveau.addActionListener(ae -> nouvellePartie());
        
        //Item Charger
        JMenuItem iCharger = new JMenuItem("Charger une partie",KeyEvent.VK_C);
        iCharger.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_C,java.awt.event.InputEvent.CTRL_DOWN_MASK));
        iCharger.addActionListener(ae -> chargerPartie());
        
        //Item sauver
        JMenuItem iSauver = new JMenuItem("Sauvegarder la partie",KeyEvent.VK_S);
        iSauver.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S,java.awt.event.InputEvent.CTRL_DOWN_MASK));
        iSauver.addActionListener(ae -> sauverPartie());
        
        //item Quitter
        JMenuItem iQuitter = new JMenuItem("Quitter",KeyEvent.VK_Q);
        iQuitter.setAccelerator(KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_X,java.awt.event.InputEvent.CTRL_DOWN_MASK));
        iQuitter.addActionListener(ae -> quitter());
        
        // Item Aide
        JMenu mAide = new JMenu("Aide");
        mAide.setMnemonic(KeyEvent.VK_A);
        
        //Item A Propos
        JMenuItem iAPropos = new JMenuItem("A Propos");
        iAPropos.addActionListener(ae -> aPropos());
        
        //Ajout de tous les elements
        mFichier.add(iNouveau);
        mFichier.add(iCharger);
        mFichier.add(iSauver);
        mFichier.add(iQuitter);
        mAide.add(iAPropos);
        menuBar.add(mFichier);
        menuBar.add(mAide);
        
        //Ajout du Menu
        getRootPane().setJMenuBar(menuBar);
        
        
        pane = new JPanel();
        pane.setLayout(new GridBagLayout());
        
        // Creation du plateau
        plateau = new Plateau();
        
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.NONE;
        c.gridx = 0;
        c.gridy= 0;
        c.gridheight = 2;
        
        
        pane.add(plateau,c);
        arbitre = new Arbitre(plateau,this);
        plateau.setArbitre(arbitre);
        setContentPane(pane);
        
    }
    
    /**
     * D�finit le panel du joueur 1
     * @param p Panel du joueur 1
     * 
     */
    public void addJ1Panel(JoueurPanel p) {
        if(joueur1Panel!=null)
            remove(joueur1Panel);
        joueur1Panel = p;
        p.setBounds(600,100,200,150);
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.NONE;
        c.gridx = 1;
        c.gridy = 0;
        pane.add(p,c);
        validate();
        repaint();
    }
    /**
     * D�finit le panel du joueur 2
     * @param p Panel du joueur 2
     */
    public void addJ2Panel(JoueurPanel p) {
        if(joueur2Panel!=null)
            remove(joueur2Panel);
        joueur2Panel = p;
        p.setBounds(600,300,200,150);
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.NONE;
        c.gridx = 1;
        c.gridy = 1;
        pane.add(p,c);
        validate();
        repaint();
    }
    
    /**
     * M�thode qui affiche la boite a propos.
     *
     */
    public void aPropos() {
        APropos aProposWindow = new APropos(this);
        aProposWindow.setVisible(true);
    }
    
    /**
     * M�thode qui charge une partie enregistr�.
     * @see Arbitre#charger      
     */
    public void chargerPartie() {
        
        JFileChooser chooser = new JFileChooser();
        if(arbitre.getJoueurActif()!=null)
            arbitre.getJoueurActif().stopTimer();
        int returnVal = chooser.showOpenDialog(this);
        if(returnVal == JFileChooser.APPROVE_OPTION) {
            arbitre.charger(chooser.getSelectedFile());
        }
        if(arbitre.getJoueurActif()!=null)
            arbitre.getJoueurActif().startTimer();
        
    }
    
    
    /**
     * M�thode appel�e pour d�buter une nouvelle partie.
     */
    public void nouvellePartie() {
        NouvellePartieWindow nouvellePartieWindow = new NouvellePartieWindow(this,arbitre);
    }
    
    /**
     * M�thode appeller lorsque le joueur quitte le jeu.
     */
    public void quitter() {
        System.exit(0);
    }
    
    /**
     * M�thode qui sauvegarde la partie.
     * @see Arbitre#sauver
     *
     */
    public void sauverPartie() {
        JFileChooser saver = new JFileChooser();
        if(arbitre.getJoueurActif()!=null)
            arbitre.getJoueurActif().stopTimer();
        int returnVal = saver.showSaveDialog(this);
        if(returnVal == JFileChooser.APPROVE_OPTION) {
            arbitre.sauver(saver.getSelectedFile());
        }
        if(arbitre.getJoueurActif()!=null)
            arbitre.getJoueurActif().startTimer();
    }
}