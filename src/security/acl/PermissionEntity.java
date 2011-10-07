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
			Class<?> targetClass, Class<?> callerClass, boolean allMethods) {
		this.targetScope = targetScope;
		this.callerScope = callerScope;
		this.targetClass = targetClass;
		this.callerClass = callerClass;
		this.allMethods = allMethods;
		
		methods = new ArrayList<String>();
		targetInstances = new ArrayList<Object>();
		callerInstances = new ArrayList<Object>();
	}
	
	public void addMethod(String methodName) {
		methods.add(methodName);
	}
	
	public void addTargetInstanceID(String id) {
		targetInstances.add(id);
	}
	
	public void addCallerInstanceID(String id) {
		callerInstances.add(id);
	}

	public Scope getTargetScope() {
		return targetScope;
	}

	public Scope getCallerScope() {
		return callerScope;
	}

	public Class<?> getTargetClass() {
		return targetClass;
	}

	public Class<?> getCallerClass() {
		return callerClass;
	}

	public ArrayList<String> getMethods() {
		return methods;
	}

	public boolean isAllMethods() {
		return allMethods;
	}

	public ArrayList<Object> getTargetInstanceIDs() {
		return targetInstances;
	}

	public ArrayList<Object> getCallerInstanceIDs() {
		return callerInstances;
	}
	
	public void setGlobalContext(boolean isGlobalContext) {
		this.isGlobalContext = isGlobalContext;
	}
	
	public boolean isGlobalContext() {
		return isGlobalContext;
	}
}
