
/* BWFileFilter.java Copyright 2007 by J. Falkink-Pol
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

package nl.BobbinWork.bwlib.io;

import java.io.File;
import javax.swing.filechooser.FileFilter;

public class BWFileFilter extends FileFilter {

    private String [] patterns = null;

    private String description = "";

    /**
     * Creates a file filter that accepts all directories and files.
     */
    public BWFileFilter() {
    }

    /**
     * Creates a file filter accepting directories and files with the specified filename extensions.
     * Example: {"gif","jpg","png"}, "Images"
     *  
     * @param extensions suffixes of allowed filenames 
     * @param description 
     */
    public BWFileFilter(String description, String [] extensions) {
        this.patterns = new String[extensions.length];
        for (int i = 0; i < extensions.length; i++) {
            this.description += ", *." + extensions[i]; 
            this.patterns[i] = ".*\\." + extensions[i];
        }
        this.description = description + " (" + this.description.substring(2) + ")";
    }

    public String getDescription() {
        return description;
    }

    public boolean accept(File file) {
        if ( patterns == null || file == null || file.isDirectory()) return true;
        String name = file.getName().toLowerCase();
        for (int i = 0; i < patterns.length; i++) {
            if ( name.matches(patterns[i]) ) return true;
        }
        return false;
    }
}
