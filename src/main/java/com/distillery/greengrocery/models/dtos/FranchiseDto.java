package com.distillery.greengrocery.models.dtos;

import java.util.Date;
import java.util.Set;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Null;
import com.distillery.greengrocery.models.dtos.validations.PostGroup;
import com.distillery.greengrocery.models.dtos.validations.PutGroup;
import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "Represents a franchise in the system.")
public class FranchiseDto {
	@ApiModelProperty(value = "${FranchiseDto.id}", example = "1", dataType = "Long")
	@NotNull(
			message = "ID must not be null",
			groups = PutGroup.class)
	private Long id;
	
	@ApiModelProperty(value = "${FranchiseDto.orderDate}", example = "15-10-2018", dataType = "Date")
	@JsonFormat(pattern="dd-MM-yyyy")
	@PastOrPresent(
			message = "orderDate must be a valid past or present date",
			groups = { PostGroup.class, PutGroup.class })
	private Date orderDate;
	
	// Only for GET by ID responses
	@Null(groups = { PostGroup.class, PutGroup.class },
			message = "Not allowed to send employees")
	private Set<Long> employeeIds;
	
	// Only for GET by ID responses
	@ApiModelProperty(value = "${FranchiseDto.franchiseProducts}", example = "[{\"franchiseId\": 1, \"productId\": 2, \"amount\": 100}]", dataType = "List")
	@Null(groups = { PutGroup.class, PostGroup.class },
			message = "Not allowed to send products")
	private Set<FranchiseProductDto> franchiseProducts;
	
	public Set<Long> getEmployeeIds() {
		return employeeIds;
	}

	public void setEmployeeIds(Set<Long> employeeIds) {
		this.employeeIds = employeeIds;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}

	public Set<FranchiseProductDto> getFranchiseProducts() {
		return franchiseProducts;
	}

	public void setFranchiseProducts(Set<FranchiseProductDto> franchiseProducts) {
		this.franchiseProducts = franchiseProducts;
	}
}
