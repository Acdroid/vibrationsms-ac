package ac.vibration.ui;

import ac.vibration.R;
import ac.vibration.morse.MorseCode;
import ac.vibration.types.Preset;
import ac.vibration.types.PresetList;
import ac.vibration.types.Vib;
import ac.vibration.util.Vibration.DoVibration;
import ac.vibration.util.config.PresetsConfig;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;


/**
 * Actividad para crear una vibracion en morse
 * */
public class MorseActivity extends Activity {
	
		
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*
        final Context mContext = this;
        final Dialog dialog = new Dialog(mContext);
        final EditText textName = (EditText) dialog.findViewById(R.id.chooserName);
        */

        setContentView(R.layout.dialog_str_morse_save);
        
        
        Button testB   =  (Button) findViewById(R.id.morseTestButton);
        Button saveB   =  (Button) findViewById(R.id.morseSaveButton);
        Button resetB  =  (Button) findViewById(R.id.morseResetButton);
        
        
        
        //Al pulsar TEST
        testB.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				
				
				//Sacamos el texto que hay en el edit text
				EditText texto = (EditText) findViewById(R.id.morseText);
				SeekBar delayBar = (SeekBar) findViewById(R.id.morseDelaySeek);
				SeekBar speedBar = (SeekBar) findViewById(R.id.morseSpeedSeek);
								
				int speed = (100-speedBar.getProgress())/10;
				if (speed == 0) speed++;
				
				long[] vi = MorseCode.stringToVib(texto.getText().toString(), delayBar.getProgress()*20, speed);
				
				DoVibration.CustomRepeat((Vibrator) getSystemService(Context.VIBRATOR_SERVICE), vi);
				
				
				return false;
			}
		});
        
        
        
        //Al pulsar RESET
        resetB.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				
				
				//Sacamos el texto que hay en el edit text
				EditText texto = (EditText) findViewById(R.id.morseText);

				texto.setText("");				
				
				return false;
			}
		});
        
        
        
        
        
        //Pulsar SAVE
        saveB.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				
				
				//Dialog dialog = new Dialog(MorseActivity.this.getParent());
				final Dialog dialog = new Dialog(MorseActivity.this.getParent());
				//final Dialog dialog = new Dialog(mContext);
				dialog.setContentView(R.layout.choose_name);
				dialog.setTitle(R.string.vibration_saved_title);
						
				//Al pulsar el Guardar del dialogo que sale al pulsar guardar
				Button saveDialogB  = (Button) dialog.findViewById(R.id.chooserSaveButton);
				
				
				
				saveDialogB.setOnTouchListener(new OnTouchListener() {
					
					@Override
					public boolean onTouch(View v, MotionEvent event) {
						
												
						
						//Dialog dialog = new Dialog(MorseActivity.this);
						TextView textName = (TextView) dialog.findViewById(R.id.chooserName);
						
						
						//Creamos la vib
						//Sacamos el texto que hay en el edit text
						EditText texto = (EditText) findViewById(R.id.morseText);
						SeekBar delayBar = (SeekBar) findViewById(R.id.morseDelaySeek);
						SeekBar speedBar = (SeekBar) findViewById(R.id.morseSpeedSeek);
						
						delayBar.setProgress(10);
						speedBar.setProgress(80);
										
						int speed = (100-speedBar.getProgress())/10;
						if (speed == 0) speed++;
						
						long[] vi = MorseCode.stringToVib(texto.getText().toString(), delayBar.getProgress()*20, speed);
						
						
						
		 				//La guardamos en la lsta de presets
		 				PresetList pl;
						try {
							pl = new PresetsConfig().loadPresets();
							pl.add(new Preset(textName.getText().toString(), new Vib(vi)));							
							new PresetsConfig().dumpPresetList(pl);
						} catch (Exception e) {
							e.printStackTrace();
						} 
						
						MorseActivity.this.finish();
						
						return false;
					}
				});
				

				dialog.show();

				
				
				
				
				
				
				
				
				
				
				
				
				

				 				 		
				return false;
			}
		});
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
    }
}