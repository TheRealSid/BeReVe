package de.hdm.seCode.security;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import de.hdm.seCode.example.world.Person;
import de.hdm.seCode.example.world.SPerson;
import de.hdm.seCode.security.acl.ACL;
import de.hdm.seCode.security.acl.XML2ACLParser;
import de.hdm.seCode.security.identity.IDObject;
import de.hdm.seCode.security.identity.IdentityManager;
import de.hdm.seCode.security.identity.IDObject.IDCheckResult;

public class SecureContext {
	static IdentityManager idM;
	static ACL acl;
	
	private static void initACL() {
		if(acl == null) {
			acl = ACL.getInstance();
			try {
				acl.parseACLFile();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

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
	
	public static Object getGlobalContext(Object id) {
		initACL();
		return acl.getGlobalObjectsList().get(id);
	}
	
	public static Object getSecureObject(Object id) {
		initACL();
		Object o = acl.getInstanceList().get(id);
		Class i;
		Object so = null;
		try {
			i = Class.forName(XML2ACLParser.class2interface(o.getClass().getName()));
			so = SecureProxy.newInstance(o, (IDObject) acl.getOwner(o), i);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return so;
	}

	public static void removeObject(Object o) {
		idM.removeObject(o);
	}

	public static IDCheckResult checkId(Object o) {
		return idM.checkId(o);
	}

}
