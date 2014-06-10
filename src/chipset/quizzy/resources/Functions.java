package chipset.quizzy.resources;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import chipset.quizzy.HomeActivity;

import com.parse.ParseUser;

public class Functions {

	/*
	 * Function to get connection status
	 */
	public boolean isConnected(Context context) {
		boolean isConnected;
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
		isConnected = (activeNetwork != null)
				&& (activeNetwork.isConnectedOrConnecting());
		return isConnected;
	}

	/*
	 * Function to hide keyboard
	 */
	public void hideKeyboard(Context context, View view) {
		InputMethodManager inputMethodManager = (InputMethodManager) context
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
	}

	/*
	 * Function to show notification
	 */
	public void showNotification(String title, String subtitle, int icon,
			Intent resultIntent, Context context, NotificationManager mNotifyMgr) {

		Uri soundUri = RingtoneManager
				.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
		PendingIntent pendingResultIntent = PendingIntent.getActivity(context,
				0, resultIntent, PendingIntent.FLAG_CANCEL_CURRENT);
		Notification mBuilder = new NotificationCompat.Builder(context)
				.setContentTitle(title).setContentText(subtitle)
				.setSmallIcon(icon).setContentIntent(pendingResultIntent)
				.setSound(soundUri).setAutoCancel(true).build();
		mNotifyMgr = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		mNotifyMgr.cancelAll();
		mNotifyMgr.notify(0, mBuilder);
	}

	/*
	 * Fuction to show notification without sound
	 */
	public void showNotificationNoSound(String title, String subtitle,
			int icon, Intent resultIntent, Context context,
			NotificationManager mNotifyMgr) {

		PendingIntent pendingResultIntent = PendingIntent.getActivity(context,
				0, resultIntent, PendingIntent.FLAG_CANCEL_CURRENT);
		Notification mBuilder = new NotificationCompat.Builder(context)
				.setContentTitle(title).setContentText(subtitle)
				.setSmallIcon(icon).setContentIntent(pendingResultIntent)
				.setAutoCancel(true).build();
		mNotifyMgr = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		mNotifyMgr.cancelAll();
		mNotifyMgr.notify(0, mBuilder);
	}

	/*
	 * Function to logout user
	 */
	public void logout(Application application, Context context) {

		Intent toHome = new Intent(application, HomeActivity.class);
		toHome.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
				| Intent.FLAG_ACTIVITY_CLEAR_TASK);
		ParseUser.logOut();
		context.startActivity(toHome);

	}

}
