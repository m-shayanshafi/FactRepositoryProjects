java -version:1.5 -Djava.util.logging.config.file=logging.properties -Djava.endorsed.dirs=jars\lib -cp jars\pacdasher_resources.jar;jars\lib\jaxp-api.jar;jars\lib\sax.jar;jars\lib\xercesImpl.jar;jars\lib\xml-apis.jar -Xmn8m -Xms32m -XX:SurvivorRatio=2 -jar jars\pacdasher.jar %1%

