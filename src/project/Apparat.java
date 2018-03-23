package project;

public class Apparat {
	
	private String navn;
	private String beskrivelse;
	
	public Apparat(String navn, String beskrivelse) {
		this.navn = navn;
		this.beskrivelse = beskrivelse;
	}
	
	
	//Brukes i finn øvelse mellom to datoer og vis info.
	public Apparat(String navn){
		this.navn = navn;
	}
	
	public String getNavn() {
		return this.navn;
	}
	
	public String getBesrivelse() {
		return this.beskrivelse;
	}

}
