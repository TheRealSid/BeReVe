package security.acl;

import java.util.ArrayList;

public class PermissionEntity {
	public static enum scope {
		INSTANCE, CLASS
	}
	
	private scope targetScope;
	private scope callerScope;
	private String targetClass;
	private String callerClass;
	private ArrayList<String> methods;
	private boolean allMethods;
	private ArrayList<String> targetInstanceIDs;
	private ArrayList<String> callerInstanceIDs;
	
	public PermissionEntity(scope targetScope, scope callerScope,
			String targetClass, String callerClass, boolean allMethods) {
		this.targetScope = targetScope;
		this.callerScope = callerScope;
		this.targetClass = targetClass;
		this.callerClass = callerClass;
		this.allMethods = allMethods;
		
		methods = new ArrayList<String>();
		targetInstanceIDs = new ArrayList<String>();
		callerInstanceIDs = new ArrayList<String>();
	}
	
	public void addMethod(String methodName) {
		methods.add(methodName);
	}
	
	public void addTargetInstanceID(String id) {
		targetInstanceIDs.add(id);
	}
	
	public void addCallerInstanceID(String id) {
		callerInstanceIDs.add(id);
	}

	public scope getTargetScope() {
		return targetScope;
	}

	public scope getCallerScope() {
		return callerScope;
	}

	public String getTargetClass() {
		return targetClass;
	}

	public String getCallerClass() {
		return callerClass;
	}

	public ArrayList<String> getMethods() {
		return methods;
	}

	public boolean isAllMethods() {
		return allMethods;
	}

	public ArrayList<String> getTargetInstanceIDs() {
		return targetInstanceIDs;
	}

	public ArrayList<String> getCallerInstanceIDs() {
		return callerInstanceIDs;
	}
}
