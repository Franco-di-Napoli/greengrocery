package com.distillery.greengrocery.models.entities;

import javax.persistence.JoinColumn;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;

@Entity
public class FranchiseProduct {
	@EmbeddedId
	FranchiseProductKey key;
	
	@ManyToOne
	@MapsId("franchiseId")
	@JoinColumn(name = "franchise_id")
	private Franchise franchise;
	
	@ManyToOne
	@MapsId("productId")
	@JoinColumn(name = "product_id")
	private Product product;
	
	private Float amount;

	public FranchiseProductKey getKey() {
		return key;
	}

	public void setKey(FranchiseProductKey key) {
		this.key = key;
	}

	public Franchise getFranchise() {
		return franchise;
	}

	public void setFranchise(Franchise franchise) {
		this.franchise = franchise;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public Float getAmount() {
		return amount;
	}

	public void setAmount(Float amount) {
		this.amount = amount;
	}
}
