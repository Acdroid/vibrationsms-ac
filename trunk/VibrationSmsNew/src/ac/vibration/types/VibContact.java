package ac.vibration.types;



public class VibContact {

	
	private String name = "";
	private String number = "";
	private Vib vib = new Vib();
	
	
	public VibContact() {}
	
	
	public VibContact(String name, String number, Vib vib) {		
		setName(name);
		setNumber(number);
		setVib(vib);		
	}
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public Vib getVib() {
		return vib;
	}
	public void setVib(Vib vib) {
		this.vib = vib;
	}
	

}
