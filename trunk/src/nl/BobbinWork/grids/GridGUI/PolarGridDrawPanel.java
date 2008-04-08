/* PolarGridDrawPanel.java Copyright 2005-2007 by J. Falkink-Pol
 *
 * This file is part of BobbinWork.
 *
 * BobbinWork is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * BobbinWork is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with BobbinWork.  If not, see <http://www.gnu.org/licenses/>.
 */

package nl.BobbinWork.grids.GridGUI;

/**
 *
 * @author J. Falkink-Pol
 */
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Arc2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class PolarGridDrawPanel
        
        extends
        JPanel
        
        implements
        Printable,
        ActionListener,
        java.beans.PropertyChangeListener, 
        javax.swing.event.ChangeListener {
    
    private nl.BobbinWork.grids.PolarGridModel.PolarGridModel pgm = null;
    
    public PolarGridDrawPanel() {
        setBackground(Color.white);
    }
    
    public void setPolarGridModel(nl.BobbinWork.grids.PolarGridModel.PolarGridModel pgm ) {
        this.pgm = pgm;
    }
    
    public void propertyChange(java.beans.PropertyChangeEvent e) {
        // listens to the text fields on the defPanel
        this.repaint();
    }
    public void stateChanged(javax.swing.event.ChangeEvent e) {
        // listens to the spinners on the defPanel
        this.repaint();
    }
    public void actionPerformed(ActionEvent e) {
        
        if ( e.getActionCommand().equalsIgnoreCase("print" ) ) {
            
            PrinterJob printJob = PrinterJob.getPrinterJob();
            printJob.setPrintable(this);
            if (printJob.printDialog()) {
                try {
                    printJob.print();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        } else {
            // listens to the refresh menu item and the non text fields on the defPanel
            this.repaint();
        }
    }
    
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawShapes( (Graphics2D) g, 5D, 180D);
    }
    
    public int print(Graphics g, PageFormat pf, int pi) throws PrinterException {
        if (pi >= 1) {
            return Printable.NO_SUCH_PAGE;
        }
        drawShapes( (Graphics2D) g, 75D, 0D);
        return Printable.PAGE_EXISTS;
    }
    
    private void drawShapes(Graphics2D g2, double margin, double rotation){
        
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // don't only show the south east part of the circle
        double radius = pgm.getOuterCircle().getDiameter() / 2D;
        radius *= nl.BobbinWork.grids.PolarGridModel.PolarGridModel.SCALE_MM;
        // respect the margin
        radius += margin;
        g2.translate(radius, Math.min(radius, 324D) ); // verticaly on the center of the paper
        
        // the repeat markings not starting from 3 o'clock but with 9 o'clock between the first two
        g2.rotate(Math.toRadians( -360D/2D / (double) pgm.getNumberOfRepeats() ));
        g2.rotate(Math.toRadians( rotation ));
        
        //g2.rotate(Math.toRadians(-45D));
        //g2.shear(0.2D, 0.2D);
        
        Line2D  []  l = pgm.getRepeats();
        Arc2D   []  a = pgm.getBoundaries();
        Point2D []  p = pgm.getAllDots();
        g2.setPaint( Color.blue  ); for ( int i=0 ; i < l.length; i++ ) { g2.draw( l[i] ); }
        g2.setPaint( Color.red   ); for ( int i=0 ; i < a.length; i++ ) { g2.draw( a[i] ); }
        g2.setPaint( Color.black ); for ( int i=0 ; i < p.length; i++ ) {
            g2.fill(new Ellipse2D.Double( p[i].getX(), p[i].getY(), 1, 1 ));
        }
        
        this.setPreferredSize(new Dimension( (int)radius*2, (int)radius*2 ));
        this.revalidate();
    }
}
