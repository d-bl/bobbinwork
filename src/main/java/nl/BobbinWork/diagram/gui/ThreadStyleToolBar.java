/* ThreadStyleToolBar.java Copyright 2006-2007 by J. Pol
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
package nl.BobbinWork.diagram.gui;

import static nl.BobbinWork.bwlib.gui.Localizer.applyStrings;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.URL;

import javax.swing.*;
import javax.swing.event.*;
import javax.xml.parsers.ParserConfigurationException;

import nl.BobbinWork.diagram.model.*;
import nl.BobbinWork.diagram.xml.*;

import org.w3c.dom.Element;
import org.xml.sax.SAXException;

@SuppressWarnings("serial")
public class ThreadStyleToolBar extends JToolBar {

    private Switch twist = createTwist();

    public ThreadStyle getCoreStyle() {
        return twist.getFront().getStyle();
    }
    private Style getShadowStyle() {
    	return twist.getFront().getStyle().getShadow();
    }

    private int getCoreWidth() {
    	return getCoreStyle().getWidth();
    }
    
    private int getShadowWidth() {
    	return getShadowStyle().getWidth();
    }
    
    private void setCoreWidth(int value) {
    	twist.getFront().getStyle().setWidth(value);
    	twist.getBack().getStyle().setWidth(value);
    }
    private void setShadowWidth(int value) {
    	twist.getFront().getStyle().getShadow().setWidth(value);
    	twist.getBack().getStyle().getShadow().setWidth(value);
    }
    
    private void setCoreColor(Color value) {
    	twist.getFront().getStyle().setColor(value);
    	twist.getBack().getStyle().setColor(value);
    }
    private void setShadowColor(Color value) {
    	twist.getFront().getStyle().getShadow().setColor(value);
    	twist.getBack().getStyle().getShadow().setColor(value);
    }
    
    // makes the style of the twist threads available
    private final Preview preview = new Preview(new Dimension(PREVIEW_WITH, PREVIEW_WITH));
    private static final int PREVIEW_WITH = 20;

    private static XmlResources xmlResources;
    public static XmlResources getXmlResources() throws ParserConfigurationException, SAXException {
      if (xmlResources == null )xmlResources = new XmlResources();
      return xmlResources;
    }

    private class Preview extends JPanel {

		Preview(Dimension dim) {

            setPreferredSize(dim);
            setMaximumSize(dim);

            setBackground(new Color(0xFFFFFF));
            // setBorder(BorderFactory.createLoweredBevelBorder());
            // has side effects: increments together with coreSpinner
        }

        public void paintComponent(Graphics g) {

            super.paintComponent(g);
            DiagramPainter.paint ((Graphics2D) g,twist.getThreads());
        }
    }

    private JSpinner coreSpinner = new JSpinner(new SpinnerNumberModel //
            (getCoreWidth(), 1, getShadowWidth() - 2, 1));

    private JSpinner shadowSpinner = new JSpinner(new SpinnerNumberModel //
            (getShadowWidth(), getCoreWidth() + 2, 20, 2));

    private int getSpinnerValue(ChangeEvent e) {

        SpinnerNumberModel source = (SpinnerNumberModel) e.getSource();
        return Integer.parseInt(source.getValue().toString());
    }

    private abstract class ColorButton extends JButton implements ActionListener {

        ColorButton(String fileName) {

    		URL url = ThreadStyleToolBar.class.getResource(fileName);
			setIcon(new ImageIcon(url));
            addActionListener(this);
        }

        public void actionPerformed(ActionEvent e) {

            Color color = JColorChooser.showDialog(this, this.getText(), getColor());
            if (color != null) {
                setColor(color);
                preview.repaint();
            }
        }

        protected abstract Color getColor();
        protected abstract void setColor(Color color);
    }

    private ColorButton shadowButton = new ColorButton("back.PNG") { //$NON-NLS-1$
    	
    	protected Color getColor() {
    		return getShadowStyle().getColor();
    	}
        protected void setColor(Color color) {
            setShadowColor(color);
        }
    };

    private ColorButton coreButton = new ColorButton("front.PNG") { //$NON-NLS-1$
    	
    	protected Color getColor() {
    		return getCoreStyle().getColor();
    	}
        protected void setColor(Color color) {
        	Color shadowColor = getShadowStyle().getColor();
        	int shadowRGB = shadowColor.getRGB();
            setCoreColor(color);
			if (shadowRGB == -1) {
                // once the shadow is white, it should stay white
                // so override the default brighter background set by the model
                setShadowColor(new Color(-1));
            }
        }
    };

    public void setCoreStyle(ThreadStyle p) {
        if (p != null) {
            getCoreStyle().apply(p);
            twist.getBack().getStyle().apply(p);
            ((SpinnerNumberModel) coreSpinner.getModel()).setValue(Integer.valueOf(p.getWidth()));
            Integer width = Integer.valueOf(p.getShadow().getWidth());
            ((SpinnerNumberModel) shadowSpinner.getModel()).setValue(width);
            preview.repaint();
        }
        
    }
    
    public ThreadStyleToolBar() throws SAXException, IOException, ParserConfigurationException {
    	
        setFloatable(false);
        
        // tooltips
        applyStrings(preview, "ThreadStyle"); //$NON-NLS-1$
        applyStrings(coreSpinner, "ThreadStyle_core_width"); //$NON-NLS-1$
        applyStrings(shadowSpinner, "ThreadStyle_shadow_width"); //$NON-NLS-1$
        applyStrings(coreButton, "ThreadStyle_core_color"); //$NON-NLS-1$
        applyStrings(shadowButton, "ThreadStyle_shadow_color"); //$NON-NLS-1$
 
        // dimensions
        Dimension dim = new Dimension(//
                (int) (coreSpinner.getPreferredSize().width * 1.4), //
                coreSpinner.getPreferredSize().height);
        coreSpinner.setMaximumSize(dim);
        shadowSpinner.setMaximumSize(dim);

        // put the components on the toolbar
        add(Box.createHorizontalStrut(3));
        add(coreButton);
        add(Box.createHorizontalStrut(3));
        add(coreSpinner);
        add(Box.createHorizontalStrut(6));
        add(shadowButton);
        add(Box.createHorizontalStrut(3));
        add(shadowSpinner);
        add(Box.createHorizontalStrut(6));
        add(preview);
        add(Box.createHorizontalStrut(0));

        // the width spinners are mutually constrained and therefore should
        // listen to each other
        // on the flight these listeners keep the threadPen and preview
        // up-to-date

        coreSpinner.getModel().addChangeListener(new ChangeListener() {

            public void stateChanged(ChangeEvent e) {

                SpinnerNumberModel shadowModel = (SpinnerNumberModel) shadowSpinner.getModel();

                int i = getSpinnerValue(e);
                shadowModel.setMinimum(Integer.valueOf(i + 2));
                setCoreWidth(i);

                // override the default shadow set by ThreadStyle
                i = shadowModel.getNumber().intValue();
                setShadowWidth(i);

                preview.repaint();
            }
        });

        shadowSpinner.getModel().addChangeListener(new ChangeListener() {

            public void stateChanged(ChangeEvent e) {

                SpinnerNumberModel coreModel = (SpinnerNumberModel) coreSpinner.getModel();

                int i = getSpinnerValue(e);
                coreModel.setMaximum(Integer.valueOf(i - 2));
                setShadowWidth(i);

                preview.repaint();
            }
        });

    }
	private Twist createTwist() throws SAXException, IOException, ParserConfigurationException {
            Element el = getXmlResources().getTwist(PREVIEW_WITH).getDocumentElement();
            return DiagramBuilder.createTwist(el);
	}
}
