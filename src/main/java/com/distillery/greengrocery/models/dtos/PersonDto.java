package com.distillery.greengrocery.models.dtos;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.distillery.greengrocery.models.dtos.validations.PostGroup;
import com.distillery.greengrocery.models.dtos.validations.PutGroup;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "Represents a person in the system.")
public class PersonDto {
	@ApiModelProperty(value = "${PersonDto.id}", example = "1", dataType = "Long")
	@NotNull(
			message = "ID must not be null",
			groups = PutGroup.class)
	private Long id;
	
	@ApiModelProperty(value = "${PersonDto.firstName}", example = "Carlos", dataType = "String")
	@NotBlank(message = "firstName must not be null nor blank",
			groups = { PostGroup.class, PutGroup.class })
	private String firstName;
	
	@ApiModelProperty(value = "${PersonDto.lastName}", example = "Castro", dataType = "String")
	@NotBlank(message = "lastName must not be null nor blank",
			groups = { PostGroup.class, PutGroup.class })
	private String lastName;
	
	@ApiModelProperty(value = "${PersonDto.dni}", example = "21578956", dataType = "String")
	@NotBlank(message = "dni must not be null nor blank",
			groups = { PostGroup.class, PutGroup.class })
	@Pattern(regexp = "^\\d+$",
			message = "dni must be a valid dni value",
			groups = { PutGroup.class, PostGroup.class })
	private String dni;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getDni() {
		return dni;
	}

	public void setDni(String dni) {
		this.dni = dni;
	}
}
