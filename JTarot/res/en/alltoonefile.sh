ls *.xml | sed "s/.*/echo \\\"\<file name=\\\\\"&\\\\\"\>\\\" \>\> allinone.html; mmv -a & allinone.html;echo \\\"\<\/file\>\\\" \>\> allinone.html/" |sh
