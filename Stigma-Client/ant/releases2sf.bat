@echo off
ECHO You need to install PSCP (from PuTTy package) in path for this batch to work
SET /P username=SourceForge.net username: 
pscp -r -C -scp ../build/releases/* %username%,stigma@frs.sourceforge.net:/home/frs/project/s/st/stigma/