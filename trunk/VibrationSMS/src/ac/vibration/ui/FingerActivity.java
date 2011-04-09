package ac.vibration.ui;

import ac.vibration.R;
import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;


/**
 * Actividad para crear una vibración con los dedos manualmente :)
 * */
public class FingerActivity extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*
        TextView textview = new TextView(this);
        textview.setText("SQUISH YOUR FINGERS !");
        setContentView(textview);
        */
        
        
        setContentView(R.layout.fingerlayout);

        
    }
}