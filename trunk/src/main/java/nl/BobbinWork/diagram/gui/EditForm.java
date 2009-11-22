/* EditForm.java Copyright 2009 by J. Pol
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
 */package nl.BobbinWork.diagram.gui;

import static nl.BobbinWork.bwlib.gui.Localizer.applyStrings;
import static nl.BobbinWork.diagram.gui.TreeSelectionUtil.getSelectedPartition;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

import nl.BobbinWork.bwlib.gui.Localizer;
import nl.BobbinWork.diagram.model.Diagram;
import nl.BobbinWork.diagram.model.Partition;
import nl.BobbinWork.diagram.xml.DiagramRebuilder;

/**
 * A tool bar to manipulate the selected item of a {@link DiagramTree}.
 * 
 * @author Joke Pol
 * 
 */
class EditForm
    extends JToolBar
    implements TreeSelectionListener
{
  private static final String PREFIX = "TreeToolBar_";
  private static final String COPY = PREFIX + "copy";
  private static final String PASTE = PREFIX + "paste";
  private static final String DELETE = PREFIX + "delete";
  private static final String SHOW_HIDE = PREFIX + "showHide";
  private static final String HIDE = PREFIX + "hide";
  private static final String SHOW = PREFIX + "show";

  public interface DiagramReplacedListener
  {
    public void rebuild(
        final Diagram newDiagram);
  }

  private final DiagramReplacedListener diagramReplacedListener;

  /** shown in {@link #clipBoard} */
  private Partition copied = null;

  /** The selected diagram element. Subject to manipulations by the buttons */
  private Partition selected = null;

  /**
   * The source of the selection change events. To be repainted when a change
   * was made to the {@link #selected} element. In practice the source will be
   * the {@link DiagramTree} listened to.
   */
  private JComponent source = null;

  private final Component repaintOnDiagramChange;
  private final JButton showHide = createButton( SHOW_HIDE );
  private final JButton delete = createButton( DELETE );
  private final JButton paste = createButton( PASTE );
  private final JButton copy = createButton( COPY );
  private final JComponent clipBoard = new JPanel()
  {
    public void paintComponent(
        Graphics g)
    {
      super.paintComponent( g );
      if (copied == null) return;
      if (copied.isVisible()) {
        final Graphics2D g2 = DiagramPainter.fit( g, copied.getBounds(), this );
        DiagramPainter.paint( g2, copied.getThreads() );
      } else {
        paste.setEnabled( false );
        copied = null;
      }
    }
  };

  /**
   * Creates a tool bar instance for a {@link DiagramTree}.
   * 
   * @param repaintOnDiagramChange
   *          another component that should be repainted when the diagram model
   *          changes. Typically the panel containing the diagram.
   * @param diagramReplacedListener
   *          TODO
   */
  EditForm(
      final Component repaintOnDiagramChange,
      DiagramReplacedListener diagramReplacedListener)
  {
    this.repaintOnDiagramChange = repaintOnDiagramChange;
    this.diagramReplacedListener = diagramReplacedListener;
    setLayout( new GridLayout( 1, 0 ) );
    add( createButtonsPanel() );
    add( clipBoard );
    applyStrings( clipBoard, "Clipboard" );
    clipBoard.setBackground( Color.white );
    copy.addActionListener( createCopyListener() );
    paste.addActionListener( createPasteListener() );
    delete.addActionListener( createDeleteListener() );
    showHide.addActionListener( createShowHideListener() );
  }

  /**
   * Creates a listener for the button that replaces the selected node with the
   * clip board content.
   */
  private ActionListener createPasteListener()
  {
    return new ActionListener()
    {
      //@Override
      public void actionPerformed(
          final ActionEvent event)
      {
        rebuild( DiagramRebuilder.replace( selected, copied ) );
      }

    };
  }

  /**
   * Creates a listener for the button that replaces the selected node with the
   * clip board content.
   */
  private ActionListener createDeleteListener()
  {
    return new ActionListener()
    {
      //@Override
      public void actionPerformed(
          final ActionEvent event)
      {
        rebuild( DiagramRebuilder.delete( selected ) );
      }
    };
  }

  private void rebuild(
      Diagram newDiagram)
  {
    if (newDiagram != null && diagramReplacedListener != null) {
      diagramReplacedListener.rebuild( newDiagram );
    }
  }

  /**
   * Creates a listener for the button that toggles visibility of the selected
   * diagram element.
   */
  private ActionListener createShowHideListener()
  {
    return new ActionListener()
    {
      //@Override
      public void actionPerformed(
          final ActionEvent event)
      {
        if (selected != null) selected.setVisible( !selected.isVisible() );
        source.repaint();
        if (copied == selected) {
          clipBoard.repaint();
        }
        setShowHideCaption();
        repaintOnDiagramChange.repaint();
      }
    };
  }

  /**
   * Creates a listener for the button that copies the selected diagram element
   * to the clip board.
   */
  private ActionListener createCopyListener()
  {
    return new ActionListener()
    {
      //@Override
      public void actionPerformed(
          final ActionEvent event)
      {
        copied = selected;
        clipBoard.repaint();
      }
    };
  }

  private JComponent createButtonsPanel()
  {
    final JComponent buttons = new JPanel();
    buttons.setLayout( new GridLayout( 0, 1 ) );
    buttons.add( copy );
    buttons.add( paste );
    buttons.add( delete );
    buttons.add( showHide );
    return buttons;
  }

  //@Override
  public void valueChanged(
      final TreeSelectionEvent event)
  {
    source = (JComponent) event.getSource();
    selected = getSelectedPartition( event.getPath() );
    if (selected == null) {
      copy.setEnabled( false );
      delete.setEnabled( false );
      paste.setEnabled( false );
      showHide.setEnabled( false );
    } else {
      copy.setEnabled( selected.isVisible()
          && DiagramRebuilder.canCopy( selected ) );
      delete.setEnabled( true );
      showHide.setEnabled( true );
      paste.setEnabled( copied != null //
          && copied != selected //
          && copied.getNrOfPairs() == selected.getNrOfPairs() //
          && DiagramRebuilder.canReplace( selected ) );
    }
    setShowHideCaption();
  }

  /**
   * Sets the caption of the button that toggles visibility of the selected
   * diagram element.
   */
  private void setShowHideCaption()
  {
    String caption = "";
    if (selected == null) {
      caption = Localizer.getString( SHOW_HIDE );
    } else if (selected.isVisible()) {
      caption = Localizer.getString( HIDE );
    } else {
      caption = Localizer.getString( SHOW );
    }
    showHide.setText( caption );
  }
  
  private JButton createButton(String key){
    JButton button = new JButton();
    applyStrings(button, key); 
    button.setEnabled(false);
    return button;  
  }
}