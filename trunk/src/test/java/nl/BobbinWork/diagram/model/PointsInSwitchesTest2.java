/* PointsInSwitches.java Copyright 2009 by J. Pol
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

import java.util.*;

import nl.BobbinWork.diagram.xml.XmlResources;

import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

/** Positive tests that check the shape of bounding polygons. */
@Ignore("not yet properly implemented")
@RunWith(Parameterized.class)
public class PointsInSwitchesTest2 extends PointsInSwitchesTest {
   
    @Parameters
    public static Collection<Object[]> data() throws Exception {
      xr = new XmlResources();
      final MultiplePairsPartition tctcBS = extractPart(TCTC_BASED_ON_BASIC_STITCHES);
      final MultiplePairsPartition tctcOS = createPart(TCTC_BUILDING_OWN_STITCHES);
      //System.out.println( XmlResources.toXmlString( parseEmbedded( TCTC_BASED_ON_BASIC_STITCHES )));
    
      final Object[][] testCaseParameters = new P[][] {
            
          // end of back of left twist; checks merging polygons 
          {new P(tctcBS,tctcOS,186,83,186,83)}, 
       /**/};
        return Arrays.asList(testCaseParameters);
    }

    P params;
  
  /** Creates a test object for a test case in data(). */
  public PointsInSwitchesTest2(P parameters){
    super(parameters);
  }
}
