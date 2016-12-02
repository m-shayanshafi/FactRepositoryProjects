README												June 2004
------------------------------------------------------------------------


REQUIRED
--------

16MB RAM. It's cross platform so you just need Java installed.

Main supported version is:

JRE for Java 1.50 beta 1 or greater. Download this for free from:
http://java.sun.com/j2se/1.5.0/download.jsp

For Java 1.4x you can use the run14 script.


RUNNING PACDASHER
-----------------

To run PacDasher, simply go to the PacDasher directory and execute 'run'.
Clicking on it in Windows Explorer should work; also good is typing 

run

in a command window. For variants of Un*x such as Linux,
the run.sh script does the same thing.

For variety, you can see the other mazes by looking at the .xml files in
the PacDasher directory and adding one as a command-line argument to
the run script. For example:

run monsterrobot.xml

The goal in every maze is just to collect all the dots. For further 
information see: http://www.oranda.com/java/pacdasher

BUILDING PACDASHER
------------------

* Ensure ant (1.6 or above is installed. (Assuming >= JDK1.5b1
  installed.)

* Configure the properties in build.properties

* On the command-line in the root directory of the distribution, call:
  ant build