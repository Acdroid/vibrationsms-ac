package ac.vibration.ui;

import java.util.Random;

import ac.vibration.R;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
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
	
	private boolean flag = false;
	
	public FrameLayout redBox ;
	public Context mContext;
	
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fingerlayout);
        
        mContext = this;
                //
        init(); // <---------------- Te lo agrego por si hay que recoger el nombre y número del contacto que te paso por el intent
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
        
        
        
        
                              
        
        
        
        
        
        
        
        
        
        
        
        //La caja roja que hay
        redBox = (FrameLayout) findViewById(R.id.fingerRedSquare);
       
        //Cuando se pulsa sobre ella cambiamos de color la caja blanca, al tuntun para que el usuario sepa que ha pulsado
        redBox.setOnTouchListener(new OnTouchListener() {
           
           @Override
           public boolean onTouch(View v, MotionEvent event) {
              
        	   //La caja blanca
        	   FrameLayout whiteBox = (FrameLayout) findViewById(R.id.fingerWhiteFrame);
        	           	           	  
        	   Random r = new Random();
        	   int rNumber = r.nextInt()%1000000;
        	   
        	   whiteBox.setBackgroundColor(0x7f000000+rNumber);        	   
        	   if (flag){
					Log.d("DEBUG","tic1" + flag);
					redBox.setBackgroundResource(R.color.colorTapButtonOFF);
					flag=false;
				}
				else{
					redBox.setBackgroundResource(R.color.colorTapButtonON);	
					Log.d("DEBUG","tic2" + flag);
					flag=true;
				}   
        	   
        	   
        	   
              return false;
           }
        });
        
        redBox.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {

				
			}
		});
        
        redBox.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				
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