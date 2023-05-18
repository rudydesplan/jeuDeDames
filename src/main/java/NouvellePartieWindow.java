/*
 *****************************************************************************
 *                         NouvellePartieWindow.java  -  description                  
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

import java.awt.event.KeyEvent;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

/**
 * Cette classe decrit la fenetre qui permet de creer une nouvelle partie.
 * @author rudy
 */
public class NouvellePartieWindow extends JDialog{
    /**
     * Bouton annuler
     */
    private JButton annulButton = null;
    /**
     * Arbitre qui controle le jeu
     */
    private transient Arbitre arbitre = null;
    /**
     * CheckBox permettant de definir s'il y a une limite de temps.
     */
    private JCheckBox checkTemps = null;
    /**
     * Hauteur de la fenetre
     */
    private int height = 415;
    /**
     * Type humain pour le joueur 1
     */
    private JRadioButton j1Humain = null;
    /**
     * Nom du joueur 1
     */
    private JTextField j1Nom = null;
    /**
     * Type Ordinateur pour le joueur 1
     */
    private JRadioButton j1Ordinateur = null;
    /**
     * Groupe les boutons j1Humain et j1Ordinateur
     */
    private ButtonGroup j1Type = null;
    /**
     * Type humain pour le joueur 2
     */
    private JRadioButton j2Humain = null;
    /**
     * Nom du joueur 2
     */
    private JTextField j2Nom = null;
    /**
     * Type Ordinateur pour le joueur 2
     */
    private JRadioButton j2Ordinateur = null;
    /**
     * Groupe les boutons j2Humain et j2Ordinateur
     */
    private ButtonGroup j2Type = null;
    /**
     * Titre de la partie du joueur 1
     */
    private JLabel joueur1 = null;
    /** 
     * Titre de la partie du joueur 2
     */
    private JLabel joueur2 = null;
    
    /**
     * JPanel contenant les composants de la fenetre
     */
    private JPanel jPanel = null;
    /**
     * JLabel Minute :
     */
    private JLabel labelMinute = null;
    /**
     * JLabel contenant un cours message d'explication
     */
    private JLabel message = null;
    
    /**
     * JTextField permettant d'entre un nombre de minute maximum
     */
    private JTextField nbMinute = null;
    /**
     * Jlabel : "Nom : " pour le joueur 1
     */
    private JLabel nomJ1 = null;
    /**
     * Jlabel : "Nom : " pour le joueur 2
     */
    private JLabel nomJ2 = null;
    /**
     * Bouton ok
     */
    private JButton okButton = null;
    /**
     * Fenetre parente
     */
    private JeuDeDamesWindow parent = null;
    /**
     * Largeur de la fenetre
     */
    private int width = 400;
    
    /**
     * Constructeur par default
     * @param a Arbitre du jeu
     * @param p Fenetre parente
     */
    public NouvellePartieWindow(JeuDeDamesWindow p, Arbitre a)  {
        super(p,"Nouvelle Partie",true);
        if(a.getJoueurActif()!=null)
            a.getJoueurActif().stopTimer();
        parent = p;
        arbitre = a;
        initialize();
    }
    
    /**
     * Methode qui cree une nouvelle partie avec les donnees rentrees.
     * @return true si tout les champs on ete rentres, false sinon.
     */
    private boolean creeNouvellePartie() {
        String j1 = j1Nom.getText();
        String j2 = j2Nom.getText();
        int min;
        
        if(!nomCorrect(j1) || !nomCorrect(j2))
            return false;
        
        if( !j1Ordinateur.isSelected() && !j1Humain.isSelected() ) {
            JOptionPane.showMessageDialog(null, "Veuillez indiquer un type de joueur pour le joueur 1.","Jeu De Dames", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        if( !j2Ordinateur.isSelected() && !j2Humain.isSelected() ) {
            JOptionPane.showMessageDialog(null, "Veuillez indiquer un type de joueur pour le joueur 2.","Jeu De Dames", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        if(j1.equals(j2)) {
            JOptionPane.showMessageDialog(null, "Les noms des joueurs sont identiques !","Jeu De Dames", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        //Creation des joueurs
        Joueur joueur1;
        Joueur joueur2;
        
        if( j1Ordinateur.isSelected())
            joueur1 = new Ordinateur(j1,Piece.BLANC,arbitre.getPlateau());
        else
            joueur1 = new Humain(j1,Piece.BLANC,arbitre.getPlateau());
        
        parent.addJ1Panel(new JoueurPanel(joueur1));
        
        if( j2Ordinateur.isSelected())
            joueur2 = new Ordinateur(j2,Piece.NOIR,arbitre.getPlateau());
        else
            joueur2 = new Humain(j2,Piece.NOIR,arbitre.getPlateau());
        
        parent.addJ2Panel(new JoueurPanel(joueur2));
        
        if(checkTemps.isSelected()) {
            String minute = nbMinute.getText();
            try {
                min = 60 * Integer.parseInt(minute);
            }
            catch(NumberFormatException e) {
                JOptionPane.showMessageDialog(null, minute + " n'est pas un nombre" +
                        " de minute valide !","Jeu De Dames", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }
        else {
            min = -1;
        }
        
        joueur1.setTemps(min);
        joueur2.setTemps(min);
        
        
        
        arbitre.setJoueur1(joueur1);
        arbitre.setJoueurActif(joueur1);
        arbitre.setJoueur2(joueur2);
        
        arbitre.getJoueurActif().startTimer();
        
        arbitre.getPlateau().initialise();
        
        // Debute la partie
        arbitre.nouvellePartie();
        
        return true;
        
    }
    
    /**
     * Redefinition de la methode dispose de JDialog
     */
    @Override
    public void dispose() {
        super.dispose();
        if(arbitre.getJoueurActif()!=null)
            arbitre.getJoueurActif().startTimer();
    }
    /**
     * Cette methode initialise <code>annulButton</code>, et le retourne.
     * 	
     * @return javax.swing.JButton
     * @see NouvellePartieWindow#annulButton	
     */    
    private JButton getAnnulButton() {
        if (annulButton == null) {
            annulButton = new JButton();
            annulButton.setMnemonic(KeyEvent.VK_A);
            annulButton.setBounds(227, 333, 116, 26);
            annulButton.setText("Annuler");
            annulButton.addActionListener(e -> dispose());
        }
        return annulButton;
    }
    
    /**
     * Retourne je JCheckBox checkTemps
     * @return Retourne le JCheckBox checkTemps
     * @see NouvellePartieWindow#checkTemps
     */
    private JCheckBox getCheckTemps() {
        if(checkTemps == null) {
            checkTemps = new JCheckBox("Limite en temps");
            checkTemps.setBounds(46, 262, 200, 19);
            checkTemps.addItemListener(e -> {
                if(checkTemps.isSelected()) {
                    nbMinute.setEnabled(true);
                    labelMinute.setEnabled(true);
                }
                else {
                    nbMinute.setEnabled(false);
                    labelMinute.setEnabled(false);
                }
            });
        }
        return checkTemps;
    }
    
    /**
     * Cette methode initialise <code>j1Humain</code>, et le retourne
     * 	
     * @return javax.swing.JRadioButton	
     * @see NouvellePartieWindow#j1Humain
     */    
    private JRadioButton getJ1Humain() {
        if (j1Humain == null) {
            j1Humain = new JRadioButton();
            j1Humain.setBounds(87, 131, 83, 21);
            j1Humain.setText("Humain");
        }
        return j1Humain;
    }
    
    /**
     * Cette methode initialise le JTextField <code>j1Nom</code>, et le retourne.	
     * 	
     * @return javax.swing.JTextField	
     * @see NouvellePartieWindow#j1Nom
     */    
    private JTextField getJ1Nom() {
        if (j1Nom == null) {
            j1Nom = new JTextField();
            j1Nom.setBounds(128, 97, 242, 21);
        }
        return j1Nom;
    }
    /**
     * Cette methode initialise <code>j1Ordinateur</code>,et le retourne.
     * 	
     * @return javax.swing.JRadioButton
     * @see NouvellePartieWindow#j1Ordinateur	
     */    
    private JRadioButton getJ1Ordinateur() {
        if (j1Ordinateur == null) {
            j1Ordinateur = new JRadioButton();
            j1Ordinateur.setBounds(202, 131, 133, 21);
            j1Ordinateur.setText("Ordinateur");
        }
        return j1Ordinateur;
    }
    
    /**
     * Cette methode initialise <code>j1Type</code>, et le retourne
     * @return j1Type
     * @see NouvellePartieWindow#j1Type
     */
    private ButtonGroup getJ1Type() {
        if(j1Type == null) {
            j1Type = new ButtonGroup();
        }
        return j1Type;
    }
    /**
     * Initialise <code>J1Humain</code>, et le retourne
     * 	
     * @return javax.swing.JRadioButton
     * @see NouvellePartieWindow#j1Humain	
     */    
    private JRadioButton getJ2Humain() {
        if (j2Humain == null) {
            j2Humain = new JRadioButton();
            j2Humain.setBounds(87, 222, 83, 21);
            j2Humain.setText("Humain");
        }
        return j2Humain;
    }
    /**
     * Cette methode initialise <code>j2Nom</code>, et le retourne.	
     * 	
     * @return javax.swing.JTextField
     * @see NouvellePartieWindow#j2Nom	
     */    
    private JTextField getJ2Nom() {
        if (j2Nom == null) {
            j2Nom = new JTextField();
            j2Nom.setBounds(133, 190, 242, 21);
        }
        return j2Nom;
    }
    /**
     * Initialise <code>J2Ordinateur</code>, et le retourne
     * 	
     * @return javax.swing.JRadioButton
     * @see NouvellePartieWindow#j2Ordinateur	
     */    
    private JRadioButton getJ2Ordinateur() {
        if (j2Ordinateur == null) {
            j2Ordinateur = new JRadioButton();
            j2Ordinateur.setBounds(202, 219, 133, 21);
            j2Ordinateur.setText("Ordinateur");
        }
        return j2Ordinateur;
    }
    
    /**
     * Cette methode initialise <code>j2Type</code>, et le retourne
     * @return j2Type
     * @see NouvellePartieWindow#j2Type
     */
    private ButtonGroup getJ2Type() {
        if(j2Type == null) {
            j2Type = new ButtonGroup();
        }
        return j2Type;
    }
    /**
     * Cette methode initialise le jpanel, et place dessus tous les elements.
     * 	
     * @return javax.swing.JPanel	
     */    
    private JPanel getJPanel() {
        if (jPanel == null) {
            labelMinute = new JLabel();
            nomJ2 = new JLabel();
            nomJ1 = new JLabel();
            joueur2 = new JLabel();
            joueur1 = new JLabel();
            message = new JLabel();
            jPanel = new JPanel();
            
            jPanel.setLayout(null);
            
            message.setText("Vous etes sur le point de creer une nouvelle partie.");
            message.setBounds(35, 19, 317, 15);
            joueur1.setBounds(24, 62, 200, 19);
            joueur1.setText("Joueur 1 (Blancs) :");
            joueur2.setBounds(24, 159, 200, 19);
            joueur2.setText("Joueur 2 (Noirs) :");
            nomJ1.setBounds(42, 97, 75, 21);
            nomJ1.setText("Nom :");
            nomJ2.setBounds(46, 190, 75, 21);
            nomJ2.setText("Nom :");
            labelMinute.setBounds(86, 289, 109, 21);
            labelMinute.setText("Minute :");
            labelMinute.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
            labelMinute.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
            jPanel.add(message, null);
            jPanel.add(joueur1, null);
            jPanel.add(joueur2, null);
            jPanel.add(nomJ1, null);
            jPanel.add(getJ1Nom(), null);
            getJ1Type().add(getJ1Humain());
            getJ1Type().add(getJ1Ordinateur());
            jPanel.add(getJ1Humain(), null);
            jPanel.add(getJ1Ordinateur(), null);
            jPanel.add(getOkButton(), null);
            jPanel.add(getAnnulButton(), null);
            jPanel.add(nomJ2, null);
            jPanel.add(getJ2Nom(), null);
            getJ2Type().add(getJ2Humain());
            getJ2Type().add(getJ2Ordinateur());
            jPanel.add(getJ2Humain(), null);
            jPanel.add(getJ2Ordinateur(), null);
            jPanel.add(getCheckTemps(),null);
            jPanel.add(getNbMinute(), null);
            jPanel.add(labelMinute, null);
            nbMinute.setEnabled(false);
            labelMinute.setEnabled(false);
        }
        return jPanel;
    }
    
    /**
     * This method initializes jTextField	
     * 	
     * @return javax.swing.JTextField	
     */    
    private JTextField getNbMinute() {
        if (nbMinute == null) {
            nbMinute = new JTextField();
            nbMinute.setBounds(202, 289, 41, 21);
        }
        return nbMinute;
    }
    /**
     * Cette methode initialise <code>okButton</code>, et le retourne.	
     * 	
     * @return javax.swing.JButton	
     * @see NouvellePartieWindow#okButton
     */    
    private JButton getOkButton() {
        if (okButton == null) {
            okButton = new JButton();
            okButton.setMnemonic(KeyEvent.VK_O);
            okButton.setBounds(45, 333, 116, 26);
            okButton.setText("OK");
            okButton.addActionListener(e -> {
                if(creeNouvellePartie()) 
                    dispose();
            });
        }
        return okButton;
    }
    
    /**
     * Cette methode initialise la fenetre.
     */
    private void initialize() {
        
        this.setTitle("Nouvelle Partie");
        this.setSize(width, height);
        
        this.setLocation((int) ( parent.getLocation().getX() + (parent.getSize().getWidth()-width)/2 ),
                (int) ( parent.getLocation().getY() + (parent.getSize().getHeight() - height)/2) );
        this.setResizable(false);
        this.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        this.setContentPane(getJPanel());
        this.setVisible(true);
        this.setPreferredSize(new java.awt.Dimension(width, height));
        this.setMinimumSize(new java.awt.Dimension(width, height));
        this.setMaximumSize(new java.awt.Dimension(width,height));
        
    }
    
    /**
     * Methode qui verifie que le <code>nom</code> est correct. Un nom est correct s'il a au moins un
     * caractere et ne contient pas les caracteres , [ ] ou |.
     * @param nom Nom que l'on souhaite verifier
     * @return true si le nom est correct, false sinon
     */
    private boolean nomCorrect(String nom) {
        
        if(nom.equals("")) {
            JOptionPane.showMessageDialog(null, "Veuillez remplir tous les champs.","Jeu De Dames", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        if( nom.indexOf(',') != -1 || nom.indexOf('|')!=-1 || nom.indexOf('[')!=-1 || nom.indexOf(']')!=-1 ) {
            JOptionPane.showMessageDialog(null, "Les caracteres : , [ ] et | sont interdits pour les noms des joueurs.","Jeu De Dames", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        return true;
    }
}