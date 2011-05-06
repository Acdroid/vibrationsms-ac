package ac.vibration;

import java.util.Iterator;

import com.google.ads.*;

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
import ac.vibration.ui.FingerActivity;
import ac.vibration.ui.ListContactsWithVib;
import ac.vibration.ui.MasterMenu;
import ac.vibration.ui.Settings;
import ac.vibration.ui.ShowPresetList;
import ac.vibration.util.Vibration.DoVibration;
import ac.vibration.util.builtIn.defVibBuiltIn;
import ac.vibration.util.config.AppConfig;
import ac.vibration.util.config.ContactsConfig;
import ac.vibration.util.config.PresetsConfig;
import ac.vibration.ui.AgregarVibracion;
import ac.vibration.util.mToast.mToast;
import ac.vibration.util.tools.Tools;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

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
	
	
	public static final int SET_VIBRATION = 0;
	public static final int CREATE_VIBRATION = 1;
	public static final int SAVED_VIBRATION = 2;
	public static final int MASTER_VIBRATION = 3;
	public static final int LIST_CONTACTS = 4;
	
	
	public AdView adView;
	
	
	private static int vibNotificationOriginal;
	private static int vibRingerOriginal;
	
    @Override 
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);        

        

        
        //Verificamos que haya donde escribir
        String message = "", title = "", button = "";
        
        switch (Tools.externalStorage()) {            
        
        	case 0: break;
        	
        	case 1: message = this.getString(R.string.sd_ro);
					title 	= this.getString(R.string.error);
					button 	= this.getString(R.string.ok);
					popup1buttonClose(title, message, button);
					break;
        			
        	case 2: message = this.getString(R.string.no_sd);
        			title 	= this.getString(R.string.error);
        			button 	= this.getString(R.string.ok);
        			popup1buttonClose(title, message, button);
					break;
        
        }
        
        
        setContentView(R.layout.new_main);
        
        /*
        // Create the adView
        AdView adView = new AdView(this, AdSize.BANNER, "a14dbd6cd640261");
        // Lookup your LinearLayout assuming it’s been given
        // the attribute android:id="@+id/mainLayout"
        LinearLayout layout = (LinearLayout)findViewById(R.id.mainLinearLayout);
        // Add the adView to it
        layout.addView(adView);
        // Initiate a generic request to load it with an ad
        adView.loadAd(new AdRequest());
        */
        adView = (AdView)this.findViewById(R.id.Publicidad);
        adView.loadAd(new AdRequest());
        
        
        am = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
        ac = new AppConfig(this, AppConfig.CONFIG_NAME_DEF);
        
        //Creacion de las vibraciones por defecto, por si los desastres
        defVibBuiltIn.builtInDefVib(this);
        
        
        //Se crea, no se muestra
        progress = new ProgressDialog(Inicio.this);
                       
        
        //Los elementos del menú principal
        String[] listItems = new String[] {
        		
        		this.getString(R.string.main_button_asignar),
        		this.getString(R.string.main_button_agregar),
        		this.getString(R.string.vibrations_list),
        		this.getString(R.string.master_vibration),
        		this.getString(R.string.list_contacts)        		
        };
        
        
        //Lista del menú principal
        ListView mainList = (ListView) findViewById(R.id.main_menu_list);        
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.preset_list_item, listItems);
		mainList.setAdapter(adapter);

		//Al hacer click en cada elemento del menu llamamos a diferentes funciones
		mainList.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView parent, View v, int position, long id) {
			
				
				switch (position) {
					
					case SET_VIBRATION:		clickAsignar(v); break;
					case CREATE_VIBRATION:	clickAgregar(v); break;
					case SAVED_VIBRATION:	clickPresets(v); break;
					case MASTER_VIBRATION:	clickMaster(v);  break;
					case LIST_CONTACTS:		clickList(v);	 break;
				
				}
				
				
			}
		});
        
        
        
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
    
    
    
    
	
	
	/**
	 * Popup de alert de 1 boton que cierra el main
	 * */
	public void popup1buttonClose(String title, String message, String button) {
		
		
		AlertDialog dialog=new AlertDialog.Builder(Inicio.this).create();
		dialog.setTitle(title);
		dialog.setMessage(message);
		
		dialog.setButton(button, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {		
				dialog.dismiss();
				Inicio.this.finish();
			}
		});

		dialog.show();
	}
	
	
	
	
	
    
    
    
    
    
    
    
    /////////////////Codigo de muestra////////////////////
    
    
    
    
    
    
    
    //Lee del archivo de config
    public void leeConfig() {
    	
    	
    	   //Creamos una instancia del cm (un handlder)
    	ContactsConfig cm = null;        
        try {
        	cm  = new ContactsConfig(this);
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
        	cm  = new ContactsConfig(this);
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