package jeuDeDames;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class JoueurTest {

    @Mock
    private Plateau mockPlateau;

    @Mock
    private CaseNoire mockCase;

    @Mock
    private Pion mockPion;

    @Mock
    private Dame mockDame;

    private Joueur joueur;

    @BeforeEach
    public void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);

        // Mock CaseNoire
        CaseNoire mockCase = mock(CaseNoire.class);
        
        // Have getPosition() return mockCase
        when(mockPion.getPosition()).thenReturn(mockCase);
        when(mockDame.getPosition()).thenReturn(mockCase);

        joueur = new Humain("Test", Piece.NOIR, mockPlateau);
        
        JoueurPanel mockPanel = mock(JoueurPanel.class);
        joueur.setPanel(mockPanel); 
    }

    @Test
    public void testConstructor() {
        assertEquals("Test", joueur.getNom());
        assertEquals(Piece.NOIR, joueur.getCouleur());
        assertEquals(0, joueur.getNombrePieces());
    }

    @Test
    public void testAddAndRemovePiece() {
        when(mockPion.getCouleur()).thenReturn(Piece.NOIR);
        joueur.addPiece(mockPion);
        assertEquals(1, joueur.getNombrePieces());
        joueur.removePiece(mockPion);
        assertEquals(0, joueur.getNombrePieces());
    }

    @Test
    public void testDebut() {
        joueur.debut();
        assertEquals(20, joueur.getNombrePieces()); // At the start, each player should have 20 pieces
    }

    @Test
    public void testPromotionAndDepromotion() {
        joueur.addPiece(mockPion);
        joueur.promotion(mockPion);
        assertTrue(joueur.getPiece(joueur.getNombrePieces() - 1) instanceof Dame);

        joueur.addPiece(mockDame);
        joueur.depromotion(mockDame);
        assertTrue(joueur.getPiece(joueur.getNombrePieces() - 1) instanceof Pion);
    }

    @Test
    public void testSettersAndGetters() {
        joueur.setActif(true);
        assertTrue(joueur.getActif());
    
        Joueur adversaire = new Humain("Adversaire", Piece.BLANC, mockPlateau);
        joueur.setAdversaire(adversaire);
        assertEquals(adversaire, joueur.getAdversaire());
    
        JoueurPanel panel = new JoueurPanel(joueur);
        joueur.setPanel(panel);
        assertEquals(panel, joueur.getPanel());
    
        joueur.setTemps(30);
        assertEquals(30, joueur.getTempsRestant());
    }

    @Test
    public void testAbandonAndNul() {
        // Create mock objects
        JeuDeDamesWindow fenetre = mock(JeuDeDamesWindow.class);
        Arbitre arbitre = mock(Arbitre.class);
        Joueur adversaire = mock(Joueur.class);

        // Define the behavior of the mock objects
        when(adversaire.nulAdversaire()).thenReturn(true);

        // Create a real Joueur object for testing
        Joueur joueur = new Humain("Test", Piece.NOIR, mockPlateau);
        joueur.setAdversaire(adversaire);

        // Assign the arbitre to the mock plateau
        when(mockPlateau.getArbitre()).thenReturn(arbitre);

        // Test abandon method
        joueur.abandon();
        // Verify that the Arbitre's abandon method was called once with our test joueur
        verify(arbitre, times(1)).abandon(joueur);

        // Test nul method
        joueur.nul();
        // Verify that the Arbitre's nul method was called once with our test joueur
        verify(arbitre, times(1)).nul(joueur);
    }
}
