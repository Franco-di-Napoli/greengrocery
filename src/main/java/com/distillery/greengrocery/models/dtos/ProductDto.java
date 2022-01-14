package com.distillery.greengrocery.models.dtos;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.distillery.greengrocery.models.dtos.validations.PostGroup;
import com.distillery.greengrocery.models.dtos.validations.PutGroup;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "Represents a product in the system.")
public class ProductDto {
	private static final String MIN_PRICE = "0.1";
	
	@ApiModelProperty(value = "${ProductDto.id}", example = "1", dataType = "Long")
	@NotNull(
			message = "ID must not be null",
			groups = PutGroup.class)
	private Long id;
	
	@ApiModelProperty(value = "${ProductDto.name}", example = "Tomato", dataType = "String")
	@NotBlank(message = "name must not be null nor blank",
			groups = { PostGroup.class, PutGroup.class })
	private String name;
	
	@ApiModelProperty(value = "${ProductDto.description}", example = "Round tomato", dataType = "String")
	@NotBlank(message = "description must not be null nor blank",
			groups = { PostGroup.class, PutGroup.class })
	private String description;
	
	@ApiModelProperty(value = "${ProductDto.price}", example = "15.80", dataType = "Float")
	@NotNull(
			message = "price must not be null",
			groups = { PostGroup.class, PutGroup.class })
	@DecimalMin(value = MIN_PRICE,
			message = "price must be greater or equal to " + MIN_PRICE,
			groups = { PostGroup.class, PutGroup.class })
	private Float price;

	public Float getPrice() {
		return price;
	}

	public void setPrice(Float price) {
		this.price = price;
	}

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
