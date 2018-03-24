package project;

import java.util.Date;

import javafx.scene.control.cell.PropertyValueFactory;

public class OvelseITreningsokt {
	
	
	/*tblcDatoLogg.setCellValueFactory(new PropertyValueFactory<TreningsOkt, Date>("dato"));
	tblcNavnLogg.setCellValueFactory(new PropertyValueFactory<Ovelse, String>("ovelsesnavn"));
	tblcKiloLogg.setCellValueFactory(new PropertyValueFactory<Ovelse, Integer>("antallKilo"));
	tblcSettLogg.setCellValueFactory(new PropertyValueFactory<Ovelse, Integer>("antallSett"));
	tblcBeskrivelseLogg.setCellValueFactory(new PropertyValueFactory<Ovelse, String>("beskrivelse"));*/
	

    private Date dato;
    private String ovelsesnavn;
    private int antallKilo;
    private int antallSett;
    private String beskrivelse;

    public OvelseITreningsokt(Date dato, String ovelsesnavn, int antallKilo, int antallSett, String beskrivelse){
    	this.dato = dato;
    	this.ovelsesnavn = ovelsesnavn;
    	this.antallKilo = antallKilo;
    	this.antallSett = antallSett;
    	this.beskrivelse = beskrivelse;
    }

	public Date getDato() {
		return dato;
	}

	public void setDato(Date dato) {
		this.dato = dato;
	}

	public String getOvelsesnavn() {
		return ovelsesnavn;
	}

	public void setOvelsesnavn(String ovelsesnavn) {
		this.ovelsesnavn = ovelsesnavn;
	}

	public int getAntallKilo() {
		return antallKilo;
	}

	public void setAntallKilo(int antallKilo) {
		this.antallKilo = antallKilo;
	}

	public int getAntallSett() {
		return antallSett;
	}

	public void setAntallSett(int antallSett) {
		this.antallSett = antallSett;
	}

	public String getBeskrivelse() {
		return beskrivelse;
	}

	public void setBeskrivelse(String beskrivelse) {
		this.beskrivelse = beskrivelse;
	}

}