package de.hdm.seCode.security;
import java.security.SecureRandom;

import de.hdm.seCode.security.identity.IDObject;



public abstract class SecureCallback extends IDObject{

	private Integer tan;
	private SecureRandom rnd;
	
	public SecureCallback(){
		rnd = new SecureRandom();
	}
	protected Integer createTan(){
		tan = rnd.nextInt();
		return tan;
	}
	public Integer getTan(){
		Integer value = tan;
		tan = null;
		return value;
	}
}
