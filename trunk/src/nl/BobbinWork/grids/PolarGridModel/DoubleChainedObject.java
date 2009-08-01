/* DoubleChainedObject.java Copyright 2005-2007 by J. Pol
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

package nl.BobbinWork.grids.PolarGridModel;

/**
 * Element for an open chain linked in both directions.
 *
 * @author J. Pol
 *
 */
// TODO: extension of an interface?
public abstract class DoubleChainedObject {
    
    private DoubleChainedObject previous;
    private DoubleChainedObject next;
    
    /** Gets the previous neighbour in the chain. */
    public final DoubleChainedObject getPrevious() {
        return previous;
    }
    
    /** Gets the next neighbour in the chain. */
    public final DoubleChainedObject getNext() {
        return this.next;
    }
    
    /** Checks if the chain provides a previous neighbour. */
    public final boolean previousAvailable() {
        return this.previous != null;
    }

    /** Checks if the chain provieds a next neighbour. */
    public final boolean nextAvailable() {
        return this.next != null;
    }

    /** Gets the first element of the chain. */
    public final DoubleChainedObject getFirst() {
        DoubleChainedObject p;
        if ( ! this.previousAvailable() ) {
            p = this;
        } else {
            // if ever the chain is closed, avoid a never ending loop
            p = this.getPrevious();
            while ( p.previousAvailable() && p != this ) {
                p = p.getPrevious();
            }
        }
        return p;
    }

    /** Gets the last element of the chain. */
    public final DoubleChainedObject getLast() {
        DoubleChainedObject p;
        if ( ! this.nextAvailable() ) {
            p = this;
        } else {
            // if ever the chain is closed, avoid a never ending loop
            p = this.getNext();
            while ( p.nextAvailable() && p != this ) {
                p = p.getNext();
            }
        }
        return p;
    }
    
    /** Adds an object after this one. Not thread safe. */
    private void insertAfter( DoubleChainedObject x) {
        if ( x.nextAvailable() ) {
            this.next = x.getNext();
            x.getNext().previous = this;
        }
        this.previous = x;
        x.next = this;
        // TODO: check for thread interference
    }
    
    /** Adds an object before this one. Not thread safe. */
    private void insertBefore( DoubleChainedObject x) {
        x.previous = this;
        x.next = this.next;
        if ( this.previousAvailable() ) {
            this.previous.next = x;
            this.previous = x;
            // TODO: check for thread interference
        }
    }
    
    /** Disconnects the element from its neighbours and connects the neighbours to one another. 
     * The element itself remains to be destroied, 
     * or can be re-used as a new chain.
     * Not thread safe. 
     */
    protected void delete( ) {
        if ( this.previousAvailable() ) {
            this.previous.next = this.next;
        }
        if ( this.nextAvailable() ) {
            this.next.previous = this.previous;
        }
        // TODO: check for thread interference
        this.next = null;
        this.previous = null;
    }
    
    /**
     * Adds a new instance of DoubleChainedObject to an existing chain. 
     *</p><p>
     * Creating new elements and linking them to one or two neighbours
     * is combined in the constructor of the element.
     * When linking would be implemented in a separate method, 
     * an already linked object could be linked again without unlinking,
     * thus a mess would be created.
     * As a side effect the chain can't be closed.
     * Not thread safe.
     */
    public DoubleChainedObject(boolean before, DoubleChainedObject x) {
        if ( before ) {
            insertBefore(x);
        } else {
            insertAfter(x);
        }
    }
    
    /**
     * Starts a new chain with a new instance of DoubleChainedObject.
     */
    public DoubleChainedObject() {
        previous = null;
        next = null;
    }
    
}
