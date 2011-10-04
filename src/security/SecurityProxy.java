package security;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;

import security.acl.ACL;
import security.identity.IDObject;

public class SecurityProxy implements InvocationHandler {

	private final Object obj;
	private final IDObject owner;

	public SecurityProxy(Object obj, IDObject owner) {
		this.obj = obj;
		this.owner = owner;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] aobj)
			throws Exception {
		if(aobj == null){
			Method realMethod = obj.getClass().getMethod(method.getName());
			return realMethod.invoke(obj);
		}
		
		SecurityCallback caller = (SecurityCallback) aobj[aobj.length - 2];
		Integer tan = (Integer) aobj[aobj.length - 1];
		Object[] args = Arrays.copyOfRange(aobj, 0, aobj.length - 2);
		
		
		if(method.getName().equals("getObject") && ACL.getInstance().equals(caller)){
			return obj;
		}
		
		if(owner.equals(caller) || ACL.getInstance().checkPermission(method,obj,caller)){
			String methodName = method.getName();
			Class[] types = Arrays.copyOfRange(method.getParameterTypes(),0,aobj.length-2);
			
			Method realMethod = obj.getClass().getMethod(methodName,
					types);
			Integer callbackTan = caller.getTan();
			if (callbackTan == null || !callbackTan.equals(tan))
				throw new SecurityObjectException();
			Object ret = realMethod.invoke(obj, args);
			return ret;
		}
		 else
			throw new SecurityObjectException();
	}

	static public Object newInstance(Object obj, IDObject owner,
			Class... interfaces) {
		return Proxy.newProxyInstance(obj.getClass().getClassLoader(),
				interfaces, new SecurityProxy(obj, owner));
	}

}
