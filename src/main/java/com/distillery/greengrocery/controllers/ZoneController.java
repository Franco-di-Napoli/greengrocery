package com.distillery.greengrocery.controllers;

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

import com.distillery.greengrocery.models.dtos.ZoneDto;
import com.distillery.greengrocery.models.dtos.validations.PostGroup;
import com.distillery.greengrocery.models.dtos.validations.PutGroup;
import com.distillery.greengrocery.services.ZoneService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Example;
import io.swagger.annotations.ExampleProperty;

@RestController
@RequestMapping("/zones")
@Api(value = "API for managing zones", tags = "Zones")
public class ZoneController {
	@Autowired
	ZoneService zoneService;
	
	@GetMapping(
			produces = { MediaType.APPLICATION_JSON_VALUE })
	@ApiOperation(notes = "${ZoneController.findAll.notes}", value = "${ZoneController.findAll}")
	@ApiResponses({
		@ApiResponse(code = 200, response = ZoneDto.class, examples =
				@Example(value = {
						@ExampleProperty(value = "{ \"id\": 1, \"name\": \"CABA\", \"description\": \"Capital Federal\" }", mediaType = MediaType.APPLICATION_JSON_VALUE)
				}), message = "List of zones", responseContainer = "List")})
	public ResponseEntity<?> findAll() {
		Set<ZoneDto> zones = this.zoneService.findAll();
		return new ResponseEntity<Set<ZoneDto>>(zones, HttpStatus.OK);
	}
	
	@GetMapping(value = "{id}",
			produces = { MediaType.APPLICATION_JSON_VALUE })
	@ApiOperation(notes = "${ZoneController.findById.notes}", value = "${ZoneController.findById}")
	@ApiResponses({
		@ApiResponse(code = 200, response = ZoneDto.class, examples = 
				@Example(value = { 
						@ExampleProperty(value = "{ \"id\": 1, \"name\": \"CABA\", \"description\": \"Capital Federal\" }", mediaType = MediaType.APPLICATION_JSON_VALUE)
				}), message = "Zone object"),
		@ApiResponse(code = 404, message = "Zone not found")})
	public ResponseEntity<?> findById(@PathVariable
			@ApiParam(value = "${ZoneController.findById.param}", type = "Long")
			Long id) {
		ResponseEntity<ZoneDto> response = null;
		try {
			ZoneDto zone = this.zoneService.findById(id);
			response = new ResponseEntity<ZoneDto>(zone, HttpStatus.OK);
		} catch (EntityNotFoundException e) {
			response = new ResponseEntity<ZoneDto>(HttpStatus.NOT_FOUND);
		}
		
		return response;
	}
	
	@PostMapping(
			consumes = { MediaType.APPLICATION_JSON_VALUE },
			produces = { MediaType.APPLICATION_JSON_VALUE })
	@ApiOperation(value = "${ZoneController.create}", notes = "${ZoneController.create.notes}")
	@ApiResponses({
		@ApiResponse(code = 200, response = ZoneDto.class, examples = 
				@Example(value = { 
						@ExampleProperty(value = "{ \"id\": 1, \"name\": \"CABA\", \"description\": \"Capital Federal\" }", mediaType = MediaType.APPLICATION_JSON_VALUE)
					}), message = "Saved zone object"),
		@ApiResponse(code = 400, message = "Bad request")})
	public ResponseEntity<?> create(@RequestBody
			@Validated(value = PostGroup.class)
			@ApiParam(value = "${ZoneController.create.param}", type = "ZoneDto")
			final ZoneDto zone) {
		ZoneDto savedZone = this.zoneService.saveNew(zone);
		return new ResponseEntity<ZoneDto>(savedZone, HttpStatus.CREATED);
	}
	
	@PutMapping(
			consumes = { MediaType.APPLICATION_JSON_VALUE },
			produces = { MediaType.APPLICATION_JSON_VALUE })
	@ApiOperation(value = "${ZoneController.updateById}", notes = "${ZoneController.updateById.notes}")
	@ApiResponses({
		@ApiResponse(code = 200, response = ZoneDto.class, examples = 
				@Example(value = { 
						@ExampleProperty(value = "{ \"id\": 1, \"name\": \"CABA\", \"description\": \"Capital Federal\" }", mediaType = MediaType.APPLICATION_JSON_VALUE)
					}), message = "Saved zone object"),
		@ApiResponse(code = 400, message = "Bad request"),
		@ApiResponse(code = 404, message = "Zone not found")})
	public ResponseEntity<?> editById(@RequestBody
			@Validated(value = PutGroup.class)
			@ApiParam(value = "${ZoneController.updateById.param}", type = "ZoneDto")
			final ZoneDto zone) {
		ResponseEntity<ZoneDto> response = null;
		try {
			ZoneDto savedZone = this.zoneService.editById(zone);
			response = new ResponseEntity<ZoneDto>(savedZone, HttpStatus.OK);
		} catch (EntityNotFoundException e) {
			response = new ResponseEntity<ZoneDto>(HttpStatus.NOT_FOUND);
		}
		
		return response;
	}
	
	@RequestMapping(value = "{id}", method = RequestMethod.DELETE)
	@ApiOperation(value = "${ZoneController.deleteById}", notes = "${ZoneController.deleteById.notes}")
	@ApiResponses({
		@ApiResponse(code = 200, message = "Zone deleted successfully"),
		@ApiResponse(code = 404, message = "Zone not found")})
	public ResponseEntity<?> deleteById(@PathVariable
			@ApiParam(value = "${ZoneController.deleteById.param}", type = "Long")
			Long id) {
		ResponseEntity<?> response = null;
		try {
			this.zoneService.deleteById(id);
			response = new ResponseEntity<>(HttpStatus.OK);
		} catch (EntityNotFoundException e) {
			response = new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		
		return response;
	}
}
