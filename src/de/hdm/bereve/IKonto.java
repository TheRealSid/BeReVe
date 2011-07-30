package de.hdm.bereve;

public interface IKonto {

	//@Creator
	public abstract void setKontoId(Integer kontoId);

	//@All
	public abstract Integer getKontoId();

	//@Creator
	public abstract void setKontostand(Integer kontostand);

	//@Creator
	public abstract Integer getKontostand();

	//@AskCreator
	public abstract Integer zahleAus(Integer betrag);

	//@All
	public abstract Integer zahleEin(Integer betrag);

}