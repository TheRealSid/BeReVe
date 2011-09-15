package main;
import java.lang.reflect.InvocationTargetException;

import security.SecurityCallback;
import security.SecurityObjectException;
import world.Stadt;
import bank.Bank;








public class Main extends SecurityCallback{
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
	public static void main(String[] args) throws SecurityObjectException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException, NoSuchFieldException {
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
