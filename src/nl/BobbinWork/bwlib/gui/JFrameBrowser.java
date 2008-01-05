/* JFrameBrowser.java Copyright 2006-2007 by J. Falkink-Pol
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

package nl.BobbinWork.bwlib.gui;

/**
 * This class creates a frame with a JEditorPane for loading HTML
 * help files
 */
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.IOException;
import java.io.InputStream;

import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.html.HTMLEditorKit;

public class JFrameBrowser
        extends JFrame {
    
    private final int WIDTH = 680;
    private final int HEIGHT = 600;
    private JEditorPane editorpane;

    public JFrameBrowser(String title, Image icon, String file) {
        super(title);
        if ( icon != null ) {
            setIconImage(icon);
        }
        
        InputStream stream = getClass().getClassLoader().getResourceAsStream(file);
        editorpane = new JEditorPane();
        editorpane.setEditable(false);
        editorpane.setEditorKit(new HTMLEditorKit());
        try {
            editorpane.read(stream,file);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        //anonymous inner listener
        editorpane.addHyperlinkListener(new HyperlinkListener() {
            public void hyperlinkUpdate(HyperlinkEvent ev) {
                try {
                    if (ev.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                        editorpane.setPage(ev.getURL());
                    }
                } catch (IOException ex) {
                    //put message in window
                    ex.printStackTrace();
                }
            }
        });
        getContentPane().add(new JScrollPane(editorpane));
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        calculateLocation();
        setVisible(true);
        // end constructor
    }
    /**
     * locate in middle of screen
     */
    private void calculateLocation() {
        Dimension screendim = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(new Dimension(WIDTH, HEIGHT));
        int locationx = (screendim.width - WIDTH) / 2;
        int locationy = (screendim.height - HEIGHT) / 2;
        setLocation(locationx, locationy);
    }
}//end HelpWindow class