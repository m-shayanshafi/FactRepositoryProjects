#!/bin/bash

LIBDIR=$(dirname $0)/lib

java -Xmx256m -cp ${LIBDIR}/commons-fileupload-1.2.1.jar:${LIBDIR}/jetty-6.1.22.jar:${LIBDIR}/jetty-util-6.1.22.jar:${LIBDIR}/servlet-api-2.5-20081211.jar:${LIBDIR}/crg-scoreboard.jar:${LIBDIR}:${CLASSPATH} com.carolinarollergirls.scoreboard.ScoreBoardManager

