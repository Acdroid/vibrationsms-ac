package ac.vibration.types;

import java.util.HashMap;

import ac.vibration.exceptions.NoContactFoundException;

/**
 * Lista de contactos con sus vibraciones y las funciones
 * para acceder a ellos.
 * */
public class VibContactList {

	//Aqui se guarda la lista de contactos con vibraciones leida
	private HashMap<String, VibContact> hm;
	
	
	public VibContactList() {
		
		 hm = new HashMap<String, VibContact>();
	}
	
	
	
	/**
	 * Anade un elemento a la lista, si el numero
	 * ya esta en la lista, se actualiza.
	 * 
	 * @param vc El VibContact a anadir, se asume que tiene datos
	 * correctos
	 * */
	public void add(VibContact vc) {
				
		
		hm.put(vc.getNumber(), vc);
	}
	
	
	/**
	 * Extrae un VibContact de la lista a partir de un numero
	 * 
	 * @param num El numero de telefono
	 * 
	 * @return El VibContact
	 * 
	 * @throws NoContactFoundException Cuando no se encuentra el contacto
	 * */
	public VibContact getVibContactByNumber(String num) throws NoContactFoundException{
		
		if (num == null) throw new NoContactFoundException();
		
		if (num.length() < 1) throw new NoContactFoundException();
		
		
	return hm.get(num);
	}
	
	
	
}
