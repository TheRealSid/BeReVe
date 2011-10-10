package security.acl;

import java.util.ArrayList;
import java.util.List;

import security.SecureInterface;

public class PermissionEntity {
	public static enum Scope {
		INSTANCE, CLASS
	}
	
	private Scope targetScope;
	private Scope callerScope;
	private Class<?> targetClass;
	private Class<?> callerClass;
	private List<String> methods;
	private boolean allMethods;
	private List<SecureInterface> targetInstances;
	private List<Object> callerInstances;
	private boolean isGlobalContext;
	
	public PermissionEntity(Scope targetScope, Scope callerScope,
			Class<?> targetClass, Class<?> callerClass, boolean allMethods, List<String> methods, List<SecureInterface> targets, List<Object> caller) {
		this.targetScope = targetScope;
		this.callerScope = callerScope;
		this.targetClass = targetClass;
		this.callerClass = callerClass;
		this.allMethods = allMethods;
		
		this.methods = methods;
		targetInstances = targets;
		callerInstances = caller;
	}
	public PermissionEntity(Scope targetScope, Scope callerScope,
			Class<?> targetClass, Class<?> callerClass, boolean allMethods) {
		this(targetScope, callerScope, targetClass, callerClass, allMethods, new ArrayList<String>(), new ArrayList<SecureInterface>(), new ArrayList<Object>());
	}
	
	public PermissionEntity() {
		// TODO Auto-generated constructor stub
	}

	public PermissionEntity addMethod(String methodName) {
		methods.add(methodName);
		return this;
	}
	public PermissionEntity addTarget(SecureInterface target) {
		targetInstances.add(target);
		return this;
	}
	public PermissionEntity addCaller(Object caller) {
		callerInstances.add(caller);
		return this;
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

	public List<String> getMethods() {
		return methods;
	}

	public boolean isAllMethods() {
		return allMethods;
	}

	public List<SecureInterface> getTargetInstances() {
		return targetInstances;
	}

	public List<Object> getCallerInstances() {
		return callerInstances;
	}
	
	public void setGlobalContext(boolean isGlobalContext) {
		this.isGlobalContext = isGlobalContext;
	}
	
	public boolean isGlobalContext() {
		return isGlobalContext;
	}
}
