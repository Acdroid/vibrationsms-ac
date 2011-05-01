package ac.vibration;

import java.util.Iterator;

import ac.vibration.exceptions.ContactFileErrorException;
import ac.vibration.exceptions.GeneralException;
import ac.vibration.exceptions.NoContactFoundException;
import ac.vibration.exceptions.NoFileException;
import ac.vibration.exceptions.NoPreferenceException;
import ac.vibration.morse.MorseCode;
import ac.vibration.types.Preset;
import ac.vibration.types.PresetList;
import ac.vibration.types.VibContact;
import ac.vibration.types.VibContactList;
import ac.vibration.types.Vib;
import ac.vibration.ui.AddVib;
import ac.vibration.ui.ListContactsWithVib;
import ac.vibration.ui.MasterMenu;
import ac.vibration.ui.Settings;
import ac.vibration.ui.ShowPresetList;
import ac.vibration.util.Vibration.DoVibration;
import ac.vibration.util.config.AppConfig;
import ac.vibration.util.config.ContactsConfig;
import ac.vibration.util.config.PresetsConfig;
import ac.vibration.ui.AgregarVibracion;
import ac.vibration.util.mToast.mToast;
import ac.vibration.util.tools.Tools;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

public class Inicio extends Activity {
	
	public static int ID = 1;
	public static final int RESULT_OK = 0;
	public static final int RESULT_ERROR = 1;
	public static final int RESULT_VIBRATION_EDIT_OK = 2;
	public static final int RESULT_SALIR = 3;
	
	private AppConfig ac;
	private AudioManager am;
	
	private boolean progressOn = false;
	private ProgressDialog progress;
	
	
	private static int vibNotificationOriginal;
	private static int vibRingerOriginal;
	
    @Override 
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        am = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
        ac = new AppConfig(this, AppConfig.CONFIG_NAME_DEF);
        
        //Se crea, no se muestra
        progress = new ProgressDialog(Inicio.this);
                       
        
        
        //Configuraciones de vibracion originales
        vibNotificationOriginal = am.getVibrateSetting(AudioManager.VIBRATE_TYPE_NOTIFICATION);
        vibRingerOriginal = am.getVibrateSetting(AudioManager.VIBRATE_TYPE_RINGER);
        
        
        try {
        
	        //Se deshablitan las vibraciones del sistema si se ha pedido en las pref
	        if (ac.getBool(AppConfig.VIBRATE_SYSTEM)) {
	        	        	
	        	//am.setVibrateSetting(AudioManager.VIBRATE_TYPE_NOTIFICATION, AudioManager.VIBRATE_SETTING_OFF);
				am.setVibrateSetting(AudioManager.VIBRATE_TYPE_RINGER, AudioManager.VIBRATE_SETTING_OFF);
	        	
	        }
        }catch(NoPreferenceException e) {
        	e.printStackTrace();
        	Log.w("main", "No hay preferencias");
        }
        
        
        
        
        //Reset config
        /*
        try {
			new ContactsConfig().deleteConfig();
		} catch (NoFileException e) {
			e.printStackTrace();
		}
		*/
		

        
        
        //leeConfig();
        //escribirConfig();
        
        //long[] v = MorseCode.stringToVib("sms", 1000, 2);
        //DoVibration.CustomRepeat((Vibrator) getSystemService(Context.VIBRATOR_SERVICE), v);
        //Vib vv = new Vib(v);
        //Log.i("main", vv.vibToString());
        
 
    }
    
   //Al volver a la app, quitamos el progreso si es que estaba
   @Override
   public void onRestart() {
       super.onRestart(); 
       
	   //Quitamos el progreso
	   if (progressOn) {		   
		   progress.dismiss();
		   progressOn = false;
	   }
	   
   }
    
       
    
    

    
    
    
    
    
    /**
     *  Este metodo es llamado por el boton agregar
     *  nueva vibracion personalizada de la actividad
     *  Inicio. Lanza la actividad AgregarVibracion
     * 
     * @param v Componente que llama a la funcion
    */ 
    public void clickAsignar(View v){
    	progress.setMessage(this.getString(R.string.loading));
    	progress.show();
    	progressOn = true;
    	
    	Intent i = new Intent(Inicio.this,AgregarVibracion.class);
    	startActivityForResult(i, ID);    	   
    }
        
    /**
     *  Este metodo es llamado por el boton Agregar
     *  nueva vibracion.
     * 
     * @param v Componente que llama a la funcion
     */
    public void clickAgregar(View v){
    	Intent i = new Intent(Inicio.this,AddVib.class);
    	startActivityForResult(i, ID);
    }
    
    
    /**
     *  Este metodo es llamado por el boton Preset list
     * 
     * @param v Componente que llama a la funcion
     */
    public void clickPresets(View v){
    	Intent i = new Intent(Inicio.this,ShowPresetList.class);
    	startActivityForResult(i, ID);
    }
    
    /**
     *  Este metodo es llamado por el boton Master Vib
     * 
     * @param v Componente que llama a la funcion
     */
    public void clickMaster(View v){
    	Intent i = new Intent(Inicio.this,MasterMenu.class);
    	startActivityForResult(i, ID);
    }
    
    /**
     *  Este metodo es llamado por el boton List
     * 
     * @param v Componente que llama a la funcion
     */
    public void clickList(View v){
    	Intent i = new Intent(Inicio.this,ListContactsWithVib.class);
    	startActivityForResult(i, ID);
    }
    
    
    
    
	//Menu al pulsar boton menu
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_button_main, menu);
		return true;
	}
    
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		// Handle item selection
		switch (item.getItemId()) {
		
		
			case R.id.menu_main_settings:
				
		    	Intent i = new Intent(Inicio.this,Settings.class);
		    	startActivityForResult(i, ID);
				
				return true;
			
				
			case R.id.menu_main_exit:				
				am.setVibrateSetting(AudioManager.VIBRATE_TYPE_NOTIFICATION, getVibNotificationOriginal());
				am.setVibrateSetting(AudioManager.VIBRATE_TYPE_RINGER, getVibRingerOriginal());
				Inicio.this.finish();
				return true;
			
			
			case R.id.menu_main_credits:
				
				Dialog dialog = new Dialog(this);
				dialog.setContentView(R.layout.credits_layout);
				dialog.setTitle(R.string.credits);				
				dialog.show();				
				return true;
		
				
			default: return super.onOptionsItemSelected(item);
		}
	}

    
    
    
    
    
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == ID){
			switch (resultCode){
			case RESULT_OK:
				break;
			case RESULT_ERROR:
				break;
			case RESULT_SALIR:
				am.setVibrateSetting(AudioManager.VIBRATE_TYPE_NOTIFICATION, getVibNotificationOriginal());
				am.setVibrateSetting(AudioManager.VIBRATE_TYPE_RINGER, getVibRingerOriginal());
				Inicio.this.finish();
			case RESULT_VIBRATION_EDIT_OK:;
				break;
			default:
				
			}
		}
	}
    
    
    
    
    
    //Configuraciones de notificacion originales
	public static int getVibNotificationOriginal() {	
		return vibNotificationOriginal;
	}
	
	public static int getVibRingerOriginal() {
		return vibRingerOriginal;		
	}
    
    
    
    
    
    
    
    
    
    
    
    /////////////////Codigo de muestra////////////////////
    
    
    
    
    
    
    
    //Lee del archivo de config
    public void leeConfig() {
    	
    	
    	   //Creamos una instancia del cm (un handlder)
    	ContactsConfig cm = null;        
        try {
        	cm  = new ContactsConfig();
		} catch (NoFileException e) {

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
		} catch (NoFileException e) {
			Log.e("main", "No se encuentra el archivo de contactos");
		}
    	
    	
    }
    
    
    
    
    
    //Prueba para escribir en el archivo de config
    public void escribirConfig() {
    	
        //Creamos una instancia del cm (un handlder)
    	ContactsConfig cm = null;        
        try {
        	cm  = new ContactsConfig();
		} catch (NoFileException e) {

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
			if (vcl.getVibContactByNumber("34") == null) Log.w("main", "34 no esta");
			//Log.w("main", vcl.getVibContactByNumber("34").getVib().vibToString());
			
			
		} catch (NoContactFoundException e) {
			
			Log.e("main", "No se ha encontrado el contacto: "+e.getMessage());
		}
		
    	
    	
    	
    }
  
    
   
    
    
}