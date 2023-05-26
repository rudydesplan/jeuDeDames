package jeuDeDames;

import org.junit.jupiter.api.*;
import org.mockito.Mock;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.awt.Graphics;

public class CaseBlancheTest {

    @Mock
    Graphics g;
    
    @Mock
    Plateau p;
    
    CaseBlanche caseBlanche;
    
    @BeforeEach
    public void setup() {
        caseBlanche = new CaseBlanche(1, 1, p);
    }
    
    @Test
    public void testConstructor() {
        assertEquals(1, caseBlanche.getLigne());
        assertEquals(1, caseBlanche.getColonne());
        assertEquals(p, caseBlanche.getPlateau());
    }
    
    @Test
    public void testCopie() {
        Case copiedCase = caseBlanche.copie(p);
        assertEquals(caseBlanche.getLigne(), copiedCase.getLigne());
        assertEquals(caseBlanche.getColonne(), copiedCase.getColonne());
    }
    
    @Test
    public void testGetPiece() {
        assertNull(caseBlanche.getPiece());
    }
    
    @Test
    public void testRemove() {
        assertDoesNotThrow(() -> caseBlanche.remove());
    }
    
    @Test
    public void testSetObligatoire() {
        assertDoesNotThrow(() -> caseBlanche.setObligatoire(true));
    }
    
    @Test
    public void testSetSelect() {
        assertDoesNotThrow(() -> caseBlanche.setSelect(true));
    }

    @Test
    public void testIsObligatoire() {
        assertFalse(caseBlanche.isObligatoire());
    }

    @Test
    public void testIsSelect() {
        assertFalse(caseBlanche.isSelect());
    }
}
