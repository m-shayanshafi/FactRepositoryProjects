#! /bin/zsh

cd "${0:h}"

java -Xmx256m -cp ./lib/commons-fileupload-1.2.1.jar:./lib/jetty-6.1.22.jar:./lib/jetty-util-6.1.22.jar:./lib/servlet-api-2.5-20081211.jar:./lib/crg-scoreboard.jar:./lib com.carolinarollergirls.scoreboard.ScoreBoardManager

