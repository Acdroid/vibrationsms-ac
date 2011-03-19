package ac.vibration.util.SDCard;

import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

/**
 * @author mtrujillo
 *
 */
public class CheckSDCard {
	
	/**
	 * <b> comprobarSDCard</b><br><br>
	 *    public static Boolean comprobarSDCard()<br>
	 * <ul>Esta clase se ha implementado para facilitar la
	 * comprobacion del estado de la tarjeta SDCard.
	 * Si el estado es correcto se devolver� true.
	 * Si el estado es incorrecto se obtendra un mensaje por
	 * pantalla informando del error y se devolvera false.
	 * </ul><br><br>
	 * @return True si la SDCard esta correctametne o false e.o.c.
	 */
	public static Boolean comprobarSDCard(Context mContext){
		
		String auxSDCardStatus = Environment.getExternalStorageState();
		
		if (auxSDCardStatus.equals(Environment.MEDIA_MOUNTED))
			return true;
		else if (auxSDCardStatus.equals(Environment.MEDIA_MOUNTED_READ_ONLY)){
			Toast.makeText(mContext, "Warning, la tarjeta SDCard s�lo se puede leer.\nEsto no impedir� el buen funcionamiento" +
					"de la aplicaci�n de lectura", Toast.LENGTH_LONG).show();
			return true;
		}
		else if(auxSDCardStatus.equals(Environment.MEDIA_NOFS)){
			Toast.makeText(mContext, "Error, la tarjeta SDCard no se puede usar, no tiene un formato aceptado o no est� " +
					"correctamente formateada.", Toast.LENGTH_LONG).show();
			return false;
		}
			
		else if(auxSDCardStatus.equals(Environment.MEDIA_REMOVED)){
			Toast.makeText(mContext, "Error, la tarjeta SDCard no se encuentra, para utilizar el lector necesita" +
					"insertar una tarjeta SD Card en el m�vil.", Toast.LENGTH_LONG).show();
			return false;
		}
		else if(auxSDCardStatus.equals(Environment.MEDIA_SHARED)){
			Toast.makeText(mContext, "Error, la tarjeta SDCard no est� montada porque se est� utilizando" +
					"conectada mediante USB. Desconectela y vuelva a intentarlo.", Toast.LENGTH_LONG).show();
			return false;
		}
		else if (auxSDCardStatus.equals(Environment.MEDIA_UNMOUNTABLE)){
			Toast.makeText(mContext, "Error, la tarjeta SDCard no puede montarse.\nEsto puede deberse a que se encuentra corrupta" +
					"o da�ada.", Toast.LENGTH_LONG).show();
			return false;
		}
		else if (auxSDCardStatus.equals(Environment.MEDIA_UNMOUNTED)){
			Toast.makeText(mContext, "Error, la tarjeta SDCard est� presente en el m�vil pero no est� montada." +
					"M�ntela antes de usar el programa.", Toast.LENGTH_LONG).show();
			return false;
		}
		
		
		return true;
	}

}
