package ac.vibration;

import ac.vibration.util.contactos.AgregarVibracion;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Inicio extends Activity {
    /** Called when the activity is first created. */
	
	public static int ID = 1;
	public static int RETURN_OK = 0;
	public static int RETURN_ERROR = 1;
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
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
}