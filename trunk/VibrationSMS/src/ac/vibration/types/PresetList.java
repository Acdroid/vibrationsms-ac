package ac.vibration.types;

import java.util.HashMap;
import java.util.Iterator;

import ac.vibration.exceptions.GeneralException;
import ac.vibration.exceptions.NoContactFoundException;

/**
 * Lista de contactos con sus vibraciones y las funciones
 * para acceder a ellos.
 * */
public class PresetList {

	//Aqui se guarda la lista de contactos con vibraciones leida
	private HashMap<String, Preset> hm = null;
		
	
	
	
	public PresetList() {
		
		 hm = new HashMap<String, Preset>();
	}
	
	
	
	/**
	 * Anade un elemento a la lista, si el numero
	 * ya esta en la lista, se actualiza.
	 * 
	 * @param vc El VibContact a anadir, se asume que tiene datos
	 * correctos
	 * */
	public void add(Preset vc) {
				
		
		hm.put(vc.getName(), vc);
	}
	
	/**
	 * Elimina un elemento de la lista.
	 * 
	 * @param vc El VibContact a anadir, se asume que tiene datos
	 * correctos
	 * */
	public void remove(Preset vc) {
				
		
		hm.remove(vc.getName());
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
	public Preset getPresetByName(String name) throws GeneralException{
		
		if (name == null) throw new GeneralException("Key is null");
		
		if (name.length() < 1) throw new GeneralException("List is void");
		
		if (!hm.containsKey(name)) throw new GeneralException("Name doesn't exist");
		
		
	return hm.get(name);
	}
	
	

	
	
	/**
	 * Comprueba si un nombre esta contenido en la 
	 * lista 
	 * 
	 * @param num El nombre
	 * 
	 * @return True si existe, false e.o.c.
	 * 
	 * */
	public boolean presetExists(String num){
		return hm.containsKey(num);
	}
	
	
	
	/**
	 * Devuelve la longitud de la lista 
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
	public Iterator<Preset> getIterator() {
		
		return hm.values().iterator();
	}
	
	
}
