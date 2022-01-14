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

import com.distillery.greengrocery.models.dtos.EmployeeDto;
import com.distillery.greengrocery.models.dtos.validations.PostGroup;
import com.distillery.greengrocery.models.dtos.validations.PutGroup;
import com.distillery.greengrocery.services.EmployeeService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Example;
import io.swagger.annotations.ExampleProperty;

@RestController
@RequestMapping("/employees")
@Api(value = "API for managing employees", tags = "Employees")
public class EmployeeController {
	@Autowired
	private EmployeeService employeeService;
	
	@GetMapping(
			produces = { MediaType.APPLICATION_JSON_VALUE })
	@ApiOperation(notes = "${EmployeeController.findAll.notes}", value = "${EmployeeController.findAll}")
	@ApiResponses({
		@ApiResponse(code = 200, response = EmployeeDto.class, examples =
				@Example(value = {
						@ExampleProperty(value = "[{ \"id\": 1, \"dateOfHire\": \"10-05-2019\", \"position\": \"CTO\", \"salary\": 12000.00 }]", mediaType = MediaType.APPLICATION_JSON_VALUE)
				}), message = "List of employees", responseContainer = "List")})
	public ResponseEntity<?> findAll() {
		List<EmployeeDto> employees = this.employeeService.findAll();
		return new ResponseEntity<List<EmployeeDto>>(employees, HttpStatus.OK);
	}
	
	@GetMapping(value = "{id}",
			produces = { MediaType.APPLICATION_JSON_VALUE })
	@ApiOperation(notes = "${EmployeeController.findById.notes}", value = "${EmployeeController.findById}")
	@ApiResponses({
		@ApiResponse(code = 200, response = EmployeeDto.class, examples = 
				@Example(value = { 
					@ExampleProperty(value = "{ \"id\": 1, \"dateOfHire\": \"10-05-2019\", \"position\": \"CTO\", \"salary\": 12000.00, \"franchiseId\": 2 }" , mediaType = MediaType.APPLICATION_JSON_VALUE)
				}), message = "Employee object"),
		@ApiResponse(code = 404, message = "Employee not found")})
	public ResponseEntity<?> findById(@PathVariable
			@ApiParam(value = "${EmployeeController.findById.param}", type = "Long")
			Long id) {
		ResponseEntity<?> response = null;
		try {
			EmployeeDto employee = this.employeeService.findById(id);
			response = new ResponseEntity<EmployeeDto>(employee, HttpStatus.OK);
		} catch (EntityNotFoundException e) {
			response = new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		
		return response;
	}
	
	@PostMapping(
			consumes = { MediaType.APPLICATION_JSON_VALUE },
			produces = { MediaType.APPLICATION_JSON_VALUE })
	@ApiOperation(value = "${EmployeeController.create}", notes = "${EmployeeController.create.notes}")
	@ApiResponses({
		@ApiResponse(code = 200, response = EmployeeDto.class, examples = 
				@Example(value = { 
						@ExampleProperty(value = "{ \"id\": 1, \"dateOfHire\": \"10-05-2019\", \"position\": \"CTO\", \"salary\": 12000.00, \"franchiseId\": 2 }" , mediaType = MediaType.APPLICATION_JSON_VALUE)
					}), message = "Saved employee object"),
		@ApiResponse(code = 400, message = "Bad request")})
	public ResponseEntity<?> create(@RequestBody
			@Validated(value = PostGroup.class)
			@ApiParam(value = "${EmployeeController.create.param}", type = "EmployeeDto")
			final EmployeeDto employee) {
		EmployeeDto savedEmployee = this.employeeService.saveNew(employee);
		return new ResponseEntity<EmployeeDto>(savedEmployee, HttpStatus.CREATED);
	}
	
	@PutMapping(
			consumes = { MediaType.APPLICATION_JSON_VALUE },
			produces = { MediaType.APPLICATION_JSON_VALUE })
	@ApiOperation(value = "${EmployeeController.updateById}", notes = "${EmployeeController.updateById.notes}")
	@ApiResponses({
		@ApiResponse(code = 200, response = EmployeeDto.class, examples = 
				@Example(value = { 
						@ExampleProperty(value = "{ \"id\": 1, \"dateOfHire\": \"10-05-2019\", \"position\": \"CTO\", \"salary\": 12000.00, \"franchiseId\": 2 }" , mediaType = MediaType.APPLICATION_JSON_VALUE)
					}), message = "Saved employee object"),
		@ApiResponse(code = 400, message = "Bad request"),
		@ApiResponse(code = 404, message = "Employee not found")})
	public ResponseEntity<?> updateById(@RequestBody
			@Validated(value = PutGroup.class)
			@ApiParam(value = "${EmployeeController.updateById.param}", type = "EmployeeDto")
			final EmployeeDto employee) {
		ResponseEntity<?> response = null;
		try {
			EmployeeDto savedEmployee = this.employeeService.editById(employee);
			response = new ResponseEntity<EmployeeDto>(savedEmployee, HttpStatus.OK);
		} catch (EntityNotFoundException e) {
			response = new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		
		return response;
	}
	
	@RequestMapping(value = "{id}", method = RequestMethod.DELETE)
	@ApiOperation(value = "${EmployeeController.deleteById}", notes = "${EmployeeController.deleteById.notes}")
	@ApiResponses({
		@ApiResponse(code = 200, message = "Employee deleted successfully"),
		@ApiResponse(code = 404, message = "Employee not found")})
	public ResponseEntity<?> deleteById(@PathVariable
			@ApiParam(value = "${EmployeeController.deleteById.param}", type = "Long")
			Long id) {
		ResponseEntity<?> response = null;
		try {
			this.employeeService.deleteById(id);
			response = new ResponseEntity<>(HttpStatus.OK);
		} catch (EntityNotFoundException e) {
			response = new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		
		return response;
	}
}
