package security;
import java.security.SecureRandom;
import java.util.Random;

import security.identity.IDObject;


public abstract class SecurityCallback extends IDObject{

	private Integer tan;
	private SecureRandom rnd;
	
	public SecurityCallback(){
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
