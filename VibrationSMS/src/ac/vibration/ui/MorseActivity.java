package ac.vibration.ui;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;


/**
 * Actividad para crear una vibración en morse
 * */
public class MorseActivity extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TextView textview = new TextView(this);
        textview.setText("MORSE ME ON !");
        setContentView(textview);
    }
}