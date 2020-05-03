package com.tivanov.travelmanager.domain.model.map;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import org.springframework.stereotype.Component;

/**
 * 
 * @author Tihomir Ivanov
 * 
 * A Graph class with update and traverse methods
 *
 */

@Component
public class CountriesMap {
	
    private Map<Country, List<Country>> adjVertices;
	
	public void addCountry(Country country) {
		adjVertices.putIfAbsent(country,  new ArrayList<>());
	}
     
	public void addVertex(String label) {
	    adjVertices.putIfAbsent(new Country(label), new ArrayList<>());
	}
	 
	public void removeVertex(String label) {
	    Country country = new Country(label);
	    adjVertices.values()
	    	.stream()
	    	.forEach(e -> e.remove(country));
	    adjVertices.remove(new Country(label));
	}
	
	public void addEdge(String label1, String label2) {
	    Country vertex1 = new Country(label1);
	    Country vertex2 = new Country(label2);
	    adjVertices.get(vertex1).add(vertex2);
	    adjVertices.get(vertex2).add(vertex1);
	}
	
	public void removeEdge(String label1, String label2) {
	    Country vertex1 = new Country(label1);
	    Country vertex2 = new Country(label2);
	    List<Country> verticesList1 = adjVertices.get(vertex1);
	    List<Country> verticesList2 = adjVertices.get(vertex2);
	    if (verticesList1 != null) {
	    	verticesList1.remove(vertex2);
	    }
	    if (verticesList2 != null) {
	    	verticesList2.remove(vertex1);
	    }
	}
	
	public Set<Country> breadthFirstTraversal(String root, int depth) {
	    Set<Country> visited = new LinkedHashSet<>();
	    Queue<Country> queue = new LinkedList<>();
	    queue.add(new Country(root));
	    int algorithmDepth = 0;
	    while (!queue.isEmpty() && algorithmDepth < depth) {
	    	algorithmDepth++; 
	        Country country = queue.poll();
	        List <Country> neghbours = adjVertices.get(country);
	        for (Country v : neghbours) {
	            if (!visited.contains(v)) {
	                visited.add(new Country(v.getCode()));
	                queue.add(new Country(v.getCode()));
	            }
	        }
	    }
	    return visited;
	}
}
