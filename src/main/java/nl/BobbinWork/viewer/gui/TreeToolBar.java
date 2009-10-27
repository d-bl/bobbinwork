/**
 * 
 */
package nl.BobbinWork.viewer.gui;

import static nl.BobbinWork.bwlib.gui.Localizer.applyStrings;
import static nl.BobbinWork.viewer.gui.TreeSelectionUtil.getSelectedPartition;

import javax.swing.*;
import javax.swing.event.*;

import nl.BobbinWork.bwlib.gui.Localizer;
import nl.BobbinWork.diagram.gui.DiagramPanel;
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

  private final DiagramPanel clipBoard = new DiagramPanel( null );
  private final JButton showHide = new LocaleButton( false, SHOW_HIDE );
  private final JButton delete = new LocaleButton( false, DELETE );
  private final JButton paste = new LocaleButton( false, PASTE );
  private final JButton copy = new LocaleButton( false, COPY );
  private Partition copied = null;

  TreeToolBar()
  {
    setFloatable( false );
    applyStrings( clipBoard, "Clipboard" );

    // TODO equal width buttons / listeners
    final JComponent buttons = new JPanel();
    buttons.setLayout( new BoxLayout( buttons, BoxLayout.PAGE_AXIS ) );
    buttons.add( copy );
    buttons.add( paste );
    buttons.add( delete );
    buttons.add( showHide );

    add( buttons );
    add( clipBoard );
  }

  @Override
  public void valueChanged(
      TreeSelectionEvent event)
  {
    final Partition partition = getSelectedPartition( event.getPath() );
    // TODO debug
    if (partition == null) {
      copy.setEnabled( false );
      delete.setEnabled( false );
      paste.setEnabled( false );
      if (showHide.isEnabled()) {
        showHide.setText( Localizer.getString( SHOW_HIDE ) );
        showHide.setEnabled( false );
      }
    } else {
      copy.setEnabled( true );
      delete.setEnabled( true );
      showHide.setEnabled( true );
      paste.setEnabled( copied != null
          && copied.getNrOfPairs() == partition.getNrOfPairs() );
      if (partition.isVisible()) {
        showHide.setText( Localizer.getString( HIDE ) );
      } else {
        showHide.setText( Localizer.getString( SHOW ) );
      }
    }
  }
}