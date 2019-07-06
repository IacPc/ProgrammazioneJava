SET NAMES latin1;

BEGIN;
DROP DATABASE IF EXISTS Prog_Av;
CREATE DATABASE Prog_Av;
COMMIT;
USE Prog_Av;


DROP TABLE  IF EXISTS Messaggi;
CREATE TABLE Messaggi(
	NomeMittente	 VARCHAR(32) NOT NULL,
        NomeDestinatario VARCHAR(32) NOT NULL,
        DataInvio        TIMESTAMP   NOT NULL,
        Testo            TEXT,
	PRIMARY KEY(NomeMittente,DataOraInvio)
)ENGINE = InnoDB DEFAULT CHARSET=latin1;



