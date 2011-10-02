package security.acl;

import java.util.HashMap;

public class ObjectEntity {
	private String clazz;
	private String id;
	private HashMap<String, String> attributes;
	
	public ObjectEntity(String clazz, String id) {
		this.clazz = clazz;
		this.id = id;
		attributes = new HashMap<String, String>();
	}
	
	public void addAttribute(String key, String val) {
		attributes.put(key, val);
	}
	
	public String getClazz() {
		return clazz;
	}

	public String getId() {
		return id;
	}

	public HashMap<String, String> getAttributes() {
		return attributes;
	}
}
