package com.distillery.greengrocery.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.distillery.greengrocery.models.entities.ProductOnSale;
import com.distillery.greengrocery.models.entities.ProductSaleKey;

public interface ProductOnSaleRepository extends JpaRepository<ProductOnSale, ProductSaleKey> {

}
