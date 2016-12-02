#! /bin/bash
if [[ -e server_config.properties ]]; then 
	cp server_config.properties server_config.properties.bak;
fi
cp server_config.properties.tests server_config.properties
