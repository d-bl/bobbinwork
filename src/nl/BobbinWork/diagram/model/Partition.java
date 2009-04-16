/* Partition.java Copyright 2006-2007 by J. Falkink-Pol
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
import java.util.Iterator;

public abstract class Partition {

    /** Show (or hide) this section of the diagram. */
    private boolean visible = true;

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
    public abstract Shape getHull();

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

		@Override
		public Iterator<Drawable> iterator() {
			return threadIterator();
		}
		
	};

	abstract Iterator<Drawable> threadIterator ();
	public Iterable<Drawable> getThreads() {
		return threads;
	}
	private final Iterable<Drawable> pairs = new Drawables () {
		
		@Override
		public Iterator<Drawable> iterator() {
			return pairIterator();
		}
		
	};

	abstract Iterator<Drawable> pinIterator ();
	public Iterable<Drawable> getPins() {
		return pins;
	}
	private final Iterable<Drawable> pins = new Drawables () {
		
		@Override
		public Iterator<Drawable> iterator() {
			return pinIterator();
		}
		
	};
}
