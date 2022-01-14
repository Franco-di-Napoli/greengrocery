package com.distillery.greengrocery.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.distillery.greengrocery.models.entities.FranchiseProduct;
import com.distillery.greengrocery.models.entities.FranchiseProductKey;

public interface FranchiseProductRepository extends JpaRepository<FranchiseProduct, FranchiseProductKey> {

}
