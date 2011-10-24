package de.hdm.seCode.example.world;

import java.util.ArrayList;
import java.util.List;

import de.hdm.seCode.example.world.bank.Bank;
import de.hdm.seCode.security.SecureCallback;
import de.hdm.seCode.security.SecureProxy;
import de.hdm.seCode.security.acl.ACL;
import de.hdm.seCode.security.acl.PermissionEntity;
import de.hdm.seCode.security.acl.PermissionEntity.Scope;
import de.hdm.seCode.security.identity.IdentityManager;



public class Stadt extends SecureCallback{
	private List<SPerson> personen = new ArrayList<SPerson>();
	private Bank dieBank = new Bank();
	private Object id;
	public Stadt(Object id) {
		this.id = id;
//		SPerson bob = (SPerson) SecureProxy.newInstance(new Person("Bob"), this, SPerson.class);
//		SPerson alice = (SPerson) SecureProxy.newInstance(new Person("alice"), this, SPerson.class);
//		SPerson eve = (SPerson) SecureProxy.newInstance(new Person("eve"), this, SPerson.class);
//		SPerson john = (SPerson) SecureProxy.newInstance(new Person("John"), this, SPerson.class);
//		personen.add(bob);
//		personen.add(alice);
//		personen.add(eve);
//		personen.add(john);
//		
//		heiraten(bob,alice);
//		freundschaft(bob,eve);
//		freundschaft(eve,john);
//		
//		dieBank.createKonto(bob,1000);
//		dieBank.createKonto(alice,9000);
//		dieBank.createKonto(eve,300);
//		dieBank.createKonto(john,5);
	}
	
	public void heiraten(SPerson a, SPerson b){
		a.setPartner(b,this,createTan());
		ACL.getInstance().addPermission(new PermissionEntity(Scope.INSTANCE,Scope.INSTANCE,null,null,false).addTarget(a).addCaller(b).addMethod("doSex"), createTan(), this);
		b.setPartner(a,this,createTan());
		ACL.getInstance().addPermission(new PermissionEntity(Scope.INSTANCE,Scope.INSTANCE,null,null,false).addTarget(b).addCaller(a).addMethod("doSex"), createTan(), this);
	}
	private void freundschaft(SPerson a, SPerson b){
		a.addFreund(b, this, createTan());
		b.addFreund(a, this, createTan());

	}

	public void go() {
		SPerson carol = (SPerson) IdentityManager.getInstance().getSecureObject("carol");
		SPerson mallory = (SPerson) IdentityManager.getInstance().getSecureObject("mallory");
		
		SPerson bob = (SPerson) SecureProxy.newInstance(new Person("Bob"), this, SPerson.class);
		SPerson alice = (SPerson) SecureProxy.newInstance(new Person("alice"), this, SPerson.class);
		
		heiraten(bob, alice);
		alice.go(this,createTan());
		carol.go(this,createTan());
//		mallory.go(this, createTan());
	}

}
