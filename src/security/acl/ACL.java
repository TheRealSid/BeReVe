package security.acl;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import security.SecureInterface;
import security.SecurityCallback;
import world.Stadt;

public class ACL extends SecurityCallback {

	private ArrayList<ObjectEntity> instanceList;
	private ArrayList<PermissionEntity> permissionList;

	private static ACL acl;

	public static ACL getInstance() {
		if (acl == null) {
			acl = new ACL();
		}
		return acl;
	}

	public void parseACLFile() throws ParserConfigurationException, SAXException, IOException {
		instanceList = XML2ACLParser.getInstancesFromACLFile(new File("resource/acl.xml"));
		permissionList = XML2ACLParser.getPermissionsFromACLFile(new File("resource/acl.xml"));
	}

	private Map<Object, Permission> map = new HashMap<Object, Permission>();

	public boolean checkPermission(Method m, Object callee, Object caller) {
		return checkPermission(m.getName(), callee, caller);
	}

	public boolean checkPermission(String method, Object callee, Object caller) {
		if (caller instanceof Stadt)
			return true; // Work-a-round bis ACL infrastruktur steht
		if (map.get(callee) != null
				&& map.get(callee).getMethods().contains(method)
				&& map.get(callee).getCaller().equals(caller))
			return true;
		return false;
	}

	public void addPermission(Object a, Object b, String... methods) {
		map.put(a, new Permission(b, Arrays.asList(methods)));
	}

	public void addPermission(SecureInterface a, SecureInterface b,
			String... methods) {
		addPermission(a.getObject(this, createTan()),
				b.getObject(this, createTan()), methods);
	}

	private class Permission {
		private Object caller;
		private List<String> methods;

		private Permission(Object caller, List<String> methods) {
			this.setCaller(caller);
			this.methods = methods;
		}

		public List<String> getMethods() {
			return methods;
		}

		public void setMethods(List<String> methods) {
			this.methods = methods;
		}

		public Object getCaller() {
			return caller;
		}

		public void setCaller(Object caller) {
			this.caller = caller;
		}
	}
}
