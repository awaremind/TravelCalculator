package com.tivanov.travelmanager.infrastructure.persistence;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.tivanov.travelmanager.domain.model.map.Country;

@Repository
public class CountryRepositorySpringDataFacade {
	
	@Autowired
	private CountryRepository countryRepository;
	
	public Country save(Country offer) {
		return countryRepository.saveAndFlush(offer);
	}
	
	public Country findById(long id) {
		return countryRepository.findById(id);
	}
	
}
