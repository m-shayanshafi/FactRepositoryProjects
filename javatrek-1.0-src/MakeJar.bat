cd bin
C:\j2sdk1.4.2\bin\jar -cvf javatrek.jar .\org\gamehost\jtrek\javatrek\*.class
C:\j2sdk1.4.2\bin\jar -cvf bots.jar .\org\gamehost\jtrek\javatrek\bot\*.class
move javatrek.jar ..\lib
move bots.jar ..\lib
cd ..
copy /y build\org\gamehost\jtrek\javatrek\bot\*.class lib\com\joe\games\javatrek\bot
