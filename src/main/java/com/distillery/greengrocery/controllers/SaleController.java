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

import com.distillery.greengrocery.models.dtos.ProductOnSaleDto;
import com.distillery.greengrocery.models.dtos.SaleDto;
import com.distillery.greengrocery.models.dtos.validations.PostGroup;
import com.distillery.greengrocery.models.dtos.validations.PutGroup;
import com.distillery.greengrocery.services.SaleService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Example;
import io.swagger.annotations.ExampleProperty;
import io.swagger.annotations.Api;

@RestController
@RequestMapping("/sales")
@Api(value = "API for managing sales", tags="Sales")
public class SaleController {
	@Autowired
	private SaleService saleService;
	
	@GetMapping(produces = { MediaType.APPLICATION_JSON_VALUE })
	@ApiOperation(notes = "${SaleController.findAll.notes}", value = "${SaleController.findAll}")
	@ApiResponses({
		@ApiResponse(code = 200, response = SaleDto.class, message = "Sale object", examples = 
				@Example(value = { 
						@ExampleProperty(value = "[{ \"id\": 1, \"totalAmount\": 100.00 }]", mediaType = MediaType.APPLICATION_JSON_VALUE)
				}), responseContainer = "List")})
	public ResponseEntity<?> findAll() {
		List<SaleDto> sales = this.saleService.findAll();
		return new ResponseEntity<List<SaleDto>>(sales, HttpStatus.OK);
	}
	
	@GetMapping(value = "{id}",
			produces = { MediaType.APPLICATION_JSON_VALUE })
	@ApiOperation(notes = "${SaleController.findById.notes}", value = "${SaleController.findById}")
	@ApiResponses({
		@ApiResponse(code = 200, response = SaleDto.class, examples = 
				@Example(value = { 
						@ExampleProperty(value = "{ \"id\": 1, \"totalAmount\": 100.00, \"employeeId\": 1, \"customerId\": 2 ]", mediaType = MediaType.APPLICATION_JSON_VALUE)
				}), message = "Sale object"),
		@ApiResponse(code = 404, message = "Sale not found")})
	public ResponseEntity<?> findById(@PathVariable 
			@ApiParam(value = "${SaleController.findById.param}", type = "Long") Long id) {
		ResponseEntity<?> response = null;
		try {			
			SaleDto sale = this.saleService.findById(id);
			response = new ResponseEntity<SaleDto>(sale, HttpStatus.OK);
		} catch (EntityNotFoundException e) {
			response = new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		
		return response;
	}
	
	@PostMapping(
			consumes = { MediaType.APPLICATION_JSON_VALUE },
			produces = { MediaType.APPLICATION_JSON_VALUE })
	@ApiOperation(value = "${SaleController.create}", notes = "${SaleController.create.notes}")
	@ApiResponses({
		@ApiResponse(code = 201, response = SaleDto.class, examples = 
				@Example(value = { 
						@ExampleProperty(value = "{ \"id\": 1, \"totalAmount\": 100.00, \"employeeId\": 1, \"customerId\": 2 }", mediaType = MediaType.APPLICATION_JSON_VALUE)
				}), message = "Saved sale object"),
		@ApiResponse(code = 400, message = "Bad request")})
	public ResponseEntity<?> create(@RequestBody
			@Validated(value = PostGroup.class)
			@ApiParam(value = "${SaleController.create.param}", type = "SaleDto") final SaleDto sale) {
		return new ResponseEntity<SaleDto>(this.saleService.saveNew(sale), HttpStatus.CREATED);
	}
	
	@RequestMapping(value = "{id}", method = RequestMethod.DELETE)
	@ApiOperation(value = "${SaleController.deleteById}", notes = "${SaleController.deleteById.notes}")
	@ApiResponses({
		@ApiResponse(code = 200, message = "Sale deleted successfully"),
		@ApiResponse(code = 404, message = "Sale not found")})
	public ResponseEntity<?> deleteById(@PathVariable 
			@ApiParam(value = "${SaleController.deleteById.param}", type = "Long") Long id) {
		ResponseEntity<?> response = null;
		try {
			this.saleService.deleteById(id);
			response = new ResponseEntity<>(HttpStatus.OK);
		} catch (EntityNotFoundException e) {
			response = new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		
		return response;
	}
	
	@PutMapping(
			consumes = { MediaType.APPLICATION_JSON_VALUE },
			produces = { MediaType.APPLICATION_JSON_VALUE })
	@ApiOperation(value = "${SaleController.updateById}", notes = "${SaleController.updateById.notes}")
	@ApiResponses({
		@ApiResponse(code = 200, response = SaleDto.class, examples = 
				@Example(value = { 
						@ExampleProperty(value = "{ \"id\": 1, \"totalAmount\": 100.00, \"employeeId\": 1, \"customerId\": 2 }", mediaType = MediaType.APPLICATION_JSON_VALUE)
				}), message = "Saved sale object"),
		@ApiResponse(code = 404, message = "Sale not found"),
		@ApiResponse(code = 400, message = "Bad request")})
	public ResponseEntity<?> updateById(@RequestBody
			@Validated(value = PutGroup.class)
			@ApiParam(value = "${SaleController.updateById.param}", type = "SaleDto") final SaleDto sale) {
		ResponseEntity<?> response = null;
		try {
			SaleDto saleDto = this.saleService.editById(sale);
			response = new ResponseEntity<SaleDto>(saleDto, HttpStatus.OK);
		} catch (EntityNotFoundException e) {
			response = new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		
		return response;
	}
	
	@PostMapping(value = "products",
			consumes = { MediaType.APPLICATION_JSON_VALUE },
			produces = { MediaType.APPLICATION_JSON_VALUE })
	@ApiOperation(value = "${SaleController.addProductsOnSale}", notes = "${SaleController.addProductsOnSale.notes}")
	@ApiResponses({
		@ApiResponse(code = 201, response = ProductOnSaleDto.class, examples = 
				@Example(value = { 
						@ExampleProperty(value = "{ \"productId\": 1, \"saleId\": 2, \"quantity\": 100, \"amount\": 200 }", mediaType = MediaType.APPLICATION_JSON_VALUE)
				}), responseContainer = "Set", message = "Saved ProductOnSale objects"),
		@ApiResponse(code = 400, message = "Bad request")})
	public ResponseEntity<?> addProductsOnSale(@RequestBody
			@Validated(value = PostGroup.class)
			@ApiParam(value = "${SaleController.addProductsOnSale.param}", type = "ProductOnSaleDto")
			final Set<ProductOnSaleDto> productOnSale) {
		return new ResponseEntity<Set<ProductOnSaleDto>>(this.saleService.addProductsOnSale(productOnSale), HttpStatus.CREATED);
	}
	
	@PutMapping(value = "products",
			consumes = { MediaType.APPLICATION_JSON_VALUE },
			produces = { MediaType.APPLICATION_JSON_VALUE })
	@ApiOperation(value = "${SaleController.editProductOnSale}", notes = "${SaleController.editProductOnSale.notes}")
	@ApiResponses({
		@ApiResponse(code = 200, response = ProductOnSaleDto.class, examples = 
				@Example(value = { 
						@ExampleProperty(value = "{ \"productId\": 1, \"saleId\": 2, \"quantity\": 100, \"amount\": 200 }", mediaType = MediaType.APPLICATION_JSON_VALUE)
				}), message = "Saved ProductOnSale object"),
		@ApiResponse(code = 404, message = "ProductOnSale not found"),
		@ApiResponse(code = 400, message = "Bad request")})
	public ResponseEntity<?> editProductOnSale(@RequestBody
			@Validated(value = PutGroup.class)
			@ApiParam(value = "${SaleController.editProductOnSale.param}", type = "ProductOnSaleDto")
			final ProductOnSaleDto productOnSale) {
		ResponseEntity<?> response = null;
		try {
			ProductOnSaleDto savedProductOnSale = this.saleService.editProductOnSale(productOnSale);
			response = new ResponseEntity<ProductOnSaleDto>(savedProductOnSale, HttpStatus.OK);
		} catch (EntityNotFoundException e) {
			response = new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		
		return response;
	}
	
	@RequestMapping(value = "products/{saleId}/{productId}", method = RequestMethod.DELETE)
	@ApiOperation(value = "${SaleController.deleteProductOnSale}", notes = "${SaleController.deleteProductOnSale.notes}")
	@ApiResponses({
		@ApiResponse(code = 200, message = "ProductOnSale deleted successfully"),
		@ApiResponse(code = 404, message = "ProductOnSale not found")})
	public ResponseEntity<?> deleteProductOnSale(@PathVariable 
			@ApiParam(value = "${SaleController.deleteProductOnSale.param.saleId}", type = "Long")
			Long saleId,
			@ApiParam(value = "${SaleController.deleteProductOnSale.param.productId}", type = "Long")
			Long productId) {
		ResponseEntity<?> response = null;
		try {
			this.saleService.deleteProductOnSale(productId, saleId);
			response = new ResponseEntity<>(HttpStatus.OK);
		} catch (EntityNotFoundException e) {
			response = new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		
		return response;
	}
	
}
