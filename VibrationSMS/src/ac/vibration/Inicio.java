package ac.vibration;

import ac.vibration.exceptions.ContactFileErrorException;
import ac.vibration.exceptions.NoContactFileException;
import ac.vibration.types.VibContact;
import ac.vibration.types.VibContactList;
import ac.vibration.types.Vibration;
import ac.vibration.util.config.ConfigManager;
import ac.vibration.util.contactos.AgregarVibracion;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class Inicio extends Activity {
    /** Called when the activity is first created. */
	
	public static int ID = 1;
	public static int RETURN_OK = 0;
	public static int RETURN_ERROR = 1;
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        //Creamos una instancia del cm (un handlder)
        ConfigManager cm = null;        
        try {
        	cm  = new ConfigManager();
		} catch (NoContactFileException e) {

			Log.e("main", "error: "+e.getMessage());
		} 
        
		
		
		
		try {
				
			//Cargamos la lista de contactos
			VibContactList vcl = cm.loadVibContactList();
			
			
			
			//Creamos un contacto nuevo con su vibracion
			long[] vi = {122,344,556,44};			
			Vibration v = new Vibration();
			v.set(vi);			
			VibContact vc = new VibContact("aa", "34", v);			
			
			
			
			//Anadimos el contacto a la lista
			vcl.add(vc);			
			
			//Anadimos el contacto a la config
			cm.addVibContact(vc);
			
			
			
			//TODO: si el numero no esta en el archivo que el prog no se mate
			//TODO: hay que crear la carpeta de destino para el archivo de config
			Log.w("main", vcl.getVibContactByNumber("34").getVib().vibToString());
			
			
			
		} catch (NoContactFileException e) {			
			
			Log.e("main", "error no hay archivo");			
		} catch (ContactFileErrorException e) {
			
			Log.e("main", "error al leer la lista");
		}
    }
    
    
    
    
    /**
     * <b>clickAsignar</b>
     *  public void clickAsignar (View v)
     *  
     *  Este metodo es llamado por el boton agregar
     *  nueva vibracion personalizada de la actividad
     *  Inicio. Lanza la actividad AgregarVibracion
     * 
     * @param v Componente que llama a la funcion
     */
    public void clickAsignar(View v){
    	Intent i = new Intent(Inicio.this,AgregarVibracion.class);
    	startActivityForResult(i, ID);
    	
    }
}