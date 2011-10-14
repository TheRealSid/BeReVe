package de.hdm.seCode.security.acl;

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
	public static Map<Object, Object> getInstancesFromACLFile(File aclFile)
			throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory
				.newInstance();
		DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
		Document doc = docBuilder.parse(aclFile);

		doc.getDocumentElement().normalize();

		Element instances = getFirstNamedElem(doc.getDocumentElement()
				.getChildNodes(), "instances");

		Map<Object, Object> ret = new HashMap<Object, Object>();

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
							if ("ref".equals(_attr.getAttributes()
									.getNamedItem("type").getTextContent())) {
								String attr_name = _attr.getAttributes()
										.getNamedItem("name").getTextContent();
								String attr_val = _attr.getTextContent();
								item.addReference(attr_name, attr_val);
							} else {
								String attr_name = _attr.getAttributes()
										.getNamedItem("name").getTextContent();
								String attr_val = _attr.getTextContent();
								item.addValue(attr_name, attr_val);
							}
						}
					}
				}
				try {
					Object obj = buildObject(item);
					ret.put(item.getId(), obj);
				} catch (Exception e) {
					e.printStackTrace();
				}
				// ret.put(item.getId(), item);
			}
		}
		Set<Object> ids = ret.keySet();
		for (Object id : ids) {
			System.out.println(ret.get(id));
		}
		return ret;
	}

	private static Object buildObject(ObjectEntity item)
			throws ClassNotFoundException, IllegalArgumentException,
			IllegalAccessException, InvocationTargetException,
			SecurityException, NoSuchMethodException, InstantiationException {
		Class clazz = Class.forName(item.getClazz());
		Set<String> keys = item.getValues().keySet();
		Object idObject = parseToInstance(item.getId());
		Method[] methods = clazz.getMethods();
		Constructor constructor = clazz.getConstructors()[0];
		Object obj = constructor.newInstance(idObject);
		for (String key : keys) {
			for (Method method : methods) {
				if (method.getName().toLowerCase()
						.equals(("set" + key.toLowerCase()))) {
					String value = item.getValues().get(key);
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
				return (int) d.intValue();
			else
				return d;
		} catch (Exception e) {
			return (String) value;
		}
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

	public static ArrayList<PermissionEntity> getPermissionsFromACLFile(
			File aclFile, Map<Object,Object> instanceList) throws ParserConfigurationException, SAXException,
			IOException, ClassNotFoundException {
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

				String targetClassString;
				PermissionEntity.Scope targetScope;
				if ("class".equals(targetScopeString)) {
					targetClassString = ((Element) permission.getElementsByTagName(
							"target").item(0)).getTextContent();
					targetScope = PermissionEntity.Scope.CLASS;
				} else {
					targetClassString = ((Element) ((Element) permission
							.getElementsByTagName("target").item(0))
							.getElementsByTagName("class").item(0))
							.getTextContent();
					targetScope = PermissionEntity.Scope.INSTANCE;
				}
				
				Class targetClass = Class.forName(targetClassString);

				String callerClassString;
				PermissionEntity.Scope callerScope;
				if ("class".equals(callerScopeString)) {
					callerClassString = ((Element) permission.getElementsByTagName(
							"caller").item(0)).getTextContent();
					callerScope = PermissionEntity.Scope.CLASS;
				} else {
					callerClassString = ((Element) ((Element) permission
							.getElementsByTagName("caller").item(0))
							.getElementsByTagName("class").item(0))
							.getTextContent();
					callerScope = PermissionEntity.Scope.INSTANCE;
				}
				
				Class callerClass = Class.forName(callerClassString);
				
				PermissionEntity item = new PermissionEntity(targetScope, callerScope, targetClass, callerClass, allMethods);

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
						Set<Object> keySet = instanceList.keySet();
						for(Object key : keySet) {
							if(instances.item(j).getTextContent().equals((String)key)) {
								//SecurityProxy bla = SecurityProxy.newInstance(obj, owner, interfaces)
							}
						}
						//item.addTargetInstanceID(instances.item(j).getTextContent());
					}
				}

				if (callerScope == PermissionEntity.Scope.INSTANCE) {
					NodeList instances = ((Element) permission
							.getElementsByTagName("caller").item(0))
							.getElementsByTagName("instanceID");
					for (int j = 0; j < instances.getLength(); j++) {
						//item.addCallerInstanceID((instances.item(j).getTextContent()));
					}
				}
				ret.add(item);
			}
		}
		return ret;
	}
}
