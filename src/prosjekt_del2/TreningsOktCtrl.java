package prosjekt_del2;

public class TreningsOktCtrl {
	
	private TreningsOkt okt;
	
	public TreningsOktCtrl() {
		// Stuff med � connecte til DB
	}
	
	public void opprettOkt(String dato, String startTidspunkt) {
		okt = new TreningsOkt(dato, startTidspunkt);
		
	}

}
