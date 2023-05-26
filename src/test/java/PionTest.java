package jeuDeDames;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PionTest {
    Pion pion;
    Plateau plateau;
    Joueur joueur;
    CaseNoire caseNoire;

    @BeforeEach
    void setUp() {
        // Initialize these objects as per the game's requirements
        plateau = new Plateau();
        joueur = new Humain("TestPlayer", Piece.NOIR, plateau);
        caseNoire = new CaseNoire(0, 0, plateau);
        pion = new Pion(Piece.BLANC, caseNoire, plateau, joueur);
    }

    @Test
    void testConstructor() {
        assertEquals(Piece.BLANC, pion.getCouleur());
        assertEquals(caseNoire, pion.getPosition());
        assertEquals(plateau, pion.getPlateau());
        assertEquals(joueur, pion.getJoueur());
    }

    @Test
    void testCasePossible() {
        CaseNoire newPosition = new CaseNoire(1, 1, plateau);  // updated position
        pion.setPosition(newPosition);
        assertNull(pion.casePossible());
    }

    @Test
    void testCopie() {
        CaseNoire newCase = new CaseNoire(1, 2, plateau);  // Using 1, 2 for example
        Pion pionCopy = (Pion) pion.copie(newCase);
        assertEquals(pion.getCouleur(), pionCopy.getCouleur());      
        assertEquals(pion.getDirection(), pionCopy.getDirection());
        assertNotEquals(pion, pionCopy);
    }

    @Test
    void testCoupObligatoire() {
        // Set up a scenario where a capture is obligatory
        CaseNoire opponentPosition1 = new CaseNoire(1, 1, plateau);
        CaseNoire opponentPosition2 = new CaseNoire(3, 3, plateau); // The position should be two steps away in the direction of capture
        Pion opponentPion = new Pion(Piece.NOIR, opponentPosition1, plateau, new Humain("Opponent", Piece.NOIR, plateau));
        opponentPosition1.setPiece(opponentPion);

        // Expectation: the current Pion should capture the opponent's Pion
        Rafle expectedRafle = pion.coupObligatoire(opponentPosition1);

        // Verify
        assertEquals(expectedRafle.getCaseDebut(), pion.getPosition());
        assertNotNull(expectedRafle.getPiecePrise());
    }


    @Test
    void testCoupPossible() {
        // Set up a scenario where a move is possible
        CaseNoire newPosition = new CaseNoire(1, 1, plateau); // for example
        // newPosition is diagonally adjacent and empty, so it's a possible move.
        
        // Move the pion
        pion.deplacer(newPosition);
        
        // Expectation: the current Pion should find another move as possible
        assertTrue(pion.coupPossible());
    }


    @Test
    void testGetDirection() {
        assertEquals(-1, pion.getDirection());
    }

    @Test
    void testIsCoupValide() {
        // Setup a valid move
        CaseNoire newPosition = new CaseNoire(1, 1, plateau); // for example
        // newPosition is diagonally adjacent and empty, so it's a valid move.
        Rafle move = new Rafle(caseNoire, newPosition, joueur);

        // Verify - TODO: this test is incomplete
       
    }
    

    @Test
    void testPromotion() {
        // Set up a scenario where promotion is possible
        // Move the pawn to the end of the board
        CaseNoire endOfBoardPosition = new CaseNoire(9, 9, plateau);
        pion.setPosition(endOfBoardPosition);
        
        // Verify
        assertTrue(pion.promotion());
    }
    

    @Test
    void testToString() {
        String expectedString = "P(BLANC):" + caseNoire.toString();
        assertEquals(expectedString, pion.toString());
    }
}
