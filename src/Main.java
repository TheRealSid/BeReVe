import de.hdm.bereve.Bank;
import de.hdm.bereve.IKonto;
import de.hdm.bereve.Person;
import de.hdm.bereve.security.SecureObject;


public class Main {
public static void main(String args[]) {
	Person bob = new Person("Bob");
	Person alice = new Person("Alice");
	Bank bank = new Bank();
	bank.createKonto(bob);
	bank.createKonto(alice);
	
	SecureObject<IKonto> konto_bob=bank.getKonto(bob);
	SecureObject<IKonto> konto_alice=bank.getKonto(alice);
	
	System.out.println(konto_bob);
	System.out.println(konto_alice);
	
}
}
