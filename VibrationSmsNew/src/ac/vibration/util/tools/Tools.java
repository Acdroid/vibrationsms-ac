package ac.vibration.util.tools;

import java.io.File;

import android.os.Environment;

public class Tools {
	
	
	
	/**
	 * <ul>Devuelve el mismo texto pero habiendo eliminado 
	 * todos los simbolos que no deseemos tales como parentesis, llaves,
	 * corchetes etc.
	 * Se elimina acentos y dieresis.</ul><br><br>
	 * @param text Texto del cual queremos eliminar los simbolos.
	 * @return Texto parseado sin simbolos.
	 */
	public static String cleanText(String text){

		//Si se queren anadir simbolos que aparezcan en el texto restultado
		//agregar aqui
		String resultado = "";
		String aux = text.toLowerCase(); 
		
		for (int i=0; i < aux.length(); i++){
			if ( ((int)(aux.charAt(i)) < 65) ||( (((int)(aux.charAt(i) )) > 90)  && (((int)(aux.charAt(i) )) < 97))
					|| ((int)(aux.charAt(i)) > 122)   ){
				//Comprueba si es una vocal con acento, espacio o signo de puntuacion
				switch(aux.charAt(i))
				{
				case ('\u00e1'): resultado += "a";
				break;
				case ('\u00e9'): resultado +="e";
				break;
				case ('\u00ed'): resultado +="i";
				break;
				case ('\u00f3'): resultado +="o";
				break;
				case ('\u00fa'): resultado +="u";
				break;
				case ('\u00FC'): resultado +="u";
				break;
				case ('\u00f1'): resultado += "n";
				break;
				default : resultado +=aux.charAt(i);   //De momento para todos los elementos extranos
				}
			}
			else
				resultado += aux.charAt(i);
		}
		

		resultado = resultado.replaceAll("[^\\p{L}\\p{N}\\ ]","");

		//Transformamos numeros a letras
//		resultado = resultado.replaceAll("[0]", "cero ");
//		resultado = resultado.replaceAll("[1]", "uno ");
//		resultado = resultado.replaceAll("[2]", "dos ");
//		resultado = resultado.replaceAll("[3]", "tres ");
//		resultado = resultado.replaceAll("[4]", "cuatro ");
//		resultado = resultado.replaceAll("[5]", "cinco ");
//		resultado = resultado.replaceAll("[6]", "seis ");
//		resultado = resultado.replaceAll("[7]", "siete ");
//		resultado = resultado.replaceAll("[8]", "ocho ");
//		resultado = resultado.replaceAll("[9]", "nueve ");

		return resultado;
	}
	
	
	
	
	
	
	
	/**
	 * Verifica si exiiste la SD y esta montada
	 * 
	 * Devuelve: 0 RW - 1 RO - 2 Error
	 * */
	public static int externalStorage(){
		
		String state = Environment.getExternalStorageState();

		//Fusilado de los ejemplos de google
		if (Environment.MEDIA_MOUNTED.equals(state)) {			
		    // We can read and write the media
		    return 0;
			
		} else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {			
		    // We can only read the media
			return 1;
		    
		} else {
		    // Something else is wrong. It may be one of many other states, but all we need
		    //  to know is we can neither read nor write
		    return 2;
		}
		
		
		
		
	}
	
	
	
	
	
	
	
	
	

}
