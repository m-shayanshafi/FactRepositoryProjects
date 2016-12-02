#!/bin/sh

cd $HOME
rm -f .hgrc
echo "identifian Gmail(sans @gmail.com)?"
read US
echo "[ui]" >> .hgrc
echo "username = $US@gmail.com" >> .hgrc
echo "nom utilisateur a la fac?"
read U
echo "[http_proxy]" >> .hgrc
echo "host = 152.77.24.38:3128" >> .hgrc
echo "user = $U" >> .hgrc
echo "mot de passe?"
read U
echo  "passwd = $U" >> .hgrc
echo "[auth]" >> .hgrc
echo "foo.prefix = *" >> .hgrc
echo "foo.username = $US@gmail.com" >> .hgrc
echo "mot de passe google code?"
read U
echo "foo.password = $U" >> .hgrc