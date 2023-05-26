package jeuDeDames;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;

/**
 * APropos is a custom dialog window that shows information about the "Jeu De Dames" game.
 * 
 * The information includes:
 * - The title of the game
 * - The creator of the game
 * - The university at which it was developed
 * - The date it was created
 * - The project leader
 * - The version of the game
 * - Licensing details under the GNU General Public License
 * 
 * APropos also contains a "OK" button that, when clicked, disposes the window.
 *
 * The window is displayed in the center of the parent window and its size is not resizable.
 */
public class APropos extends JDialog {

    /**
     * Button that closes the dialog window when clicked.
     */
    private JButton jButton = null;

    /**
     * Content pane of the dialog.
     */
    private javax.swing.JPanel jContentPane = null;

    /**
     * The labels used to display the various information about the game.
     */
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
     * The parent window of this dialog.
     */
    private JeuDeDamesWindow parent;

    /**
     * The height of the dialog window.
     */
    private int height = 375;

    /**
     * The width of the dialog window.
     */
    private int width = 400;
    
    /**
     * A constructor for the APropos class. This constructor initializes the dialog box with details about the game.
     *
     * @param fenetre The parent window (i.e., the main game window).
     */
    public APropos(JeuDeDamesWindow fenetre) {
        super();
        parent = fenetre;
        initialize();
    }

    /**
     * This method creates and returns a JButton with the label "OK". If clicked, it disposes of the current dialog.
     * The button is centered vertically with the text positioned at the top. 
     *
     * @return The JButton to be added to the dialog.
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
     * This method creates and returns a JPanel containing several JLabels with information about the game, 
     * along with an "OK" button. If this JPanel has not been initialized, it will do so by adding all the JLabels 
     * and the JButton. The background of the JPanel is orange.
     *
     * @return The JPanel to be set as the content pane of the dialog.
     */
    private javax.swing.JPanel getJContentPane() {
        if(jContentPane == null) {
            jLabel9 = new JLabel();
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
     * This method initializes the dialog box. It sets the title, size, location, and whether the dialog is resizable. 
     * It also sets the default close operation to DISPOSE_ON_CLOSE, which means the dialog is disposed of when 
     * the user closes it. After setting up these parameters, it makes the dialog visible and sets the content pane 
     * to be the JPanel returned by getJContentPane().
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
