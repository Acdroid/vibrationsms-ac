package ac.vibration.util.config;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import ac.vibration.exceptions.NoContactFileException;
import android.util.Log;



/**
 * Esta clase se encarga de gestionar configuraciones independientemente del contenido
 * los archivo de configuración tienen este aspecto:<br>
 * <br>
 * clave1=valor<br>
 * clave2=valor
 * 
 * */
public class ConfigBackend {

	
	
	
	/**
	 * Crea la estructura hasta el archivo deseado
	 * 
	 * @param dirPath El directorio completo donde esta el archivo sin el nombre del archivo
	 * @param fullPath
	 * @throws NoContactFileException 
	 * 
	 * */
	public static void createStructure(String dirPath, String fullPath) throws NoContactFileException {
		
		File file = new File(fullPath);

		try {

			File directory = new File(dirPath);		    	

			//Intentamos crear los directorios si no existen ya
			if (!directory.exists()) {
				boolean okDir  = directory.mkdirs();

				if (!okDir) {
					Log.e("ConfigBackend", "Unable to create directory: "+dirPath);
					throw new NoContactFileException("Unable to create directory: "+dirPath);
				}
			}

			//Intentamos pues crear el archivo
			boolean okFile = file.createNewFile();

			if (!okFile) {
				Log.e("ConfigBackend", "Unable to create file: "+fullPath);
				throw new NoContactFileException("Unable to create file: "+fullPath);
			}


		} catch (IOException e) {

			Log.e("ConfigBackend", "Unable to create file");
			throw new NoContactFileException("Unable to create file: "+fullPath);
		}

		Log.i("ConfigBackend", "Config file created");
		
		
		
	}
	
	
	
	
	/**
	 * Recibe una linea de configuración y devuelve los valores de su contenido
	 * 
	 * @param String line La linea a parsear en la forma clave=valor
	 * 
	 * @return Un array de dos posiciones, la 0 es la clave la 1 el valor o null en cada posición en caso de error
	 * */
	public static String[] parseLine(String line) {
		
		if (line == null) return null;
		
		//Dividimos la linea
		String chunks[] = line.split("=");

		//Quitamos los espacios al final y al principio
		if (chunks[0] != null) chunks[0].replaceAll("\\s*", "");
		if (chunks[0] != null) chunks[1].replaceAll("\\s*", "");
		
		return chunks;
				
		
	} 
	
	
	
	
	/**
	 * Añade una linea al final del archivo bajo la forma clave=valor
	 * 
	 * @param file Fullpath al nombre del archivo
	 * @param key La clave
	 * @param value El valor
	 * @throws NoContactFileException 
	 * 
	 * */
	public static void addLine(String fullPath, String key, String value) throws NoContactFileException {
		
		FileWriter fstream;

		try {

			//Anadimos al final del archivo
			fstream = new FileWriter(fullPath,true);
			BufferedWriter out = new BufferedWriter(fstream);

			out.write(key+"="+value+"\n");

			out.close();

			Log.i("ConfigBAckend", "Line added with key: "+key);

			//No se puede hacer nada con el archivo
		} catch (IOException e) {

			Log.e("ConfigBackend", "Cannot add entry: config file not found");
			throw new NoContactFileException("Cannot add entry: config file not found");
		}
	}
	
	
	
	
}
