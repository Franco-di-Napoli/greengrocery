package com.distillery.greengrocery.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.distillery.greengrocery.models.dtos.CustomerDto;
import com.distillery.greengrocery.models.entities.Customer;
import com.distillery.greengrocery.repositories.CustomerRepository;

@Service
public class CustomerService {
	@Autowired
	private CustomerRepository customerRepository;
	
	public List<CustomerDto> findAll() {
		List<Customer> customers = this.customerRepository.findAll();
		
		return customers.stream().map(customer -> {
			CustomerDto customerDto = new CustomerDto();
			BeanUtils.copyProperties(customer, customerDto);
			return customerDto;
		}).collect(Collectors.toList());
	}
	
	public CustomerDto findById(Long id)  throws EntityNotFoundException {
		Customer customer = this.getById(id);
		CustomerDto customerDto = new CustomerDto();
		BeanUtils.copyProperties(customer, customerDto);
		
		return customerDto;
	}
	
	public CustomerDto saveNew(CustomerDto customerDto) {
		Customer customer = new Customer();
		BeanUtils.copyProperties(customerDto, customer, "id");
		
		CustomerDto savedCustomerDto = new CustomerDto();
		Customer savedCustomer = this.customerRepository.saveAndFlush(customer);
		BeanUtils.copyProperties(savedCustomer, savedCustomerDto);
		
		return savedCustomerDto;
	}
	
	public CustomerDto editById(CustomerDto customerDto) throws EntityNotFoundException {
		Customer customer = this.getById(customerDto.getId());
		BeanUtils.copyProperties(customerDto, customer);
		
		CustomerDto savedCustomerDto = new CustomerDto();
		Customer savedCustomer = this.customerRepository.saveAndFlush(customer);
		BeanUtils.copyProperties(savedCustomer, savedCustomerDto);
		
		return savedCustomerDto;
	}
	
	public void deleteById(Long id) throws EntityNotFoundException {
		try {
			this.customerRepository.deleteById(id);
		} catch (EmptyResultDataAccessException e) {
			throw new EntityNotFoundException();
		}
	}

	private Customer getById(Long id) throws EntityNotFoundException {
		Optional<Customer> optional = this.customerRepository.findById(id);
		if (optional.isEmpty()) {
			throw new EntityNotFoundException();
		}
		
		return optional.get();
	}
}
