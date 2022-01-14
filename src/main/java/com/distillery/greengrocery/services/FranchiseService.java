package com.distillery.greengrocery.services;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.distillery.greengrocery.models.dtos.FranchiseDto;
import com.distillery.greengrocery.models.dtos.FranchiseProductDto;
import com.distillery.greengrocery.models.entities.Employee;
import com.distillery.greengrocery.models.entities.Franchise;
import com.distillery.greengrocery.models.entities.FranchiseProduct;
import com.distillery.greengrocery.models.entities.FranchiseProductKey;
import com.distillery.greengrocery.models.entities.Product;
import com.distillery.greengrocery.repositories.FranchiseProductRepository;
import com.distillery.greengrocery.repositories.FranchiseRepository;

@Service
public class FranchiseService {
	@Autowired
	private FranchiseRepository franchiseRepository;
	
	@Autowired
	private FranchiseProductRepository franchiseProductRepository;
	
	/**
	 * Finds all the franchises in the database
	 * @return a list containing the franchises
	 */
	public List<FranchiseDto> findAll() {
		List<Franchise> franchises = this.franchiseRepository.findAll();
		
		return franchises.stream().map(franchise -> {
			FranchiseDto franchiseDto = new FranchiseDto();
			BeanUtils.copyProperties(franchise, franchiseDto);
			return franchiseDto;
		}).collect(Collectors.toList());
	}
	
	/**
	 * Finds franchise by its id, its employees and products.
	 * @param id
	 * @return Franchise with its employees and products.
	 */
	public FranchiseDto findById(Long id) {
		Franchise franchise = this.getById(id);
		FranchiseDto franchiseDto = new FranchiseDto();
		BeanUtils.copyProperties(franchise, franchiseDto);
		franchiseDto.setEmployeeIds(
				franchise.getEmployees().stream()
				.map(employee -> employee.getId())
				.collect(Collectors.toSet()));
		franchiseDto.setFranchiseProducts(
				franchise.getFranchiseProducts().stream()
				.map(franchiseProduct -> {
					FranchiseProductDto franchiseProductDto = new FranchiseProductDto();
					franchiseProductDto.setAmount(franchiseProduct.getAmount());
					franchiseProductDto.setProductId(franchiseProduct.getProduct().getId());
					franchiseProductDto.setFranchiseId(franchiseProduct.getFranchise().getId());
					return franchiseProductDto;
				})
				.collect(Collectors.toSet()));
		
		return franchiseDto;
	}
	
	/**
	 * Saves a new franchise and its products (if exist).
	 * It does not save employees (you can do so by saving new or editing existing ones)
	 * @param franchiseDto
	 * @return saved franchise.
	 */
	public FranchiseDto saveNew(FranchiseDto franchiseDto) {
		Franchise franchise = new Franchise();
		BeanUtils.copyProperties(franchiseDto, franchise, "id");
		
		Franchise savedFranchise = this.franchiseRepository.saveAndFlush(franchise);
		
		// Map properties, products and employees to the dto
		FranchiseDto savedFranchiseDto = new FranchiseDto();
		BeanUtils.copyProperties(savedFranchise, savedFranchiseDto);
		
		return savedFranchiseDto;
	}
	
	/**
	 * Edits a franchise
	 * It does not edit its employees (to do so, edit the employee itself)
	 * It does not edit its products either (edit them using the methods for that)
	 * @param franchiseDto
	 * @return saved franchise
	 */
	public FranchiseDto editById(FranchiseDto franchiseDto) {
		Franchise franchise = this.getById(franchiseDto.getId());
		BeanUtils.copyProperties(franchiseDto, franchise);
		
		Franchise savedFranchise = this.franchiseRepository.saveAndFlush(franchise);
		
		// Map properties and employees to the dto
		FranchiseDto savedFranchiseDto = new FranchiseDto();
		BeanUtils.copyProperties(savedFranchise, savedFranchiseDto);
		savedFranchiseDto.setEmployeeIds(
				savedFranchise.getEmployees().stream()
				.map(Employee::getId)
				.collect(Collectors.toSet()));
		
		return savedFranchiseDto;
	}
	
	public void deleteById(Long id) throws EntityNotFoundException {
		try {
			this.franchiseRepository.deleteById(id);
		} catch (EmptyResultDataAccessException e) {
			throw new EntityNotFoundException();
		}
	}
	
	/**
	 * Adds products to a franchise. For every product in the Set, if the product was already associated 
	 * with the franchise, sums the amount and saves it. If not, assigns the received amount.
	 * @param franchiseProducts Set of products to add
	 * @return the saved products
	 */
	public Set<FranchiseProductDto> addFranchiseProducts(Set<FranchiseProductDto> franchiseProductsDto) {
		Set<FranchiseProduct> savedFranchiseProducts = new HashSet<>();
		franchiseProductsDto.forEach(franchiseProduct -> {
			FranchiseProductKey key = new FranchiseProductKey();
			FranchiseProduct franchiseProductToSave = null;
			key.setFranchiseId(franchiseProduct.getFranchiseId());
			key.setProductId(franchiseProduct.getProductId());
			
			Optional<FranchiseProduct> optional = this.franchiseProductRepository.findById(key);
			if (optional.isEmpty()) {
				franchiseProductToSave = new FranchiseProduct();
				franchiseProductToSave.setKey(key);
				franchiseProductToSave.setAmount(franchiseProduct.getAmount());
			} else {
				FranchiseProduct savedFranchiseProduct = optional.get();
				savedFranchiseProduct.setAmount(savedFranchiseProduct.getAmount() + franchiseProduct.getAmount());
				franchiseProductToSave = savedFranchiseProduct;
			}
			franchiseProductToSave.setFranchise(new Franchise());
			franchiseProductToSave.setProduct(new Product());
			franchiseProductToSave.getFranchise().setId(franchiseProduct.getFranchiseId());
			franchiseProductToSave.getProduct().setId(franchiseProduct.getProductId());
			
			savedFranchiseProducts.add(this.franchiseProductRepository.save(franchiseProductToSave));
		});
		
		this.franchiseProductRepository.flush();
		
		return savedFranchiseProducts.stream().map(franchiseProduct -> {
			FranchiseProductDto franchiseProductDto = new FranchiseProductDto();
			franchiseProductDto.setAmount(franchiseProduct.getAmount());
			franchiseProductDto.setFranchiseId(franchiseProduct.getFranchise().getId());
			franchiseProductDto.setProductId(franchiseProduct.getProduct().getId());
			return franchiseProductDto;
		}).collect(Collectors.toSet());
	}
	
	/**
	 * Edits a franchise product. Assigns the amount received as parameter.
	 * @param franchiseProductDto
	 * @return the saved franchise product
	 */
	public FranchiseProductDto editFranchiseProduct(FranchiseProductDto franchiseProductDto) {
		FranchiseProduct franchiseProduct = this.getFranchiseProductById(franchiseProductDto.getProductId(), 
				franchiseProductDto.getFranchiseId());
		franchiseProduct.setAmount(franchiseProductDto.getAmount());
		
		FranchiseProduct savedFranchiseProduct = this.franchiseProductRepository.saveAndFlush(franchiseProduct);
		
		FranchiseProductDto savedFranchiseProductDto = new FranchiseProductDto();
		savedFranchiseProductDto.setAmount(savedFranchiseProduct.getAmount());
		savedFranchiseProductDto.setFranchiseId(savedFranchiseProduct.getFranchise().getId());
		savedFranchiseProductDto.setProductId(franchiseProduct.getProduct().getId());
		
		return savedFranchiseProductDto;
	}
	
	/**
	 * Removes a franchise product
	 * @param productId
	 * @param franchiseId
	 */
	public void deleteFranchiseProduct(Long productId, Long franchiseId) {
		FranchiseProductKey key = new FranchiseProductKey();
		key.setFranchiseId(franchiseId);
		key.setProductId(productId);
		
		this.franchiseProductRepository.deleteById(key);
	}
	
	private Franchise getById(Long id) {
		Optional<Franchise> optional = this.franchiseRepository.findById(id);
		if (optional.isEmpty()) {
			throw new EntityNotFoundException();
		}
		
		return optional.get();
	}
	
	private FranchiseProduct getFranchiseProductById(Long productId, Long franchiseId) {
		FranchiseProductKey key = new FranchiseProductKey();
		key.setFranchiseId(franchiseId);
		key.setProductId(productId);
		
		Optional<FranchiseProduct> optional = this.franchiseProductRepository.findById(key);
		if (optional.isEmpty()) {
			throw new EntityNotFoundException();
		}
		
		return optional.get();
	}
}
