/**
 * 
 */
package nl.BobbinWork.viewer.gui;

import static nl.BobbinWork.bwlib.gui.Localizer.applyStrings;
import static nl.BobbinWork.viewer.gui.TreeSelectionUtil.getSelectedPartition;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.event.*;

import nl.BobbinWork.bwlib.gui.Localizer;
import nl.BobbinWork.diagram.gui.*;
import nl.BobbinWork.diagram.model.Partition;
import nl.BobbinWork.viewer.guiUtils.*;

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
  private final JButton showHide = new LocaleButton( false, SHOW_HIDE );
  private final JButton delete = new LocaleButton( false, DELETE );
  private final JButton paste = new LocaleButton( false, PASTE );
  private final JButton copy = new LocaleButton( false, COPY );
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
   */
  EditForm(final Component repaintOnDiagramChange)
  {
    this.repaintOnDiagramChange = repaintOnDiagramChange;
    setLayout( new GridLayout( 1, 0 ) );
    add( createButtonsPanel() );
    add( clipBoard );
    applyStrings( clipBoard, "Clipboard" );
    clipBoard.setBackground( Color.white );
    copy.addActionListener( createCopyListener() );
    showHide.addActionListener( createShowHideListener() );
  }

  /**
   * Creates a listener for the button that toggles visibility of the selected
   * diagram element.
   */
  private ActionListener createShowHideListener()
  {
    return new ActionListener()
    {
      @Override
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
      @Override
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

  @Override
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
      copy.setEnabled( selected.isVisible() );
      delete.setEnabled( true );
      showHide.setEnabled( true );
      paste.setEnabled( copied != null //
          && copied != selected //
          && copied.getNrOfPairs() == selected.getNrOfPairs() //
      );
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
}