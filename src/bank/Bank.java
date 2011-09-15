package bank;

import java.util.HashMap;
import java.util.Random;

import security.SecurityCallback;
import security.SecurityProxy;
import world.SPerson;
import bank.security.SKonto;

public class Bank extends SecurityCallback{
	//private SKonto konto;
	private boolean confirm;
	private Random rnd;
	private Integer tan;
	private HashMap<SPerson, SKonto> konten;
	
	public Bank() {
		rnd = new Random();
		konten = new HashMap<SPerson, SKonto>();
	}

	/*public SKonto getKonto() {
		if(konto != null) return konto;
		else {
			konto = (SKonto) SecurityProxy.newInstance(new Konto(500), this, SKonto.class);
			return konto;
		}
	}*/

	public void doPrivileged(SKonto konto){
		tan = createTan();
	    konto.setGeld(400000, this, tan);
	    tan = createTan();
		Integer geld = konto.getGeld(this,tan);
		System.out.println("Bank2: "+geld);
	}

	public void createKonto(SPerson person, int guthaben) {
		konten.put(person, (SKonto) SecurityProxy.newInstance(new Konto(guthaben), this, SKonto.class));
	}

	public SKonto getKonto(SPerson person) {
		return konten.get(person);
	}


}
