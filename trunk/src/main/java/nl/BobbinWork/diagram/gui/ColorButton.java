package nl.BobbinWork.diagram.gui;

import static nl.BobbinWork.bwlib.gui.Localizer.applyStrings;
import static nl.BobbinWork.bwlib.gui.Localizer.getString;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.colorchooser.ColorChooserComponentFactory;

class ColorButton
    extends JButton
{
  public interface OkListener
  {
    void colorChosen(
        Color color);
  };

  private static final long serialVersionUID = 1L;
  private final JDialog dialog;
  private final JColorChooser chooser = new JColorChooser();

  ColorButton(
      final String iconFileName,
      final String keyBase,
      final Component parent,
      final OkListener okListener)
  {
    
    final ActionListener buttonListener = new ActionListener()
    {
      @Override
      public void actionPerformed(
          final ActionEvent event)
      {
        dialog.setVisible( true );
      }
    };
    
    final ActionListener dialogListener = new ActionListener()
    {
      @Override
      public void actionPerformed(
          final ActionEvent event)
      {
        okListener.colorChosen( chooser.getColor() );
      }
    };

    final String dialogTitle = dressUp( iconFileName, keyBase );
    
    addActionListener( buttonListener );
    chooser.setChooserPanels( ColorChooserComponentFactory
        .getDefaultChooserPanels() );
    dialog =
        JColorChooser.createDialog( parent, dialogTitle, true, chooser,
            dialogListener, null );
  }

  private String dressUp(
      final String iconFileName,
      final String keyBase)
  {
    setRequestFocusEnabled( false );
    applyStrings( this, keyBase );
    final String dialogTitle = getString( keyBase + "_dialog_title" );
    final URL url = ThreadStyleToolBar.class.getResource( iconFileName );
    setIcon( new ImageIcon( url ) );
    return dialogTitle;
  }
}
