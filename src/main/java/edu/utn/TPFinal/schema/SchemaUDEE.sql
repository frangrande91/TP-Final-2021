CREATE DATABASE IF NOT EXISTS UDEE;
#drop database UDEE;
use UDEE;

CREATE TABLE IF NOT EXISTS brands(
	id_brand INT NOT NULL AUTO_INCREMENT,
	`name` VARCHAR(50) NOT NULL,
	CONSTRAINT pk_brand PRIMARY KEY (id_brand)
);

CREATE TABLE IF NOT EXISTS models(
	id_model INT NOT NULL AUTO_INCREMENT,
	id_brand INT NOT NULL,
	`name` VARCHAR(50) NOT NULL,
	CONSTRAINT pk_model PRIMARY KEY (id_model),
	CONSTRAINT fk_model_brand FOREIGN KEY (id_brand) REFERENCES brands(id_brand)
);

CREATE TABLE IF NOT EXISTS meters (
	id_meter INT NOT NULL AUTO_INCREMENT,
	id_model INT NOT NULL,
	serial_number VARCHAR(50) NOT NULL,
	`password` VARCHAR(16) NOT NULL,
	CONSTRAINT pk_meter PRIMARY KEY (id_meter),
	CONSTRAINT fk_meter_model FOREIGN KEY (id_model) REFERENCES models(id_model),
	CONSTRAINT unq_serial_number UNIQUE (serial_number)
);

CREATE TABLE IF NOT EXISTS users(
	id_user INT NOT NULL AUTO_INCREMENT,
	`name` VARCHAR(50) NOT NULL,
	last_name VARCHAR(50) NOT NULL,
	username VARCHAR(50) NOT NULL,
	`password` VARCHAR(16) NOT NULL,
	type_user VARCHAR(50) NOT NULL,
	CONSTRAINT pk_user PRIMARY KEY (id_user),
	CONSTRAINT unq_username UNIQUE (username)
);

SELECT * FROM users;

ALTER TABLE users CHANGE COLUMN type_user type_user INT NOT NULL DEFAULT 1;

CREATE TABLE IF NOT EXISTS rates(
	id_rate INT NOT NULL AUTO_INCREMENT,
	`value` DOUBLE NOT NULL,
	type_rate VARCHAR(50) NOT NULL,
	CONSTRAINT pk_rate PRIMARY KEY (id_rate)
);

CREATE TABLE IF NOT EXISTS addresses(
	id_address INT NOT NULL AUTO_INCREMENT,
	id_meter INT NOT NULL,
	id_user INT NOT NULL,
	id_rate INT NULL,
	address VARCHAR(100) NOT NULL,
	CONSTRAINT pk_address PRIMARY KEY (id_address),
	CONSTRAINT fk_address_meter FOREIGN KEY (id_meter) REFERENCES meters(id_meter),
    CONSTRAINT fk_address_user FOREIGN KEY (id_user) REFERENCES users(id_user),
    CONSTRAINT fk_address_rate FOREIGN KEY (id_rate) REFERENCES rates(id_rate)
);


CREATE TABLE IF NOT EXISTS bills(
	id_bill INT NOT NULL AUTO_INCREMENT,
	id_address INT NOT NULL,
	id_meter INT NOT NULL,
	id_user INT NOT NULL,
	initial_measurement DATETIME NOT NULL,
	final_measurement DATETIME NOT NULL,
	total_consumption DOUBLE NOT NULL,
	total_payable DOUBLE,
	expiration DATETIME default ( now() + DAY*15 ),
	CONSTRAINT pk_bill PRIMARY KEY (id_bill),
	CONSTRAINT fk_bill_address FOREIGN KEY (id_address) REFERENCES addresses(id_address),
	CONSTRAINT fk_bil_meter FOREIGN KEY (id_meter) REFERENCES meters(id_meter),
	CONSTRAINT fk_bill_user FOREIGN KEY (id_user) REFERENCES users(id_user)
);

alter table bills add column `date` datetime default now();
alter table bills change column expiration expiration datetime default ( now() + DAY*15 );
/*alter table bills change column expiration expiration datetime default DATE_ADD("2017-06-15", INTERVAL 10 DAY)*/

/*select DATE_ADD("2017-06-15", INTERVAL 10 DAY) * from biills;*/

CREATE TABLE IF NOT EXISTS measurements(
	id_measurement INT NOT NULL AUTO_INCREMENT,
	id_meter INT NOT NULL,
	id_bill INT,
    date_time DATETIME,
	quantity_kw DOUBLE NOT NULL,
    price_measurement DOUBLE DEFAULT 0,
	CONSTRAINT pk_measurement PRIMARY KEY (id_measurement),
	CONSTRAINT fk_measurement_meter FOREIGN KEY (id_meter) REFERENCES meters(id_meter),
	CONSTRAINT fk_measurement_bill FOREIGN KEY (id_bill) REFERENCES bills(id_bill)
);


/* Changes (They are already implemented in table creation) */

/*
ALTER TABLE measurements ADD COLUMN id_rate INT;
ALTER TABLE measurements ADD COLUMN price_measurement DOUBLE DEFAULT 0;
ALTER TABLE measurements ADD CONSTRAINT fk_measurements_rates FOREIGN KEY (id_rate) REFERENCES rates (id_rate) ON DELETE CASCADE ON UPDATE CASCADE;
ALTER TABLE measurements CHANGE COLUMN id_bill id_bill INT;
ALTER TABLE bills ADD COLUMN expiration DATETIME;
*/


/* INSERTS */

INSERT INTO brands(`name`) VALUES
("Nike"),
("Adidas");

INSERT INTO models(id_brand,`name`) values
(1,"SB"),
(1,"Janoski"),
(2, "D10S");

INSERT INTO meters(`id_model`,`serial_number`,`password`) VALUES
(1,"123456","123456"),
(1,"1234567","1234567"),
(2,"1234568","1234568")
;

INSERT INTO users(`name`, last_name, username, `password`, type_user) VALUES
("Pepe", "Grillo", "pepegrillo", "123", "Client"),
("Pepe2", "Grillo2", "pepegrillo2", "1234", "Client"),
("Pepe3", "Grillo3", "pepegrillo3", "1235", "Client")
;

INSERT INTO rates(`value`,`type_rate`) VALUES
(200,"A"),
(300,"B")
;

INSERT INTO addresses(`id_meter`,`id_user`,`address`,`id_rate`) VALUES
(1,1,"Brown 1855",1),
(2,1,"Castelli 5685",2),
(3,2,"Juan J. Paso 2056",2)
;

INSERT INTO `measurements` (`id_meter`,`id_bill`,`date_time`,`quantity_kw`) VALUES
(1,null,NOW(),2),
(1,NULL,NOW(),5),
(1,NULL,NOW(),9),
(1,NULL,NOW(),15),
(1,NULL,NOW(),17),
(1,NULL,NOW(),18),
(1,NULL,NOW(),24),
(2,NULL,NOW(),8),
(2,NULL,NOW(),15),
(2,NULL,NOW(),20),
(2,NULL,NOW(),26),
(2,NULL,NOW(),30),
(2,NULL,NOW(),31),
(2,NULL,NOW(),34),
(3,NULL,NOW(),8),
(3,NULL,NOW(),15),
(3,NULL,NOW(),24),
(3,NULL,NOW(),30),
(3,NULL,NOW(),35),
(3,NULL,NOW(),38),
(3,NULL,NOW(),40);

DELETE FROM measurements;

 SELECT * FROM addresses;
 SELECT * FROM bills;
 SELECT * FROM brands;
 SELECT * FROM measurements;
 SELECT * FROM meters;
 SELECT * FROM models;
 SELECT * FROM rates;
 SELECT * FROM users;

/*PUNTO 2
La facturación se realizará por un proceso automático en la base de datos. Se
debe programar este proceso para el primer día de cada mes y debe generar una
factura por medidor y debe tomar en cuenta todas las mediciones no facturadas
para cada uno de los medidores, sin tener en cuenta su fecha. La fecha de vencimiento de
esta factura será estipulado a 15 días.
*/

/*
- una factura por medidor
- todas las mediciones no facturadas
*/
DROP PROCEDURE IF EXISTS liquidate_client;
DELIMITER $$
CREATE PROCEDURE liquidate_client(pIdClient INT)
BEGIN
	DECLARE vIdMeter INT;
	DECLARE vIdAddress INT;
	DECLARE vInitialMeasurement DATETIME;
	DECLARE vFinalMeasurement DATETIME;
	DECLARE vTotalConsumption DOUBLE;
	DECLARE vTotalPayable DOUBLE;
	DECLARE vFinished INT DEFAULT 0;
	DECLARE cur_liquidate CURSOR FOR SELECT me.id_meter,
						a.id_address,
						MIN(measurements.date_time) AS `initialMeasurement`,
						MAX(measurements.date_time) AS `finalMeasurement`,
						SUM(measurements.quantity_kw) AS `totalConsumption`,
						SUM(measurements.quantity_kw * r.value) AS `totalPayable`
					 FROM
						(SELECT m.id_meter,m.date_time,m.quantity_kw, m.id_bill
							FROM measurements m
							JOIN meters me
							ON m.id_meter = me.id_meter
							JOIN addresses AS a
							ON a.id_meter = m.id_meter
							WHERE a.id_user = pIdClient
							ORDER BY m.date_time) AS `measurements`
					 INNER JOIN meters me
					 ON measurements.id_meter = me.id_meter
					 INNER JOIN addresses a
					 ON a.id_meter = me.id_meter
					 INNER JOIN rates r
					 ON a.id_rate = r.id_rate
					 WHERE (measurements.id_bill IS NULL) AND (a.id_user = pIdClient)
					 GROUP BY me.id_meter;

	DECLARE CONTINUE HANDLER FOR NOT FOUND SET vFinished = 1;

	OPEN cur_liquidate;
	FETCH cur_liquidate INTO vIdMeter,vIdAddress,vInitialMeasurement,vFinalMeasurement,vTotalConsumption,vTotalPayable;

		WHILE (vFinished = 0) DO

			INSERT INTO bills (`id_address`,`id_meter`,`id_user`,`initial_measurement`,`final_measurement`,`total_consumption`,`total_payable`) VALUES
			(vIdAddress,vIdMeter,pIdClient,vInitialMeasurement,vFinalMeasurement,vTotalConsumption,vTotalPayable);

			FETCH cur_liquidate INTO vIdMeter,vIdAddress,vInitialMeasurement,vFinalMeasurement,vTotalConsumption,vTotalPayable;

		END WHILE;

	CLOSE cur_liquidate;
END
$$

CALL liquidate_client(2);
SELECT * FROM bills;


/* PUNTO 3 */

/* 3) Generar las estructuras necesarias para el cálculo de precio de cada medición y las
inserción de la misma. Se debe tener en cuenta que una modificación en la tarifa debe
modificar el precio de cada una de estas mediciones en la base de datos y generar una
factura de ajuste a la nueva medición de cada una de las mediciones involucradas con esta
tarifa. */

/*PRIMER INTENTO*/
DROP TRIGGER IF EXISTS TIB_add_calculate_price;
DELIMITER $$
CREATE TRIGGER TIB_add_calculate_price BEFORE INSERT ON measurements FOR EACH ROW
BEGIN
	DECLARE vIdRate INT;
	DECLARE vPriceMeasurement DOUBLE;

	SELECT r.id_rate INTO vIdRate
	FROM rates r
	INNER JOIN addresses a
	ON r.id_rate = a.id_rate
	INNER JOIN meters m
	ON a.id_meter = m.id_meter
	WHERE m.id_meter = new.id_meter;

	SELECT r.value * new.quantity_kw INTO vPriceMeasurement FROM rates r WHERE r.id_rate = vIdRate;

	SET new.id_rate = vIdRate, new.price_measurement = vPriceMeasurement;
END;
$$

/*Hecho por pablo*/
drop trigger if exists TIB_add_calculate_price2;
DELIMITER $$
CREATE TRIGGER TIB_add_calculate_price2 BEFORE INSERT ON measurement FOR EACH ROW
BEGIN
	DECLARE vLastDate DATETIME DEFAULT NULL;
    DECLARE vLastMeas FLOAT DEFAULT 0;
    /*Buscar la máxima fecha que tenga*/
    SELECT MAX(`date`) INTO vLastDate FROM measurements WHERE id_meter = new.id_meter AND `date` < new.date;
	IF (vLastDate IS NOT NULL) THEN
		SELECT kw INTO vLastMeas FROM measurements WHERE id_meter = new.id_meter and `date` = vLastDate;
		SET new.price = (new.kw - vLastMeas) * 5.0;
	END IF;
END;
$$

/*CORRECCION SEGUNDO INTENTO (Funciona)*/
DROP TRIGGER IF EXISTS TIB_add_calculate_price3;
DELIMITER $$
CREATE TRIGGER TIB_add_calculate_price3 BEFORE INSERT ON measurements FOR EACH ROW
BEGIN
	DECLARE vLastDate DATETIME DEFAULT NULL;
    DECLARE vKwLastMeas FLOAT DEFAULT NULL;
    DECLARE vValueRate FLOAT DEFAULT NULL;
    
    SELECT MAX(`date_time`) INTO vLastDate FROM measurements WHERE id_meter = new.id_meter and `date_time` <= new.date_time;
	SELECT r.`value` INTO vValueRate FROM rates r INNER JOIN addresses a ON a.id_rate = r.id_rate WHERE id_meter = new.id_meter LIMIT 1; 
     
    IF(vLastDate IS NOT NULL) THEN
		SELECT MAX(quantity_kw) INTO vKwLastMeas FROM measurements WHERE id_meter = new.id_meter AND `date_time` = vLastDate;
        SET new.price_measurement = (new.quantity_kw - vKwLastMeas) * vValueRate;
    ELSE 
		SET new.price_measurement = new.quantity_kw * vValueRate;
    END IF;
    
END;
$$

    SELECT MAX(`date_time`) FROM measurements WHERE id_meter = 1 and `date_time` <= new.date_time;
    SELECT MAX(`date_time`) FROM measurements WHERE id_meter = new.id_meter and `date_time` <= new.date_time;

INSERT INTO `measurements` (`id_meter`,`id_bill`,`date_time`,`quantity_kw`) VALUES
(3,NULL,NOW(),3);


SELECT * FROM rates;
insert into rates values (10,400,"C");



