package security.acl;

import java.util.ArrayList;

public class PermissionEntity {
	public static enum targetScope {
		INSTANCE, CLASS
	}
	
	private targetScope scope;
	private String clazz;
	private ArrayList<String> methods;
	private ArrayList<String> instanceIDs;
	private boolean allMethods;
	
	public PermissionEntity(targetScope scope, String clazz) {
		this.scope = scope;
		this.clazz = clazz;
		methods = new ArrayList<String>();
		instanceIDs = new ArrayList<String>();
	}
	
	void setAllMethods(boolean allMethods) {
		this.allMethods = allMethods;
	}
	
	public void addMethod(String methodName) {
		methods.add(methodName);
	}
	
	public void addinstanceID(String id) {
		instanceIDs.add(id);
	}
	
	public targetScope getScope() {
		return scope;
	}
	
	public String getClazz() {
		return clazz;
	}
	
	public ArrayList<String> getMethods() {
		return methods;
	}
	
	public ArrayList<String> getInstanceIDs() {
		return instanceIDs;
	}
	
	public boolean getAllMethods() {
		return allMethods;
	}
}