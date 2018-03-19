package prosjekt_del2;

public class ApparatOvelse extends Ovelse{

	private int antallKilo;
	private int antallSett;
	private Apparat apparat;
	
	public ApparatOvelse(String ovelsesnavn) {
		super(ovelsesnavn);
	}

	public int getAntallKilo() {
		return antallKilo;
	}

	public int getAntallSett() {
		return antallSett;
	}

	public Apparat getApparat() {
		return apparat;
	}

	public void setAntallKilo(int antallKilo) {
		this.antallKilo = antallKilo;
	}

	public void setAntallSett(int antallSett) {
		this.antallSett = antallSett;
	}

	public void setApparat(Apparat apparat) {
		this.apparat = apparat;
	}
	
	

	
	

}
