/* JMenuItemBrowse.java Copyright 2005-2007 by J. Falkink-Pol
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

package nl.BobbinWork.grids.GridGUI;

/** A JMenuItem with an URL as actionCommand.
 * If the URL starts with "http:" an attempt is made 
 * to show the page in the default browser of the system.
 * If the URL looks like "nl/___.html" the page is shown in a JFrameBrowser.
 *
 * @author J. Falkink-Pol
 */
public class JMenuItemBrowse

extends
javax.swing.JMenuItem

implements
java.awt.event.ActionListener {

    private String unknownBrowser = "Sorry, can't find your browser";
    private java.awt.Image icon;
    private String frameCaption;

    public JMenuItemBrowse(String unknownBrowser, java.awt.Image icon, String frameCaption) {
        this.addActionListener(this);
        this.unknownBrowser = unknownBrowser;
        this.icon = icon;
        this.frameCaption = frameCaption;
    }

    private static boolean showInBrowser(String url){
        String os = System.getProperty("os.name").toLowerCase();
        Runtime rt = Runtime.getRuntime();
        try{
            if (os.indexOf( "win" ) >= 0) {
                // this doesn't support showing urls in the form of "page.html#nameLink"
                rt.exec( "rundll32 url.dll,FileProtocolHandler " + url);
            } else if (os.indexOf( "mac" ) >= 0) {
                rt.exec( "open " + url);
            } else if (os.indexOf( "nix") >=0 || os.indexOf( "nux") >=0) {
                // Do a best guess on unix until we get a platform independent way
                // Build a list of browsers to try, in this order.
                String[] browsers = {"epiphany", "firefox", "mozilla", "konqueror",
                        "netscape","opera","links","lynx"};

                // Build a command string which looks like "browser1 "url" || browser2 "url" ||..."
                StringBuffer cmd = new StringBuffer();
                for (int i=0; i<browsers.length; i++)
                    cmd.append( (i==0  ? "" : " || " ) + browsers[i] +" \"" + url + "\" ");

                rt.exec(new String[] { "sh", "-c", cmd.toString() });
            } else {
                return false;
            }
        }catch (java.io.IOException e){
            javax.swing.JOptionPane.showMessageDialog
            ( null
                    , e.getLocalizedMessage()
                    , "browser"
                    , javax.swing.JOptionPane.INFORMATION_MESSAGE
            );
            return false;
        }
        return true;
    }
    public void actionPerformed(java.awt.event.ActionEvent e) {

        //      caption of pop-up
        String frameCaption = this.frameCaption + " : "+this.getText(); 
        frameCaption = frameCaption.replaceFirst("\\.*$",  "");

        if ( e.getActionCommand().matches("[Hh][Tt][Tt][Pp]:.*") ) {
            if ( ! showInBrowser( e.getActionCommand() ) ) {
                javax.swing.JOptionPane.showMessageDialog
                ( this
                        , unknownBrowser
                        + "\n\n"
                        + e.getActionCommand()
                        , frameCaption
                        , javax.swing.JOptionPane.INFORMATION_MESSAGE
                );

            }
        } else if ( e.getActionCommand().matches("nl/.*\\.[Hh][Tt][Mm][Ll]*") ) {
            new nl.BobbinWork.bwlib.gui.JFrameBrowser
            ( frameCaption
                    , icon
                    , e.getActionCommand()
            );
        }
    }
}
