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

import nl.BobbinWork.diagram.xml.AttributeType;

import org.w3c.dom.Element;

public abstract class Partition {

    static final String RANGE_SEPARATOR = "-";

    public static final String MODEL_TO_DOM = "model";

    /** Show (or hide) this section of the diagram. */
    protected boolean visible = true;

    public Partition() {
    }
    
    /**
     * Create a new tree of Partition's from an XML Element.
     * 
     * @param element
     *            XML element &lt;pattern&gt;, &lt;group&gt;, &lt;cross&gt;,
     *            &lt;twist&gt; or &lt;pin&gt;
     */
    @Deprecated
    public Partition(Element element) {
        element.setUserData(MODEL_TO_DOM, this, null);

        if (element.getAttribute(AttributeType.display.toString()).matches("(no)|(No)|(NO)")) {
            visible = false;
        }
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

    /**
     * Draws a section of a diagram.
     * </p>
     * <p>
     * Pins are shown as dots in all types of diagrams. Lines are shown as
     * connected Cubic Bezier curves. Cubic Bezier curves can vary from straight
     * lines to C-like or S-like curves.
     * </p>
     * <p>
     * The most obvious valid call is having either <code>pair</code> or
     * <code>thread</code> set to true. Having both true might be usefull for
     * debugging purposes but makes no sense otherwise.
     * 
     * For some types of lace (such as Honiton) a pricking consists of dots
     * only. So <code>pair</code> and </code>thread</code> both false is
     * valid. For other lace types the pricking will be incomplete. As
     * <em>BobbinWork</em> stands for <em>Bobbin Lace Working diagrams</em>,
     * identifying runners/weavers and other pairs that should be drawn
     * (slightly different than in the pair view) in pricking, is not supported.
     * 
     * @param g2
     *            The diagram.
     *            </P>
     *            <P>
     * @param pair
     *            The diagram shows pairs.
     *            </P>
     *            <P>
     *            Pairs of threads are drawn as single lines. Colors and
     *            annotation symbols indicate different types of stitches.
     *            </P>
     *            <P>
     * @param thread
     *            The diagram shows threads.
     *            </P>
     *            <P>
     *            Though lace seems to have a 2D nature, threads going over and
     *            under each other cause a 3D effect. This effect is mimiced by
     *            giving the threads a simplistic shadow. This shadow is a
     *            brighter color along both sides of the core. To complete the
     *            effect, the lines are segmented. First the segments in the
     *            back are drawn, then the segments in front. The lines hence
     *            consist of visually connected cubic Bezier curves.
     *            </P>
     *            <P>
     *            For pure black-and-white diagrams the shadow should have the
     *            same colour as the background. Then the threads in the
     *            background will appear interrupted what is the traditional way
     *            of drawing.
     */
    /*
     * TODO combine the boolean parameters into a diagram properties object. an
     * option for black and white diagram for printing would also be usefull. In
     * that case the shadow should be rendered with the background color.
     *
     * TODO First draw non-pins, then pins.
     */
    abstract public void draw(java.awt.Graphics2D g2, boolean pair, boolean thread);
}
