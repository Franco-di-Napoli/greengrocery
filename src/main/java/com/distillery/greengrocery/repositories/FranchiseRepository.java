package com.distillery.greengrocery.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.distillery.greengrocery.models.entities.Franchise;

public interface FranchiseRepository extends JpaRepository<Franchise, Long> {

}
