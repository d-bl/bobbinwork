package nl.BobbinWork.diagram.gui;

import java.net.URL;

import javax.swing.*;

import nl.BobbinWork.diagram.gui.BwDiagrams;
import nl.BobbinWork.diagram.xml.DiagramRebuilder;

/**
 * TODO somehow show another colour during each time consuming step  
 * in {@link BwDiagrams#createFileListener}
 * and in {@link DiagramRebuilder#rebuild}
 * The zero'th icon is a safe value for not busy.
 * 
 * simply setting an icon on a button like the others did not work
 * possibly another thread is needed
 * previous attempts to change the cursor was also buggy 
 * */
public class BusyIndicator
{
  protected static final Icon[] ICONS = initIcons();
  private static Icon[] initIcons()
  {
    final Icon[] result = new ImageIcon[4];
    for (int i=0;i<result.length;i++){
      final URL url = BwDiagrams.class.getResource( "busy"+i+".gif" );
      if (url != null)
      result[i] = new ImageIcon(url);
    }
    return result;
  }
  
}
