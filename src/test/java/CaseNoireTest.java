package jeuDeDames;
import org.junit.jupiter.api.*;

import java.awt.Component;

import static org.junit.jupiter.api.Assertions.*;

class CaseNoireTest {
    Plateau plateau;
    CaseNoire caseNoire;

    @BeforeEach
    void setUp() {
        plateau = new Plateau();
        caseNoire = new CaseNoire(5,5,plateau);
    }

    @Test
    void caseNoire_copy_constructor() {
        CaseNoire copy = new CaseNoire(caseNoire, plateau);
        assertEquals(caseNoire.getLigne(), copy.getLigne());
        assertEquals(caseNoire.getColonne(), copy.getColonne());
        // For a shallow copy
        assertSame(caseNoire.getPiece(), copy.getPiece());
        // For a deep copy
        assertEquals(caseNoire.getPiece(), copy.getPiece());
    }

    @Test
    void add() {
        Piece piece = new Pion(Piece.BLANC, caseNoire, plateau, null);
        caseNoire.add(piece);
        assertEquals(piece, caseNoire.getPiece());
    }

    @Test
    void copie() {
        Plateau newPlateau = new Plateau();
        CaseNoire copied = (CaseNoire) caseNoire.copie(newPlateau);
        assertEquals(caseNoire.getLigne(), copied.getLigne());
        assertEquals(caseNoire.getColonne(), copied.getColonne());
        assertEquals(newPlateau, copied.getPlateau());
        // For a shallow copy
        assertSame(caseNoire.getPiece(), copied.getPiece());
        // For a deep copy
        assertEquals(caseNoire.getPiece(), copied.getPiece());
    }

    @Test
    void getPiece() {
        Piece piece = new Pion(Piece.BLANC, caseNoire, plateau, null);
        caseNoire.add(piece);
        assertEquals(piece, caseNoire.getPiece());
    }

    @Test
    void remove() {
        Piece piece = new Pion(Piece.BLANC, caseNoire, plateau, null);
        caseNoire.add(piece);
        caseNoire.remove();
        assertNull(caseNoire.getPiece());
    }

    @Test
    void setObligatoire() {
        caseNoire.setObligatoire(true);
        assertTrue(caseNoire.isObligatoire());
    }

    @Test
    void setSelect() {
        caseNoire.setSelect(true);
        assertTrue(caseNoire.isSelect());
    }
}
