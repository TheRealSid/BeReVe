package de.hdm.seCode.security;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;

import de.hdm.seCode.security.acl.ACL;
import de.hdm.seCode.security.identity.IDObject;


public class SecureProxy implements InvocationHandler {

	private final Object obj;
	private final IDObject owner;

	public SecureProxy(Object obj, IDObject owner) {
		this.obj = obj;
		this.owner = owner;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] aobj)
			throws Exception {
		if(aobj == null){
			Method realMethod = obj.getClass().getMethod(method.getName());
			return realMethod.invoke(obj);
		}else if(aobj.length==1){
			if(method.getName().equals("equals")){
				return obj.equals(aobj[0]);
			}
		}
		
		SecureCallback caller = (SecureCallback) aobj[aobj.length - 2];
		Integer tan = (Integer) aobj[aobj.length - 1];
		Object[] args = Arrays.copyOfRange(aobj, 0, aobj.length - 2);
		
		
		if(method.getName().equals("getObject") && ACL.getInstance().equals(caller)){
			return obj;
		}
		if(method.getName().equals("isOwner") && ACL.getInstance().equals(caller)){
			return owner.equals(args[0]);
		}
		
		if(owner.equals(caller) || ACL.getInstance().checkPermission(method,obj,caller)){
			String methodName = method.getName();
			Class[] types = Arrays.copyOfRange(method.getParameterTypes(),0,aobj.length-2);
			
			Method realMethod = obj.getClass().getMethod(methodName,
					types);
			Integer callbackTan = caller.getTan();
			if (callbackTan == null || !callbackTan.equals(tan))
				throw new SecureObjectException();
			Object ret = realMethod.invoke(obj, args);
			return ret;
		}
		 else
			throw new SecureObjectException();
	}

	static public Object newInstance(Object obj, IDObject owner,
			Class... interfaces) {
		return Proxy.newProxyInstance(obj.getClass().getClassLoader(),
				interfaces, new SecureProxy(obj, owner));
	}

}
