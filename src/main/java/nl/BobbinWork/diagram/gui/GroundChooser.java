/* GroundChooser.java Copyright 2006-2007 by J. Pol
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
package nl.BobbinWork.diagram.gui;

import static nl.BobbinWork.bwlib.gui.Localizer.applyStrings;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

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
          event.setSource(is);
          externalActionListener.actionPerformed(event);                  
        }
      });
      add(item);
    }
  }
}
