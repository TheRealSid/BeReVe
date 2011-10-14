package de.hdm.seCode.security;

import de.hdm.seCode.security.acl.ACL;
import de.hdm.seCode.security.identity.IDObject;
import de.hdm.seCode.security.identity.IdentityManager;
import de.hdm.seCode.security.identity.IDObject.IDCheckResult;

public class SecureContext {
	static IdentityManager idM;

	public static IDObject createObject(ObjectCreator<?> oc) {
		IDObject o = oc.createObject();
		if (idM == null) {
			idM = new IdentityManager();
			try {
				ACL.getInstance().parseACLFile();
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}
		o.setId(idM.addObject(o));
		return o;
	}

	public static void removeObject(Object o) {
		idM.removeObject(o);
	}

	public static IDCheckResult checkId(Object o) {
		return idM.checkId(o);
	}

}
