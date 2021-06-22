CREATE DATABASE IF NOT EXISTS UDEE;
#drop database UDEE;
USE UDEE;

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


#ALTER TABLE bills CHANGE COLUMN expiration expiration DATETIME DEFAULT DATE_ADD(NOW(),INTERVAL 15 DAY);
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
SELECT * FROM measurements where id_measurement = 65002;
SELECT * FROM meters;
SELECT * FROM models;
SELECT * FROM rates;
SELECT * FROM users;

select NOW();

show processlist;

select measuremen0_.id_measurement as id_measu1_3_, measuremen0_.id_bill as id_bill5_3_, measuremen0_.date as date2_3_, measuremen0_.id_meter as id_meter6_3_, measuremen0_.price_measurement as price_me3_3_, measuremen0_.quantity_kw as quantity4_3_ 
from measurements measuremen0_ where measuremen0_.id_meter=3 and (measuremen0_.date between "2021-06-19 02:48:20" and "2021-06-19 02:48:26")

