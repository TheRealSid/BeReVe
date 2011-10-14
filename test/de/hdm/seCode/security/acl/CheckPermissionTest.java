package de.hdm.seCode.security.acl;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import de.hdm.seCode.example.world.Person;
import de.hdm.seCode.example.world.SPerson;
import de.hdm.seCode.example.world.Stadt;
import de.hdm.seCode.security.SecureCallback;
import de.hdm.seCode.security.SecureProxy;
import de.hdm.seCode.security.acl.PermissionEntity.Scope;



public class CheckPermissionTest extends SecureCallback{

	ACL acl;
	Person bob;
	SPerson sBob;
	Person alice;
	SPerson sAlice;
	Person eve;
	SPerson sEve;
	Stadt stadt;
	Stadt evilStadt;
	
	@Before
	public void tearUp(){
		acl = ACL.getInstance();
		 bob = new Person("bob");
		 alice = new Person("alice");
		 eve = new Person("eve");
		 stadt = new Stadt();
		 evilStadt = new Stadt();
		 sBob = (SPerson) SecureProxy.newInstance(bob, this, SPerson.class);
		 sEve = (SPerson) SecureProxy.newInstance(eve, alice, SPerson.class);
		 sAlice = (SPerson) SecureProxy.newInstance(alice, this, SPerson.class);
	}
	
	@Test
	public void testCantAddPermissionForUnownedObject(){
		PermissionEntity entity = new PermissionEntity(Scope.INSTANCE,Scope.INSTANCE,null,null,false).addMethod("doSex").addTarget(sEve).addCaller(bob);
		assertFalse(acl.addPermission(entity,createTan(),this));
		assertFalse(acl.checkPermission("doSex", eve, bob));
		
	}
	@Test
	public void testCheckPermissionIdentity2Identity() {
		PermissionEntity entity = new PermissionEntity(Scope.INSTANCE,Scope.INSTANCE,null,null,false).addMethod("doSex").addTarget(sBob).addCaller(alice);
		acl.addPermission(entity,createTan(),this);
		assertTrue(acl.checkPermission("doSex",bob,alice));
		assertFalse(acl.checkPermission("getKonto",bob,alice));
		assertFalse(acl.checkPermission("doSex",bob,eve));
	}
	@Test
	public void testCheckPermissionIdentity2Identity2() {
		PermissionEntity entity = new PermissionEntity(Scope.INSTANCE,Scope.INSTANCE,null,null,false).addMethod("doSex").addMethod("getKonto").addTarget(sBob).addCaller(alice);
		acl.addPermission(entity,createTan(),this);
		assertTrue(acl.checkPermission("doSex",bob,alice));
		assertTrue(acl.checkPermission("getKonto",bob,alice));
		assertFalse(acl.checkPermission("doSex",bob,eve));
	}
	@Test
	public void testCheckPermissionIdentity2Identity3() {
		PermissionEntity entity = new PermissionEntity(Scope.INSTANCE,Scope.INSTANCE,null,null,false).addMethod("doSex").addMethod("getKonto").addTarget(sBob).addCaller(alice).addCaller(eve);
		acl.addPermission(entity,createTan(),this);
		assertTrue(acl.checkPermission("doSex",bob,alice));
		assertTrue(acl.checkPermission("getKonto",bob,alice));
		assertTrue(acl.checkPermission("doSex",bob,eve));
	}
	@Test
	public void testCheckPermissionClass2Class() {
		PermissionEntity entity = new PermissionEntity(Scope.CLASS,Scope.CLASS,Person.class,Stadt.class,false).addMethod("doSex");
		acl.addPermission(entity,createTan(),this);
		assertFalse(acl.checkPermission("doSex",bob,alice));
		assertFalse(acl.checkPermission("doSex",bob,eve));
		assertTrue(acl.checkPermission("doSex",bob,stadt));
		assertFalse(acl.checkPermission("getKonto",bob,alice));
		assertFalse(acl.checkPermission("getKonto",bob,stadt));
		assertFalse(acl.checkPermission("getKonto",alice,stadt));
	}
	@Test
	public void testCheckPermissionClass2Identity() {
		PermissionEntity entity = new PermissionEntity(Scope.INSTANCE,Scope.CLASS,null,Stadt.class,false).addMethod("getKonto").addTarget(sBob);
		acl.addPermission(entity,createTan(),this);
		assertTrue(acl.checkPermission("getKonto",bob,stadt));
		assertFalse(acl.checkPermission("getKonto",bob,eve));
		assertFalse(acl.checkPermission("getKonto",eve,stadt));
	}
	@Test
	public void testCheckPermissionIdentity2Class() {
		PermissionEntity entity = new PermissionEntity(Scope.CLASS,Scope.INSTANCE,Person.class,null,false).addMethod("getKonto").addCaller(stadt);
		acl.addPermission(entity,createTan(),this);
		assertTrue(acl.checkPermission("getKonto",bob,stadt));
		assertTrue(acl.checkPermission("getKonto",eve,stadt));
		assertTrue(acl.checkPermission("getKonto",alice,stadt));
		assertFalse(acl.checkPermission("getKonto",alice,eve));
		assertFalse(acl.checkPermission("getKonto",alice,evilStadt));
	}
	@Test
	public void testCheckPermissionAllMethods(){
		PermissionEntity entity = new PermissionEntity(Scope.INSTANCE,Scope.INSTANCE,null,null,true).addTarget(sBob).addCaller(alice);
		acl.addPermission(entity,createTan(),this);
		assertTrue(acl.checkPermission("doSex",bob,alice));
		assertTrue(acl.checkPermission("getKonto",bob,alice));
		assertFalse(acl.checkPermission("doSex",bob,eve));
	}
	@Test
	public void testCheckPermissionAllMethodsClass2Class(){
		PermissionEntity entity = new PermissionEntity(Scope.CLASS,Scope.CLASS,Person.class,Stadt.class,true);
		acl.addPermission(entity,createTan(),this);
		assertTrue(acl.checkPermission("doSex",bob,stadt));
		assertTrue(acl.checkPermission("getKonto",bob,stadt));
		assertFalse(acl.checkPermission("doSex",bob,eve));
	}

}
