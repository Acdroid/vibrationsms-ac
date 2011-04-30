package ac.vibration.util.phone;

import ac.vibration.exceptions.NoPreferenceException;
import ac.vibration.exceptions.NoVibrationFoundException;
import ac.vibration.types.VibContact;
import ac.vibration.types.VibContactList;
import ac.vibration.util.Vibration.DoVibration;
import ac.vibration.util.config.AppConfig;
import ac.vibration.util.config.ContactsConfig;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.telephony.TelephonyManager;
import android.util.Log;



public class ReceiverCall extends BroadcastReceiver {

	
	

	VibContactList vcl = null;
	
	
	
	
	@Override
	public void onReceive(Context mContext, Intent intent) {
		
		AppConfig ac =  new AppConfig(mContext, AppConfig.CONFIG_NAME_DEF);
		
		boolean vibrateOnCall = true;		
		try { vibrateOnCall = ac.getBool(AppConfig.VIBRATE_ON_CALL);			
		} catch (NoPreferenceException e1) {e1.printStackTrace();}
		
		
		//Hacemos todo solo si el usuario lo ha elegido, claro :D
		if (vibrateOnCall) {
		
			Bundle extras = intent.getExtras();
			
			if (extras != null) {
				
				//Vemos que esta pasando
				String state = extras.getString(TelephonyManager.EXTRA_STATE);																	
				
				
				
				//Si nos estan llamando mostramos el numero
				if (state.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
					String phoneNumber = extras.getString(TelephonyManager.EXTRA_INCOMING_NUMBER);
					
					Log.w("ReceiverCall", phoneNumber);
														
					
					
					
					try {
					
					
						//Cargamos la lista
						vcl = new ContactsConfig().loadVibContactList();
						
						//Buscamos el contacto por si tiene una vibracion propia
						VibContact vc = vcl.getVibContactByNumber(phoneNumber);
						
						//Si se ha encontrado su vib propia se vibra
						if (vc != null) {
							long[] v = vc.getVib().get();
							DoVibration.CustomRepeatInfinity((Vibrator) mContext.getSystemService(Context.VIBRATOR_SERVICE), v);
						}
						
						//Sino ejecutamos la master
						else doVibrationMaster(mContext);
						
					} catch(Exception e) {
						e.printStackTrace();
					}					
				}
				
				//Cuando ha parado de sonar por lo que sea, dejamos de vibrar !
				else {
					Log.i("ReceiverCall", "Cancelando Vibracion");
					Vibrator vt = (Vibrator) mContext.getSystemService(Context.VIBRATOR_SERVICE);
					vt.cancel();					
				}
			}
		}
	}
	
	
	
	
	
	public void doVibrationMaster(Context mContext){
		
		try {
			
			
			VibContact masterContact = vcl.getMasterContact(); 
			
			//Sacamos la vibración master
			if (masterContact != null) {
				long[] v = masterContact.getVib().get();
				DoVibration.CustomRepeat((Vibrator) mContext.getSystemService(Context.VIBRATOR_SERVICE),v);
			}
			
		} catch (NoVibrationFoundException e) {
			e.printStackTrace();
		}
		
	}
	
	
	
	
	
	
	
	
	
}