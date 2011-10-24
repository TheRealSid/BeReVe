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
		Stadt stuttgart = (Stadt) IdentityManager.getInstance().getGlobalContext("stuttgart");
		stuttgart.go();

	}

}
