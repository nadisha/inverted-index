package org.ir.invertedindex;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.stream.Collectors;

public class InvertedIndexService {
	public Map<Integer, String> documentNames = new HashMap<>();
	
	/**
	 * Search the document ids which consist both terms  
	 * @param firstTermPosting
	 * @param secondTermPosting
	 * @return list of document ids which match both terms
	 */
	public List<Integer> searchByTwoTerms(List<Integer> firstTermPosting, List<Integer> secondTermPosting) {
		int firstPointer = 0;
		int secondPointer = 0;
		
		List<Integer> answer = new ArrayList<>();
		
		while ( ( firstTermPosting != null ) && ( secondTermPosting != null ) ) {
 			int firstTermPostingDocId = 0;
 			int secondTermPostingDocId = 0;
 			
 			try {
 				firstTermPostingDocId = firstTermPosting.get( firstPointer );
 			} catch ( IndexOutOfBoundsException ex ) {
 				firstTermPosting = null;
 			}
 			
 			try {
 				secondTermPostingDocId = secondTermPosting.get( secondPointer );
 			} catch ( IndexOutOfBoundsException ex ) {
 				secondTermPosting = null;
 			}
 			
			if ( firstTermPostingDocId == secondTermPostingDocId ) {
				answer.add( firstTermPostingDocId );
				firstPointer++;
				secondPointer++;
			} else if ( firstTermPostingDocId < secondTermPostingDocId) {
				firstPointer++;
			} else {
				secondPointer++;
			}
		}
		
		return answer;
	}
	
	public Map<String, Dictionary> buildIndex(String...fileNames) {
		List<Document> documents = collectDocuments(fileNames);
		List<Term> unsortedTerms = generateTermsCollection(documents);
		List<Term> sortedTerms = sortByTermAndDocId(unsortedTerms);
		List<Term> distinctTerms = distinctTerms(sortedTerms);
		Map<String, Dictionary> invertedIndexer = generateInvertedIndex(distinctTerms);		
		return invertedIndexer;
	}
	
	private List<Term> generateTermsCollection(List<Document> documents) {
		List<Term> terms = new ArrayList<>();
		for (Document doc : documents) {
			terms.addAll(tokernize(doc.getData(), doc.getDocId()));
		}
		return terms;
	}		
	
	private List<Term> sortByTermAndDocId(List<Term> terms) {		
		terms.sort(new Comparator<Term>() {
			@Override
			public int compare(Term t1, Term t2) {
				return Comparator.comparing(Term::getTerm).thenComparingInt(Term::getDocId).compare(t1, t2);
			}
		});
		return terms;
	}
	
	private List<Term> distinctTerms(List<Term> terms) {
		return terms.stream().distinct().collect(Collectors.toList());
	}
	
	private Map<String, Dictionary> generateInvertedIndex(List<Term> terms) {
		Map<String, List<Integer>> result = terms.stream().collect(Collectors.groupingBy(Term::getTerm, Collectors.mapping(Term::getDocId, Collectors.toList())));
		
		Map<String, Dictionary> termDictionary = new HashMap<>();
		for(Map.Entry<String, List<Integer>> element : result.entrySet()) {
			String key = element.getKey();
			List<Integer> value = element.getValue();
			Dictionary dicElement = new Dictionary(key, value.size(), value);
			termDictionary.put(key, dicElement);
		}
		
		return termDictionary;
	}
	
	private List<Term> tokernize(String data, int docId) {
		StringTokenizer tokenizer = new StringTokenizer(data, "\t -");
		List<Term> tokens = new ArrayList<>();
		Collections.list(tokenizer).stream().forEach(t -> {
			String token = (String) t;
			token = token.toLowerCase();
			
			// Remove commas
			token = token.replace(",", "");
			// Remove (
			token = token.replace("(", "");
			// Remove )
			token = token.replace(")", "");
			// Remove [
			token = token.replace("[", "");
			// Remove ]
			token = token.replace("]", "");
			boolean goodToAdd = true;
			
			// Ignore price
			if (token.startsWith("$")) {
				goodToAdd = false;
			}
			
			// Ignore common words
			if (token.equals("or") || token.equals("and")){
				goodToAdd = false;
			}
			
			// Ignore empty
			if (token.length() == 0) {
				goodToAdd = false;
			}
			
			if (goodToAdd) {
				tokens.add(new Term(docId, token));
			}
		});
		return tokens;
	}
	
	
	private List<Document> collectDocuments(String...fileNames) {
		int docId = 1;
		List<Document> documents = new ArrayList<>();
		for (String fName : fileNames) {
			try {
				List<String> lines = Files.readAllLines(Paths.get("resource/" + fName));
				StringBuffer content = new StringBuffer();
				lines.stream().forEach(l -> content.append(l + " "));
				documents.add(new Document(docId, lines.toString(), fName));
				documentNames.put(docId, fName);
				docId++;
			} catch (IOException e) {
				e.printStackTrace();
			}						
		}
		return documents;
	}
}
