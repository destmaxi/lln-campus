package be.ac.ucl.lfsab1509.llncampus.onbootservice;

import be.ac.ucl.lfsab1509.llncampus.services.AlarmService;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

/**
 * Class intended to lauch the service of notification
 * when the device is turned on
 *
 */
public class OnBootReceiver extends BroadcastReceiver {
	 @Override
	    public void onReceive(Context context, Intent intent) {

		 context.startService(new Intent().setComponent(new ComponentName(
				 context.getPackageName(), AlarmService.class.getName())));

	    }
}
