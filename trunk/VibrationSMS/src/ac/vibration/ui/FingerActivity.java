package ac.vibration.ui;

import java.util.Random;

import ac.vibration.R;
import ac.vibration.util.mToast.mToast;
import android.app.Activity;
import android.content.pm.LabeledIntent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;


/**
 * Actividad para crear una vibración con los dedos manualmente :)
 * */
public class FingerActivity extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        
        setContentView(R.layout.fingerlayout);
       
        
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
        FrameLayout redBox = (FrameLayout) findViewById(R.id.fingerRedSquare);
       
        //Cuando se pulsa sobre ella cambiamos de color la caja blanca, al tuntun para que el usuario sepa que ha pulsado
        redBox.setOnTouchListener(new OnTouchListener() {
           
           @Override
           public boolean onTouch(View v, MotionEvent event) {
              
        	   //La caja blanca
        	   FrameLayout whiteBox = (FrameLayout) findViewById(R.id.fingerWhiteFrame);
        	           	           	  
        	   Random r = new Random();
        	   int rNumber = r.nextInt()%1000000;
        	   
        	   whiteBox.setBackgroundColor(0x7f000000+rNumber);        	   
        	  
        	   
              return false;
           }
        });
		
        
        
        
    }
}