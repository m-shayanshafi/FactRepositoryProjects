java -Djava.util.logging.config.file=logging.properties -Djava.endorsed.dirs=jars/lib -cp jars/lib/retroweaver.jar:jars/lib/bcel-5.1.jar:jars/lib/jace.jar:jars/lib/Regex.jar:jars/pacdasher_resources.jar:jars/lib/jaxp-api.jar:jars/lib/sax.jar:jars/lib/xercesImpl.jar:jars/lib/xml-apis.jar -Xmn8m -Xms32m -XX:SurvivorRatio=2 -jar jars/pacdasher14.jar $1

