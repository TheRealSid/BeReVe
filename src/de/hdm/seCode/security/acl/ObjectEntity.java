package de.hdm.seCode.security.acl;

import java.util.HashMap;

public class ObjectEntity {
	private String clazz;
	private String id;
	private HashMap<String, String> values;
	private HashMap<String, String> references;
	
	public ObjectEntity(String clazz, String id) {
		this.clazz = clazz;
		this.id = id;
		values = new HashMap<String, String>();
		references = new HashMap<String, String>();
	}
	
	public void addValue(String key, String val) {
		values.put(key, val);
	}
	
	public void addReference(String key, String val) {
		references.put(key, val);
	}
	
	public String getClazz() {
		return clazz;
	}

	public String getId() {
		return id;
	}

	public HashMap<String, String> getValues() {
		return values;
	}
	
	public HashMap<String, String> getReferences() {
		return references;
	}
}
