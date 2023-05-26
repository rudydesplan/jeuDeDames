package jeuDeDames;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class HistoriqueCoupTest {

    private HistoriqueCoup historiqueCoup;
    private Joueur joueur;
    private Plateau plateau;
    private CaseNoire caseNoire;

    @BeforeEach
    public void setUp() {
        plateau = new Plateau();
        joueur = new Humain("TestPlayer", Piece.NOIR, plateau);
        caseNoire = new CaseNoire(0, 0, plateau);
        historiqueCoup = new HistoriqueCoup();
    }

    @Test
    public void testAddCoup() {
        assertEquals(0, historiqueCoup.getNbCoups());

        Rafle rafle = new Rafle(caseNoire, null, joueur); 
        historiqueCoup.addCoup(rafle);

        assertEquals(1, historiqueCoup.getNbCoups());
        assertSame(rafle, historiqueCoup.get(0));
    }

    @Test
    public void testClear() {
        Rafle rafle = new Rafle(caseNoire, null, joueur);
        historiqueCoup.addCoup(rafle);

        historiqueCoup.clear();

        assertEquals(0, historiqueCoup.getNbCoups());
    }

    @Test
    public void testGet() {
        Rafle rafle = new Rafle(caseNoire, null, joueur);
        historiqueCoup.addCoup(rafle);

        assertSame(rafle, historiqueCoup.get(0));
        assertNull(historiqueCoup.get(-1));
        assertNull(historiqueCoup.get(1));
    }

    @Test
    public void testGetDernierCoup() {
        assertEquals(-1, historiqueCoup.getDernierCoup(joueur));

        Rafle rafle = new Rafle(caseNoire, null, joueur);
        historiqueCoup.addCoup(rafle);

        assertEquals(0, historiqueCoup.getDernierCoup(joueur));
    }

    @Test
    public void testRemove() {
        Rafle rafle = new Rafle(caseNoire, null, joueur);
        historiqueCoup.addCoup(rafle);

        Rafle removedRafle = historiqueCoup.remove(0);
        assertEquals(0, historiqueCoup.getNbCoups());
        assertSame(rafle, removedRafle);
    }

    @Test
    public void testToString() {
        Rafle rafle = new Rafle(caseNoire, null, joueur);
        historiqueCoup.addCoup(rafle);

        String expected = rafle.toString() + "\n";
        assertEquals(expected, historiqueCoup.toString());
    }
}
