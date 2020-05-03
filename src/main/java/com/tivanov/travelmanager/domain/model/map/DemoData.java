package com.tivanov.travelmanager.domain.model.map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class DemoData implements ApplicationRunner {

    @Autowired
    private CountriesMap countriesMap;

    @Override
	public void run(ApplicationArguments args) throws Exception {

    	countriesMap.addVertex("BG");
    	countriesMap.addVertex("RO");
    	countriesMap.addVertex("GR");
    	countriesMap.addVertex("TR");
    	countriesMap.addVertex("RS");
    	countriesMap.addVertex("AL");
    	countriesMap.addVertex("MK");
    	countriesMap.addEdge("BG","MK");
    	countriesMap.addEdge("BG","TR");
    	countriesMap.addEdge("BG","RS");
    	countriesMap.addEdge("BG","GR");
    	countriesMap.addEdge("BG","RO");
    	countriesMap.addEdge("GR","MK");
    	countriesMap.addEdge("GR","AL");
    	countriesMap.addEdge("GR","TR");
    	countriesMap.addEdge("MK","GR");
    	countriesMap.addEdge("MK","AL");
    	countriesMap.addEdge("MK","RS");
    	
	}
    
}