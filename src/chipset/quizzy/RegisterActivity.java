package chipset.quizzy;

import static chipset.quizzy.resources.Constants.KEY_NAME;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import chipset.quizzy.menu.SettingsActivity;
import chipset.quizzy.resources.Functions;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class RegisterActivity extends Activity {

	EditText registerName, registerUsername, registerEmail, registerPassword;
	Button registerDo, resetDo, loginIntent;
	String name, username, email, password;
	Functions functions = new Functions();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);

		getActionBar().setIcon(R.drawable.ic_launcher_activity);
		getActionBar().setHomeButtonEnabled(true);
		getActionBar().setDisplayHomeAsUpEnabled(true);

		registerDo = (Button) findViewById(R.id.registerDo);
		resetDo = (Button) findViewById(R.id.resetDo);
		loginIntent = (Button) findViewById(R.id.loginIntent);
		registerName = (EditText) findViewById(R.id.registerName);
		registerUsername = (EditText) findViewById(R.id.registerUsername);
		registerEmail = (EditText) findViewById(R.id.registerEmail);
		registerPassword = (EditText) findViewById(R.id.registerPassword);

		Typeface tf = null;
		registerPassword.setTypeface(tf);

		registerDo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				functions.hideKeyboard(getApplicationContext(),
						getCurrentFocus());
				if (functions.isConnected(getApplicationContext())) {

					name = registerName.getText().toString();
					username = registerUsername.getText().toString();
					email = registerEmail.getText().toString();
					password = registerPassword.getText().toString();

					if (name.isEmpty() || username.isEmpty() || email.isEmpty()
							|| password.isEmpty()) {

						Toast.makeText(getApplicationContext(),
								"Enter all the details", Toast.LENGTH_SHORT)
								.show();
					} else {
						functions.initParse(getApplicationContext());
						Toast.makeText(getApplicationContext(), "Coming Soon",
								Toast.LENGTH_SHORT).show();
						final ProgressDialog pDialog = new ProgressDialog(
								getApplicationContext());
						pDialog.setTitle("Please Wait");
						pDialog.setCancelable(true);
						pDialog.setMessage("Registering...");
						pDialog.setIndeterminate(false);
						pDialog.show();
						ParseUser user = new ParseUser();
						user.put(KEY_NAME, name);
						user.setUsername(username);
						user.setPassword(password);
						user.setEmail(email);
						user.signUpInBackground(new SignUpCallback() {
							public void done(ParseException e) {
								if (e == null) {
									pDialog.dismiss();
									Intent toLogin = new Intent(
											getApplication(),
											LoginActivity.class);
									Toast.makeText(getApplicationContext(),
											"Registered Successfully",
											Toast.LENGTH_SHORT).show();
									// Close all views before launching
									startActivity(toLogin);
									toLogin.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
											| Intent.FLAG_ACTIVITY_CLEAR_TASK);
								} else {
									e.printStackTrace();
									pDialog.dismiss();
									Toast.makeText(getApplicationContext(),
											"Oops, Something Went Wrong",
											Toast.LENGTH_SHORT).show();
								}
							}
						});
					}
				} else {
					Toast.makeText(getApplicationContext(),
							"No Internet Connection", Toast.LENGTH_SHORT)
							.show();
				}

			}
		});

		resetDo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				registerName.setText(null);
				registerEmail.setText(null);
				registerUsername.setText(null);
				registerPassword.setText(null);

			}
		});

		loginIntent.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(getApplication(), LoginActivity.class));
				finish();

			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.register, menu);
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
		} else if (id == android.R.id.home) {
			onBackPressed();
		}
		return super.onOptionsItemSelected(item);
	}
}
