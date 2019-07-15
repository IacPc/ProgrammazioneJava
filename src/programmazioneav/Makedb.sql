SET NAMES latin1;

BEGIN;
DROP DATABASE IF EXISTS prog_av;
CREATE DATABASE Prog_Av;
COMMIT;
USE Prog_Av;


DROP TABLE  IF EXISTS Messaggi;
CREATE TABLE Messaggi(
        Prog             INTEGER     NOT NULL,
	NomeMittente	 VARCHAR(32) NOT NULL,
        NomeDestinatario VARCHAR(32) NOT NULL,
        DataInvio        TIMESTAMP   NOT NULL,
        Testo            TEXT,
	PRIMARY KEY(Prog,NomeMittente,DataInvio)
)ENGINE = InnoDB DEFAULT CHARSET=latin1;



