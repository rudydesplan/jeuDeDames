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
 * This class represents the player's panel in the game. It includes several buttons for different actions, such as
 * abandoning the game, canceling a move, proposing a draw, and displays the current time for each player.
 */
public class JoueurPanel extends JPanel {
    
    /**
     * The height of the buttons on the panel.
     */
    private static int heightButton = 20;

    /**
     * The width of the buttons on the panel.
     */
    private static int widthButton = 250;

    /**
     * A button that allows the player to abandon the game.
     */
    private JButton abandon = null;

    /**
     * A button that allows the player to cancel a move.
     */
    private JButton annul = null;

    /**
     * The player to whom this panel belongs.
     */
    private transient Joueur joueur;

    /**
     * A label displaying the name of the player.
     */
    private JLabel nom = null;

    /**
     * A button that allows the player to propose a draw.
     */
    private JButton nul = null;

    /**
     * A label displaying the remaining time for the player.
     */
    private JLabel temps = null;
    
    /**
     * Constructs a new JoueurPanel object for a specific player.
     * @param j Joueur: the player that this panel represents.
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
     * Converts a given time (in seconds) to a string in MM:SS format.
     * @param t int: the time to convert, in seconds.
     * @return String: the time as a string in MM:SS format.
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
     * Hides all buttons (abandon, draw proposal, move cancellation) on the player's panel.
     */
    public void hideButton() {
        abandon.setVisible(false);
        nul.setVisible(false);
        annul.setVisible(false);
    }
    
    /**
     * Initializes the player's panel by adding and setting up components such as labels and buttons.
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
     * Updates the player's name on the panel and indicates if they are the active player.
     * @param b boolean: true if the player is active, false otherwise.
     */
    public void setActif(boolean b) {
        if(b)
            nom.setText("->" + joueur.getNom() + " (" + joueur.getNombrePieces() + ")");
        else 
            nom.setText(joueur.getNom() + " (" + joueur.getNombrePieces() + ")");
    }
    
    /**
     * Updates the displayed timer on the player's panel.
     * @param t int: the current time left for the player, in seconds.
     */
    public void setTimer(int t) {
        temps.setText(getTempsString(t));
    }

    /**
     * Gets the 'Abandon' button from the player's panel.
     *
     * @return JButton: The button which allows the player to abandon the game.
     */
    public JButton getAbandon() {
        return abandon;
    }

    /**
     * Gets the 'Annul' button from the player's panel.
     *
     * @return JButton: The button which allows the player to cancel a move.
     */
    public JButton getAnnul() {
        return annul;
    }

    /**
     * Gets the 'Nom' label from the player's panel.
     *
     * @return JLabel: The label which displays the player's name.
     */
    public JLabel getNom() {
        return nom;
    }

    /**
     * Gets the 'Nul' button from the player's panel.
     *
     * @return JButton: The button which allows the player to propose a draw.
     */
    public JButton getNul() {
        return nul;
    }

    /**
     * Gets the 'Temps' label from the player's panel.
     *
     * @return JLabel: The label which displays the remaining time for the player.
     */
    public JLabel getTemps() {
        return temps;
    }

    /**
     * Gets the 'Joueur' object related to the player's panel.
     *
     * @return Joueur: The player to whom this panel belongs.
     */
    public Joueur getJoueur() {
        return joueur;
    }

    
}