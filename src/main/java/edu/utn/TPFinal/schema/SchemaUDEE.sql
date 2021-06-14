
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
<<<<<<< HEAD
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
=======
	id_bill INT NOT NULL AUTO_INCREMENT,
	id_address INT NOT NULL,
	id_meter INT NOT NULL,
	id_user INT NOT NULL,
	initial_measurement DATETIME NOT NULL,
	final_measurement DATETIME NOT NULL,
	total_consumption DOUBLE NOT NULL,
	total_payable DOUBLE,
	expiration DATETIME default now(),
    `date` datetime default now(),
    payed bool default false,
	CONSTRAINT pk_bill PRIMARY KEY (id_bill),
	CONSTRAINT fk_bill_address FOREIGN KEY (id_address) REFERENCES addresses(id_address),
	CONSTRAINT fk_bil_meter FOREIGN KEY (id_meter) REFERENCES meters(id_meter),
	CONSTRAINT fk_bill_user FOREIGN KEY (id_user) REFERENCES users(id_user)
>>>>>>> 86c40d04f66b5b0fd6a04363dfca3de74a87cf8e
);



alter table bills add column `date` datetime default now();
ALTER TABLE bills CHANGE COLUMN expiration expiration DATETIME DEFAULT DATE_ADD(NOW(),INTERVAL 15 DAY)
/*alter table bills add column payed bool default false;*/
alter table bills change column expiration expiration datetime default DATE_ADD("2017-06-15", INTERVAL 10 DAY);


CREATE TABLE IF NOT EXISTS measurements(
<<<<<<< HEAD
                                           id_measurement INT NOT NULL AUTO_INCREMENT,
                                           id_meter INT NOT NULL,
                                           id_bill INT,
                                           date_time DATETIME,
                                           quantity_kw DOUBLE NOT NULL,
                                           price_measurement DOUBLE DEFAULT 0,
                                           CONSTRAINT pk_measurement PRIMARY KEY (id_measurement),
                                           CONSTRAINT fk_measurement_meter FOREIGN KEY (id_meter) REFERENCES meters(id_meter),
                                           CONSTRAINT fk_measurement_bill FOREIGN KEY (id_bill) REFERENCES bills(id_bill)
=======
	id_measurement INT NOT NULL AUTO_INCREMENT,
	id_meter INT NOT NULL,
	id_bill INT,
    `date` DATETIME,
	quantity_kw DOUBLE NOT NULL,
    price_measurement DOUBLE DEFAULT 0,
	CONSTRAINT pk_measurement PRIMARY KEY (id_measurement),
	CONSTRAINT fk_measurement_meter FOREIGN KEY (id_meter) REFERENCES meters(id_meter),
	CONSTRAINT fk_measurement_bill FOREIGN KEY (id_bill) REFERENCES bills(id_bill)
>>>>>>> 86c40d04f66b5b0fd6a04363dfca3de74a87cf8e
);

/*ALTER TABLE measurements CHANGE COLUMN date_time `date` DATETIME;
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
(1,"123456789asdsad10sdda","123456"),
(1,"12345678sadsad9asdsda","1234567"),
(2,"1234568101adsa1sdasda","1234568")
;

INSERT INTO users(`name`, last_name, username, `password`, type_user) VALUES
("Pepe", "Grillo", "pepegrilloloa", "123", 1),
("Pepe2", "Grillo2", "pepegrillo23a", "1234", 1),
("Pepe3", "Grillo3", "pepegrillo32a", "1235", 1)
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


select * from addresses;
select * from users;

INSERT INTO `measurements` (`id_meter`,`id_bill`,`date`,`quantity_kw`) VALUES
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

/*Datos
-FROM
-TO*/

select * from measurements where `date` between "2021-07-30" and "2021-08-31" and id_meter = 1;
<<<<<<< HEAD
select * from measurements where `date` < "2021-07-30" and id_meter = 1 order by `date` desc limit 0,1;


SELECT * FROM addresses;
SELECT * FROM bills;
SELECT * FROM brands;
SELECT * FROM measurements where id_meter = 3;
SELECT * FROM meters;
SELECT * FROM models;
SELECT * FROM rates;
SELECT * FROM users;

select *
from users u
         inner join addresses a
                    on u.id_user = a.id_user
         inner join meters me
                    on me.id_meter = a.id_meter
         inner join measurements m
                    on m.id_meter = me.id_meter and m.date between "2021-04-01" and "2021-06-05"
group by me.id_meter
order by quantity_kw desc limit 10;

=======
select * from measurements where `date` < "2021-07-30" and id_meter = 1 order by `date` desc limit 0,1;  


 SELECT * FROM addresses;
 SELECT * FROM bills;
 SELECT * FROM brands;
 SELECT * FROM measurements where id_meter = 3;
 SELECT * FROM meters;
 SELECT * FROM models;
 SELECT * FROM rates;
 SELECT * FROM users;


 select *
 from users u
 inner join addresses a
 on u.id_user = a.id_user
 inner join meters me
 on me.id_meter = a.id_meter
 inner join measurements m
 on m.id_meter = me.id_meter and m.date between "2021-04-01" and "2021-06-05"
 group by me.id_meter
 order by quantity_kw desc limit 10;
 
>>>>>>> 86c40d04f66b5b0fd6a04363dfca3de74a87cf8e

/*PUNTO 2
La facturación se realizará por un proceso automático en la base de datos. Se
debe programar este proceso para el primer día de cada mes y debe generar una
factura por medidor y debe tomar en cuenta todas las mediciones no facturadas
para cada uno de los medidores, sin tener en cuenta su fecha. La fecha de vencimiento de
esta factura será estipulado a 15 días.
- una factura por medidor
- todas las mediciones no facturadas*/

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
CREATE TRIGGER TIB_add_calculate_price2 BEFORE INSERT ON measurements FOR EACH ROW
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



/*QUERY FOR EXCERCISE 5*/
SELECT max_consumption.id_user AS id,
       max_consumption.name,
       max_consumption.last_name,
       max_consumption.username,
       (SUM(max_consumption.consumption) - SUM(IFNULL(min_consumption.minimo,0))) AS consumption
FROM

    (SELECT m.id_meter, u.id_user, u.name, u.last_name, u.username, MAX(me.quantity_kw) AS consumption
     FROM users AS u
              JOIN addresses AS a
                   ON u.id_user = a.id_user
              JOIN meters AS m
                   ON a.id_meter = m.id_meter
              JOIN measurements AS me
                   ON m.id_meter = me.id_meter AND (me.date_time BETWEEN '2015-08-01' AND '2020-12-10' )
     GROUP BY m.id_meter, u.id_user, u.name, u.last_name, u.username) AS max_consumption
        LEFT JOIN
    (SELECT m.id_meter,IFNULL(MAX(me.quantity_kw), 0) AS minimo
     FROM users AS u
              JOIN addresses AS a
                   ON u.id_user = a.id_user
              JOIN meters AS m
                   ON a.id_meter = m.id_meter
              JOIN measurements AS me
                   ON m.id_meter = me.id_meter AND (me.date_time < '2015-07-01')
     GROUP BY m.id_meter) AS min_consumption
    ON max_consumption.id_meter = min_consumption.id_meter
        JOIN meters m
             ON max_consumption.id_meter = m.id_meter
/*
	(SELECT m.id_meter, u.id_user, u.name, u.last_name, u.username, MAX(me.quantity_kw) AS consumption
	FROM users AS u
	JOIN addresses AS a
	ON u.id_user = a.id_user
	JOIN meters AS m
	ON a.id_meter = m.id_meter
	JOIN measurements AS me
	ON m.id_meter = me.id_meter AND (me.date_time BETWEEN '2015-08-01' AND '2020-12-10' )
	GROUP BY m.id_meter, u.id_user, u.name, u.last_name, u.username) AS max_consumption
LEFT JOIN
	(SELECT m.id_meter,IFNULL(MAX(me.quantity_kw), 0) AS minimo
	FROM users AS u
	JOIN addresses AS a
	ON u.id_user = a.id_user
	JOIN meters AS m
	ON a.id_meter = m.id_meter
	JOIN measurements AS me
	ON m.id_meter = me.id_meter AND (me.date < '2015-07-01')
	GROUP BY m.id_meter) AS min_consumption
ON max_consumption.id_meter = min_consumption.id_meter
JOIN meters m
ON max_consumption.id_meter = m.id_meter
GROUP BY max_consumption.id_user
ORDER BY consumption DESC LIMIT 10

 */
;