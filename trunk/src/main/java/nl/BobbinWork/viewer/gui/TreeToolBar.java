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
import nl.BobbinWork.viewer.guiUtils.LocaleButton;

class TreeToolBar
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

  /** to be manipulated by the buttons */
  private Partition current = null;

  /** to be repainted when a change was made to a diagram {@link #current} */
  private JComponent source = null;

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
      if (current == null) return;
      final Graphics2D g2 = DiagramPainter.fit(g,current.getBounds(),this);
      DiagramPainter.paint (g2,current.getThreads());
    }
  };

  /**
   * Creates a tool bar instance that can listen to one {@link DiagramTree}
   * instance.
   */
  TreeToolBar()
  {
    setFloatable( false );

    // TODO equal width buttons / listeners
    final JComponent buttons = new JPanel();
    buttons.setLayout( new BoxLayout( buttons, BoxLayout.PAGE_AXIS ) );
    buttons.add( copy );
    buttons.add( paste );
    buttons.add( delete );
    buttons.add( showHide );

    add( buttons );
    add( clipBoard );
    applyStrings( clipBoard, "Clipboard" );
    clipBoard.setBackground( Color.white );

    copy.addActionListener( new ActionListener()
    {

      @Override
      public void actionPerformed(
          final ActionEvent arg0)
      {
        copied = current;
        clipBoard.repaint();
      }
    } );
    showHide.addActionListener( new ActionListener()
    {
      @Override
      public void actionPerformed(
          final ActionEvent event)
      {
        current.setVisible( !current.isVisible() );
        source.repaint();
        setShowHideCaption();
      }
    } );
  }

  @Override
  public void valueChanged(
      final TreeSelectionEvent event)
  {
    source = (JComponent) event.getSource();
    current = getSelectedPartition( event.getPath() );
    if (current == null) {
      copy.setEnabled( false );
      delete.setEnabled( false );
      paste.setEnabled( false );
      showHide.setEnabled( false );
    } else {
      copy.setEnabled( current.isVisible() );
      delete.setEnabled( true );
      showHide.setEnabled( true );
      paste.setEnabled( copied != null 
          && copied.getNrOfPairs() == current.getNrOfPairs() );
    }
    setShowHideCaption();
  }

  private void setShowHideCaption()
  {
    String caption = "";
    if (current == null) {
      caption = Localizer.getString( SHOW_HIDE );
    } else if (current.isVisible()) {
      caption = Localizer.getString( HIDE );
    } else {
      caption = Localizer.getString( SHOW );
    }
    showHide.setText( caption );
  }
}