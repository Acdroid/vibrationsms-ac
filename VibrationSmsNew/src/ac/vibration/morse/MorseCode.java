package ac.vibration.morse;

import android.util.Log;

public class MorseCode {

	/*
	 * entra cada simbolo . o - hay 1 de espera
	 * entre cada letra hay 3 de espera
	 * entre cada palabra hay 7 de espera
	 * 
	 * la raya dura 3 veces el punto
	 * 
	 * */
	
	///Duracion multiplo de todo
	private static final int MULTIPLE = 50;
	//Speed por defecto. Se debera cambiar por configuracion
	public static final int SPEED_DEFECTO = 2;
	
	
	
	/**
	 * Convierte una cadena convencionar a vibracion en morse
	 * 
	 * 
	 * @param s La cadena a convertir
	 * 
	 * @param delay Cuanto se espera antes de empezar
	 * 
	 * @param speed La velocidad, lo ideal es de 1 a 10, cuanto mas
	 * alta es el valor MAS LENTO es, si la velocidad es 3, la vibracion
	 * dura 3*this.MULTIPLE ms (multiplos de this.MULTIPLE), no hay limite por arriba. Afecta de
	 * igual manera a las pausas.
	 * */
	public static long[] stringToVib(String s, long delay, long speed) {
		
		
		//Condiciones de parada
		if ((s == null) || (delay < 0) || (speed < 0)) return null;
			//throw new GeneralExcepption();
				
		if (s.length() == 0) return null;
			//throw new GeneralExcepption();
		
		
		//Convertimos lo que tenemos a morse
		String morse = MorseCode.stringToMorse(s);
		
		if (morse == null) return null;
			//throw new GeneralExcepption();		

		
		/*La cantidad de longs necesarios va a ser:
		 * 
		 * 1 delay inicial
		 * 1 opr caracter incluyendo espacios
		 */  
		int lArray = morse.length()+1;
		
		long[] vibration = new long[lArray];
		
		//El delay inicial
		vibration[0] = delay;
				
		Log.e("main", morse);
		
		//Recorremos la cadena y convertimos
		for (int i=0;i<lArray-1; i++)
			vibration[i+1] = MorseCode.symbolToTime(morse.charAt(i), speed);
		
		
		return vibration;
		
	}
	
	
	/**
	 * Devuelve la longitud en tiempo (ms) que debe esperar
	 * o vibrar dependiendo del caracter y la velocidad y elegida
	 * 
     * @param c El caracter
     *	
	 * @param speed La velocidad, lo ideal es de 1 a 10, cuanto mas
	 * alta es el valor MAS LENTO es, si la velocidad es 3, la vibracion
	 * dura 3*this.MULTIPLE ms (multiplos de this.MULTIPLE), no hay limite por arriba. Afecta de
	 * igual manera a las pausas.
	 * 
	 * @return Un long con el tiempo a vibrar o 0 en caso de error
	 * */
	private static long symbolToTime(char c, long speed) {
		
		if (speed < 0) return 0;
		
		
		switch (c) {
		
			case '-': return MULTIPLE*3*speed;
					
			case '.': return MULTIPLE*speed; 
				
			case ' ': return MULTIPLE*speed;
			
			case '#': return MULTIPLE*3*speed;
			
			case '_': return MULTIPLE*7*speed;
			
			case '0': return 0; //Para forzar el silencio
			
			
			default: return 0;

		}
		
	
	}
	
	
	
	
	/**
	 * Convierte una cadena a codigo morse, devuelve una cadena con
	 * . y - y espacios para separar.
	 * 
	 * @param s Cadena a convertir
	 * 
	 * @return Cadena convertida o null en caso de error
	 * */
	private static String stringToMorse(String s){
		
		if (s == null) return null;		
		if (s.length() == 0) return "";
		
		String ret = "";
		
		//Recorremos la cadena, los carateres erroneos se ignoraran
		//vamos a crear una cadena con el codigo morse de lo pedido
		for (int i=0; i<s.length(); i++)
			ret += MorseCode.charToMorse(s.charAt(i));
		
		
		return ret;
	} 
	

	
	/**
	 * Convierte un char a su codigo morse
	 * 
	 * 
	 * @param c El char a convertir
	 * 
	 * @return El codigo morse del caracter pedido
	 * */
	private static String charToMorse(char c) {
		
		
		String morse[] = {
				//Letras
				". -#","- . . .#","- . - .#","- . .#",".#",". . - .#","- - .#",". . . .#",". .#",". - - -#","- . -#",". - . .#","- -#","- .#","- - -#",
                ". - - .#","- - . -#",". - .#",". . .#","-#",". . -#",". . . -#",". - -#","- . . -#","- . - -#","- - . .#",
                //Numeros
                "- - - - -#", ". - - - -#", ". . - - -#", ". . . - -#", ". . . . -#", ". . . . .#",
                "- . . . .#", "- - . . .#", "- - - . .#", "- - - - .#",
                //Espacio, el 0 indica que no vibra
                "0_"
				};
		
		String morseMap = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789 ";
		
		
		//Lo pasamos a mayus
		String character = String.valueOf(c).toUpperCase();
		
		//Vemos si el char es valido
		if (!MorseCode.inMap(character.charAt(0), morseMap)) return null;
		
		//Sacamos el codigo morse
		String morseSeq = morse[morseMap.indexOf(character)];

				
		return morseSeq;
	}
	
	
	
	/**
	 * Comprueba que un caracter esta en un mapa, no hace
	 * comprobaciones, asume que los parametros son correctos.
	 * 
	 * @param c El caracter
	 * @param map El mapa
	 * 
	 * @return true o false, segun este o no
	 * */
	private static boolean inMap(char c, String map) {
		
		for (int i=0; i<map.length(); i++) {
			
			if (c == map.charAt(i)) return true;
			
		}
	
		return false;
	}
	
	
}
