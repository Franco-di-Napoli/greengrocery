package com.distillery.greengrocery.services;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.distillery.greengrocery.models.dtos.ProductOnSaleDto;
import com.distillery.greengrocery.models.dtos.SaleDto;
import com.distillery.greengrocery.models.entities.Customer;
import com.distillery.greengrocery.models.entities.Employee;
import com.distillery.greengrocery.models.entities.Product;
import com.distillery.greengrocery.models.entities.ProductOnSale;
import com.distillery.greengrocery.models.entities.ProductSaleKey;
import com.distillery.greengrocery.models.entities.Sale;
import com.distillery.greengrocery.repositories.ProductOnSaleRepository;
import com.distillery.greengrocery.repositories.SaleRepository;

@Service
public class SaleService {
	@Autowired
	private SaleRepository saleRepository;
	
	@Autowired
	private ProductOnSaleRepository productOnSaleRepository;
	
	/**
	 * Retrieves a list containing all the sales.
	 * @return the sales list
	 */
	public List<SaleDto> findAll() {
		List<Sale> sales = this.saleRepository.findAll();
		
		return sales.stream().map(sale -> {
			SaleDto saleDto = new SaleDto();
			BeanUtils.copyProperties(sale, saleDto);
			return saleDto;
		}).collect(Collectors.toList());
	}
	
	/**
	 * Find a sale by its id
	 * @param id the id value with which the sale will be retrieved
	 * @return the sale with the id passed or null if not found
	 * @throws EntityNotFoundException
	 */
	public SaleDto findById(Long id) throws EntityNotFoundException {
		Sale sale = this.getById(id);
		SaleDto saleDto = new SaleDto();
		BeanUtils.copyProperties(sale, saleDto);
		saleDto.setEmployeeId(sale.getEmployee().getId());
		saleDto.setCustomerId(sale.getCustomer().getId());
		saleDto.setProductsOnSale(sale.getProductsOnSale().stream().map(productOnSale -> {
			ProductOnSaleDto productOnSaleDto = new ProductOnSaleDto();
			productOnSaleDto.setAmount(productOnSale.getAmount());
			productOnSaleDto.setQuantity(productOnSale.getQuantity());
			productOnSaleDto.setProductId(productOnSale.getProduct().getId());
			productOnSaleDto.setSaleId(productOnSale.getSale().getId());
			
			return productOnSaleDto;
		}).collect(Collectors.toSet()));
		
		return saleDto;
	}
	
	/**
	 * Saves a new sale into the database. It must have the employeeId and customerId set with valid ids.
	 * The attribute id is ignored and totalAmount is set to 0.
	 * @param saleDto the sale to create.
	 * @return the saved sale.
	 */
	public SaleDto saveNew(SaleDto saleDto) {
		Sale sale = new Sale();
		SaleDto savedSaleDto = new SaleDto();
		BeanUtils.copyProperties(saleDto, sale, "id", "totalAmount");
		sale.setEmployee(new Employee());
		sale.setCustomer(new Customer());
		sale.getEmployee().setId(saleDto.getEmployeeId());
		sale.getCustomer().setId(saleDto.getCustomerId());
		sale.setTotalAmount(Float.valueOf(0));
		
		Sale savedSale = this.saleRepository.saveAndFlush(sale);
		BeanUtils.copyProperties(savedSale, savedSaleDto);
		savedSaleDto.setCustomerId(savedSale.getCustomer().getId());
		savedSaleDto.setEmployeeId(savedSale.getEmployee().getId());
		
		return savedSaleDto;
	}
	
	/**
	 * Deletes the sale whose id is the parameter
	 * @param id
	 */
	public void deleteById(Long id) {
		this.saleRepository.deleteById(id);
	}
	
	/**
	 * Edits the sale
	 * @param saleDto sale to edit. Must have its id attribute set. Attribute totalAmount is ignored.
	 * @return the saved sale
	 * @throws EntityNotFoundException
	 */
	public SaleDto editById(SaleDto saleDto) throws EntityNotFoundException {
		Sale existingSale = this.getById(saleDto.getId());
		BeanUtils.copyProperties(saleDto, existingSale, "totalAmount");
		existingSale.setCustomer(new Customer());
		existingSale.setEmployee(new Employee());
		existingSale.getCustomer().setId(saleDto.getCustomerId());
		existingSale.getEmployee().setId(saleDto.getEmployeeId());
		
		SaleDto savedSaleDto = new SaleDto();
		Sale savedSale = this.saleRepository.saveAndFlush(existingSale);
		BeanUtils.copyProperties(savedSale, savedSaleDto);
		savedSaleDto.setCustomerId(savedSale.getCustomer().getId());
		savedSaleDto.setEmployeeId(savedSale.getEmployee().getId());
		
		return savedSaleDto;
	}
	
	/**
	 * Adds products to sales.
	 * Updates the sales totalAmount attribute with the amount calculated for the new products.
	 * @param productsOnSaleDto Set of new products to add.
	 * @return Set of saved products on sale.
	 */
	public Set<ProductOnSaleDto> addProductsOnSale(Set<ProductOnSaleDto> productsOnSaleDto) {
		Set<ProductOnSale> savedProductsOnSale = new HashSet<>();
		
		productsOnSaleDto.forEach(productOnSaleDto -> {
			ProductOnSale productOnSale = new ProductOnSale();
			productOnSale.setKey(new ProductSaleKey());
			productOnSale.setProduct(new Product());
			productOnSale.setSale(new Sale());
			productOnSale.setQuantity(productOnSaleDto.getQuantity());
			productOnSale.getProduct().setId(productOnSaleDto.getProductId());
			productOnSale.getSale().setId(productOnSaleDto.getSaleId());
			
			ProductOnSale savedProductOnSale = this.productOnSaleRepository.save(productOnSale);
			
			// Set the amount to the productOnSale and add it to the sale totalAmount attribute
			Float amount = savedProductOnSale.getQuantity() * savedProductOnSale.getProduct().getPrice();
			savedProductOnSale.setAmount(amount);
			savedProductOnSale.getSale().addAmount(amount);
			
			savedProductOnSale = this.productOnSaleRepository.save(savedProductOnSale);
			savedProductsOnSale.add(savedProductOnSale);
		});
		this.productOnSaleRepository.flush();
		
		return savedProductsOnSale.stream().map(savedProductOnSale -> {
			ProductOnSaleDto savedProductOnSaleDto = new ProductOnSaleDto();
			savedProductOnSaleDto.setProductId(savedProductOnSale.getProduct().getId());
			savedProductOnSaleDto.setSaleId(savedProductOnSale.getSale().getId());
			savedProductOnSaleDto.setQuantity(savedProductOnSale.getQuantity());
			savedProductOnSaleDto.setAmount(savedProductOnSale.getAmount());
			
			return savedProductOnSaleDto;
		}).collect(Collectors.toSet());
	}
	
	/**
	 * Edits a product on sale.
	 * Updates the sale totalAmount with the edited product on sale.
	 * @param productOnSaleDto
	 * @return saved productOnSale
	 */
	public ProductOnSaleDto editProductOnSale(ProductOnSaleDto productOnSaleDto) {
		ProductOnSale productOnSale = this.getProductOnSaleById(productOnSaleDto.getProductId(), productOnSaleDto.getSaleId());
		Float originalQuantity = productOnSale.getQuantity();
		Float originalAmount = productOnSale.getAmount();
		productOnSale.setQuantity(productOnSaleDto.getQuantity());
		
		// if quantity changed, update amount on the productOnSale
		// and update totalAmount on the sale as well
		if (! originalQuantity.equals(productOnSaleDto.getQuantity())) {
			Float newAmount = productOnSaleDto.getQuantity() * productOnSale.getProduct().getPrice();
			productOnSale.setAmount(newAmount);
			productOnSale.getSale().substractAmount(originalAmount);
			productOnSale.getSale().addAmount(newAmount);
		}
		
		ProductOnSale savedProductOnSale = this.productOnSaleRepository.saveAndFlush(productOnSale);
		ProductOnSaleDto savedProductOnSaleDto = new ProductOnSaleDto();
		savedProductOnSaleDto.setAmount(savedProductOnSale.getAmount());
		savedProductOnSaleDto.setProductId(savedProductOnSale.getProduct().getId());
		savedProductOnSaleDto.setSaleId(savedProductOnSale.getSale().getId());
		savedProductOnSaleDto.setQuantity(savedProductOnSale.getQuantity());
		
		return savedProductOnSaleDto;
	}
	
	/**
	 * Deletes a productOnSale. Updates the sale totalAmount.
	 * @param productId
	 * @param saleId
	 */
	public void deleteProductOnSale(Long productId, Long saleId) {
		ProductOnSale productOnSale = this.getProductOnSaleById(productId, saleId);
		Float productAmount = productOnSale.getAmount();
		productOnSale.getSale().substractAmount(productAmount);
		
		ProductSaleKey key = new ProductSaleKey();
		key.setProductId(productId);
		key.setSaleId(saleId);
		this.saleRepository.saveAndFlush(productOnSale.getSale());
		this.productOnSaleRepository.deleteById(key);
	}
	
	private Sale getById(Long id) throws EntityNotFoundException {
		Optional<Sale> optional = this.saleRepository.findById(id);
		if (optional.isEmpty()) {
			throw new EntityNotFoundException();
		}
		
		return optional.get();
	}
	
	private ProductOnSale getProductOnSaleById(Long productId, Long saleId) throws EntityNotFoundException {
		ProductSaleKey key = new ProductSaleKey();
		key.setProductId(productId);
		key.setSaleId(saleId);
		
		Optional<ProductOnSale> optional = this.productOnSaleRepository.findById(key);
		if (optional.isEmpty()) {
			throw new EntityNotFoundException();
		}
		
		return optional.get();
	}

}
