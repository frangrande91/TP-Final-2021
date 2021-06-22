/*******************************************PUNTO 2*****************************************************/
/*
La facturación se realizará por un proceso automático en la base de datos. Se
debe programar este proceso para el primer día de cada mes y debe generar una
factura por medidor y debe tomar en cuenta todas las mediciones no facturadas
para cada uno de los medidores, sin tener en cuenta su fecha. La fecha de vencimiento de
esta factura será estipulado a 15 días.
- una factura por medidor
- todas las mediciones no facturadas
*/

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

#LLAMO AL PROCEDURE PARA FACTURAR
CALL p_all_clients_billing();

select count(*) from measurements where id_bill is not null;

#ME FIJO QUE SE CARGE LA FACTURA
SELECT * FROM bills;
describe bills;

#ME FIJO QUE LAS MEDICIONES SE MARQUEN COMO FACTURADAS
SELECT * FROM measurements;


/*PROCESO AUTOMATICO FACTURACION*/

SET GLOBAL event_scheduler = ON;

DROP EVENT e_all_clients_billing
CREATE EVENT e_liquidate_clients 
ON SCHEDULE EVERY 1 MONTH STARTS '2021-07-01 00:00:00'
DO CALL p_all_clients_billing();

ALTER EVENT e_all_clients_billing DISABLE;

SHOW EVENTS;