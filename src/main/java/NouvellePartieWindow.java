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
 * Class representing the "New Game" window of the checkers game.
 * This class extends JDialog and is responsible for creating and managing the new game dialog box,
 * which is used to set up a new checkers game.
 */
public class NouvellePartieWindow extends JDialog{

    /** Button to cancel the creation of the new game. */
    private JButton annulButton = null;

    /** The arbitrator of the checkers game. */
    private transient Arbitre arbitre = null;

    /** Checkbox to select if the game should be time-based. */
    private JCheckBox checkTemps = null;

    /** Height of the dialog box. */
    private int height = 415;

    /** Radio button to select if the first player is human. */
    private JRadioButton j1Humain = null;

    /** Text field to input the first player's name. */
    private JTextField j1Nom = null;

    /** Radio button to select if the first player is a computer. */
    private JRadioButton j1Ordinateur = null;
  
    /** Group of radio buttons to select the type of the first player. */
    private ButtonGroup j1Type = null;

    /** Radio button to select if the second player is human. */
    private JRadioButton j2Humain = null;

    /** Text field to input the second player's name. */
    private JTextField j2Nom = null;

    /** Radio button to select if the second player is a computer. */
    private JRadioButton j2Ordinateur = null;
 
    /** Group of radio buttons to select the type of the second player. */
    private ButtonGroup j2Type = null;

    /** Label for the first player. */
    private JLabel joueur1 = null;

    /** Label for the second player. */
    private JLabel joueur2 = null;

    /** The panel containing all the components of the dialog box. */
    private JPanel jPanel = null;

    /** Label for the minutes field. */
    private JLabel labelMinute = null;

    /** Message to be displayed on the dialog box. */
    private JLabel message = null;

    /** Text field to input the number of minutes for the game. */
    private JTextField nbMinute = null;

    /** Label for the first player's name. */
    private JLabel nomJ1 = null;

    /** Label for the second player's name. */
    private JLabel nomJ2 = null;

    /** Button to confirm the creation of the new game. */
    private JButton okButton = null;

    /** Parent window of the dialog box. */
    private JeuDeDamesWindow parent = null;

    /** Width of the dialog box. */
    private int width = 400;

    /** 
     * Constructor for the NouvellePartieWindow class.
     * @param p The parent window of the dialog box.
     * @param a The arbitrator of the checkers game.
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
     * Creates a new checkers game according to the inputs in the dialog box.
     * @return True if the new game was successfully created, false otherwise.
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
     * Overridden dispose method from JDialog.
     * Restarts the active player's timer when the dialog box is disposed.
     */
    @Override
    public void dispose() {
        super.dispose();
        if(arbitre.getJoueurActif()!=null)
            arbitre.getJoueurActif().startTimer();
    }

    /**
     * Retrieves the cancel button, initializing it if necessary.
     * @return JButton - the cancel button for the GUI.
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
     * Retrieves the time check box, initializing it if necessary.
     * @return JCheckBox - the time check box for the GUI.
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
     * Retrieves the human player 1 radio button, initializing it if necessary.
     * @return JRadioButton - the human player 1 radio button for the GUI.
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
     * Retrieves the player 1 name text field, initializing it if necessary.
     * @return JTextField - the player 1 name text field for the GUI.
     */
    private JTextField getJ1Nom() {
        if (j1Nom == null) {
            j1Nom = new JTextField();
            j1Nom.setBounds(128, 97, 242, 21);
        }
        return j1Nom;
    }

    /**
     * Retrieves the computer player 1 radio button, initializing it if necessary.
     * @return JRadioButton - the computer player 1 radio button for the GUI.
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
     * Retrieves the player 1 type button group, initializing it if necessary.
     * @return ButtonGroup - the player 1 type button group for the GUI.
     */
    private ButtonGroup getJ1Type() {
        if(j1Type == null) {
            j1Type = new ButtonGroup();
        }
        return j1Type;
    }

    /**
     * Retrieves the human player 2 radio button, initializing it if necessary.
     * @return JRadioButton - the human player 2 radio button for the GUI.
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
     * Retrieves the player 2 name text field, initializing it if necessary.
     * @return JTextField - the player 2 name text field for the GUI.
     */
    private JTextField getJ2Nom() {
        if (j2Nom == null) {
            j2Nom = new JTextField();
            j2Nom.setBounds(133, 190, 242, 21);
        }
        return j2Nom;
    }
   
    /**
     * Retrieves the computer player 2 radio button, initializing it if necessary.
     * @return JRadioButton - the computer player 2 radio button for the GUI.
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
     * Retrieves the player 2 type button group, initializing it if necessary.
     * @return ButtonGroup - the player 2 type button group for the GUI.
     */
    private ButtonGroup getJ2Type() {
        if(j2Type == null) {
            j2Type = new ButtonGroup();
        }
        return j2Type;
    }

    /**
     * Retrieves the JPanel, initializing it if necessary.
     * @return JPanel - the main panel for the GUI.
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
     * Retrieves the number of minutes text field, initializing it if necessary.
     * @return JTextField - the number of minutes text field for the GUI.
     */
    private JTextField getNbMinute() {
        if (nbMinute == null) {
            nbMinute = new JTextField();
            nbMinute.setBounds(202, 289, 41, 21);
        }
        return nbMinute;
    }

    /**
     * Retrieves the OK button, initializing it if necessary.
     * @return JButton - the OK button for the GUI.
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
     * Initializes the game board.
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
     * Checks if the provided name is correct according to the application's rules. 
     * The name is considered correct if it's not empty and doesn't contain the characters ',', '|', '[' or ']'.
     *
     * @param nom - the name to be checked.
     * @return boolean - returns true if the name is correct, false otherwise.
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