package security;

import security.identity.IDObject;
import security.identity.IdentityManager;
import security.identity.IDObject.IDCheckResult;

public class SecureContext {
	static IdentityManager idM;
	
	public static IDObject createObject(ObjectCreator<?> oc) {
		IDObject o = oc.createObject();
		if(idM == null) {
			idM = new IdentityManager();
		}
		o.setId(idM.addObject(o));
		return o;
	}
	
	public static void removePerson(Person p) {
		idM.removeObject(p);
	}
	
	public static IDCheckResult checkId(Object o) {
		return idM.checkId(o);
	}
	
}
