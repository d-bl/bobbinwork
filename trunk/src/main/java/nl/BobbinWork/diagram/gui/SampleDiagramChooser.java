/* SampleDiagramChooser.java Copyright 2006-2008 by J. Pol
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

import static nl.BobbinWork.bwlib.gui.Localizer.*;

import java.awt.Component;
import java.awt.event.*;
import java.net.URL;

import javax.swing.*;

import nl.BobbinWork.bwlib.gui.LocaleMenuItem;

/**
 * A menu that lets a user open a web page as a stream. Most menu items specify
 * predefined URLs of sample diagrams. One item launches a dialog allowing a
 * user to enter a URL.
 * 
 * @author J. Pol
 * 
 */
@SuppressWarnings("serial")//$NON-NLS-1$
public class SampleDiagramChooser
    extends JMenu
{
  // TODO keep revision number synchronized
  private static final String REVISIONED_URL =
    "http://bobbinwork.googlecode.com/svn/!svn/bc/569/wiki/diagrams/"; //$NON-NLS-1$
  private static final String LATEST_URL =
      "http://bobbinwork.googlecode.com/svn/wiki/diagrams/";
  private static final String[] SAMPLE_URLS = new String[] {//
          "stars.xml", //$NON-NLS-1$
          "snow.xml", //$NON-NLS-1$
          "flanders.xml", //$NON-NLS-1$
          "flandersPea.xml", //$NON-NLS-1$
          "braid-half-stitch.xml", //$NON-NLS-1$
          "braid-chaos.xml", //$NON-NLS-1$
          "braid-row-cloth-row-half-stitch.xml", //$NON-NLS-1$
      };
  /**
   * Handed down to dialogs.
   */
  private final Component parent;

  private final ActionListener externalActionListener;

  /**
   * Creates an inputStream from the specified URL.
   * 
   * @param event
   * 
   * @param url
   *          selected or entered by the user
   * @return
   */
  private void createInputStream(
      ActionEvent event,
      String url)
  {
    try {
      event.setSource( (new URL( url )).openStream() );
      externalActionListener.actionPerformed( event );
    } catch (Exception exception) {
      final String message =
          url + NEW_LINE + exception.getClass().getName() + NEW_LINE
              + exception.getLocalizedMessage();
      JOptionPane.showMessageDialog( parent, message,
          getString( "Load_error_caption" ), JOptionPane.ERROR_MESSAGE );
    }
  }

  static final String NEW_LINE = System.getProperty( "line.separator" );

  /**
   * @param parent
   *          handed down to dialogs
   * @param externalActionListener
   *          triggered when an InputStream is created from a user selected URL
   */
  public SampleDiagramChooser(
      Component parent,
      ActionListener externalActionListener)
  {
    super();
    applyStrings(this, "MenuFile_LoadSample"); //$NON-NLS-1$
    this.externalActionListener = externalActionListener;
    this.parent = parent;

    for (final String f : SAMPLE_URLS) {
      add( createItem( REVISIONED_URL + f ) );
    }
    add( new JSeparator() );
    for (final String f : SAMPLE_URLS) {
      add( createItem( LATEST_URL + f ) );
    }
    add( new JSeparator() );
    add( createDownloadOther() );
  }

  private JMenuItem createItem(
      String url)
  {
    // TODO enhancements to prepare in the background
    // replace with actual links from
    // http://bobbinwork.googlecode.com/svn/wiki/diagrams/
    // cache files
    // cache preview images for the drop down list
    final JMenuItem jMenuItem;
    jMenuItem = new JMenuItem( url );
    jMenuItem.setActionCommand( url );
    jMenuItem.addActionListener( new ActionListener()
    {
      public void actionPerformed(
          final ActionEvent event)
      {
        createInputStream( event, event.getActionCommand() );
      }
    } );
    return jMenuItem;
  }

  private JMenuItem createDownloadOther()
  {
    // can't access "this" in anonymous listener
    final Component parent = this.parent;

    final JMenuItem jMenuItem = new LocaleMenuItem( "MenuFile_ChooseSample" );
    jMenuItem.addActionListener( new ActionListener()
    {
      public void actionPerformed(
          final ActionEvent event)
      {
        final String url =
            (String) JOptionPane.showInputDialog( parent, "url", "http://" );
        createInputStream( event, url );
      }
    } );
    return jMenuItem;
  }
}
