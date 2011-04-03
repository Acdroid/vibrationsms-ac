/*
 * ReceiverSMS 
 * 
 * 
 * @author Carlos Diaz Canovas y Marcos Trujillo Seoane
 * @version 1.0
 */
package ac.vibration.util.sms;

import ac.vibration.R;
import ac.vibration.exceptions.ContactFileErrorException;
import ac.vibration.exceptions.NoFileException;
import ac.vibration.exceptions.NoContactFoundException;
import ac.vibration.exceptions.NoPreferenceException;
import ac.vibration.morse.MorseCode;
import ac.vibration.types.VibContact;
import ac.vibration.types.VibContactList;
import ac.vibration.util.Vibration.DoVibration;
import ac.vibration.util.config.AppConfig;
import ac.vibration.util.config.ContactsConfig;
import ac.vibration.util.mToast.mToast;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

/**
 * ReceiverSMS.java 09/03/2011
 * 
 * <ul>Esta clase esta implementada para agregarse como servicio o
 * esuchador (Requiere de permisos de usuario en androidManifest)
 * para que al recibir un SMS compruebe si el que lo envía
 * esta en la lista de contactos con vibraciones especiales y
 * en tal caso realiza la vibracion.
 * @author mtrujillo
 */
public class ReceiverSMS extends BroadcastReceiver {

	private String numTelf[];
	private int index = 0; //index numeros telefono


	//	public static String KEY_SMS = "SMS";
	//	public static String KEY_CONTACTO = "CONTACTO";
	//	private NotificationManager mNotificationManager;
	//	private int SIMPLE_NOTFICATION_ID = 1984;


	/* (non-Javadoc)
	 * @see android.content.BroadcastReceiver#onReceive(android.content.Context, android.content.Intent)
	 */
	@Override
	public void onReceive(Context mContext, Intent intent) {

		//---Recuperamos el SMS---
		Bundle bundle = intent.getExtras();        
		SmsMessage[] msgs = null;

		if (bundle != null)
		{
			//---Recuperamos el mensaje---
			// PDU = protocol discription unit
			Object[] pdus = (Object[]) bundle.get("pdus");
			msgs = new SmsMessage[pdus.length];            
			numTelf= new String[pdus.length];
			//Extraemos los numeros de telefono
			for (int i=0; i<msgs.length; i++){
				msgs[i] = SmsMessage.createFromPdu((byte[])pdus[i]);                
				numTelf [index] = msgs[i].getOriginatingAddress();    
				index++;
			}
			//Vamos anecesitar la lista en todo el scope
			VibContactList vcl = null;

			//Para cada telefono recibido comprobamos si tiene una vibracion personificada
			for (int i=0 ; i<numTelf.length ; i ++){


				try {
					//Cargamos la lista
					vcl = new ContactsConfig().loadVibContactList();
					//Buscamos el contacto por si tiene una vibracion propia
					VibContact vc = vcl.getVibContactByNumber(numTelf[i]);
					//Y a vibrar se ha dicho
					DoVibration.CustomRepeat((Vibrator) mContext.getSystemService(Context.VIBRATOR_SERVICE), vc.getVib().get());
					//FIXME 
					mToast.Make(mContext, "Vibracion Custom", 0);
					//En caso de no encontrar la vibracion personalizada ponemos la master
				}catch (NoContactFoundException e) {
					doVibrationMaster(mContext);			
					//Errores en el archivo	
				} catch (NoFileException e) {
					Log.e("ReceiverSMS", "Cannot find config file");
					doVibrationMaster(mContext);
				} catch (ContactFileErrorException e) {										
					Log.e("ReceiverSMS", "Config file contains errors");
					doVibrationMaster(mContext);
				}

				//Informamos por pantalla 
				//FIXME ?¿?¿?¿?
				Toast.makeText(mContext, mContext.getResources().getString(R.string.smsfrom) + numTelf[i], Toast.LENGTH_SHORT).show();
			}


			//creaNotification(textoSMS,mContext,msgs[0].getOriginatingAddress());

		}
	}

	//	private void creaNotification(String textSMS,Context mContext,String contacto){
	//		mNotificationManager = (NotificationManager)mContext.getSystemService(Context.NOTIFICATION_SERVICE);
	//        Notification notifyDetails = new Notification(R.drawable.tacticlogo2,"Nuevo SMS",System.currentTimeMillis());
	//        Intent i = new Intent (mContext,LeerSMS.class);
	//        i.putExtra(KEY_SMS, textSMS);
	//        i.putExtra(KEY_CONTACTO, contacto);        
	//        //PendingIntent myIntent = PendingIntent.getActivity(mContext, 0, new Intent(mContext, SMSNuevosViejos.class), PendingIntent.FLAG_UPDATE_CURRENT);
	//        PendingIntent myIntent = PendingIntent.getActivity(mContext, 0, i, 0);
	//        notifyDetails.setLatestEventInfo(mContext, textSMS, "Pulsa para abrir", myIntent);
	//        notifyDetails.flags |= Notification.FLAG_AUTO_CANCEL;
	//        mNotificationManager.notify(SIMPLE_NOTFICATION_ID, notifyDetails);
	//	}

	
	public void doVibrationMaster(Context mContext){
		AppConfig ac =  new AppConfig(mContext, AppConfig.CONFIG_NAME_DEF);
		
		try {			
			//Comprobamos si se desea realizar vibracion master
			boolean aux = ac.getBool(AppConfig.DO_VIB_MASTER);
			if (!aux)
				return;
			
			//Obtenemos el delay y la velocidad por defecto en la configuracion
			int delay = ac.getInt(AppConfig.DELAY_INI);
			int speed = ac.getInt(AppConfig.VELOCIDAD_VIB);

			DoVibration.CustomRepeat((Vibrator) mContext.getSystemService(Context.VIBRATOR_SERVICE), MorseCode.stringToVib("sms", delay, speed));
		} catch (NoPreferenceException e1) {
			Log.e("ReceiverSMS","Unable to obtain the configuration parameter");
			return;
		}
		//FIXME
		mToast.Make(mContext, "Vibracion Master", 0);		
	}
}
