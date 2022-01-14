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

import com.distillery.greengrocery.models.dtos.ProductDto;
import com.distillery.greengrocery.models.dtos.validations.PostGroup;
import com.distillery.greengrocery.models.dtos.validations.PutGroup;
import com.distillery.greengrocery.services.ProductService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Example;
import io.swagger.annotations.ExampleProperty;

@RestController
@RequestMapping("/products")
@Api(value = "API for managing products", tags = "Products")
public class ProductController {
	@Autowired
	private ProductService productService;

	@GetMapping(produces = { MediaType.APPLICATION_JSON_VALUE })
	@ApiOperation(notes = "${ProductController.findAll.notes}", value = "${ProductController.findAll}")
	@ApiResponses({
		@ApiResponse(code = 200, response = ProductDto.class, examples =
				@Example(value = { 
						@ExampleProperty(value = "[{ \"id\": 1, \"name\": \"Tomato\", \"description\": \"Round tomato\", \"price\": 100.00 }]", mediaType = MediaType.APPLICATION_JSON_VALUE)
				}), message = "List of products", responseContainer = "List")})
	public ResponseEntity<?> getAll() {
		List<ProductDto> products = this.productService.findAll();
		return new ResponseEntity<List<ProductDto>>(products, HttpStatus.OK);
	}
	
	@GetMapping(value = "{id}",
			produces = { MediaType.APPLICATION_JSON_VALUE })
	@ApiOperation(notes = "${ProductController.findById.notes}", value = "${ProductController.findById}")
	@ApiResponses({
		@ApiResponse(code = 200, response = ProductDto.class, examples = 
				@Example(value = { 
						@ExampleProperty(value = "{ \"id\": 1, \"name\": \"Tomato\", \"description\": \"Round tomato\", \"price\": 100.00 }", mediaType = MediaType.APPLICATION_JSON_VALUE)
				}), message = "Product object"),
		@ApiResponse(code = 404, message = "Product not found")})
	public ResponseEntity<?> getById(@PathVariable
			@ApiParam(value = "${ProductController.findById.param}", type = "Long")
			Long id) {
		ResponseEntity<?> response = null;
		try {			
			ProductDto product = this.productService.findById(id);
			response = new ResponseEntity<ProductDto>(product, HttpStatus.OK);
		} catch (EntityNotFoundException e) {
			response = new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		
		return response;
	}
	
	@PostMapping(
			consumes = { MediaType.APPLICATION_JSON_VALUE },
			produces = { MediaType.APPLICATION_JSON_VALUE })
	@ApiOperation(value = "${ProductController.create}", notes = "${ProductController.create.notes}")
	@ApiResponses({
		@ApiResponse(code = 200, response = ProductDto.class, examples = 
				@Example(value = { 
						@ExampleProperty(value = "{ \"id\": 1, \"name\": \"Tomato\", \"description\": \"Round tomato\", \"price\": 100.00 }", mediaType = MediaType.APPLICATION_JSON_VALUE)
					}), message = "Saved product object"),
		@ApiResponse(code = 400, message = "Bad request")})
	public ResponseEntity<?> create(@RequestBody
			@Validated(value = PostGroup.class)
			@ApiParam(value = "${ProductController.create.param}", type = "ProductDto")
			final ProductDto product) {
		return new ResponseEntity<ProductDto>(this.productService.saveNew(product), HttpStatus.CREATED);
	}
	
	@PutMapping(
			consumes = { MediaType.APPLICATION_JSON_VALUE },
			produces = { MediaType.APPLICATION_JSON_VALUE })
	@ApiOperation(value = "${ProductController.updateById}", notes = "${ProductController.updateById.notes}")
	@ApiResponses({
		@ApiResponse(code = 200, response = ProductDto.class, examples = 
				@Example(value = { 
						@ExampleProperty(value = "{ \"id\": 1, \"name\": \"Tomato\", \"description\": \"Round tomato\", \"price\": 100.00 }", mediaType = MediaType.APPLICATION_JSON_VALUE)
					}), message = "Saved product object"),
		@ApiResponse(code = 400, message = "Bad request"),
		@ApiResponse(code = 404, message = "Product not found")})
	public ResponseEntity<?> updateById(@RequestBody
			@Validated(value = PutGroup.class)
			@ApiParam(value = "${ProductController.updateById.param}", type = "ProductDto")
			final ProductDto product) {
		ResponseEntity<?> response = null;
		try {
			ProductDto savedProduct = this.productService.editById(product);
			response = new ResponseEntity<ProductDto>(savedProduct, HttpStatus.OK);
		} catch (EntityNotFoundException e) {
			response = new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		
		return response;
	}
	
	@RequestMapping(value = "{id}", method = RequestMethod.DELETE)
	@ApiOperation(value = "${ProductController.deleteById}", notes = "${ProductController.deleteById.notes}")
	@ApiResponses({
		@ApiResponse(code = 200, message = "Product deleted successfully"),
		@ApiResponse(code = 404, message = "Product not found")})
	public ResponseEntity<?> deleteById(@PathVariable
			@ApiParam(value = "${ProductController.deleteById.param}", type = "Long")
			Long id) {
		ResponseEntity<?> response = null;
		try {
			this.productService.deleteById(id);
			response = new ResponseEntity<>(HttpStatus.OK);
		} catch (EntityNotFoundException e) {
			response = new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		
		return response;
	}
}
