package de.hdm.bereve;

public class Konto implements IKonto {
	
private Integer kontostand;
private Integer kontoId;

//@Creator
/* (non-Javadoc)
 * @see de.hdm.bereve.IKonto#setKontoId(java.lang.Integer)
 */
@Override
public void setKontoId(Integer kontoId) {
	this.kontoId = kontoId;
}
//@All
/* (non-Javadoc)
 * @see de.hdm.bereve.IKonto#getKontoId()
 */
@Override
public Integer getKontoId() {
	return kontoId;
}
//@Creator
/* (non-Javadoc)
 * @see de.hdm.bereve.IKonto#setKontostand(java.lang.Integer)
 */
@Override
public void setKontostand(Integer kontostand) {
	kontostand = this.kontostand;
}
//@Creator
/* (non-Javadoc)
 * @see de.hdm.bereve.IKonto#getKontostand()
 */
@Override
public Integer getKontostand() {
	return kontostand;
}
//@AskCreator
/* (non-Javadoc)
 * @see de.hdm.bereve.IKonto#zahleAus(java.lang.Integer)
 */
@Override
public Integer zahleAus(Integer betrag){
	if(kontostand - betrag <=0) return null;
	else kontostand -=betrag;
	return kontostand;
}
//@All
/* (non-Javadoc)
 * @see de.hdm.bereve.IKonto#zahleEin(java.lang.Integer)
 */
@Override
public Integer zahleEin(Integer betrag){
	kontostand+=betrag;
	return kontostand;
}

}
