package prosjekt_del2;

public class FriOvelse extends Ovelse{

	private String beskrivelse; 
	
	public FriOvelse(String ovelsesnavn, String beskrivelse) {
		super(ovelsesnavn);
		this.beskrivelse = beskrivelse;
	}
	
	public String getBeskrivelse() {
		return beskrivelse;
	}
}
