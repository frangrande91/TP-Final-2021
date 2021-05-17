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


CREATE TABLE IF NOT EXISTS addresses(
	id_address INT NOT NULL AUTO_INCREMENT,
	id_meter INT NOT NULL,
	id_user INT NOT NULL,
	address VARCHAR(100) NOT NULL,
	CONSTRAINT pk_address PRIMARY KEY (id_address),
	CONSTRAINT fk_address_meter FOREIGN KEY (id_meter) REFERENCES meters(id_meter),
    CONSTRAINT fk_address_user FOREIGN KEY (id_user) REFERENCES users(id_user)
);


CREATE TABLE IF NOT EXISTS rates(
	id_rate INT NOT NULL AUTO_INCREMENT,
	`value` DOUBLE NOT NULL,
	type_rate VARCHAR(50) NOT NULL,
	CONSTRAINT pk_rate PRIMARY KEY (id_rate)
);


CREATE TABLE IF NOT EXISTS bills(
	id_bill INT NOT NULL AUTO_INCREMENT,
	id_address INT NOT NULL,
	id_meter INT NOT NULL,
	id_rate INT NULL,
	id_user INT NOT NULL,
	initial_measurement DATETIME NOT NULL,
	final_measurement DATETIME,
	total_consumption DOUBLE NOT NULL,
	total_payable DOUBLE,
	CONSTRAINT pk_bill PRIMARY KEY (id_bill),
	CONSTRAINT fk_bill_address FOREIGN KEY (id_address) REFERENCES addresses(id_address),
	CONSTRAINT fk_bil_meter FOREIGN KEY (id_meter) REFERENCES meters(id_meter),
	CONSTRAINT fk_bill_rate FOREIGN KEY (id_rate) REFERENCES rates(id_rate),
	CONSTRAINT fk_bill_user FOREIGN KEY (id_user) REFERENCES users(id_user)
);


CREATE TABLE IF NOT EXISTS measurements(
	id_measurement INT NOT NULL AUTO_INCREMENT,
	id_meter INT NOT NULL,
	id_bill INT NOT NULL,
    date_time DATETIME,
	quantity_kw DOUBLE NOT NULL,
	CONSTRAINT pk_measurement PRIMARY KEY (id_measurement),
	CONSTRAINT fk_measurement_meter FOREIGN KEY (id_meter) REFERENCES meters(id_meter),
	CONSTRAINT fk_measurement_bill FOREIGN KEY (id_bill) REFERENCES bills(id_bill)
);
