drop schema if exists pozoriste_is;
create schema pozoriste_is default character set utf8mb4 COLLATE utf8mb4_unicode_ci;
use pozoriste_is;


CREATE TABLE scena
(
	id                        INTEGER UNSIGNED NOT NULL AUTO_INCREMENT,
	nazivScene                 VARCHAR(64) NOT NULL,
	PRIMARY KEY (id)
);



CREATE TABLE sjediste
(
	brojSjedista          INTEGER UNSIGNED NOT NULL,
	idScene               INTEGER UNSIGNED NOT NULL,
	PRIMARY KEY (brojSjedista,idScene),
	FOREIGN KEY R_63 (idScene) REFERENCES scena(id)
);



CREATE TABLE gostujuca_predstava
(
	id                    INTEGER UNSIGNED NOT NULL AUTO_INCREMENT,
	naziv                 VARCHAR(64) NOT NULL,
	opis                  TEXT NOT NULL,
	tip                   VARCHAR(40) NOT NULL,
	pisac                 VARCHAR(40) NOT NULL,
	reziser               VARCHAR(40) NOT NULL,
	glumci                TEXT NOT NULL,
	PRIMARY KEY (id)
);



CREATE TABLE predstava
(
	id                    INTEGER UNSIGNED NOT NULL AUTO_INCREMENT,
	naziv                 VARCHAR(40) NOT NULL,
	opis                  TEXT NOT NULL,
	tip                   VARCHAR(40) NOT NULL,
	PRIMARY KEY (id)
);



CREATE TABLE repertoar
(
	id                    INTEGER UNSIGNED NOT NULL AUTO_INCREMENT,
	mjesecIGodina         DATE NOT NULL,
	PRIMARY KEY (id)
);



CREATE TABLE igranje
(
	termin                DATE NOT NULL,
	idScene               INTEGER UNSIGNED NOT NULL,
	idGostujucePredstave  INTEGER UNSIGNED NULL,
	idPredstave           INTEGER UNSIGNED NULL,
	idRepertoara          INTEGER UNSIGNED NOT NULL,
	PRIMARY KEY (termin,idScene),
	FOREIGN KEY R_40 (idScene) REFERENCES scena(id),
	FOREIGN KEY R_41 (idGostujucePredstave) REFERENCES gostujuca_predstava(id),
	FOREIGN KEY R_42 (idPredstave) REFERENCES predstava(id),
	FOREIGN KEY R_43 (idRepertoara) REFERENCES repertoar(id)
);



CREATE TABLE rezervacija
(
	id                    INTEGER UNSIGNED NOT NULL AUTO_INCREMENT,
	ime                   VARCHAR(20) NOT NULL,
	termin                DATE NOT NULL,
	idScene               INTEGER UNSIGNED NOT NULL,
	PRIMARY KEY (id,termin,idScene),
	FOREIGN KEY R_47 (termin,idScene) REFERENCES igranje(termin,idScene)
);



CREATE TABLE rezervisano_sjediste
(
	idScene               INTEGER UNSIGNED NOT NULL,
	brojSjedista          INTEGER UNSIGNED NOT NULL,
	idRezervacije         INTEGER UNSIGNED NOT NULL,
	termin                DATE NOT NULL,
	PRIMARY KEY (idScene,brojSjedista,idRezervacije,termin),
	FOREIGN KEY R_66 (brojSjedista,idScene) REFERENCES sjediste(brojSjedista,idScene),
	FOREIGN KEY R_68 (idRezervacije,termin,idScene) REFERENCES rezervacija(id,termin,idScene)
);



CREATE TABLE radnik
(
	idRadnik              INTEGER UNSIGNED NOT NULL AUTO_INCREMENT,
	ime                   VARCHAR(40) NOT NULL,
	prezime               VARCHAR(40) NULL,
	jmb                   VARCHAR(13) NOT NULL,
	statusRadnika         BOOLEAN NOT NULL,
    kontakt 			  TEXT NOT NULL,
	PRIMARY KEY (idRadnik)
);


CREATE TABLE radnik_koji_koristi_sistem
(
	korisnickoIme         VARCHAR(20) UNIQUE NOT NULL,
	heshLozinke           VARCHAR(64) NOT NULL,
	idRadnik              INTEGER UNSIGNED NOT NULL,
    tipKorisnika		  VARCHAR(64) NOT NULL,
    aktivan				  BOOLEAN NOT NULL,
    #postojiUSistemu BOOLEAN NOT NULL DEFAULT FALSE,
	PRIMARY KEY (idRadnik),
	FOREIGN KEY (idRadnik) REFERENCES radnik(idRadnik) ON DELETE CASCADE
);



CREATE TABLE administrativni_radnik
(
	idRadnik              INTEGER UNSIGNED NOT NULL,
	PRIMARY KEY (idRadnik),
	FOREIGN KEY (idRadnik) REFERENCES radnik_koji_koristi_sistem(idRadnik) ON DELETE CASCADE
);



CREATE TABLE azurira
(
	id                    INTEGER UNSIGNED NOT NULL AUTO_INCREMENT,
	idPredstave           INTEGER UNSIGNED NULL,
	idRepertoara          INTEGER UNSIGNED NULL,
	idGostujucaPredstave  INTEGER UNSIGNED NULL,
	datumIVrijeme         DATE NOT NULL,
	idRadnik              INTEGER UNSIGNED NULL,
	PRIMARY KEY (id),
	FOREIGN KEY R_30 (idRepertoara) REFERENCES repertoar(id),
	FOREIGN KEY R_31 (idGostujucaPredstave) REFERENCES gostujuca_predstava(id),
	FOREIGN KEY R_32 (idPredstave) REFERENCES predstava(id),
	FOREIGN KEY R_51 (idRadnik) REFERENCES administrativni_radnik(idRadnik)
);



CREATE TABLE kreira
(
	id                    INTEGER UNSIGNED NOT NULL AUTO_INCREMENT,
	idPredstave           INTEGER UNSIGNED NULL,
	idRepertoara          INTEGER UNSIGNED NULL,
	idGostujucePredstave  INTEGER UNSIGNED NULL,
	datumIVrijeme         DATE NOT NULL,
	idRadnik              INTEGER UNSIGNED NULL,
	PRIMARY KEY (id),
	FOREIGN KEY R_23 (idPredstave) REFERENCES predstava(id),
	FOREIGN KEY R_24 (idRepertoara) REFERENCES repertoar(id),
	FOREIGN KEY R_25 (idGostujucePredstave) REFERENCES gostujuca_predstava(id),
	FOREIGN KEY R_26 (idRadnik) REFERENCES administrativni_radnik(idRadnik)
);



CREATE TABLE karta
(
	id                    INTEGER UNSIGNED NOT NULL AUTO_INCREMENT,
	brojReda              INTEGER NOT NULL,
	brojSjedista          INTEGER NOT NULL,
	iznos                 FLOAT NOT NULL,
	termin                DATE NOT NULL,
	idScene               INTEGER UNSIGNED NOT NULL,
	PRIMARY KEY (id),
	FOREIGN KEY R_44 (termin,idScene) REFERENCES igranje(termin,idScene)
);



CREATE TABLE umjetnik
(
	biografija            TEXT NOT NULL,
	idRadnik              INTEGER UNSIGNED NOT NULL,
	PRIMARY KEY (idRadnik),
    FOREIGN KEY (idRadnik) REFERENCES radnik(idRadnik) ON DELETE CASCADE
);



CREATE TABLE vrsta_angazmana
(
	id                    INTEGER UNSIGNED NOT NULL AUTO_INCREMENT,
	Naziv                 VARCHAR(64) NOT NULL,
	PRIMARY KEY (id)
);



CREATE TABLE angazman
(
	datumOd               DATE NOT NULL,
	datumDo               DATE NULL,
	idUmjetnika           INTEGER UNSIGNED NOT NULL,
	idPredstave           INTEGER UNSIGNED NOT NULL,
	idVrsteAngazmana      INTEGER UNSIGNED NOT NULL,
	PRIMARY KEY (idUmjetnika,idPredstave,idVrsteAngazmana,datumOd),
    FOREIGN KEY R_16 (idUmjetnika) REFERENCES umjetnik(idRadnik),
	FOREIGN KEY R_17 (idPredstave) REFERENCES predstava(id),
	FOREIGN KEY R_18 (idVrsteAngazmana) REFERENCES vrsta_angazmana(id)
	#on delete cascade
);



CREATE TABLE biletar
(
	idRadnik              INTEGER UNSIGNED NOT NULL,
	PRIMARY KEY (idRadnik),
    FOREIGN KEY (idRadnik) REFERENCES radnik_koji_koristi_sistem(idRadnik) ON DELETE CASCADE
);
