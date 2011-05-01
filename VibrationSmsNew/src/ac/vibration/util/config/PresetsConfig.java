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

import ac.vibration.exceptions.ContactFileErrorException;
import ac.vibration.exceptions.GeneralException;
import ac.vibration.exceptions.NoFileException;
import ac.vibration.exceptions.VibrationErrorException;
import ac.vibration.types.Preset;
import ac.vibration.types.PresetList;
import ac.vibration.types.Vib;
import android.os.Environment;
import android.util.Log;



/**
 * Gestión de los presets en un archivo identificados por un nombre
 * */
public class PresetsConfig {

	
	//Carpeta para guardar el archivo
	private static String PRESETPATH = Environment.getExternalStorageDirectory()+"/VibrationSMS/";

	//Nombre del archivo donde se guardan los presets
	private static String PRESETFILENAME = "presets.vib";

	//Path completo
	private static String PRESETFILE = PRESETPATH+PRESETFILENAME;
	
	
	
	/**
	 * El constructor verifica si existe ee archivo y si no lo crea
	 * 
	 * @throws NoContactFileException
	 */
	public PresetsConfig() throws NoFileException {

		Log.i("PresetsConfig", "PresetsConfig called");

		boolean exists = (new File(PRESETFILE)).exists();

		//Si no existe intentamos crearlo
		if (!exists) {

			ConfigBackend.createStructure(PRESETPATH, PRESETFILE);
		}

	}
	
	
	
	/**
	 * Carga los presets
	 * @throws NoFileException 
	 * @throws ContactFileErrorException 
	 * */
	public PresetList loadPresets() throws NoFileException, ContactFileErrorException {
		

		//Por necesidades del scope :P
		DataInputStream in = null;

		//Esta es la lista
		PresetList pList = new PresetList();

		try {
			//Abrimos el fichero para leer
			FileInputStream fstream = new FileInputStream(PRESETFILE);
			in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));

			String line = "";

			//Leemos linea a linea
			while ((line = br.readLine()) != null) {


				//Dividimos la linea
				String chunks[] = ConfigBackend.parseLine(line);


				//Nuevo preset
				Preset p = new Preset();

				//El nombre del preset
				p.setName(chunks[0]);

				//Creamos la vibracion	        	
				try {

					Vib vib = new Vib();
					vib.set(Vib.vibStringToLong(chunks[1]));

					p.setVib(vib);

					//A la lista 
					pList.add(p);
					
				} catch (VibrationErrorException e) {
					//Cuando se mete aqui evitamos agregar
					//a la lista un elemento con un error
				}

			}

			Log.i("PresetConfig", "Config file loaded to memory");
			in.close();

		} catch (FileNotFoundException e) {			
			Log.e("PresetConfig", "Unable to load contact list");
			throw new NoFileException("Unable to load contact list");

		} catch (IOException e) {
			Log.e("PresetConfig", "Error reading from file");
			throw new ContactFileErrorException("Error reading from file");
		}


		return pList;

	}
	
	
	
	/**
	 * Anade un elemento al archivo de configuracion, para simplificar no se
	 * verifica si existe o no el contacto, si esta duplicado, a la hora de cargar solo
	 * el ultimo sera considerado.
	 * 
	 * @param vc Un VibContact con datos
	 * 
	 * @throws NoContactFileException 
	 * */
	public void addPreset(Preset p) throws NoFileException {

				
		ConfigBackend.addLine(PRESETFILE, p.getName(), p.getVib().vibToString());
	}
	
	
	
	
	
	
	/**
	 * Vuelca el contenido de una lista
	 * al archivo de configuracion machacando lo que haya.
	 * USAR CON CUIDADO
	 * 
	 * @param pl Lista  
	 * 
	 * @throws GeneralException En caso de error  
	 * */
	public void dumpPresetList(PresetList pl) throws GeneralException {

		if (pl.length() < 0) throw new GeneralException("List length incorrect");

		//Delete old file	
		boolean ok = deleteConfig();


		//Si hemos conseguido borrarlo...
		if (ok) {



			//...lo creamos de nuevo vacio y escribimos lo que haya
			FileWriter fstream = null;
			try {
				fstream = new FileWriter(PRESETFILE,true);
			} catch (IOException e1) {				
				Log.e("PresetsConfig", "Error when opening file: "+PRESETFILE);
				throw new GeneralException("Error when opening file: "+PRESETFILE);
			}
			BufferedWriter out = new BufferedWriter(fstream);


			Log.i("PresetsConfig", pl.length()+" entries.");

			//Sacamos el iterator y lo recorremos
			Iterator<Preset> iter = pl.getIterator();
			Preset p = null;



			while (iter.hasNext()) {

				p = iter.next();

				
				Log.i("PresetsConfig", "in");	        	
				try {
					out.write(p.getName()+"="+p.getVib().vibToString()+"\n");
					Log.i("PresetsConfig", "Written: "+p.getName()+"="+p.getVib().vibToString()+"\n");
				} catch (IOException e) {

					Log.e("PresetsConfig", "Error when writing entry");
				}
								
			}

			//Cerramos el archivo, las excepciones son una puta mierda
			try {
				out.close();
			} catch (IOException e) {
				Log.e("PresetsConfig", "Error when closing file: "+PRESETFILE);
				throw new GeneralException("Error when closing file: "+PRESETFILE);
			}


		}
		else {
			Log.e("PresetsConfig", "File couldn't be deleted: "+PRESETFILE);
			throw new GeneralException("File couldn't be deleted: "+PRESETFILE);
		}


	}
	
	
	
	/**
	 * Elimina el archivo de configuracion 
	 * 
	 * @return true si ha borrado false en caso de error
	 * */
	public static boolean deleteConfig() {
		
		File f = new File(PRESETFILE);
		return f.delete();
	}
	
	
	
	
	
	
	
}
