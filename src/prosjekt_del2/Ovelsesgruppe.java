package prosjekt_del2;

import java.util.ArrayList;
import java.util.List;

public class Ovelsesgruppe {
	
	private String navn;
	private List<Ovelse> ovelser = new ArrayList<Ovelse>();
	
	public Ovelsesgruppe(String navn) {
		// checkOvelser(ovelser);
		this.navn = navn;
	}
	
	private void checkOvelser(List<Ovelse> ovelser) {
		if (ovelser.isEmpty()) {
			throw new IllegalArgumentException();
		}
	}
	
	public String getNavn() {
		return this.navn;
	}
	
	public List<Ovelse> getOvelser() {
		return this.ovelser;
	}
	
	public void addToList(Ovelse ovelse) {
		ovelser.add(ovelse);
	}

}
