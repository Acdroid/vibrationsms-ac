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
public class ContactsConfig {


	//Carpeta para guardar el archivo
	private static String CONTACTPATH = Environment.getExternalStorageDirectory()+"/VibrationSMS/";
	private static String INTERNAL_CONTACTPATH = Environment.getDataDirectory()+"/VibrationSMS/";

	//Nombre del archivo donde se guardan los contactos
	private static String CONTACTFILENAME = "contacts.vib";

	//Path completo
	private static String CONTACTFILE = CONTACTPATH+CONTACTFILENAME;
	private static String INTERNAL_CONTACTFILE = INTERNAL_CONTACTPATH+CONTACTFILENAME;




	/**
	 * El constructor verifica si existe ee archivo y si no lo crea
	 * 
	 * @throws NoContactFileException
	 */
	public ContactsConfig() throws NoFileException {

		Log.i("ContactsConfig", "ContactsConfig called");

		boolean exists = (new File(CONTACTFILE)).exists();

		//Si no existe intentamos crearlo
		if (!exists) {

			try {
				
				//Lo intentamos en la SD
				ConfigBackend.createStructure(CONTACTPATH, CONTACTFILE);
			} catch (NoFileException e) {
				
				//Si en la SD no se puede lo intentamos en el sitio por defecto
				ConfigBackend.createStructure(INTERNAL_CONTACTPATH, INTERNAL_CONTACTFILE);
				CONTACTPATH = INTERNAL_CONTACTPATH;
				CONTACTFILE = INTERNAL_CONTACTFILE;
			}						
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
	public VibContactList loadVibContactList() throws NoFileException, ContactFileErrorException {

		//Por necesidades del scope :P
		DataInputStream in = null;

		//Esta es la lista
		VibContactList vcList = new VibContactList();

		try {
			//Abrimos el fichero para leer
			FileInputStream fstream = new FileInputStream(CONTACTFILE);
			in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));

			String line = "";

			//Leemos linea a linea
			while ((line = br.readLine()) != null) {


				//Dividimos la linea
				String chunks[] = ConfigBackend.parseLine(line);


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

			Log.i("ContactsConfig", "Config file loaded to memory");
			in.close();

		} catch (FileNotFoundException e) {			
			Log.e("ContactsConfig", "Unable to load contact list");
			throw new NoFileException("Unable to load contact list");

		} catch (IOException e) {
			Log.e("ContactsConfig", "Error reading from file");
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
	public void addVibContact(VibContact vc) throws NoFileException {

				
		ConfigBackend.addLine(CONTACTFILE, vc.getNumber(), vc.getVib().vibToString());
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
		boolean ok = deleteConfig();


		//Si hemos conseguido borrarlo...
		if (ok) {



			//...lo creamos de nuevo vacio y escribimos lo que haya
			FileWriter fstream = null;
			try {
				fstream = new FileWriter(CONTACTFILE,true);
			} catch (IOException e1) {				
				Log.e("ContactsConfig", "Error when opening file: "+CONTACTFILE);
				throw new GeneralException("Error when opening file: "+CONTACTFILE);
			}
			BufferedWriter out = new BufferedWriter(fstream);


			Log.i("ContactsConfig", vcl.length()+" entries.");

			//Sacamos el iterator y lo recorremos
			Iterator<VibContact> iter = vcl.getIterator();
			VibContact vc = null;



			while (iter.hasNext()) {

				vc = iter.next();

				
				Log.i("ContactsConfig", "in");	        	
				try {
					out.write(vc.getNumber()+"="+vc.getVib().vibToString()+"\n");
					Log.i("ContactsConfig", "Written: "+vc.getNumber()+"="+vc.getVib().vibToString()+"\n");
				} catch (IOException e) {

					Log.e("ContactsConfig", "Error when writing entry");
				}
								
			}

			//Cerramos el archivo, las excepciones son una puta mierda
			try {
				out.close();
			} catch (IOException e) {
				Log.e("ContactsConfig", "Error when closing file: "+CONTACTFILE);
				throw new GeneralException("Error when closing file: "+CONTACTFILE);
			}


		}
		else {
			Log.e("ContactsConfig", "File couldn't be deleted: "+CONTACTFILE);
			throw new GeneralException("File couldn't be deleted: "+CONTACTFILE);
		}


	}
	
	
	
	/**
	 * Elimina el archivo de configuracion de contactos
	 * 
	 * @return true si ha borrado false en caso de error
	 * */
	public static boolean deleteConfig() {
		
		File f = new File(CONTACTFILE);
		return f.delete();
	}
	

}

