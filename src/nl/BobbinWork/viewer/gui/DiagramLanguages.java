/* DiagramLanguages.java Copyright 2006-2007 by J. Falkink-Pol
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
package nl.BobbinWork.viewer.gui;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import nl.BobbinWork.bwlib.gui.Localizer;
import nl.BobbinWork.diagram.xml.ElementType;

/**
 * Manages the languages encountered in diagrams and their order as preferred by
 * the user. A diagram may support another set of languages than the application.
 * 
 * @author J. Falkink-Pol
 * 
 */
public class DiagramLanguages {
    /*
     * TODO: turn into some user sortable combo box of languages found in
     * diagrams and/or cookies. Language extraction should be part of the 
     * diagram.model package.
     */

    /**
     * @param element
     *            a <code>&lt;title&gt;</code> (or its parent) which may have
     *            have either <code>&lt;title&nbsp;lang=".."&gt;</code>
     *            siblings or <code>&lt;value&nbsp;lang=".."&gt;</code>
     *            children.
     * @return for the time being: the first line of text found
     */
    static String getPrimaryTitle(Element element) {

        if (element == null) {
            return ""; //$NON-NLS-1$
        } else {
            Element title = element;
            try {
                if (ElementType.valueOf(element.getNodeName()) != ElementType.title) {
                    title = (Element) element.getElementsByTagName("title").item(0); //$NON-NLS-1$
                    if (title == null || !title.getParentNode().isSameNode(element)) {
                        return ""; //$NON-NLS-1$
                    }
                }
                NodeList values = element.getElementsByTagName("value"); //$NON-NLS-1$
                String lang = Localizer.getBundle().getLocale().getLanguage();
                int i, len = values.getLength();
                for (i=0 ; i<len ; i++) {
                    if ( values.item(i).getAttributes().getNamedItem("lang").getTextContent().matches(lang)) { //$NON-NLS-1$
                        return values.item(i).getTextContent();
                    }
                }
                return values.item(0).getTextContent().//
                        replaceFirst("\\A\\s*", "").// //$NON-NLS-1$ //$NON-NLS-2$
                        replaceFirst("\\r|\\n.*", ""); //$NON-NLS-1$ //$NON-NLS-2$
            } catch (NullPointerException e) {
                return ""; //$NON-NLS-1$
            }
        }
    }

}
