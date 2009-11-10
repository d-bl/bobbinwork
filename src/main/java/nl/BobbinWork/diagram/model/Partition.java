/* Partition.java Copyright 2006-2007 by J. Pol
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
package nl.BobbinWork.diagram.model;

import java.awt.Shape;
import java.net.URL;
import java.util.*;

import javax.swing.*;

import nl.BobbinWork.bwlib.gui.Localizer;

public abstract class Partition {

    /** Show (or hide) this section of the diagram. */
    private boolean visible = true;
    private Object sourceObject;

    Partition() {
    }
    
    public abstract int getNrOfPairs();

    /**
     * Gets a shape containing all pair or thread segments of the partition.
     * 
     * @return a shape containing all pair or thread segments of the partition.
     *         Adjacent hulls should not overlap but preferably touch one
     *         another with a (complex) line in stead of individual points.
     */
    public abstract Shape getBounds();

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public boolean isVisible() {
		return visible;
	}

	abstract class Drawables implements Iterable<Drawable>{};
	
	abstract Iterator<Drawable> pairIterator ();
	public Iterable<Drawable> getPairs() {
		return pairs;
	}
	private final Iterable<Drawable> threads = new Drawables () {

		public Iterator<Drawable> iterator() {
			return threadIterator();
		}
		
	};

	abstract Iterator<Drawable> threadIterator ();
	public Iterable<Drawable> getThreads() {
		return threads;
	}
	private final Iterable<Drawable> pairs = new Drawables () {
		
		public Iterator<Drawable> iterator() {
			return pairIterator();
		}
		
	};

	abstract Iterator<Drawable> pinIterator ();
	public Iterable<Drawable> getPins() {
		return pins;
	}
	private final Iterable<Drawable> pins = new Drawables () {
		
		public Iterator<Drawable> iterator() {
			return pinIterator();
		}
		
	};

  public String getCaption()
  {
    return Localizer.getString( "VoodooTree_"+getClass().getSimpleName() );
  }

  public String getTooltip()
  {
    String s = Localizer.getString( "VoodooTree_"+getClass().getSimpleName()+"_hint" );
    if (s != null) {
      s = s.replaceAll("\n", "<br>");
    }
    return "<html><body>" + s + "</body></html>";
  }

	private static Map<Class<? extends Partition>, Icon> icons = new HashMap<Class<? extends Partition>, Icon>();

  public Icon getIcon() {
	  Icon icon = icons.get( getClass() );
	  if (icon != null) return icon;
	  String name = getClass().getSimpleName().toLowerCase()+".gif";
	  URL iconURL = getClass().getResource(name);
    if (iconURL == null) {
      icons.put( getClass(), null );
      return null;
    }
    icon = new ImageIcon(iconURL);
    icons.put( getClass(), icon );
	  return icon;
	}

  public void setSourceObject(
      Object sourceObject)
  {
    this.sourceObject = sourceObject;
  }

  public Object getSourceObject()
  {
    return sourceObject;
  }
}
