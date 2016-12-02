start "Stigma-Config" /D.\Stigma-Server /B apply_tests_config.bat
start "Stigma-Server" /D.\Stigma-Server java -jar Stigma-Server.jar
start "Stigma-Client" /D.\Stigma-Client /B java -jar Stigma-Client.jar

