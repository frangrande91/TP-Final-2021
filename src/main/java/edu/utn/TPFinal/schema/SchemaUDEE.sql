
CREATE DATABASE IF NOT EXISTS UDEE;
#drop database UDEE;
USE UDEE;

select count(*) from measurements where id_meter = 1;
select max(quantity_kw) from measurements where id_meter = 2;
select max(quantity_kw) from measurements where id_meter = 3; 

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
                                    type_user INT NOT NULL DEFAULT 1,
                                    CONSTRAINT pk_user PRIMARY KEY (id_user),
                                    CONSTRAINT unq_username UNIQUE (username),
									CONSTRAINT check_type_user check (type_user BETWEEN 0 AND 1)
);

/*
ALTER TABLE users CHANGE COLUMN type_user type_user INT NOT NULL DEFAULT 1;
ALTER TABLE users add constraint check_type_user check (type_user between 0 and 1);
*/

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
	expiration DATETIME DEFAULT NOW(),
	`date` DATETIME DEFAULT NOW(),
	payed BOOL DEFAULT FALSE,
	CONSTRAINT pk_bill PRIMARY KEY (id_bill),
	CONSTRAINT fk_bill_address FOREIGN KEY (id_address) REFERENCES addresses(id_address),
	CONSTRAINT fk_bil_meter FOREIGN KEY (id_meter) REFERENCES meters(id_meter),
	CONSTRAINT fk_bill_user FOREIGN KEY (id_user) REFERENCES users(id_user)
);



ALTER TABLE bills ADD COLUMN `date` DATETIME DEFAULT NOW();
#ALTER TABLE bills CHANGE COLUMN expiration expiration DATETIME DEFAULT DATE_ADD(NOW(),INTERVAL 15 DAY);
/*alter table bills add column payed bool default false;*/
#ALTER TABLE bills CHANGE COLUMN expiration expiration DATETIME DEFAULT DATE_ADD("2017-06-15", INTERVAL 10 DAY);


CREATE TABLE IF NOT EXISTS measurements(
	id_measurement INT NOT NULL AUTO_INCREMENT,
	id_meter INT NOT NULL,
	id_bill INT,
	`date` DATETIME,
	quantity_kw DOUBLE NOT NULL,
	price_measurement DOUBLE DEFAULT 0,
	CONSTRAINT pk_measurement PRIMARY KEY (id_measurement),
	CONSTRAINT fk_measurement_meter FOREIGN KEY (id_meter) REFERENCES meters(id_meter),
	CONSTRAINT fk_measurement_bill FOREIGN KEY (id_bill) REFERENCES bills(id_bill)
);

ALTER TABLE measurements CHANGE COLUMN `date` `date` DATETIME;


/* INSERTS */

INSERT INTO brands(`name`) VALUES
("Nike"),
("Adidas");

INSERT INTO models(id_brand,`name`) VALUES
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


SELECT * FROM addresses;
SELECT * FROM users;

INSERT INTO `measurements` (`id_meter`,`id_bill`,`date`,`quantity_kw`) VALUES
(8,NULL,NOW(),0),
(8,NULL,DATE_ADD(NOW(),INTERVAL 5 MINUTE),2),      
(8,NULL,DATE_ADD(NOW(),INTERVAL 10 MINUTE),5),     
(8,NULL,DATE_ADD(NOW(),INTERVAL 15 MINUTE),9),     
(8,NULL,DATE_ADD(NOW(),INTERVAL 20 MINUTE),15),    
(8,NULL,DATE_ADD(NOW(),INTERVAL 25 MINUTE),17),    
(8,NULL,DATE_ADD(NOW(),INTERVAL 30 MINUTE),18),
(8,NULL,DATE_ADD(NOW(),INTERVAL 35 MINUTE),24),
(9,NULL,NOW(),0),
(9,NULL,DATE_ADD(NOW(),INTERVAL 5 MINUTE),8),
(9,NULL,DATE_ADD(NOW(),INTERVAL 10 MINUTE),15),
(9,NULL,DATE_ADD(NOW(),INTERVAL 15 MINUTE),20),
(9,NULL,DATE_ADD(NOW(),INTERVAL 20 MINUTE),26),
(9,NULL,DATE_ADD(NOW(),INTERVAL 25 MINUTE),30),
(9,NULL,DATE_ADD(NOW(),INTERVAL 30 MINUTE),31),
(9,NULL,DATE_ADD(NOW(),INTERVAL 35 MINUTE),34),
(10,NULL,NOW(),0),
(10,NULL,DATE_ADD(NOW(),INTERVAL 5 MINUTE),8),
(10,NULL,DATE_ADD(NOW(),INTERVAL 10 MINUTE),15),
(10,NULL,DATE_ADD(NOW(),INTERVAL 15 MINUTE),24),
(10,NULL,DATE_ADD(NOW(),INTERVAL 20 MINUTE),30),
(10,NULL,DATE_ADD(NOW(),INTERVAL 25 MINUTE),35),
(10,NULL,DATE_ADD(NOW(),INTERVAL 30 MINUTE),38),
(10,NULL,DATE_ADD(NOW(),INTERVAL 35 MINUTE),40);


ALTER TABLE measurements AUTO_INCREMENT = 1;
ALTER TABLE bills AUTO_INCREMENT = 1;


SELECT * FROM addresses;
SELECT * FROM bills;
SELECT * FROM brands;
SELECT * FROM measurements;
SELECT * FROM meters;
SELECT * FROM models;
SELECT * FROM rates;
SELECT * FROM users;

select max(quantity_kw) from measurements where id_meter = 1;

select * from measurements where id_measurement = 103656;
select count(*) from measurements where id_measurement = 103656;

/*PUNTO 1
1) Generar las estructuras necesarias para dar soporte a 4 sistemas diferentes :
a) BACKOFFICE, que permitirá el manejo de clientes, medidores y tarifas.
b) CLIENTES, que permitirá consultas de mediciones y facturación.
c) MEDIDORES, que será el sistema que enviará la información de
mediciones a la base de datos.
d) FACTURACIÓN , proceso automático de facturación.
*/

/***********************BACKOFFICE**************************************/
CREATE USER 'backoffice' IDENTIFIED BY 'backoffice123';

GRANT SELECT,INSERT,UPDATE,DELETE ON udee.rates TO 'backoffice';
GRANT SELECT,INSERT,UPDATE,DELETE ON udee.meters TO 'backoffice';
GRANT SELECT,INSERT,UPDATE,DELETE ON udee.users TO 'backoffice';
GRANT SELECT,INSERT,UPDATE,DELETE ON udee.addresses TO 'backoffice';
GRANT SELECT,INSERT,UPDATE,DELETE ON udee.models TO 'backoffice';
GRANT SELECT,INSERT,UPDATE,DELETE ON udee.brands TO 'backoffice';
GRANT SELECT,INSERT,UPDATE,DELETE ON udee.rates TO 'backoffice';

/***********************CLIENTS**************************************/
CREATE USER 'clients' IDENTIFIED BY 'clients123'; 

GRANT SELECT ON udee.measurements TO 'clients';
GRANT SELECT ON udee.bills TO 'clients';

/***********************METERS**************************************/
CREATE USER 'meters' IDENTIFIED BY 'meters123'; 

GRANT INSERT ON udee.measurements TO 'meters';

/***********************BILLING**************************************/
CREATE USER 'billing' IDENTIFIED BY 'billing123'; 

GRANT EXECUTE ON PROCEDURE p_all_clients_billing TO billing;
GRANT EXECUTE ON PROCEDURE p_billing_update_rate TO billing;


/*PUNTO 2
La facturación se realizará por un proceso automático en la base de datos. Se
debe programar este proceso para el primer día de cada mes y debe generar una
factura por medidor y debe tomar en cuenta todas las mediciones no facturadas
para cada uno de los medidores, sin tener en cuenta su fecha. La fecha de vencimiento de
esta factura será estipulado a 15 días.
- una factura por medidor
- todas las mediciones no facturadas*/

    select * from measurements where id_meter = 1 and id_measurement >= 49995;

/*STORE PROCEDURE PARA FACTURACION*/
DROP PROCEDURE IF EXISTS  p_all_clients_billing;
DELIMITER $$
CREATE PROCEDURE p_all_clients_billing()
BEGIN
    DECLARE vIdClient INT;
    DECLARE vIdMeter INT;
    DECLARE vIdAddress INT;
    DECLARE vIdBill INT;
    DECLARE vInitialMeasurement DATETIME;
    DECLARE vFinalMeasurement DATETIME;
    DECLARE vTotalConsumption DOUBLE;
    DECLARE vTotalPayable DOUBLE;
    DECLARE vIdBillCreated INT DEFAULT NULL;
    DECLARE vFinished INT DEFAULT 0;
    

    
    #Declaramos un cursor agrupando todas las mediciones de cada medidor que no han sido facturadas
    DECLARE cur_liquidate CURSOR FOR SELECT u.id_user,
											me.id_meter,
                                            a.id_address,
                                            measurements.id_bill,
                                            MIN(measurements.date) AS `initialMeasurement`,
                                            MAX(measurements.date) AS `finalMeasurement`,
                                            MAX(measurements.quantity_kw) - MIN(measurements.quantity_kw) AS `totalConsumption`,
                                            ( MAX(measurements.quantity_kw) - MIN(measurements.quantity_kw) ) * r.value AS `totalPayable`
                                     FROM
					     measurements  
					     INNER JOIN meters me
							ON measurements.id_meter = me.id_meter 
					     INNER JOIN addresses a
							ON a.id_meter = me.id_meter
					     INNER JOIN users u
							ON u.id_user = a.id_user
					     INNER JOIN rates r
							ON a.id_rate = r.id_rate
                                     GROUP BY u.id_user, me.id_meter, a.id_address, measurements.id_bill
				     HAVING measurements.id_bill IS NULL;

    DECLARE CONTINUE HANDLER FOR NOT FOUND SET vFinished = 1;

    OPEN cur_liquidate;
    FETCH cur_liquidate INTO vIdClient, vIdMeter,vIdAddress,vIdBill,vInitialMeasurement,vFinalMeasurement,vTotalConsumption,vTotalPayable;

    WHILE (vFinished = 0) DO
		
        #Los proximos dos eventos tienen que pasar los dos juntos si o si, por lo tanto usamos transacciones
        START TRANSACTION;
        
			#Creamos la factura
			INSERT INTO bills (`id_address`,`id_meter`,`id_user`,`initial_measurement`,`final_measurement`,`total_consumption`,`total_payable`) VALUES
				(vIdAddress,vIdMeter,vIdClient,vInitialMeasurement,vFinalMeasurement,vTotalConsumption,vTotalPayable);
			
            #Tomamos el valor de la factura para luego registrar las mediciones como facturadas
            SET vIdBillCreated = LAST_INSERT_ID();
			
            #Actualizamos todas las mediciones como facturadas de dicha factura
            UPDATE measurements SET id_bill = vIdBillCreated WHERE id_meter = vIdMeter AND `date` BETWEEN vInitialMeasurement AND vFinalMeasurement;
            
        COMMIT;
        
		FETCH cur_liquidate INTO vIdClient, vIdMeter,vIdAddress,vIdBill,vInitialMeasurement,vFinalMeasurement,vTotalConsumption,vTotalPayable;
	END WHILE;

    CLOSE cur_liquidate;
END
$$


CALL p_all_clients_billing();
SELECT * FROM bills;
SELECT * FROM measurements;
DELETE FROM measurements;
DELETE FROM bills;

/*PROCESO AUTOMATICO FACTURACION*/

SET GLOBAL event_scheduler = ON;

DROP EVENT e_all_clients_billing
CREATE EVENT e_liquidate_clients 
ON SCHEDULE EVERY 1 MONTH STARTS '2021-07-01 00:00:00'
DO CALL p_all_clients_billing();

ALTER EVENT e_all_clients_billing DISABLE;

SHOW EVENTS;


/*******************************************PUNTO 3*****************************************************/

/* Generar las estructuras necesarias para el cálculo de precio de cada medición y las
inserción de la misma. Se debe tener en cuenta que una modificación en la tarifa debe
modificar el precio de cada una de estas mediciones en la base de datos y generar una
factura de ajuste a la nueva medición de cada una de las mediciones involucradas con esta
tarifa.*/


/******************************TRIGGER FOR PRICE MEASUREMENTS*********************************************/

DROP TRIGGER IF EXISTS TIB_add_calculate_price3;
DELIMITER $$
CREATE TRIGGER TIB_add_calculate_price BEFORE INSERT ON measurements FOR EACH ROW
BEGIN
    DECLARE vLastDate DATETIME DEFAULT NULL;
    DECLARE vKwLastMeas FLOAT DEFAULT NULL;
    DECLARE vValueRate FLOAT DEFAULT NULL;

    SELECT MAX(`date`) INTO vLastDate FROM measurements WHERE id_meter = new.id_meter AND `date` <= new.date;
    SELECT r.`value` INTO vValueRate FROM rates r INNER JOIN addresses a ON a.id_rate = r.id_rate WHERE id_meter = new.id_meter LIMIT 1;

    IF(vLastDate IS NOT NULL) THEN
        SELECT MAX(quantity_kw) INTO vKwLastMeas FROM measurements WHERE id_meter = new.id_meter AND `date` = vLastDate;
        SET new.price_measurement = (new.quantity_kw - vKwLastMeas) * vValueRate;
    ELSE
        SET new.price_measurement = new.quantity_kw * vValueRate;
    END IF;
END;
$$

/******************************TRIGGER FOR UPDATE PRICE MEASUREMENTS IF CHANGE A RATE*********************************************/

/*TRIGGER BEFORE UPDATE RATES*/
DROP TRIGGER IF EXISTS TUA_update_price_measurements;
DELIMITER $$
CREATE TRIGGER TUA_update_price_measurements AFTER UPDATE ON rates FOR EACH ROW
BEGIN
	
	/*update measurements set `price_measurement` = (`price_measurement` / old.value) * new.value where id_meter in (SELECT m.id_meter 
														FROM rates r
														INNER JOIN addresses a
														ON a.id_rate = r.id_rate
														INNER JOIN meters m
														ON a.id_meter = m.id_meter				
														WHERE r.id_rate = new.rate);*/

		#Modificamos el precio de las mediciones de la tarifa que ha sido modificada
		UPDATE measurements SET `price_measurement` = (`price_measurement` / old.value) * new.value WHERE id_measurement IN (SELECT me.id_measurement
																FROM measurements me
																INNER JOIN meters m
																ON m.id_meter = me.id_meter
																INNER JOIN addresses a
																ON a.id_meter = me.id_meter
																INNER JOIN rates r
																ON a.id_rate = r.id_rate
																WHERE r.id_rate = new.id_rate);
						
		#Se crean las nuevas facturas de ajuste que formen parte de la tarifa que ha sido modificada										
		CALL p_billing_update_rate(new.id_rate);
END;
$$

DROP PROCEDURE IF EXISTS p_billing_update_rate;
DELIMITER $$
CREATE PROCEDURE  p_billing_update_rate(IN pIdRate INT)
BEGIN
	DECLARE vNewTotalPayable DOUBLE;
	DECLARE vIdBill INT;
	DECLARE vIdAddress INT;
	DECLARE vIdMeter INT;
	DECLARE vIdClient INT;
	DECLARE vInitialMeasurement DATETIME;
	DECLARE vFinalMeasurement DATETIME;
	DECLARE vTotalConsumption DOUBLE;
	DECLARE vTotalPayable DOUBLE;
	DECLARE vFinished INT DEFAULT 0;
	
    #Declaramos uun cursor que contenga todas las facturas de una determinada tarifa
    DECLARE cur_liquidate CURSOR FOR SELECT b.`id_bill`,b.`id_address`,b.`id_meter`,b.`id_user`,b.`initial_measurement`,b.`final_measurement`,b.`total_consumption`,b.`total_payable`
						FROM bills b
						INNER JOIN addresses a
						ON a.id_address = b.id_address
						WHERE a.id_rate = pIdRate;
						
    DECLARE CONTINUE HANDLER FOR NOT FOUND SET vFinished = 1;

    OPEN cur_liquidate;
    FETCH cur_liquidate INTO vIdBill,vIdAddress, vIdMeter, vIdClient,vInitialMeasurement,vFinalMeasurement,vTotalConsumption,vTotalPayable;

    WHILE (vFinished = 0) DO
		
        #Calculamos el total de la nueva facturq, que seria la diferencia entre el precio viejo y el nuevo de todas las mediciones en conjunto
	    SET vNewTotalPayable = (SELECT SUM(price_measurement) FROM measurements WHERE id_bill = vIdBill) - vTotalPayable;
		
        #Se inserta la nueva factura de ajuste
		INSERT INTO bills (`id_address`,`id_meter`,`id_user`,`initial_measurement`,`final_measurement`,`total_consumption`,`total_payable`) VALUES
		(vIdAddress,vIdMeter,vIdClient,vInitialMeasurement,vFinalMeasurement,vTotalConsumption,vNewTotalPayable);
	   
	    FETCH cur_liquidate INTO vIdBill,vIdAddress, vIdMeter, vIdClient,vInitialMeasurement,vFinalMeasurement,vTotalConsumption,vTotalPayable;
        
	END WHILE;

    CLOSE cur_liquidate;
	
END 
$$


/*******************************************PUNTO 4*****************************************************/

/*
Generar las estructuras necesarias para dar soporte a las consultas de mediciones
por fecha y por usuario , debido a que tenemos restricción de que estas no pueden demorar
más de dos segundos y tenemos previsto que tendremos 500.000.000 de mediciones en el
sistema en el mediano plazo. Este reporte incluirá :
● Cliente
● Medidor
● Fecha medición
● Medicion
● Consumo Kwh
● Consumo precio
*/

#CREAMOS LOS INDICES
DROP INDEX order_dates ON measurements;
CREATE INDEX order_dates ON measurements(`date`) USING BTREE;


#PROBAMOS SI FUNCIONAN LOS INDICES 
EXPLAIN SELECT * FROM measurements WHERE `date` BETWEEN "2021-06-19 02:47:01" AND "2021-06-19 02:48:01";
EXPLAIN SELECT u.name, u.last_name, a.id_meter, me.date, me.id_measurement, me.quantity_kw, me.price_measurement 
		FROM users u
		INNER JOIN addresses a
			ON a.id_user = u.id_user
		INNER JOIN measurements me
			ON me.id_meter = a.id_meter
		WHERE ( u.id_user = 2 ) AND ( me.date between "2021-06-19 02:47:01" AND "2021-06-19 02:48:01");


#CREAMOS PROCEDURE PARA REALIZAR LA QUERY DENTRO DE EL Y SIMPLIFICAR
DROP PROCEDURE IF EXISTS p_consult_measurements;
DELIMITER $$
CREATE PROCEDURE p_consult_measurements(IN pIdUser INT, IN pFrom DATETIME, IN pTo DATETIME)
BEGIN
	IF(pFrom > pTo) THEN
		SIGNAL SQLSTATE '10001' 
		SET MESSAGE_TEXT = 'The TO must be more big than FROM', 
		MYSQL_ERRNO = 2.2;
    ELSE
		SELECT 
			u.name, 
			u.last_name, 
			a.id_meter, 
			me.date, 
			me.id_measurement,
			me.quantity_kw, 
			me.price_measurement
		FROM users u
		INNER JOIN addresses a
			ON a.id_user = u.id_user
		INNER JOIN measurements me
			ON me.id_meter = a.id_meter
		WHERE ( u.id_user = pIdUser ) AND ( me.date BETWEEN pFrom AND pTo);
	END IF;
END;

#LLAMADA AL PROCEDURE
CALL p_consult_measurements(2,"2021-02-28 02:47:01","2020-06-19 02:48:01");

/*EJERCIO 5 PROG AVANZADA I*/
/*
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
                   ON m.id_meter = me.id_meter AND (me.date BETWEEN '2015-08-01' AND '2021-12-10' )
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
                   ON m.id_meter = me.id_meter AND (me.date BETWEEN '2015-08-01' AND '2021-12-10' )
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
             


SELECT consum.id_user AS id,
       consum.name,
       consum.last_name,
       consum.username,
       ( SUM(consum.max) - SUM(consum.min) ) AS consumption
FROM
		( SELECT m.id_meter, u.id_user, u.name, u.last_name, u.username, MAX(me.quantity_kw) AS max, MIN(me.quantity_kw) AS min, me.date
			 FROM users AS u
					  JOIN addresses AS a
						   ON u.id_user = a.id_user
					  JOIN meters AS m
						   ON a.id_meter = m.id_meter
					  JOIN measurements AS me
						   ON m.id_meter = me.id_meter
		GROUP BY m.id_meter, u.id_user, u.name, u.last_name, u.username
        HAVING me.date BETWEEN '2015-08-01' AND '2021-12-10' ) consum
GROUP BY id, consum.name, consum.last_name, consum.username ORDER BY consumption DESC LIMIT 10;


 SELECT consum.id_user AS id, consum.name,consum.last_name, consum.username, ( SUM(consum.max) - SUM(consum.min) ) AS consumption
FROM
		( SELECT m.id_meter, u.id_user, u.name, u.last_name, u.username, MAX(me.quantity_kw) AS max, MIN(me.quantity_kw) AS min, me.date
			 FROM users AS u
					  JOIN addresses AS a
						   ON u.id_user = a.id_user
					  JOIN meters AS m
						   ON a.id_meter = m.id_meter
					  JOIN measurements AS me
						   ON m.id_meter = me.id_meter
		GROUP BY m.id_meter, u.id_user, u.name, u.last_name, u.username
        HAVING me.date BETWEEN "2021-05-05" AND "2021-12-31" ) consum
GROUP BY id, consum.name, consum.last_name, consum.username ORDER BY consumption DESC LIMIT 10;
*/