Requirements
------------

Java Runtime Environment 1.3 or later
Screen resolution 1024x768 or higher



Contents
--------

readme.txt     this file
license.txt    license information
changes.txt    change history
bloodball.jar  program file
bloodball.bat  windows batch file
bloodball.sh   unix shell script
lib/*          external libraries



Installation
------------

Unzip the contents of the binary distribution (bloodball_bin.zip)
in a directory of your choice.



Running the game
----------------

Start Blood Ball by either

- executing bloodball.bat (Windows)
- executing bloodball.sh (Unix, you might have to set its executable flag)
- typing "java -jar bloodball.jar" at the command line (any OS), or
- double clicking bloodball.jar (Windows)

The program will try to guess the language to use for the game by examining 
the settings of your operating system.
You may use an optional parameter to force a specific language. Currently
two localizations are supported:

en - english
de - german

That is, by starting the game using

"java -jar bloodball.jar de"

you force it to use the german localization.



Rules
-----

Blood Ball follows the original rules of the Blood Bowl game - currently
the basic rules and parts of the veteran rules of the second edition 
are implemented.
See changes.txt for details.

Rules Changes:

A player whom the ball scatters to doesn't get the option to pick it up
immediately. Instead every player may pick up the ball before any movement
without loosing its ordinary movement. A player standing in the antagonistic
End Zone will pick up the ball automatically, therefore scoring a touch down.



Contact
-------

http://sf.net/projects/bloodball/
mailto:ipreuss@users.sourceforge.net