/**
 * Pner permisos
 * @author Carlos Diaz Canovas y Marcos Trujillo Seoane
 * 
 * @version 1.0
 */
package ac.vibration.util.sms;

import ac.vibration.exceptions.ContactFileErrorException;
import ac.vibration.exceptions.NoContactFileException;
import ac.vibration.exceptions.NoContactFoundException;
import ac.vibration.exceptions.NoVibrationFoundException;
import ac.vibration.types.VibContact;
import ac.vibration.types.VibContactList;
import ac.vibration.util.Vibration.DoVibration;
import ac.vibration.util.config.ConfigManager;
import ac.vibration.util.mToast.mToast;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.telephony.SmsMessage;
import android.widget.Toast;

/**
 * ReceiverSMS.java 09/03/2011
 * @author mtrujillo
 */
public class ReceiverSMS extends BroadcastReceiver {

	private String numTelf[];
	private int index = 0; //index numeros telefono


	//	public static String KEY_SMS = "SMS";
	//	public static String KEY_CONTACTO = "CONTACTO";
	//	private NotificationManager mNotificationManager;
	//	private int SIMPLE_NOTFICATION_ID = 1984;
	//	


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
			//---Mostrar el mensaje del SMS---

			//Para cada telefono recibido comprobamos si tiene una vibracion personificada
			for (int i=0 ; i<numTelf.length ; i ++){


				try {
					VibContactList vcl = new ConfigManager().loadVibContactList();
					VibContact vc = vcl.getVibContactByNumber(numTelf[i]);

					DoVibration.CustomRepeat((Vibrator) mContext.getSystemService(Context.VIBRATOR_SERVICE), vc.getVib().get());
					mToast.Make(mContext, "Vibracion Custom", 0);


//FIXME tratamiento de excepciones mirar Preguntar a Carlos
				}catch (NoContactFoundException e) {
					try {
						DoVibration.CustomRepeat((Vibrator) mContext.getSystemService(Context.VIBRATOR_SERVICE), new ConfigManager().getMasterVibration());
					} catch (NoVibrationFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (NoContactFileException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					mToast.Make(mContext, "Vibracion Master", 0);
					e.printStackTrace();
				}catch (NoContactFileException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ContactFileErrorException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
				Toast.makeText(mContext, "Mensaje de " + numTelf[i], Toast.LENGTH_SHORT).show();
				DoVibration.SMS((Vibrator) mContext.getSystemService(Context.VIBRATOR_SERVICE));
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

}
