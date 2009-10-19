/* BWViewer.java Copyright 2006-2008 by J. Pol
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

import static nl.BobbinWork.bwlib.gui.Localizer.setBundle;

import java.io.*;
import java.net.*;
import java.util.Locale;

import javax.swing.JScrollPane;

import nl.BobbinWork.bwlib.gui.BWFrame;

/**
 * Show the xml based tree in a frame for a visual test.
 * 
 * @author Joke Pol
 * 
 */
public class XmlTreeTester
{
  private static final String LOCALIZER_BUNDLE_NAME = "nl/BobbinWork/viewer/gui/labels";         //$NON-NLS-1$

  private static final String DIAGRAM               = "nl/BobbinWork/diagram/xml/newDiagram.xml"; //$NON-NLS-1$

  private static String readFile(
      URI uri) throws IOException, URISyntaxException
  {
    final File file = new File( uri );
    final InputStream inputStream = new FileInputStream( file );

    final byte[] buffer = new byte[(int) file.length()];
    inputStream.read( buffer );
    inputStream.close();
    return new String( buffer );
  }

  public static void main(
      String[] args)
  {
    if (args.length > 0)
      setBundle( LOCALIZER_BUNDLE_NAME, new Locale( args[0] ) );
    try {
      BWFrame frame = new BWFrame( LOCALIZER_BUNDLE_NAME );
      URI uri = XmlTree.class.getClassLoader().getResource( DIAGRAM ).toURI();
      XmlTree tree = new XmlTree( uri.toString(), readFile( uri ) );
      frame.getContentPane().add( new JScrollPane( tree ) );
      frame.setVisible( true );

    } catch (Exception exception) {
      exception.printStackTrace();
    }
  }
}
