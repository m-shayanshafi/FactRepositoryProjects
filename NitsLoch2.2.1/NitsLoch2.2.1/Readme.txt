Basics:

Explore!  As you kill enemies you will find weapons and armor.  You will
also see shops where you can purchase things.  Watch out for people
stealing your money though.  REMEMBER TO REST.  Passing time will heal
your character and it is very important, expecially early on in the
game.  Use numpad 5 or spacebar to rest.  Some enemies in the game are
special, and you will not be allowed to proceed to the next city until
they are all killed.  If you can't move on to the next city, you haven't
killed the "leaders" yet.

Game Controls:

Arrow keys will move the player.  Also, you can use numpad: 8 (forward),
6 (right), 4(left), 2 (down).  1, 7, 9 and 3 are diagonal.  5 is to
pass.  Passing will heal your player at the risk of an enemy spawning.
The space bar is also used for passing time.  In dungeons you can only
move forward, and turn left, right, and turn around.

You can also move around by clicking on the game window.  If you click
your character, you will pass time.  You can not move with the mouse in
dungeons.

Hold control to shoot in the specified direction instead of moving your
character.

Map Editor:

The editor is RoofNotifier.jar.  In order to use it, you must have an
extracted scenario in the same folder as it.  The scenario.xml needs to
be in the same folder as well as the map and images that go along with
it.  When you are done editing it, zip up the files and rename it
<name>.nits.  Then update your ScenarioFile to point to the new .nits
file.

You need a 3 button mouse to use the editor.  The left button places
Land, which appears in the top half of the "Roof Action" window, the
middle button copies whatever you click on (also useful for finding
information about that location), and the right button places objects
that are currently selected in the bottom half of the "Roof Action"
window.  The edit menu lets you add new levels and give them a size.
When making levels, you must create exits that go between your cities.
If you want a one way only gate, make sure to not open the gate until
all of the "leader" enemies are dead.  You can do this by unchecking the
"Is Open" checkbox when placing the exit on the map.  It will open when
the leaders are dead on that city, and any dungeons that city happens to
have.  You must add a player start location to each map.

When placing dungeons, use the "dungeon level" option to specify which
dungeon level you want that level to be.  This will correspond with the
dungeon level you specify in the scenario file, and will determine which
enemies spawn.  Do the same thing with ladders.  IMPORTANT: when placing
a ladder that will bring you back to the surface, set the dungeon level
to 0.

If you want a teleporter, place an exit that has the "next city" option
set to the current city.  Then it will take you to destination row, and
column.  If it does not take you to the same city that you're already
in, the destination row and column don't matter.

It is possible to make a map that is impossible to complete.  Make sure
you have the correct city destinations set up and that you don't leave a
city with leaders alive that you won't be able to get back to later.
The game won't be possible to complete.

The editor is fairly complicated, and it's hard to explain it all here.
If you have questions about it, usually someone is in the #nitsloch IRC
room on irc.freenode.net.  Or you can email me at ddwatts at gmail.com.

Scenario XML Editor:
RoofDamager is a GUI application that will make it much easier to create
scenario XML files.  It is pretty straightforward, so I won't go into
details, but it should eliminate the need to edit the XML file by hand.

If you create a scenario, please send it to me so I can upload it to my
website so other people can play it.  My website is:
http://lochnits.com/

Scenario building: See the scenario html document, and the example
scenario files.
