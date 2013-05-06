package be.ac.ucl.lfsab1509.llncampus.onbootservice;

import be.ac.ucl.lfsab1509.llncampus.LLNCampus;
import be.ac.ucl.lfsab1509.llncampus.services.AlarmService;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Class intended to lauch the service of notification
 * when the device is turned on
 *
 */
public class OnBootReceiver extends BroadcastReceiver {
	 @Override
	    public void onReceive(Context context, Intent intent) {
		 SharedPreferences preferences = PreferenceManager
					.getDefaultSharedPreferences(LLNCampus.getContext());
			if (!preferences.getBoolean("courses_notify", false))
			{
				context.startService(new Intent().setComponent(new ComponentName(
	                context.getPackageName(), AlarmService.class.getName())));
			}
	    }
}
