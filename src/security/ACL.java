package security;

import java.lang.reflect.Method;

import security.identity.IDObject;

public class ACL {

	private static ACL acl;
	public static ACL getInstance(){
		if(acl == null) acl = new ACL();
		return acl;
	}
	public boolean checkPermission(Method m, IDObject caller){
		return true;
	}
}
