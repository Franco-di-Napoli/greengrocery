package com.distillery.greengrocery.models.dtos;

import io.swagger.annotations.ApiModelProperty;

public class ZoneDto {
	@ApiModelProperty(value = "${ZoneDto.id}", example = "1", dataType = "Long")
	private Long id;
	
	@ApiModelProperty(value = "${ZoneDto.name}", example = "CABA", dataType = "String")
	private String name;
	
	@ApiModelProperty(value = "${ZoneDto.description}", example = "Capital Federal", dataType = "String")
	private String description;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
