package com.distillery.greengrocery.models.entities;

import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PreRemove;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;



@Entity
public class Employee extends Person {
	@OneToMany(mappedBy = "employee")
	private Set<Sale> sales;
	
	@Temporal(TemporalType.DATE)
	@Column(nullable = false)
	private Date dateOfHire;
	
	@Column(length = 30)
	private String position;
	private Float salary;
	
	@ManyToOne
	@JoinColumn(nullable = false, name = "franchise_id")
	private Franchise franchise;
	
	// Set employee reference to null on associated sales and franchise before remove
	@PreRemove
	public void onRemove() {
		this.getFranchise().setEmployees(
				this.getFranchise().getEmployees().stream()
				.filter(employee -> ! employee.getId().equals(this.getId())
		).collect(Collectors.toSet()));
		this.getSales().forEach(sale -> {
			sale.setEmployee(null);
		});
	}
	
	public Set<Sale> getSales() {
		return sales;
	}
	public void setSales(Set<Sale> sales) {
		this.sales = sales;
	}
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
	public Franchise getFranchise() {
		return franchise;
	}
	public void setFranchise(Franchise franchise) {
		this.franchise = franchise;
	}
}
