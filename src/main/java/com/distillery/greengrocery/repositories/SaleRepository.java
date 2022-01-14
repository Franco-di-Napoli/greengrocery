package com.distillery.greengrocery.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.distillery.greengrocery.models.entities.Sale;

public interface SaleRepository extends JpaRepository<Sale, Long> {

}
