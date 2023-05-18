/*
 *****************************************************************************
 *                         APropos.java  -  description                  
 *                            -------------------                        
 *   begin                : 18 mai. 2023                                      
 *   copyright            : (C) 2023 by Rudy Desplan
 *   email                :                  
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

import java.awt.Color;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
/**
 * Classe qui definit la fenetre A Propos. On y affiche quelques informations relatives au projet.
 * @author rudy
 *
 */
public class APropos extends JDialog {
    /**
     * Hauteur de la fenetre
     */
    private int height = 375;
    
    private JButton jButton = null;
    
    private javax.swing.JPanel jContentPane = null;
    private JLabel jLabel = null;
    private JLabel jLabel1 = null;
    private JLabel jLabel10 = null;
    private JLabel jLabel2 = null;
    private JLabel jLabel3 = null;
    private JLabel jLabel4 = null;
    private JLabel jLabel5 = null;
    private JLabel jLabel6 = null;
    private JLabel jLabel7 = null;
    private JLabel jLabel8 = null;
    private JLabel jLabel9 = null;
    /**
     * Fenetre parente
     */
    private JeuDeDamesWindow parent;
    /**
     * Largeur de la fenetre
     */
    private int width = 400;
    
    /**
     * This is the default constructor
     * @param fenetre Fenetre parente
     */
    public APropos(JeuDeDamesWindow fenetre) {
        super();
        parent = fenetre;
        initialize();
    }
    /**
     * This method initializes jButton	
     * 	
     * @return javax.swing.JButton	
     */    
    private JButton getJButton() {
        if (jButton == null) {
            jButton = new JButton();
            jButton.setText("OK");
            jButton.setVerticalAlignment(javax.swing.SwingConstants.CENTER);
            jButton.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
            jButton.setPreferredSize(new java.awt.Dimension(51,26));
            jButton.setHorizontalTextPosition(javax.swing.SwingConstants.TRAILING);
            jButton.setBounds(130, 297, 113, 33);
            jButton.addActionListener(e -> dispose());
        }
        return jButton;
    }
    /**
     * This method initializes jContentPane
     * 
     * @return javax.swing.JPanel
     */
    private javax.swing.JPanel getJContentPane() {
        if(jContentPane == null) {
            JLabel			jLabel9 = new JLabel();
            jLabel10 = new JLabel();
            jLabel8 = new JLabel();
            jLabel2 = new JLabel();
            jLabel1 = new JLabel();
            jLabel7 = new JLabel();
            jLabel6 = new JLabel();
            jLabel5 = new JLabel();
            jLabel4 = new JLabel();
            jLabel3 = new JLabel();
            jLabel = new JLabel();
            jContentPane = new javax.swing.JPanel();
            jContentPane.setBackground(Color.ORANGE);
            jContentPane.setLayout(null);
            jLabel.setBounds(2, 105, 395, 16);
            jLabel.setText("Cree par Rudy ");
            jLabel.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
            jLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            jLabel3.setBounds(2, 121, 393, 18);
            jLabel3.setText("Université de Paris 8 - IED");
            jLabel3.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
            jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            jLabel4.setBounds(4, 76, 395, 16);
            jLabel4.setText("Mai 2023");
            jLabel5.setBounds(3, 138, 394, 22);
            jLabel5.setText("Maitre de projet :  ");
            jLabel5.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
            jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            jLabel6.setBounds(3, 57, 394, 18);
            jLabel6.setText("  Version 1.0");
            jLabel6.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
            jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            jLabel7.setBounds(2, 41, 396, 16);
            jLabel7.setText("Dans le cadre d'un projet Java ");
            jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            jLabel1.setBounds(10, 175, 395, 25);
            jLabel1.setText("This program is free software; you can redistribute it and/or modify");
            jLabel1.setFont(new Font("sans-serif",Font.PLAIN,11));
            jLabel2.setBounds(10, 200, 395, 25);
            jLabel2.setText("it under the terms of the GNU General Public License as published by");
            jLabel2.setFont(new Font("sans-serif",Font.PLAIN,11));
            jLabel8.setBounds(10, 225, 395, 25);
            jLabel8.setText("the Free Software Foundation; either version 2 of the License, or ");
            jLabel8.setFont(new Font("sans-serif",Font.PLAIN,11));
            jLabel9.setBounds(10, 250, 395, 25);
            jLabel9.setText("(at your option) any later version. ");
            jLabel9.setFont(new Font("sans-serif",Font.PLAIN,11));
            jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            jLabel4.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
            jLabel10.setBounds(7, 7, 382, 13);
            jLabel10.setText("Jeu De Dames");
            jLabel10.setFont(new Font("sans-serif",Font.BOLD,15));
            jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            jLabel10.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
            jContentPane.add(getJButton(), null);
            jContentPane.add(jLabel, null);
            jContentPane.add(jLabel6, null);
            jContentPane.add(jLabel5, null);
            jContentPane.add(jLabel7, null);
            jContentPane.add(jLabel4, null);
            jContentPane.add(jLabel3, null);
            jContentPane.add(jLabel1, null);
            jContentPane.add(jLabel2, null);
            jContentPane.add(jLabel8, null);
            jContentPane.add(jLabel9, null);
            jContentPane.add(jLabel10, null);
        }
        return jContentPane;
    }
    /**
     * This method initializes this
     * 
     * 
     */
    private void initialize() {
        this.setTitle("A Propos");
        this.setSize(width, height);
        
        this.setLocation((int) ( parent.getLocation().getX() + (parent.getSize().getWidth()-width)/2 ),
                (int) ( parent.getLocation().getY() + (parent.getSize().getHeight() - height)/2) );
        this.setResizable(false);
        this.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        this.setVisible(true);
        this.setPreferredSize(new java.awt.Dimension(width,350));
        this.setMinimumSize(new java.awt.Dimension(width,350));
        this.setMaximumSize(new java.awt.Dimension(width,height));
        this.setContentPane(getJContentPane());
    }
}
