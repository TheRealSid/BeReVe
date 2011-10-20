package de.hdm.seCode.example.world;

import de.hdm.seCode.example.world.bank.security.SKonto;
import de.hdm.seCode.security.SecureCallback;
import de.hdm.seCode.security.SecureInterface;

public interface SPerson extends SecureInterface{
	public String getName(SecureCallback callback, Integer tan);
	public Person setName(String name, SecureCallback callback, Integer tan);
	public SPerson getPartner(SecureCallback callback, Integer tan);
	public Person setPartner(SPerson partner, SecureCallback callback,
			Integer tan);
	public SKonto getKonto(SecureCallback callback, Integer tan);
	public Person setKonto(SKonto konto, SecureCallback callback, Integer tan);
	public Person addFreund(SPerson person, SecureCallback callback,
			Integer tan);
	public void go(SecureCallback callback,
			Integer tan);
	public void doSex(SecureCallback callback, Integer tan);
	
	public String toString();
}
