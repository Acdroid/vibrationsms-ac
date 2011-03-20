package ac.vibration.types;

public class Vibration {

	private long[] vib;
	
	
	public long[] get(){
		
		return vib;
	}
	
	
	public void set(long[] vib) {
		
		this.vib = vib; 
	}
	
	/**
	 * Convierte la secuencia de long en una cadena
	 * */
	public String vibToString(){
		
		String s = "";
		
		
		for (int i=0; i<vib.length;i++)			
			s += vib[i]+((i==(vib.length-1))?"":",");
		
		
	return s;	
	}
	
}
