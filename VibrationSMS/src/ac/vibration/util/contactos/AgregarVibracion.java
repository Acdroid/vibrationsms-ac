/**
 *  Poner licencia
 * @author Carlos Diaz Canovas y Marcos Trujillo Seoane
 * 
 */
package ac.vibration.util.contactos;

import ac.vibration.Inicio;
import ac.vibration.R;
import ac.vibration.types.VibContact;
import ac.vibration.util.mToast.mToast;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Data;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AlphabetIndexer;
import android.widget.ListView;
import android.widget.SectionIndexer;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;


public final class AgregarVibracion extends ListActivity
{
	public static final int DIALOG_LIST_OPTIONS = 0;
	public static final int ID = 2;
	//FIXME mejorable por el momento se queda asi
	public static final String op1 = "Edit Vibration";
	public static final String op2 = "Custom Vibration";
	public static final String op3 = "Custom Morse Vibration";
	public static final String op4 = "Name in Morse";


	private Cursor cursor;
	private CharSequence[] items = { op1, op2,op3,op4};
	public VibContact contactSend;

	/**
	 * Called when the activity is first created. Responsible for initializing the UI.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lista_contactos); 


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
		contactSend = new VibContact(nombre,phone,null);
		showDialog(DIALOG_LIST_OPTIONS);

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
			mToast.Make(this,"Elegido Custom vibration", 0);
			break;
		case 2:
			mToast.Make(this,"Elegido Custom Morse", 0);
			break;
		case 3:
			mToast.Make(this,"Elegido Name in morse", 0);
			break;
		default:
			return;

		}

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
	

	class MiCursorAdapter extends SimpleCursorAdapter implements SectionIndexer{
		AlphabetIndexer alphaIndexer;

		public MiCursorAdapter(Context context, int layout, Cursor c,
				String[] from, int[] to) {
			super(context, layout, c, from, to);
			// TODO Auto-generated constructor stub
			Log.d("DEBUG","entro1111");
			alphaIndexer=new AlphabetIndexer(c,c.getColumnIndex(Data.DISPLAY_NAME), " ABCDEFGHIJKLMNOPQRSTUVWXYZ");
		}
      

		

		@Override
		public int getPositionForSection(int section) {
			Log.d("DEBUG","entro");
			return alphaIndexer.getPositionForSection(section);
		}

		@Override
		public int getSectionForPosition(int position) {
			Log.d("DEBUG","entro2");
			return alphaIndexer.getSectionForPosition(position);
		}

		@Override
		public Object[] getSections() {
			Log.d("DEBUG","entro3");
			return alphaIndexer.getSections();
		}


	}

}
