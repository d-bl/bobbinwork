A tool that shows various representations of a bobbin lace diagram. Change one representation and the other representations are automtically changed accordingly. Thus consistency is guaranteed.

| Table of Contents  |![![](http://bobbinwork.googlecode.com/svn/wiki/images/screenshot-thumb.png)](http://bobbinwork.googlecode.com/svn/wiki/images/screenshot.png)|
|:-------------------|:---------------------------------------------------------------------------------------------------------------------------------------------|

# Why #

General purpose graphical tools don't understand the relations between the dots and lines of a lace design, so you have to do a lot of thinking to create and maintain the relationships, if possible at all. Graphical tools adapted for bobbin lace makers simplify the creational phase of some of the tasks, but don't solve the fundamental cohesion problem. Thus adjusting a design is still a tedious task.

# What #

The BobbinWork approach closely follows how a lace maker learns and applies the stitches in working order and thus creates and maintains the cohesion missed elsewhere. A single design can have various presentations, create just one to get them all at your finger tips:
  * descriptive instructions (presented as a folding tree for as much or little detail as desired)
  * thread diagrams
  * pair diagrams
  * prickings with conventions that can vary per lace style (not yet implemented)
  * animation (not unlike a [voodoo board](http://www.mail-archive.com/search?q=voodoo&l=lace%40arachne.com))
    * walk through the descriptive instructions to highlight the corresponding part in the diagram or vice versa,
    * generate a sequence of screen shots of the diagrams. Other tools can turn these screen shots in animated gifs or interactive slide shows.

# In practice #

The polar grid generator is not yet integrated. It allows for stitches with a constant shape, not high at the inside and wide at the outside.

The main application can't yet:
  * add pairs nor throw them out again
  * make knots nor sewings
  * let you drag and drop stitches arround
it can:
  * construct basic and complex stitches from cross and twist
  * apply stitches in simple and complex grounds
  * make a variation of a complex ground with a few clicks
  * use pairs as threads
  * apply threads with different colors and widths
  * gimps (solitaire thick threads) are mimiced by using a pair as a thread (unfortunately a gimp is usually an added pair)

# Who can help? #
_Any bobbin lace making (and other) Java developers, Java Webstart wizzards, Bezier wizzards, polygon mergers, and translators: please help_

# Project cost and other statistics #
<table><tr><td valign='top'>
<wiki:gadget url="http://www.ohloh.net/p/66188/widgets/project_cocomo.xml" height="200" width="380" border="0" /><br>
<br /><strong>Commit activity time line</strong><br />
<img src='http://www.ohloh.net/p/bobbinwork/analyses/latest/commits_spark.png' />
</td><td valign='top'>
<wiki:gadget url="http://www.ohloh.net/p/66188/widgets/project_languages.xml" height="200" width="380" border="0"/><br>
</td></tr></table>