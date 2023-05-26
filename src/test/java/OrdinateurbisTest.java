package jeuDeDames;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Vector;

public class OrdinateurbisTest {

    private Ordinateur ordinateur;
    private Joueur adversaire;
    private Plateau plateau;
    private Arbitre arbitre;
    private JoueurPanel joueurPanel;

    @BeforeEach
    public void setUp() {
        adversaire = mock(Joueur.class);
        plateau = mock(Plateau.class);
        arbitre = mock(Arbitre.class);
        joueurPanel = mock(JoueurPanel.class);
        when(plateau.getArbitre()).thenReturn(arbitre);

        ordinateur = new Ordinateur("Test Player",1, plateau);
        ordinateur.setPanel(joueurPanel);
        ordinateur.setAdversaire(adversaire); // set the opponent
    }

    @Test
    public void testJouer_withMandatoryMove() {
        // Set up mandatory move
        Vector<Rafle> coup = new Vector<>();
        
        // Create mock Rafle objects for the 'coup' vector.
        Rafle r1 = mock(Rafle.class);
        Rafle r2 = mock(Rafle.class);
        
        // Create mock CaseNoire and Piece objects.
        CaseNoire caseNoire1 = mock(CaseNoire.class);
        CaseNoire caseNoire2 = mock(CaseNoire.class);
        Piece piece = mock(Piece.class);
        
        // Set the return values of Rafle's getCaseDebut and getPrise methods.
        when(r1.getCaseDebut()).thenReturn(caseNoire1);
        when(r2.getPrise()).thenReturn(caseNoire2);

        // Set the return values of Plateau's get method.
        when(plateau.get(caseNoire1)).thenReturn(caseNoire1);
        when(plateau.get(caseNoire2)).thenReturn(caseNoire2);
        
        // Set the return values of Case's getPiece method.
        when(caseNoire1.getPiece()).thenReturn(piece);
        
        // Adding Rafle objects to 'coup' vector
        coup.add(r1);
        when(r1.getRaflesSuivantes(0)).thenReturn(r2);

        when(plateau.isObligatoire()).thenReturn(true);
        when(arbitre.getCoupObligatoire()).thenReturn(coup);
        
        assertTrue(ordinateur.jouer());

        // Assertions to verify that the state of the game is as expected after the move
        verify(joueurPanel, times(1)).setActif(true);
        verify(adversaire, times(1)).setActif(false);
    }


    @Test
    public void testJouer_withNoMandatoryMove() {
        // Set up pieces
        Piece piece1 = mock(Piece.class);
    
        // Create mock CaseNoire object.
        CaseNoire caseNoire = mock(CaseNoire.class);
        
        // Mocking casePossible method of the piece
        // Assuming that for this piece, a valid non-mandatory move is possible
        when(piece1.casePossible()).thenReturn(caseNoire);
    
        // Set the return value of Plateau's get method.
        when(plateau.get(caseNoire)).thenReturn(caseNoire);
    
        // Set the return value of Case's getPiece method.
        when(caseNoire.getPiece()).thenReturn(piece1);
    
        // Add the piece to the computer's pieces
        ordinateur.addPiece(piece1);
    
        when(plateau.isObligatoire()).thenReturn(false);
    
        assertTrue(ordinateur.jouer());
    
        // Assertions to verify that the state of the game is as expected after the move
        verify(joueurPanel, times(1)).setActif(true);
        verify(adversaire, times(1)).setActif(false);
    }
    


    @Test
    public void testJouer_withNoAvailableMove() {
        // Set up pieces
        Vector<Piece> pieces = new Vector<>();

        // Assuming Piece objects are created and added to the 'pieces' vector here.
        // These objects should represent the pieces owned by the computer player.

        // Add each piece to the ordinateur object through its public API
        for (Piece piece : pieces) {
            // Mocking casePossible method of each piece
            // Assuming that for each piece, there is no available move
            when(piece.casePossible()).thenReturn(null);

            ordinateur.addPiece(piece);  // Assuming that addPiece method exists
        }

        when(plateau.isObligatoire()).thenReturn(false);

        assertFalse(ordinateur.jouer());

        // Assertions to verify that the state of the game is as expected after the move
        // This could involve checking the positions of pieces, etc.
        verify(joueurPanel, times(1)).setActif(true);
        verify(adversaire, times(1)).setActif(false);
    }


    @Test
    public void testJouer_activeInactiveState() {
        // Mock the required methods
        when(plateau.isObligatoire()).thenReturn(false);

        Vector<Piece> pieces = new Vector<>();

        // Assuming Piece objects are created and added to the 'pieces' vector here.
        // These objects should represent the pieces owned by the computer player.

        // Add each piece to the ordinateur object through its public API
        for (Piece piece : pieces) {
            // Mocking casePossible method of each piece
            // Assuming that for each piece, there is no available move
            when(piece.casePossible()).thenReturn(null);

            ordinateur.addPiece(piece);  // Assuming that addPiece method exists
        }

        // Try to play
        boolean result = ordinateur.jouer();

        // Since there are no possible moves, jouer() should return false
        assertFalse(result);

        // Even though the play was unsuccessful, the player should be set as active and the adversary as inactive
        verify(joueurPanel, times(1)).setActif(true);
        verify(adversaire, times(1)).setActif(false);
    }

}
