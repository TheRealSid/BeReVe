package de.hdm.seCode.example.world.bank;

import java.util.HashMap;
import java.util.Random;

import de.hdm.seCode.example.world.SPerson;
import de.hdm.seCode.example.world.bank.security.SKonto;
import de.hdm.seCode.security.SecurityCallback;
import de.hdm.seCode.security.SecurityProxy;


public class Bank extends SecurityCallback {
	// private SKonto konto;
	private boolean confirm;
	private Random rnd;
	private Integer tan;
	private HashMap<SPerson, SKonto> konten;

	public Bank() {
		rnd = new Random();
		konten = new HashMap<SPerson, SKonto>();
	}

	/*
	 * public SKonto getKonto() { if(konto != null) return konto; else { konto =
	 * (SKonto) SecurityProxy.newInstance(new Konto(500), this, SKonto.class);
	 * return konto; } }
	 */

	public void doPrivileged(SKonto konto) {
		tan = createTan();
		konto.setGeld(400000, this, tan);
		tan = createTan();
		Integer geld = konto.getGeld(this, tan);
		System.out.println("Bank2: " + geld);
	}

	public void createKonto(SPerson person, int guthaben) {
		konten.put(person, (SKonto) SecurityProxy.newInstance(new Konto(
				guthaben), this, SKonto.class));
	}

	public SKonto getKonto(SPerson person) {
		return konten.get(person);
	}

}
