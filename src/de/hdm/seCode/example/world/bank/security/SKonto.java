package de.hdm.seCode.example.world.bank.security;

import de.hdm.seCode.security.SecurityCallback;

public interface SKonto {
	public void setGeld(Integer geld, SecurityCallback callback,Integer tan );

	public Integer getGeld(SecurityCallback callback, Integer tan) ;
}
