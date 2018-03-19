package prosjekt_del2;

import java.sql.*;
import java.util.*;

public class TreningsOkt {
	
	public String dato;
	public String startTidspunkt;
	private int varighet;
	private int personligForm;
	private int prestasjon;
	private String notat;
	public List<Ovelse> ovelser;
	
	public TreningsOkt(String dato, String startTidspunkt, int varighet, int personligForm, int prestasjon, String notat) {
		this.dato = dato;
		this.startTidspunkt = startTidspunkt;
		this.varighet = varighet;
		this.personligForm = personligForm;
		this.prestasjon = prestasjon;
		this.notat = notat;
		ovelser = new ArrayList<Ovelse>();
	}
	
	public TreningsOkt(String dato, String startTidspunkt) {
		this.dato = dato;
		this.startTidspunkt = startTidspunkt;
		ovelser = new ArrayList<Ovelse>();
	}
	
	public void setVarighet(int varighet) {this.varighet = varighet;}

	public void setPersonligForm(int personligForm) {this.personligForm = personligForm;}

	public void setPrestasjon(int prestasjon) {this.prestasjon = prestasjon;}

	public void setNotat(String notat) {this.notat = notat;}

	public String getDato() {return dato;}

	public String getStartTidspunkt() {return startTidspunkt;}

	public int getVarighet() {return varighet;}

	public int getPersonligForm() {return personligForm;}

	public int getPrestasjon() {return prestasjon;}

	public String getNotat() {return notat;}
	
	public List<Ovelse> getOvelser() {return ovelser;}
	
	public void addØvelse(Ovelse øvelse) {
		ovelser.add(øvelse);
	}
	public void removeØvelse(Ovelse øvelse) {
		ovelser.remove(øvelse);
	}
	

	
}
