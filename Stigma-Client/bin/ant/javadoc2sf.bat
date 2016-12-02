@echo off
ECHO You need to install PSCP (from PuTTy package) in path for this batch to work
SET /P username=SourceForge.net username: 
pscp -r -C -sftp ../build/javadoc/ %username%,stigma@web.sourceforge.net:htdocs/javadoc/