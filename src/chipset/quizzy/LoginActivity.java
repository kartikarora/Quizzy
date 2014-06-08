package chipset.quizzy;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import chipset.quizzy.resources.Functions;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class LoginActivity extends Activity {

	EditText loginUsername, loginPassword;
	Button loginDo, registerIntent, forgotPasswordIntent;
	String username, password;
	Functions functions = new Functions();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		loginDo = (Button) findViewById(R.id.loginDo);
		registerIntent = (Button) findViewById(R.id.registerIntent);
		loginUsername = (EditText) findViewById(R.id.loginUsername);
		loginPassword = (EditText) findViewById(R.id.loginPassword);
		forgotPasswordIntent = (Button) findViewById(R.id.forgotPasswordIntent);
		Typeface tf = null;
		loginPassword.setTypeface(tf);

		getActionBar().setIcon(R.drawable.ic_launcher_activity);
		getActionBar().setHomeButtonEnabled(true);
		getActionBar().setDisplayHomeAsUpEnabled(true);

		forgotPasswordIntent.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				startActivity(new Intent(getApplication(),
						ForgotPasswordActivity.class));
			}
		});

		loginDo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				functions.hideKeyboard(getApplicationContext(),
						getCurrentFocus());
				if (functions.isConnected(getApplicationContext())) {
					loginUsername.setError(null);
					loginPassword.setError(null);
					username = loginUsername.getText().toString().trim();
					password = loginPassword.getText().toString().trim();

					if (username.isEmpty()) {
						loginUsername.setError("Username cannot be left empty");
					}
					if (password.isEmpty()) {

						loginPassword.setError("Password cannot be left empty");
					} else {
						final ProgressDialog pDialog = new ProgressDialog(
								LoginActivity.this);
						pDialog.setTitle("Please Wait");
						pDialog.setCancelable(false);
						pDialog.setMessage("Logggin In...");
						pDialog.setIndeterminate(false);
						pDialog.show();

						ParseUser.logInInBackground(username, password,
								new LogInCallback() {
									public void done(ParseUser user,
											ParseException e) {
										if (user != null) {
											pDialog.dismiss();
											ParseUser currentUser = ParseUser
													.getCurrentUser();
											boolean emailVerified = (Boolean) currentUser
													.get("emailVerified");
											if (emailVerified == true) {
												Intent toDash = new Intent(
														getApplication(),
														DashActivity.class);
												Toast.makeText(
														getApplicationContext(),
														"Logged In Successfully",
														Toast.LENGTH_SHORT)
														.show();
												// Close all views before
												// launching
												toDash.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
														| Intent.FLAG_ACTIVITY_CLEAR_TASK);
												startActivity(toDash);

											} else {
												AlertDialog.Builder builder = new AlertDialog.Builder(
														LoginActivity.this);
												builder.setTitle("Email Verification");
												builder.setMessage("Please complete email verfication before logging in");
												builder.setNeutralButton("OK",
														null);
												builder.create();
												builder.show();
											}

										} else {
											e.printStackTrace();
											pDialog.dismiss();
											Toast.makeText(
													getApplicationContext(),
													e.getMessage(),
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

		registerIntent.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(getApplication(),
						RegisterActivity.class));
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
