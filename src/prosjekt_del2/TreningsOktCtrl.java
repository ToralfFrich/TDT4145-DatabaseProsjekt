package prosjekt_del2;

import project.TreningsOkt;

public class TreningsOktCtrl {
	
	private TreningsOkt okt;
	
	public TreningsOktCtrl() {
		// Connection til Model
	}
	
	public void opprettOkt(String dato, String startTidspunkt) {
		okt = new TreningsOkt(dato, startTidspunkt);
		
	}

}
