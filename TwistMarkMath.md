By conventions lace makers mark twists in pair diagrams with short perpendicular lines. In our application this line goes approximately through the centre of a pair segment.

The figures below inspired the approximation (no need to be exact) of `t`. The final calculation can be found in the method [createTwistMark](http://code.google.com/p/bobbinwork/source/browse/trunk/src/nl/BobbinWork/diagram/math/Annotations.java). The black figures are adapted screen shots from the interactive pages of [AbleStable](http://www.ablestable.com/play/bezier/), follow the link to _Understanding Bezier Curves_

The basic idea for calculation: the ratio of the distances of the end points to both control points. Just `r1/(r1+r4)` is too simple as thus the direction would play no role.

![http://bobbinwork.googlecode.com/svn/wiki/images/bezier/Divide.gif](http://bobbinwork.googlecode.com/svn/wiki/images/bezier/Divide.gif)

Inspiration for the correction:

![http://bobbinwork.googlecode.com/svn/wiki/images/bezier/divide2.png](http://bobbinwork.googlecode.com/svn/wiki/images/bezier/divide2.png)

The following diagram illustrates the variables used in further calculations.

![http://bobbinwork.googlecode.com/svn/wiki/images/bezier/Casteljau.png](http://bobbinwork.googlecode.com/svn/wiki/images/bezier/Casteljau.png)