package security.acl;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class XML2ACLParser {
	public static Map<Object,Object> getInstancesFromACLFile(File aclFile)
			throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory
				.newInstance();
		DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
		Document doc = docBuilder.parse(aclFile);

		doc.getDocumentElement().normalize();

		Element instances = getFirstNamedElem(doc.getDocumentElement()
				.getChildNodes(), "instances");

		Map<Object, Object> ret = new HashMap<Object,Object>();

		for (int i = 0; i < instances.getChildNodes().getLength(); i++) {
			if (instances.getChildNodes().item(i).getNodeType() != Document.ELEMENT_NODE)
				continue;
			Element instance = (Element) instances.getChildNodes().item(i);
			if ("object".equals(instance.getNodeName())) {
				String clazz = instance.getAttributes().getNamedItem("class")
						.getTextContent();
				String id = getFirstNamedElem(instance.getChildNodes(), "id")
						.getTextContent();
				ObjectEntity item = new ObjectEntity(clazz, id);
				for (int j = 0; j < instance.getChildNodes().getLength(); j++) {
					if (instance.getChildNodes().item(j).getNodeType() == Document.ELEMENT_NODE) {
						Element _attr = (Element) instance.getChildNodes()
								.item(j);
						if ("attr".equals(_attr.getNodeName())) {
							String attr_name = _attr.getAttributes()
									.getNamedItem("name").getTextContent();
							String attr_val = _attr.getTextContent();
							item.addAttribute(attr_name, attr_val);
						}
					}
				}
				try {
					Object obj = buildObject(item);
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SecurityException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (NoSuchMethodException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InstantiationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				ret.put(item.getId(),item);
			}
		}
		return ret;
	}

	private static Object buildObject(ObjectEntity item) throws ClassNotFoundException, IllegalArgumentException, IllegalAccessException, InvocationTargetException, SecurityException, NoSuchMethodException, InstantiationException {
		Class clazz  = Class.forName(item.getClazz());
		Set<String> keys = item.getAttributes().keySet();
		Method[] methods = clazz.getMethods();
		Constructor constructor = clazz.getConstructor();
		Object obj = constructor.newInstance();
		for(String key:keys){
			for(Method method:methods){
				if(method.getName().toLowerCase().equals(("set"+key.toLowerCase()))){
					String value = item.getAttributes().get(key);
					Object valueObj = parseToInstance(value);
					method.invoke(obj, valueObj);
				}
			}
		}
		return obj;
	}

	private static Object parseToInstance(String value) {
		try {
			Double d = Double.valueOf(value);
			if (d - Math.floor(d) == 0)
				return d.intValue();
			else return d;
		} catch (Exception e) {
			return value;
		}
	}

	public static ArrayList<PermissionEntity> getPermissionsFromACLFile(
			File aclFile) throws ParserConfigurationException, SAXException,
			IOException {
		DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory
				.newInstance();
		DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
		Document doc = docBuilder.parse(aclFile);

		doc.getDocumentElement().normalize();

		Element permissions = getFirstNamedElem(doc.getDocumentElement()
				.getChildNodes(), "permissions");

		ArrayList<PermissionEntity> ret = new ArrayList<PermissionEntity>();

		for (int i = 0; i < permissions.getChildNodes().getLength(); i++) {
			if (permissions.getChildNodes().item(i).getNodeType() != Document.ELEMENT_NODE)
				continue;
			Element permission = (Element) permissions.getChildNodes().item(i);
			if ("permission".equals(permission.getNodeName())) {

				String targetScopeString = ((Element) permission
						.getElementsByTagName("target").item(0))
						.getAttributes().getNamedItem("scope").getTextContent();
				String callerScopeString = ((Element) permission
						.getElementsByTagName("caller").item(0))
						.getAttributes().getNamedItem("scope").getTextContent();
				boolean allMethods = false;
				if ("*".equals(((Element) permission.getElementsByTagName(
						"methods").item(0)).getTextContent()))
					allMethods = true;

				String targetClass;
				PermissionEntity.Scope targetScope;
				if ("class".equals(targetScopeString)) {
					targetClass = ((Element) permission.getElementsByTagName(
							"target").item(0)).getTextContent();
					targetScope = PermissionEntity.Scope.CLASS;
				} else {
					targetClass = ((Element) ((Element) permission
							.getElementsByTagName("target").item(0))
							.getElementsByTagName("class").item(0))
							.getTextContent();
					targetScope = PermissionEntity.Scope.INSTANCE;
				}

				String callerClass;
				PermissionEntity.Scope callerScope;
				if ("class".equals(callerScopeString)) {
					callerClass = ((Element) permission.getElementsByTagName(
							"caller").item(0)).getTextContent();
					callerScope = PermissionEntity.Scope.CLASS;
				} else {
					callerClass = ((Element) ((Element) permission
							.getElementsByTagName("caller").item(0))
							.getElementsByTagName("class").item(0))
							.getTextContent();
					callerScope = PermissionEntity.Scope.INSTANCE;
				}

//				PermissionEntity item = new PermissionEntity(targetScope,
//						callerScope, targetClass, callerClass, allMethods);

				if (!allMethods) {
					NodeList methods = ((Element) permission
							.getElementsByTagName("methods").item(0))
							.getElementsByTagName("method");
					for (int j = 0; j < methods.getLength(); j++) {
						item.addMethod(methods.item(j).getTextContent());
					}
				}

				if (targetScope == PermissionEntity.Scope.INSTANCE) {
					NodeList instances = ((Element) permission
							.getElementsByTagName("target").item(0))
							.getElementsByTagName("instanceID");
					for (int j = 0; j < instances.getLength(); j++) {
						item.addTargetInstanceID(instances.item(j)
								.getTextContent());
					}
				}

				if (callerScope == PermissionEntity.Scope.INSTANCE) {
					NodeList instances = ((Element) permission
							.getElementsByTagName("caller").item(0))
							.getElementsByTagName("instanceID");
					for (int j = 0; j < instances.getLength(); j++) {
						item.addCallerInstanceID((instances.item(j)
								.getTextContent()));
					}
				}
				ret.add(item);
			}
		}
		return ret;
	}

	private static Element getFirstNamedElem(NodeList lst, String name) {
		for (int i = 0; i < lst.getLength(); i++) {
			if (name.equals(lst.item(i).getNodeName())
					&& lst.item(i).getNodeType() == Document.ELEMENT_NODE) {
				return (Element) lst.item(i);
			}
		}
		return null;
	}
}
