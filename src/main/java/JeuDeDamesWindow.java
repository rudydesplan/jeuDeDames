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
 * This class represents the main window of the Checkers game. It manages the game's graphical user interface,
 * including the game board and player panels, as well as menu interactions such as starting a new game, 
 * loading and saving games, and exiting the program.
 */
public class JeuDeDamesWindow extends JFrame {
    
    /**
     * Constant to set the height of the window as a proportion of the screen height.
     * This value represents two thirds of the screen height.
     */
    private static final float hauteur = (float) 2/3;

    /**
     * Constant to set the width of the window as a proportion of the screen width.
     * This value represents two thirds of the screen width.
     */
    private static final float largeur = (float) 2/3;

    /**
     * Constant to set the horizontal position of the window as a proportion of the screen width.
     * This value represents one sixth of the screen width.
     */
    private static final float posX = (float) 1/6;

    /**
     * Constant to set the vertical position of the window as a proportion of the screen height.
     * This value represents one sixth of the screen height.
     */
    private static final float posY = (float) 1/6;

    /**
     * Main method to run the checkers game.
     * @param args Command-line arguments (not used).
     */
    public static void main(String[] args) {
        JeuDeDamesWindow jeuDeDamesWindow = new JeuDeDamesWindow();
        jeuDeDamesWindow.setVisible(true);
    }

    private transient Arbitre arbitre;
    
    private JoueurPanel joueur1Panel = null;

    private JoueurPanel joueur2Panel = null;

    private JPanel pane = null;

    private Plateau plateau;
    
    /**
     * Constructs a new JeuDeDamesWindow. Initializes the window settings, creates the menu bar, and sets up the game board.
     */
    public JeuDeDamesWindow() {
        //Initialisation de la fenetre
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
     * Adds a panel for player 1 to the window.
     * @param p JoueurPanel: the panel to add.
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
     * Adds a panel for player 2 to the window.
     * @param p JoueurPanel: the panel to add.
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
     * Opens the "About" window.
     */
    public void aPropos() {
        APropos aProposWindow = new APropos(this);
        aProposWindow.setVisible(true);
    }

    /**
     * Opens a file chooser to load a saved game.
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
     * Opens a new window to start a new game.
     */
    public void nouvellePartie() {
        NouvellePartieWindow nouvellePartieWindow = new NouvellePartieWindow(this,arbitre);
    }

    /**
     * Exits the program.
     */
    public void quitter() {
        System.exit(0);
    }
    
    /**
     * Opens a file chooser to save the current game.
     * @see Arbitre#sauver
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