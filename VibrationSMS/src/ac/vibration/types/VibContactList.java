package ac.vibration.types;

import java.util.Vector;


/**
 * Lista de contactos con sus vibraciones y las funciones
 * para acceder a ellos.
 * */
public class VibContactList {

	//Aqui se guarda la lista de contactos con vibraciones leida
	private Vector<VibContact> v;
	
	
	public VibContactList() {
		
		 v = new Vector<VibContact>();
	}
	
	
	
	/**
	 * Anade un elemento a la lista
	 * */
	public void add(VibContact vc) {
		
		v.add(vc);
	}
	
	
}
