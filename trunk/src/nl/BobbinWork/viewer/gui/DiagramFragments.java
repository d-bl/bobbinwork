/* Fragments.java Copyright 2006-2007 by J. Falkink-Pol
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
package nl.BobbinWork.viewer.gui;

import static nl.BobbinWork.diagram.xml.DiagramLanguages.getPrimaryTitle;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.Enumeration;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.ListCellRenderer;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;

import nl.BobbinWork.diagram.model.Partition;

import org.w3c.dom.Element;

/**
 * A palette that allows the user to select a fragment of the diagram that can
 * be used again.
 */
@SuppressWarnings("serial")
public class DiagramFragments extends JToolBar {

    /** Appearance (threads and/or pairs) of the rendered cells. */
    private boolean showThreads = true, showPairs = false;

    /** Dimension of the rendered cells. Larger fragments are reduced to fit. */
    private static final Dimension DIM = new Dimension(80, 80);

    /**
     * Provides the methods for the list with diagram fragments to maintain its
     * contents.
     */
    private DefaultListModel listModel = new DefaultListModel();

    /** The container of selectable diagram fragments. */
    private JList list = new JList(listModel); // TODO JList -> JTree

    /**
     * Creates a toolbar that allows the user to select a fragment of the
     * diagram that can be used again
     */
    DiagramFragments() {
        super(JToolBar.VERTICAL);

        setFloatable(false);

        list.setCellRenderer(new MyListRenderer());
        list.setFixedCellWidth(DIM.width);
        list.setFixedCellHeight(DIM.height);
        // list.setLayoutOrientation(JList.VERTICAL_WRAP);

        JScrollPane sp = new JScrollPane(//
                list,//
                ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,//
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        add(sp);
        int width = (int) (DIM.width + sp.getVerticalScrollBar().getMaximumSize().getWidth() + 6);
        int height = (int) sp.getVerticalScrollBar().getMaximumSize().getHeight();
        setMinimumSize(new Dimension(width, DIM.height));
        setMaximumSize(new Dimension(width, height));
    }

    /**
     * Adds a listener to the fragments object that's notified each time a
     * change to the selection occurs.
     * 
     * @param listener
     *            the listener to add
     */
    void addSelectionListener(DiagramFragments.SelectionListener listener) {
        list.addListSelectionListener(listener.listener);
    }

    /** A listener that's notified when the selection of fragments changes. */
    abstract class SelectionListener {

        private SelectionListener self = this;

        private ListSelectionListener listener = new ListSelectionListener() {

            /** Called whenever the value of the selection changes. */
            public void valueChanged(ListSelectionEvent e) {
                self.selectedValueChanged();
            }
        };

        /** Called whenever the value of the selection changes. */
        abstract void selectedValueChanged();
    }

    private class MyListRenderer extends JPanel implements ListCellRenderer {

        private final Border lowered = BorderFactory.createBevelBorder(BevelBorder.LOWERED);
        private final Border raised = BorderFactory.createBevelBorder(BevelBorder.RAISED);
        private final int borderWidth = lowered.getBorderInsets(this).left;

        MyListRenderer() {
            setPreferredSize(DIM);
            setMinimumSize(DIM);
            setMaximumSize(DIM);
        }

        private Partition partition;

        /** The rendering method of the returend component. */
        public void paintComponent(Graphics g) {

            super.paintComponent(g);

            if (partition != null) {
                
                Graphics2D g2 = (Graphics2D) g;
                Rectangle rect = partition.getHull().getBounds();
                double scale = (((double) (DIM.height-2*borderWidth)) / ((double) rect.height));
                double dx =  -rect.x + borderWidth;
                double dy =  -rect.y + borderWidth;
                
                if (scale < 1) {
                    g2.scale(scale, scale);
                }else{// put in the centre
                    dy += ((DIM.height-2*borderWidth) - rect.height)/2D;
                    dx += ((DIM.width-2*borderWidth) - rect.width)/2D;
                }
                g2.translate(dx, dy);
                
                partition.draw(g2, showPairs, showThreads);
                
                g2.translate(-dx, -dy);
                if (scale < 1) {
                    g2.scale(1/scale, 1/scale);
                }
            }
        }

        /** Returns a component who's paintComponent method renders a cell. */
        public Component getListCellRendererComponent//
        (JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            if (isSelected) {
                setBorder(lowered);
                setBackground(list.getSelectionBackground());
                //setForeground(list.getSelectionForeground());
            } else {
                setBorder(raised);
                setBackground(list.getBackground());
                //setForeground(list.getForeground());
            }
            Element element = (Element) value;
            partition = (Partition) element.getUserData(Partition.MODEL_TO_DOM);
            setToolTipText("<html><body><p><strong>" + // //$NON-NLS-1$
                    element.getAttribute("id") + // //$NON-NLS-1$
                    "</strong></p><p>" + // //$NON-NLS-1$
                    getPrimaryTitle(element) + //
                    "</p></body></html>"); //$NON-NLS-1$
            return this;
        }
    }

    /**
     * Populate the model. Previous content is cleared
     * 
     * @param root
     *            TODO: some iterator over elements-with-id's would be
     *            preferable, or better the corresponding property objects of
     *            the diagram model, but the list also should become a tree
     */
    @SuppressWarnings("unchecked") // because depthFirstEnumeration() has a raw type signature 
	void populate(DefaultMutableTreeNode root) {
        listModel.clear();
        for (Enumeration n = root.depthFirstEnumeration(); n.hasMoreElements();) {
            Object object = ((DefaultMutableTreeNode) n.nextElement()).getUserObject();
            if (object instanceof Element //
                    && !((Element) object).getAttribute("id").equals("")// //$NON-NLS-1$ //$NON-NLS-2$
                    && !((Element) object).getAttribute("display").equals("none")// //$NON-NLS-1$ //$NON-NLS-2$
                    ) {
                listModel.addElement((Element) object);
            }
        }
    }

    /**
     * Sets whether pairs and or threads are shown in the diagrams of the
     * fragments.
     */
    void setDiagramType(boolean showPairs, boolean showThreads) {
        this.showPairs = showPairs;
        this.showThreads = showThreads;
        list.repaint(getBounds());
    }

    /** Gets the selected dom element */
    Element getSelectedElement() {
        return (Element) list.getSelectedValue();
    }
}
