package nl.BobbinWork.viewer.gui;

import static nl.BobbinWork.bwlib.gui.Localizer.applyStrings;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import nl.BobbinWork.bwlib.io.NamedInputStream;
import nl.BobbinWork.diagram.xml.Ground;


@SuppressWarnings("serial")
public class GroundChooser extends JMenu {

  public GroundChooser (final ActionListener externalActionListener){

    super();
    applyStrings(this, "MenuGround_Choose"); //$NON-NLS-1$

    for ( Ground g : Ground.values() ) {
      final JMenuItem item = new JMenuItem();
      item.setActionCommand( g.xmlString() );
      applyStrings(item, "MenuGround_"+g.name()); //$NON-NLS-1$
      item.addActionListener(new ActionListener () {

        public void actionPerformed(ActionEvent event) {
          InputStream is = new ByteArrayInputStream(item.getActionCommand().getBytes());
          event.setSource(new NamedInputStream("ground.xml",is));
          externalActionListener.actionPerformed(event);                  
        }
      });
      add(item);
    }
  }
}
