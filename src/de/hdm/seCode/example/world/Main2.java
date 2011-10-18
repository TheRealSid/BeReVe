package de.hdm.seCode.example.world;

import de.hdm.seCode.security.ObjectCreator;
import de.hdm.seCode.security.SecureContext;
import de.hdm.seCode.security.identity.IDObject.IDCheckResult;

public class Main2 {
	public static void main(String[] args) {
		Person A = (Person) SecureContext.createObject(new ObjectCreator<Person>() {
			@Override
			public Person createObject() {
				return new Person("Person A");
			}
		});
		Person B = (Person) SecureContext.createObject(new ObjectCreator<Person>() {
			@Override
			public Person createObject() {
				return new Person("Person B");
			}
		});
		
		Person C = (Person) SecureContext.createObject(new ObjectCreator<Person>() {
			@Override
			public Person createObject() {
				return new Person("Person C");
			}
		});
		
		
		C.setId(A.getId());
		
//		System.out.println(A);
//		System.out.println(B);
//		System.out.println(C);
//		
//		if(SecureContext.checkId(A) == IDCheckResult.OK)
//			System.out.println("A ist A");
//		else
//			System.out.println("A ist nicht A");
//		
//		if(SecureContext.checkId(C) == IDCheckResult.OK)
//			System.out.println("C ist A");
//		else
//			System.out.println("C ist nicht A");
//		
//		
//		SecureContext.removeObject(A);
//		
//		if(SecureContext.checkId(A) == IDCheckResult.REMOVED)
//			System.out.println("A ist nicht mehr...");
	}
}
