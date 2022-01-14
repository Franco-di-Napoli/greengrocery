package com.distillery.greengrocery.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.distillery.greengrocery.models.entities.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

}
