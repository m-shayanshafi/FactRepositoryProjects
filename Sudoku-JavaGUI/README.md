A Java Swing GUI version of a Sudoku game implementation, in which it performs depth-first search with backtracking technique to search for all possible solution(s).   

The analysis of the time complexity of searching the solution:  Overall, it runs O(n^2 * m!), where n is the number of the outer and number of the inner buckets of the Sudoku game (i.e. in total there are 9 x 9 = 81 buckets) and m is the average total number of the possible values of each bucket.   Notice that m = n in the worst case senario.

For detailed documentation, please refer ./documentations.doc

To run the program, download the Sudoku-v{$version}.jar file and issue this command :   
java -cp Sudoku-v{$version}.jar kw.sudoku.view.Sudoku 

The game (screenshot 1) has the following menus:
1.	Game	
	New:		Create a blank new game
	Solution: 	Show the solution of the current game
              Level – V.Easy: 	Set the game level to very easy 
              Level - Easy: 	Set the game level to easy
              Level - Medium: Set the game level to medium
              Level - Hard: 	Set the game level to hard
              Level - V.Hard: 	Set the game level to very hard
              Level - Expert: 	Set the game level to expert
	Save:		Save the current game
	Exit:		Exit the current game
2.	Action
	Check:		Display some invalid moves
	Undo:		Undo the current move (i.e. restore the previous state)
3.	Help
	About:		Show the version of the software

In each game, user can set the level of difficulty (screenshot2) or can use the solution finder (i.e. go to Game -> Solution) to find all possible solutions (screenshot3).   

![screenshot](https://raw.github.com/wwken/Games/master/Sudoku-JavaGUI/images/s1.png)
Screen shot 1: The game starting screen (with the difficulty of being 'Very Hard') 

![screenshot](https://raw.github.com/wwken/Games/master/Sudoku-JavaGUI/images/s2.png)
Screen shot 2: User can choose different level of the difficult of the game

![screenshot](https://raw.github.com/wwken/Games/master/Sudoku-JavaGUI/images/s3.png)
Screen shot 3: User chooses to have the program to generate all the possible solutions on the given Sudoku puzzle.  In this example, 13 solutions are found in the given Sudoku puzzle.


Author: Ken Wu
Date:	2012-2013 

**Note: in order to protect the copyright, I have taken out some *.java source files.  Any serious inquirer can contact me for the whole source code.

© 2012-2013 Ken All rights reserved. 