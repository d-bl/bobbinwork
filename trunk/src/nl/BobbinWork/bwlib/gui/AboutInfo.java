/* AboutInfo.java Copyright 2005-2007 by J. Falkink-Pol
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

/**
 *
 * @author J. Falkink-Pol
 */
public class AboutInfo {
    
    private String caption;
    private String version;
    private String years;
    private String author;
    
    public String getCaption() {
        return caption;
    }
    public String getVersion() {
        return version;
    }
    private String getYears() {
        return years;
    }
    private String getAuthor() {
        return author;
    }
    
    public String getMessage() {
        return "<html><p> "
            + Localizer.getString("MenuHelp_release") + " "
            + getVersion() + "<br>Copyright © " 
            + getYears() + " " 
            + getAuthor()  
            + "<p><br>" 
            + Localizer.getString("MenuHelp_About_License").replaceAll("\\n", "<br>")
            + "<p></html>" 
            ;
    }
    
    public AboutInfo(String caption, String version, String years, String author) {
        this.caption = caption;
        this.version = version;
        this.years = years;
        this.author = author;
    }
}
