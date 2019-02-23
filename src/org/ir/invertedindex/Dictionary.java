package org.ir.invertedindex;

import java.util.List;

public class Dictionary {
	private String term;
	
	private int docFrequency;
	
	private List<Integer> posting;

	public Dictionary(String t, int freq, List<Integer> posting) {
		this.term = t;
		this.docFrequency = freq;
		this.posting = posting;
	}
	
	public String getTerm() {
		return term;
	}

	public void setTerm(String term) {
		this.term = term;
	}

	public int getDocFrequency() {
		return docFrequency;
	}

	public void setDocFrequency(int docFrequency) {
		this.docFrequency = docFrequency;
	}

	public List<Integer> getPosting() {
		return posting;
	}

	public void setPosting(List<Integer> posting) {
		this.posting = posting;
	}

	@Override
	public String toString() {
		return "[term=" + term + ", docFrequency=" + docFrequency + ", posting=" + posting + "]\n";
	}
	
}
