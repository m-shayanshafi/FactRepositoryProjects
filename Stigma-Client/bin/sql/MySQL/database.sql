-- =============================================================================
-- Database Name: stigma
-- =============================================================================
DROP DATABASE IF EXISTS `stigma`;

CREATE DATABASE IF NOT EXISTS `stigma`;

DROP USER stigmaAdmin;
GRANT SELECT, INSERT, UPDATE, DELETE, CREATE, DROP ON stigma.* TO stigmaAdmin@"%" IDENTIFIED BY 'tajne1haslo';
GRANT SELECT, INSERT, UPDATE, DELETE, CREATE, DROP ON stigma.* TO stigmaAdmin@"localhost" IDENTIFIED BY 'tajne1haslo';

DROP USER stigma;
GRANT SELECT, INSERT, UPDATE, DELETE ON stigma.* TO stigma@localhost IDENTIFIED BY 'tajne1haslo';

FLUSH PRIVILEGES;
