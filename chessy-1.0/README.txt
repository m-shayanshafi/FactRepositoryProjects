This is a simple set of Java code that allow you to review a Yahoo chess match.

It was coded by Dav Coleman in March 2002.

http://www.danger-island.com/~dav/

See LICENSE.txt for licensing information.

It is open source GPL2 Java2 code. If you have the source distribution then
you should unzip it and use ant to build. See http://ant.apache.org/

There should be a pre-built ChessY.jar in the dist/ directory.

To run the program invoke from the command line:

java -jar dist/ChessY.jar [gamehistory]

[gamehistory] is an optional parameter.

Where [gamehistory] is a text file containing the Yahoo! game history as 
emailed to you by Yahoo. An example game.txt gamehistory is included in 
distribution. You can see from the example game why I really needed to 
review my games.

You can also eschew the parameter and load game history files from the
File->Open menu.

Example:
java -jar dist/ChessY.jar game.txt
