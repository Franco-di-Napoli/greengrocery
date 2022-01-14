package com.distillery.greengrocery.models.entities;

import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class Franchise {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@OneToMany(mappedBy = "franchise")
	private Set<Employee> employees;
	
	@Temporal(TemporalType.DATE)
	@Column(nullable = false)
	private Date orderDate;
	
	@OneToMany(mappedBy = "franchise")
	Set<FranchiseProduct> franchiseProducts;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Set<Employee> getEmployees() {
		return employees;
	}
	public void setEmployees(Set<Employee> employees) {
		this.employees = employees;
	}
	public Date getOrderDate() {
		return orderDate;
	}
	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}
	public Set<FranchiseProduct> getFranchiseProducts() {
		return franchiseProducts;
	}
	public void setFranchiseProducts(Set<FranchiseProduct> franchiseProducts) {
		this.franchiseProducts = franchiseProducts;
	}
}
