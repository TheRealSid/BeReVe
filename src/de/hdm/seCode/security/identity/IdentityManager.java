package de.hdm.seCode.security.identity;


import java.util.UUID;
import java.util.WeakHashMap;

import de.hdm.seCode.security.SecureProxy;
import de.hdm.seCode.security.acl.ACL;
import de.hdm.seCode.security.acl.XML2ACLParser;
import de.hdm.seCode.security.identity.IDObject.IDCheckResult;


public class IdentityManager {
	private IdentityHashMap<UUID, Object> idBase;
	private static IdentityManager im = null;
	private static ACL acl;
	public static IdentityManager getInstance(){
		if(im == null)
			im = new IdentityManager();
		return im;
	}
	private IdentityManager() {
		idBase = new IdentityHashMap<UUID, Object>();
		acl = ACL.getInstance();
	}
	
	public static Object getGlobalContext(Object id) {
		return acl.getGlobalObjectsList().get(id);
	}
	
	public static Object getSecureObject(Object id) {
		Object o = acl.getInstanceList().get(id);
		Class i;
		Object so = null;
		try {
			i = Class.forName(XML2ACLParser.class2interface(o.getClass().getName()));
			so = SecureProxy.newInstance(o, (IDObject) acl.getOwner(id), i);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return so;
	}
	
	public UUID addObject(Object o) {
		UUID id = UUID.randomUUID();
		idBase.put(id, o);
		return id;
	}
	
	public void removeObject(Object o) {
		idBase.remove(((IDObject)o).getId());
	}
	
	public IDCheckResult checkId(Object o) {
		if(!idBase.containsKey(((IDObject)o).getId()))
			return IDCheckResult.REMOVED;
		if(idBase.get(((IDObject)o).getId()) != o)
			return IDCheckResult.NOT_OK;
		return IDCheckResult.OK;
	}
	
	
	
	
	private  class IdentityHashMap<K, V> extends WeakHashMap<K, V> {
		
		private IdentityHashMap(){};
		
		@Override
		public V put(K key, V value) {
//			if(!Reflection.getCallerClass(3).getName().equals("secure.SecureContext"))
//				return null;
			return super.put(key, value);
		}
		
		@Override
		public V get(Object key) {
			return super.get(key);
		}
	}

}