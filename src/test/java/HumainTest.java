package jeuDeDames;
import org.junit.Test;
import org.mockito.Mockito;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class HumainTest {
    
    @Test
    public void testAbandonAdversaire() {
        // TODO
    }
    
    @Test
    public void testAnnulerCoup() {
        // TODO
    }
    
    @Test
    public void testNulAdversaire() {
        // TODO
    }
    
    @Test
    public void testNulRefuse() {
        // TODO
    }

    @Test
    public void testToString() {
        Plateau plateau = new Plateau(); 
        Humain humain = new Humain("TestPlayer", 1, plateau);
        
        assertEquals("Humain:TestPlayer", humain.toString());
    }

    @Test
    public void testJouer() {
        // You need to set up the game state here so that a move is possible
        Plateau plateau = Mockito.mock(Plateau.class); // Use Mockito to create a mock Plateau
        Mockito.when(plateau.isObligatoire()).thenReturn(true); // Mock the isObligatoire method to return true
        
        Humain humain = new Humain("TestPlayer", 1, plateau);

        // Mock JoueurPanel and Joueur (adversary) and set them to humain
        JoueurPanel mockedPanel = Mockito.mock(JoueurPanel.class);
        humain.setPanel(mockedPanel);
        
        Joueur adversaire = Mockito.mock(Joueur.class);
        humain.setAdversaire(adversaire);

        assertTrue(humain.jouer());
        assertTrue(humain.getActif());
    }
}