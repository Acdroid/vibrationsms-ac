package ac.vibration.ui;


import ac.vibration.R;
import android.app.Activity;
import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.TabHost;



public class AddVib extends TabActivity {
	
	
	public Context mContext;
	
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {

    	mContext = this;
    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addvib);

        Resources res = getResources(); // Resource object to get Drawables
        TabHost tabHost = getTabHost();  // The activity TabHost
        TabHost.TabSpec spec;  // Resusable TabSpec for each tab
        Intent intent;  // Reusable Intent for each tab

        // Create an Intent to launch an Activity for the tab (to be reused)
        intent = new Intent().setClass(this, FingerActivity.class);

        // Initialize a TabSpec for each tab and add it to the TabHost
        spec = tabHost.newTabSpec("finger").setIndicator(this.getString(R.string.tap_mode), res.getDrawable(android.R.drawable.ic_dialog_dialer)).setContent(intent);
        tabHost.addTab(spec);

        // Do the same for the other tabs
        intent = new Intent().setClass(this, MorseActivity.class);
        spec = tabHost.newTabSpec("morse").setIndicator(this.getString(R.string.morse_mode), res.getDrawable(android.R.drawable.sym_action_chat)).setContent(intent);
        tabHost.addTab(spec);



        tabHost.setCurrentTab(0);

    	
    	
    }
    
    
    public Context getContext() {
    	
    	return mContext;
    }
    
    
}


