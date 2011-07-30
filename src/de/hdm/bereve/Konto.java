package de.hdm.bereve;

public class Konto {
	
private Integer kontostand;
private Integer kontoId;

@Creator
public void setKontoId(Integer kontoId) {
	this.kontoId = kontoId;
}
@All
public Integer getKontoId() {
	return kontoId;
}
@Creator
public void setKontostand(Integer kontostand) {
	kontostand = this.kontostand;
}
@Creator
public Integer getKontostand() {
	return kontostand;
}
@AskCreator
public Integer zahleAus(Integer betrag){
	if(kontostand - betrag <=0) return null;
	else kontostand -=betrag;
	return kontostand;
}
@All
public Integer zahleEin(Integer betrag){
	kontostand+=betrag;
	return kontostand;
}

}
