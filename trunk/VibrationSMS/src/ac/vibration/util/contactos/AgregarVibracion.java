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
import android.provider.ContactsContract;
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
        
        
        Cursor cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, new String[] {Phone._ID, Phone.DISPLAY_NAME, Phone.NUMBER}, null, null, null);

        startManagingCursor(cursor);

        String[] from = new String[] { Phone.DISPLAY_NAME, Phone.NUMBER};

        int[] to = new int[] { R.id.item_lista_nombre, R.id.item_lista_numero};

        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.item_lista_contactos, cursor, from, to);
        this.setListAdapter(adapter);
    }
    
}
