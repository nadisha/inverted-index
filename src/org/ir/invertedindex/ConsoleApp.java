package org.ir.invertedindex;

import java.util.List;
import java.util.Map;

public class ConsoleApp {

	public static void main(String[] args) {
		InvertedIndexService service = new InvertedIndexService();
		String [] fileNames = {"breads.txt", "desserts.txt", "favourites.txt", 
				"gliten_pizza.txt", "normal_pizza.txt", "special.pizza.txt"};
		Map<String, Dictionary> indexer = service.buildIndex(fileNames);
		
		List<Integer> pizza = indexer.get("pizza").getPosting();
		List<Integer> cheese = indexer.get("cheese").getPosting();
		
		List<Integer> documentIds = service.searchByTwoTerms(pizza, cheese);
		
		System.out.println("Menus Are");
		for (Integer docId : documentIds) {
			System.out.println("\t [" + docId + "] " + service.documentNames.get(docId));
		}
	}
	
	

}
