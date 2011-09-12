package bank;

public class Konto{

	
	private Integer geld;

	public Konto(Integer geld) {
		this.geld=geld;
	}
	
	public void setGeld(Integer geld) {
		this.geld = geld;
	}

	public Integer getGeld() {
		return geld;
	}
	
	@Override
	public String toString() {
		return "Geld: "+ geld;
	}
}
