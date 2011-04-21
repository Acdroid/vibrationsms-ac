package ac.vibration.ui;

import java.util.Random;
import java.util.Vector;

import ac.vibration.R;
import ac.vibration.exceptions.ContactFileErrorException;
import ac.vibration.exceptions.GeneralException;
import ac.vibration.exceptions.NoFileException;
import ac.vibration.morse.MorseCode;
import ac.vibration.types.Preset;
import ac.vibration.types.PresetList;
import ac.vibration.types.Vib;
import ac.vibration.types.VibContact;
import ac.vibration.types.VibContactList;
import ac.vibration.util.Vibration.DoVibration;
import ac.vibration.util.config.ContactsConfig;
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
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;


/**
 * Actividad para crear una vibracion con los dedos manualmente :)
 * */
public class FingerActivity extends Activity {
	
	private String name;
	private String number;
	
	private boolean pressed = false;
	
	public FrameLayout redBox ;
	public Button tapB;
	public Context mContext = this;
		
	long tStamp = 0;
	long timePressed = 0;
	
	//Aqui se guarda inicialmente la vibracion
	Vector vibVec = new Vector();
	
	
	
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fingerlayout);
        
        mContext = this;
                //
        init(); // <---------------- Te lo agrego por si hay que recoger el nombre y n�mero del contacto que te paso por el intent
                //
        
        //El medidor desplazable del delay
        SeekBar delayBar = (SeekBar) findViewById(R.id.fingerDelaySeek);
        
        //Cuando se mueve cambiamos el label
         delayBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				
				TextView delayLabel = (TextView) findViewById(R.id.fingerMsLabel);
				
				delayLabel.setText((progress*20)+" ms");
				
			}
		});
         
         
         
         
         
         //----------------Botones-----------------
         Button testB   = (Button) findViewById(R.id.fingerTestButton);
         Button saveB   = (Button) findViewById(R.id.fingerSaveButton);
         Button resetB  = (Button) findViewById(R.id.fingerResetButton);
                  
         
         
         //Pulsar TEST
         testB.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				
				String tmp = "";
												
				SeekBar delayBar = (SeekBar) findViewById(R.id.fingerDelaySeek);
				
				//Creamos la vib temporal y la hacemos vibrar
				long vt[] = new long[vibVec.size()+1];
				vt[0] = delayBar.getProgress()*20;						
				
				for (int i=0; i<vibVec.size(); i++)
					vt[i+1] = Long.parseLong((vibVec.elementAt(i)).toString());
				
				
				Log.e("FingerActivity", tmp);
				
				DoVibration.CustomRepeat((Vibrator) getSystemService(Context.VIBRATOR_SERVICE), vt);
											
				return false;				
			}
		});
         
         
         
         
         //Pulsar RESET
         resetB.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				
				vibVec.clear();
				tStamp = 0;
				
				return false;
			}
		});
              
        
         
         /*
         //Pulsar SAVE
         saveB.setOnTouchListener(new OnTouchListener() {
 			
 			@Override
 			public boolean onTouch(View v, MotionEvent event) {
 				
 				
						
				//Al pulsar el Guardar del dialogo que sale al pulsar guardar
				Button saveDialogB  = (Button) findViewById(R.id.chooserSaveButton);
				
				
				
				saveDialogB.setOnTouchListener(new OnTouchListener() {
					
					@Override
					public boolean onTouch(View v, MotionEvent event) {
						
						
						TextView textName = (TextView) findViewById(R.id.chooserText);
						
						
						//Creamos la vib
		 				SeekBar delayBar = (SeekBar) findViewById(R.id.fingerDelaySeek);
						long vt[] = new long[vibVec.size()+1];
						vt[0] = delayBar.getProgress()*20;						
						
						for (int i=0; i<vibVec.size(); i++)
							vt[i+1] = Long.parseLong((vibVec.elementAt(i)).toString());
						
						
						
		 				//La guardamos en la lsta de presets
		 				PresetList pl;
						try {
							pl = new PresetsConfig().loadPresets();
							pl.add(new Preset(textName.getText().toString(), new Vib(vt)));
							new PresetsConfig().dumpPresetList(pl);
						} catch (Exception e) {
							e.printStackTrace();
						} 
						
						FingerActivity.this.finish();
						return false;
					}
				});
				
				Dialog dialog = new Dialog(FingerActivity.this);
				dialog.setContentView(R.layout.choose_name);
				dialog.show();

 				 				 		
 				return false;
 			}
 		});
        */
                 
        //La caja roja que hay
        tapB   = (Button) findViewById(R.id.fingerTapButton);
        pressed = false;
       
        //Cuando se pulsa sobre ella cambiamos de color la caja blanca, al tuntun para que el usuario sepa que ha pulsado
        tapB.setOnTouchListener(new OnTouchListener() {
           
           @Override
           public boolean onTouch(View v, MotionEvent e) {
              
        	   long milis = System.currentTimeMillis();		
        	   
      	   
        	   int ev = e.getAction();
        	   
        	   switch(ev) {
        	   
        	   
        	   		case MotionEvent.ACTION_DOWN:        	   			
        	   			if (!pressed){
        	   				tapB.setBackgroundResource(R.color.colorTapButtonON);
        	   				
        	   				//Cuando lo ha empezado a pulsar
        	   				timePressed = System.currentTimeMillis();
        	   				
        	   				//Si no es la primera pulsacion ponemos cuanto silencio ha habido
        	   				if (tStamp == 0) tStamp = milis;
        	   				else vibVec.add(milis-tStamp);        	   				
        	   				
        	   			}
        	   			pressed=true;
        	   			break;
        	   			
        	   		case MotionEvent.ACTION_UP:	
        	   			if (pressed){
        	   				tapB.setBackgroundResource(R.color.colorTapButtonOFF);
        	   				
        	   				//Cuando lo ha dejado de pulsar
        	   				long endPress = System.currentTimeMillis();
        	   				tStamp = System.currentTimeMillis();
        	   				
        	   				vibVec.add(endPress-timePressed);
        	   			}
        	   			pressed = false;
        	   			break;
        	   }
        	   
        	   
              return false;
           }
        });
        

        
        
    }
    
    
    
    
    private void init(){
    	
    	//Si existen extras pasados pro intent, los obtiene
    	Bundle bundle = getIntent().getExtras();
		if(bundle!=null){
			name = bundle.getString(AgregarVibracion.KEY_CONTACTO); //Nombre del contacto al que agregar la vibracion
			number = bundle.getString(AgregarVibracion.KEY_NUMERO); //Numero del contacto al que agregar la vibracion
		}
    	
    	
    }




	public Context getmContext() {
		return mContext;
	}
}