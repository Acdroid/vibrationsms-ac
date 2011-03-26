/**
 *  Poner licencia
 * @author Carlos Diaz Canovas y Marcos Trujillo Seoane
 * 
 */
package ac.vibration.util.contactos;

import ac.vibration.R;
import android.app.ListActivity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Data;
import android.util.Log;
import android.view.View;
import android.widget.AlphabetIndexer;
import android.widget.ListView;
import android.widget.SectionIndexer;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TwoLineListItem;


public final class AgregarVibracion extends ListActivity
{
	
	public Cursor cursor;


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
        
        cursor.moveToPosition(position);
        String nombre = cursor.getString(cursor.getColumnIndex(Data.DISPLAY_NAME));
        Toast.makeText(this, nombre + " pos " + position, Toast.LENGTH_LONG).show();
        cursor.moveToFirst();
        
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
