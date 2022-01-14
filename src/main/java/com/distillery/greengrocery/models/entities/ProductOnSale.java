package com.distillery.greengrocery.models.entities;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;

@Entity
public class ProductOnSale {
	@EmbeddedId
	private ProductSaleKey key;
	
	@ManyToOne
	@MapsId("productId")
	@JoinColumn(name = "product_id")
	private Product product;
	
	@ManyToOne
	@MapsId("saleId")
	@JoinColumn(name = "sale_id")
	private Sale sale;
	
	private Float quantity;
	
	private Float amount;
	
	public ProductSaleKey getKey() {
		return key;
	}
	public void setKey(ProductSaleKey key) {
		this.key = key;
	}
	public Product getProduct() {
		return product;
	}
	public void setProduct(Product product) {
		this.product = product;
	}
	public Sale getSale() {
		return sale;
	}
	public void setSale(Sale sale) {
		this.sale = sale;
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
