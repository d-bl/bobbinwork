/* BWTree.java Copyright 2006-2008 by J. Falkink-Pol
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

import java.awt.Component;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JTree;
import javax.swing.ToolTipManager;
import javax.swing.event.TreeModelEvent;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import nl.BobbinWork.diagram.model.Partition;
import nl.BobbinWork.diagram.xml.ElementType;
import nl.BobbinWork.diagram.xml.TreeBuilder;
import nl.BobbinWork.diagram.xml.expand.TreeExpander;
import static nl.BobbinWork.diagram.xml.DiagramLanguages.getPrimaryTitle;

import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.xml.sax.SAXParseException;

/**
 * Visual presentation of the dom tree of the xml code for a diagram.
 * 
 * @author J. Falkink-Pol
 * 
 */
@SuppressWarnings("serial") //$NON-NLS-1$
public class BWTree extends JTree {

    /** outer this */
    private BWTree self = this; // for inner classes

    private static TreeBuilder //
            validatingTreeBuilder = new TreeBuilder(true),
            TreeBuilder = new TreeBuilder(false);
    
    public BWTree() {
        super(new Vector<String>());
        ToolTipManager.sharedInstance().registerComponent(this);
        setShowsRootHandles(false);
        setRootVisible(true);
        setCellRenderer(new BWTreeCellRenderer());
        getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
    }
    private void setDoc(Element domRoot) {
        
		if (domRoot == null) {
            try {
                domRoot = TreeBuilder.build("<diagram><title> </title></diagram>"); //$NON-NLS-1$
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        DefaultTreeModel treeModel = (DefaultTreeModel) getModel();
        DefaultMutableTreeNode viewModelRoot = (DefaultMutableTreeNode) treeModel.getRoot();

		// destoy old tree
        for (int i = viewModelRoot.getChildCount(); i > 0; viewModelRoot.remove(--i)) {
        }

		buildTree(viewModelRoot, domRoot);
        String s = (String) domRoot.getUserData("source"); //$NON-NLS-1$
        if (s == null) {
            s = ""; //$NON-NLS-1$
        }
		viewModelRoot.setUserObject(s);
        treeModel.nodeStructureChanged(viewModelRoot);
    }

    /**
     * recursively populates the tree model with the element nodes of the dom
     * tree
     */
    private void buildTree(DefaultMutableTreeNode treeNode, Node domNode) {

        for //
        (Node domChild = domNode.getFirstChild() //
        ; domChild != null //
        ; domChild = domChild.getNextSibling() //
        ) {
            if (domChild.getNodeType() == Node.ELEMENT_NODE) {
                DefaultMutableTreeNode nodeChild = new DefaultMutableTreeNode(domChild);
                domChild.setUserData(TreeExpander.DOM_TO_VIEW, nodeChild, null);
                treeNode.add(nodeChild);
                buildTree(nodeChild, domChild);
            }
        }
    }

    private static String basicStitches = null;

    /** 
     * Loads the definitions of the basic stitches.
     * These definitions are loaded just once and inserted at the head of 
     * every xml file of a diagram.
     */
    private void loadBasicStitches() {
       InputStream is = getClass().getClassLoader().getResourceAsStream("nl/BobbinWork/diagram/xml/basicStitches.xml"); //$NON-NLS-1$
       BufferedReader in = new BufferedReader(new InputStreamReader(is));
       StringBuffer buffer = new StringBuffer();
       String line;
       try {
           while ((line = in.readLine()) != null) {
             buffer.append(line);
           }
           is.close();
           basicStitches = buffer.toString();
           int start = basicStitches.indexOf("<group"); //$NON-NLS-1$
           int end = basicStitches.lastIndexOf("</group"); //$NON-NLS-1$
           basicStitches = basicStitches.substring(start, end) + "</group>"; //$NON-NLS-1$
       } catch (IOException e) {
           // TODO Auto-generated catch block
           e.printStackTrace();
       }
    }

    void setDoc(String content1) {
        
        /* TODO workaround because
         *     buildTree(viewModelRoot, basicStitches);
         * didn't work out right in setDoc(Element)
         * probably because the <copy> instruction doesn't work over different documents
         *  
         * rather only load once into the list of fragments
        */
        if (basicStitches==null) {
            loadBasicStitches();
        }
        int i = content1.indexOf("<group"); //$NON-NLS-1$
        String content = content1.substring(0, i) + basicStitches + content1.substring(i);
        ////// end workaround
        
        if (content == null || content.equals("")) { //$NON-NLS-1$
            setDoc((Element) null);
        } else {
            String caption = "XML_ERROR_caption"; //$NON-NLS-1$
            try {
                String fileName = getDocName();
                setDoc(validatingTreeBuilder.build(content));
                setDocName(fileName);
            } catch (SAXParseException e) {
                String s = "XML_ERROR_position" //$NON-NLS-1$
                	.replaceFirst("<LLL>", e.getLineNumber()+"" )  //$NON-NLS-1$ //$NON-NLS-2$
                	.replaceFirst("<CCC>", e.getColumnNumber()+"" ) //$NON-NLS-1$ //$NON-NLS-2$
                	+ "\n"; //$NON-NLS-1$
                JOptionPane.showMessageDialog( //
                        self, // 
                        s + e.getLocalizedMessage(), //
                        caption, //
                        JOptionPane.ERROR_MESSAGE);
                setDoc((Element) null);
            } catch (Exception e) {
                JOptionPane.showMessageDialog( //
                        self, // 
                        e.getLocalizedMessage(), //
                        caption, //
                        JOptionPane.ERROR_MESSAGE);
                setDoc((Element) null);
            }
        }
    }

    /**
     * gets the partition of the diagram referred to by the selected element of
     * the tree model
     */
    public Partition getSelectedPartition() {
        try {
            DefaultMutableTreeNode viewNode = (DefaultMutableTreeNode) getSelectionPath()
                    .getLastPathComponent();
            Node domNode = (Node) viewNode.getUserObject();
            if ((viewNode != null) && (viewNode.toString().matches(".*copy:.*"))) { //$NON-NLS-1$
                // from the orphaned dom node to the original node
                domNode = (Node) domNode.getUserData(TreeExpander.ORPHANE_TO_CLONE);
            }
            return ((Partition) domNode.getUserData(Partition.MODEL_TO_DOM));
        } catch (Exception e) {
            return null;
        }
    }

    /** gets the root element of the tree model */
    DefaultMutableTreeNode getRoot() {
        return (DefaultMutableTreeNode) getModel().getRoot();
    }

    /** gets the root element of the dom tree */
    Element getRootElement() {
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) getModel().getRoot();
        if (root.getUserObject().getClass().getName().matches(".*String.*")) { //$NON-NLS-1$
            // the root of the tree view is just a filename
            root = (DefaultMutableTreeNode) root.getFirstChild();
        }
        Node domRoot = (Node) root.getUserObject();

        return (Element) domRoot.getOwnerDocument().getDocumentElement();
    }

    /**
     * restores the orphaned node in the dom tree by the original nodes that are
     * still in the tree model
     * 
     * @param e
     */
    static void restoreOrphans(TreeModelEvent e) {

        DefaultMutableTreeNode mtn = (DefaultMutableTreeNode) e.getTreePath().getLastPathComponent();
        if (mtn.getUserObject().getClass().getName().matches(".*String.*")) { //$NON-NLS-1$
            // the root of the tree view is just a filename
            /*
             * TODO redesign/eliminate TreeBuilder so ownerDocument stores the
             * filename and treeCellRenderer can retrieve the filename from the
             * DomNode
             */
            mtn = (DefaultMutableTreeNode) mtn.getFirstChild();
        } else {
            // restore the orphaned DOM node, and subsequent ones, back into
            // their original place in the DOM tree
            for (DefaultMutableTreeNode next = mtn; next != null; next = next.getNextNode()) {
                Element el = (Element) next.getUserObject();
                Element clone = (Element) el.getUserData(TreeExpander.ORPHANE_TO_CLONE);
                if (clone != null) {
                    clone.getParentNode().replaceChild(el, clone);
                    el.setUserData(TreeExpander.ORPHANE_TO_CLONE, null, null);
                }
            }
        }
    }

    /**
     * @param selectedElement
     *            element to be replaced
     * @param alternativeElement
     *            proposed alternative
     * 
     * @return both true if both elements require the same number of pairs
     */
    static boolean elementsMatch(Element selectedElement, Element alternativeElement) {

        if (selectedElement == null || alternativeElement == null) {
            return false;
        }
        Partition alternativePartition = (Partition) alternativeElement.getUserData(Partition.MODEL_TO_DOM);
        Partition selectedPartition = (Partition) selectedElement.getUserData(Partition.MODEL_TO_DOM);
        if (selectedPartition == null) {
            Element clonedElement = (Element) selectedElement.getUserData(TreeExpander.ORPHANE_TO_CLONE);
            if (clonedElement != null) {
                selectedPartition = (Partition) clonedElement.getUserData(Partition.MODEL_TO_DOM);
            }
        }
        if (selectedPartition == null || alternativePartition == null) {
            return false;
        }
        return alternativePartition.getNrOfPairs() == selectedPartition.getNrOfPairs();
    }

    /**
     * gets the dom element referred to by the selected element of the tree
     * model
     */
    Element getSelectedElement() {
        try {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) getSelectionPath().getLastPathComponent();
            return (Element) node.getUserObject();
        } catch (Exception e) {
            return null;
        }
    }

    boolean SelectedElementIsDeletable() {
        try {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) getSelectionPath().getLastPathComponent();
            Element element = (Element) node.getUserObject();
            Node displayAttribute = element.getAttributes().getNamedItem("display"); //$NON-NLS-1$
            return element.getUserData(TreeExpander.CLONED) == null //$NON-NLS-1$
                    && (displayAttribute == null //
                    || displayAttribute.toString().equalsIgnoreCase("no")); //$NON-NLS-1$
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * deletes the currently selected node from the tree model, adjusts the dom
     * tree accordingly
     */
    void deleteSelected() {

        try {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) getSelectionPath().getLastPathComponent();
            Element selectedElement = (Element) node.getUserObject();
            TreePath nextToSelect = null;
            try {
                nextToSelect = new TreePath(node.getNextSibling().getPath());
            } catch (NullPointerException e) {
            }

            if (ElementType.copy.toString().equals(selectedElement.getNodeName())) {
                selectedElement = (Element) selectedElement.getUserData(TreeExpander.ORPHANE_TO_CLONE); //$NON-NLS-1$
            }
            MutableTreeNode tn = (MutableTreeNode) selectedElement.getUserData(TreeExpander.DOM_TO_VIEW); //$NON-NLS-1$
            MutableTreeNode tnp = (MutableTreeNode) tn.getParent();

            // remove from dom tree and from tree view
            selectedElement.getParentNode().removeChild(selectedElement);
            tn.removeFromParent();

            // notify the change to the tree view and other listeners
            DefaultTreeModel m = (DefaultTreeModel) getModel();
            m.nodeChanged(tnp);
            m.reload(tnp);

            if (nextToSelect != null) {
                setSelectionPath(nextToSelect);
            }
        } catch (Exception e) {
        }
    }

    /**
     * replaces the "of" attribute of the the selected dom element with the "id"
     * attribute of hte alternative element and re-evaluate the dom tree
     */
    void replaceSelected(Element alternativeElement) {
        try {
            TreePath sp = getSelectionPath();
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) sp.getLastPathComponent();
            Element se = (Element) node.getUserObject();

            // TODO? setSelectedElement(null);

            se.setAttribute("of", alternativeElement.getAttribute("id")); //$NON-NLS-1$ //$NON-NLS-2$

            MutableTreeNode tn = (MutableTreeNode) se.getUserData(TreeExpander.DOM_TO_VIEW); //$NON-NLS-1$
            MutableTreeNode tnp = (MutableTreeNode) tn.getParent();

            // notify the change to the tree view and other listeners
            DefaultTreeModel m = (DefaultTreeModel) getModel();
            m.nodeChanged(tnp);
            m.reload(tnp);
            setSelectionPath(sp);
        } catch (Exception e) {

        }
    }

    /** assigns the filename to the userobject of the first node of the treemodel */
    void setDocName(String fileName) {

        DefaultTreeModel treeModel = (DefaultTreeModel) getModel();
        DefaultMutableTreeNode viewModelRoot = (DefaultMutableTreeNode) treeModel.getRoot();
        viewModelRoot.setUserObject(fileName);
        treeModel.nodeChanged(viewModelRoot);
    }

    /** gets the filename from the userobject of the first node of the treemodel */
    private String getDocName() {

        DefaultTreeModel treeModel = (DefaultTreeModel) getModel();
        DefaultMutableTreeNode viewModelRoot = (DefaultMutableTreeNode) treeModel.getRoot();
        return (String) viewModelRoot.getUserObject();
    }

    /**
     * Renders the dom elements with icons and minimal text, mainly numbers:
     * numbers of pairs, bobbins or co-ordinates.
     */
    private class BWTreeCellRenderer extends DefaultTreeCellRenderer {

        private ImageIcon[] icons;

        BWTreeCellRenderer() {

            // load the icons for the element tags
            String dir = getClass().getPackage().getName().replaceAll("\\.", "/") + "/"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            icons = new ImageIcon[ElementType.values().length];
            int i = 0;
            for (ElementType et : ElementType.values()) {
                String iconFileName = dir + et.toString() + ".gif"; //$NON-NLS-1$
                URL iconURL = getClass().getClassLoader().getResource(iconFileName);
                if (iconURL != null) {
                    icons[i++] = new ImageIcon(iconURL);
                } else {
                    icons[i++] = null;
                }
            }
        }

        public Component getTreeCellRendererComponent//
        (JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {

            super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);

            Object userObject = ((DefaultMutableTreeNode) value).getUserObject();

            if (userObject instanceof String) {

                String fileName = (String) userObject;

                setToolTipText(fileName);
                setText(fileName.replaceAll(".*[/\\\\]", "")); //$NON-NLS-1$ //$NON-NLS-2$

            } else if (userObject instanceof Element) {

                Element element = (Element) userObject;
                ElementType elementType = ElementType.valueOf(element.getNodeName());

                String s = ""; //$NON-NLS-1$
                if ((element.getAttribute("color") != null) && (element.getAttribute("color").length() > 0)) { //$NON-NLS-1$ //$NON-NLS-2$

                    s = "<html><head><body><p><span style='background:"// //$NON-NLS-1$
                            + element.getAttribute("color")// //$NON-NLS-1$
                            + "'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span> " // //$NON-NLS-1$
                            + element.getAttribute("width") // //$NON-NLS-1$
                            + "</p></body></html>"; //$NON-NLS-1$

                } else {
                    s = " (" + element.getAttribute("x") + "," + element.getAttribute("y") + ") "; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
                    s = s.replaceAll(" \\(,\\)", ""); //$NON-NLS-1$ //$NON-NLS-2$
                    s += (" (" + element.getAttribute("start") + ")").replace(" ()", ""); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
                    s += (" (" + element.getAttribute("c1") + ")").replace(" ()", ""); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
                    s += (" (" + element.getAttribute("c2") + ")").replace(" ()", ""); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
                    s += (" (" + element.getAttribute("end") + ")").replace(" ()", ""); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
                    s += (" (" + element.getAttribute("centre") + ")").replace(" ()", ""); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
                    s += (" (" + element.getAttribute("position") + ")").replace(" ()", ""); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
                    s += " " + element.getAttribute("angle"); //$NON-NLS-1$ //$NON-NLS-2$
                    if (element.getAttribute("pairs").length() > 0) { //$NON-NLS-1$
                        s += "" + " " + element.getAttribute("pairs") + " : "; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
                    } else if (element.getAttribute("bobbins").length() > 0) { //$NON-NLS-1$
                        s += "" + " " + element.getAttribute("bobbins") + " : "; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
                    }
                    s += element.getAttribute("nrs"); //$NON-NLS-1$
                    if (element.getAttribute("lang").length() > 0) { //$NON-NLS-1$
                        s += (element.getAttribute("lang") + ": ")// //$NON-NLS-1$ //$NON-NLS-2$
                        + element.getFirstChild().getNodeValue();
                    }
                    s += " " + getPrimaryTitle(element); //$NON-NLS-1$
                    s += " " + getPrimaryTitle((Element) element.getUserData(TreeExpander.ORPHANE_TO_CLONE)); //$NON-NLS-1$

                    s = s.replaceAll("\\s+", " "); //$NON-NLS-1$ //$NON-NLS-2$
                    s = s.replaceAll("^\\s*", ""); //$NON-NLS-1$ //$NON-NLS-2$
                }

                setIcon(elementType.getIcon());
                setText(s);

                // same label (bold)
                // + XML element with attributes for the tooltip

                if (s.indexOf("</body>") > 0) { //$NON-NLS-1$
                    s = ""; // clear color //$NON-NLS-1$
                }
                s = s.replaceAll("<", "&lt;").replaceAll(">", "&gt;"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
                s = "<html><head><body><p><strong>" + s + "</strong></p><p>&lt;" + element.getNodeName(); //$NON-NLS-1$ //$NON-NLS-2$
                NamedNodeMap n = element.getAttributes();
                int count = n.getLength();
                for (int i = 0; i < count; i++) {
                    s += " " + n.item(i).getNodeName() + "='" + n.item(i).getNodeValue() + "'"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                }
                setToolTipText(s + "&gt;</p></body></html>"); //$NON-NLS-1$
            }
            return this;
        }
    }

}
