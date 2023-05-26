package jeuDeDames;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class RafleTest {

    @Mock
    private CaseNoire caseDebutMock;

    @Mock
    private CaseNoire casePriseMock;

    @Mock
    private Joueur joueurMock;

    @Mock
    private Piece pieceMock;

    @Mock
    private Plateau plateauMock;

    private Rafle rafleUnderTest;

    @Before
    public void setUp() {
        when(casePriseMock.getPiece()).thenReturn(pieceMock);
        rafleUnderTest = new Rafle(caseDebutMock, casePriseMock, joueurMock);
    }

    @Test
    public void testConstructor() {
        // Assert that the constructor has correctly initialized the properties.
        assertEquals(caseDebutMock, rafleUnderTest.getCaseDebut());
        assertEquals(casePriseMock, rafleUnderTest.getPrise());
        assertEquals(joueurMock, rafleUnderTest.getJoueur());
        assertEquals(pieceMock, rafleUnderTest.getPiecePrise());
    }

    @Test
    public void testAddCasesSuivantes() {
        CaseNoire newCaseMock = mock(CaseNoire.class);
        rafleUnderTest.addCasesSuivantes(newCaseMock);
        assertEquals(newCaseMock, rafleUnderTest.getCasesSuivantes(0));
    }

    @Test
    public void testAddRaflesSuivantes() {
        Rafle newRafleMock = mock(Rafle.class);
        rafleUnderTest.addRaflesSuivantes(newRafleMock);
        assertEquals(newRafleMock, rafleUnderTest.getRaflesSuivantes(0));
    }

    @Test
    public void testClearCasesSuivantes() {
        rafleUnderTest.clearCasesSuivantes();
        assertEquals(0, rafleUnderTest.getNbCasesSuivantes());
    }

    @Test
    public void testSetCaseDebut() {
        CaseNoire newCaseMock = mock(CaseNoire.class);
        rafleUnderTest.setCaseDebut(newCaseMock);
        assertEquals(newCaseMock, rafleUnderTest.getCaseDebut());
    }

    @Test
    public void testSetCasePrise() {
        CaseNoire newCaseMock = mock(CaseNoire.class);
        Piece newPieceMock = mock(Piece.class);
        when(newCaseMock.getPiece()).thenReturn(newPieceMock);
        rafleUnderTest.setCasePrise(newCaseMock);
        assertEquals(newCaseMock, rafleUnderTest.getPrise());
        assertEquals(newPieceMock, rafleUnderTest.getPiecePrise());
    }

    @Test
    public void testSetJoueur() {
        Joueur newJoueurMock = mock(Joueur.class);
        rafleUnderTest.setJoueur(newJoueurMock);
        assertEquals(newJoueurMock, rafleUnderTest.getJoueur());
    }

    @Test
    public void testSetNbPrises() {
        rafleUnderTest.setNbPrises(5);
        assertEquals(5, rafleUnderTest.getNbPrises());
    }

    @Test
    public void testSetPiecePrise() {
        Piece newPieceMock = mock(Piece.class);
        rafleUnderTest.setPiecePrise(newPieceMock);
        assertEquals(newPieceMock, rafleUnderTest.getPiecePrise());
    }

    @Test
    public void testSetRaflesSuivantes() {
        Rafle newRafleMock1 = mock(Rafle.class);
        Rafle newRafleMock2 = mock(Rafle.class);
        rafleUnderTest.addRaflesSuivantes(newRafleMock1);
        rafleUnderTest.setRaflesSuivantes(0, newRafleMock2);
        assertEquals(newRafleMock2, rafleUnderTest.getRaflesSuivantes(0));
    }

    @Test
    public void testMisAJourCoups() {
        // Create a mock for Plateau
        Plateau mockPlateau = mock(Plateau.class);

        // Create mocks for CaseNoire
        CaseNoire mockCaseDebut = mock(CaseNoire.class);
        CaseNoire mockCasePrise = mock(CaseNoire.class);
        
        // Setup Rafle to use the mock CaseNoire instances
        rafleUnderTest.setCaseDebut(mockCaseDebut);
        rafleUnderTest.setCasePrise(mockCasePrise);
        
        // Setup the Plateau to return our mock CaseNoire instances
        when(mockPlateau.get(mockCaseDebut)).thenReturn(mockCaseDebut);
        when(mockPlateau.get(mockCasePrise)).thenReturn(mockCasePrise);
        
        // Call the method under test
        rafleUnderTest.misAJourCoups(mockPlateau);

        // Verify that the setObligatoire method was called with true for both CaseNoire instances
        verify(mockCaseDebut).setObligatoire(true);
        verify(mockCasePrise).setObligatoire(true);
    }

    @Test
    public void testEffaceCoups() {
        // Create a mock for Plateau
        Plateau mockPlateau = mock(Plateau.class);

        // Create mocks for CaseNoire
        CaseNoire mockCaseDebut = mock(CaseNoire.class);
        CaseNoire mockCasePrise = mock(CaseNoire.class);
        
        // Setup Rafle to use the mock CaseNoire instances
        rafleUnderTest.setCaseDebut(mockCaseDebut);
        rafleUnderTest.setCasePrise(mockCasePrise);
        
        // Setup the Plateau to return our mock CaseNoire instances
        when(mockPlateau.get(mockCaseDebut)).thenReturn(mockCaseDebut);
        when(mockPlateau.get(mockCasePrise)).thenReturn(mockCasePrise);
        
        // Call the method under test
        rafleUnderTest.effaceCoups(mockPlateau);

        // Verify that the setObligatoire method was called with false for both CaseNoire instances
        verify(mockCaseDebut).setObligatoire(false);
        verify(mockCasePrise).setObligatoire(false);
    }

}