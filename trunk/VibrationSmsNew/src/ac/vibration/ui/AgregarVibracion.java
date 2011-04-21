/**
 *  Poner licencia
 * @author Carlos Diaz Canovas y Marcos Trujillo Seoane
 * 
 */
package ac.vibration.ui;

import ac.vibration.Inicio;
import ac.vibration.R;
import ac.vibration.exceptions.ContactFileErrorException;
import ac.vibration.exceptions.GeneralException;
import ac.vibration.exceptions.NoFileException;
import ac.vibration.exceptions.NoPreferenceException;
import ac.vibration.morse.MorseCode;
import ac.vibration.types.Preset;
import ac.vibration.types.PresetList;
import ac.vibration.types.Vib;
import ac.vibration.types.VibContact;
import ac.vibration.types.VibContactList;
import ac.vibration.ui.quickAction.ActionItem;
import ac.vibration.ui.quickAction.QuickAction;
import ac.vibration.util.Vibration.DoVibration;
import ac.vibration.util.config.AppConfig;
import ac.vibration.util.config.ContactsConfig;
import ac.vibration.util.config.PresetsConfig;
import ac.vibration.util.mToast.mToast;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Vibrator;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Data;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AlphabetIndexer;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SectionIndexer;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;



public final class AgregarVibracion extends ListActivity
{
	public static final int DIALOG_LIST_OPTIONS = 0;
	public static final int DIALOG_STR_MORSE = 1;
	public static final int ID = 2;
	public static final String KEY_CONTACTO = "CONTACTO";
	public static final String KEY_NUMERO= "NUMERO";

	private Cursor cursor;
	private CharSequence[] items;
	public VibContact selectContact;
	public Context mContext;
	public EditText textDialog;
	public Dialog dialogStrMorse;
	public QuickAction q;

	VibContactList vcl;

	/**
	 * Called when the activity is first created. Responsible for initializing the UI.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lista_contactos); 
		
		//Lista de items del dialog cuando se pulsa un contacto
		CharSequence[] aux = {getResources().getString(R.string.edit_vibracion) ,
							getResources().getString(R.string.custom_vibration),
							getResources().getString(R.string.custom_vibration_morse),
							getResources().getString(R.string.name_morse)};
		items = aux;

		try {
			vcl = new ContactsConfig().loadVibContactList();
		} catch (NoFileException e1) {
			Log.e("VS_AgregarVibracion", e1.getMessage());
			e1.printStackTrace();
			setResult(Inicio.RESULT_ERROR);
			mToast.Make(this, "Error al cargar la lista de configuracion.El fichero no existe", 1);
			AgregarVibracion.this.finish();
		} catch (ContactFileErrorException e1) {
			Log.e("VS_AgregarVibracion", e1.getMessage());
			e1.printStackTrace();
			setResult(Inicio.RESULT_ERROR);
			mToast.Make(this, "Error al cargar la lista de configuracion", 1);
			AgregarVibracion.this.finish();
		}

		mContext = this;
		cursor = getContacts();
		startManagingCursor(cursor);
		String[] fields = new String[] {
				Data.DISPLAY_NAME,
				Phone.NUMBER
		};

		int[] to = new int[] { R.id.item_lista_nombre, R.id.item_lista_numero};

		//		SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.item_lista_contactos, cursor,
		//				fields, to);
		MiCursorAdapter adapter = new MiCursorAdapter(this, R.layout.item_lista_contactos, cursor,
				fields, to);

		setListAdapter(adapter);

	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);

		//Obtenemos el contacto seleccionado
		cursor.moveToPosition(position);
		String nombre = cursor.getString(cursor.getColumnIndex(Data.DISPLAY_NAME));
		String phone = cursor.getString(cursor.getColumnIndex(Phone.NUMBER));
		cursor.moveToFirst();

		//Creamos un VibContact para enviarlo por intent
		selectContact = new VibContact(nombre,phone,null);
		//showDialog(DIALOG_LIST_OPTIONS);
		
		createQuickAction(v);

	}

	protected Dialog onCreateDialog(int id) {
		Dialog dialog = null;
		switch(id) {
		case DIALOG_LIST_OPTIONS:
			//Creamos un dialog lista a partir de items
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle(R.string.dialog_selection);
			builder.setItems(items, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int item) {
					selectAction(item);
				}
			});
			return  builder.create();

		case DIALOG_STR_MORSE:
			dialogStrMorse = new Dialog(mContext);
			dialogStrMorse.setContentView(R.layout.dialog_str_morse);
			dialogStrMorse.setTitle(mContext.getResources().getString(R.string.dialog_str_morse_tittle));
			Button buttonDialog= (Button) dialogStrMorse.findViewById(R.id.buttonDialog);
			buttonDialog.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					AppConfig ac = new AppConfig(mContext, AppConfig.CONFIG_NAME_DEF);
					long vi[];
					String aux = getTextDialog().getText().toString();

					//Comprobamos si es nulo lo que se ha introducido
					if ((aux == null) || (aux.equals(""))){
						getTextDialog().setError(getString(R.string.error_missing_name_tomorse));
						return;
					}

					//Obtenemos la vibracion a partir del texto traduciendola a morse
					try {
						vi = MorseCode.stringToVib(aux,ac.getInt(AppConfig.DELAY_INI) , ac.getInt(AppConfig.VELOCIDAD_VIB));
					} catch (NoPreferenceException e) {
						Log.e("VS_AgregarVibracion",e.getMessage());
						vi = MorseCode.stringToVib(selectContact.getName(),2 , 50);
					}

					Vib vibResultante = new Vib (vi);

					//La asignamos y guardamos
					selectContact.setVib(vibResultante);
					vcl.add(selectContact);

					DoVibration.CustomRepeat((Vibrator) mContext.getSystemService(Context.VIBRATOR_SERVICE), vi);
					mToast.Make(mContext, getResources().getString(R.string.vib_add_ok), 0);
					dump();


					//Guardamos la vibracion
					Preset preset = new Preset(aux + getResources().getString(R.string.in_morse), vibResultante);
					PresetsConfig pc;
					try {
						pc = new PresetsConfig();

						PresetList pl = pc.loadPresets();
						pl.add(preset);
						pc.dumpPresetList(pl);
					} catch (NoFileException e) {
						Log.e("VS_AgregarVibracion",e.getMessage());
						mToast.Make(mContext, getResources().getString(R.string.error_guardar_preset), 0);
					} catch (ContactFileErrorException e) {
						mToast.Make(mContext, getResources().getString(R.string.error_guardar_preset), 0);
						Log.e("VS_AgregarVibracion",e.getMessage());
					} catch (GeneralException e) {
						Log.e("VS_AgregarVibracion",e.getMessage());
						mToast.Make(mContext, getResources().getString(R.string.error_guardar_preset), 0);
					}

					dialogStrMorse.cancel();

				}
			});
			textDialog = (EditText) dialogStrMorse.findViewById(R.id.dialog_str_morse_text);
			return dialogStrMorse;

		default:
		}
		return dialog;
	}

	/**
	 * Obtains the contact list for the currently selected account.
	 *
	 * @return A cursor for for accessing the contact list.
	 */
	private Cursor getContacts()
	{
		return  getContentResolver().query(Data.CONTENT_URI,
				new String[] { Data._ID, Data.DISPLAY_NAME, Phone.NUMBER, Phone.TYPE },
				Data.MIMETYPE + "='" + Phone.CONTENT_ITEM_TYPE + "' AND "
				+ Phone.NUMBER + " IS NOT NULL", null,
				Data.DISPLAY_NAME + " ASC");
	}


	/**
	 * Realiza la accion correcta a partir de lo que el usuario
	 * ha elegido en el dialog de acciones
	 * 
	 * @param seleccion Numero de la seleccion elegida
	 */
	private void selectAction(int seleccion){

		switch (seleccion){
		case 0:
			mToast.Make(this,"Elegido edit",0);
			break;
		case 1:
			createVib();
			break;
		case 2:
			showDialog(DIALOG_STR_MORSE);
			break;
		case 3:
			addNameMorse();
			break;
		default:
			return;

		}

	}


	/**
	 * Obtiene la vibracion en morse a partir del nombre y se guarda en ConfigurationManager
	 * 
	 */
	private void addNameMorse(){
		AppConfig ac = new AppConfig(this, AppConfig.CONFIG_NAME_DEF);
		//TODO Mejora preguntar si solo el nombre o todo el nombre
		//Es decir si por ejemplo carlos d�az canovas preguntar si carlos o carlos diaz canovas

		long v[];
		try {
			v = MorseCode.stringToVib(selectContact.getName(),ac.getInt(AppConfig.DELAY_INI) , ac.getInt(AppConfig.VELOCIDAD_VIB));
		} catch (NoPreferenceException e) {
			Log.e("VS_AgregarVibracion",e.getMessage());
			v = MorseCode.stringToVib(selectContact.getName(),2 , 50);
		}
		selectContact.setVib(new Vib(v));
		vcl.add(selectContact);

		DoVibration.CustomRepeat((Vibrator) getSystemService(Context.VIBRATOR_SERVICE), v);
		mToast.Make(this, getResources().getString(R.string.vib_add_ok), 0);
		dump();
	}
	
	
	private void createVib(){
		Intent intent = new Intent(AgregarVibracion.this, AddVib.class);
		intent.putExtra("KEY_CONTACTO", selectContact.getName());
		intent.putExtra("KEY_NUMERO", selectContact.getNumber());
		startActivityForResult(intent, ID);
		
	}
	
	private void createQuickAction(View v){
		
		final ActionItem edit = new ActionItem();
		
		edit.setTitle(mContext.getResources().getString(R.string.edit_vibracion));
		edit.setIcon(getResources().getDrawable(android.R.drawable.ic_menu_manage));
		edit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mToast.Make(mContext,"Elegido edit",0);
				q.dismiss();
			}
		});
		
		final ActionItem createVib = new ActionItem();
		
		createVib.setTitle(mContext.getResources().getString(R.string.custom_vibration));
		createVib.setIcon(getResources().getDrawable(android.R.drawable.ic_menu_edit));
		createVib.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				createVib();
				q.dismiss();
			}
		});
		
		final ActionItem createMorse = new ActionItem();
		
		createMorse.setTitle(mContext.getResources().getString(R.string.custom_vibration_morse));
		createMorse.setIcon(getResources().getDrawable(android.R.drawable.ic_menu_info_details));
		createMorse.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showDialog(DIALOG_STR_MORSE);
				q.dismiss();
			}
		});
		
		
		final ActionItem nameMorse = new ActionItem();
		
		nameMorse.setTitle(mContext.getResources().getString(R.string.name_morse));
		nameMorse.setIcon(getResources().getDrawable(android.R.drawable.ic_menu_my_calendar));
		nameMorse.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				addNameMorse();
				q.dismiss();
			}
		});
		
		q = new QuickAction(v);
		q.addActionItem(edit);
		q.addActionItem(createVib);
		q.addActionItem(createMorse);
		q.addActionItem(nameMorse);
		q.show();
		
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == ID){
			switch (resultCode){
			case Inicio.RESULT_OK:
				break;
			case Inicio.RESULT_ERROR:
				break;
			case Inicio.RESULT_SALIR:
				setResult(Inicio.RESULT_SALIR);
				AgregarVibracion.this.finish();
			case Inicio.RESULT_VIBRATION_EDIT_OK:
				mToast.Make(this, "Vibration Save!", 0);
				break;
			default:

			}
		}
	}


	@Override
	protected void onDestroy() {
		dump();
		super.onDestroy();
	}

	@Override
	protected void onResume() {
		dump();
		super.onResume();
	}

	@Override
	protected void onStop() {
		dump();
		super.onStop();
	}


	public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		switch (actionId) {
		case EditorInfo.IME_ACTION_DONE:
			clickAsignarDialog(v);
			break;
		}
		return false;
	}

	public void dump(){
		try {
			new ContactsConfig().dumpVibContactList(vcl);
		} catch (GeneralException e) {
			Log.e("VS_AgregarVibracion",e.getMessage());
			e.printStackTrace();
		} catch (NoFileException e) {
			Log.e("VS_AgregarVibracion",e.getMessage());
			e.printStackTrace();
		}
		return;
	}

	public void clickAsignarDialog(View v){

	}

	public EditText getTextDialog() {
		return textDialog;
	}

	class MiCursorAdapter extends SimpleCursorAdapter implements SectionIndexer{
		AlphabetIndexer alphaIndexer;

		public MiCursorAdapter(Context context, int layout, Cursor c,
				String[] from, int[] to) {
			super(context, layout, c, from, to);
			// TODO Auto-generated constructor stub
			alphaIndexer=new AlphabetIndexer(c,c.getColumnIndex(Data.DISPLAY_NAME), " ABCDEFGHIJKLMNOPQRSTUVWXYZ");
		}




		@Override
		public void bindView(View view, Context context, Cursor cursor) {
			String phone = cursor.getString(cursor.getColumnIndex(Phone.NUMBER));
			if (vcl.contactExists(phone)){
				ImageView i = (ImageView) view.findViewById(R.id.item_list_image);
				i.setBackgroundResource(R.drawable.btn_check_on);
			}
			else{
				ImageView i = (ImageView) view.findViewById(R.id.item_list_image);
				i.setBackgroundResource(R.drawable.btn_check_off);
			}

			LinearLayout l = (LinearLayout) view.findViewById(R.id.lay_item);
			if ((cursor.getPosition() % 2) == 0){
				l.setBackgroundResource(R.color.white);	
			}
			else {
				l.setBackgroundResource(R.color.lista_yellow);
			}


			super.bindView(view, context, cursor);
		}




		@Override
		public int getPositionForSection(int section) {
			return alphaIndexer.getPositionForSection(section);
		}

		@Override
		public int getSectionForPosition(int position) {
			return alphaIndexer.getSectionForPosition(position);
		}

		@Override
		public Object[] getSections() {
			return alphaIndexer.getSections();
		}


	}

}
