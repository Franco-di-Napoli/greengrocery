package com.distillery.greengrocery.controllers;

import java.util.List;
import java.util.Set;

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

import com.distillery.greengrocery.models.dtos.FranchiseDto;
import com.distillery.greengrocery.models.dtos.FranchiseProductDto;
import com.distillery.greengrocery.models.dtos.validations.PostGroup;
import com.distillery.greengrocery.models.dtos.validations.PutGroup;
import com.distillery.greengrocery.services.FranchiseService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Example;
import io.swagger.annotations.ExampleProperty;

@RestController
@RequestMapping("/franchises")
@Api(value = "API for managing franchises", tags = "Franchises")
public class FranchiseController {
	@Autowired
	private FranchiseService franchiseService;
	
	@GetMapping(produces = { MediaType.APPLICATION_JSON_VALUE })
	@ApiOperation(notes = "${FranchiseController.findAll.notes}", value = "${FranchiseController.findAll}")
	@ApiResponses({
		@ApiResponse(code = 200, response = FranchiseDto.class, examples =
				@Example(value = { 
						@ExampleProperty(value = "[{ \"id\": 1, \"orderDate\": \"10-05-2019\" }]", mediaType = MediaType.APPLICATION_JSON_VALUE)
				}), message = "List of franchises", responseContainer = "List")})
	public ResponseEntity<?> findAll() {
		List<FranchiseDto> franchises = this.franchiseService.findAll();
		return new ResponseEntity<List<FranchiseDto>>(franchises, HttpStatus.OK);
	}
	
	@GetMapping(value = "{id}",
			produces = { MediaType.APPLICATION_JSON_VALUE })
	@ApiOperation(notes = "${FranchiseController.findById.notes}", value = "${FranchiseController.findById}")
	@ApiResponses({
		@ApiResponse(code = 200, response = FranchiseDto.class, examples = 
				@Example(value = { 
					@ExampleProperty(value = "{ \"id\": 1, \"orderDate\": \"10-05-2019\", \"employeeIds\": \"[1, 2]\", \"franchiseProducts\": [{\"franchiseId\": 1, \"productId\": 2, \"amount\": 100}] }" , mediaType = MediaType.APPLICATION_JSON_VALUE)
				}), message = "Franchise object"),
		@ApiResponse(code = 404, message = "Franchise not found")})
	public ResponseEntity<?> findById(@PathVariable
			@ApiParam(value = "${FranchiseController.findById.param}", type = "Long")
			Long id) {
		ResponseEntity<?> response = null;
		try {
			FranchiseDto franchise = this.franchiseService.findById(id);
			response = new ResponseEntity<FranchiseDto>(franchise, HttpStatus.OK);
		} catch (EntityNotFoundException e) {
			response = new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		
		return response;
	}
	
	@PostMapping(
			consumes = { MediaType.APPLICATION_JSON_VALUE },
			produces = { MediaType.APPLICATION_JSON_VALUE })
	@ApiOperation(value = "${FranchiseController.create}", notes = "${FranchiseController.create.notes}")
	@ApiResponses({
		@ApiResponse(code = 200, response = FranchiseDto.class, examples = 
				@Example(value = { 
						@ExampleProperty(value = "{ \"id\": 1, \"orderDate\": \"10-05-2019\", \"employeeIds\": \"[1, 2]\", \"franchiseProducts\": [{\"franchiseId\": 1, \"productId\": 2, \"amount\": 100}] }" , mediaType = MediaType.APPLICATION_JSON_VALUE)
					}), message = "Saved franchise object"),
			@ApiResponse(code = 400, message = "Bad request")})
	public ResponseEntity<?> create(@RequestBody 
			@Validated(value = PostGroup.class) 
			@ApiParam(value = "${FranchiseController.create.param}", type = "FranchiseDto")
	final FranchiseDto franchise) {
		FranchiseDto savedFranchise = this.franchiseService.saveNew(franchise);
		return new ResponseEntity<FranchiseDto>(savedFranchise, HttpStatus.CREATED);
	}
	
	@PutMapping(
			consumes = { MediaType.APPLICATION_JSON_VALUE },
			produces = { MediaType.APPLICATION_JSON_VALUE })
	@ApiOperation(value = "${FranchiseController.updateById}", notes = "${FranchiseController.updateById.notes}")
	@ApiResponses({
		@ApiResponse(code = 200, response = FranchiseDto.class, examples = 
				@Example(value = { 
						@ExampleProperty(value = "{ \"id\": 1, \"orderDate\": \"10-05-2019\", \"employeeIds\": \"[1, 2]\", \"franchiseProducts\": [{\"franchiseId\": 1, \"productId\": 2, \"amount\": 100}] }" , mediaType = MediaType.APPLICATION_JSON_VALUE)
					}), message = "Saved franchise object"),
		@ApiResponse(code = 400, message = "Bad request"),
		@ApiResponse(code = 404, message = "Franchise not found")})
	public ResponseEntity<?> editById(@RequestBody 
			@Validated(value = PutGroup.class)
			@ApiParam(value = "${FranchiseController.updateById.param}", type = "FranchiseDto")
			final FranchiseDto franchise) {
		ResponseEntity<?> response = null;
		try {
			FranchiseDto savedFranchise = this.franchiseService.editById(franchise);
			response = new ResponseEntity<FranchiseDto>(savedFranchise, HttpStatus.OK);
		} catch (EntityNotFoundException e) {
			response = new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		
		return response;
	}
	
	@RequestMapping(value = "{id}", method = RequestMethod.DELETE)
	@ApiOperation(value = "${FranchiseController.deleteById}", notes = "${FranchiseController.deleteById.notes}")
	@ApiResponses({
		@ApiResponse(code = 200, message = "Franchise deleted successfully"),
		@ApiResponse(code = 404, message = "Franchise not found")})
	public ResponseEntity<?> deleteById(@PathVariable
			@ApiParam(value = "${FranchiseController.deleteById.param}", type = "Long")
	Long id) {
		ResponseEntity<?> response = null;
		try {
			this.franchiseService.deleteById(id);
			response = new ResponseEntity<>(HttpStatus.OK);
		} catch (EntityNotFoundException e) {
			response = new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		
		return response;
	}
	
	@PostMapping(value = "products",
			consumes = { MediaType.APPLICATION_JSON_VALUE },
			produces = { MediaType.APPLICATION_JSON_VALUE })
	@ApiOperation(value = "${FranchiseController.addFranchiseProducts}", notes = "${FranchiseController.addFranchiseProducts.notes}")
	@ApiResponses({
		@ApiResponse(code = 200, response = FranchiseProductDto.class, responseContainer = "Set", examples = 
				@Example(value = { 
						@ExampleProperty(value = "[{ \"franchiseId\": 1, \"productId\": 2, \"amount\": 12.00 }]" , mediaType = MediaType.APPLICATION_JSON_VALUE)
				}), message = "Saved Franchise Products"),
		@ApiResponse(code = 400, message = "Bad request")})
	public ResponseEntity<?> addFranchiseProducts(@RequestBody
			@Validated(value = { PostGroup.class })
			@ApiParam(value = "${FranchiseController.addFranchiseProducts.param}", type = "FranchiseProductDto")
			final Set<FranchiseProductDto> franchiseProducts) {
		Set<FranchiseProductDto> savedFranchiseProducts = this.franchiseService.addFranchiseProducts(franchiseProducts);
		return new ResponseEntity<Set<FranchiseProductDto>>(savedFranchiseProducts, HttpStatus.CREATED);
	}
	
	@PutMapping(value = "products",
			consumes = { MediaType.APPLICATION_JSON_VALUE },
			produces = { MediaType.APPLICATION_JSON_VALUE })
	@ApiOperation(value = "${FranchiseController.updateFranchiseProduct}", notes = "${FranchiseController.updateFranchiseProduct.notes}")
	@ApiResponses({
		@ApiResponse(code = 200, response = FranchiseProductDto.class, examples = 
				@Example(value = { 
					@ExampleProperty(value = "{ \"franchiseId\": 1, \"productId\": 2, \"amount\": 12.00 }" , mediaType = MediaType.APPLICATION_JSON_VALUE),
				}),
			message = "Saved franchise product"),
		@ApiResponse(code = 400, message = "Bad request"),
		@ApiResponse(code = 404, message = "Franchise product not found")})
	public ResponseEntity<?> updateFranchiseProduct(@RequestBody
			@Validated(value = { PutGroup.class })
			@ApiParam(value = "${FranchiseController.updateFranchiseProduct.param}", type = "FranchiseProductDto")
	final FranchiseProductDto franchiseProduct) {
		ResponseEntity<?> response = null;
		try {
			FranchiseProductDto savedFranchiseProduct = this.franchiseService.editFranchiseProduct(franchiseProduct);
			response = new ResponseEntity<FranchiseProductDto>(savedFranchiseProduct, HttpStatus.OK);
		} catch (EntityNotFoundException e) {
			response = new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		
		return response;
	}
	
	@RequestMapping(value = "products/{franchiseId}/{productId}", method = RequestMethod.DELETE)
	@ApiOperation(value = "${FranchiseController.deleteFranchiseProductById}", notes = "${FranchiseController.deleteFranchiseProductById.notes}")
	@ApiResponses({
		@ApiResponse(code = 200, message = "Franchise product deleted successfully"),
		@ApiResponse(code = 404, message = "Franchise product not found")})
	public ResponseEntity<?> deleteFranchiseProductById(
			@PathVariable
			@ApiParam(value = "${FranchiseController.deleteFranchiseProductById.param.franchiseId}", type = "Long")
			Long franchiseId,
			@PathVariable
			@ApiParam(value = "${FranchiseController.deleteFranchiseProductById.param.productId}", type = "Long")
			Long productId) {
		ResponseEntity<?> response = null;
		try {
			this.franchiseService.deleteFranchiseProduct(productId, franchiseId);
			response = new ResponseEntity<>(HttpStatus.OK);
		} catch (EntityNotFoundException e) {
			response = new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		
		return response;
	}
}
