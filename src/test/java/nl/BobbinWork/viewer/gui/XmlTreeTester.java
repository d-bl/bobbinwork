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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.Locale;

import javax.swing.JScrollPane;
import javax.xml.parsers.ParserConfigurationException;

import nl.BobbinWork.bwlib.gui.BWFrame;

import org.xml.sax.SAXException;

/**
 * Show the xml based tree in a frame for a visual test. 
 * 
 * @author Joke Pol
 *
 */
public class XmlTreeTester
{

  private static final String LOCALIZER_BUNDLE_NAME = "nl/BobbinWork/viewer/gui/labels";         //$NON-NLS-1$

  private static final String NEW_DIAGRAM           = "nl/BobbinWork/diagram/xml/newDiagram.xml"; //$NON-NLS-1$

  private BWFrame             frame                 = new BWFrame(
                                                        LOCALIZER_BUNDLE_NAME );

  public XmlTreeTester(String name)
      throws ParserConfigurationException, SAXException, IOException, URISyntaxException
  {
    XmlTree tree = new XmlTree();
    tree.setDoc( readFile(name) );
    tree.setDocName( name );

    frame.getContentPane().add( new JScrollPane( tree ) );
  }

  private static String readFile(
      String name) throws IOException, URISyntaxException
  {
    final InputStream inputStream = XmlTree.class.getClassLoader()
        .getResourceAsStream( name );
    java.net.URI uri = XmlTree.class.getClassLoader().getResource( name ).toURI();

    final byte[] buffer = new byte[(int) new File( uri ).length()];
    inputStream.read( buffer );
    inputStream.close();
    return new String (buffer);
  }

  public static void main(
      String[] args)
  {
    if (args.length > 0)
      setBundle( LOCALIZER_BUNDLE_NAME, new Locale( args[0] ) );
    try {
      new XmlTreeTester(NEW_DIAGRAM).frame.setVisible( true );
    } catch (Exception exception) {
      exception.printStackTrace();
    }
  }
}
