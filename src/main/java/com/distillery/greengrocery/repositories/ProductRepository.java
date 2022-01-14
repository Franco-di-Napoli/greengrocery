package com.distillery.greengrocery.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.distillery.greengrocery.models.entities.Product;

public interface ProductRepository extends JpaRepository<Product, Long>  {

}
