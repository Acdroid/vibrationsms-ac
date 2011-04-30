/**
 *  Poner licencia
 * @author Carlos Diaz Canovas y Marcos Trujillo Seoane
 * 
 */
package ac.vibration.ui;

import java.util.Iterator;
import java.util.Vector;

import ac.vibration.R;
import ac.vibration.exceptions.GeneralException;
import ac.vibration.exceptions.NoFileException;
import ac.vibration.types.Preset;
import ac.vibration.types.PresetList;
import ac.vibration.util.Vibration.DoVibration;
import ac.vibration.util.config.PresetsConfig;
import ac.vibration.util.mToast.mToast;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;


public final class ShowPresetList extends Activity {

	
	private static final int CONTEXT_RENAME = 0x0;
	private static final int CONTEXT_DELETE = 0x1;
	private static final int CONTEXT_TEST = 0x2;
	
	private static final int ID = 2;
	
	//Aqui se guarda la lista de presets
	private String[] presetsA;
	

	/**
	 * Called when the activity is first created. Responsible for initializing the UI.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		//Los layouts necesarios
		setContentView(R.layout.presets_list);
		ListView list = (ListView)findViewById(R.id.list);
	
				
		populatePresetList();

		
		//Creación del menu contectual
		registerForContextMenu(list);
		
		
		
	}


	
	
	/**
	 * Formamos la lista
	 * */
	private void populatePresetList() {
		
		
		ListView list = (ListView)findViewById(R.id.list);
		
		//Leemos la lista de presets y los metemos en un array
		Vector presetsV = new Vector(); 
		
		PresetList pl;
		try {
			pl = new PresetsConfig().loadPresets();
			Iterator iter = pl.getIterator();				
			
			while (iter.hasNext()){
								
				Preset ps = (Preset)iter.next();			
				Log.i("ShowPresetList", ps.getName());
				presetsV.add(ps.getName());
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
		
						
		//Pasamos el vector a un array para poder meterlos en el listView		
		presetsA = new String[presetsV.size()];
		for (int i=0; i<presetsV.size(); i++)
			presetsA[i] = (String) presetsV.elementAt(i);
					
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.preset_list_item, presetsA);
		list.setAdapter(adapter);
		
		
	}
	
	
	
	
	
	
	/**
	 * Creamos el menú de opciones
	 */
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		
	  if (v.getId()==R.id.list) {
		  
	    AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
	    menu.setHeaderTitle(presetsA[info.position]);
	    	    	    
	    menu.add(Menu.NONE, CONTEXT_RENAME, CONTEXT_RENAME, R.string.rename);
	    menu.add(Menu.NONE, CONTEXT_DELETE, CONTEXT_DELETE, R.string.delete);
	    menu.add(Menu.NONE, CONTEXT_TEST, CONTEXT_TEST, R.string.test);
	    
	    
	  }
	}
	
	
	
	
	/**
	 * Al hacer click en un elemento
	 * */
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		
		try {
			final PresetsConfig pc = new PresetsConfig(); 
			final PresetList pl = pc.loadPresets();
		
			
			AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
		  
			//El preset pinchado
			final String presetName = presetsA[info.position];	  
		  
			//La opcion del context seleccionada
			int menuItemIndex = item.getItemId();	  
		  
			//Opcion seleccionada
			switch (menuItemIndex) {
		
				
				case CONTEXT_RENAME:
					
					
					//Abrimos el dialogo para escribir
					final Dialog dialog = new Dialog(this);
					dialog.setContentView(R.layout.choose_name);
					dialog.setTitle(R.string.vibration_saved_title);
					Button saveDialogB  = (Button) dialog.findViewById(R.id.chooserSaveButton);
					
					final EditText textName = (EditText) dialog.findViewById(R.id.chooserName);
					textName.setText(presetName);
					textName.selectAll();
					
					
					//Cuando se pulsa sobre guardar
					saveDialogB.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							
							
							//Hace falta un nombre !
							if (textName.getText().toString().compareTo("") != 0) {
								
																							
								//Creamos un preset nuevo, borramos el anterior y lo guardamos
								try {
									Preset p = new Preset(textName.getText().toString(), pl.getPresetByName(presetName).getVib());
									pl.remove(pl.getPresetByName(presetName));
									pl.add(p);
						  			pc.dumpPresetList(pl);
						  			populatePresetList();
									
								} catch (GeneralException e) {
									e.printStackTrace();
									mToast.Make(ShowPresetList.this, R.string.error, 0);
								}
								
								dialog.dismiss();
								//ShowPresetList.this.finish();
							}
							else mToast.Make(ShowPresetList.this, R.string.write_something, 0);
							
						}
					});
					
					dialog.show();
					
					
				break;
		  		
				
				
		  		case CONTEXT_DELETE:
		  			pl.remove(pl.getPresetByName(presetName));
		  			pc.dumpPresetList(pl);
		  			populatePresetList();
			  	break;
			  	
			  	
			  	
		  		case CONTEXT_TEST:
		  			DoVibration.CustomRepeat((Vibrator) getSystemService(Context.VIBRATOR_SERVICE), pl.getPresetByName(presetName).getVib().get());
		  		break;
		  				  		
			  	
			  	
	
		  		default:break;
			}
			
		} catch (Exception e) {
			
			mToast.Make(this, R.string.error, 0);
		}
	  	  
	  
	  return true;
	}
	
	
    
    /**
     *  Este metodo es llamado por el boton New
     */
    public void clickAgregar(View v){
    	Intent i = new Intent(ShowPresetList.this,AddVib.class);
    	startActivityForResult(i, ID);    	
    }

    /**
     *  Este metodo es llamado por el boton Refresh
     */
    public void clickRefresh(View v){
    	populatePresetList();    	
    }
	
    /**
     *  Este metodo es llamado por el boton Limpiar
     */
    public void clickClear(View v){
    	

    	

		final AlertDialog.Builder alert = new AlertDialog.Builder(ShowPresetList.this);

		alert.setTitle(R.string.information);
		alert.setMessage(R.string.preset_clear_question);

		alert.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
		public void onClick(DialogInterface dialog, int whichButton) {
			
			//Limpiamos...
	    	try {
				new PresetsConfig().deleteConfig();
			} catch (NoFileException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			//...y actualizamos
			populatePresetList();
													
		  }
		});

		alert.setNegativeButton(R.string.no, null);
				
		alert.show();
    	
    	
   
    }
    
    
    
	
 

}


