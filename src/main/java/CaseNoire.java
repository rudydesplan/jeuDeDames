/*
 *****************************************************************************
 *                         CaseNoire.java  -  description                  
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
import java.awt.Component;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;


/**
 * Classe d crivant un case noire du Jeu de Dames. Cette case est active, contrairement aux
 * cases blanches.
 * 
 * @author rudy
 */
public class CaseNoire extends Case implements MouseListener {
    
    /**
     * Variable priv  contenant la pi ce positionn  sur la case. Elle vaut <b>null</b> si la case
     * est vide
     */
    private Piece piece;
    
    /**
     * Constructeur prenant en argument une CaseNoire, on r alise une copie de la case.
     * @param c CaseNoire que l'on souhaite "copier"
     * @param p Plateau sur lequel se trouve la case
     * @see Case#Case(Case, Plateau)
     * 
     */
    public CaseNoire(CaseNoire c,Plateau p) {
        super(c,p);
        if(c.getPiece() != null) {
            piece = c.getPiece().copie(this);
        }
        else piece = null;
    }
    
    /**
     * Constructeur de base d'une CaseNoire.
     * @param c colonne de la case.
     * @param l ligne de la case.
     * @param p Plateau sur lequel se trouve la case
     */
    public CaseNoire(int c, int l,Plateau p) {
        super(c,l,p);
        piece = null;
        addMouseListener(this);
    }
    
    
    /**
     * M thode qui permet d'ajouter un pi ce   une case.
     * Il s'agit de la m thode add de la classe JLabel red finit
     * pour s'adapter au case des pi ces du jeu de dames.
     * @param comp Le component ajout 
     * @return retourne le comp ajout 
     */
    public Component add(Component comp) {
        piece = (Piece) comp;
        if(comp != null) {
            super.add(comp);
            piece.setLocation(Case.TAILLE/2-5,Case.TAILLE/2-5);
            piece.setBounds(5,5,Case.TAILLE,Case.TAILLE);
        }
        
        
        return comp;
        
    }
    
    /**
     * Retourne un copie de la case.
     * @return la copie de la case.
     * @see Case#copie(Plateau)
     */
    public Case copie(Plateau p) {
        return new CaseNoire(this,p);
    }
    
    /**
     * Retourne la pi ce qui est sur la case. Null s'il n'y a pas de piece
     * sur la case.
     * 
     * @return Piece se situant sur la case. Null s'il n'y a pas de piece.
     */
    public Piece getPiece() {
        return piece;
    }
    
    /**
     * M thode apell  lorsque le joueur clique sur la case.
     * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
     */
    public void mouseClicked(MouseEvent e) {
        // Il y a un pi ce sur la case
        if(piece!=null) {
            
            //Le joueur d selectionne la case.		
            if(this == plateau.getCaseSelect()) {
                plateau.caseSelect(this,null);
            }
            else {
                //Aucune case n'a encore  t  selectionn e
                if(plateau.getCaseSelect()==null) {
                    //Si la case fait partie d'un coup obligatoire
                    if(obligatoire) {
                        plateau.caseSelect(this,piece);	
                    }
                    else {
                        // La case ne fait donc pas partie d'un coup obligatoire,
                        // on v rifie qu'il n'y a pas de coup obligatoire possible
                        // si ce n'est pas la case, on ne s lectionne pas la case.
                        if(!plateau.isObligatoire())
                            plateau.caseSelect(this,piece);
                    }
                }
            }
            
            
        }
        
        // Il n'y a pas de piece sur la case
        else {
            
            //Si il n'y a pas de piece sur la case, et qu'une piece a  t  s lectionn 
            if(plateau.getCaseSelect() != null) {
                //La case fait partie d'un coup obligatoire.
                if(obligatoire) {
                    plateau.caseSelect(this,null);
                }
                else {
                    //Sinon on autorise le d placement que s'il n'y a pas de coup
                    // obligatoire.
                    if(!plateau.isObligatoire()) {
                        CaseNoire caseDepart = plateau.getCaseSelect();
                        Piece p = plateau.getPieceSelec();
                        // On v rifie que le coup est valide
                        // si oui, on envoie au plateau un "message" pour avertir que le coup est valide
                        Rafle rafle = new Rafle(caseDepart,null,null);
                        rafle.addCasesSuivantes(this);
                        if(p.isCoupValide(rafle)) {
                            plateau.caseSelect(this,null);
                        }
                    }
                    
                }
            }
            
        }
    }
    
    
    /**
     * M thode appel e lorsque la souris entre sur une case. Cette m thode g re la mis en surbrillance de la case.
     * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
     * @see Case#select
     */
    
    public void mouseEntered(MouseEvent e) {
        //Si le joueur a s lectionn e une piece
        
        if(plateau.getCaseSelect()!=null) {
            
            if(obligatoire && piece == null) {
                select = true; 
                repaint();
                return;
            }
            
            if(plateau.isObligatoire())
                return;
            
            //Si le coup potentiel est valable, on met la case en surbrillance
            Rafle rafle = new Rafle(plateau.getCaseSelect(),null,null);
            rafle.addCasesSuivantes(this);
            
            if(plateau.getPieceSelec().isCoupValide(rafle)) {
                select=true;
                repaint();
            }
            
        }
    }
    
    /**
     * M thode appel e lorsque la souris sort sur une case. Cette m thode g re la surbrillance de la case.
     * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
     * @see Case#select
     */
    public void mouseExited(MouseEvent e) {
        //Si la case  tait en surbrillance, on la rend "normale"
        if(select) {
            select=false;
            repaint();
        }
        
    }
    
    /*
     * Gestion de la souris (non-Javadoc)
     * 
     */
    
    /**
     * Gestion de la souris
     * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
     */
    public void mousePressed(MouseEvent e) {
        //On ne gere pas cet  venement        
    }
    
    /**
     * Gestion de la souris
     * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
     */
    public void mouseReleased(MouseEvent e) {
        //On ne gere pas cet  venement
    }
    
    /**
     * M thode red finit de la classe JPanel, permettant de personnaliser 
     * l'affichage d'une case noire, ici on dessine un carre de c t  TAILLE
     * @see Case#TAILLE
     * @see javax.swing.JComponent#paintComponent(Graphics)
     */
    public void paintComponent(Graphics g) {
        // Si la case est en surbrillance on dessine un petit carr  cyan
        if(select)  {
            g.setColor(Color.CYAN);
            g.fillRect(0,0,Case.TAILLE,Case.TAILLE);
            g.setColor(Color.BLACK);
            g.fillRect(5,5,Case.TAILLE-10,Case.TAILLE-10);
        }
        else {
            //Si la case est obligatoire on dessine un carre rouge
            if(obligatoire) {
                g.setColor(Color.RED);
                g.fillRect(0,0,Case.TAILLE,Case.TAILLE);
                g.setColor(Color.BLACK);
                g.fillRect(5,5,Case.TAILLE-10,Case.TAILLE-10);
            }
            //			 Sinon on dessin un carr  noir
            else {
                g.setColor(Color.BLACK);
                g.fillRect(0,0,Case.TAILLE,Case.TAILLE);
            }
            
            
            
        }
    }
    
    /**
     * Supprime la piece se situant sur la case.
     */
    public void remove() {
        if(piece!=null) {
            remove(piece);
            piece = null;
        }
    }
    
    /**
     * Permet de rendre la case "obligatoire". Une case est qualifi  d'obligatoire si elle fait
     * partie d'un coup obligatoire.
     * 
     * @param b bool en permettant d'indiquer si la case est ou n'est pas une case "obligatoire"
     * @see Arbitre#calculCoupObligatoire()
     */
    public void setObligatoire(boolean b) {
        obligatoire = b;
        repaint();
    }
    
    /**
     * Met la case en surbrillance.
     * @param b true si la case est s lectionn e, false sinon
     * @see CaseNoire#select
     * 
     */
    public void setSelect(boolean b) {
        select = b;
        repaint();
    }
    
}
