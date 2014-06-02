package chipset.quizzy;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.parse.RequestPasswordResetCallback;

public class ForgotPasswordActivity extends Activity {

	Button forgotPasswordDo;
	EditText forgotPasswordEmail;
	String email;
	Functions functions = new Functions();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_forgot_password);

		getActionBar().setIcon(R.drawable.ic_launcher_activity);
		getActionBar().setHomeButtonEnabled(true);
		getActionBar().setDisplayHomeAsUpEnabled(true);

		forgotPasswordDo = (Button) findViewById(R.id.forgotPasswordDo);
		forgotPasswordEmail = (EditText) findViewById(R.id.forgotPasswordEmail);

		forgotPasswordDo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				functions.hideKeyboard(getApplicationContext(),
						getCurrentFocus());
				if (functions.isConnected(getApplicationContext())) {

					email = forgotPasswordEmail.getText().toString();

					if (email.isEmpty()) {

						Toast.makeText(getApplicationContext(),
								"Enter a valid email", Toast.LENGTH_SHORT)
								.show();
					} else {
						functions.initParse(getApplicationContext());
						Toast.makeText(getApplicationContext(), "Coming Soon",
								Toast.LENGTH_SHORT).show();
						final ProgressDialog pDialog = new ProgressDialog(
								getApplicationContext());
						pDialog.setTitle("Please Wait");
						pDialog.setCancelable(true);
						pDialog.setMessage("Requesting...");
						pDialog.setIndeterminate(false);
						pDialog.show();
						ParseUser.requestPasswordResetInBackground(email,
								new RequestPasswordResetCallback() {
									public void done(ParseException e) {
										if (e == null) {
											pDialog.dismiss();
											AlertDialog.Builder builder = new AlertDialog.Builder(
													getApplication());
											builder.setTitle("Sample Alert");
											builder.setMessage("Sample One Action Button Alert");
											builder.setNeutralButton(
													"OK",
													new DialogInterface.OnClickListener() {
														public void onClick(
																DialogInterface dialog,

																int which) {
															Intent toLogin = new Intent(
																	getApplication(),
																	LoginActivity.class);
															startActivity(toLogin);
															toLogin.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
																	| Intent.FLAG_ACTIVITY_CLEAR_TASK);
														}
													});
											builder.show();
										} else {
											e.printStackTrace();
											pDialog.dismiss();
											Toast.makeText(
													getApplicationContext(),
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

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.forgot_password, menu);
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
