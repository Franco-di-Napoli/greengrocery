package com.distillery.greengrocery.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.distillery.greengrocery.models.dtos.EmployeeDto;
import com.distillery.greengrocery.models.entities.Employee;
import com.distillery.greengrocery.models.entities.Franchise;
import com.distillery.greengrocery.repositories.EmployeeRepository;

@Service
public class EmployeeService {
	@Autowired
	private EmployeeRepository employeeRepository;
	
	public List<EmployeeDto> findAll() {
		List<Employee> employees = this.employeeRepository.findAll();
		
		return employees.stream().map(employee -> {
			EmployeeDto employeeDto = new EmployeeDto();
			BeanUtils.copyProperties(employee, employeeDto);
			return employeeDto;
		}).collect(Collectors.toList());
	}
	
	public EmployeeDto findById(Long id) throws EntityNotFoundException {	
		Employee employee = this.getById(id);
		EmployeeDto employeeDto = new EmployeeDto();
		BeanUtils.copyProperties(employee, employeeDto);
		employeeDto.setFranchiseId(employee.getFranchise().getId());
		
		return employeeDto;
	}
	
	public EmployeeDto saveNew(EmployeeDto employeeDto) {
		Employee employee = new Employee();
		BeanUtils.copyProperties(employeeDto, employee, "id");
		employee.setFranchise(new Franchise());
		employee.getFranchise().setId(employeeDto.getFranchiseId());
		
		EmployeeDto savedEmployeeDto = new EmployeeDto();
		Employee savedEmployee = this.employeeRepository.saveAndFlush(employee);		
		BeanUtils.copyProperties(savedEmployee, savedEmployeeDto);
		savedEmployeeDto.setFranchiseId(savedEmployee.getFranchise().getId());
		
		return savedEmployeeDto;
	}
	
	public EmployeeDto editById(EmployeeDto employeeDto) throws EntityNotFoundException {
		Employee employee = this.getById(employeeDto.getId());
		BeanUtils.copyProperties(employeeDto, employee);
		
		EmployeeDto savedEmployeeDto = new EmployeeDto();
		Employee savedEmployee = this.employeeRepository.saveAndFlush(employee);		
		BeanUtils.copyProperties(savedEmployee, savedEmployeeDto);
		
		return savedEmployeeDto;
	}
	
	public void deleteById(Long id) {
		try {			
			this.employeeRepository.deleteById(id);
		} catch (EmptyResultDataAccessException e) {
			throw new EntityNotFoundException();
		}
	}
	
	private Employee getById(Long id) throws EntityNotFoundException {
		Optional<Employee> optional = this.employeeRepository.findById(id);
		if (optional.isEmpty()) {
			throw new EntityNotFoundException();
		}
		
		return optional.get();
	}
}
