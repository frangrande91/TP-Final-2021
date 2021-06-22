/******************************************* NoSQL *****************************************************/
/*
Como PLAN B , generar una estructura de base de datos NoSQL de su preferencia
para dar soporte al problema planteado
 */
/******************************************ESTRUCTURA**************************************************/

/*A partir del usuario puedo traer muchos datos relacionados con el , busco tanto addresses como meters filtrados por users*/
udee.users = collection(){
    id_user : int,
    name : string,
    last_name : string,
    username : string,
    password : string,
    type_user : int
    addresses = Array(Address)
}
	 Address = Object() {
		id_address : int,
		address : string,
		meter : Object(Meter),
		rate : Object(Rate),
	}
		Meter = Objetc(){
			id_meter : int,
			serial_number : string,
			password : string,
			model : Object(Model),
            bills : Array(Bill)
		}
			Model = Object(){
				id_model : int,
				name : string,
				brand : Object(Brand)
			}
				Brand = Object(){
					id_brand : int,
					name : string
				}
		     Bill = Object(){
                    id_bill : int,
                    initial_measurement : datetime,
                    final_measurement : datetime,
                    total_consumption : double,
                    total_payable : double,
                    date : datetime,
                    expiration : datetime
               }

		Rate = Object() {
			id_rate : int,
			value : double,
			type_rate : string
		}

/*Coleccion de rates. Para obtener los tipos de rates que hay*/
udee.rates = collection(){
	id_rate : int,
	value : double,
	type_rate : string
}

/*Coleccion de bills que sirven para traer facturas por usuarios y/o address*/
udee.bills = collection(){
				id_bill : int,
				initial_measurement : datetime,
				final_measurement : datetime,
				total_consumption : double,
				total_payable : double,
				date : datetime,
				expiration : datetime,
                payed: boolean
                user : Object(User)
			}
	User(Object) = {
		id_user : int,
		name : string,
		last_name : string,
		username : string,
		password : string,
		type_user : int,
		addresses = Array(Address)
    }
			Address = Object() {
			id_address : int,
			address : string,
			rate : Object(Rate),
		}
			Rate = Object() {
				id_rate : int,
				value : double,
				type_rate : string
			}

/*Coleccion de mediciones. Manera perfomante tratada como si fuera SQL por la gran cantidad de registros. Mediciones por usuario o address*/
udee.measurements = collection() {
    id_measurement : int,
    date : datetime,
    quantity_kw : double,
    price_measurement : double,
    id_address: int(user)
}

/*Se podría crear un indice*/
INDICE => address, date



/*Coleccion de address para */
/*udee.addresses = collection(){
		id_address : int,
		address : string,
		meter : Object(Meter)
	}
		Meter = Objetc(){
			id_meter : int,
			serial_number : string,
			password : string
		}*/
        
/*otra forma menos performante para traer las mediciones*/
/*udee.addresses = collection(){
    id_address : int,
    address : string,
    measurements : collection(Measurement)
}
	Measurement = Object(){
		id_measurement : int,
		date : datetime,
		quantity_kw : double,
		price_measurement : double
	}*/
        

/*******************COMANDOS PARA LA INSERCION DE DATOS************************/
/*Seleccionar base de datos*/
use udee;

/*Buscar una direccion en los usuarios*/
db.users.find( {'addresses.id_address' : "2" } );

/*Hacer proyeccion para traer solo el address*/
db.users.find( {'addresses.id_address' : "2" },{addresses: 1 })

/*Insertar usuarios*/
db.users.insert(
[
	{ "id": "1",
	"name": "Pepe",
	"last_name": "Grillo",
	"username": "pepegrillo",
	"password": "123",
	"type_user": "1",
	"addresses": [{
		"id_address": "1",
		"address": "Castelli 123",
		"meter": {
			"id_meter": "1",
			"serial_number": "123456",
			"password": "123456",
			"model": {
				"id_model": "1",
				"name": "SB",
				"brand": {
					"id_brand": "1",
					"name": "NIKE"
				}
			},
			"bills": [{
				"id_bill": "1",
				"initial_measurement": "2021-08-09",
				"final_measurement": "2021-09-09",
				"total_consumption": "5000",
				"total_payable": "60000",
				"date": "2021-10-09",
				"expiration": "2021-09-24",
				"payed": "false"
			}],
			"rate": {
				"id_rate": "1",
				"value": "200",
				"type_rate": "A"
			}
		}
	}] 
},{ "id": "2",
	"name": "Pepe2",
	"last_name": "Grillo2",
	"username": "pepegrillo2",
	"password": "1234",
	"type_user": "1",
	"addresses": [{
		"id_address": "2",
		"address": "Arenales 123",
		"meter": {
			"id_meter": "2",
			"serial_number": "1234567",
			"password": "1234567",
			"model": {
				"id_model": "1",
				"name": "SB",
				"brand": {
					"id_brand": "1",
					"name": "Nike"
				}
			},
			"bills": [{
				"id_bill": "2",
				"initial_measurement": "2021-08-09",
				"final_measurement": "2021-09-09",
				"total_consumption": "6000",
				"total_payable": "70000",
				"date": "2021-10-09",
				"expiration": "2021-09-24",
				"payed": "true"
			}],
			"rate": {
				"id_rate": "2",
				"value": "300",
				"type_rate": "B"
			}
		}
	}] 
}, 
   { "id": "3",
	"name": "Pepe3",
	"last_name": "Grillo3",
	"username": "pepegrillo3",
	"password": "12345",
	"type_user": "1",
	"addresses": [{
		"id_address": "3",
		"address": "Sarmiento 123",
		"meter": {
			"id_meter": "3",
			"serial_number": "123456789",
			"password": "123456789",
			"model": {
				"id_model": "2",
				"name": "Janoski",
				"brand": {
					"id_brand": "1",
					"name": "Nike"
				}
			},
			"bills": [{
				"id_bill": "3",
				"initial_measurement": "2020-08-09",
				"final_measurement": "2020-09-09",
				"total_consumption": "7000",
				"total_payable": "60000",
				"date": "2020-10-09",
				"expiration": "2020-09-24",
				"payed": "true"
			}],
			"rate": {
				"id_rate": "1",
				"value": "200",
				"type_rate": "A"
			}
		}
	}] 
} , 
{ "id": "1",
"name": "Pepe4",
"last_name": "Grillo4",
"username": "pepegrillo4",
"password": "12389",
"type_user": "0"
} ]
    
db.addresses.insert({ 

})
    

INSERT INTO users(`name`, last_name, username, `password`, type_user) VALUES
("Pepe", "Grillo", "pepegrillo", "123",  1),
("Pepe2", "Grillo2", "pepegrillo2", "1234", 1),
("Pepe3", "Grillo3", "pepegrillo3", "1235", 1)
;



udee.users = collection(){
    id_user : int,
    name : string,
    last_name : string,
    username : string,
    password : string,
    type_user : int
    addresses = collection(Address)
}

