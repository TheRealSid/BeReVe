package security.identity;

import java.util.UUID;

public abstract class IDObject {
	public static enum IDCheckResult {
		OK, REMOVED, NOT_OK
	}
	
	private UUID id;

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}
}
