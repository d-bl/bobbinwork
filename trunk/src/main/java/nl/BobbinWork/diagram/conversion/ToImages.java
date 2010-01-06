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

import static java.awt.RenderingHints.KEY_ANTIALIASING;
import static java.awt.RenderingHints.KEY_INTERPOLATION;
import static java.awt.RenderingHints.VALUE_ANTIALIAS_ON;
import static java.awt.RenderingHints.VALUE_INTERPOLATION_BILINEAR;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.imageio.ImageIO;

import nl.BobbinWork.diagram.gui.DiagramPainter;
import nl.BobbinWork.diagram.model.Diagram;
import nl.BobbinWork.diagram.model.Drawable;
import nl.BobbinWork.diagram.model.MultiplePairsPartition;
import nl.BobbinWork.diagram.model.MultipleThreadsPartition;
import nl.BobbinWork.diagram.model.Partition;
import nl.BobbinWork.diagram.model.Stitch;

/**
 * Creates images from a {@link Diagram}.
 * 
 * @author Joke Pol
 *
 */
public abstract class ToImages
{
  private String base;
  private String extension;
  private int fileSequenceNumber;

  /**
   * Gets a visible partition in the diagram of the instance. Supposed to be the
   * last main partition, or its last child, or its last grand... A visible
   * partition partition is recognized by having a bounds.
   * 
   * @return null if no visible partition is found
   */
  public abstract Partition getAnimatedPartition();

  abstract Iterable<Drawable> getDrawables();

  /**
   * Creates an instance that creates thread diagrams.
   * 
   * @param diagram
   *          the diagram to save as image(s)
   * @return an instance
   */
  public static ToImages threads(
      final Diagram diagram)
  {
    return new ToImages()
    {
      @Override
      Iterable<Drawable> getDrawables()
      {
        return diagram.getThreads();
      }

      @Override
      public Partition getAnimatedPartition()
      {
        return getAnimatedPartition( diagram );
      }
    };
  }

  /**
   * Creates an instance that creates pair diagrams.
   * 
   * @param diagram
   *          the diagram to save as image(s)
   * @return an instance
   */
  public static ToImages pairs(
      final Diagram diagram)
  {
    return new ToImages()
    {
      @Override
      protected Iterable<Drawable> getDrawables()
      {
        return diagram.getPairs();
      }

      @Override
      public Partition getAnimatedPartition()
      {
        return getAnimatedPartition( diagram );
      }
    };
  }

  static Partition getAnimatedPartition(
      final Diagram diagram)
  {
    Partition partition = diagram;
    while (null == partition.getBounds()) {
      if (!(partition instanceof MultiplePairsPartition)) {
        return null;
      }
      final List<Partition> list =
          ((MultiplePairsPartition) partition).getPartitions();
      return list.get( list.size() - 1 );
    }
    return partition;
  }

  /**
   * Creates an image of the diagram. Typical usage: {@link ImageIO#write}. The
   * resulting image can be included in documents or web pages.
   * 
   * @param size
   *          Dimensions of the created image. The image will not be resized or
   *          repositioned to fit.
   * @return an image
   * @return {@link IllegalArgumentException} if size is null
   */
  public BufferedImage newImage(
      final Dimension size)
  {
    if (size == null) throw new IllegalArgumentException( "" );
    final BufferedImage image =
        new BufferedImage( size.width, size.height, BufferedImage.TYPE_INT_RGB );
    final Graphics2D g2 = image.createGraphics();
    g2.setBackground( new Color( 0xFFFFFF ) );
    g2.clearRect( 0, 0, size.width, size.height );
    g2.setRenderingHint( KEY_INTERPOLATION, VALUE_INTERPOLATION_BILINEAR );
    g2.setRenderingHint( KEY_ANTIALIASING, VALUE_ANTIALIAS_ON );
    DiagramPainter.paint( g2, getDrawables() );
    g2.dispose();
    return image;
  }

  /**
   * Writes a set of images. The created files could be used for an animated gif
   * or slide show. The images show how to create the piece of lace stitch by
   * stitch.
   * 
   * @param size
   *          dimensions of the created images
   * @param file
   *          name of the files to create. A sequence number is inserted before
   *          the dot of the extension. The extension defines the format.
   * @throws {@link IllegalArgumentException} if the file name extension is not
   *         found in the result of {@link #formats}
   * @throws {@link IOException}
   */
  public void createSlides(
      final Dimension size,
      final String file) throws IOException, IllegalArgumentException
  {
    fileSequenceNumber = 0;
    createFileNameParameters( file );
    ImageIO.write( newImage( size ), extension, createFile() );
    Partition partition = getAnimatedPartition();
    if (partition instanceof MultiplePairsPartition) {
      MultiplePairsPartition mpp = (MultiplePairsPartition) partition;
      mpp.setAllVisible( false );
      animationStep( size, mpp );
    }
  }

  private void animationStep(
      final Dimension size,
      MultipleThreadsPartition mtp) throws IOException
  {
    mtp.setVisible( true );
    if (mtp instanceof Stitch) {
      ImageIO.write( newImage( size ), extension, createFile() );
    }
    if (mtp instanceof MultiplePairsPartition) {
      for (Partition p : ((MultiplePairsPartition) mtp).getPartitions()) {
        // TODO allow MultipleThreadsPartition/Switch
        // but currently all switches are made visible at once
        // as the pairs are made visible by group
        if (p instanceof MultiplePairsPartition) {
          animationStep( size, ((MultiplePairsPartition) p) );
        }
      }
    }
  }

  private void createFileNameParameters(
      final String file)
  {
    base = file.replaceAll( "\\.[^\\.]*$", "" );
    extension = file.replaceAll( ".*\\.", "" );

    List<String> names = formats();
    if (!names.contains( extension )) {
      throw new IllegalArgumentException( "got extension [" + extension
          + "] valid extensions: " + Arrays.toString( names.toArray() ) );
    }

  }

  public static List<String> formats()
  {
    return Arrays.asList( ImageIO.getWriterFormatNames() );
  }

  private File createFile()
  {
    File output = new File( base + (fileSequenceNumber++) + "." + extension );
    return output;
  }
}
