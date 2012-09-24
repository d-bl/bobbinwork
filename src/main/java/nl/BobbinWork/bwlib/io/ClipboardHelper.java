/* FileMenu.java Copyright 2006-2007 by J. Pol
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
package nl.BobbinWork.bwlib.io;

import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

public class ClipboardHelper
{
  public static void pasteText(
      final InputStreamHandler inputStreamHandler)
  {
    final Transferable contents =
        Toolkit.getDefaultToolkit().getSystemClipboard().getContents( null );
    if (contents == null) return;
    try {
      final String text = (String) contents.getTransferData( DataFlavor.stringFlavor );
      final InputStream is = new ByteArrayInputStream( text.getBytes( "UTF-8" ) );
      inputStreamHandler.proces( is );
    } catch (final UnsupportedFlavorException exception) {
      // empty clipboard
    } catch (final UnsupportedEncodingException exception) {
      System.out.println( exception );
      exception.printStackTrace();
    } catch (final IOException exception) {
      System.out.println( exception );
      exception.printStackTrace();
    }
  }
}
