package de.hdm.seCode.security.identity;


import java.util.UUID;
import java.util.WeakHashMap;

import de.hdm.seCode.security.identity.IDObject.IDCheckResult;

import sun.reflect.Reflection;

public class IdentityManager {
	private IdentityHashMap<UUID, Object> idBase;
	
	public IdentityManager() {
		idBase = new IdentityHashMap<UUID, Object>();
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