package ac.vibration.ui;

import ac.vibration.Inicio;
import ac.vibration.R;
import ac.vibration.exceptions.NoPreferenceException;
import ac.vibration.util.config.AppConfig;
import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;




public class Settings extends Activity{
	
	
	private boolean smsChecked    = false;
	private boolean callChecked   = false;
	private boolean systemChecked = false;
	
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lista_settings);
        
        
        final AudioManager am = (AudioManager)getSystemService(Context.AUDIO_SERVICE);

        //Configuraciones de vibracion originales
        //final int vibNotificationOriginal = am.getVibrateSetting(AudioManager.VIBRATE_TYPE_NOTIFICATION);
        //final int vibRingerOriginal = am.getVibrateSetting(AudioManager.VIBRATE_TYPE_RINGER);
        
        
        final AppConfig ac =  new AppConfig(this, AppConfig.CONFIG_NAME_DEF);
        
        CheckBox smsCheckBox  = (CheckBox) findViewById(R.id.settingsSmsCheckBox);
        CheckBox callCheckBox = (CheckBox) findViewById(R.id.settingsCallCheckBox);
        CheckBox systemCheckBox = (CheckBox) findViewById(R.id.settingsSystemCheckBox);
        
        
        try {
			smsChecked  = ac.getBool(AppConfig.VIBRATE_ON_SMS);
			callChecked = ac.getBool(AppConfig.VIBRATE_ON_CALL);
			systemChecked = ac.getBool(AppConfig.VIBRATE_SYSTEM);
		} catch (NoPreferenceException e) {
			e.printStackTrace();
		}
  
		
		smsCheckBox.setChecked(smsChecked);
		callCheckBox.setChecked(callChecked);
		systemCheckBox.setChecked(systemChecked);
        
        
        
        //Tocan sms
        smsCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				
				ac.put(isChecked, AppConfig.VIBRATE_ON_SMS);
			}
		});
        
        
        
        //Tocan call
        callCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				
				ac.put(isChecked, AppConfig.VIBRATE_ON_CALL);
			}
		});
        
        
        //Tocan system
        systemCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				
				ac.put(isChecked, AppConfig.VIBRATE_SYSTEM);
				
				//Han activado las vibraciones
				if (!isChecked) {
					//am.setVibrateSetting(AudioManager.VIBRATE_TYPE_NOTIFICATION, vibNotificationOriginal);
					//am.setVibrateSetting(AudioManager.VIBRATE_TYPE_RINGER, vibRingerOriginal);
					am.setVibrateSetting(AudioManager.VIBRATE_TYPE_NOTIFICATION, Inicio.getVibNotificationOriginal());
					am.setVibrateSetting(AudioManager.VIBRATE_TYPE_RINGER, Inicio.getVibRingerOriginal());
					
				}
				
				//Han DESactivado las vibraciones
				else {
					//am.setVibrateSetting(AudioManager.VIBRATE_TYPE_NOTIFICATION, AudioManager.VIBRATE_SETTING_OFF);
					am.setVibrateSetting(AudioManager.VIBRATE_TYPE_RINGER, AudioManager.VIBRATE_SETTING_OFF);										
				}
				
				
			}
		});
        
        
        
        
        
        
    }
	
	
}
