#!/bin/bash
cd Stigma-Server
. apply_tests_config.sh
java -jar Stigma-Server.jar 1> /dev/null &
cd ../Stigma-Client
java -jar Stigma-Client.jar &
cd ..
