/**
 * Indra Software Labs 
 * Departamente de I+D
 *
 * @TacticMovil Todos los derechos reservados 
 */
package ac.vibration.util.mToast;

import android.content.Context;
import android.widget.Toast;

/**
 * MuestraTexto.java 24/03/2011
 * @author mtrujillo
 */
public class mToast {
	
	
	/**
	 * <b>Make</b><br><br>
	 *   public void Make()<br>
	 * <ul>Metodo de apoyo para mostrar un mensaje Toast por pantalla.</ul><br><br>
	 * @param mContext Actividad donde se quiere mostrar el mensaje, normalmente this
	 * @param text Texto del mensaje
	 * @param duracion 0 si queremos una duracion corta, 1 si queremos  una duracion larga
	 */
	public static void Make(Context mContext,String text,int duracion){
		Toast.makeText(mContext, text, (duracion ==  0 ? Toast.LENGTH_SHORT : Toast.LENGTH_LONG) ).show();
	}

}
