@echo off
ECHO You need to install PSCP (from PuTTy package) in path for this batch to work
SET /P username=SourceForge.net username: 
pscp -r -C -sftp ../res/metadata/xsd/*.xsd %username%,stigma@web.sourceforge.net:htdocs/schemes/