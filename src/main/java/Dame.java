/*
 *****************************************************************************
 *                         Dame.java  -  description                  
 *                            -------------------                        
 *   begin                : 18 mai. 2023                                      
 *   copyright            : (C) 2023 by Rudy Desplan
 *   email                : rudy.desplan@etud.univ-paris8.fr                    
 *****************************************************************************
 
 ***************************************************************************
 *                                                                         *
 *   This program is free software; you can redistribute it and/or modify  *
 *   it under the terms of the GNU General Public License as published by  *
 *   the Free Software Foundation; either version 2 of the License, or     *
 *   (at your option) any later version.                                   *
 *                                                                         *
 ***************************************************************************
 * 
 */

package jeuDeDames;
import java.awt.Color;
import java.awt.Graphics;


/**
 * Classe symbolisant la dame du Jeu de Dame.
 * @author rudy
 */
public class Dame extends Piece {
    
    /**
     * Constructeur prenant en argument une autre Dame. Ce constructeur
     * permet de realiser une copie de <code>d</code>.
     * @param d Dame a copier
     * @param c Case sur laquelle se trouve la piece 
     * 
     */
    public Dame(Dame d,CaseNoire c) {
        super(d,c);
    }
    
    /**
     * Constructeur de la classe Dame
     * @param c Couleur de la piece.
     * @param j Joueur qui possede la piece
     * @param p Plateau sur lequel la piece est dispose
     * @param pos Case (CaseNoire) sur laquelle se trouve la piece.
     */
    public Dame(int c,CaseNoire pos,Plateau p,Joueur j) {
        super(c,pos,p,j);
        direction = 0;
    }
    
    /**
     * Retourne la premiere case vide trouvee ou peut se deplacer la dame.
     * @return premiere case vide ou peut se deplacer la dame.
     */
    public Case casePossible() {
        Case c = null;
        int[] directions = {1, -1};
    
        for (int d1 : directions) {
            for (int d2 : directions) {
                c = casePossibleDirection(d1, d2);
                if (c != null) {
                    return c;
                }
            }
        }
        return null;
    }
    
    
    /**
     * Methode qui retourne la premiere case vide dans la direction dLig,dCol
     * @param dLig Direction d'evolution des lignes
     * @param dCol Direction d'evolution des colonnes
     * @return premiere case possible dans la direction dLig,dCol
     */
    private Case casePossibleDirection(int dLig, int dCol) {
        int ligne = position.getLigne();
        int colonne = position.getColonne();
    
        while(true) {
            ligne += dLig;
            colonne += dCol;
    
            if(ligne < 0 || ligne > 9 || colonne < 0 || colonne > 9 
               || plateau.get(ligne, colonne).getPiece() != null) {
                return null;
            }
    
            return plateau.get(ligne, colonne);
        }
    }
    
    
    /**
     * Retourne un copie de la dame.
     * @param c Case que l'on souhaite copier
     * @return la copie de la dame.
     */
    public Piece copie(CaseNoire c) {
        return new Dame(this,c);
    }
    
    /**
     * Retourne le coup obligatoire pour la dame.
     * @param prise Case ou la piece precedent a ete prise
     * @return le coup obligatoire pour la dame.
     * @see Rafle
     *
     */
    public Rafle coupObligatoire(CaseNoire prise) {
        
        Rafle rafle = new Rafle(position,prise,null);
        int nbPrises = 0;
        
        int ligne = position.getLigne();
        int colonne = position.getColonne();
        
        int iLigne = ligne;
        int iColonne = colonne;
        
        int dLig;
        int dCol;
        
        // On recherche dans les 4 directions une prise possible.
        // Ceci correspond au premier coup de la rafle
        if(prise == null) {
            
            // Direction 1,1
            //nbPrises = rechercherPremierePrise(iLigne,iColonne,1,1,rafle,nbPrises);
            dLig = 1;
            dCol = 1;
            while(true) {
                Piece p = null;
                
                ligne += dLig;
                colonne += dCol;
                
                if(ligne < 1 || ligne > 8)
                    break;
                if(colonne < 1 || colonne > 8)
                    break;
                
                if( ( p = plateau.get(ligne,colonne).getPiece() )!= null) {
                    if(p.getCouleur() != couleur) {
                        if(plateau.get(ligne+dLig,colonne+dCol).getPiece() == null) {
                            // Prise possible
                            deplacer( (CaseNoire) plateau.get(ligne-dLig,colonne-dCol) );
                            Rafle r = coupObligatoire(p.getPosition());
                            r.setCaseDebut( (CaseNoire) plateau.get(iLigne,iColonne));
                            r.setCasePrise(null);
                            // La rafle trouvee est "meilleure"
                            if(r.getNbPrises() > nbPrises) {
                                rafle = r;
                                nbPrises = r.getNbPrises();
                            }
                            else {
                                // la rafle trouvee est equivalente
                                if(nbPrises > 0 && r.getNbPrises() == nbPrises) {
                                    for(int i = 0; i<r.getNbCasesSuivantes();i++) {
                                        rafle.addRaflesSuivantes(r.getRaflesSuivantes(i));
                                    }
                                }
                            }
                            
                        }
                    }
                    break;
                }
                
            }
            
            // Direction -1,1
            //nbPrises = rechercherPremierePrise(iLigne,iColonne,-1,1,rafle,nbPrises);
            dLig = -1;
            dCol = 1;
            ligne = iLigne;
            colonne = iColonne;
            while(true) {
                Piece p = null;
                
                ligne += dLig;
                colonne += dCol;
                
                if(ligne < 1 || ligne > 8)
                    break;
                if(colonne < 1 || colonne > 8)
                    break;
                
                if( ( p = plateau.get(ligne,colonne).getPiece() )!= null) {
                    if(p.getCouleur() != couleur) {
                        if(plateau.get(ligne+dLig,colonne+dCol).getPiece() == null) {
                            // Prise possible
                            deplacer( (CaseNoire) plateau.get(ligne-dLig,colonne-dCol) );
                            Rafle r = coupObligatoire(p.getPosition());
                            r.setCaseDebut( (CaseNoire) plateau.get(iLigne,iColonne));
                            r.setCasePrise(null);
                            // La rafle trouvee est "meilleure"
                            if(r.getNbPrises() > nbPrises) {
                                rafle = r;
                                nbPrises = r.getNbPrises();
                            }
                            else {
                                // la rafle trouvee est equivalente
                                if(nbPrises > 0 && r.getNbPrises() == nbPrises) {
                                    for(int i = 0; i<r.getNbCasesSuivantes();i++) {
                                        rafle.addRaflesSuivantes(r.getRaflesSuivantes(i));
                                    }
                                }
                            }
                            
                        }
                    }
                    break;
                }
                
            }
            
            // Direction -1,-1
            //nbPrises = rechercherPremierePrise(iLigne,iColonne,-1,-1,rafle,nbPrises);
            dLig = -1;
            dCol = -1;
            ligne = iLigne;
            colonne = iColonne;
            while(true) {
                Piece p = null;
                
                ligne += dLig;
                colonne += dCol;
                
                if(ligne < 1 || ligne > 8)
                    break;
                if(colonne < 1 || colonne > 8)
                    break;
                
                if( ( p = plateau.get(ligne,colonne).getPiece() )!= null) {
                    if(p.getCouleur() != couleur) {
                        if(plateau.get(ligne+dLig,colonne+dCol).getPiece() == null) {
                            // Prise possible
                            deplacer( (CaseNoire) plateau.get(ligne-dLig,colonne-dCol) );
                            Rafle r = coupObligatoire(p.getPosition());
                            r.setCaseDebut( (CaseNoire) plateau.get(iLigne,iColonne));
                            r.setCasePrise(null);
                            // La rafle trouvee est "meilleure"
                            if(r.getNbPrises() > nbPrises) {
                                rafle = r;
                                nbPrises = r.getNbPrises();
                            }
                            else {
                                // la rafle trouvee est equivalente
                                if(nbPrises > 0 && r.getNbPrises() == nbPrises) {
                                    for(int i = 0; i<r.getNbCasesSuivantes();i++) {
                                        rafle.addRaflesSuivantes(r.getRaflesSuivantes(i));
                                    }
                                }
                            }
                            
                        }
                    }
                    break;
                }
                
            }
            // Direction 1,-1
            //nbPrises = rechercherPremierePrise(iLigne,iColonne,1,-1,rafle,nbPrises);
            dLig = 1;
            dCol = -1;
            ligne = iLigne;
            colonne = iColonne;
            while(true) {
                Piece p = null;
                
                ligne += dLig;
                colonne += dCol;
                
                if(ligne < 1 || ligne > 8)
                    break;
                if(colonne < 1 || colonne > 8)
                    break;
                
                if( ( p = plateau.get(ligne,colonne).getPiece() )!= null) {
                    if(p.getCouleur() != couleur) {
                        if(plateau.get(ligne+dLig,colonne+dCol).getPiece() == null) {
                            // Prise possible
                            deplacer( (CaseNoire) plateau.get(ligne-dLig,colonne-dCol) );
                            Rafle r = coupObligatoire(p.getPosition());
                            r.setCaseDebut( (CaseNoire) plateau.get(iLigne,iColonne));
                            r.setCasePrise(null);
                            // La rafle trouvee est "meilleure"
                            if(r.getNbPrises() > nbPrises) {
                                rafle = r;
                                nbPrises = r.getNbPrises();
                            }
                            else {
                                // la rafle trouvee est equivalente
                                if(nbPrises > 0 && r.getNbPrises() == nbPrises) {
                                    for(int i = 0; i<r.getNbCasesSuivantes();i++) {
                                        rafle.addRaflesSuivantes(r.getRaflesSuivantes(i));
                                    }
                                }
                            }
                            
                        }
                    }
                    break;
                }
                
            }
        }
        
        // On connait la premiere prise a effectue
        else {
            dLig = prise.getLigne() - position.getLigne();
            dCol = prise.getColonne() - position.getColonne();
            
            ligne += dLig;
            colonne += dCol;
            
            while(true) {
                Piece p = null;
                Case c = null;
                Case c2 = null;
                
                ligne += dLig;
                colonne += dCol;				
                if(ligne<0 || ligne > 9)
                    break;
                if(colonne <0 || colonne > 9)
                    break;
                
                deplacer( (CaseNoire) plateau.get(ligne,colonne));
                
                // Piece prenable sur le cote
                nbPrises = prisePossible(ligne,colonne,dLig,-dCol,rafle,nbPrises,prise);
                
                // Piece prenable sur l'autre cote
                deplacer( (CaseNoire) plateau.get(ligne,colonne));
                nbPrises = prisePossible(ligne,colonne,-dLig,dCol,rafle,nbPrises,prise);
                
                
                // Piece prenable dans la direction ou on avance
                if( (c=plateau.get(ligne+dLig,colonne+dCol)) != null) {
                    if( (p = c.getPiece())!= null)  {
                        if(p.getCouleur() != couleur) {
                            if( (c2 = plateau.get(ligne+2*dLig,colonne+2*dCol)) != null) {
                                if(c2.getPiece() == null) {
                                    Rafle r = coupObligatoire( (CaseNoire) c);
                                    r.setCasePrise(prise);
                                    if(r.getNbPrises() > nbPrises) {
                                        nbPrises = r.getNbPrises();
                                        rafle.clearCasesSuivantes();
                                        rafle.addRaflesSuivantes(r);
                                    }
                                    else {
                                        if(nbPrises>0 && nbPrises == r.getNbPrises()) {
                                            // On doit verifier s'il n'y a pas une rafle equivalente qui commence par la meme case
                                            Rafle r1 = rafle.getRaflesSuivantes(r.getCaseDebut());
                                            if(r1==null)
                                                // Si il n'y a pas on ajoute simplement la nouvelle rafle trouvee
                                                rafle.addRaflesSuivantes(r);
                                            else {
                                                //dans le cas ou une rafle commencant par la meme case existe deja
                                                // on ajoute a cette raflen, les nouvelles rafles suivantes trouves.
                                                for(int i=0;i<r.getNbCasesSuivantes();i++)
                                                    r1.addRaflesSuivantes(r.getRaflesSuivantes(i));
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        break;
                    }
                }
                
            }
            //Si il n'y a pas de prises possibles dans toutes la diagonales, on ajoute
            // aux cases suivantes, toutes les cases de la diagonale.
            if(nbPrises == 0) {
                position.remove();
                iLigne += 2*dLig;
                iColonne += 2*dCol;
                while(true) {
                    Rafle r = new Rafle( (CaseNoire) plateau.get(iLigne,iColonne),null,null);
                    r.setCasePrise(prise);
                    r.setNbPrises(1);
                    rafle.addRaflesSuivantes(r);
                    
                    iLigne += dLig;
                    iColonne += dCol;
                    
                    if(iLigne <0 || iLigne > 9)
                        break;
                    if(iColonne <0 || iColonne >9)
                        break;
                    if(plateau.get(iLigne,iColonne).getPiece()!=null)
                        break;
                }
                nbPrises=1;
            }
        }
        
        if(prise != null) 
            nbPrises++;
        //On doit diminuer de un le nombre de prises totales, car dans l'algorithme, on en ajoute un en plus.
        else nbPrises--;
        
        
        rafle.setNbPrises(nbPrises);
        return rafle;
    }
    /**
     * Methode qui recherche si la dame peut bouger.
     * @return true si la dame peut bouger, false sinon.
     */
    public boolean coupPossible() {
        int[] directions = {1, -1};
        for (int dLig : directions) {
            for (int dCol : directions) {
                if (coupPossibleDirection(dLig, dCol)) {
                    return true;
                }
            }
        }
        return false;
    }
    
    
    /**
     * Renvoit un booleen suivant si un coup est possible pour la dame dans une certaine direction.
     * @param dLig direction d'evolution des lignes
     * @param dCol direction d'evolution des colonnes
     * @return true si la dame peut effectuer un coup, false sinon
     */
    public boolean coupPossibleDirection(int dLig, int dCol) {
        int ligne = position.getLigne();
        int colonne = position.getColonne();
        
        ligne += dLig;
        colonne += dCol;
        
        if(ligne<0 || ligne >9 || colonne<0 || colonne >9)
            return false;
        
        return plateau.get(ligne, colonne).getPiece() == null;
    }
    
    
    /**
     * Methode qui renvoit un boolean indiquant si le Coup <code>coup</code> est valide 
     * pour la dame.
     * @param coup le coup a valide
     * @return true si la coup est bon, false sinon.
     */
    public boolean isCoupValide(Rafle coup) {
        CaseNoire depart = coup.getCaseDebut();
        CaseNoire arrivee = coup.getCasesSuivantes(0);
        
        // Les deux cases sont identiques, c bon
        if(depart == arrivee)
            return true;
        
        int dlig = depart.getLigne();
        int dcol = depart.getColonne();
        
        int alig = arrivee.getLigne();
        int acol = arrivee.getColonne();
        
        int dirCol = (depart.getColonne() > arrivee.getColonne()) ? -1 : 1;
        int dirLig = (depart.getLigne() > arrivee.getLigne()) ? -1 : 1;
    
        while( (dlig != alig) || (dcol != acol) ) {
            dlig += dirLig;
            dcol += dirCol;
            
            if( dlig < 0 || dlig > 9 || dcol <0 || dcol > 9 || plateau.get(dlig,dcol).getPiece()!=null) 
                return false;
        }
        
        return true;
    }
    
    
    /**
     * Methode redefinit de la classe JPanel, permettant de personnaliser 
     * l'affichage d'une dame, ici on dessine un cercle.
     * @see javax.swing.JComponent#paintComponent(Graphics)
     */
    @Override
    public void paintComponent(Graphics g) {
        int color = couleur*75+150;
        int cote = Case.TAILLE-10;
        g.setColor(new Color(color,color,color));
        g.fillOval(x-cote/2,y-cote/2,cote,cote);	
        
        g.setColor(Color.RED);
        g.fillOval(cote/2-5,cote/2-5,10,10);
        
    }
    
    /**
     * Methode calculant les prise possible dans les directions dLig, dCol a partir
     * de la case ligne,colonne. On renvoie le nombre de prises maximale que l'on peut faire.
     * @param ligne ligne de la case du debut de la recherche
     * @param colonne colonne de la case du debut de la recherche 
     * @param dLig direction de recherche des lignes
     * @param dCol direction de recherche des colonnes
     * @param rafle Rafle dans lequel les resultats sont stockes
     * @param nbPrises nombre de prises maximale que l'on peut faire avant l'appel a cette fonction.
     * @param prise Case que l'on affecte a la Rafle rafle
     * @return Nombre de prises maximales que l'on peut faire apres l'appel a cette fonction.
     */
    private int prisePossible(int ligne, int colonne, int dLig, int dCol,Rafle rafle, int nbPrises,CaseNoire prise) {
        
        Piece p = null;
        Case c = null;
        Case c2 = null;
        if( (c = plateau.get(ligne+dLig,colonne+dCol)) != null) {
            if( (p = c.getPiece())!= null)  {
                if(p.getCouleur() != couleur) {
                    if( (c2 = plateau.get(ligne+2*dLig,colonne+2*dCol)) != null) {
                        if(c2.getPiece() == null) {
                            Rafle r = coupObligatoire( (CaseNoire) c);
                            r.setCasePrise(prise);
                            if(r.getNbPrises() > nbPrises) {
                                nbPrises = r.getNbPrises();
                                rafle.clearCasesSuivantes();
                                rafle.addRaflesSuivantes(r);
                                return nbPrises;
                            }
                            if(nbPrises>0 && nbPrises == r.getNbPrises()) {
                                // On doit verifier s'il n'y a pas une rafle equivalente qui commence par la meme case
                                Rafle r1 = rafle.getRaflesSuivantes(r.getCaseDebut());
                                if(r1==null)
                                    // Si il n'y a pas on ajoute simplement la nouvelle rafle trouvee
                                    rafle.addRaflesSuivantes(r);
                                else {
                                    //dans le cas ou une rafle commeneant par la meme case existe deja
                                    // on ajoute a cette rafle, les nouvelles rafles suivantes trouves.
                                    for(int i=0;i<r.getNbCasesSuivantes();i++)
                                        r1.addRaflesSuivantes(r.getRaflesSuivantes(i));
                                }
                                return nbPrises;
                            }
                            
                        }
                    }
                }
            }
        }
        return nbPrises;
    }
    
    /**
     * Methode qui gere la promotion de la piece
     */
    public void promotion() {
        //Les dames ne peuvent etre promues
    }
    
    /**
     * Renvoit sous forme de chaine une description de la dame. La chaine est de la forme
     * "<code>D:ligne,colonne</code>".
     * @return chaine decrivant la dame. 
     */ 
    public String toString() {
        return "D("+couleur+"):" + position.toString();
    }
}
