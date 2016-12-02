@echo off
ping 127.0.0.1 -n 4 -w 1000 > nul
copy dist\ShoddyBattleU.jar dist\ShoddyBattle.jar
del dist\ShoddyBattleU.jar
call bin\StopApp-NT.bat
call bin\StartApp-NT.bat

