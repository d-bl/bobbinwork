/* BWViewer.java Copyright 2006-2008 by J. Falkink-Pol
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

import static javax.swing.JSplitPane.HORIZONTAL_SPLIT;
import static javax.swing.JSplitPane.VERTICAL_SPLIT;
import static nl.BobbinWork.bwlib.gui.Localizer.applyStrings;
import static nl.BobbinWork.bwlib.gui.Localizer.getString;
import static nl.BobbinWork.bwlib.gui.Localizer.setBundle;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.GregorianCalendar;
import java.util.Locale;

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.Timer;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

import nl.BobbinWork.bwlib.gui.BWFrame;
import nl.BobbinWork.bwlib.gui.FileMenu;
import nl.BobbinWork.bwlib.gui.HelpMenu;
import nl.BobbinWork.bwlib.gui.LocaleMenuItem;
import nl.BobbinWork.bwlib.io.NamedInputStream;
import nl.BobbinWork.diagram.gui.DiagramPanel;
import nl.BobbinWork.diagram.gui.InteractiveDiagramPanel;
import nl.BobbinWork.diagram.model.Diagram;
import nl.BobbinWork.diagram.xml.expand.TreeExpander;
import nl.BobbinWork.viewer.guiUtils.CPanel;
import nl.BobbinWork.viewer.guiUtils.CursorController;
import nl.BobbinWork.viewer.guiUtils.LocaleButton;
import nl.BobbinWork.viewer.guiUtils.SplitPane;

import org.w3c.dom.Element;

public class BWViewer {

	private static final String YEARS = "2006-2008";  //$NON-NLS-1$  
	private static final String BASE_CAPTION = "Viewer"; //$NON-NLS-1$  

	private static final String LOCALIZER_BUNDLE_NAME = "nl/BobbinWork/viewer/gui/labels"; //$NON-NLS-1$

	private static final int TOTAL_LEFT_WIDTH = 300;
    private static final String NEW_DIAGRAM = "nl/BobbinWork/diagram/xml/newDiagram.xml"; //$NON-NLS-1$

    private BWFrame frame = new BWFrame(LOCALIZER_BUNDLE_NAME); 
    
	/** JTextArea with the XML source */
	private SourcePanel source;

	/** tree view of the XML elements */
	private BWTree tree;

	/** Fragments of the diagram that can be used again (referred to with &lt;copy of=".."&gt;) */
	private DiagramFragments fragments;

	/** Drawn by the model created from the DOM tree generated from the XML file. */
	private DiagramPanel diagramPanel;


	public BWViewer() {

		/* ---- create components and listeners ---- */

		source = new SourcePanel(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tree.setDoc(source.getText());
			}
		});
		tree = new BWTree();
		diagramPanel = new DiagramPanel();
		fragments = new DiagramFragments();
		JButton delete = new LocaleButton(false, "TreeToolBar_delete");//$NON-NLS-1$
		JButton replace = new LocaleButton(false, "TreeToolBar_replace"); //$NON-NLS-1$

		createNonMenuListeners(delete,replace);
		createGlobalMenus();
		loadFromStream(NEW_DIAGRAM, getClass().getClassLoader().getResourceAsStream(NEW_DIAGRAM));

		/* ---- put components and their local menu's/toolbars together ---- */

		int dividerPosition = TOTAL_LEFT_WIDTH 
		- SplitPane.DIVIDER_WIDTH 
		- (int) fragments.getMinimumSize().getWidth();

		JSplitPane splitPane;

		splitPane = new SplitPane(//
				dividerPosition, 
				HORIZONTAL_SPLIT, // 
				new CPanel( // component of spiltPane
						new AbstractButton[] { delete, replace }, // tool bar
						new JScrollPane(tree)),
				new CPanel( // component of spiltPane
						createFragmentsViewMenu(), // tool bar
						fragments)); 

		splitPane = new SplitPane(//
				1000, // dividerPosition
				VERTICAL_SPLIT, // orientation
				splitPane, // 
				source); // 

		splitPane = new SplitPane(//
				TOTAL_LEFT_WIDTH, // dividerPosition
				HORIZONTAL_SPLIT, // orientation
				splitPane, // left component of spiltPane
				new InteractiveDiagramPanel(diagramPanel, frame));

		frame.getContentPane().add(splitPane);
	}

	private void createGlobalMenus() {

		ActionListener inputStreamListener = new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				NamedInputStream is = (NamedInputStream)e.getSource();
				loadFromStream( is.getName(), is.getStream() );
		}};
		ActionListener saveListener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				saveFile((File)e.getSource());
			}

		};
		final HelpMenu helpMenu = new HelpMenu(frame, YEARS,BASE_CAPTION);
		frame.setTitle(helpMenu.getVersionedCaption());
		final FileMenu fileMenu = new FileMenu( inputStreamListener, saveListener, NEW_DIAGRAM);
		
		ActionListener inputStreamListener2 = new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				fileMenu.clearSelectedFile();
				NamedInputStream is = (NamedInputStream)e.getSource();
				loadFromStream( is.getName(), is.getStream() );
		}};

		JMenuBar jMenuBar;
		jMenuBar = new JMenuBar();
		jMenuBar.add(fileMenu);
		jMenuBar.add(new SampleDiagramChooser(frame,inputStreamListener2));
		jMenuBar.add(new GroundChooser(inputStreamListener2)); 
		jMenuBar.add(helpMenu); 
		frame.setJMenuBar(jMenuBar);
	}
	
	private void saveFile(File file) {
		try {
			PrintStream p = (new PrintStream(new FileOutputStream(file)));
			p.print(source.getText());
			p.flush();
			p.close();
		} catch (IOException ioe) {
			showError(file.toString(), ioe, "");
		}
		tree.setDocName(file.toString());
	}

	/** Connects the non-menu components with listeners */
	private void createNonMenuListeners(final JButton delete, final JButton replace) {

		delete.addActionListener(CursorController.createListener(frame,new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				tree.deleteSelected();
			}
		}));

		replace.addActionListener(CursorController.createListener(frame,new ActionListener() {

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

	/** A fully dressed JMenu, controlling the view of the fragments */
	/** Creates a fully dressed JMenu, controlling the view of the fragments */
	private JMenu createFragmentsViewMenu() {
		JMenu menu = new JMenu();

			applyStrings(menu, "MenuFragments_fragments"); //$NON-NLS-1$

			JMenuItem//
			jMenuItem = new LocaleMenuItem("MenuView_thread"); //$NON-NLS-1$ 
			jMenuItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					fragments.setDiagramType(false, true);
				}
			});
			menu.add(jMenuItem);

			jMenuItem = new LocaleMenuItem("MenuView_pair"); //$NON-NLS-1$ 
			jMenuItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					fragments.setDiagramType(true, false);
				}
			});
			menu.add(jMenuItem);

			jMenuItem = new LocaleMenuItem("MenuView_hybrid"); //$NON-NLS-1$ 
			jMenuItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					fragments.setDiagramType(true, true);
				}
			});
			menu.add(jMenuItem);
			return menu;
	}

	void loadFromStream(String fileName, InputStream stream) {
		if (stream != null) {
			try {
				source.read(new InputStreamReader(stream), fileName);
				tree.setDoc(source.getText());
				tree.setDocName(fileName);
			} catch (Exception exception) {
				showError(fileName, exception, getString("LOAD_ERROR_caption"));
				source.setText(null);
				tree.setDoc(""); //$NON-NLS-1$
			}

			try {
				stream.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void showError(String fileName, Exception exception, String action) {
		
		JOptionPane.showMessageDialog(frame, //
				fileName + "\n" + exception.getLocalizedMessage(), //$NON-NLS-1$
				action, //
				JOptionPane.ERROR_MESSAGE);
	}

	public static void main(String[] args) {

		if (args.length > 0) setBundle(LOCALIZER_BUNDLE_NAME, new Locale(args[0]));
		new BWViewer().frame.setVisible(true);
	}
}
