package project;

import java.util.ArrayList;
import java.util.List;

public class Ovelsesgruppe {
	
	private String navn;
	private List<Ovelse> ovelser = new ArrayList<Ovelse>();
	
	public Ovelsesgruppe(String navn) {
		this.navn = navn;
	}
	
	public String getNavn() {
		return this.navn;
	}
	
	public List<Ovelse> getOvelser() {
		return this.ovelser;
	}
	
	public void addOvelseIOvelsesgruppe(String ovelsesstring){
		Ovelse ovelse = new Ovelse(ovelsesstring);
		this.ovelser.add(ovelse);
	}


}
