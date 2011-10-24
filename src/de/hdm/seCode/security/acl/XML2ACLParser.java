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
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import de.hdm.seCode.example.world.SPerson;
import de.hdm.seCode.security.SecureInterface;
import de.hdm.seCode.security.SecureProxy;
import de.hdm.seCode.security.identity.IDObject;

public class XML2ACLParser {
	public static Map<String, Object> getInstancesFromACLFile(File aclFile)
			throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory
				.newInstance();
		DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
		Document doc = docBuilder.parse(aclFile);

		doc.getDocumentElement().normalize();

		Element instances = getFirstNamedElem(doc.getDocumentElement()
				.getChildNodes(), "instances");

		Map<Object, Object> ret_objects = new HashMap<Object, Object>();
		Map<Object, Object> global_objects = new HashMap<Object, Object>();
		Map<Object, Object> refs = new HashMap<Object, Object>();
		Map<Object, String> owners = new HashMap<Object, String>();
		Map<Object, ObjectEntity> raw_obj = new HashMap<Object, ObjectEntity>();
		
		Map<String, Object> data = new HashMap<String, Object>();
		

		for (int i = 0; i < instances.getChildNodes().getLength(); i++) {
			if (instances.getChildNodes().item(i).getNodeType() != Document.ELEMENT_NODE)
				continue;
			Element instance = (Element) instances.getChildNodes().item(i);
			if ("object".equals(instance.getNodeName())) {
				boolean objIsGlobalContext = false;
				String clazz = instance.getAttributes().getNamedItem("class")
						.getTextContent();
				Node globalContext = instance.getAttributes().getNamedItem("globalContext");
				if(globalContext != null) {
					String globalContextVal = globalContext.getTextContent();
					if ("true".equals(globalContextVal)) {
						objIsGlobalContext = true;
					}
				}
				String id = getFirstNamedElem(instance.getChildNodes(), "id")
						.getTextContent();
				String owner = getFirstNamedElem(instance.getChildNodes(), "owner")
						.getTextContent();
				ObjectEntity item = new ObjectEntity(clazz, id, owner);
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
					if (objIsGlobalContext) {
						global_objects.put(item.getId(), obj);
					}
					ret_objects.put(item.getId(), obj);
					refs.put(item.getId(), item.getReferences());
					owners.put(item.getId(), item.getOwner());
					raw_obj.put(item.getId(), item);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		data.put("objects", ret_objects);
		data.put("global_objects", global_objects);
		data.put("refs", refs);
		data.put("owners", owners);
		data.put("raw_objects", raw_obj);
		data.put("owner_objects", new HashMap<Object, Object>());
		try {
			addReferences(data);
			setOwners(data);
			data.put("secureInterfaces", createSecureInterfacesFromData(data));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return data;
	}
	
	private static HashMap<Object, SecureInterface> createSecureInterfacesFromData(Map<String, Object> data) {
		HashMap<Object, SecureInterface> ret = new HashMap<Object, SecureInterface>();
		HashMap<Object, Object> objects = (HashMap<Object, Object>) data.get("objects");
		Map<Object, Object> ownerObj = (Map<Object, Object>) data.get("owner_objects");
		Set<Object> objKeys = objects.keySet();
		for (Object objKey : objKeys) {
			Object owner = ownerObj.get(objKey);
			Object obj = objects.get(objKey);
			String className = obj.getClass().getName();
			SecureInterface secObj = null;
			Class interfaceClass;
			try {
				interfaceClass = Class.forName(class2interface(className));
				secObj = (SecureInterface) SecureProxy.newInstance(obj, (IDObject) owner, new Class[]{interfaceClass});
			} catch (ClassNotFoundException e) {
				//e.printStackTrace();
			}
			ret.put(objKey, secObj);
		}
		return ret;
	}
	
	private static void setOwners(Map<String, Object> data) {
		Map<Object, String> ownerMap = (Map<Object, String>) data.get("owners");
		Map<Object, Object> ownerObj = (Map<Object, Object>) data.get("owner_objects");
		Set<Object> owner_keys = ownerMap.keySet();
		for (Object owner_key : owner_keys) {
			Object owner = ((Map<Object, Object>) data.get("objects")).get(ownerMap.get(owner_key));
			ownerObj.put(owner_key, owner);
		}
	}
	
	private static void addReferences(Map<String, Object> data) throws ClassNotFoundException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		Set<Object> ref_keys = ((Map<Object, Object>) data.get("refs")).keySet();
		for (Object ref_key : ref_keys) {
			Object refOwner = ((Map<Object, Object>)data.get("objects")).get(ref_key);
			Map<String, String> thisRefs = (Map<String, String>) ((Map<Object, Object>) data.get("refs")).get(ref_key);
			Set<String> thisRefsKeys = thisRefs.keySet();
			for (String thisRefKey : thisRefsKeys) {
				Map<Object, ObjectEntity> raw_objs = (Map<Object, ObjectEntity>) data.get("raw_objects");
				Set<Object> raw_objs_keys = raw_objs.keySet();
				for (Object raw_objs_key : raw_objs_keys) {
					if(((ObjectEntity) raw_objs.get(raw_objs_key)).getId().equals(thisRefs.get(thisRefKey))) {
						Class refOwnerclazz = Class.forName(((ObjectEntity) raw_objs.get(raw_objs_key)).getClazz());
						Method[] refOwnerMethods = refOwnerclazz.getMethods();
						for (Method refOwnerMethod : refOwnerMethods) {
							if (refOwnerMethod.getName().toLowerCase().equals(("set" + thisRefKey.toLowerCase()))) {
								Object obj = ((Map<Object, Object>)data.get("objects")).get(raw_objs_key);
								Object objOwner = getObjectOwner(raw_objs_key, data);
								String clazzName = obj.getClass().getName();
								String interfaceName = class2interface(clazzName);
								String secName = class2interface(clazzName);
								Class interfaceClazz = Class.forName(interfaceName);
								Object SObj = SecureProxy.newInstance(obj, (IDObject) objOwner, new Class[]{interfaceClazz});
								refOwnerMethod.invoke(refOwner, SObj);
							}
						}
					}
				}
			}
		}
	}
	
	private static Object getObjectOwner(Object objKey, Map<String, Object> data) {
		Map<Object, String> owners = (Map<Object, String>) data.get("owners");
		Set<Object> owners_keys = owners.keySet();
		for (Object owner_key : owners_keys) {
			if(owner_key.equals(objKey)) {
				return ((Map<Object, Object>)data.get("objects")).get(owners.get(owner_key));
			}
		}
		return null;
	}
	
	public static String class2interface(String className) {
		String[] tokens = className.split("\\.");
		tokens[tokens.length-1] = "S" + tokens[tokens.length-1];
		String ret = "";
		for (String token : tokens) {
			ret += token + ".";
		}
		return ret.substring(0, ret.length()-1);
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
			File aclFile, Map<String,Object> data) throws ParserConfigurationException, SAXException,
			IOException, ClassNotFoundException {
		Map<Object, Object> instanceList = (Map<Object, Object>) data.get("objects");
		
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
						Method[] realMethods = targetClass.getMethods();
						for (Method realMethod : realMethods) {
							if (methods.item(j).getTextContent().toLowerCase().equals(realMethod.getName().toLowerCase())) {
								//item.addMethod(realMethod);
								break;
							}
						}
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
								Map<Object, ObjectEntity> raw_objs = (Map<Object, ObjectEntity>) data.get("raw_objects");
								Set<Object> raw_objs_keys = raw_objs.keySet();
								Object target_raw_key = null;
								for (Object raw_objs_key : raw_objs_keys) {
									if(raw_objs_key.equals(key)) {
										target_raw_key = raw_objs_key;
									}
								}
								Object obj = instanceList.get(key);
								if(target_raw_key != null) {
									Object owner = getObjectOwner(target_raw_key, data);
									String clazzName = obj.getClass().getName();
									String interfaceName = class2interface(clazzName);
									Class interfaceClazz = Class.forName(interfaceName);
									SecureInterface SObj = (SecureInterface) SecureProxy.newInstance(obj, (IDObject) owner, new Class[]{interfaceClazz});
									item.addTarget(SObj);
								}
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
						Set<Object> keySet = instanceList.keySet();
						for(Object key : keySet) {
							if(instances.item(j).getTextContent().equals((String)key)) {
								item.addCaller(instanceList.get(key));
							}
						}
					}
				}
				ret.add(item);
			}
		}
		return ret;
	}
}
