package bank.security;

import security.SecurityCallback;

public interface SKonto {
	public void setGeld(Integer geld, SecurityCallback callback,Integer tan );

	public Integer getGeld(SecurityCallback callback, Integer tan) ;
}
