package com.distillery.greengrocery.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.distillery.greengrocery.models.dtos.ProductDto;
import com.distillery.greengrocery.models.entities.Product;
import com.distillery.greengrocery.repositories.ProductRepository;

@Service
public class ProductService {
	@Autowired
	private ProductRepository productRepository;

	/**
	 * Retrieves all the products
	 * @return a list containing all the products on the system
	 */
	public List<ProductDto> findAll() {
		List<Product> products = this.productRepository.findAll();
		
		return products.stream().map(product -> {
			ProductDto productDto = new ProductDto();
			BeanUtils.copyProperties(product, productDto, "franchises", "sales");
			return productDto;
		}).collect(Collectors.toList());
	}
	
	/**
	 * Retrieves the product whose id is the parameter
	 * @param id of the product to return
	 * @return the mentioned product or null if not found
	 */
	public ProductDto findById(Long id) throws EntityNotFoundException {
		Product product = this.getById(id);
		ProductDto productDto = new ProductDto();
		BeanUtils.copyProperties(product, productDto, "franchises", "sales");
		
		return productDto;
	}
	
	/**
	 * Saves a new product into the database
	 * @param productDto the new product. The attribute id, if passed, is ignored
	 * @return the saved product with its id
	 */
	public ProductDto saveNew(ProductDto productDto) {
		Product product = new Product();
		ProductDto savedProductDto = new ProductDto();
		BeanUtils.copyProperties(productDto, product, "id");
		
		Product savedProduct = this.productRepository.saveAndFlush(product);
		BeanUtils.copyProperties(savedProduct, savedProductDto);
		
		return savedProductDto;
	}
	
	/**
	 * Deletes the product whose id is the parameter
	 * @param id
	 */
	public void deleteById(Long id) throws EntityNotFoundException {
		try {
			this.productRepository.deleteById(id);
		} catch (EmptyResultDataAccessException e) {
			throw new EntityNotFoundException();
		}
	}
	
	/**
	 * Updates the product with the parameter
	 * @param product to update
	 * @return updated product
	 */
	public ProductDto editById(ProductDto product) throws EntityNotFoundException {
		Product existingProduct = this.getById(product.getId());
		BeanUtils.copyProperties(product, existingProduct);
		
		Product savedProduct = this.productRepository.saveAndFlush(existingProduct);
		
		ProductDto savedProductDto = new ProductDto();
		BeanUtils.copyProperties(savedProduct, savedProductDto);
		
		return savedProductDto;
	}
	
	private Product getById(Long id) throws EntityNotFoundException {
		Optional<Product> optional  = this.productRepository.findById(id);
		if (optional.isEmpty()) {
			throw new EntityNotFoundException();
		}
		
		return optional.get();
	}
}
