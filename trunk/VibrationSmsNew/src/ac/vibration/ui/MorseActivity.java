package ac.vibration.ui;

import ac.vibration.R;
import ac.vibration.morse.MorseCode;
import ac.vibration.types.Preset;
import ac.vibration.types.PresetList;
import ac.vibration.types.Vib;
import ac.vibration.util.Vibration.DoVibration;
import ac.vibration.util.config.PresetsConfig;
import ac.vibration.util.mToast.mToast;
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
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;


/**
 * Actividad para crear una vibracion en morse
 * */
public class MorseActivity extends Activity {
	
	public Context pContext;
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*
        final Context mContext = this;
        final Dialog dialog = new Dialog(mContext);
        final EditText textName = (EditText) dialog.findViewById(R.id.chooserName);
        */
        pContext = this.getParent();
                

        setContentView(R.layout.dialog_str_morse_save);
        
             
        
        Button testB   =  (Button) findViewById(R.id.morseTestButton);
        Button saveB   =  (Button) findViewById(R.id.morseSaveButton);
        Button resetB  =  (Button) findViewById(R.id.morseResetButton);
        
        
        
        //Al pulsar TEST
        testB.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				
				
				
				//Sacamos el texto que hay en el edit text
				EditText texto = (EditText) findViewById(R.id.morseText);
				
				
				if (texto.getText().toString().compareTo("") != 0) {
					SeekBar delayBar = (SeekBar) findViewById(R.id.morseDelaySeek);
					SeekBar speedBar = (SeekBar) findViewById(R.id.morseSpeedSeek);
									
					int speed = (100-speedBar.getProgress())/10;
					if (speed == 0) speed++;
					
					long[] vi = MorseCode.stringToVib(texto.getText().toString(), delayBar.getProgress()*20, speed);
					
					DoVibration.CustomRepeat((Vibrator) getSystemService(Context.VIBRATOR_SERVICE), vi);
				}

			}
		});
        
        
        
        //Al pulsar RESET
        resetB.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
								
				//Sacamos el texto que hay en el edit text
				EditText texto = (EditText) findViewById(R.id.morseText);
				texto.setText("");
				
				mToast.Make(MorseActivity.this.getParent(), R.string.reset, 0);
				

								
			}
		});
        
        
        
        
        
        //Pulsar SAVE
        saveB.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				//El texto a convertir
				final EditText texto = (EditText) findViewById(R.id.morseText);
				
				
				
				//Solo mostramos el dialogo de guardar si ha escrito algo				
				if (texto.getText().toString().compareTo("") != 0) {
					
					final Dialog dialog = new Dialog(MorseActivity.this.getParent());
					dialog.setContentView(R.layout.choose_name);
					dialog.setTitle(R.string.vibration_saved_title);
					
					final EditText textName = (EditText) dialog.findViewById(R.id.chooserName);
					textName.setText("[Morse] ");
					textName.selectAll();
							
					//Al pulsar el Guardar del dialogo que sale al pulsar guardar
					Button saveDialogB  = (Button) dialog.findViewById(R.id.chooserSaveButton);				
					
					
					saveDialogB.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							
																				
							//Dialog dialog = new Dialog(MorseActivity.this);
							//TextView textName = (TextView) dialog.findViewById(R.id.chooserName);
							
							//Hace falta un nombre !
							if (textName.getText().toString().compareTo("") != 0) {
														
								SeekBar delayBar = (SeekBar) findViewById(R.id.morseDelaySeek);							
								SeekBar speedBar = (SeekBar) findViewById(R.id.morseSpeedSeek);
								
												
								int speed = (100-speedBar.getProgress())/10;
								if (speed == 0) speed++;
								
								final long[] vt = MorseCode.stringToVib(texto.getText().toString(), delayBar.getProgress()*20, speed);
																
								
								//La guardamos en la lsta de presets
				 				final PresetList pl;
								try {
									pl = new PresetsConfig().loadPresets();
									
									
									//El preset ya existe !
									if (pl.getPresetByName(textName.getText().toString()) != null) {
										
										final AlertDialog.Builder alert = new AlertDialog.Builder(MorseActivity.this.getParent());

										alert.setTitle(R.string.information);
										alert.setMessage(R.string.preset_exist_overwrite_question);

										alert.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
										public void onClick(DialogInterface dialog, int whichButton) {
											pl.add(new Preset(textName.getText().toString(), new Vib(vt)));
											
											try {
												new PresetsConfig().dumpPresetList(pl);
											} catch (Exception e) {
												e.printStackTrace();
											}
											
											MorseActivity.this.finish();										
										  }
										});

										alert.setNegativeButton(R.string.no, null);
										
										alert.show();
									}
									
									//El preset no existe
									else {																					
										pl.add(new Preset(textName.getText().toString(), new Vib(vt)));
										new PresetsConfig().dumpPresetList(pl);
										MorseActivity.this.finish();
									}

								} catch (Exception e) {
									e.printStackTrace();
								} 
							}
							//Si no hay nada escrito
							else mToast.Make(MorseActivity.this.getParent(), R.string.write_something, 0);
						}
					});
					
	
					dialog.show();
				}
				
				//Si no hay nada escrito
				else {
					
					mToast.Make(MorseActivity.this.getParent(), R.string.write_something, 0);
					
				}
				
				
				

			}
		});
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
    }
}