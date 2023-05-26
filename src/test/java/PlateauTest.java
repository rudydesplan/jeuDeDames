package jeuDeDames;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PlateauTest {
    private Plateau plateau;
    private JeuDeDamesWindow fenetre;
    private Arbitre arbitre;

    @BeforeEach
    public void setUp() {
        plateau = new Plateau();
        fenetre = new JeuDeDamesWindow(); // Assuming JeuDeDamesWindow has a default constructor
        arbitre = new Arbitre(plateau, fenetre);  // No need to declare arbitre here anymore
        plateau.setArbitre(arbitre);
    }

    @Test
    public void testGet() {
        int ligne = 5;
        int colonne = 5;
        Case caseTest = plateau.get(ligne, colonne);
        assertNotNull(caseTest);
        assertEquals(caseTest.getLigne(), ligne);
        assertEquals(caseTest.getColonne(), colonne);
    }

    @Test
    public void testGetArbitre() {
        assertNotNull(plateau.getArbitre());
    }

    @Test
    public void testSetActif() {
        plateau.setActif(true);
        CaseNoire caseNoire = new CaseNoire(0, 0, plateau);
        Joueur joueur = new Humain("TestPlayer", Piece.NOIR, plateau); 
        Piece piece = new Pion(Piece.NOIR, caseNoire, plateau, joueur); 

        // Set the active player
        arbitre.setJoueurActif(joueur);
    
        caseNoire.setPiece(piece);
        plateau.caseSelect(caseNoire, piece);
    
        assertEquals(plateau.getCaseSelect(), caseNoire, "Selected case should be equal to the one that was set");
        assertEquals(plateau.getPieceSelec(), piece, "Selected piece should be equal to the one that was set");
    }

    @Test
    public void testSetActifFalse() {
        plateau.setActif(false);
        CaseNoire caseNoire = new CaseNoire(0, 0, plateau);
        Joueur joueur = new Humain("TestPlayer", Piece.NOIR, plateau); 
        Piece piece = new Pion(Piece.NOIR, caseNoire, plateau, joueur); 

        caseNoire.setPiece(piece);
        plateau.caseSelect(caseNoire, piece);

        assertNull(plateau.getCaseSelect(), "No case should be selected as plateau is not active");
        assertNull(plateau.getPieceSelec(), "No piece should be selected as plateau is not active");
    }


    @Test
    public void testSetArbitre() {
        Arbitre newArbitre = new Arbitre(plateau, fenetre);
        plateau.setArbitre(newArbitre);
        assertEquals(plateau.getArbitre(), newArbitre);
    }

    @Test
    public void testVider() {
        plateau.vider();
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                assertNull(plateau.get(i, j).getPiece());
            }
        }
    }
}
