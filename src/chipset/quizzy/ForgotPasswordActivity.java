package chipset.quizzy;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
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

					forgotPasswordEmail.setError(null);
					email = forgotPasswordEmail.getText().toString();

					if (email.isEmpty()) {

						forgotPasswordEmail.setError("Email Required");
					} else {
						final ProgressDialog pDialog = new ProgressDialog(
								ForgotPasswordActivity.this);
						pDialog.setTitle("Please Wait");
						pDialog.setCancelable(false);
						pDialog.setMessage("Requesting...");
						pDialog.setIndeterminate(false);
						pDialog.show();

						ParseUser.requestPasswordResetInBackground(email,
								new RequestPasswordResetCallback() {
									public void done(ParseException e) {
										if (e == null) {
											pDialog.dismiss();
											AlertDialog.Builder builder = new AlertDialog.Builder(
													ForgotPasswordActivity.this);
											builder.setTitle("Mail Sent");
											builder.setMessage("Please login with new password to continue");
											builder.setNeutralButton(
													"OK",
													new DialogInterface.OnClickListener() {
														public void onClick(
																DialogInterface dialog,
																int which) {
															finish();
														}
													});
											builder.create();
											builder.show();
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
