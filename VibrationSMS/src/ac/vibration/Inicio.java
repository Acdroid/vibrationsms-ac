package ac.vibration;

import ac.vibration.exceptions.ContactFileErrorException;
import ac.vibration.exceptions.GeneralException;
import ac.vibration.exceptions.NoContactFileException;
import ac.vibration.exceptions.NoContactFoundException;
import ac.vibration.morse.MorseCode;
import ac.vibration.types.VibContact;
import ac.vibration.types.VibContactList;
import ac.vibration.types.Vib;
import ac.vibration.util.Vibration.DoVibration;
import ac.vibration.util.config.ConfigManager;
import ac.vibration.util.contactos.AgregarVibracion;
import ac.vibration.util.mToast.mToast;
import ac.vibration.util.tools.Tools;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;

public class Inicio extends Activity {
    /** Called when the activity is first created. */
	
	public static int ID = 1;
	public static final int RESULT_OK = 0;
	public static final int RESULT_ERROR = 1;
	public static final int RESULT_VIBRATION_EDIT_OK = 2;
	public static final int RESULT_SALIR = 3;
	
	
    @Override 
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        //leeConfig();
        //escribirConfig();
        Log.i("DEBUG",Tools.cleanText("texto ??[]}limpio...;!"));
        
        long[] v = MorseCode.stringToVib("morse code", 1000, 4);
      
        DoVibration.CustomRepeat((Vibrator) getSystemService(Context.VIBRATOR_SERVICE), v);
        
        Vib vv = new Vib(v);
        Log.i("main", vv.vibToString());
        
        
        
        
        
        
    }
    
       
    
    
    //Lee del archivo de config
    public void leeConfig() {
    	
    	
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

	
			//Si esta
			Log.w("main", vcl.getVibContactByNumber("341").getVib().vibToString());
	
			//No esta
			Log.w("main", vcl.getVibContactByNumber("34").getVib().vibToString());
			
			
			
		} catch (NoContactFoundException e) {			
			Log.e("main", "No se ha encontrado el contacto: "+e.getMessage());
		} catch (ContactFileErrorException e) {			
			Log.e("main", "Error en el archivo de contactos");		
		} catch (NoContactFileException e) {
			Log.e("main", "No se encuentra el archivo de contactos");
		}
    	
    	
    }
    
    
    
    
    
    
    
    
    
    //Prueba para escribir en el archivo de config
    public void escribirConfig() {
    	
        //Creamos una instancia del cm (un handlder)
        ConfigManager cm = null;        
        try {
        	cm  = new ConfigManager();
		} catch (NoContactFileException e) {

			Log.e("main", "error: "+e.getMessage());
		} 
    	
		
		
		try {	
			
			VibContactList vcl = new VibContactList();
			
			//Creamos un contacto nuevo con su vibracion
			long[] vi = {122,344,556,44};			
			Vib v = new Vib();
			v.set(vi);			
			VibContact vc0 = new VibContact("aa", "340", v);
			VibContact vc1 = new VibContact("aa", "341", v);
			VibContact vc2 = new VibContact("aa", "342", v);
			VibContact vc3 = new VibContact("aa", "343", v);
			VibContact vc4 = new VibContact("aa", "344", v);
			
			
			//Anadimos el contacto a la lista
			vcl.add(vc0);			
			vcl.add(vc1);
			vcl.add(vc2);
			vcl.add(vc3);
			vcl.add(vc4);
			
			
			try {
				cm.dumpVibContactList(vcl);
			} catch (GeneralException e) {

				Log.e("main", "Error al dump");
			}

			//Este si esta
			Log.w("main", vcl.getVibContactByNumber("341").getVib().vibToString());

						
			//Este no esta
			Log.w("main", vcl.getVibContactByNumber("34").getVib().vibToString());
			
			
		} catch (NoContactFoundException e) {
			
			Log.e("main", "No se ha encontrado el contacto: "+e.getMessage());
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
    
    
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == ID){
			switch (resultCode){
			case RESULT_OK:
				break;
			case RESULT_ERROR:
				break;
			case RESULT_SALIR:
				Inicio.this.finish();
			case RESULT_VIBRATION_EDIT_OK:
				mToast.Make(this, "Vibration Save!", 0);
				break;
			default:
				
			}
		}
	}
    
    
}