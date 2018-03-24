package project;

public class Ovelse {

	
	private String ovelsesnavn = null;
	private String type = null; 
	
	public Ovelse(String ovelsesnavn, String type) {
		this.ovelsesnavn = ovelsesnavn; 
		this.type = type;
	}
	
	public String getOvelsesnavn() { 
		return ovelsesnavn;
	}
	
	public void setOvelsesnavn(String ovelsesnavn) {
		this.ovelsesnavn = ovelsesnavn;
	}
	
	public String getType(){
		return this.type;
	}
	
	public void setType(String type){
		this.type = type;
	}

}
