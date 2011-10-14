package de.hdm.seCode.example.world.bank.security;

import de.hdm.seCode.security.SecureCallback;

public interface SKonto {
	public void setGeld(Integer geld, SecureCallback callback,Integer tan );

	public Integer getGeld(SecureCallback callback, Integer tan) ;
}
