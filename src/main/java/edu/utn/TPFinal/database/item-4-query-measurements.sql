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
    DECLARE vCount INT DEFAULT 0;
    SELECT COUNT(*) INTO vCount FROM users WHERE id_user = pIdUser;
    IF(vCount = 1) THEN
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
    ELSE
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'User not exists',
        MYSQL_ERRNO = 2.3;
    END IF;
END;

#LLAMADA AL PROCEDURE
CALL p_consult_measurements(2,"2021-02-28 02:47:01","2020-06-19 02:48:01");
