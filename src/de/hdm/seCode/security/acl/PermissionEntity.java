package de.hdm.seCode.security.acl;

import java.util.ArrayList;
import java.util.List;

import de.hdm.seCode.security.SecureInterface;


public final class  PermissionEntity {
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

	public PermissionEntity addMethod(String method) {
		methods.add(method);
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
	
	@Override
	public int hashCode() {
		return 31;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PermissionEntity other = (PermissionEntity) obj;
		if (allMethods != other.allMethods)
			return false;
		if (callerClass == null) {
			if (other.callerClass != null)
				return false;
		} else if (!callerClass.equals(other.callerClass))
			return false;
		if (callerInstances == null) {
			if (other.callerInstances != null)
				return false;
		} else if (!callerInstances.equals(other.callerInstances))
			return false;
		if (callerScope != other.callerScope)
			return false;
		if (methods == null) {
			if (other.methods != null)
				return false;
		} else if (!methods.equals(other.methods))
			return false;
		if (targetClass == null) {
			if (other.targetClass != null)
				return false;
		} else if (!targetClass.equals(other.targetClass))
			return false;
		if (targetInstances == null) {
			if (other.targetInstances != null)
				return false;
		} else if (!targetInstances.equals(other.targetInstances))
			return false;
		if (targetScope != other.targetScope)
			return false;
		return true;
	}
	
	


	
	
}
