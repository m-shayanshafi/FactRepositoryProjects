#!/bin/sh

export LD_LIBRARY_PATH=lib/osx
CP=lib/jake2.jar:lib/jogl.jar:lib/linux/joal.jar

exec java -Xmx100M -Djava.library.path=lib/osx -cp $CP jake2.Jake2
