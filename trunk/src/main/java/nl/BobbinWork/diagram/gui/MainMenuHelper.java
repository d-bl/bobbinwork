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
package nl.BobbinWork.diagram.gui;

import static java.awt.event.InputEvent.ALT_DOWN_MASK;
import static java.awt.event.InputEvent.CTRL_DOWN_MASK;
import static java.awt.event.KeyEvent.VK_F4;
import static java.awt.event.KeyEvent.VK_O;
import static nl.BobbinWork.bwlib.gui.Localizer.applyStrings;
import static nl.BobbinWork.bwlib.gui.Localizer.getString;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.filechooser.FileFilter;

import nl.BobbinWork.bwlib.gui.ExceptionHandler;
import nl.BobbinWork.bwlib.gui.LocaleMenuItem;
import nl.BobbinWork.bwlib.io.ClipboardHelper;
import nl.BobbinWork.bwlib.io.InputStreamHandler;
import nl.BobbinWork.diagram.xml.Ground;

public class MainMenuHelper
{
  public ExceptionHandler exceptionHandler;
  public JMenuBar menuBar;
  public InputStreamHandler inputStreamHandler;

  /**
   * Creates a {@link JMenuBar} with items that load diagrams and the exit item
   * at the tail of the file menu.
   * @param inputStreamHandler
   *          loads the diagrams
   * @param exceptionHandler
   */
  public MainMenuHelper(
      final InputStreamHandler inputStreamHandler,
      final ExceptionHandler exceptionHandler)
  {
    this.exceptionHandler = exceptionHandler;
    this.inputStreamHandler = inputStreamHandler;
    menuBar = new JMenuBar();
    menuBar.add( createFileMenu() );
    menuBar.add( createSampleMenu() );
    menuBar.add( createGroundMenu() );
    menuBar.add( createImportMenu() );
  }

  /**
   * Gets the {@link JMenuBar}
   * 
   * @return
   */
  public JMenuBar get()
  {
    return menuBar;
  }

  private JMenu createFileMenu()
  {
    final JMenu menu = new JMenu();
    applyStrings( menu, "MenuFile_file" ); //$NON-NLS-1$
    menu.add( createFileOpenMenuItem() );
    menu.add( createExitMenuItem() );
    return menu;
  }

  private JMenu createImportMenu()
  {
    final JMenu menu = new JMenu();
    applyStrings( menu, "Import_menu" ); //$NON-NLS-1$
    menu.add( createImportMenuItem() );
    return menu;
  }

  private JMenuItem createImportMenuItem()
  {
    final JMenuItem menuItem = new LocaleMenuItem( "From_clipboard" ); //$NON-NLS-1$
    menuItem.addActionListener( new ActionListener()
    {
      public void actionPerformed(
          final ActionEvent e)
      {
        ClipboardHelper.pasteText( inputStreamHandler );
      }
    } );
    return menuItem;
  }

  private static JMenuItem createExitMenuItem()
  {
    final JMenuItem menuItem =
        new LocaleMenuItem( "MenuFile_exit", VK_F4, ALT_DOWN_MASK ); //$NON-NLS-1$
    menuItem.addActionListener( new ActionListener()
    {
      public void actionPerformed(
          final ActionEvent e)
      {
        System.exit( 0 );
      }
    } );
    return menuItem;
  }

  private JMenu createGroundMenu()
  {
    final JMenu menu = new JMenu();
    applyStrings( menu, "MenuGround_Choose" ); //$NON-NLS-1$
    for (final Ground g : Ground.values()) {
      menu.add( createGroundMenuItem( inputStreamHandler, g.diamond(),
          g.name(), " <>" ) );
      menu.add( createGroundMenuItem( inputStreamHandler, g.square(),
          g.name(), " []" ) );
    }
    return menu;
  }

  private JMenuItem createGroundMenuItem(
      final InputStreamHandler inputStreamHandler,
      final String xmlString,
      final String name,
      final String variant)
  {
    final JMenuItem item = new JMenuItem();
    applyStrings( item, "MenuGround_" + name ); //$NON-NLS-1$
    item.setText( item.getText() + variant );
    item.addActionListener( new ActionListener()
    {

      public void actionPerformed(
          final ActionEvent event)
      {
        InputStream is;
        try {
          is = new ByteArrayInputStream( xmlString.getBytes( "UTF-8" ) );
          inputStreamHandler.proces( is );
        } catch (final UnsupportedEncodingException e) {
          exceptionHandler.show( e, "could not load diagram" );
        } catch (final IOException e) {
          exceptionHandler.show( e, "could not load diagram" );
        }
      }
    } );
    return item;
  }

  private JMenuItem createFileOpenMenuItem()
  {
    final JFileChooser chooser = new JFileChooser();
    chooser.setFileSelectionMode( JFileChooser.FILES_ONLY );
    chooser.setFileFilter( new FileFilter()
    {
      public String getDescription()
      {
        return getString( "FileType" ); //$NON-NLS-1$
      }

      public boolean accept(
          final File file)
      {
        final String extension =
            file.getName().toLowerCase().replaceAll( ".*[.]", "" ); //$NON-NLS-1$
        if (file.isDirectory()) return true;
        if (extension.equals( "xml" )) return true; //$NON-NLS-1$
        if (extension.equals( "bwml" )) return true; //$NON-NLS-1$
        return false;
      }
    } );
    final JMenuItem menuItem =
        new LocaleMenuItem( "MenuFile_open", VK_O, CTRL_DOWN_MASK ); //$NON-NLS-1$
    menuItem.addActionListener( new ActionListener()
    {
      public void actionPerformed(
          final ActionEvent e)
      {
        if (chooser.showOpenDialog( null ) != JFileChooser.APPROVE_OPTION)
          return;
        try {
          inputStreamHandler.proces( new FileInputStream( chooser
              .getSelectedFile() ) );
        } catch (final Exception exception) {
          exceptionHandler.show( exception, "could not load diagram" );
          return;
        }
        // TODO adjust recent-file menu
      }
    } );
    return menuItem;
  }

  private JMenu createSampleMenu()
  {
    final JMenu menu = new JMenu();
    final JMenu stable = new JMenu();
    final JMenu latest = new JMenu();
    applyStrings( menu, "MenuFile_LoadSample" ); //$NON-NLS-1$
    applyStrings( stable, "MenuFile_StableSample" );
    applyStrings( latest, "MenuFile_LatestSample" );
    stable.setToolTipText( new File( Sample.stars.getStableUrl() ).getParent() );
    menu.add( stable );
    menu.add( latest );
    for (final Sample sample : Sample.values()) {
      stable
          .add( createSampleMenuItem( sample.getKey(), sample.getStableUrl() ) );
      latest
          .add( createSampleMenuItem( sample.getKey(), sample.getLatestUrl() ) );
    }
    return menu;
  }

  private JMenuItem createSampleMenuItem(
      final String key,
      final String url)
  {
    final JMenuItem menuItem = new JMenuItem( key );
    menuItem.addActionListener( new ActionListener()
    {
      public void actionPerformed(
          final ActionEvent event)
      {
        try {
          inputStreamHandler.proces( new URL( url ).openStream() );
        } catch (final MalformedURLException e) {
          exceptionHandler.show( e, "could not load diagram" );
        } catch (final IOException e) {
          exceptionHandler.show( e, "could not load diagram" );
        }
      }

    } );
    return menuItem;
  }
}
