package project;

public class Apparat {
	
	private String navn;
	private String beskrivelse;
	
	public Apparat(String navn, String beskrivelse) {
		this.navn = navn;
		this.beskrivelse = beskrivelse;
	}
	
	public String getNavn() {
		return this.navn;
	}
	
	public String getBesrivelse() {
		return this.beskrivelse;
	}

}
