package de.hdm.bereve;

import java.util.HashMap;

import de.hdm.bereve.security.SecureObject;

public class Bank {
	private HashMap<Person,Konto> person_konto = new HashMap<Person, Konto>();
	
	public Bank() {
	}
	
	
	public void createKonto(Person p){
		Konto k = new Konto();
		person_konto.put(p,k);
	}
	
	public SecureObject<IKonto> getKonto(Person p){
		IKonto k = person_konto.get(p);
		SecureObject<IKonto> secObj = new SecureObject<IKonto>(k);
		return secObj;
	}
}
