package ac.vibration.types;

import ac.vibration.exceptions.VibrationErrorException;
import android.util.Log;

public class Vib {

	private long[] vib;
	
	
	/**
	 * <b>Vib</b><br>
	 *   public Vib ()<br><br>
	 *   
	 * <ul>Constructor de la clase Vib</ul><br>
	 */
	public Vib (){
		
	}
	
	/**
	 * <b>Vib</b><br>
	 *   public Vib (long[] v)<br><br>
	 *   
	 * <ul>Constructor de la clase Vib</ul><br>
	 * @parm v Array de longs donde se representa la vibracion
	 * con el formato num,num,num...
	 */
	public Vib (long[] v){
		vib = v;
	}
	
	/**
	 * <b>Vib</b><br>
	 *   public Vib (String v)<br><br>
	 *   
	 * <ul>Constructor de la clase Vib</ul><br>
	 * 
	 * @param v String que representa una vibracion con el formato
	 */
	public Vib (String v)throws VibrationErrorException{
		vib = vibStringToLong(v);
	}
	
	
	public long[] get(){
		
		return vib;
	}
	
	
	public void set(long[] vib) {
		
		this.vib = vib; 
	}
	
	public void set(String v) throws VibrationErrorException{
		vib = vibStringToLong(v);
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
	
	
	/**
	 * <b>vibStringToLong</b><br>
	 *   public long[] vibStringToLong(String s) throws VibrationErrorException<br><br>
	 *   
	 * <ul>Convierte una cadena con el formato:<br>
	 * num,num,num...<br>
	 * A un array de long, un long por cada elemento entre comas</ul><br>
	 * 
	 * @param s La cadena a convertir
	 * 
	 * @return Un array de long, que es una vibracion que se puede usar
	 * 
	 * @throws VibrationErrorException  Cuando el formato no es correcto
	 * */
	public static long[] vibStringToLong(String s) throws VibrationErrorException {
		
		long[] vList;
		
		//Separamos por las comas
		String[] splitted = s.split(",");
		
				
		//Declaramos el long donde se va a guardar
		if (splitted.length > 1)
			vList = new long[splitted.length];
		else {

			Log.e("ConfigManager", "Format error");
			throw new VibrationErrorException("Format error");
		}
		
		//Vamos sacando los valores 
		for (int i=0; i<splitted.length; i++) {
			
			try {
				vList[i] = Long.parseLong(splitted[i]);
			}
			catch (NumberFormatException e){
				
				Log.e("ConfigManager", "Format error");
				throw new VibrationErrorException("Format error");
			}
		}
		
	return vList;
	}
	
}
