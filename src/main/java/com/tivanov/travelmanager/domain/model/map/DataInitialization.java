package com.tivanov.travelmanager.domain.model.map;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitialization implements ApplicationRunner {

    @Autowired
    private CountriesMap countriesMap;

    @Override
	public void run(ApplicationArguments args) throws Exception {
    	
    	Country bg = new Country("Bulgaria", "BG", "BGN");
    	Country gr = new Country("Greece", "GR", "EUR");
    	Country mk = new Country("Northern Macedonia", "MK", "MKD");
    	Country tr = new Country("Turkey", "TR", "TRL");
    	Country rs = new Country("Serbia", "RS", "RSD");
    	Country ro = new Country("Romania", "RO", "RON");
    	Country al = new Country("Albania", "AL", "ALL");
    	Country ua = new Country("Ukraine", "UA", "UAH");
    	Country md = new Country("Moldova", "MD", "MDL");
    	
    	List<Country> countryList = Arrays.asList(bg, gr, mk, tr, rs, ro, al, ua, md);
    	countryList.forEach(c -> {
    		countriesMap.addCountry(c);
    	});
    	
    	addNeighbours(bg, gr, mk, tr, rs, ro);
    	addNeighbours(mk, gr, bg, rs);
    	addNeighbours(gr, mk, bg, tr);
    	addNeighbours(ro, rs, bg, ua, md);
	}

	private void addNeighbours(Country baseCountry, Country ...neighboursList) {
		Arrays.stream(neighboursList).forEach(c -> {
    		countriesMap.addCountryConnection(baseCountry, c);
    	});
	}
    
}