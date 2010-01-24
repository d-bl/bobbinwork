package nl.BobbinWork.diagram.model;

import java.util.Iterator;

import nl.BobbinWork.bwlib.gui.Localizer;

/**
 * A stitch that can not be expressed with a list of simple cross and twist objects.
 */
public class Knot
    extends MultiplePairsPartition
{
  private Pin[] pins = null;
  private PairSegment[] pairs = null;
  private ThreadSegment[] fronts = null;
  private ThreadSegment[] backs = null;

  private Knot(final String title, int nrOfPairs)
  {
    super( title );
    setThreadConnectors( new Connectors<ThreadSegment>( nrOfPairs*2 ) );
    setPairConnectors( new Connectors<PairSegment>( nrOfPairs ) );

  }

  public static Knot rightKnot()
  {
    final Knot picot = new Knot( Localizer.getString( "KnottedPicotRight" ),1 );
    final ThreadSegment[][] ts =
        new ThreadSegment[][] {
            {
                t( p( 7, 0 ), p( 10, 8 ), p( 10, 8 ), p( 14, 10 ) ),
                t( p( 14, 10 ), p( 20, 15 ), p( 35, 5 ), p( 35, 15 ) ),
                t( p( 35, 15 ), p( 35, 25 ), p( 20, 15 ), p( 14, 20 ) ),
                t( p( 14, 20 ), p( 10, 22 ), p( 10, 22 ), p( 7, 30 ) ),
            },
            {
                t( p( 0, 10 ), p( 0, 15 ), p( 8, 18 ), p( 8, 18 ) ),
                t( p( 8, 18 ), p( 8, 18 ), p( 10, 22 ), p( 15, 22 ) ),
                t( p( 15, 22 ), p( 20, 22 ), p( 20, 8 ), p( 15, 8 ) ),
                t( p( 15, 8 ), p( 10, 8 ), p( 0, 15 ), p( 0, 20 ) ),
            },
        };
    picot.pairs = new PairSegment[] {
        new PairSegment( p( 0, 0 ), p( 50, 60 ), p( 50, -25 ), p( 0, 25 ), 0 ),
    };
    picot.pins = new Pin[] {
        new Pin( p( 28, 15 ) ),
    };
    picot.backs = new ThreadSegment[] {
        ts[0][0], ts[0][3], ts[1][0], ts[1][2]
    };
    picot.fronts = new ThreadSegment[] {
        ts[0][1], ts[0][2], ts[1][1], ts[1][3]
    };
    picot.getPairConnectors().setIns( picot.pairs );
    picot.getPairConnectors().setOuts( picot.pairs );
    picot.getThreadConnectors().setIns( ts );
    picot.getThreadConnectors().setOuts( ts[1], ts[0] );
    return picot;
  }

  private static Point p(
      final int x,
      final int y)
  {
    return new Point( x, y );
  }

  private static ThreadSegment t(
      final Point p1,
      final Point p2,
      final Point p3,
      final Point p4)
  {
    return new ThreadSegment( p1, p2, p3, p4 );
  }

  final Iterator<Drawable> pairIterator()
  {
    final DrawableList drawables = new DrawableList();
    drawables.addPairSegments( pairs );
    drawables.addPins( pins );
    return drawables.iterator();
  }

  @Override
  final Iterator<Drawable> threadIterator()
  {
    final DrawableList drawables = new DrawableList();
    drawables.addThreadSegments( backs );
    drawables.addThreadSegments( fronts );
    drawables.addPins( pins );
    return drawables.iterator();
  }

  final Iterator<Drawable> pinIterator()
  {
    final DrawableList drawables = new DrawableList();
    drawables.addPins( pins );
    return drawables.iterator();
  }

}
