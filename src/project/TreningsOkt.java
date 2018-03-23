package project;

import java.sql.*;
import java.sql.Date;
import java.util.*;

public class TreningsOkt {
	
	public Date dato;
	public Time startTidspunkt;
	private int varighet;
	private int personligForm;
	private int prestasjon;
	private String notat;
	public List<Ovelse> ovelser;
	
	public TreningsOkt(Date dato, Time startTidspunkt, int varighet, int personligForm, int prestasjon, String notat) {
		this.dato = dato;
		this.startTidspunkt = startTidspunkt;
		this.varighet = varighet;
		this.personligForm = personligForm;
		this.prestasjon = prestasjon;
		this.notat = notat;
		ovelser = new ArrayList<Ovelse>();
	}
	
	public TreningsOkt(Date date, Time startTidspunkt) {
		this.dato = date;
		this.startTidspunkt = startTidspunkt;
		ovelser = new ArrayList<Ovelse>();
	}
	
	public void setDate(Date dato) {this.dato = dato;}
	
	public void setVarighet(int varighet) {this.varighet = varighet;}
	
	public void setStartsTidspunkt(Time starttidspunkt) {this.startTidspunkt = starttidspunkt ;}

	public void setPersonligForm(int personligForm) {this.personligForm = personligForm;}

	public void setPrestasjon(int prestasjon) {this.prestasjon = prestasjon;}

	public void setNotat(String notat) {this.notat = notat;}

	public Date getDato() {return dato;}

	public Time getStartTidspunkt() {return startTidspunkt;}

	public int getVarighet() {return varighet;}

	public int getPersonligForm() {return personligForm;}

	public int getPrestasjon() {return prestasjon;}

	public String getNotat() {return notat;}
	
	public List<Ovelse> getOvelser() {return ovelser;}
	
	public void addOvelse(Ovelse ovelse) {
		ovelser.add(ovelse);
	}
	public void removeOvelse(Ovelse ovelse) {
		ovelser.remove(ovelse);
	}
	

	
}
