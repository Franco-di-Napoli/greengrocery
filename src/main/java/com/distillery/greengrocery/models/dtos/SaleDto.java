package com.distillery.greengrocery.models.dtos;

import java.util.Set;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

import com.distillery.greengrocery.models.dtos.validations.PostGroup;
import com.distillery.greengrocery.models.dtos.validations.PutGroup;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "Represents a sale in the system.")
public class SaleDto {
	@ApiModelProperty(value = "${SaleDto.id}", example = "1", dataType = "Long")
	@NotNull(
			message = "ID must not be null",
			groups = PutGroup.class)
	private Long id;
	
	@ApiModelProperty(value = "${SaleDto.totalAmount}", example = "150.50", dataType = "Float")
	@Null(
			message = "Not allowed to send totalAmount",
			groups = { PostGroup.class, PutGroup.class })
	private Float totalAmount;
	
	@ApiModelProperty(value = "${SaleDto.employeeId}", example = "120", dataType = "Long")
	@NotNull(
			message = "employeeId must not be null",
			groups = { PostGroup.class, PutGroup.class })
	private Long employeeId;
	
	@ApiModelProperty(value = "${SaleDto.customerId}", example = "80", dataType = "Long")
	@NotNull(
			message = "customerId must not be null",
			groups = { PostGroup.class, PutGroup.class })
	private Long customerId;
	
	// Only for GET by ID responses
	@ApiModelProperty(value = "${SaleDto.productsOnSale}", example = "[{\"franchiseId\": 1, \"productId\": 2, \"amount\": 100}]", dataType = "Set")
	@Null(groups = { PutGroup.class, PostGroup.class },
			message = "Not allowed to send products")
	private Set<ProductOnSaleDto> productsOnSale;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Float getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(Float totalAmount) {
		this.totalAmount = totalAmount;
	}

	public Long getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Long employeeId) {
		this.employeeId = employeeId;
	}

	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

	public Set<ProductOnSaleDto> getProductsOnSale() {
		return productsOnSale;
	}

	public void setProductsOnSale(Set<ProductOnSaleDto> productsOnSale) {
		this.productsOnSale = productsOnSale;
	}
}
