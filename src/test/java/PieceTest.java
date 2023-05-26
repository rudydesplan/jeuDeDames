package jeuDeDames;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PieceTest {

    private Piece piece;
    private Joueur joueur;
    private CaseNoire caseNoire;
    private Plateau plateau;

    @BeforeEach
    public void setup() {
        // Assuming we have a class extending Piece called TestPiece for testing purposes
        joueur = new Humain("TestPlayer", Piece.NOIR, plateau);
        caseNoire = new CaseNoire(0, 0, plateau);
        plateau = new Plateau();
        piece = new Pion(Piece.BLANC, caseNoire, plateau, joueur);
    }

    @Test
    public void testGetCouleur() {
        assertEquals(Piece.BLANC, piece.getCouleur());
    }

    @Test
    public void testGetJoueur() {
        assertEquals(joueur, piece.getJoueur());
    }

    @Test
    public void testGetPosition() {
        assertEquals(caseNoire, piece.getPosition());
    }

    @Test
    public void testDeplacer() {
        CaseNoire newCase = new CaseNoire(0, 2, plateau);
        piece.deplacer(newCase);
        assertEquals(newCase, piece.getPosition());
    }

    @Test
    public void testSetJoueur() {
        Joueur newJoueur = new Humain("TestPlayer2", Piece.NOIR, plateau);
        piece.setJoueur(newJoueur);
        assertEquals(newJoueur, piece.getJoueur());
    }

}
