package de.hdm.seCode.example.world;

import java.util.ArrayList;
import java.util.List;

import de.hdm.seCode.example.world.bank.security.SKonto;
import de.hdm.seCode.security.SecureCallback;



public class Person extends SecureCallback implements IPerson  {
	private String name;
	private SPerson partner;
	private List<SPerson> freunde = new ArrayList<SPerson>();
	private SKonto konto;
	
	
	public Person(String name) {
		super();
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	public Person setName(String name) {
		this.name = name;
		return this;
	}
	public SPerson getPartner() {
		return partner;
	}
	public Person setPartner(SPerson partner) {
		this.partner = partner;
		return this;
	}
	public SKonto getKonto() {
		return konto;
	}
	public Person setKonto(SKonto konto) {
		this.konto = konto;
		return this;
	}
	
	public Person addFreund(SPerson person){
	  freunde.add(person);	
	  return this;
	}
	@Override
	public void go() {
		partner.doSex(this,createTan());
	}
	@Override
	public String toString() {
		return name;
	}
	@Override
	public void doSex() {
		System.out.println(name+ ": OHHH YEAH!");
	}

	@Override
	public int hashCode() {
		final int prime = 31;

		return prime;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Person other = (Person) obj;
		if (freunde == null) {
			if (other.freunde != null)
				return false;
		} else if (!freunde.equals(other.freunde))
			return false;
		if (konto == null) {
			if (other.konto != null)
				return false;
		} else if (!konto.equals(other.konto))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (partner == null) {
			if (other.partner != null)
				return false;
		} else if (!partner.equals(other.partner))
			return false;
		return true;
	}

	
}
