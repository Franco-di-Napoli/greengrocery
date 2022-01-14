package com.distillery.greengrocery.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.distillery.greengrocery.models.entities.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

}
