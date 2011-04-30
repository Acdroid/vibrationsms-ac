/**
 *  Poner licencia
 * @author Carlos Diaz Canovas y Marcos Trujillo Seoane
 * 
 */
package ac.vibration.ui;

import java.util.Iterator;
import java.util.Vector;

import ac.vibration.R;
import ac.vibration.exceptions.ContactFileErrorException;
import ac.vibration.exceptions.NoFileException;
import ac.vibration.morse.MorseCode;
import ac.vibration.types.Preset;
import ac.vibration.types.PresetList;
import ac.vibration.types.Vib;
import ac.vibration.types.VibContact;
import ac.vibration.types.VibContactList;
import ac.vibration.util.Vibration.DoVibration;
import ac.vibration.util.config.ContactsConfig;
import ac.vibration.util.config.PresetsConfig;
import ac.vibration.util.mToast.mToast;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;


public final class MasterMenu extends Activity {

	
	private static final int OPTION_SELECT = 0x0;
	private static final int OPTION_RESET  = 0x1;
	private static final int OPTION_TEST  = 0x2;
	


	/**
	 * Called when the activity is first created. Responsible for initializing the UI.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		//Los layouts necesarios
		setContentView(R.layout.master_dialog);
		ListView list = (ListView)findViewById(R.id.list);
		
		
		//Añadimos las opciones
		String menuItems[] = {
				this.getString(R.string.select_from_list),
				this.getString(R.string.reset_to_default),
				this.getString(R.string.test)
		};
		
	
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.preset_list_item, menuItems);
		list.setAdapter(adapter);
		
		
		
		//Al hacer click en un elemento...
		list.setOnItemClickListener(new OnItemClickListener() {
			
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				
				
				//Cargamos la lista
				try {
					VibContactList vcl = new ContactsConfig().loadVibContactList();
					
					
				      switch (position) {
				      
					      case OPTION_SELECT:
					    	  selectVib();
					    	  break;
					      
					      case OPTION_RESET:
					    	  vcl.add(new VibContact("master", VibContactList.MASTERNUMBER, new Vib(MorseCode.stringToVib("sms",150, 2))));
					    	  new ContactsConfig().dumpVibContactList(vcl);
					    	  mToast.Make(MasterMenu.this, R.string.reset, 0);
					    	  break;
					    	  
					      case OPTION_TEST:
					    	  DoVibration.CustomRepeat((Vibrator) getSystemService(Context.VIBRATOR_SERVICE), vcl.getMasterContact().getVib().get());
					    	  break;
		
					      default:break;
			      }	
					
					
					
				} catch (Exception e) {
					Log.e("MasterMenu", "Error.");
					e.printStackTrace();
				} 
										      			      			   
			}
		});

	}
	
	
	
	
	
	
	
	
	//Abre un dialogo con la lista de vibraciones y elige una al clickar sobre ella
	private void selectVib() throws NoFileException, ContactFileErrorException {
		

		//Lista de contactos
		final VibContactList vcl = new ContactsConfig().loadVibContactList();			
		
		//Leemos la lista de presets y los metemos en un array
		Vector presetsV = new Vector(); 
		
		PresetList pl;
		try {
			pl = new PresetsConfig().loadPresets();
			Iterator iter = pl.getIterator();				
			
			while (iter.hasNext()){
								
				Preset ps = (Preset)iter.next();			
				Log.i("MenuMaster", ps.getName());
				presetsV.add(ps.getName());
			}
						
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
							
		//Pasamos el vector a un array		
		final CharSequence presetsA[] = new CharSequence[presetsV.size()];
		for (int i=0; i<presetsV.size(); i++) presetsA[i] = (CharSequence) presetsV.elementAt(i);
		
				
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.select_vibration);
		final AlertDialog alert;
		
		//Al hacer click en una vibracion
		builder.setSingleChoiceItems(presetsA, -1, new DialogInterface.OnClickListener() {
		    public void onClick(DialogInterface dialog, int item) {
		        
		    	
		    	
				try {
					long vi[] = new PresetsConfig().loadPresets().getPresetByName((String) presetsA[item]).getVib().get();
					
					Vib vibResultante = new Vib(vi);

					//La asignamos y guardamos
					vcl.add(new VibContact("master", VibContactList.MASTERNUMBER, new Vib(vi)));
					
					new ContactsConfig().dumpVibContactList(vcl);
					
					DoVibration.CustomRepeat((Vibrator) getSystemService(Context.VIBRATOR_SERVICE), vi);
					
					MasterMenu.this.finish();
			    	
					
				} catch (Exception e) { e.printStackTrace(); } 
		    			    	
		    }
		});
		
		alert = builder.create();
		alert.show();
		
			
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
