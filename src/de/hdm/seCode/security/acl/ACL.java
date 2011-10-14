package de.hdm.seCode.security.acl;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import de.hdm.seCode.security.SecureCallback;
import de.hdm.seCode.security.SecureInterface;
import de.hdm.seCode.security.acl.PermissionEntity.Scope;

public class ACL extends SecureCallback {

	private Map<Object,Object> instanceList;
	private ArrayList<PermissionEntity> permissionList = new ArrayList<PermissionEntity>();

	private static ACL acl;
	private ACL() {

		}
	public static ACL getInstance() {
		if (acl == null) {
			acl = new ACL();
		}
		return acl;
	}

	public void parseACLFile() throws ParserConfigurationException, SAXException, IOException {
		instanceList = XML2ACLParser.getInstancesFromACLFile(new File("resource/acl.xml"));
		try {
			permissionList = XML2ACLParser.getPermissionsFromACLFile(new File("resource/acl.xml"), instanceList);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}


	public boolean checkPermission(Method m, Object callee, Object caller) {
		return checkPermission(m.getName(), callee, caller);
	}

	private Set<PermissionEntity> findPermissionWithRawObject(String method, Object callee, List<PermissionEntity> permissionList){
		Set<PermissionEntity> result = new HashSet<PermissionEntity>();
		for(PermissionEntity entity: permissionList){
			if(entity.getTargetScope()==Scope.INSTANCE){
				if(secureInterfaces2Objects(entity.getTargetInstances()).contains(callee)){
					if(entity.isAllMethods()){
						result.add(entity);
					} else if(entity.getMethods().contains(method)){
						result.add(entity);
					}
				}
			} 
		}
		return result;
	}
	
	private Set<Object> secureInterfaces2Objects(List<SecureInterface> secureInterfaces){
		Set<Object> objects = new HashSet<Object>();
		for(SecureInterface si:secureInterfaces){
			objects.add(si.getObject(this, createTan()));
		}
		return objects;
	}

	private Set<PermissionEntity> findPermission(String method, Object callee, List<PermissionEntity> permissionList){
		Set<PermissionEntity> result = findPermissionWithRawObject( method, callee, permissionList);
		for(PermissionEntity entity: permissionList){
			if(entity.getTargetScope() == Scope.INSTANCE){
				if(entity.getTargetInstances().contains(callee)){
					if(entity.isAllMethods()){
						result.add(entity);
					} else if(entity.getMethods().contains(method)){
						result.add(entity);
					}
				}
			} else if(entity.getTargetScope() == Scope.CLASS){
				if(entity.getTargetClass().equals(callee.getClass())){
					if(entity.isAllMethods()){
						result.add(entity);
					} else if(entity.getMethods().contains(method)){
						result.add(entity);
					}
				}
			}
		}
		return result;
	}
	
	public boolean checkPermission(String method, Object callee, Object caller) {
		Set<PermissionEntity> permissions = findPermission(method, callee, permissionList);
		for(PermissionEntity entity:permissions){
			if(entity.getCallerScope() == Scope.INSTANCE){
				if(entity.getCallerInstances().contains(caller)) return true;
			}
			else if(entity.getCallerScope() == Scope.CLASS){
				if(entity.getCallerClass().equals(caller.getClass())) return true;
			}
		}
		return false;
		

	}

	public boolean addPermission(PermissionEntity entity, Integer tan, SecureCallback caller) {
		if(tan.equals(caller.getTan())){
			for(SecureInterface target: entity.getTargetInstances()){
				if(!target.isOwner(caller,this, createTan())){
					return false;
				}
			}
			permissionList.add(entity);
			return true;
		}
		return false;
	}


	
}
