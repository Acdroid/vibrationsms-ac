/**
 *  Poner licencia
 * @author Carlos Diaz Canovas y Marcos Trujillo Seoane
 * 
 */
package ac.vibration.util.contactos;

import ac.vibration.R;
import android.app.ListActivity;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.widget.SimpleCursorAdapter;


public final class AgregarVibracion extends ListActivity
{


	/**
	 * Called when the activity is first created. Responsible for initializing the UI.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lista_contactos);     

		Cursor cursor = getContacts();
		startManagingCursor(cursor);
		String[] fields = new String[] {
				Data.DISPLAY_NAME,
				Phone.NUMBER
		};

        int[] to = new int[] { R.id.item_lista_nombre, R.id.item_lista_numero};

		SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.item_lista_contactos, cursor,
				fields, to);

		setListAdapter(adapter);
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

}
