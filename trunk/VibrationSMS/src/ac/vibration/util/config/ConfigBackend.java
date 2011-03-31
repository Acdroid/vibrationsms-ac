
package ac.vibration.util.config;

import ac.vibration.R;
import ac.vibration.util.mToast.mToast;
import android.content.Context;

/**
 * CargarPrefPrimTime.java 30/03/2011
 * @author mtrujillo
 */
public class ConfigBackend extends MSharedPreferences{
	public static final String FIRST_TIME = "first_time";
	public static final String IDIOMA_DEF = "idioma_def";
	public static final String DELAY_DEF = "delay_def";
	public static final String DELAY_INICIO_DEF = "delay_inicio_def";
	public static final String BACKGROUND = "background_color";
	public static final String TEXT_COLOR = "text_color";
	
	
		
	
	public ConfigBackend(Context mContext, String name,int firstTime){
		super(mContext,name);
		
		if(!pref.contains(FIRST_TIME)){
			mToast.Make(mContext, "Aplicacion se inicia por primera vez, cargando preferencias", 1);
		
			//Procedemos a cargar los valores por primera vez en las preferencias.
			//Estos valores son por defecto
			
			//Valores por defecto
			put(false,FIRST_TIME); //Flag para indicar que no es la primera vez que se usa
			put(1, IDIOMA_DEF); //Idioma malossi
			put(500, DELAY_DEF); //delay 500 mil entre letras
			put(1000, DELAY_INICIO_DEF);
//			put(R.color.gris, BACKGROUND); //Color de fondo de los layout
			put(R.color.black, TEXT_COLOR); //Color del texto
				
		}
		
	}
}
