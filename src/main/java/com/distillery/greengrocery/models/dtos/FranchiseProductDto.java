package com.distillery.greengrocery.models.dtos;

import javax.validation.constraints.NotNull;

import com.distillery.greengrocery.models.dtos.validations.PostGroup;
import com.distillery.greengrocery.models.dtos.validations.PutGroup;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "Represents a franchise product in the system.")
public class FranchiseProductDto {
	@ApiModelProperty(value = "${FranchiseProductDto.franchiseId}", example = "1", dataType = "Long")
	@NotNull(message = "franchiseId must not be null",
			groups = { PutGroup.class, PostGroup.class })
	private Long franchiseId;
	
	@ApiModelProperty(value = "${FranchiseProductDto.productId}", example = "1", dataType = "Long")
	@NotNull(message = "productId must not be null",
			groups = { PutGroup.class, PostGroup.class })
	private Long productId;
	
	@ApiModelProperty(value = "${FranchiseProductDto.amount}", example = "10.50", dataType = "Float")
	@NotNull(message = "amount must not be null",
			groups = { PutGroup.class, PostGroup.class })
	private Float amount;
	
	public Long getFranchiseId() {
		return franchiseId;
	}
	public void setFranchiseId(Long franchiseId) {
		this.franchiseId = franchiseId;
	}
	public Long getProductId() {
		return productId;
	}
	public void setProductId(Long productId) {
		this.productId = productId;
	}
	public Float getAmount() {
		return amount;
	}
	public void setAmount(Float amount) {
		this.amount = amount;
	}
	
}
