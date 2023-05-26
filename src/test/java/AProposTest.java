package jeuDeDames;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AProposTest {
    private APropos aPropos;
    private JeuDeDamesWindow parent;

    @BeforeEach
    void setUp() {
        parent = new JeuDeDamesWindow();
        aPropos = new APropos(parent);
    }

    @Test
    void testDialogProperties() {
        assertEquals("A Propos", aPropos.getTitle());
        assertEquals(new java.awt.Dimension(400, 375), aPropos.getSize());
        assertFalse(aPropos.isResizable());
        assertEquals(javax.swing.WindowConstants.DISPOSE_ON_CLOSE, aPropos.getDefaultCloseOperation());
        assertTrue(aPropos.isVisible());
        assertEquals(new java.awt.Dimension(400, 350), aPropos.getPreferredSize());
        assertEquals(new java.awt.Dimension(400, 350), aPropos.getMinimumSize());
        assertEquals(new java.awt.Dimension(400, 375), aPropos.getMaximumSize());
    }
}
