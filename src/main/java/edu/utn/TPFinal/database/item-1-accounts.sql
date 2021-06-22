/*******************************************PUNTO 1*****************************************************/
/*
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

/***********************CLIENTS*****************************************/
CREATE USER 'clients' IDENTIFIED BY 'clients123'; 

GRANT SELECT ON udee.measurements TO 'clients';
GRANT SELECT ON udee.bills TO 'clients';

/***********************METERS******************************************/
CREATE USER 'meters' IDENTIFIED BY 'meters123'; 

GRANT INSERT ON udee.measurements TO 'meters';

/***********************BILLING*****************************************/
CREATE USER 'billing' IDENTIFIED BY 'billing123'; 

GRANT EXECUTE ON PROCEDURE p_all_clients_billing TO billing;
GRANT EXECUTE ON PROCEDURE p_billing_update_rate TO billing;