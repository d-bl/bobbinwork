/*
 * BobbinWork
 *
 * Copyright (C) 1997 - 2002 Nils Meier <nils@meiers.net>
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
 * 
 * Original: 
 * http://genj.cvs.sourceforge.net/viewvc/genj/dev/app/src/core/genj/util/swing/HeapStatusWidget.java?revision=1.3&view=markup
 */
package nl.BobbinWork.bwlib.gui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.MessageFormat;

import javax.swing.JProgressBar;
import javax.swing.Timer;

/** An updating widget showing memory consumption */
public class HeapStatusWidget extends JProgressBar {
  
  private static final long serialVersionUID = 1L;

  private MessageFormat caption = new MessageFormat(Localizer.getString("heap_caption"));

  private MessageFormat tooltip = new MessageFormat(Localizer.getString("heap_tooltip"));

  public HeapStatusWidget() {
    super(0,100);
    setValue(0);
    setBorderPainted(true);
    setStringPainted(true);
    setBackground(new Color(0xEEEEFF));
    setForeground(new Color(0xCCCCFF));
    new Timer(3000, new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        update();
      }
    }).start();
  }
  
  /** update status */
  private void update() {
    
    // calculate values
    Runtime r = Runtime.getRuntime();
    long max = r.maxMemory();
    long free = r.freeMemory();
    long total = r.totalMemory();
    long used = total-free;
    int percent = (int)Math.round(used*100D/max);
    
    // set status
    setValue(percent);
    setString(caption.format(new String[]{ format(used), ""+percent}));

    // add tip
    super.setToolTipText(null);
    super.setToolTipText(tooltip.format(new String[]{ format(used), format(free), format(max)}));
  }
  
  private String format(long mb) {
    double val = mb/1000000D;
    return Integer.toString((int)Math.round(val));
  }

  /**
   * Allow to set tooltip with placeholders {0} for used memory, {1} for free memory, {2} max memory 
   */
  public void setToolTipText(String text) {
    // remember
    this.tooltip = new MessageFormat(text);
    // blank for now
    super.setToolTipText("");
  }
}
