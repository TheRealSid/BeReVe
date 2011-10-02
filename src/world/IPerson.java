package world;

import bank.security.SKonto;

public interface IPerson {
	public String getName();
	public Person setName(String name) ;
	public SPerson getPartner() ;
	public Person setPartner(SPerson partner) ;
	public SKonto getKonto() ;
	public Person setKonto(SKonto konto) ;
	public Person addFreund(SPerson person);
	public void doSex();
	public void leben();
}
