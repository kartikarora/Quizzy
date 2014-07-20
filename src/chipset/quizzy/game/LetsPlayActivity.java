package chipset.quizzy.game;

import static chipset.quizzy.resources.Constants.KEY_ADMIN;
import static chipset.quizzy.resources.Constants.KEY_LAST_LEVEL;
import static chipset.quizzy.resources.Constants.KEY_NAME;
import static chipset.quizzy.resources.Constants.PREFS_FIRST_RUN;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import chipset.quizzy.R;
import chipset.quizzy.resources.Functions;
import chipset.quizzy.showcase.ShowcaseActivity;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.RefreshCallback;

public class LetsPlayActivity extends Activity {

	Functions functions = new Functions();
	String name;
	boolean admin;
	int lastLevelCleared;
	TextView lastLevel, welcomeTitle;
	LinearLayout letsPlayBox;
	Button letsPlayDo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lets_play);

		getActionBar().setIcon(R.drawable.ic_launcher_activity);

		ParseUser.getCurrentUser().refreshInBackground(new RefreshCallback() {

			@Override
			public void done(ParseObject object, ParseException e) {
				if (e == null) {
					if (object.getBoolean(PREFS_FIRST_RUN) == true) {
						startActivity(new Intent(getApplication(),
								ShowcaseActivity.class)
								.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
										| Intent.FLAG_ACTIVITY_NEW_TASK));
					}
					lastLevelCleared = object.getInt(KEY_LAST_LEVEL);
					welcomeTitle = (TextView) findViewById(R.id.welcomeTitle);
					lastLevel = (TextView) findViewById(R.id.lastLevel);
					letsPlayBox = (LinearLayout) findViewById(R.id.letsPlayBox);
					letsPlayDo = (Button) findViewById(R.id.letsPlayDo);

					name = object.getString(KEY_NAME);
					admin = object.getBoolean(KEY_ADMIN);

					welcomeTitle.setText("Hi " + name + "!\nWelcome to Quizzy");
					functions.updateLeaderboard(getApplicationContext());
					if (admin == true) {
						TextView adminText = (TextView) findViewById(R.id.adminText);
						adminText.setVisibility(View.VISIBLE);

					}

					new Handler().postDelayed(new Runnable() {

						@Override
						public void run() {
							Animation animSlideUpDisappear = AnimationUtils
									.loadAnimation(getApplicationContext(),
											R.anim.animation_slide_up_disappear);
							welcomeTitle.startAnimation(animSlideUpDisappear);
							Animation animSlideUpAppear = AnimationUtils
									.loadAnimation(getApplicationContext(),
											R.anim.animation_slide_up_appear);
							letsPlayBox.setVisibility(View.VISIBLE);
							letsPlayBox.startAnimation(animSlideUpAppear);

							if (!functions.isConnected(getApplicationContext())) {

								setContentView(R.layout.no_connection);

							} else {

								if (lastLevelCleared < 1) {
									lastLevelCleared = 0;

									lastLevel
											.setText("First Question Is Ready");

								} else {
									lastLevel.setText("Last Level Cleared : "
											+ String.valueOf(lastLevelCleared));
								}
								letsPlayDo.setVisibility(View.VISIBLE);

							}
						}
					}, 2000); // wait for 2 seconds

					letsPlayDo.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View arg0) {
							Intent toQuestion = new Intent(getApplication(),
									QuestionActivity.class);
							toQuestion.putExtra(KEY_LAST_LEVEL,
									lastLevelCleared);
							toQuestion.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
									| Intent.FLAG_ACTIVITY_NEW_TASK);
							startActivity(toQuestion);

						}
					});
				} else {
					functions.logout(getApplication(), getApplicationContext());
				}

			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.lets_play, menu);
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

		} else if (id == R.id.action_leaderboard) {
			startActivity(new Intent(getApplication(),
					LeaderboardActivity.class));

		} else if (id == R.id.action_my_details) {
			functions.getMyDetails(LetsPlayActivity.this);

		} else if (id == R.id.action_rules) {

			AlertDialog.Builder builder = new AlertDialog.Builder(
					LetsPlayActivity.this);
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
