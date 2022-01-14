package com.distillery.greengrocery.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.distillery.greengrocery.models.entities.Zone;

public interface ZoneRepository extends JpaRepository<Zone, Long> {

}
