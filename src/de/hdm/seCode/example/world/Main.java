package de.hdm.seCode.example.world;
import java.lang.reflect.InvocationTargetException;

import de.hdm.seCode.example.world.bank.Bank;
import de.hdm.seCode.security.SecureCallback;
import de.hdm.seCode.security.SecureObjectException;









public class Main extends SecureCallback{
	private Object[] sput;

	/**
	 * @param args
	 * @throws NoSuchMethodException 
	 * @throws SecurityObjectException 
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 * @throws NoSuchFieldException 
	 */
	public static void main(String[] args) throws SecureObjectException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException, NoSuchFieldException {
		Stadt stadt = new Stadt();
		stadt.leben();
	}
	
	public void doSomething(){
		Bank bank = new Bank();
		//SecurityException
//		try{
//			konto.getGeld(this);
//		}catch(Exception e){
//			System.out.println("SOE caught");
//		}
//		konto.setGeld(5000,this);
	}
	@Override
	public boolean equals(Object obj) {
		return true;
	}




}
