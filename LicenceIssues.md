Discussions about these issues:
[1](http://groups.google.com/group/google-code-hosting/browse_thread/thread/c29b04d10111ab26/)
[2](http://groups.google.com/group/google-code-hosting/browse_thread/thread/8fd3954ae2456e53/)
[3](http://groups.google.com/group/bobbinwork/browse_thread/thread/1e538ff005c6db48/)
[4](http://groups.google.com/group/bobbinwork/browse_thread/thread/d84ecad9e8f65e71/)
[5](http://groups.google.com/group/google-code-hosting/browse_thread/thread/24cf5157ebbb5613/70dbbca5ff488fdd?lnk=gst&q=branches#70dbbca5ff488fdd)

## Introduction ##

The [GNU/GPL-FAQ](http://www.gnu.org/licenses/gpl-faq.html#GPLOutput) states:
> when a program translates its input into some other form, the copyright status of the output inherits that of the input it was generated from.
Therefore, as long as the XML files with diagrams (or fractions of diagrams) are bundled with the code under the GPL license, lacemakers can't publish copyrighted patterns illustrating the technique with BobbinWork diagrams ([sublicenses](http://dictionary.lp.findlaw.com/scripts/results.pl?co=dictionary.lp.findlaw.com&topic=f3/f34c6efa8bcf2d5f656a80f181c3274d)) unless the BobbinWork copyright holders provides another license too, see [wikipedia](http://en.wikipedia.org/wiki/Multi-licensing) about multi licensing.

Problems:
  * Over time the project may get more contributors/authors, each would have to agree with the other license or their code could not be used
  * Google Code doesn't want to host projects with multiple licenses
  * users would have to ask for license arangements

## Solution ##

The diagrams are separated from the code and placed in the wiki section of the SubVersion repository. The [wiki](http://code.google.com/p/bobbinwork/source/browse/#svn/wiki) section has another license than the [source code](http://code.google.com/p/bobbinwork/source/browse/#svn/trunk) section. The diagram section in the wiki serves as input for the software. However, the definitions of the basic stitches are kept together with the rest of the code and are included in the distributed jar file.