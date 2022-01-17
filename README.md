# How to run the application
You can run the application from the console with the following command:  
	
	mvn spring-boot:run
	
In this case, the application will execute in the *dev* profile (the default). If you'd like to change that, you should add the following parameter, where *profile* is the profile you want the application to run:  
	
	-Drun.jvmArguments="-Dspring.profiles.active={profile}"
	
## Access to the Swagger documentation

To access the Swagger documentation (only in *dev* profile), assuming the app is running on `localhost:8080`, you should go to the following URL:  
	
	http://localhost:8080/api/swagger-ui/index.html
	
The URL for the Swagger JSON is as follows:  

	http://localhost:8080/api/v2/api-docs
	
# Testing the application

## Access to the H2 console

To access the H2 database console (only in *dev* profile), you should go to the following URL (assuming you're running the application on *{hostname}:{port}*):  
	
	http://{hostname}:{port}/api/h2-console
	
## Products API

### API URL
The URL for the Products API (*APIURL* from now on) is as follows:  
	
	http://{hostname}:{port}/api/products
	
Assuming you're running your server on localhost:8080, it would be:  
	
	http://localhost:8080/api/products
	
### List products
To list all the products, send a **HTTP GET** request to the products API URL. Using the *curl* command, you should run the following:  
	
	curl {APIURL} -H "Content-Type: application/json"
	
### Create a new product
To create a new product, send a **HTTP POST** request to the API URL. For example, using *curl*, inserting the product *lettuce* with a price of 150 would be:  
	
	curl {APIURL} -H "Content-Type: application/json"  -d '{"name":"Lettuce","description":"Lettuce","price":150.0}'
	
### Edit a product
To edit an existing product, send a **HTTP PUT** request to the API URL, similarly to creating a new one, but in this case you must include de ID of the product in the request body. Example:  
	
	curl -X PUT {APIURL} -H "Content-Type: application/json" -d '{"id": 1, "name":"Lettuce","description":"Lettuce","price":200.0}'
	
### Delete a product
To delete a product, send a **HTTP DELETE** request to the API URL, sending the ID of the product to delete as a URL parameter:  
	
	curl -X DELETE {APIURL}/1 -H "Content-Type: application/json"
	
## Sales API

### API URL
The URL for the Sales API is as follows:  
	
	http://{hostname}:{port}/api/sales
	
### Sales API operations
The operations for this API are the same as the Products API ones, but in this case the JSON object you should use for creating or updating a sale is as follows:  
	
	{"id":1,"totalAmount":100.0,"employeeId":1,"customerId":2}
	
If you're going to create a new sale, you should not include the *id* attribute in the object. The *employeeId* and *customerId* values must be valid ids for an employee and customer respectively.

## Customers API

### API URL
The URL for the Customers API is as follows:  
	
	http://{hostname}:{port}/api/sales
	
### API operations
The operations for this API are the same as the previous ones, but in this case the JSON object you should use for creating or updating a customer is as follows:  
	
	{"id":2,"firstName":"Walter","lastName":"Nelson","dni":"15256478","shipmentAddress":"Mitre 1234", "finalCustomer":true, "cuit":"20-15256478-9"}
	
If you're going to create a new customer, you should not include the *id* attribute in the object.

## Employees API

### API URL
The URL for the Employees API is as follows:  
	
	http://{hostname}:{port}/api/employees
	
### API operations
The operations for this API are the same as the previous ones, but in this case the JSON object you should use for creating or updating an employee is as follows:  
	
	{"id":1, "firstName": "Alejandro", "lastName": "Fabbri", "dni": "14256477", "dateOfHire": "05-12-2021", "position": "CTO", "salary": 100000.5, "franchiseId": 1}
	
If you're going to create a new employee, you should not include the *id* attribute in the object. In all cases, the *franchiseId* value must be a valid franchise id.

## Franchises API

### API URL
The URL for the Franchises API is as follows:  
	
	http://{hostname}:{port}/api/franchises
	
### API operations
The operations for this API are the same as the previous ones, but in this case the JSON object you should use for creating or updating a franchise is as follows:  
	
	{"id":1, "dateOfHire": "10-11-2021"}
	
If you're going to create a new franchise, you should not include the *id* attribute in the object.
You can add products to the franchise at the moment of creating it. To do so, add to the franchise object mentioned previously a list of products as follows:  
	
	"franchiseProducts":[{"productId": 1, "amount": 120}]}
	
You can add as many products as you want, as long as these products already exist.

### Manage franchise products
You can add, edit or remove products from a franchise using the following URL:  
	
	http://{hostname}:{port}/api/franchises/products
	
#### Add products
You can add products sending a **HTTP POST** request to the mentioned URL with a list of the products to add as follows:  
	
	[{"franchiseId: 1, productId": 1, "amount": 120}, {"franchiseId: 1, "productId": 2, "amount": 80}]
	
#### Edit a product
To edit a product, send a **HTTP PUT** request to the same URL with the product to edit:  
	
	{"franchiseId: 1, productId": 1, "amount": 100}
	
#### Remove a product
To remove a product, send a **HTTP DELETE** request with the franchise ID and the product ID in the URL as follows:  
	
	curl -X DELETE http://{hostname}:{port}/api/franchises/products/{franchiseId}/{productId}
	
## Zones API

### API URL
The URL for the Franchises API is as follows:  
	
	http://{hostname}:{port}/api/zones
	
### API operations
The operations for this API are the same as the previous ones, but in this case the JSON object you should use for creating or updating a zone is as follows:  
	
	{"id":1, "name": "CABA", "description": "Capital Federal"}
	
If you're going to create a new zone, you should not include the *id* attribute in the object.
