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

import java.awt.BorderLayout;
import static java.awt.BorderLayout.PAGE_START;
import static java.awt.BorderLayout.CENTER;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.GregorianCalendar;
import java.util.Locale;
import static java.awt.event.KeyEvent.*;
import static java.awt.event.InputEvent.CTRL_DOWN_MASK;
import static java.awt.event.InputEvent.CTRL_MASK;
import static java.awt.event.InputEvent.ALT_DOWN_MASK;
import static java.awt.event.InputEvent.SHIFT_DOWN_MASK;

import javax.swing.AbstractButton;
import javax.swing.Box;
import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import static javax.swing.JSplitPane.HORIZONTAL_SPLIT;
import static javax.swing.JSplitPane.VERTICAL_SPLIT;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import static javax.swing.KeyStroke.getKeyStroke;
import static javax.swing.SwingUtilities.isRightMouseButton;
import static javax.swing.SwingUtilities.isLeftMouseButton;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

import static nl.BobbinWork.bwlib.gui.Localizer.*;
import nl.BobbinWork.bwlib.gui.AboutInfo;
import nl.BobbinWork.bwlib.gui.NativeBrowser;
import nl.BobbinWork.bwlib.io.BWFileFilter;
import nl.BobbinWork.bwlib.io.BWFileHandler;

public class BWVApplet extends JApplet {

    private static final int TOTAL_LEFT_WIDTH = 300;

    private static final AboutInfo aboutInfo = new AboutInfo(//
            " BobbinWork - Viewer" // caption //$NON-NLS-1$
            , "2.0.58" // version //$NON-NLS-1$
            , "2006-2007" // years //$NON-NLS-1$
            , "J. Falkink-Pol" // author //$NON-NLS-1$
    );

    private static final String //
            LOCALIZER_BUNDLE_NAME = "nl/BobbinWork/viewer/gui/labels"; //$NON-NLS-1$
            
    private static final String NEW_DIAGRAM = "nl/BobbinWork/diagram/xml/newDiagram.xml";

    /** icon for dialogs and application frame */
    private final Image icon = getToolkit().getImage(getClass().getClassLoader()//
            .getResource("nl/BobbinWork/viewer/gui/bobbin.gif")); //$NON-NLS-1$

    /** outer this */
    private BWVApplet self = this;

    /** JTextArea with the XML source */
    private SourceArea source;

    /** tree view of the XML elements */
    private BWTree tree;

    /**
     * Fragments of the diagram that can be used again (refered to with &lt;copy
     * of=".."&gt;)
     */
    private Fragments fragments;

    /**
     * A vector drawing drawn by the model created from the dom tree that was
     * generated from the xml file.
     */
    private DiagramPanel diagramPanel;

    /** Toolbar defining styles for threads. */
    private ThreadStyleToolBar threadStyleToolBar;

    /**
     * JButton's triggering modification of the dom tree that was generated from
     * the xml file.
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
        threadStyleToolBar = new ThreadStyleToolBar(getBundle());
        fragments = new Fragments();
        delete = new Button(false, "TreeToolBar_delete");//$NON-NLS-1$
        replace = new Button(false, "TreeToolBar_replace"); //$NON-NLS-1$

        /* ---- connect non-menu components with listeners ---- */

        createListeners();

        /* ---- create global menus with listeners ---- */

        JMenuBar //

        jMenuBar = new JMenuBar();
        jMenuBar.add(new FileMenu());
        jMenuBar.add(new HelpMenu());

        setJMenuBar(jMenuBar);

        /* ---- load content ---- */

        loadSample(NEW_DIAGRAM);

        /* ---- put components and their local menu's/toolbars together ---- */

        JSplitPane//

        splitPane = new SplitPane(//
                TOTAL_LEFT_WIDTH - SplitPane.DIVIDER_WIDTH - (int) fragments.getMinimumSize().getWidth(), // dividerPosition
                HORIZONTAL_SPLIT, // 
                new Panel( // component of spiltPane
                        new AbstractButton[] { delete, replace }, // toolbar
                        new JScrollPane(tree)),
                new Panel( // component of spiltPane
                        new FragmentsViewMenu(), // toolbar
                        fragments)); 

        splitPane = new SplitPane(//
                1000, // dividerPosition
                VERTICAL_SPLIT, // orientation
                splitPane, // 
                new Panel( //
                        new EditMenu(), // toolbar
                        new JScrollPane(source))); // 

        splitPane = new SplitPane(//
                TOTAL_LEFT_WIDTH, // dividerPosition
                HORIZONTAL_SPLIT, // orientation
                splitPane, // left component of spiltPane
                new Panel(
                        // 
                        new ButtonBar(//
                                threadStyleToolBar,//
                                new JMenu[] { new DiagramPrintMenu(), new DiagramViewMenu() }),
                        new JScrollPane(diagramPanel)));

        getContentPane().add(splitPane);
    }

    /** Connects the non-menu components with listeners */
    private void createListeners() {

        diagramPanel.setDiagramType(true, false);
        diagramPanel.addMouseMotionListener(mouseMotionListener);

        delete.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                tree.deleteSelected();
            }
        });

        replace.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                tree.replaceSelected(fragments.getSelectedElement());
            }
        });

        fragments.addSelectionListener(fragments.new SelectionListener() {

            void selectedValueChanged() {
                replace.setEnabled(BWTree.elementsMatch(//
                        tree.getSelectedElement(), //
                        fragments.getSelectedElement()));
            }
        });

        tree.getModel().addTreeModelListener(new TreeModelListener() {

            public void treeNodesChanged(TreeModelEvent e) {
                BWTree.restoreOrphans(e);
                diagramPanel.setPattern(tree.getRootElement());
                fragments.populate(tree.getRoot());
            }

            public void treeNodesInserted(TreeModelEvent e) {
                BWTree.restoreOrphans(e);
                diagramPanel.setPattern(tree.getRootElement());
                fragments.populate(tree.getRoot());
            }

            public void treeNodesRemoved(TreeModelEvent e) {
                BWTree.restoreOrphans(e);
                diagramPanel.setPattern(tree.getRootElement());
                fragments.populate(tree.getRoot());
            }

            public void treeStructureChanged(TreeModelEvent e) {
                BWTree.restoreOrphans(e);
                diagramPanel.setPattern(tree.getRootElement());
                fragments.populate(tree.getRoot());
            }
        });

        diagramPanel.addMouseListener(new MouseAdapter() {

            public void mouseClicked(MouseEvent e) {
                if (isLeftMouseButton(e)) {
                    diagramPanel.setThreadStyleAt(//
                            threadStyleToolBar.getStyleOfFrontThread(), //
                            e.getX(), //
                            e.getY());
                } else if (isRightMouseButton(e)) {
                    threadStyleToolBar.setStyleOfFrontThread//
                            (diagramPanel.getThreadStyleAt(e.getX(), e.getY()));
                }
            }
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

    /** A JPanel with a additional convenience constructors. */
    private class Panel extends JPanel {

        /** Convenience JPanel constructor */
        private Panel(AbstractButton[] buttons, Component content) {
            super(new BorderLayout());
            add(new ButtonBar(buttons), PAGE_START);
            add(content, CENTER);
        }

        /** Convenience JPanel constructor */
        private Panel(JMenu menu, Component content) {
            super(new BorderLayout());
            add(new MenuBar(menu), PAGE_START);
            add(content, CENTER);
        }

        /** Convenience JPanel constructor */
        private Panel(Component toolBar, Component content) {
            super(new BorderLayout());
            add(toolBar, PAGE_START);
            add(content, CENTER);
        }
    }

    /** A JToolbar with an additional convenience constructor. */
    private class SplitPane extends JSplitPane {

        static final int DIVIDER_WIDTH = 8;

        /** Convenience JSplitPane constructor */
        private SplitPane(int dividerposition, int orientation, Component Left, Component Right) {
            super(orientation, Left, Right);
            if (getDividerSize() < DIVIDER_WIDTH) {
                setDividerSize(DIVIDER_WIDTH);
            }
            setBorder(null); // prevent nested borders
            setDividerLocation(dividerposition);
            setOneTouchExpandable(true);
        }
    }

    /** A JToolbar with a additional convenience constructors. */
    private class ButtonBar extends JToolBar {

        /** Convenience JToolBar constructor */
        private ButtonBar(Component[] components) {
            setFloatable(false);
            setRollover(true);
            setBorder(null);

            for (Component component : components) {
                add(component);
            }
        }

        /** Convenience JToolBar constructor */
        private ButtonBar(JToolBar toolBar, JMenu[] menu) {
            setFloatable(false);
            setRollover(true);
            setBorder(null);

            add(new MenuBar(menu));
            add(Box.createHorizontalGlue());
            add(toolBar);
        }
    }

    /** A JMenubar with a additional convenience constructors. */
    private class MenuBar extends JMenuBar {

        /** Convenience JMenuBar constructor */
        private MenuBar(JMenuItem[] items) {
            for (AbstractButton item : items) {
                add(item);
            }
        }

        /** Convenience JMenuBar constructor */
        private MenuBar(JMenuItem item) {
            add(item);
        }
    }

    /** A JButton with an additional convenience constructor. */
    private class Button extends JButton {
        /**
         * Convenience JButton constructor
         * 
         * @param enabled
         *            initial enablement of the button
         * @param key
         *            for the Localizer bundle for caption, hint etc.
         */
        private Button(boolean enabled, String key) {
            super();
            applyStrings(this, key); //$NON-NLS-1$
            setEnabled(enabled);
        }
    }

    /** A fully dresses JMenu, extendable for the applet */
    private class FileMenu extends JMenu implements AppletApplicationMenu {

        /** Creates a JMenu with items that load a predefined file. */
        private FileMenu() {

            ActionListener loadSampleActionListener = new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    loadSample(e.getActionCommand());
                }
            };
            applyStrings(this, "MenuFile_file"); //$NON-NLS-1$

            JMenuItem//
            jMenuItem = (JMenuItem) applyStrings(new JMenuItem(), "MenuFile_New"); //$NON-NLS-1$ 
            jMenuItem.setAccelerator(getKeyStroke(VK_N, CTRL_MASK));
            jMenuItem.setActionCommand(NEW_DIAGRAM);
            jMenuItem.addActionListener(loadSampleActionListener);
            add(jMenuItem);
        }

        /**
         * Extends the JMenu with an exit at the bottom, and open/save/save-as
         * at the top.
         */
        public void extend() {

            add(new JSeparator());

            JMenuItem//
            jMenuItem = (JMenuItem) applyStrings(new JMenuItem(), "MenuFile_exit"); //$NON-NLS-1$
            jMenuItem.setAccelerator(getKeyStroke(VK_F4, ALT_DOWN_MASK));
            jMenuItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    System.exit(0);
                }
            });
            add(jMenuItem);

            insertSeparator(0);

            jMenuItem = (JMenuItem) applyStrings(new JMenuItem(), "MenuFile_SaveAs"); //$NON-NLS-1$
            jMenuItem.setActionCommand("saveAs"); //$NON-NLS-1$
            jMenuItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    saveFileAs();
                }
            });
            insert(jMenuItem, 0);

            jMenuItem = (JMenuItem) applyStrings(new JMenuItem(), "MenuFile_save"); //$NON-NLS-1$
            jMenuItem.setAccelerator(getKeyStroke(VK_S, CTRL_DOWN_MASK));
            jMenuItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    fileHandler.save(source.getText());
                }
            });
            insert(jMenuItem, 0);

            jMenuItem = (JMenuItem) applyStrings(new JMenuItem(), "MenuFile_open"); //$NON-NLS-1$
            jMenuItem.setAccelerator(getKeyStroke(VK_O, CTRL_DOWN_MASK));
            jMenuItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    loadFile();
                }
            });
            insert(jMenuItem, 0);
        }
    }

    /** A fully dressed JMenu, controlling the view of the diagram */
    private class DiagramPrintMenu extends JMenu {

        /** Creates a fully dressed JMenu, controlling the view of the diagram */
        private DiagramPrintMenu() {

            applyStrings(this, "MenuPrint"); //$NON-NLS-1$

            add(new javax.swing.JSeparator());
            JMenuItem//
            jMenuItem = (JMenuItem) applyStrings(new JMenuItem(), "MenuPrint_PageSetup"); //$NON-NLS-1$
            jMenuItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent arg0) {
                    diagramPanel.updatePageFormat();
                }
            });
            add(jMenuItem);

            jMenuItem = (JMenuItem) applyStrings(new JMenuItem(), "MenuPrint_print"); //$NON-NLS-1$
            jMenuItem.setAccelerator(getKeyStroke(VK_P, CTRL_DOWN_MASK));
            jMenuItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent arg0) {
                    diagramPanel.adjustablePrint();
                }
            });
            add(jMenuItem);
        }
    }

    /** A fully dressed JMenu, to edit the XML source */
    private class EditMenu extends JMenu {

        /** Creates a fully dressed JMenu, to edit the XML source */
        private EditMenu() {

            applyStrings(this, "MenuEdit_edit"); //$NON-NLS-1$

            JMenuItem//
            jMenuItem = (JMenuItem) applyStrings(new JMenuItem(), "MenuEdit_show"); //$NON-NLS-1$
            jMenuItem.setAccelerator(getKeyStroke(VK_F5, 0));
            jMenuItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    // String fileName = tree.getDocName();
                    tree.setDoc(source.getText());
                    // tree.setDocName(fileName);
                }
            });
            add(jMenuItem);

            add(new javax.swing.JSeparator());

            jMenuItem = (JMenuItem) applyStrings(new JMenuItem(), "MenuEdit_InsertColor"); //$NON-NLS-1$
            jMenuItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    source.insertColor();
                }
            });
            add(jMenuItem);

            add(new javax.swing.JSeparator());

            jMenuItem = (JMenuItem) applyStrings(new JMenuItem(), "MenuEdit_cut"); //$NON-NLS-1$ 
            jMenuItem.setAccelerator(getKeyStroke(VK_X, CTRL_DOWN_MASK));
            jMenuItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    source.cut();
                }
            });
            add(jMenuItem);

            jMenuItem = (JMenuItem) applyStrings(new JMenuItem(), "MenuEdit_copy"); //$NON-NLS-1$
            jMenuItem.setAccelerator(getKeyStroke(VK_C, CTRL_DOWN_MASK));
            jMenuItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    source.copy();
                }
            });
            add(jMenuItem);

            jMenuItem = (JMenuItem) applyStrings(new JMenuItem(), "MenuEdit_paste"); //$NON-NLS-1$
            jMenuItem.setAccelerator(getKeyStroke(VK_V, CTRL_DOWN_MASK));
            jMenuItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    source.paste();
                }
            });
            add(jMenuItem);

            jMenuItem = (JMenuItem) applyStrings(new JMenuItem(), "MenuEdit_paste"); //$NON-NLS-1$ 
            jMenuItem.setAccelerator(getKeyStroke(VK_V, CTRL_DOWN_MASK));
            jMenuItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    source.paste();
                }
            });
        }
    }

    /** A JMenu with more item for an application than for an applet */
    private interface AppletApplicationMenu {
        /** Adds items to the menu that are not allowed in applets */
        void extend();
    }

    /** A fully dressed JMenu, controlling the view of the fragments */
    private class FragmentsViewMenu extends JMenu {

        /** Creates a fully dressed JMenu, controlling the view of the fragments */
        private FragmentsViewMenu() {
            applyStrings(this, "MenuFragments_fragments"); //$NON-NLS-1$

            JMenuItem//
            jMenuItem = (JMenuItem) applyStrings(new JMenuItem(), "MenuView_thread"); //$NON-NLS-1$ 
            jMenuItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent arg0) {
                    fragments.setDiagramType(false, true);
                }
            });
            add(jMenuItem);

            jMenuItem = (JMenuItem) applyStrings(new JMenuItem(), "MenuView_pair"); //$NON-NLS-1$ 
            jMenuItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent arg0) {
                    fragments.setDiagramType(true, false);
                }
            });
            add(jMenuItem);

            jMenuItem = (JMenuItem) applyStrings(new JMenuItem(), "MenuView_hybrid"); //$NON-NLS-1$ 
            jMenuItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent arg0) {
                    fragments.setDiagramType(true, true);
                }
            });
            add(jMenuItem);
        }
    }

    /** A fully dressed JMenu, extendable for the application */
    private class HelpMenu extends JMenu implements AppletApplicationMenu {

        /**
         * Creates a fully dressed JMenu, with items showing internal
         * information in dialogs
         */
        private HelpMenu() {

            applyStrings(this, "MenuHelp_help"); //$NON-NLS-1$
            add(new javax.swing.JSeparator());
            add(new javax.swing.JSeparator());

            JMenuItem//

            jMenuItem = (JMenuItem) applyStrings(new JMenuItem(), "MenuHelp_About"); //$NON-NLS-1$
            jMenuItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent arg0) {
                    JOptionPane.showMessageDialog(self, aboutInfo.getMessage(), aboutInfo.getCaption(),
                            JOptionPane.INFORMATION_MESSAGE);
                }
            });
            add(jMenuItem);
        }

        private class BrowsingActionListener implements ActionListener {
        	
        	private String url;
        	
        	BrowsingActionListener (String url) {
        		this.url = url;
        	}

            public void actionPerformed(ActionEvent e) {
                if (!NativeBrowser.show(url)) {
                    javax.swing.JOptionPane.showMessageDialog(//
                            self,//
                            "browser not found" + "\n\n" + url, //$NON-NLS-2$
                            getString("MenuHelp_userGuide"), //$NON-NLS-1$
                            javax.swing.JOptionPane.INFORMATION_MESSAGE);
                }
            }
        }

        /** Convenience method: extends the menu with a browser item. */
        private void extend(String text, String code) {

            extend(1,text, "http://bw-"+code+".wikispaces.com/");  //$NON-NLS-1$ $NON-NLS-2$
        }        

        /** Convenience method: extends the menu with a browser item. */
        private void extend(int index, String text, String url) {

            JMenuItem //
            jMenuItem = new JMenuItem();
            jMenuItem.setText(text);  //$NON-NLS-1$
			jMenuItem.addActionListener(new BrowsingActionListener(url));
            insert(jMenuItem, index);
        }
        
        /** Extends the applet version of the menu with items that launch a browser. */
        public void extend() {

            extend("Nederlands","nl");  //$NON-NLS-1$ $NON-NLS-2$
            extend("Français","fr");  //$NON-NLS-1$ $NON-NLS-2$
            extend("Español","es");  //$NON-NLS-1$ $NON-NLS-2$
            extend("English","en");  //$NON-NLS-1$ $NON-NLS-2$
            extend("Deutsch","de");  //$NON-NLS-1$ $NON-NLS-2$
            //myBundle().getString(keyBase)
            extend(0,"download updates", "http://code.google.com/p/bobbinwork/downloads/list/");  //$NON-NLS-2$
            extend(0,"download examples", "http://groups.google.com/group/bobbinwork/files/");  //$NON-NLS-2$
        }
    }

    /** A fully dressed JMenu, controlling the view of the diagram */
    private class DiagramViewMenu extends JMenu {


        /**
         * A fully dressed JMenu, controlling the appearance of the high lights on
         * the diagram
         */
        private class DiagramHighlightsMenu extends JMenu {

            private DiagramHighlightsMenu() {
                JMenuItem//

                jMenuItem = (JMenuItem) applyStrings(new JMenuItem(), "MenuHighlight_AreaColor"); //$NON-NLS-1$
                jMenuItem.setBackground(new Color(diagramPanel.getAreaHighlight().getRGB()));
                jMenuItem.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        Color color = JColorChooser.showDialog(//
                                self, getString("Dialog_AreaHiglight"), diagramPanel.getAreaHighlight());
                        if (color != null) {
                            ((JMenuItem) e.getSource()).setBackground(color);
                            diagramPanel.setAreaHighlight(color);
                        }
                    }
                });
                add(jMenuItem);

                jMenuItem = (JMenuItem) applyStrings(new JMenuItem(), "MenuHighlight_ThreadColor"); //$NON-NLS-1$
                jMenuItem.setBackground(new Color(diagramPanel.getThreadHighlight().getRGB()));
                jMenuItem.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        Color color = JColorChooser.showDialog(//
                                self, getString("Dialog_ThreadHighlight"), diagramPanel.getThreadHighlight());
                        if (color != null) {
                            ((JMenuItem) e.getSource()).setBackground(color);
                            diagramPanel.setThreadHighlight(color);
                        }
                    }
                });
                add(jMenuItem);
            }
        }

        /** Creates a fully dressed JMenu, controlling the view of the diagram */
        private DiagramViewMenu() {
            applyStrings(this, "MenuView_view"); //$NON-NLS-1$

            JMenuItem//

            jMenuItem = (JMenuItem) applyStrings(new JMenuItem(), "MenuView_zoomIn"); //$NON-NLS-1$
            jMenuItem.setAccelerator(getKeyStroke(VK_I, CTRL_DOWN_MASK));
            jMenuItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent arg0) {
                    diagramPanel.setScreenScale(diagramPanel.getScreenScale() * 1.25);
                }
            });
            add(jMenuItem);

            jMenuItem = (JMenuItem) applyStrings(new JMenuItem(), "MenuView_zoomOut"); //$NON-NLS-1$
            jMenuItem.setAccelerator(getKeyStroke(VK_J, CTRL_DOWN_MASK));
            jMenuItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent arg0) {
                    diagramPanel.setScreenScale(diagramPanel.getScreenScale() * 0.8);
                }
            });
            add(jMenuItem);

            jMenuItem = (JMenuItem) applyStrings(new JMenuItem(), "MenuView_zoomReset"); //$NON-NLS-1$ 
            jMenuItem.setAccelerator(getKeyStroke(VK_K, CTRL_DOWN_MASK));
            jMenuItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent arg0) {
                    diagramPanel.setScreenScale(1d);
                }
            });
            add(jMenuItem);

            add(new javax.swing.JSeparator());

            add((DiagramHighlightsMenu) applyStrings(new DiagramHighlightsMenu(), "MenuView_highlight"));

            add(new javax.swing.JSeparator());

            jMenuItem = (JMenuItem) applyStrings(new JMenuItem(), "MenuView_thread"); //$NON-NLS-1$
            jMenuItem.setAccelerator(getKeyStroke(VK_F7, SHIFT_DOWN_MASK));
            jMenuItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent arg0) {
                    diagramPanel.setDiagramType(true, false);
                    if (diagramPanel.getMouseMotionListeners().length <= 0) {
                        diagramPanel.addMouseMotionListener(mouseMotionListener);
                    }
                    threadStyleToolBar.setVisible(true);
                }
            });
            add(jMenuItem);

            jMenuItem = (JMenuItem) applyStrings(new JMenuItem(), "MenuView_pair"); //$NON-NLS-1$
            jMenuItem.setAccelerator(getKeyStroke(VK_F7, 0));
            jMenuItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent arg0) {
                    diagramPanel.setDiagramType(false, true);
                    if (diagramPanel.getMouseMotionListeners().length > 0) {
                        diagramPanel.removeMouseMotionListener(mouseMotionListener);
                    }
                    threadStyleToolBar.setVisible(false);
                }
            });
            add(jMenuItem);

            jMenuItem = (JMenuItem) applyStrings(new JMenuItem(), "MenuView_hybrid"); //$NON-NLS-1$
            jMenuItem.setAccelerator(getKeyStroke(VK_F7, CTRL_DOWN_MASK));
            jMenuItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent arg0) {
                    diagramPanel.setDiagramType(true, true);
                    if (diagramPanel.getMouseMotionListeners().length <= 0) {
                        diagramPanel.addMouseMotionListener(mouseMotionListener);
                    }
                    threadStyleToolBar.setVisible(true);
                }
            });
            add(jMenuItem);
        }
    }

    private final MouseMotionListener mouseMotionListener = new MouseMotionListener() {

        public void mouseDragged(MouseEvent arg0) {
            // no dragging
        }

        public void mouseMoved(MouseEvent e) {
            diagramPanel.highlightThreadAt(e.getX(), e.getY(), tree.getSelectedPartition());
        }

    };

    /** manages the last opened/saved file */
    private BWFileHandler fileHandler;

    /** Loads a file into the source and tree. */
    private void loadSample(String fileName) {

        InputStream stream = getClass().getClassLoader().getResourceAsStream(fileName);
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
     * Convinience API for JOptionPane.showMessageDialog to show the exception
     * message to the user
     */
    private void showError(String fileName, Exception exception, String action) {
        JOptionPane.showMessageDialog(this, //
                fileName + "\n" + exception.getLocalizedMessage(),//
                action, //
                JOptionPane.ERROR_MESSAGE);
    }

    /** Save the content of source in a file with a name selected by the user */
    private void saveFileAs() {
        fileHandler.saveAs(source.getText());
        tree.setDocName(fileHandler.getFileName());
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

    /**
     * @param args
     */
    public static void main(String[] args) {

        BWVApplet applet = new BWVApplet();

        // override the default of the applet
        if (args.length > 0) {
            setBundle(LOCALIZER_BUNDLE_NAME, new Locale(args[0]));
        }

        applet.fileHandler = new BWFileHandler(applet, //
            new BWFileFilter( getString("FileType"), "xml,bwml".split(",") )); //$NON-NLS-1$ //$NON-NLS-2$  //$NON-NLS-3$
                        

        // Initialize the applet
        applet.init();

        // extend the menu with I/O items (not allowed for applets)
        for (Component menu : applet.getJMenuBar().getComponents()) {
            ((AppletApplicationMenu) menu).extend();
        }

        // create a frame with the applet on it
        JFrame frame = new JFrame();
        frame.setSize(700, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle(aboutInfo.getCaption() + " " + aboutInfo.getVersion()); //$NON-NLS-1$
        frame.setIconImage(applet.icon);
        frame.add(applet);
        frame.setVisible(true);
    }
}
