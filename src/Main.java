import de.hdm.bereve.Bank;
import de.hdm.bereve.Konto;
import de.hdm.bereve.Person;
import de.hdm.bereve.security.SecureObject;


public class Main {
public static void main(String args[]) {
	Person bob = new Person("Bob");
	Person alice = new Person("Alice");
	Bank bank = new Bank();
	bank.createKonto(bob);
	bank.createKonto(alice);
	
	SecureObject<Konto> konto_bob=bank.getKonto(bob);
	SecureObject<Konto> konto_alice=bank.getKonto(alice);
	
	System.out.println(konto_bob);
	System.out.println(konto_alice);
	
}
}
