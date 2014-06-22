package chipset.quizzy;

import static chipset.quizzy.resources.Constants.KEY_ADMIN;
import static chipset.quizzy.resources.Constants.KEY_CROSSED_AT;
import static chipset.quizzy.resources.Constants.KEY_DEVICE;
import static chipset.quizzy.resources.Constants.KEY_LAST_LEVEL;
import static chipset.quizzy.resources.Constants.KEY_NAME;
import static chipset.quizzy.resources.Constants.KEY_RANK;
import static chipset.quizzy.resources.Constants.PREFS_FIRST_RUN;

import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import chipset.quizzy.resources.Functions;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class RegisterActivity extends Activity {

	EditText registerName, registerUsername, registerEmail, registerPassword;
	Button registerDo, resetDo, loginIntent;
	String name, username, email, password, model;
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
					registerName.setError(null);
					registerUsername.setError(null);
					registerEmail.setError(null);
					registerPassword.setError(null);
					name = registerName.getText().toString().trim();
					username = registerUsername.getText().toString().trim();
					email = registerEmail.getText().toString().trim();
					password = registerPassword.getText().toString().trim();

					if (name.isEmpty()) {
						registerName.setError("Name Required");
					}
					if (username.isEmpty()) {
						registerUsername.setError("Username Required");
					}
					if (email.isEmpty()) {
						registerEmail.setError("Email Required");
					}
					if (password.isEmpty()) {
						registerPassword.setError("Password Required");
					}

					else {
						final ProgressDialog pDialog = new ProgressDialog(
								RegisterActivity.this);
						pDialog.setTitle("Please Wait");
						pDialog.setCancelable(false);
						pDialog.setMessage("Registering...");
						pDialog.setIndeterminate(false);
						pDialog.show();

						model = Build.MANUFACTURER + " " + Build.MODEL;
						ParseUser user = new ParseUser();
						user.put(KEY_NAME, name);
						user.setUsername(username);
						user.setPassword(password);
						user.setEmail(email);
						user.put(KEY_ADMIN, false);
						user.put(KEY_LAST_LEVEL, -1);
						user.put(KEY_DEVICE, model);
						user.put(PREFS_FIRST_RUN, true);
						user.put(KEY_RANK, 0);
						user.put(KEY_CROSSED_AT,new Date());
						user.signUpInBackground(new SignUpCallback() {
							public void done(ParseException e) {
								if (e == null) {
									pDialog.dismiss();

									AlertDialog.Builder builder = new AlertDialog.Builder(
											RegisterActivity.this);
									builder.setTitle("Registered Successfully");
									builder.setMessage("You've been sent an account verification link on your email. Please verify your account and then login");
									builder.setCancelable(false);
									builder.setNeutralButton(
											android.R.string.ok,
											new DialogInterface.OnClickListener() {

												@Override
												public void onClick(
														DialogInterface arg0,
														int arg1) {
													// Close all views before
													// launching
													Intent toLogin = new Intent(
															getApplication(),
															LoginActivity.class);
													toLogin.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
															| Intent.FLAG_ACTIVITY_CLEAR_TASK);
													startActivity(toLogin);

												}
											});
									builder.create();
									builder.show();

								} else {
									pDialog.dismiss();
									e.printStackTrace();
									Toast.makeText(getApplicationContext(),
											e.getMessage(), Toast.LENGTH_SHORT)
											.show();
								}
							}
						});

					}
				} else {
					setContentView(R.layout.no_connection);
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
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == android.R.id.home) {
			onBackPressed();
		}
		return super.onOptionsItemSelected(item);
	}
}
