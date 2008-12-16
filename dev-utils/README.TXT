development utilities, copies from original sites
each includes its own own license

trunk/build.xml refers to these with ../bw-dev-utils
unzip each at bw-dev-utils (not at bw-dev-utils/...)

------------------------------------------

use subversion for the download phase
or download the zip files one by one 

TODO: see section 1.10 Bootstrapping your build scripts
Smart, John Ferguson. Java Power Tools. Beijing: O'Reilly, 2008. 

the download procedure with Eclipse Ganymede

menu: file - import
- SVN
  - projects from SVN
next 
create new repository location (if not done already, path without trunk!):
  http://bobbinwork.googlecode.com/svn
select trunk
finish
select new project wizard
- general
  - project
next
project name: bw-dev-utils
finish
------------------------------------------
