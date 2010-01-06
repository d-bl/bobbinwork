/* Partition.java Copyright 20010 by J. Pol
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
package nl.BobbinWork.diagram.conversion;

import static nl.BobbinWork.bwlib.gui.Localizer.setBundle;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;

import javax.imageio.ImageIO;

import nl.BobbinWork.diagram.model.Diagram;
import nl.BobbinWork.diagram.xml.DiagramBuilder;
import nl.BobbinWork.diagram.xml.Ground;

import org.junit.BeforeClass;
import org.junit.Test;

public class TestImages
{
  static Dimension size;
  static ToImages toThreadImage;
  private static ToImages toPairImage;

  @BeforeClass
  public static void setUp() throws Exception
  {
    setBundle( "nl/BobbinWork/diagram/gui/labels" );
    final Diagram diagram = newDiagram();
    toThreadImage = ToImages.threads(diagram);
    toPairImage = ToImages.pairs(diagram);
    size = toThreadImage.getAnimatedPartition().getBounds().getBounds().getSize();
  }

  private static Diagram newDiagram() throws Exception
  {
    final byte[] bytes = Ground.flanders.diamond().getBytes();
    final ByteArrayInputStream inputStream = new ByteArrayInputStream( bytes );
    return DiagramBuilder.createDiagramModel( inputStream );
  }

  // TODO for now just a visual check of the generated files
  @Test(timeout=1500)
  public void gif() throws Exception
  {
    final BufferedImage image = toThreadImage.newImage( size );
    ImageIO.write( image, "gif", new File( "build/tmp1.gif" ) );
  }

  @Test(timeout=1500)
  public void jpeg() throws Exception
  {
    final BufferedImage image = toPairImage.newImage( size );
    ImageIO.write( image, "jpeg", new File( "build/tmp1.jpg" ) );
  }

  @Test(timeout=1500)
  public void png() throws Exception
  {
    final BufferedImage image = toPairImage.newImage( size );
    ImageIO.write( image, "png", new File( "build/tmp1.png" ) );
  }

  @Test(expected=IllegalArgumentException.class)
  public void extension() throws Exception
  {
    toPairImage.createSlides( size, "build/pairSlides.xxx" );
  }

  @Test
  public void threadSlides() throws Exception
  {
    toThreadImage.createSlides( size, "build/threadSlides.jpeg" );
  }
  @Test
  public void pairSlides() throws Exception
  {
    toPairImage.createSlides( size, "build/pairSlides.jpeg" );
  }
}
