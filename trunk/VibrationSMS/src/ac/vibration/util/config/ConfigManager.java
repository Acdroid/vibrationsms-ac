/**
 * Pner permisos
 * @author Carlos Diaz Canovas y Marcos Trujillo Seoane
 * 
 * @version 1.0
 */
package ac.vibration.util.config;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

import ac.vibration.exceptions.*;
import ac.vibration.types.VibContact;
import ac.vibration.types.VibContactList;
import ac.vibration.types.Vibration;
import android.os.Environment;
import android.util.Log;


/**
 * 
 * Los contactos se van a identificar por su numero de telefono,
 * los numeros de telefono seran cadenas, ya que algunos pueden
 * empezar por +.
 * 
 * El formato del archivo es el siguiente: <br>
 * numero = vibracion <br>
 * <br>
 * ejemplo:<br>
 * 
 * 612345678 = 200,345,2,45,324,56
 * 
 * 
 */
public class ConfigManager {

	//Nombre del archivo donde se guardan los contactos
	private String CONTACTFILE = Environment.getExternalStorageDirectory()+"/contacts.vib";

	
	
	
	//El constructor vcerifica si existe ee archivo y si no lo crea
	public ConfigManager() throws NoContactFileException {
		
		boolean exists = (new File(this.CONTACTFILE)).exists();
		
		//Si no existe intentamos crearlo
		if (!exists) {
			
		    File file = new File(this.CONTACTFILE);
		    		    
		    try {
		    	
				boolean ok = file.createNewFile();
				
				if (!ok) throw new NoContactFileException("Unable to create file: "+this.CONTACTFILE);
				
			} catch (IOException e) {			
				throw new NoContactFileException("Unable to create file 2: "+this.CONTACTFILE);
			}
		}	
	}
	
	
	/**
	 * Carga la lista de contactos con vibracion que se tiene en
	 * el archivo de configuracion. La lista es cargada en memoria
	 * y devuelta se debe acceder a sus elementos con las funciones 
	 * correspondientes en {@link VibContactList}
	 * 
	 * @return 
	 * @throws ContactFileErrorException 
	 * */
	public VibContactList loadVibContactList() throws NoContactFileException, ContactFileErrorException {
		
		//Por necesidades del scope :P
		DataInputStream in = null;
		
		//Esta es la lista
		VibContactList vcList = new VibContactList();
		
	    try {
			//Abrimos el fichero para leer
			FileInputStream fstream = new FileInputStream(this.CONTACTFILE);
		    in = new DataInputStream(fstream);
	        BufferedReader br = new BufferedReader(new InputStreamReader(in));
	        
	        String line = "";
	        
	        //Leemos linea a linea
	        while ((line = br.readLine()) != null) {
	        	
	        	
	        	//Dividimos la linea
	        	String chunks[] = line.split("=");
	        	
	        	//Quitamos los espacios al final y al principio
	        	chunks[0].replaceAll("\\s*", "");
	        	chunks[1].replaceAll("\\s*", "");
	        	
	        	
	        	//Nuevo contacto
	        	VibContact vc = new VibContact();
	        	
	        	//El numero de la persona
	        	vc.setNumber(chunks[0]);
	        	
	        	//Creamos la vibracion	        	
	        	try {
	        		Vibration vib = new Vibration();
					vib.set(vibrationStringToLong(chunks[1]));
					vc.setVib(vib);
					
					//A la lista 
					vcList.add(vc);
				} catch (VibrationErrorException e) {
					//Cuando se mete aqui evitamos agregar
					//a la lista un elemento con un error
				}
	        	
	        }
			
	        in.close();
									
		} catch (FileNotFoundException e) {			
			Log.e("config", "Unable to load contact list");
			throw new NoContactFileException("Unable to load contact list");
			
		} catch (IOException e) {
			Log.e("config", "Error reading from file");
			throw new ContactFileErrorException("Error reading from file");
		}
		
		

				
				
		return vcList;
	}
	
	
	
	/**
	 * Anade un contacto al archivo de configuracion
	 * @throws NoContactFileException 
	 * */
	public void addVibContact(VibContact vc) throws NoContactFileException {
		
	    FileWriter fstream;
		
	    try {
	    	
	    	//Anadimos al final del archivo
			fstream = new FileWriter(this.CONTACTFILE,true);
	        BufferedWriter out = new BufferedWriter(fstream);

	        out.write(vc.getNumber()+"="+vc.getVib().vibToString());
	        
	        out.close();
	        
	    //No se puede hacer nada con el archivo
		} catch (IOException e) {
					
			Log.e("config", "Cannot add entry: config file not found");
			throw new NoContactFileException("Cannot add entry: config file not found");
		}

		
	}
	
	
	
	
	/**
	 * La Vibracion master es la que suplanta a todas
	 * las que no esten definidas para un usuario
	 * especifico.
	 * 
	 * 
	 * */
	public long[] getMasterVibration() throws NoVibrationFoundException {
		
		
			return null;	
	}
	
	
	

	/**
	 * @throws VibrationErrorException 
	 * 
	 * */
	private static long[] vibrationStringToLong(String s) throws VibrationErrorException {
		
		long[] vList;
		
		//Separamos por las comas
		String[] splitted = s.split(",");
		
				
		//Declaramos el long donde se va a guardar
		if (splitted.length > 1)
			vList = new long[splitted.length];
		else {

			Log.e("config", "Format error");
			throw new VibrationErrorException("Format error");
		}
		
		//Vamos sacando los valores 
		for (int i=0; i<splitted.length; i++) {
			
			try {
				vList[i] = Long.parseLong(splitted[i]);
			}
			catch (NumberFormatException e){
				
				Log.e("config", "Format error");
				throw new VibrationErrorException("Format error");
			}
						
		}
		
		return vList;
	}

}

