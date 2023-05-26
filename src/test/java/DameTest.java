package jeuDeDames;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import java.awt.Graphics;
import java.awt.Color;

public class DameTest {

    private Joueur joueur;
    private Plateau plateau;
    private CaseNoire caseNoire;

    @BeforeEach
    public void setUp() {
        plateau = new Plateau();
        joueur = new Humain("TestPlayer", Piece.NOIR, plateau);
        caseNoire = new CaseNoire(0, 0, plateau);
    }

    @Test
    public void testDameConstructor() {
        CaseNoire caseNoire = new CaseNoire(1, 2, plateau);

        // Testing the first constructor
        Dame originalDame = new Dame(1, caseNoire, plateau, joueur);
        assertEquals(1, originalDame.getCouleur());
        assertEquals(caseNoire, originalDame.getPosition());
        assertEquals(plateau, originalDame.getPlateau());
        assertEquals(joueur, originalDame.getJoueur());
        assertEquals(0, originalDame.getDirection());

        // Testing the second constructor
        Dame copiedDame = new Dame(originalDame, caseNoire);
        assertEquals(originalDame.getCouleur(), copiedDame.getCouleur());
        assertEquals(caseNoire, copiedDame.getPosition());
        assertEquals(originalDame.getPlateau(), copiedDame.getPlateau());
        assertEquals(originalDame.getDirection(), copiedDame.getDirection());
    }

    
    @Test
    public void testCopie() {

        CaseNoire initialPosition = new CaseNoire(5, 5, plateau);
        Dame dame = new Dame(1, initialPosition, plateau, joueur);
        initialPosition.setPiece(dame);
        
        // Testing copie
        CaseNoire newCase = new CaseNoire(4, 4, plateau);
        Dame copiedDame = (Dame)dame.copie(newCase);
        // The copied Dame should have the same properties but a different position
        assertEquals(copiedDame.getCouleur(), dame.getCouleur());
        assertEquals(copiedDame.getPlateau(), dame.getPlateau());
        assertEquals(copiedDame.getPosition(), newCase);
    }

    @Test
    public void testCoupPossible() {

        // Set up a board with no pieces, so all moves are possible
        for(int i = 0; i < 10; i++) {
            for(int j = 0; j < 10; j++) {
                CaseNoire caseNoire = new CaseNoire(i, j, plateau);
                caseNoire.setPiece(null);
                plateau.setCase(caseNoire);
            }
        }

        CaseNoire initialPosition = new CaseNoire(5, 5, plateau);
        Dame dame = new Dame(1, initialPosition, plateau, joueur);
        initialPosition.setPiece(dame);

        // Since the board is empty, a move should be possible in every direction
        assertTrue(dame.coupPossible());

        // Now, put a piece in the way in one direction
        CaseNoire obstaclePosition1 = new CaseNoire(6, 6, plateau);
        Dame obstacle1 = new Dame(2, obstaclePosition1, plateau, joueur);
        obstaclePosition1.setPiece(obstacle1);

        // A move should still be possible, since other directions are open
        assertTrue(dame.coupPossible());

    }


    @Test
    public void testCoupPossibleDirection() {

        // Set up a board with no pieces, so all moves are possible
        for(int i = 0; i < 10; i++) {
            for(int j = 0; j < 10; j++) {
                CaseNoire caseNoire = new CaseNoire(i, j, plateau);
                caseNoire.setPiece(null);
                plateau.setCase(caseNoire);
            }
        }

        CaseNoire initialPosition = new CaseNoire(5, 5, plateau);
        Dame dame = new Dame(1, initialPosition, plateau, joueur);
        initialPosition.setPiece(dame);

        // Test all directions
        int[] directions = {-1, 0, 1};
        for(int dx : directions) {
            for(int dy : directions) {
                if(dx != 0 || dy != 0) {  // ignore the no movement case
                    assertTrue(dame.coupPossibleDirection(dx, dy));
                }
            }
        }

    }

    @Test
    public void testIsCoupValide() {

        // Setting up a board with no pieces, so all moves are possible
        for(int i = 0; i < 10; i++) {
            for(int j = 0; j < 10; j++) {
                CaseNoire caseNoire = new CaseNoire(i, j, plateau);
                caseNoire.setPiece(null);
                plateau.setCase(caseNoire);
            }
        }

        CaseNoire initialPosition = new CaseNoire(5, 5, plateau);
        Dame dame = new Dame(1, initialPosition, plateau, joueur);
        initialPosition.setPiece(dame);

        // Valid move: moving to an open space
        CaseNoire targetPosition = new CaseNoire(6, 6, plateau);
        Rafle rafle = new Rafle(initialPosition, null, joueur);
        rafle.addCasesSuivantes(targetPosition);
        assertTrue(dame.isCoupValide(rafle));

        // Invalid move: moving off the board
        targetPosition = new CaseNoire(10, 10, plateau);
        rafle = new Rafle(initialPosition, null, joueur);
        rafle.addCasesSuivantes(targetPosition);
        assertFalse(dame.isCoupValide(rafle));
    }



    @Test
    public void testPaintComponent() {
        // 1. Instantiate a Dame object.
        Dame dame = new Dame(1, caseNoire, plateau, joueur);  // color is set to 1
    
        // 2. Create a mock Graphics object using Mockito.
        Graphics gMock = mock(Graphics.class);
    
        // 3. Call the paintComponent(Graphics g) method on the Dame object, 
        // passing in the mock Graphics object.
        dame.paintComponent(gMock);
    
        // 4. Verify that the setColor(Color c) and fillOval(int x, int y, int width, int height)
        // methods are called on the mock Graphics object with the expected arguments.
    
        // color calculation
        int color = dame.couleur * 75 + 150;
        // size of the oval
        int cote = Case.TAILLE - 10;
        
        // Check that setColor was called with the expected color
        verify(gMock).setColor(new Color(color, color, color));
        // Check that fillOval was called with the expected size and location for the large oval
        verify(gMock).fillOval(dame.x - cote / 2, dame.y - cote / 2, cote, cote);
        // Check that setColor was called with Color.RED
        verify(gMock).setColor(Color.RED);
        // Check that fillOval was called with the expected size and location for the small red oval
        verify(gMock).fillOval(cote / 2 - 5, cote / 2 - 5, 10, 10);
    }

    @Test
    public void testPromotion() {
        // Instantiate a Dame object.
        Dame dame = new Dame(1, caseNoire, plateau, joueur);  // color is set to 1

        // Call the promotion() method and assert the result is false
        assertFalse(dame.promotion(), "The promotion method should always return false");
    }

    // public Rafle coupObligatoire(CaseNoire prise) TODO
    // private int prisePossible TODO
}
