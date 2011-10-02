package security;

import security.identity.IDObject;

public abstract class ObjectCreator<T extends IDObject> {

	public abstract T createObject();
	
}
