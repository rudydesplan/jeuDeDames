package jeuDeDames;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;


public class OrdinateurTest {

    Plateau plateau;
    Ordinateur ordinateur;

    @Before
    public void setUp() {
        // Initialize these objects as per the game's requirements
        plateau = new Plateau();
        ordinateur = new Ordinateur("TestPlayer", 1, plateau);
    }

    @Test
    public void testConstructor() {
        // Assert that the fields are correctly set
        assertEquals("TestPlayer", ordinateur.getNom());
        assertEquals(1, ordinateur.getCouleur());
        assertEquals(plateau, ordinateur.getPlateau());
    }

    @Test
    public void testAnnulerCoup() {
        // Assert that annulerCoup() returns true
        assertTrue(ordinateur.annulerCoup());
    }

    @Test
    public void testNulAdversaire() {
        // Assert that nulAdversaire() returns true
        assertTrue(ordinateur.nulAdversaire());
    }

    @Test
    public void testJouer() {
        // Create a new Ordinateur object and a game state
        // Test different scenarios for jouer():
        // - A valid move is available
        // - No move is possible
        // You may need to mock the Arbitre and Piece classes
    }

    @Test
    public void testSetPanel() {
        // Create a JoueurPanel
        JoueurPanel joueurPanel = new JoueurPanel(ordinateur);

        // Call setPanel() 
        ordinateur.setPanel(joueurPanel);

        // Assert that the panel is correctly set
        assertEquals(joueurPanel, ordinateur.getPanel()); // Assuming getPanel() exists in superclass

        // Assert that all buttons are hidden
        assertFalse(joueurPanel.getAbandon().isVisible());
        assertFalse(joueurPanel.getAnnul().isVisible());
        assertFalse(joueurPanel.getNul().isVisible());
    }

    @Test
    public void testToString() {
        // Create a new Ordinateur object
        Plateau plateau = new Plateau();
        Ordinateur ordinateur = new Ordinateur("TestPlayer", 1, plateau);

        // Assert that toString() returns the expected string
        assertEquals("Ordinateur:TestPlayer", ordinateur.toString());
    }
}
