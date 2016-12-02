#!/bin/sh

export PYTHONPATH="$HOME/lib/python" 
P="$HOME/bin" 
 case "$#" in
'1')
if [ $1 = 'clone' ]
  then
     $P/hg clone https://university-game.googlecode.com/hg/ university-game
      cp ./hg.sh ./university-game
  fi
    $P/hg $1
;;
'2')
$P/hg $1 $2
;;
'3')
$P/hg $1 $2 $3
;;
*)
echo "entrez les arguments normaux pour hg"
echo "sauf pour clone ou vous pouvez vous contenter de"
echo "  ./hg.sh clone  " 
echo "sans arguments (l'adresse est deja rentre)"
;;
esac