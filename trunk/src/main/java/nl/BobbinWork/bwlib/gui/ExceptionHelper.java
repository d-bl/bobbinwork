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
package nl.BobbinWork.bwlib.gui;

import java.awt.Component;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.swing.JOptionPane;

import org.xml.sax.SAXException;

public class ExceptionHelper
{
  private static final String NEW_LINE = System.getProperty( "line.separator" );

  public static void show (Component parent, Throwable exception, String title){
    JOptionPane.showMessageDialog( parent, toString(exception), title,
        JOptionPane.ERROR_MESSAGE );

  }
  public static String toString(
      final Throwable exception)
  {
    final String msg =
        exception.getClass().getName() + NEW_LINE
            + exception.getLocalizedMessage();
    if (exception instanceof SAXException) return msg;

    final StringWriter stringWriter = new StringWriter();
    final PrintWriter printWriter = new PrintWriter( stringWriter );
    exception.printStackTrace( printWriter );
    printWriter.flush();
    stringWriter.flush();

    final String stackTrace = stringWriter.toString()//
        .replaceAll( "(?m)^\\tat java.awt[ -~]*[\\r\\n]+", "" )//
        .replaceAll( "(?m)^\\tat javax.swing[ -~]*[\\r\\n]+", "" );

    return msg + NEW_LINE + stackTrace;
  }
}
