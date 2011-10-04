package security.acl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class XML2ACLParser {
	public static ArrayList<ObjectEntity> getInstancesFromACLFile(File aclFile)
			throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory
				.newInstance();
		DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
		Document doc = docBuilder.parse(aclFile);

		doc.getDocumentElement().normalize();

		Element instances = getFirstNamedElem(doc.getDocumentElement()
				.getChildNodes(), "instances");

		ArrayList<ObjectEntity> ret = new ArrayList<ObjectEntity>();

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
				ret.add(item);
			}
		}
		return ret;
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
				PermissionEntity.scope targetScope;
				if ("class".equals(targetScopeString)) {
					targetClass = ((Element) permission.getElementsByTagName(
							"target").item(0)).getTextContent();
					targetScope = PermissionEntity.scope.CLASS;
				} else {
					targetClass = ((Element) ((Element) permission
							.getElementsByTagName("target").item(0))
							.getElementsByTagName("class").item(0))
							.getTextContent();
					targetScope = PermissionEntity.scope.INSTANCE;
				}

				String callerClass;
				PermissionEntity.scope callerScope;
				if ("class".equals(callerScopeString)) {
					callerClass = ((Element) permission.getElementsByTagName(
							"caller").item(0)).getTextContent();
					callerScope = PermissionEntity.scope.CLASS;
				} else {
					callerClass = ((Element) ((Element) permission
							.getElementsByTagName("caller").item(0))
							.getElementsByTagName("class").item(0))
							.getTextContent();
					callerScope = PermissionEntity.scope.INSTANCE;
				}

				PermissionEntity item = new PermissionEntity(targetScope,
						callerScope, targetClass, callerClass, allMethods);

				if (!allMethods) {
					NodeList methods = ((Element) permission
							.getElementsByTagName("methods").item(0))
							.getElementsByTagName("method");
					for (int j = 0; j < methods.getLength(); j++) {
						item.addMethod(methods.item(j).getTextContent());
					}
				}

				if (targetScope == PermissionEntity.scope.INSTANCE) {
					NodeList instances = ((Element) permission
							.getElementsByTagName("target").item(0))
							.getElementsByTagName("instanceID");
					for (int j = 0; j < instances.getLength(); j++) {
						item.addTargetInstanceID(instances.item(j)
								.getTextContent());
					}
				}

				if (callerScope == PermissionEntity.scope.INSTANCE) {
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
