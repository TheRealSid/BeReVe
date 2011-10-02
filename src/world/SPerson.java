package world;

import security.SecureInterface;
import security.SecurityCallback;
import bank.security.SKonto;

public interface SPerson extends SecureInterface{
	public String getName(SecurityCallback callback, Integer tan);
	public Person setName(String name, SecurityCallback callback, Integer tan);
	public SPerson getPartner(SecurityCallback callback, Integer tan);
	public Person setPartner(SPerson partner, SecurityCallback callback,
			Integer tan);
	public SKonto getKonto(SecurityCallback callback, Integer tan);
	public Person setKonto(SKonto konto, SecurityCallback callback, Integer tan);
	public Person addFreund(SPerson person, SecurityCallback callback,
			Integer tan);
	public void leben(SecurityCallback callback,
			Integer tan);
	public void doSex(SecurityCallback callback, Integer tan);
	
	public String toString();
}
