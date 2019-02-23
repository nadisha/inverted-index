package org.ir.invertedindex;

public class Document {
	private int docId;
	
	private String data;
	
	private String name;

	public Document(int id, String content, String name) {
		this.docId = id;
		this.data = content;
		this.name = name;
	}
	
	public int getDocId() {
		return docId;
	}

	public void setDocId(int docId) {
		this.docId = docId;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "Document [docId=" + docId + ", data=" + data + ", name=" + name + "]";
	}	
	
}
