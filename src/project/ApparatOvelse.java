package project;

public class ApparatOvelse extends Ovelse{

	private String antallKilo;
	private String antallSett;
	private Apparat apparat;
	
	public ApparatOvelse(String ovelsesnavn, String antallKilo, String antallSett, Apparat apparat) {
		super(ovelsesnavn, "ApparatOvelse");
		this.antallKilo = antallKilo;
		this.antallSett = antallSett;
		this.apparat = apparat;
	}
	
	public ApparatOvelse(String ovelsesnavn) {
		super(ovelsesnavn, "ApparatOvelse");
	}

	public String getAntallKilo() {
		return antallKilo;
	}

	public String getAntallSett() {
		return antallSett;
	}

	public Apparat getApparat() {
		return apparat;
	}

	public void setAntallKilo(String antallKilo) {
		this.antallKilo = antallKilo;
	}

	public void setAntallSett(String antallSett) {
		this.antallSett = antallSett;
	}

	public void setApparat(Apparat apparat) {
		this.apparat = apparat;
	}
	
	
}
