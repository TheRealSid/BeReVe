package security.acl;

import java.util.ArrayList;

public class PermissionEntity {
	public static enum Scope {
		INSTANCE, CLASS
	}
	
	private Scope targetScope;
	private Scope callerScope;
	private Class<?> targetClass;
	private Class<?> callerClass;
	private ArrayList<String> methods;
	private boolean allMethods;
	private ArrayList<Object> targetInstances;
	private ArrayList<Object> callerInstances;
	private boolean isGlobalContext;
	
	public PermissionEntity(Scope targetScope, Scope callerScope,
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

	public Scope getTargetScope() {
		return targetScope;
	}

	public Scope getCallerScope() {
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
