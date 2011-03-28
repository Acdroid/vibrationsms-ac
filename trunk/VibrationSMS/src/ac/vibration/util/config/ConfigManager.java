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
import java.util.Iterator;

import ac.vibration.exceptions.*;
import ac.vibration.types.VibContact;
import ac.vibration.types.VibContactList;
import ac.vibration.types.Vib;
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


	//Carpeta para guardar el archivo
	private String CONTACTPATH = Environment.getExternalStorageDirectory()+"/VibrationSMS/";

	//Nombre del archivo donde se guardan los contactos
	private String CONTACTFILENAME = "contacts.vib";

	//Path completo
	private String CONTACTFILE = this.CONTACTPATH+this.CONTACTFILENAME;




	/**
	 * El constructor vcerifica si existe ee archivo y si no lo crea
	 * 
	 * @throws NoContactFileException
	 */
	public ConfigManager() throws NoContactFileException {

		Log.i("ConfigManager", "ConfigManager called");

		boolean exists = (new File(this.CONTACTFILE)).exists();

		//Si no existe intentamos crearlo
		if (!exists) {

			File file = new File(this.CONTACTFILE);

			try {

				File directory = new File(this.CONTACTPATH);		    	

				//Intentamos crear los directorios
				boolean okDir  = directory.mkdirs();

				if (!okDir) {
					Log.e("ConfigManager", "Unable to create directory: "+this.CONTACTPATH);
					throw new NoContactFileException("Unable to create directory: "+this.CONTACTPATH);
				}

				//Intentamos pues crear el archivo
				boolean okFile = file.createNewFile();

				if (!okFile) {
					Log.e("ConfigManager", "Unable to create file: "+this.CONTACTFILE);
					throw new NoContactFileException("Unable to create file: "+this.CONTACTFILE);
				}


			} catch (IOException e) {

				Log.e("ConfigManager", "Unable to create file");
				throw new NoContactFileException("Unable to create file: "+this.CONTACTFILE);
			}

			Log.i("ConfigManager", "Config file created");
		}

	}


	/**
	 * Carga la lista de contactos con vibracion que se tiene en
	 * el archivo de configuracion. La lista es cargada en memoria
	 * y devuelta se debe acceder a sus elementos con las funciones 
	 * correspondientes en {@link VibContactList}
	 * 
	 * @return Una {@link VibContactList} con todo lo que hay en el archivo
	 *  
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

					Vib vib = new Vib();
					vib.set(Vib.vibStringToLong(chunks[1]));

					vc.setVib(vib);

					//A la lista 
					vcList.add(vc);
				} catch (VibrationErrorException e) {
					//Cuando se mete aqui evitamos agregar
					//a la lista un elemento con un error
				}

			}

			Log.i("ConfigManager", "Config file loaded to memory");
			in.close();

		} catch (FileNotFoundException e) {			
			Log.e("ConfigManager", "Unable to load contact list");
			throw new NoContactFileException("Unable to load contact list");

		} catch (IOException e) {
			Log.e("ConfigManager", "Error reading from file");
			throw new ContactFileErrorException("Error reading from file");
		}


		return vcList;
	}



	/**
	 * Anade un contacto al archivo de configuracion, para simplificar no se
	 * verifica si existe o no el contacto, si esta duplicado, a la hora de cargar solo
	 * el ultimo sera considerado.
	 * 
	 * @param vc Un VibContact con datos
	 * 
	 * @throws NoContactFileException 
	 * */
	public void addVibContact(VibContact vc) throws NoContactFileException {

		FileWriter fstream;

		try {

			//Anadimos al final del archivo
			fstream = new FileWriter(this.CONTACTFILE,true);
			BufferedWriter out = new BufferedWriter(fstream);

			out.write(vc.getNumber()+"="+vc.getVib().vibToString()+"\n");

			out.close();

			Log.i("ConfigManager", "VibContact added with number: "+vc.getNumber());

			//No se puede hacer nada con el archivo
		} catch (IOException e) {

			Log.e("ConfigManager", "Cannot add entry: config file not found");
			throw new NoContactFileException("Cannot add entry: config file not found");
		}		
	}



	/**
	 * Vuelca el contenido de una lista de VibContacts
	 * al archivo de configuracion machacando lo que haya.
	 * USAR CON CUIDADO
	 * 
	 * @param vcl Lista de VibContacts 
	 * 
	 * @throws GeneralException En caso de error  
	 * */
	public void dumpVibContactList(VibContactList vcl) throws GeneralException {

		if (vcl.length() < 1) throw new GeneralException("List is void");

		//Delete old file
		File f = new File(this.CONTACTFILE);		
		boolean ok = f.delete();


		//Si hemos conseguido borrarlo...
		if (ok) {






			//...lo creamos de nuevo vacio y escribimos lo que haya
			FileWriter fstream = null;
			try {
				fstream = new FileWriter(this.CONTACTFILE,true);
			} catch (IOException e1) {				
				Log.e("ConfigManager", "Error when opening file: "+this.CONTACTFILE);
				throw new GeneralException("Error when opening file: "+this.CONTACTFILE);
			}
			BufferedWriter out = new BufferedWriter(fstream);


			Log.i("ConfigManager", vcl.length()+" entries.");

			//Sacamos el iterator y lo recorremos
			Iterator<VibContact> iter = vcl.getIterator();
			VibContact vc = null;



			while (iter.hasNext()) {

				vc = iter.next();

				Log.i("ConfigManager", "in");	        	
				try {
					out.write(vc.getNumber()+"="+vc.getVib().vibToString()+"\n");
					Log.i("ConfigManager", "Written: "+vc.getNumber()+"="+vc.getVib().vibToString()+"\n");
				} catch (IOException e) {

					Log.e("ConfigManager", "Error when writing entry");
				}
			}

			//Cerramos el archivo, las excepciones son una puta mierda
			try {
				out.close();
			} catch (IOException e) {
				Log.e("ConfigManager", "Error when closing file: "+this.CONTACTFILE);
				throw new GeneralException("Error when closing file: "+this.CONTACTFILE);
			}







		}
		else {
			Log.e("ConfigManager", "File couldn't be deleted: "+this.CONTACTFILE);
			throw new GeneralException("File couldn't be deleted: "+this.CONTACTFILE);
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

		long[] aux = {0,1000,100,150};
		return aux;	
	}











}

