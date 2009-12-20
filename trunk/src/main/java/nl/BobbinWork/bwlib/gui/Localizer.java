/* Localizer.java Copyright 2006-2007 by J. Pol
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
package nl.BobbinWork.bwlib.gui;

import java.util.*;

import javax.swing.AbstractButton;
import javax.swing.JComponent;

/**
 * Manager of language specific strings. An application should provide a single
 * set of property files (a bundle) collecting the key value pairs for all the
 * packages using this class.
 * </p>
 * <p>
 * The applyStrings methods sets language specific string properties of
 * components as far as applicable for the type of component and as far
 * available in the set of properties files.
 * </p>
 * <p>
 * The properties and suffixes are:
 * <ul>
 * <li>text</li>
 * <li>mnemonic - underline</li>
 * <li>accesibleName - accesibleName</li>
 * <li>toolTipText - hint (new lines are replaced by &lt;br&gt;, the result
 * embedded in &lt;html&gt;&lt;body&gt;...&lt;/body&gt;&lt;/html&gt;)</li>
 * </ul>
 * For the caption of a component no suffix is applied. Suffixes can take
 * several forms: with or without starting capitals for words in the suffix,
 * with our without underscores preceding the words in the suffix.
 * 
 * @author J Pol
 * 
 */
public class Localizer {

    /**
     * Base name of the default (bundle of) properties files. Used for lazy
     * initialisation if strings are fetched from the bundle, before setBundle()
     * is called by the application.
     */
    private static final String BASE_NAME = "labels"; //$NON-NLS-1$

    /** The bundle with the language specific strings. */
    private static ResourceBundle bundle = null;

    /** Suffixes added to the keyBase when searching the bundle. */
    private static final String mnemonicSuffixes[] = { "_underline", //$NON-NLS-1$
            "_Underline", //$NON-NLS-1$
            "Underline" }; //$NON-NLS-1$

    /** Suffixes added to the keyBase when searching the bundle. */
    private static final String accessibleNameSuffixes[] = { "_accessible_name", //$NON-NLS-1$
            "_Accessible_Name", //$NON-NLS-1$
            "_accessibleName", //$NON-NLS-1$
            "_AccessibleName", //$NON-NLS-1$
            "AccessibleName" }; //$NON-NLS-1$

    /** Suffixes added to the keyBase when searching the bundle. */
    private static final String toolTipTextSuffixes[] = { "_hint", //$NON-NLS-1$
            "_Hint", //$NON-NLS-1$
            "Hint" }; //$NON-NLS-1$

    /**
     * Sets the bundle for subsequent retrieval of language specific strings.
     * 
     * @see java.util.ResourceBundle#getBundle(String)
     * 
     * @param baseName
     */
    public static void setBundle(String baseName) {
        bundle = ResourceBundle.getBundle(baseName);
    }

    /**
     * Sets the bundle for subsequent retrieval of language specific strings.
     * 
     * @see java.util.ResourceBundle#getBundle(String)
     * 
     * @param baseName
     * @param locale
     */
    public static void setBundle(String baseName, Locale locale) {
        bundle = ResourceBundle.getBundle(baseName, locale);
    }

    /**
     * Gets the lazy initialized bundle, for strings fetched before the
     * application called setBundle.
     * 
     * @return the bundle used to set labels and other user messages.
     */
    private static ResourceBundle myBundle() {
        if (bundle == null) {
            bundle = ResourceBundle.getBundle(BASE_NAME);
        }
        return bundle;

    }

    /**
     * Gets an individual language specific string from the bundle.
     * 
     * @param key
     * @return the value for the key
     */
    public static String getString(String key) {
        return myBundle().getString(key).replaceAll( "[\r]?[\n][\r]?", "<br/>" );
    }

    /**
     * Sets the text, mnemonic, toolTipText and accessible name of a
     * abstractButton. See class level documentation for details.
     * 
     * @param abstractButton
     *            to receive the language specific strings as far as available
     *            in the bundle
     * @param keyBase
     *            base of the key to search in the bundle
     * 
     * @return the component as a convenience
     */
    public static AbstractButton applyStrings(AbstractButton abstractButton, String keyBase) {

        try {
            abstractButton.setText(myBundle().getString(keyBase));
        } catch (Exception e) {
            // an icon is expected instead
        }

        String value = getTranslation(keyBase, mnemonicSuffixes);
        if (value != null) {
            abstractButton.setMnemonic(value.toUpperCase().charAt(0)); //$NON-NLS-1$
        }

        setToolTipText(abstractButton, keyBase);

        value = getTranslation(keyBase, accessibleNameSuffixes);
        if (value != null) {
            abstractButton.getAccessibleContext().setAccessibleName(value);
        }
        return abstractButton;
    }

    /**
     * Sets the toolTipText property of a component. See class level
     * documentation for details.
     * 
     * @param component
     *            to receive a toolTipText if available in the bundle
     * @param keyBase
     *            base of the key to search in the bundle
     * 
     * @return the compent as a convenience
     */
    public static JComponent applyStrings(JComponent component, String keyBase) {

        // prevent overriding side effects with a shared private method
        setToolTipText(component, keyBase);

        return component;
    }

    /**
     * Gets a language specific string, searching the bundle with various key
     * variants until a value is found.
     * 
     * @param base
     *            base part of the key used to search the bundle
     * @param suffixes
     *            possible tails of the key used to search the bundle
     * 
     * @return the value corresponding with the key assembled by concatenating
     *         the property with one of the suffixes <br>
     *         null if no such key is found in the bundle
     */
    private static String getTranslation(String base, String suffixes[]) {
        String value = null;
        for (String suffix : suffixes) {
            try {
                value = myBundle().getString(base + suffix);
            } catch (Throwable e) {

            }
            if (value != null) {
                break;
            }
        }
        return value;
    }

    /**
     * Sets the toolTipText property of a component.
     * 
     * @param component
     *            to receive a toolTipText
     * @param keyBase
     *            base of the key to search in the bundle
     */
    private static void setToolTipText(JComponent component, String keyBase) {
        String value = getTranslation(keyBase, toolTipTextSuffixes);
        if (value != null) {
            value = value.replaceAll("\n", "<br>");
            component.setToolTipText("<html><body>" + value + "</body></html>");
        }

    }

    /** no instantiation as all methods are static */
    private Localizer() {
    }

    public static String getLanguage()
    {
      try {
      return myBundle().getLocale().getLanguage();
      } catch (MissingResourceException exception){
        return null;
      }
    }
}
