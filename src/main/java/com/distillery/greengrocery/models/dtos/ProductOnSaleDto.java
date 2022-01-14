package com.distillery.greengrocery.models.dtos;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

import com.distillery.greengrocery.models.dtos.validations.PostGroup;
import com.distillery.greengrocery.models.dtos.validations.PutGroup;

public class ProductOnSaleDto {
	@NotNull(
			message = "productId must not be null",
			groups = { PutGroup.class, PostGroup.class })
	private Long productId;
	
	@NotNull(
			message = "saleId must not be null",
			groups = { PutGroup.class, PostGroup.class })
	private Long saleId;
	
	@NotNull(
			message = "quantity must not be null",
			groups = { PutGroup.class, PostGroup.class })
	private Float quantity;
	
	// Calculated automatically. Editing is not allowed
	@Null(
			message = "Not allowed to send amount",
			groups = { PutGroup.class, PostGroup.class })
	private Float amount;

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public Long getSaleId() {
		return saleId;
	}

	public void setSaleId(Long saleId) {
		this.saleId = saleId;
	}

	public Float getQuantity() {
		return quantity;
	}

	public void setQuantity(Float quantity) {
		this.quantity = quantity;
	}

	public Float getAmount() {
		return amount;
	}

	public void setAmount(Float amount) {
		this.amount = amount;
	}
}
