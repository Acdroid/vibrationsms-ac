package ac.vibration.types;

public class Preset {

	private String name = "";
	private Vib vib = new Vib();
	
	
	public Preset() {}
	
	

	
	public Preset(String name, Vib vib){
		setName(name);
		setVib(vib);
	}


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Vib getVib() {
		return vib;
	}

	public void setVib(Vib vib) {
		this.vib = vib;
	}
	
	
	

}
