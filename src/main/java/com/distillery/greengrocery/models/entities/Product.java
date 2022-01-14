package com.distillery.greengrocery.models.entities;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Product {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(length = 50)
	private String name;
	
	@Column(length = 100)
	private String description;
	
	@OneToMany(mappedBy = "product")
	private Set<FranchiseProduct> franchises;
	
	@OneToMany(mappedBy = "product")
	private Set<ProductOnSale> sales;
	
	private Float price;
	
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
	public Float getPrice() {
		return price;
	}
	public void setPrice(Float price) {
		this.price = price;
	}
	public Set<FranchiseProduct> getFranchises() {
		return franchises;
	}
	public void setFranchises(Set<FranchiseProduct> franchises) {
		this.franchises = franchises;
	}
	public Set<ProductOnSale> getSales() {
		return sales;
	}
	public void setSales(Set<ProductOnSale> sales) {
		this.sales = sales;
	}
}
