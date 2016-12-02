Yaha! A math game for everyone.
Huahai Yang <huahai@yyhh.org>
Last updated: %%mtime(%c)

Since you are reading this file, you must have successfully unpacked Yaha! package. 

If you have downloaded source package, let's compile the code. If you downloaded bin packge, please skip compilation to run Yaha!

= Compilation =

You need to have a JDK installed to compile the code. If you can run ""javac"" on your system, you are fine.

It's easiest to compile the code with maven2 <http://maven.apache.org>. If your system has maven2 installed, just cd to this directory, and run:
  
```
  mvn package 
```

The code will compile, and a jar file, e.g. yaha-1.0.jar, will be created in the ""target"" directory. Now you can run Yaha!

= Run Yaha!=

Yaha! can be run as either a standalone desktop application, or an Applet on the Web.

== Run as Application ==

Run the program with following command:

```
  java -jar yaha-1.0.jar
```

== Run as Applet ==

Create a HTML file with the following code: 

  <applet code="Yaha.class" 
  codebase="path/to/directory" 
  archive="yaha-1.0.jar"
  width="640" height="400">

Where "path/to/direcotry" is the directory that containts yaha-1.0.jar. Then point your browser to the HTML file.  

You can put the Applet on your own Web site too, please read ""LICENSE.txt"" for terms and conditions. 

% vim:ft=txt2tags
