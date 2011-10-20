package de.hdm.seCode.example.client;

import java.io.IOException;

import de.hdm.seCode.example.world.SPerson;
import de.hdm.seCode.example.world.Stadt;
import de.hdm.seCode.security.acl.ACL;
import de.hdm.seCode.security.identity.IdentityManager;

public class Client {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		IdentityManager im = IdentityManager.getInstance();
		ACL acl = ACL.getInstance();
		//TODO sollte aus IM kommen
		Stadt stuttgart = (Stadt) im.getGlobalContext("stuttgart");
		stuttgart.go();
		
	}

}
