package de.hdm.seCode.security;

import de.hdm.seCode.security.identity.IDObject;

public abstract class ObjectCreator<T extends IDObject> {

	public abstract T createObject();
	
}
