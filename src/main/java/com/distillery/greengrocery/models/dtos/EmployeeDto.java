package com.distillery.greengrocery.models.dtos;

import java.util.Date;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;

import com.distillery.greengrocery.models.dtos.validations.PostGroup;
import com.distillery.greengrocery.models.dtos.validations.PutGroup;
import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "Represents a employee in the system.")
public class EmployeeDto extends PersonDto {
	public final static String MIN_SALARY = "100.0";
	
	@ApiModelProperty(value = "${EmployeeDto.dateOfHire}", example = "20-05-2019", dataType = "Date")
	@JsonFormat(pattern = "dd-MM-yyyy")
	@PastOrPresent(
			message = "dateOfHire must be a valid past or present date",
			groups = { PostGroup.class, PutGroup.class })
	private Date dateOfHire;
	
	@ApiModelProperty(value = "${EmployeeDto.position}", example = "CTO", dataType = "String")
	@NotBlank(groups = { PostGroup.class, PutGroup.class },
			message = "position must not be null nor blank")
	private String position;
	
	@ApiModelProperty(value = "${EmployeeDto.salary}", example = "10000", dataType = "Float")
	@DecimalMin(value = MIN_SALARY,
			message = "salary must be greater or equal to " + MIN_SALARY,
			groups = { PostGroup.class, PutGroup.class })
	private Float salary;
	
	@ApiModelProperty(value = "${EmployeeDto.franchiseId}", example = "10", dataType = "Long")
	@NotNull(message = "franchiseId must not be null",
			groups = { PostGroup.class, PutGroup.class })
	private Long franchiseId;

	public Date getDateOfHire() {
		return dateOfHire;
	}

	public void setDateOfHire(Date dateOfHire) {
		this.dateOfHire = dateOfHire;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public Float getSalary() {
		return salary;
	}

	public void setSalary(Float salary) {
		this.salary = salary;
	}

	public Long getFranchiseId() {
		return franchiseId;
	}

	public void setFranchiseId(Long franchiseId) {
		this.franchiseId = franchiseId;
	}
}
