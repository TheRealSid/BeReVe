package de.hdm.seCode.security;

public interface SecureInterface {
	public Object getObject(SecurityCallback callback, int tan);
	public boolean isOwner(Object owner, SecurityCallback callback, int tan);
}
