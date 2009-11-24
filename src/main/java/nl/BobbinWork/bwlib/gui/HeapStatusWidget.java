/*
 * BobbinWork
 *
 * Copyright (C) 2009 J. Pol
 *
 * This piece of code is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License as
 * published by the Free Software Foundation; either version 3 of the
 * License, or (at your option) any later version.
 *
 * This code is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package nl.BobbinWork.bwlib.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.MessageFormat;

import javax.swing.JProgressBar;
import javax.swing.Timer;

/** An updating widget showing memory consumption */
public class HeapStatusWidget
    extends JProgressBar
{

  private static final MessageFormat TOOL_TIP =
      new MessageFormat( propertyToHtml( Localizer.getString( "heap_tooltip" ) ) );

  private static final MessageFormat CAPTION =
      new MessageFormat( Localizer.getString( "heap_caption" ) );

  private static final long serialVersionUID = 1L;

  public HeapStatusWidget()
  {
    super( 0, 100 );
    setValue( 0 );
    setStringPainted( true );
    new Timer( 3000, new ActionListener()
    {
      public void actionPerformed(
          ActionEvent e)
      {
        update();
      }
    } ).start();
  }

  private void update()
  {

    // calculate values
    Runtime r = Runtime.getRuntime();
    long max = r.maxMemory();
    long free = r.freeMemory();
    long total = r.totalMemory();
    long used = total - free;
    int percent = (int) Math.round( used * 100D / max );

    String toolTip = TOOL_TIP.format( new String[] {
        format( used ), format( free ), format( max )
    } );
    String caption = CAPTION.format( new String[] {
        format( used ), "" + percent
    } );

    setValue( percent );
    setString( caption );
    super.setToolTipText( toolTip );
  }

  private static String propertyToHtml(
      String value)
  {
    return "<html><body>" + value.replaceAll( "[\r]?[\n][\r]?", "<br/>" )
    + "</body></html>";
  }
  
  private String format(
      long mb)
  {
    double val = mb / 1000000D;
    return Integer.toString( (int) Math.round( val ) );
  }
}
