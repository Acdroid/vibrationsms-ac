package ac.vibration.types;



public class VibContact {

	
	private String name = "";
	private String number = "";
	private Vibration vib = new Vibration();
	
	
	public VibContact() {}
	
	
	public VibContact(String name, String number, Vibration vib) {		
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
	public Vibration getVib() {
		return vib;
	}
	public void setVib(Vibration vib) {
		this.vib = vib;
	}
	

}
