package chipset.quizzy.game;

import static chipset.quizzy.resources.Constants.KEY_CROSSED_AT;
import static chipset.quizzy.resources.Constants.KEY_DEVICE;
import static chipset.quizzy.resources.Constants.KEY_LAST_LEVEL;
import static chipset.quizzy.resources.Constants.KEY_NAME;
import static chipset.quizzy.resources.Constants.KEY_PICTURE_PATH;
import static chipset.quizzy.resources.Constants.KEY_QUESTION;
import static chipset.quizzy.resources.Constants.KEY_QUESTION_ANSWER;
import static chipset.quizzy.resources.Constants.KEY_QUESTION_CLASS;
import static chipset.quizzy.resources.Constants.KEY_QUESTION_HINT;
import static chipset.quizzy.resources.Constants.KEY_QUESTION_IMAGE;
import static chipset.quizzy.resources.Constants.KEY_QUESTION_NUMBER;
import static chipset.quizzy.resources.Constants.KEY_RANK;
import static chipset.quizzy.resources.Constants.KEY_USERNAME;

import java.util.Date;
import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import chipset.quizzy.R;
import chipset.quizzy.resources.Functions;
import chipset.quizzy.resources.ShowImageActivity;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.RefreshCallback;
import com.parse.SaveCallback;
import com.squareup.picasso.Picasso;

public class QuestionActivity extends Activity {

	int lastLevelCleared, currentLevel;
	String picturePath, question, questionNumber, questionAnswer, questionHint,
			questionImage, submitAnswer;
	TextView playQuestion;
	EditText playAnswer;
	Button playSubmitDo;
	ImageView playImage;
	Functions functions = new Functions();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_question);
		lastLevelCleared = getIntent().getExtras().getInt(KEY_LAST_LEVEL);
		getActionBar().setIcon(R.drawable.ic_launcher_activity);
		currentLevel = lastLevelCleared + 1;
		Log.i("testlQ", String.valueOf(lastLevelCleared));
		Log.i("testcQ", String.valueOf(currentLevel));

		getActionBar().setTitle("Level " + String.valueOf(currentLevel));

		playSubmitDo = (Button) findViewById(R.id.playSubmitDo);
		playAnswer = (EditText) findViewById(R.id.playAnswer);

		if (functions.isConnected(getApplicationContext())) {
			final ProgressDialog pDialog = new ProgressDialog(
					QuestionActivity.this);
			pDialog.setCancelable(false);
			pDialog.setIndeterminate(false);
			pDialog.setTitle(R.string.pleaseWait);
			pDialog.setMessage("Fetching...");
			pDialog.show();

			ParseQuery<ParseObject> query = ParseQuery
					.getQuery(KEY_QUESTION_CLASS);
			query.whereEqualTo(KEY_QUESTION_NUMBER, currentLevel);
			query.getFirstInBackground(new GetCallback<ParseObject>() {
				public void done(ParseObject object, ParseException e) {
					if (object == null) {
						pDialog.dismiss();
						setContentView(R.layout.all_done);
						Button shareProgress = (Button) findViewById(R.id.shareProgressDone);
						shareProgress.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View arg0) {
								Intent share = new Intent(Intent.ACTION_SEND);
								share.setType("text/plain");
								share.putExtra(
										Intent.EXTRA_TEXT,
										"I have completed all the levels on Quizzy and looking forward to new Questions!\n\nLearn new things on Quizzy, https://play.google.com/store/apps/details?id=chipset.quizzy");
								startActivity(share);

							}
						});
					} else {
						pDialog.dismiss();
						playQuestion = (TextView) findViewById(R.id.playQuestion);
						playImage = (ImageView) findViewById(R.id.playImage);

						question = object.getString(KEY_QUESTION);
						questionAnswer = object.getString(KEY_QUESTION_ANSWER);
						questionHint = object.getString(KEY_QUESTION_HINT);
						picturePath = object.getString(KEY_QUESTION_IMAGE);
						playQuestion.setText(question);
						if (picturePath != "null") {
							playImage.setVisibility(View.VISIBLE);
							Picasso.with(getApplicationContext())
									.load(picturePath).into(playImage);
							playImage.setOnClickListener(new OnClickListener() {

								@Override
								public void onClick(View arg0) {
									Intent loadBigImage = new Intent(
											getApplication(),
											ShowImageActivity.class);
									loadBigImage.putExtra(KEY_PICTURE_PATH,
											picturePath);
									startActivity(loadBigImage);

								}
							});
						}

					}
				}
			});

		} else {
			setContentView(R.layout.no_connection);
		}

		playSubmitDo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				playAnswer.setError(null);
				submitAnswer = playAnswer.getText().toString()
						.toLowerCase(Locale.UK).trim();
				if (submitAnswer.isEmpty()) {
					playAnswer.setError("Required");

				} else {
					final ProgressDialog pDialog = new ProgressDialog(
							QuestionActivity.this);
					pDialog.setCancelable(false);
					pDialog.setIndeterminate(false);
					pDialog.setTitle(R.string.pleaseWait);
					pDialog.setMessage("Checking...");
					pDialog.show();
					if (submitAnswer.equals(questionAnswer)) {

						ParseUser currentUser = ParseUser.getCurrentUser();
						currentUser.put(KEY_LAST_LEVEL, currentLevel);
						currentUser.put(KEY_CROSSED_AT,new Date());
						currentUser.saveInBackground(new SaveCallback() {

							@Override
							public void done(ParseException e) {
								if (e == null) {
									pDialog.dismiss();
									playSubmitDo.setText(getResources()
											.getString(R.string.CA));
									playSubmitDo.setBackground(getResources()
											.getDrawable(R.color.turquoize));
									playSubmitDo.setTextColor(getResources()
											.getColor(R.color.clouds));
									playSubmitDo.setClickable(false);
									functions
											.updateLeaderboard(getApplicationContext());
									new Handler().postDelayed(new Runnable() {

										@Override
										public void run() {
											Intent switchLevel = new Intent(
													getApplication(),
													LevelTransitionActivity.class);
											switchLevel
													.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
															| Intent.FLAG_ACTIVITY_NEW_TASK);
											switchLevel.putExtra(
													KEY_LAST_LEVEL,
													currentLevel);
											startActivity(switchLevel);

										}
									}, 2000);
								} else {
									Toast.makeText(getApplicationContext(),
											e.getMessage(), Toast.LENGTH_SHORT)
											.show();
								}

							}

						});
					} else {
						pDialog.dismiss();
						playSubmitDo.setText(getResources().getString(
								R.string.IA));
						playSubmitDo.setBackground(getResources().getDrawable(
								R.color.alizarin));
						playSubmitDo.setTextColor(getResources().getColor(
								R.color.clouds));
						playSubmitDo.setClickable(false);
						new Handler().postDelayed(new Runnable() {

							@Override
							public void run() {
								playSubmitDo.setText(getResources().getString(
										R.string.submit));
								playSubmitDo.setBackground(getResources()
										.getDrawable(R.drawable.button_click));
								playSubmitDo.setTextColor(getResources()
										.getColor(R.color.turquoize));

								playSubmitDo.setClickable(true);
							}
						}, 2000);

					}
				}
			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.question, menu);
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
		} else if (id == R.id.action_hint) {
			Toast.makeText(getApplicationContext(), "Hint: " + questionHint,
					Toast.LENGTH_SHORT).show();

		} else if (id == R.id.action_leaderboard) {
			startActivity(new Intent(getApplication(),
					LeaderboardActivity.class));

		} else if (id == R.id.action_my_details) {
			ParseUser.getCurrentUser().refreshInBackground(
					new RefreshCallback() {

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
								message = "Name: " + name + "\nUsername: "
										+ username + "\nLast Level Completed: "
										+ ll + "\n" + at
										+ String.valueOf(crossedAt) + "\nOn: "
										+ device + "\nRank: "
										+ String.valueOf(rank);
								AlertDialog.Builder builder = new AlertDialog.Builder(
										QuestionActivity.this);

								builder.setTitle(R.string.my_details);
								builder.setMessage(message);
								builder.setNeutralButton(android.R.string.ok,
										null);
								builder.create();
								builder.show();

							}

						}
					});

		} else if (id == R.id.action_rules) {

			AlertDialog.Builder builder = new AlertDialog.Builder(
					QuestionActivity.this);
			builder.setCancelable(false);
			builder.setTitle(R.string.rules);
			builder.setMessage(R.string.rulesMessage);
			builder.setNeutralButton(android.R.string.ok, null);
			builder.create();
			builder.show();

		}
		return super.onOptionsItemSelected(item);
	}
}
