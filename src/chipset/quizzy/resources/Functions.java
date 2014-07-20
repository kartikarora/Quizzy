package chipset.quizzy.resources;

import static chipset.quizzy.resources.Constants.KEY_ADMIN;
import static chipset.quizzy.resources.Constants.KEY_CROSSED_AT;
import static chipset.quizzy.resources.Constants.KEY_DEVICE;
import static chipset.quizzy.resources.Constants.KEY_LAST_LEVEL;
import static chipset.quizzy.resources.Constants.KEY_LEADER_CLASS;
import static chipset.quizzy.resources.Constants.KEY_NAME;
import static chipset.quizzy.resources.Constants.KEY_RANK;
import static chipset.quizzy.resources.Constants.KEY_USERNAME;

import java.util.Date;
import java.util.List;

import android.app.AlertDialog;
import android.app.Application;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;
import chipset.quizzy.HomeActivity;
import chipset.quizzy.R;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

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

	/*
	 * Function to put boolean SharedPrefrences data
	 */

	public void putSharedPrefs(Context context, String preferenceName,
			boolean val) {
		SharedPreferences pref = context
				.getSharedPreferences(preferenceName, 0); // 0 - for private
															// mode
		Editor editor = pref.edit();
		editor.clear();
		editor.putBoolean(preferenceName, val);
		editor.commit();
	}

	/*
	 * Function to put boolean SharedPrefrences data
	 */

	public boolean getSharedPrefs(Context context, String preferenceName) {
		SharedPreferences pref = context
				.getSharedPreferences(preferenceName, 0); // 0 - for private
															// mode

		boolean val = pref.getBoolean(preferenceName, true);
		return val;

	}

	/*
	 * Function to update leaderboard
	 */
	public void updateLeaderboard(final Context context) {
		Log.d("Leaderboard ", "Updating");
		ParseQuery<ParseObject> query = ParseQuery.getQuery(KEY_LEADER_CLASS);
		query.addDescendingOrder(KEY_LAST_LEVEL);
		query.addAscendingOrder(KEY_CROSSED_AT);
		query.whereNotEqualTo(KEY_ADMIN, true);
		query.findInBackground(new FindCallback<ParseObject>() {

			@Override
			public void done(List<ParseObject> users, ParseException e) {
				Log.d("Leaderboard ", "done() caled");
				if (e == null) {
					Log.d("Leaderboard ", "inside if");

					for (int i = 0; i < users.size(); i++) {
						ParseObject user = users.get(i);
						Log.d("Leaderboard ",
								"Updating user " + String.valueOf(i + 1));
						user.put(KEY_RANK, i + 1);
						user.saveInBackground(new SaveCallback() {

							@Override
							public void done(ParseException e) {
								if (e == null) {
									Log.d("Leader", "Saved");
								} else {
									Toast.makeText(context, e.getMessage(),
											Toast.LENGTH_SHORT).show();
								}

							}
						});
					}

				} else {
					Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT)
							.show();
				}
			}
		});

		Log.d("Leaderboard ", "Closing");
	}

	/*
	 * Function to get current user details
	 */
	public void getMyDetails(final Context context) {
		updateLeaderboard(context);
		String username = ParseUser.getCurrentUser().getUsername();
		ParseQuery<ParseObject> query = ParseQuery.getQuery(KEY_LEADER_CLASS);
		query.whereEqualTo(KEY_USERNAME, username);
		query.getFirstInBackground(new GetCallback<ParseObject>() {

			@Override
			public void done(ParseObject user, ParseException e) {
				if (e == null) {
					int lastLevel, rank;
					String name, message, username, device, ll, at;
					Date crossedAt;
					username = user.getString(KEY_USERNAME);
					name = user.getString(KEY_NAME);
					crossedAt = user.getDate(KEY_CROSSED_AT);
					device = user.getString(KEY_DEVICE);
					lastLevel = user.getInt(KEY_LAST_LEVEL);
					rank = user.getInt(KEY_RANK);
					ll = String.valueOf(lastLevel);
					at = "At: ";
					if (lastLevel < 1) {
						ll = "Not Played Yet";
						at = "Joined On:";
					}
					message = "Name: " + name + "\nUsername: " + username
							+ "\nLast Level Completed: " + ll + "\n" + at
							+ String.valueOf(crossedAt) + "\nOn: " + device
							+ "\nRank: " + String.valueOf(rank);
					AlertDialog.Builder builder = new AlertDialog.Builder(
							context);

					builder.setTitle(R.string.my_details);
					builder.setMessage(message);
					builder.setNeutralButton(android.R.string.ok, null);
					builder.create();
					builder.show();

				}

			}
		});

	}
}
