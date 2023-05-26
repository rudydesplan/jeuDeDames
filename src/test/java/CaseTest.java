package jeuDeDames;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CaseTest {

    private Plateau plateau;
    private Case case1;
    private Case case2;

    @BeforeEach
    public void setUp() {
        plateau = new Plateau();
        case1 = new CaseBlanche(1, 2, plateau);
    }

    @Test
    public void testConstructors() {
        assertEquals(1, case1.getLigne());
        assertEquals(2, case1.getColonne());

        case2 = new CaseBlanche(2,3, plateau);
        assertEquals(2, case2.getLigne());
        assertEquals(3, case2.getColonne());
    }

    @Test
    public void testCompare() {
        Case case3 = new CaseBlanche(1, 2, plateau);
        Case case4 = new CaseBlanche(3, 4, plateau);

        assertTrue(case1.compare(case3));
        assertFalse(case1.compare(case4));
    }

    @Test
    public void testGetColonneAndGetLigne() {
        assertEquals(1, case1.getLigne());
        assertEquals(2, case1.getColonne());
    }

    @Test
    public void testGetPlateau() {
        assertEquals(plateau, case1.getPlateau());
    }

    @Test
    public void testToString() {
        assertEquals("1,2", case1.toString());
    }
}
