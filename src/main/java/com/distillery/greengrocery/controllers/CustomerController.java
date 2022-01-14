package com.distillery.greengrocery.controllers;

import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.distillery.greengrocery.models.dtos.CustomerDto;
import com.distillery.greengrocery.models.dtos.validations.PostGroup;
import com.distillery.greengrocery.models.dtos.validations.PutGroup;
import com.distillery.greengrocery.services.CustomerService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Example;
import io.swagger.annotations.ExampleProperty;

@RestController
@RequestMapping("/customers")
@Api(value = "API for managing customers", tags = "Customers")
public class CustomerController {
	@Autowired
	private CustomerService customerService;
	
	@GetMapping(
			produces = { MediaType.APPLICATION_JSON_VALUE })
	@ApiOperation(notes = "${CustomerController.findAll.notes}", value = "${CustomerController.findAll}")
	@ApiResponses({
		@ApiResponse(code = 200, response = CustomerDto.class, examples =
				@Example(value = {
						@ExampleProperty(value = "[{ \"id\": 1, \"shipmentAddress\": \"Mitre 156 CABA\", \"finalCustomer\": \"true\", \"cuit\": \"20-15987456\" }]", mediaType = MediaType.APPLICATION_JSON_VALUE)
				}), message = "List of customers", responseContainer = "List")})
	public ResponseEntity<?> findAll() {
		List<CustomerDto> customers = this.customerService.findAll();
		return new ResponseEntity<List<CustomerDto>>(customers, HttpStatus.OK);
	}
	
	@GetMapping(value = "{id}",
			produces = { MediaType.APPLICATION_JSON_VALUE })
	@ApiOperation(notes = "${CustomerController.findById.notes}", value = "${CustomerController.findById}")
	@ApiResponses({
		@ApiResponse(code = 200, response = CustomerDto.class, examples = 
				@Example(value = { 
						@ExampleProperty(value = "{ \"id\": 1, \"shipmentAddress\": \"Mitre 156 CABA\", \"finalCustomer\": \"true\", \"cuit\": \"20-15987456\" }", mediaType = MediaType.APPLICATION_JSON_VALUE)
				}), message = "Customer object"),
		@ApiResponse(code = 404, message = "Customer not found")})
	public ResponseEntity<?> findById(@PathVariable
			@ApiParam(value = "${CustomerController.findById.param}", type = "Long")
			Long id) {
		ResponseEntity<CustomerDto> response = null;
		try {
			CustomerDto customer = this.customerService.findById(id);
			response = new ResponseEntity<CustomerDto>(customer, HttpStatus.OK);
		} catch (EntityNotFoundException e) {
			response = new ResponseEntity<CustomerDto>(HttpStatus.NOT_FOUND);
		}
		
		return response;
	}
	
	@PostMapping(
			consumes = { MediaType.APPLICATION_JSON_VALUE },
			produces = { MediaType.APPLICATION_JSON_VALUE })
	@ApiOperation(value = "${CustomerController.create}", notes = "${CustomerController.create.notes}")
	@ApiResponses({
		@ApiResponse(code = 200, response = CustomerDto.class, examples = 
				@Example(value = { 
						@ExampleProperty(value = "{ \"id\": 1, \"shipmentAddress\": \"Mitre 156 CABA\", \"finalCustomer\": \"true\", \"cuit\": \"20-15987456\" }", mediaType = MediaType.APPLICATION_JSON_VALUE)
					}), message = "Saved customer object"),
		@ApiResponse(code = 400, message = "Bad request")})
	public ResponseEntity<?> create(@RequestBody
			@Validated(value = PostGroup.class)
			@ApiParam(value = "${CustomerController.create.param}", type = "CustomerDto")
			final CustomerDto customer) {
		CustomerDto savedCustomer = this.customerService.saveNew(customer);
		return new ResponseEntity<CustomerDto>(savedCustomer, HttpStatus.CREATED);
	}
	
	@PutMapping(
			consumes = { MediaType.APPLICATION_JSON_VALUE },
			produces = { MediaType.APPLICATION_JSON_VALUE })
	@ApiOperation(value = "${CustomerController.updateById}", notes = "${CustomerController.updateById.notes}")
	@ApiResponses({
		@ApiResponse(code = 200, response = CustomerDto.class, examples = 
				@Example(value = { 
						@ExampleProperty(value = "{ \"id\": 1, \"shipmentAddress\": \"Mitre 156 CABA\", \"finalCustomer\": \"true\", \"cuit\": \"20-15987456\" }", mediaType = MediaType.APPLICATION_JSON_VALUE)
					}), message = "Saved customer object"),
		@ApiResponse(code = 400, message = "Bad request"),
		@ApiResponse(code = 404, message = "Customer not found")})
	public ResponseEntity<?> editById(@RequestBody
			@Validated(value = PutGroup.class)
			@ApiParam(value = "${CustomerController.updateById.param}", type = "CustomerDto")
			final CustomerDto customer) {
		ResponseEntity<CustomerDto> response = null;
		try {
			CustomerDto savedCustomer = this.customerService.editById(customer);
			response = new ResponseEntity<CustomerDto>(savedCustomer, HttpStatus.OK);
		} catch (EntityNotFoundException e) {
			response = new ResponseEntity<CustomerDto>(HttpStatus.NOT_FOUND);
		}
		
		return response;
	}
	
	@RequestMapping(value = "{id}", method = RequestMethod.DELETE)
	@ApiOperation(value = "${CustomerController.deleteById}", notes = "${CustomerController.deleteById.notes}")
	@ApiResponses({
		@ApiResponse(code = 200, message = "Customer deleted successfully"),
		@ApiResponse(code = 404, message = "Customer not found")})
	public ResponseEntity<?> deleteById(@PathVariable
			@ApiParam(value = "${CustomerController.deleteById.param}", type = "Long")
			Long id) {
		ResponseEntity<?> response = null;
		try {
			this.customerService.deleteById(id);
			response = new ResponseEntity<>(HttpStatus.OK);
		} catch (EntityNotFoundException e) {
			response = new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		
		return response;
	}
}
