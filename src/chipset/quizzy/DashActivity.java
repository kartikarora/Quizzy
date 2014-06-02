package chipset.quizzy;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import chipset.quizzy.menu.SettingsActivity;
import chipset.quizzy.resources.Functions;

public class DashActivity extends Activity {

	Functions functions = new Functions();

	@Override
	protected void onPause() {
		super.onPause();
		Log.i("onPasue", "called");
		functions.closeParse(getApplicationContext());
		Log.i("onPasue", "closed");
	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.i("onResume", "called");
		functions.initParse(getApplicationContext());
		Log.i("onResume", "closed");
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dash);

		TextView welcomeTitle = (TextView) findViewById(R.id.welcomeTitle);

		welcomeTitle.setText("Hi\nWelcome to Quizzy");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.dash, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			startActivity(new Intent(getApplication(), SettingsActivity.class));
			return true;
		} else if (id == R.id.action_logout) {

			functions.logout(getApplication(), getApplicationContext());

		}
		return super.onOptionsItemSelected(item);
	}

}
