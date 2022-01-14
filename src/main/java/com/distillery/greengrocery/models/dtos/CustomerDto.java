package com.distillery.greengrocery.models.dtos;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.NotBlank;

import com.distillery.greengrocery.models.dtos.validations.PostGroup;
import com.distillery.greengrocery.models.dtos.validations.PutGroup;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "Represents a customer in the system.")
public class CustomerDto extends PersonDto {
	@ApiModelProperty(value = "${CustomerDto.shipmentAddress}", example = "Mitre 534 CABA", dataType = "String")
	@NotBlank(message = "shipmentAddress must not be null nor blank",
			groups = { PutGroup.class, PostGroup.class })
	private String shipmentAddress;
	
	@ApiModelProperty(value = "${CustomerDto.finalCustomer}", example = "true", dataType = "Boolean")
	@NotNull(message = "finalCustomer must not be null",
			groups = { PutGroup.class, PostGroup.class })
	private Boolean finalCustomer;
	
	@ApiModelProperty(value = "${CustomerDto.cuit}", example = "20-21567895-9", dataType = "String")
	@NotNull(message = "cuit must not be null",
			groups = { PutGroup.class, PostGroup.class })
	@Pattern(regexp = "^\\d{2}-\\d+-\\d$",
			message = "cuit must be a valid cuit value",
			groups = { PutGroup.class, PostGroup.class })
	private String cuit;

	public String getShipmentAddress() {
		return shipmentAddress;
	}

	public void setShipmentAddress(String shipmentAddress) {
		this.shipmentAddress = shipmentAddress;
	}

	public Boolean getFinalCustomer() {
		return finalCustomer;
	}

	public void setFinalCustomer(Boolean finalCustomer) {
		this.finalCustomer = finalCustomer;
	}

	public String getCuit() {
		return cuit;
	}

	public void setCuit(String cuit) {
		this.cuit = cuit;
	}
}
