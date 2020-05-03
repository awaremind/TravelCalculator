package com.tivanov.travelmanager.infrastructure.persistence;

import java.math.BigDecimal;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.tivanov.travelmanager.domain.model.map.Country;


@EnableJpaRepositories
public interface CountryRepository extends JpaRepository<Country, Long> {
	
	Country findById(long id);
	
	Country findByCode(String code);
	
	Country findByCurrency(String currency);
	
	@Transactional
	@Modifying
	@Query("update Country c set c.rate = ?2 where c.currency = ?1")
	void setRateByCurrency(String currency, BigDecimal rate);
	
//	@Query(value = "select n "  
//			+ " from neighbours n "
//			+ " join country n.country1 c " 
//			+ " where c.name = ?1 ")
//	List<Country> findAllNeighbours(String country);
	
}
