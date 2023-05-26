package jeuDeDames;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.mockito.Mockito;

public class JoueurPanelTest {

    @Test
    public void testGetTempsString() {
        Joueur mockJoueur = Mockito.mock(Joueur.class);
        JoueurPanel joueurPanel = new JoueurPanel(mockJoueur);

        // Test positive values
        assertEquals("01:00", joueurPanel.getTempsString(60));
        assertEquals("02:30", joueurPanel.getTempsString(150));
        assertEquals("10:00", joueurPanel.getTempsString(600));
        assertEquals("00:00", joueurPanel.getTempsString(0));

        // Test negative values
        assertEquals(" ", joueurPanel.getTempsString(-1));
    }

    @Test
    public void testSetTimer() {
        Joueur mockJoueur = Mockito.mock(Joueur.class);
        JoueurPanel joueurPanel = new JoueurPanel(mockJoueur);

        joueurPanel.setTimer(60);
        assertEquals("01:00", joueurPanel.getTemps().getText());
        
        joueurPanel.setTimer(150);
        assertEquals("02:30", joueurPanel.getTemps().getText());
        
        joueurPanel.setTimer(600);
        assertEquals("10:00", joueurPanel.getTemps().getText());
        
        joueurPanel.setTimer(0);
        assertEquals("00:00", joueurPanel.getTemps().getText());
    }

    @Test
    public void testHideButton() {
        Joueur mockJoueur = Mockito.mock(Joueur.class);
        JoueurPanel joueurPanel = new JoueurPanel(mockJoueur);

        joueurPanel.hideButton();

        assertFalse(joueurPanel.getAbandon().isVisible());
        assertFalse(joueurPanel.getNul().isVisible());
        assertFalse(joueurPanel.getAnnul().isVisible());
    }

    @Test
    public void testSetActif() {
        Joueur mockJoueur = Mockito.mock(Joueur.class);
        Mockito.when(mockJoueur.getNom()).thenReturn("Player1");
        Mockito.when(mockJoueur.getNombrePieces()).thenReturn(10);
        JoueurPanel joueurPanel = new JoueurPanel(mockJoueur);

        joueurPanel.setActif(true);
        assertEquals("->Player1 (10)", joueurPanel.getNom().getText());

        joueurPanel.setActif(false);
        assertEquals("Player1 (10)", joueurPanel.getNom().getText());
    }

    @Test
    public void testJoueurPanelConstructor() {
        Joueur mockJoueur = Mockito.mock(Joueur.class);
        Mockito.when(mockJoueur.getNom()).thenReturn("Player1");
        JoueurPanel joueurPanel = new JoueurPanel(mockJoueur);

        assertEquals(mockJoueur, joueurPanel.getJoueur());
        assertEquals("Player1", joueurPanel.getNom().getText());
    }

    @Test
    public void testInitialise() {
        Joueur mockJoueur = Mockito.mock(Joueur.class);
        Mockito.when(mockJoueur.getNom()).thenReturn("Player1");
        JoueurPanel joueurPanel = new JoueurPanel(mockJoueur);
        
        assertEquals("Abandonner", joueurPanel.getAbandon().getText());
        assertEquals("Annuler coup", joueurPanel.getAnnul().getText());
        assertEquals("Proposer nul", joueurPanel.getNul().getText());
        assertEquals("Player1", joueurPanel.getNom().getText());
        assertEquals("", joueurPanel.getTemps().getText());
    }

}
