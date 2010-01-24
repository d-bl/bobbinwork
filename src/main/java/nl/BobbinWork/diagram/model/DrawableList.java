package nl.BobbinWork.diagram.model;

import java.awt.geom.CubicCurve2D;
import java.util.ArrayList;

public class DrawableList
    extends ArrayList<Drawable>
{
  void addThreadSegments(
      ThreadSegment... segments)
  {
    for (final ThreadSegment segment : segments) {
      final ThreadStyle style = segment.getStyle();
      if (style.getWidth() > 0) {
        final CubicCurve2D curve = segment.getCurve();
        add( new Drawable( curve, style.getShadow() ) );
        add( new Drawable( curve, style ) );
      }
    }
  }
  
  void addPairSegments(
      PairSegment... segments)
  {
    for (PairSegment segment:segments)
      add( new Drawable( segment.getCurve(), segment.getStyle() ) );
  }

  void addPins(
      Pin... pins)
  {
    for (Pin pin:pins)
      add( pin.getPairs().iterator().next() );
  }
}
