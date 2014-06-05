package chipset.quizzy;

import static chipset.quizzy.resources.Constants.KEY_ADMIN;
import static chipset.quizzy.resources.Constants.KEY_NAME;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import chipset.quizzy.resources.Functions;

import com.parse.ParseUser;

public class DashActivity extends Activity {

	Functions functions = new Functions();
	String name;
	int admin;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dash);

		getActionBar().setIcon(R.drawable.ic_launcher_activity);
		ParseUser currentUser = ParseUser.getCurrentUser();
		if (currentUser != null) {
			name = currentUser.getString(KEY_NAME);
			admin = currentUser.getInt(KEY_ADMIN);
		} else {
			// show the signup or login screen
		}

		TextView welcomeTitle = (TextView) findViewById(R.id.welcomeTitle);

		welcomeTitle.setText("Hi " + name + "!\nWelcome to Quizzy");

		if (admin == 1) {
			TextView adminText = (TextView) findViewById(R.id.adminText);
			adminText.setVisibility(View.VISIBLE);
		}

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
		if (id == R.id.action_logout) {

			functions.logout(getApplication(), getApplicationContext());

		}
		return super.onOptionsItemSelected(item);
	}

}
