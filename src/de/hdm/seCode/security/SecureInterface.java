package de.hdm.seCode.security;

public interface SecureInterface {
	public Object getObject(SecureCallback callback, int tan);
	public boolean isOwner(Object owner, SecureCallback callback, int tan);
}
