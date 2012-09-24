/* FileMenu.java Copyright 2006-2007 by J. Pol
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
package nl.BobbinWork.diagram.gui;

public enum Sample {
  braid1("braid-half-stitch"),
  braid2("braid-chaos"),
  braid3("braid-row-cloth-row-half-stitch"),
  braid4("sym-braid"),
  flanders("flanders"),
  plaits("plaits"),
  pea("flandersPea"),
  snake1("snake-cloth"),
  snake2("snake-cloth-footside"),
  snake3("snake-half"),
  snow1("snow"),
  snow2("ringed-snow"),
  stars("stars"),
  stairs("trapgevel"), ;

  public static final String STABLE_URL_BASE =
      "http://bobbinwork.googlecode.com/svn/!svn/bc/569/wiki/diagrams/";
  public static final String LATEST_URL_BASE =
      "http://bobbinwork.googlecode.com/svn/wiki/diagrams/";
  private final String key;
  private final String stableUrl;
  private final String latestUrl;

  private Sample(final String key)
  {
    this.key = key;
    stableUrl = STABLE_URL_BASE + key + ".xml";
    latestUrl = LATEST_URL_BASE + key + ".xml";
  }

  public String getKey()
  {
    return key;
  }

  public String getStableUrl()
  {
    return stableUrl;
  }

  public String getLatestUrl()
  {
    return latestUrl;
  }
}
