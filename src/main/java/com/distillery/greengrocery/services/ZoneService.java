package com.distillery.greengrocery.services;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.distillery.greengrocery.models.dtos.ZoneDto;
import com.distillery.greengrocery.models.entities.Zone;
import com.distillery.greengrocery.repositories.ZoneRepository;

@Service
public class ZoneService {
	@Autowired
	private ZoneRepository zoneRepository;
	
	public Set<ZoneDto> findAll() {
		List<Zone> zones = this.zoneRepository.findAll();
		return zones.stream().map(zone -> {
			ZoneDto zoneDto = new ZoneDto();
			BeanUtils.copyProperties(zone, zoneDto);
			return zoneDto;
		}).collect(Collectors.toSet());
	}
	
	public ZoneDto findById(Long id)  throws EntityNotFoundException {
		Zone zone = this.getById(id);
		ZoneDto zoneDto = new ZoneDto();
		BeanUtils.copyProperties(zone, zoneDto);
		
		return zoneDto;
	}
	
	public ZoneDto saveNew(ZoneDto zoneDto) {
		Zone zone = new Zone();
		BeanUtils.copyProperties(zoneDto, zone, "id");
		
		ZoneDto savedZoneDto = new ZoneDto();
		Zone savedZone = this.zoneRepository.saveAndFlush(zone);
		BeanUtils.copyProperties(savedZone, savedZoneDto);
		
		return savedZoneDto;
	}
	
	public ZoneDto editById(ZoneDto zoneDto) throws EntityNotFoundException {
		Zone zone = this.getById(zoneDto.getId());
		BeanUtils.copyProperties(zoneDto, zone);
		
		ZoneDto savedZoneDto = new ZoneDto();
		Zone savedZone = this.zoneRepository.saveAndFlush(zone);
		BeanUtils.copyProperties(savedZone, savedZoneDto);
		
		return savedZoneDto;
	}
	
	public void deleteById(Long id) throws EntityNotFoundException {
		try {
			this.zoneRepository.deleteById(id);
		} catch (EmptyResultDataAccessException e) {
			throw new EntityNotFoundException();
		}
	}
	
	private Zone getById(Long id) throws EntityNotFoundException {
		Optional<Zone> optional = this.zoneRepository.findById(id);
		if (optional.isEmpty()) {
			throw new EntityNotFoundException();
		}
		
		return optional.get();
	}
}
