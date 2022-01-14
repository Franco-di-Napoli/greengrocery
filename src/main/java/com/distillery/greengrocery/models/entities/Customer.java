package com.distillery.greengrocery.models.entities;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

@Entity
public class Customer extends Person {
	@OneToMany(mappedBy = "customer")
	private Set<Sale> sales;
	
	@Column(nullable = false)
	private String shipmentAddress;
	
	private Boolean finalCustomer;
	
	private String cuit;
	
	public Set<Sale> getSales() {
		return sales;
	}
	public void setSales(Set<Sale> sales) {
		this.sales = sales;
	}
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
