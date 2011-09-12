package bank;

import java.lang.reflect.InvocationTargetException;
import java.util.Random;

import bank.security.SKonto;

import security.SecurityCallback;
import security.SecurityProxy;

public class Bank extends SecurityCallback{
	private SKonto konto;
	private boolean confirm;
	private Random rnd;
	private Integer tan;
	
	public Bank() {
		rnd = new Random();
		
	}

	public SKonto getKonto() {
		if(konto != null) return konto;
		else return (SKonto) SecurityProxy.newInstance(new Konto(500), this, SKonto.class);
	}

	public void doPrivileged(SKonto konto){
		tan = createTan();
	    konto.setGeld(400000, this, tan);
	    tan = createTan();
		Integer geld = konto.getGeld(this,tan);
		System.out.println("Bank2: "+geld);
	}

	


}
