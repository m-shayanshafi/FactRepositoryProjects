===========================================
 ReadMe file for Stigma-Server application
===========================================

Project web page:
http://stigma.sourceforge.net

Support, wiki, trac, etc.:
http://sourceforge.net/apps/trac/stigma/

For license information see LICENSE.txt

------------------------------
 Requirements
------------------------------

 - Java(TM) SE Runtime Environment version 1.6.0_20 or newer

---------------------------
 Quick start / Tests
---------------------------

0. Works only with "SQLite Server package" or "AllJdbcDrivers Server package".

1. Extract server package.

2. Use proper 'apply_tests_config' script or manually
   rename file 'server_config.properties.tests' to 'server_config.properties'.

3. Run server.

4. Download client package.

5. Extract client package.

6. Copy Stigma-Client.jar to directory where server was extracted.

7. Run client.

8. Login using one of test account: mmorpg, rabbit, test. Each with password: qwe. 
   Every account has similar actors. Choose one and enjoy.

[IMPORTANT] This will allow only local tests! 
            All clients has to be run on same machine and from same directory as server.
            For more complex use read instructions below and modify server_config.properties.


---------------------------
 Installation / Deployment
---------------------------

0. Extract server package.

1. Create a database and one user with SELECT, INSERT, UPDATE, DELETE rights on that database 
   (restricted rights for security reasons).
   This step is not necessary - server can work on existing database using existing user to access.
   We just recommend it for security/backup/etc.
   Default script is in sql/<supported-database-management-system-name>/database.sql
   Currently MySQL and SQLite database management systems are supported. For SQLite even 'empty' database file is provided.
   Server should work with any other relational database system, as long as SQL queries look similar
   
2. Create tables in database.
   Modify and deploy script from: sql/<supported-database-management-system-name>/tables.sql
   OR create database with similar structure.
   Every database system listed in "supported databases" directory has proper server package with JDBC driver, 
   available for download on project page.
   
3. Configure game server.
   When server is launched for the first time with no configuration file present,
   it will create an empty server_config.properties file with default values for properties instead of running and exit.
   Minimum configuration consists of:
    sql-driver=<driver for JDBC connection (must be in classpath), by default points to SQLite driver provided>
    sql-url=<connection string to database>
    sql-login=<login name for database access, not needed in SQLite>
    sql-password=<password for database access, not needed in SQLite>
    resource-root=<path to directory with resources (maps, terrain sets etc), by default points to provided res directory>
    resource-set=<which resource set from directory should be used, by default 'default'>
    client-resource-root=<URL with HTTP/FTP/file protocol for client to access resources>
    resource-http-server-on=<enables/disables server's internal HTTP engine for distributing resources>
   
4. Configure server logger.
   You can tweak logger configuration using log_config.properties file
   
5. Deploy resource files in a location client can access.
   Copy whole resource directory to place it can be accessed using given 'client-resource-root' URL.
   We encourage compression of XML resource files for final deployment.
   Client will not require original (non-compressed) files. To do this:
    - Compress XML files using GZip so their names will end with ".xml.gz". 
   OR: set client to work on uncompressed files - this will slow work on real network but may easy test. To do this:
    - add 'client-resource-compression=false' line to server configuration file.
    - if you wish to do only local tests you can set 'client-resource-root' to absolute path to server resources, 
      with "file://" prefix OR relative path without any prefix.
   
6. [OPTIONAL] Deploy client application as an applet.
   Download proper version of client application (first two numbers in version must be same as in server). 
   Client can be embedded in web page (for example using HTML code in www/index.html).
   Because of what, in our opinion, is a bug in Java/JAXB, applet must be signed to work properly. 
   Currently default package is signed using "Thawte Freemail" certificate. 
   Unsigned package is also available - so it is possible to sign client with more "user friendly" certificate.  
   
7. Run server and enjoy!


---------------------------
 Example configurations
---------------------------

0. Quick start
--------------------

sql-driver=org.sqlite.JDBC
sql-url=jdbc:sqlite:./sql/SQLite/tests/StigmaWithTests.db
resource-root=res
resource-set=tests
resource-http-server-on=true
client-resource-root=http://localhost:6980/resource/
client-resource-compression=false

1. 'Real' deployment:
----------------------

sql-driver=org.gjt.mm.mysql.Driver
sql-url=jdbc:mysql://some.MySQL.server/stigma
sql-login=stigma
sql-password=**PASSWORD**
resource-root=res
resource-set=tests
client-resource-root=http://some.stigma.resource.server/stigma-resources

#############################################

Report all bugs, functionality request etc. using Trac SourceForge.net:
https://sourceforge.net/apps/trac/stigma/newticket?component=Server