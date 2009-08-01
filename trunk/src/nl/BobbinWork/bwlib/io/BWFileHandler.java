/* BWFileHandler.java Copyright 2006-2007 by J. Pol
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

import java.awt.Component;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

/**
 * Maintains the name of the last read/written file.
 * 
 * @author J. Pol
 */
public class BWFileHandler {

    private Component parent = null; // for dialogs reporting errors

    private String fileName = null;

    private JFileChooser fileChooser = new JFileChooser();

    public BWFileHandler(Component parent, BWFileFilter filter) {

        this.parent = parent;
        fileChooser.setFileFilter(filter);
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
    }

    public void clearFileName() {
        fileName = null;
    }

    public String getFileName() {
        return fileName;
    }

    public void save(String buffer) {
        if (fileName == null) {
            saveAs(buffer);
        } else {
            write(buffer, "save");
        }
    }

    public void saveAs(String buffer) {
        if (fileChooser.showSaveDialog(parent) == JFileChooser.APPROVE_OPTION) {
            try {
                if (fileChooser.getSelectedFile().createNewFile()) {
                    fileName = fileChooser.getSelectedFile().toString();
                    write(buffer, "save as");
                } else {
                    showError( "could not create \n"
                            + fileChooser.getSelectedFile().toString(), "save as");
                }
            } catch (IOException ioe) {
                showError(ioe.getLocalizedMessage(), "save as");
            }
        }
    }

    public InputStream open() {
        if (fileChooser.showOpenDialog(parent) == JFileChooser.APPROVE_OPTION) {
            fileName = fileChooser.getSelectedFile().getAbsolutePath();
            try {
                return new FileInputStream(fileName);
            } catch (FileNotFoundException ioe) {
                showError(ioe.getLocalizedMessage(), "open");
                clearFileName();
                return null;
            }
        } else {
            return null;
        }
    }

    private void showError(String message, String title) {
        JOptionPane.showMessageDialog(parent, message, title, JOptionPane.ERROR_MESSAGE);
    }

    private void write(String buffer, String title) {
        try {
            PrintStream p = (new PrintStream(new FileOutputStream(fileName)));
            p.print(buffer);
            p.flush();
            p.close();
        } catch (IOException ioe) {
            showError(ioe.getLocalizedMessage(), title);
            clearFileName();
        }
    }
}
