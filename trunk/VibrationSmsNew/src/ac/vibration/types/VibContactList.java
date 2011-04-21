package ac.vibration.types;

import java.util.HashMap;
import java.util.Iterator;

import ac.vibration.exceptions.NoContactFoundException;
import ac.vibration.exceptions.NoVibrationFoundException;

/**
 * Lista de contactos con sus vibraciones y las funciones
 * para acceder a ellos.
 * */
public class VibContactList {

	//Aqui se guarda la lista de contactos con vibraciones leida
	private HashMap<String, VibContact> hm = null;
		
	
	//El contacto master es que aloja la vibración que se usa en caso de que no haya nada 
	public static final String MASTERNUMBER = "master"; 
	
	
	
	
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
	 * Elimina un elemento de la lista.
	 * 
	 * @param vc El VibContact a anadir, se asume que tiene datos
	 * correctos
	 * */
	public void remove(VibContact vc) {
				
		
		hm.remove(vc.getNumber());
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
		
		if (num == null) throw new NoContactFoundException("Key is null");
		
		if (num.length() < 1) throw new NoContactFoundException("Contact list is void");
		
		if (!hm.containsKey(num)) throw new NoContactFoundException("Contact number doesn't exist");
		
		
	return hm.get(num);
	}
	
	
	
	
	/**
	 * La Vibracion master es la que suplanta a todas
	 * las que no esten definidas para un usuario
	 * especifico.
	 *  
	 * */
	public VibContact getMasterContact() throws NoVibrationFoundException {

		
		return hm.get(MASTERNUMBER);		
	}	
	
	
	
	
	/**
	 * Comprueba si un numero de telefono esta contenido en la 
	 * lista de vibContact
	 * 
	 * @param num El numero de telefono
	 * 
	 * @return True si existe, false e.o.c.
	 * 
	 * */
	public boolean contactExists(String num){
		return hm.containsKey(num);
	}
	
	
	
	/**
	 * Devuelve la longitud de la lista de contactos
	 * 
	 * @return La longitud
	 * */
	public int length() {
		
		if (hm == null) return 0;
		
		return hm.size();
		
	}
	
	
	/**
	 * Devuelve un Iterator del HashMap para poder recorrer sus elementos
	 * 
	 *  @return El Iterator o null en caso de error
	 * */
	public Iterator<VibContact> getIterator() {
		
		return hm.values().iterator();
	}
	
	
}
