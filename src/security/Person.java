package security;

import security.identity.IDObject;

public class Person extends IDObject {
	private String name;

	public Person(String name) {
		super();
		this.name = name;
	}

	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return "Person [name=" + name + ", getId()=" + getId() + "]";
	}
}
