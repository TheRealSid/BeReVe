package de.hdm.seCode.example.world;

import java.util.ArrayList;
import java.util.List;

import de.hdm.seCode.example.world.bank.Bank;
import de.hdm.seCode.security.SecureCallback;
import de.hdm.seCode.security.SecureProxy;



public class Stadt extends SecureCallback{
	private List<SPerson> personen = new ArrayList<SPerson>();
	private Bank dieBank = new Bank();
	public Stadt() {
		SPerson bob = (SPerson) SecureProxy.newInstance(new Person("Bob"), this, SPerson.class);
		SPerson alice = (SPerson) SecureProxy.newInstance(new Person("alice"), this, SPerson.class);
		SPerson eve = (SPerson) SecureProxy.newInstance(new Person("eve"), this, SPerson.class);
		SPerson john = (SPerson) SecureProxy.newInstance(new Person("John"), this, SPerson.class);
		personen.add(bob);
		personen.add(alice);
		personen.add(eve);
		personen.add(john);
		
		heiraten(bob,alice);
		freundschaft(bob,eve);
		freundschaft(eve,john);
		
		dieBank.createKonto(bob,1000);
		dieBank.createKonto(alice,9000);
		dieBank.createKonto(eve,300);
		dieBank.createKonto(john,5);
	}
	
	private void heiraten(SPerson a, SPerson b){
		a.setPartner(b,this,createTan());
//		ACL.getInstance().addPermission(a,b,"doSex");
		b.setPartner(a,this,createTan());
//		ACL.getInstance().addPermission(b,a,"doSex");
	}
	private void freundschaft(SPerson a, SPerson b){
		a.addFreund(b, this, createTan());
		b.addFreund(a, this, createTan());

	}

	public void leben() {
		for(SPerson person:personen){
			person.leben(this,createTan());
		}
	}
}
