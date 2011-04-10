
package ac.vibration.util.config;

import ac.vibration.R;
import ac.vibration.exceptions.ContactFileErrorException;
import ac.vibration.exceptions.GeneralException;
import ac.vibration.exceptions.NoFileException;
import ac.vibration.morse.MorseCode;
import ac.vibration.types.Vib;
import ac.vibration.types.VibContact;
import ac.vibration.types.VibContactList;
import ac.vibration.util.mToast.mToast;
import android.content.Context;
import android.util.Log;

/**
 * AppConfig.java 30/03/2011
 * 
 * Esta clase, que extiende de la clase MSharedPreferences,
 * se encarga de interceder entre esta y la aplicacion
 * facilitando su uso. Contiene una serie de Static con
 * todas las posibles preferencias que se pueden guardar.
 * Durante el primer uso guarda la configuración inicial.
 * @author mtrujillo & cdiaz
 */
public class AppConfig extends MSharedPreferences{
	public static final String CONFIG_NAME_DEF = "VibrationSMSConfig";
	public static final String FIRST_TIME = "first_time";
	public static final String DELAY_INI = "delay_ini";
	public static final String DELAY_ENTRE_VIB = "delay_entre_vib";
	public static final String VELOCIDAD_VIB = "velocidad_vib";
	public static final String DO_VIB_MASTER = "do_vib_master";
	
	VibContactList vcl;
	
		
	
	public AppConfig(Context mContext, String name){
		super(mContext,name);
		
		//Comprobamos si es la primera vez que se llama alconstructor, en tal caso
		//se guardan los valores iniciales
		if(!pref.contains(FIRST_TIME)){
			mToast.Make(mContext,mContext.getResources().getString(R.string.first_time_toast), 1);
		
			//Procedemos a cargar los valores por primera vez en las preferencias.
			//Estos valores son por defecto
			
			//Valores por defecto
			put(false,FIRST_TIME); //Flag para indicar que no es la primera vez que se usa
			put(150, DELAY_INI); //delay inicio, predefinido a 0
			put(50, DELAY_ENTRE_VIB); //delay entre vibraciones, predefinido a 40 milisegundos
			put(2, VELOCIDAD_VIB);
			put(true,DO_VIB_MASTER); //Se realiza vibmaster cuando llega un sms de contacto desconocido?
			
			
			//Agregamos vibracion master
			try {
				vcl = new ContactsConfig().loadVibContactList();
				vcl.add(new VibContact("master", VibContactList.MASTERNUMBER, new Vib(MorseCode.stringToVib("sms",150, 2))));
			} catch (NoFileException e1) {
				Log.e("VS_AppConfig", e1.getMessage());
				e1.printStackTrace();
			} catch (ContactFileErrorException e1) {
				Log.e("VS_AppConfic", e1.getMessage());
				e1.printStackTrace();
			}
			
			try {
				new ContactsConfig().dumpVibContactList(vcl);
			} catch (GeneralException e) {
				Log.e("VS_AppConfig",e.getMessage());
				e.printStackTrace();
			} catch (NoFileException e) {
				Log.e("VS_AppConfig",e.getMessage());
				e.printStackTrace();
			}
			return;
			
		
		}
		
	}
}
