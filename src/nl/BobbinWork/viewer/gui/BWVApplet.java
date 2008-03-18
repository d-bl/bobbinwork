/* BWVApplet.java Copyright 2006-2008 by J. Falkink-Pol
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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.GregorianCalendar;
import java.util.Locale;
import static java.awt.event.KeyEvent.*;
import static java.awt.event.InputEvent.CTRL_DOWN_MASK;
import static java.awt.event.InputEvent.CTRL_MASK;
import static java.awt.event.InputEvent.ALT_DOWN_MASK;

import javax.swing.AbstractButton;
import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import static javax.swing.JSplitPane.HORIZONTAL_SPLIT;
import static javax.swing.JSplitPane.VERTICAL_SPLIT;
import javax.swing.JTextArea;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

import org.w3c.dom.Element;

import static nl.BobbinWork.bwlib.gui.Localizer.*;
import nl.BobbinWork.bwlib.gui.HelpMenu;
import nl.BobbinWork.bwlib.gui.LocaleMenuItem;
import nl.BobbinWork.bwlib.gui.CursorController;
import nl.BobbinWork.bwlib.gui.LocaleButton;
import nl.BobbinWork.bwlib.gui.CPanel;
import nl.BobbinWork.bwlib.gui.SplitPane;
import nl.BobbinWork.bwlib.io.BWFileFilter;
import nl.BobbinWork.bwlib.io.BWFileHandler;
import nl.BobbinWork.bwlib.io.InputStreamCreator;
import nl.BobbinWork.diagram.gui.InteractiveDiagramPanel;
import nl.BobbinWork.diagram.gui.DiagramPanel;
import nl.BobbinWork.diagram.model.Diagram;
import nl.BobbinWork.diagram.xml.expand.TreeExpander;

/**
 * @author User
 *
 */
@SuppressWarnings("serial")
public class BWVApplet extends JApplet {

	private static final String years = "2006-2008";  
	private static String caption = "Viewer"; // get extended by the help menu  

	private static final int TOTAL_LEFT_WIDTH = 300;
    private static final String LOCALIZER_BUNDLE_NAME = "nl/BobbinWork/viewer/gui/labels"; //$NON-NLS-1$
    private static final String NEW_DIAGRAM = "nl/BobbinWork/diagram/xml/newDiagram.xml"; //$NON-NLS-1$

    /** icon for the left upper corner of dialogs and application frame */
    private final Image icon = getToolkit().getImage(getClass().getClassLoader()//
            .getResource("nl/BobbinWork/viewer/gui/bobbin.gif")); //$NON-NLS-1$

    /** JTextArea with the XML source */
    private SourceArea source;

    /** tree view of the XML elements */
    private BWTree tree;

    /**
     * Fragments of the diagram that can be used again (referred to with &lt;copy
     * of=".."&gt;)
     */
    private DiagramFragments fragments;

    /**
     * A vector drawing drawn by the model created from the DOM tree that was
     * generated from the XML file.
     */
    private DiagramPanel diagramPanel;

    /**
     * JButton's triggering modification of the DOM tree that was generated from
     * the XML file.
     */
    private JButton delete, replace;

    public BWVApplet() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        setPreferredSize(new Dimension(790, 500));
        setBundle(LOCALIZER_BUNDLE_NAME);
        /*
         * After completing the constructor of the applet, the main of the
         * application gets a chance to overrule the defaults of the applet
         * before init() gets called.
         */
    }

    /**
     * Completes creation of the applet now that the look-and-feel and Localizer
     * are initialized.
     */
    public void init() {

        /* ---- create components ---- */

        tree = new BWTree();
        source = new SourceArea();
        diagramPanel = new DiagramPanel();
        fragments = new DiagramFragments();
        delete = new LocaleButton(false, "TreeToolBar_delete");//$NON-NLS-1$
        replace = new LocaleButton(false, "TreeToolBar_replace"); //$NON-NLS-1$

        /* ---- connect non-menu components with listeners ---- */

        createNonMenuListeners();

        /* ---- create global menus with listeners ---- */

        ActionListener inputStreamListener = new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				InputStreamCreator ish = ((InputStreamCreator)((JPopupMenu)((JMenuItem)e.getSource()).getParent()).getInvoker());
				loadFromStream( ish.getInputStreamName(), ish.getInputStream() );
		}};
		final HelpMenu helpMenu = new HelpMenu(this, years,caption);
		caption = helpMenu.getCaption();
		
        JMenuBar //
        jMenuBar = new JMenuBar();
        jMenuBar.add(new FileMenu());
        jMenuBar.add(new SampleDiagramChooser(this,inputStreamListener));
        jMenuBar.add(helpMenu); 
        setJMenuBar(jMenuBar);

        /* ---- load content ---- */

        loadNewFile();

        /* ---- put components and their local menu's/toolbars together ---- */

        int dividerPosition = TOTAL_LEFT_WIDTH 
        					- SplitPane.DIVIDER_WIDTH 
        					- (int) fragments.getMinimumSize().getWidth();
        
		JSplitPane//

        splitPane = new SplitPane(//
                dividerPosition, 
                HORIZONTAL_SPLIT, // 
                new CPanel( // component of spiltPane
                        new AbstractButton[] { delete, replace }, // toolbar
                        new JScrollPane(tree)),
                new CPanel( // component of spiltPane
                        new FragmentsViewMenu(), // toolbar
                        fragments)); 

        splitPane = new SplitPane(//
                1000, // dividerPosition
                VERTICAL_SPLIT, // orientation
                splitPane, // 
                new CPanel( //
                        new EditMenu(), // toolbar
                        new JScrollPane(source))); // 

        splitPane = new SplitPane(//
                TOTAL_LEFT_WIDTH, // dividerPosition
                HORIZONTAL_SPLIT, // orientation
                splitPane, // left component of spiltPane
                new InteractiveDiagramPanel(diagramPanel, this));

        getContentPane().add(splitPane);
    }

    /** Connects the non-menu components with listeners */
    private void createNonMenuListeners() {

        delete.addActionListener(CursorController.createListener(this,new ActionListener() {

            public void actionPerformed(ActionEvent e) {
   	                tree.deleteSelected();
            }
        }));

        replace.addActionListener(CursorController.createListener(this,new ActionListener() {

            public void actionPerformed(ActionEvent e) {
   	                tree.replaceSelected(fragments.getSelectedElement());
            }
        }));

        fragments.addSelectionListener(fragments.new SelectionListener() {

            void selectedValueChanged() {
                replace.setEnabled(BWTree.elementsMatch(//
                        tree.getSelectedElement(), //
                        fragments.getSelectedElement()));
            }
        });

        tree.getModel().addTreeModelListener(new TreeModelListener() {

			private void treeChanged(TreeModelEvent e) {
				BWTree.restoreOrphans(e);
		        TreeExpander.parse((Element) tree.getRootElement());
                diagramPanel.setPattern(new Diagram(tree.getRootElement()));
                fragments.populate(tree.getRoot());
			}

            public void treeNodesChanged(TreeModelEvent e) { treeChanged(e); }
            public void treeNodesInserted(TreeModelEvent e) { treeChanged(e); }
            public void treeNodesRemoved(TreeModelEvent e) { treeChanged(e); }
            public void treeStructureChanged(TreeModelEvent e) { treeChanged(e);}
        });

        tree.addTreeSelectionListener(new TreeSelectionListener() {

            // Skip too frequent events.

            private long lastTime = new GregorianCalendar().getTimeInMillis();

            static private final int delay = 100; // milliseconds

            public void valueChanged(TreeSelectionEvent e) {

                lastTime = new GregorianCalendar().getTimeInMillis();
                Timer timer = new Timer(//
                        delay, //
                        new ActionListener() {
                            public void actionPerformed(ActionEvent e) {
                                long now = new GregorianCalendar().getTimeInMillis();
                                if ((now - lastTime) > delay) {
                                    lastTime = new GregorianCalendar().getTimeInMillis();
                                    // perform the actions:
                                    replace.setEnabled(BWTree.elementsMatch(tree.getSelectedElement(),
                                            fragments.getSelectedElement()));
                                    delete.setEnabled(tree.SelectedElementIsDeletable());
                                    diagramPanel.highLight(tree.getSelectedPartition());
                                }
                            }
                        });
                timer.setRepeats(false);
                timer.start();
            }
        });

    }
    
    /** A fully dressed JMenu, extendable for the applet */
	private class FileMenu extends JMenu {

        /** Creates a JMenu with items that load a predefined file. */
        private FileMenu() {

            applyStrings(this, "MenuFile_file"); //$NON-NLS-1$

            JMenuItem//

            jMenuItem = new LocaleMenuItem("MenuFile_New", VK_N, CTRL_MASK); //$NON-NLS-1$
            jMenuItem.addActionListener(CursorController.createListener(this,new ActionListener() {
            	public void actionPerformed(ActionEvent e) {
                	//FIXME cursor doesn't become hour glass
            		loadNewFile();
            	}
            }));
            add(jMenuItem);

            if (! wrappedInApplicationFrame() ) return;
            // the remaining IO items only if executed as a Java application

            add(new JSeparator());

            jMenuItem = new LocaleMenuItem( "MenuFile_exit",VK_F4, ALT_DOWN_MASK); //$NON-NLS-1$
            jMenuItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    System.exit(0);
                }
            });
            add(jMenuItem);

            if ( getFileHandler() == null ) return;
            insertSeparator(0);

            jMenuItem = new LocaleMenuItem("MenuFile_SaveAs"); //$NON-NLS-1$
            jMenuItem.setActionCommand("saveAs"); //$NON-NLS-1$
            jMenuItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    if ( getFileHandler() == null ) return;
                	fileHandler.saveAs(source.getText());
                    tree.setDocName(fileHandler.getFileName());
                }
            });
            insert(jMenuItem, 0);

            jMenuItem = new LocaleMenuItem ("MenuFile_save",VK_S, CTRL_DOWN_MASK); //$NON-NLS-1$
            jMenuItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    if ( getFileHandler() == null ) return;
                	fileHandler.save(source.getText());
                }
            });
            insert(jMenuItem, 0);

            jMenuItem = new LocaleMenuItem( "MenuFile_open",VK_O, CTRL_DOWN_MASK); //$NON-NLS-1$
            jMenuItem.addActionListener(CursorController.createListener(this,new ActionListener() {
            	//FIXME cursor doesn't become hour glass
                public void actionPerformed(ActionEvent e) {
                    if ( getFileHandler() == null ) return;
                    loadFile();
                }
            }));
            insert(jMenuItem, 0);
        }
    }

    /** A fully dressed JMenu, to edit the XML source */
	private class EditMenu extends JMenu {

        /** Creates a fully dressed JMenu, to edit the XML source */
        private EditMenu() {

            applyStrings(this, "MenuEdit_edit"); //$NON-NLS-1$

            JMenuItem//
            jMenuItem = new LocaleMenuItem( "MenuEdit_show",VK_F5, 0); //$NON-NLS-1$
            jMenuItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    // String fileName = tree.getDocName();
                    tree.setDoc(source.getText());
                    // tree.setDocName(fileName);
                }
            });
            add(jMenuItem);

            add(new javax.swing.JSeparator());

            jMenuItem = new LocaleMenuItem( "MenuEdit_InsertColor"); //$NON-NLS-1$
            jMenuItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    source.insertColor();
                }
            });
            add(jMenuItem);

            add(new javax.swing.JSeparator());

            jMenuItem = new LocaleMenuItem( "MenuEdit_cut",VK_X, CTRL_DOWN_MASK); //$NON-NLS-1$ 
            jMenuItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    source.cut();
                }
            });
            add(jMenuItem);

            jMenuItem = new LocaleMenuItem( "MenuEdit_copy",VK_C, CTRL_DOWN_MASK); //$NON-NLS-1$
            jMenuItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    source.copy();
                }
            });
            add(jMenuItem);

            jMenuItem = new LocaleMenuItem( "MenuEdit_paste",VK_V, CTRL_DOWN_MASK); //$NON-NLS-1$
            jMenuItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    source.paste();
                }
            });
            add(jMenuItem);
        }
    }

    /** A fully dressed JMenu, controlling the view of the fragments */
	private class FragmentsViewMenu extends JMenu {

        /** Creates a fully dressed JMenu, controlling the view of the fragments */
        private FragmentsViewMenu() {
            applyStrings(this, "MenuFragments_fragments"); //$NON-NLS-1$

            JMenuItem//
            jMenuItem = new LocaleMenuItem("MenuView_thread"); //$NON-NLS-1$ 
            jMenuItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent arg0) {
                    fragments.setDiagramType(false, true);
                }
            });
            add(jMenuItem);

            jMenuItem = new LocaleMenuItem("MenuView_pair"); //$NON-NLS-1$ 
            jMenuItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent arg0) {
                    fragments.setDiagramType(true, false);
                }
            });
            add(jMenuItem);

            jMenuItem = new LocaleMenuItem("MenuView_hybrid"); //$NON-NLS-1$ 
            jMenuItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent arg0) {
                    fragments.setDiagramType(true, true);
                }
            });
            add(jMenuItem);
        }
    }

    /** manages the last opened/saved file */
    private BWFileHandler fileHandler;
    
    private BWFileHandler getFileHandler() {
        if (fileHandler == null ) try { 
        	fileHandler = new BWFileHandler(this, //
                new BWFileFilter( getString("FileType"), "xml,bwml".split(",") ));
        } 
        catch (Exception e) { }
        return fileHandler;
    }

    /** Loads a new file into the source and tree. */
    private void loadNewFile() {
        InputStream stream = getClass().getClassLoader().getResourceAsStream(NEW_DIAGRAM);
        loadFromStream(NEW_DIAGRAM, stream);
    }

	void loadFromStream(String fileName, InputStream stream) {
		if (stream == null) {
            JOptionPane.showMessageDialog(this, //
                    fileName + "\nnot found", "load sample", JOptionPane.ERROR_MESSAGE);
            tree.setDoc("");
            tree.setDocName("");
        } else {
            try {
                source.read(new InputStreamReader(stream), fileName);
                tree.setDoc(source.getText());
                tree.setDocName(fileName);
            } catch (Exception exception) {
                showError(fileName, exception, "load sample, read failed");
                source.setText(null);
                tree.setDoc("");
            }

            try {
                stream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (fileHandler != null) {
        	fileHandler.clearFileName();
        }
	}

    /** Lets the user select a file and loads it into source and tree */
	private void loadFile() {
		InputStream stream = fileHandler.open();
		if (stream != null) {
			String fileName = fileHandler.getFileName();
			try {
				source.read(new InputStreamReader(stream), fileName);
				tree.setDoc(source.getText());
				tree.setDocName(fileName);
			} catch (Exception exception) {
				showError(fileName, exception, "open file");
				source.setText(null);
				tree.setDoc("");
				fileHandler.clearFileName();
			}

			try {
				stream.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

    /**
     * Convenience API for JOptionPane.showMessageDialog to show the exception
     * message to the user
     */
    private void showError(String fileName, Exception exception, String action) {
        JOptionPane.showMessageDialog(this, //
                fileName + "\n" + exception.getLocalizedMessage(),//
                action, //
                JOptionPane.ERROR_MESSAGE);
    }

    /** A JTextArea with an additional method to insert color codes. */
	private class SourceArea extends JTextArea {

        /**
         * Shows a JColorChooser dialog and pastes the result as a hexadecimal
         * code into the associated text model.
         */
        private void insertColor() {
            Color color = JColorChooser.showDialog(this, source.getText(), Color.BLACK);
            if (color != null) {
                String s = "#" + Integer.toHexString(color.getRGB() & 0xFFFFFF);
                replaceSelection(s);
            }
        }
    }

    /** @return true if the applet is framed in an application */
    private boolean wrappedInApplicationFrame() {
    	
    	try { 
    		getDocumentBase(); 
    		return false; 
    	} catch (NullPointerException e) {}
    	return true;
    }

    private static void wrapInApplicationFrame(BWVApplet applet) {
    	
		JFrame frame = new JFrame();
        frame.setSize(700, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle(caption); //$NON-NLS-1$
        frame.setIconImage(applet.icon);
        frame.add(applet);
        frame.setVisible(true);
	}

    public static void main(String[] args) {

        BWVApplet applet = new BWVApplet();
        if (args.length > 0) setBundle(LOCALIZER_BUNDLE_NAME, new Locale(args[0]));
        applet.init();
        wrapInApplicationFrame(applet);
    }
}
