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

	#Seleccionamos la fecha de la ultima medicion anterior a la insertada reciente, es menor o igual en caso de que se inserte una medicion justo al mismo tiempo que otra
    SELECT MAX(`date`) INTO vLastDate FROM measurements WHERE id_meter = new.id_meter AND `date` <= new.date;
    
    #Saco el valor de la tarifa para luego calcular el precio de la medicion
    SELECT r.`value` INTO vValueRate FROM rates r INNER JOIN addresses a ON a.id_rate = r.id_rate WHERE id_meter = new.id_meter LIMIT 1;

    IF(vLastDate IS NOT NULL) THEN
        #Calculo los kw de esta ultima medicion
        SELECT MAX(quantity_kw) INTO vKwLastMeas FROM measurements WHERE id_meter = new.id_meter AND `date` = vLastDate;
        
        #Seteo el valor
        SET new.price_measurement = (new.quantity_kw - vKwLastMeas) * vValueRate;
    ELSE
		#Aca entra solo la primera vez, solo para la primera medicion del medidor
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
	
    #Modificamos el precio de las mediciones de la tarifa que ha sido modificada
	IF(old.`value` <> new.`value`) THEN
		UPDATE measurements SET `price_measurement` = (`price_measurement` / old.value) * new.value WHERE id_meter in (SELECT m.id_meter 
															FROM rates r
															INNER JOIN addresses a
															ON a.id_rate = r.id_rate
															INNER JOIN meters m
															ON a.id_meter = m.id_meter				
															WHERE r.id_rate = old.id_rate);
			
					#Se crean las nuevas facturas de ajuste que formen parte de la tarifa que ha sido modificada
					CALL p_billing_update_rate(new.id_rate);
		END IF;
        
        #Otra forma
			/*UPDATE measurements SET `price_measurement` = (`price_measurement` / old.value) * new.value WHERE id_measurement IN (SELECT me.id_measurement
																	FROM measurements me
																	INNER JOIN meters m
																	ON m.id_meter = me.id_meter
																	INNER JOIN addresses a
																	ON a.id_meter = me.id_meter
																	INNER JOIN rates r
																	ON a.id_rate = r.id_rate
																	WHERE r.id_rate = old.id_rate);*/
							
										

END;
$$
SELECT * FROM rates;

UPDATE rates SET `value` = 400 where id_rate = 1;  

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
