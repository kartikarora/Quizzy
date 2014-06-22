package chipset.quizzy.showcase;

import static chipset.quizzy.resources.Constants.PREFS_FIRST_RUN;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import chipset.quizzy.R;
import chipset.quizzy.game.LetsPlayActivity;
import chipset.quizzy.resources.Functions;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class ShowcaseActivity extends Activity {

	Functions functions = new Functions();
	Button c;
	int flag;
	TextView appshow1, question;
	EditText answer;
	ListView sampleList;
	SwipeRefreshLayout sampleRefresh;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_showcase);

		getActionBar().setIcon(R.drawable.ic_launcher_activity);

		appshow1 = (TextView) findViewById(R.id.appshow1);
		c = (Button) findViewById(R.id.c);

		c.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				appshow1.setVisibility(View.GONE);
				c.setVisibility(View.GONE);
				setContentView(R.layout.activity_question);
				c = (Button) findViewById(R.id.playSubmitDo);
				question = (TextView) findViewById(R.id.playQuestion);

				question.setText("Question will be shown here. If there's any image related to the question, it will be show just below the question and can be enlarged by clicking on it!\nEnter 'Sample Answer' in the answer field and click the button");
				c.setText(getResources().getString(R.string.submit));

				c.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						c = (Button) findViewById(R.id.playSubmitDo);
						question = (TextView) findViewById(R.id.playQuestion);

						answer = (EditText) findViewById(R.id.playAnswer);
						c.setText(getResources().getString(R.string.IA));
						c.setBackground(getResources().getDrawable(
								R.color.alizarin));
						c.setTextColor(getResources().getColor(R.color.clouds));
						c.setEnabled(false);
						question.setText("If the answer is wrong, the button will change to as shown. All the answers will be alpha-numeric and in small case, without any white spaces.\nNow Enter 'sampleanswer' in the answer field and click the button once it becomes white again");
						new Handler().postDelayed(new Runnable() {

							@Override
							public void run() {
								c.setText(getResources().getString(
										R.string.submit));
								c.setBackground(getResources().getDrawable(
										R.drawable.button_click));
								c.setTextColor(getResources().getColor(
										R.color.turquoize));

								c.setEnabled(true);
							}
						}, 5000);

						c.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								c = (Button) findViewById(R.id.playSubmitDo);
								question = (TextView) findViewById(R.id.playQuestion);

								answer = (EditText) findViewById(R.id.playAnswer);
								c.setText(getResources().getString(R.string.CA));
								c.setBackground(getResources().getDrawable(
										R.color.turquoize));
								c.setTextColor(getResources().getColor(
										R.color.clouds));
								c.setEnabled(false);
								question.setText("If the answer is correct, the button will change to as shown and you'll be taken to a new intermediate stage where the level upgrade takes place\nClick the button once it becomes white again");
								new Handler().postDelayed(new Runnable() {

									@Override
									public void run() {
										c.setText(getResources().getString(
												R.string.c));
										c.setBackground(getResources()
												.getDrawable(
														R.drawable.button_click));
										c.setTextColor(getResources().getColor(
												R.color.turquoize));

										c.setEnabled(true);
									}
								}, 5000);
								c.setOnClickListener(new OnClickListener() {

									@Override
									public void onClick(View v) {
										setContentView(R.layout.activity_leaderboard);
										String[] list = getResources()
												.getStringArray(
														R.array.sampleNames);
										sampleList = (ListView) findViewById(R.id.leaderList);
										sampleRefresh = (SwipeRefreshLayout) findViewById(R.id.leaderboardRefresh);
										sampleRefresh.setColorScheme(
												R.color.alizarin,
												R.color.peter_river,
												R.color.sun_flower,
												R.color.carrot);
										sampleList
												.setAdapter(new ArrayAdapter<String>(
														getApplicationContext(),
														R.layout.leaderboard_list_view,
														list));

										sampleRefresh
												.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

													@Override
													public void onRefresh() {
														sampleRefresh
																.setRefreshing(true);
														new Handler()
																.postDelayed(
																		new Runnable() {

																			@Override
																			public void run() {
																				sampleRefresh
																						.setRefreshing(false);

																			}
																		}, 3000);

													}

												});
										sampleList
												.setOnItemClickListener(new OnItemClickListener() {

													@Override
													public void onItemClick(
															AdapterView<?> parent,
															View view,
															int position,
															long id) {
														setContentView(R.layout.activity_showcase);
														appshow1 = (TextView) findViewById(R.id.appshow1);
														c = (Button) findViewById(R.id.c);
														appshow1.setText("The settings button on your device will give access to the leaderboards, hint to a question, you details, rules and the option to logout\nIf you do not have a settings button, you'll see it on the top right corner of the app screen!");
														c.setText(R.string.c);
														c.setOnClickListener(new OnClickListener() {

															@Override
															public void onClick(
																	View v) {
																setContentView(R.layout.activity_showcase);
																appshow1 = (TextView) findViewById(R.id.appshow1);
																c = (Button) findViewById(R.id.c);
																appshow1.setText("You can now start playing Quizzy! Please do read the rules once! All the best!");
																c.setText("CONTINUE TO DASHBOARD");
																c.setOnClickListener(new OnClickListener() {

																	@Override
																	public void onClick(
																			View v) {

																		ParseUser currentUser = ParseUser
																				.getCurrentUser();
																		currentUser
																				.put(PREFS_FIRST_RUN,
																						false);
																		currentUser
																				.saveInBackground(new SaveCallback() {

																					@Override
																					public void done(
																							ParseException e) {
																						if (e == null) {
																							startActivity(new Intent(
																									getApplication(),
																									LetsPlayActivity.class)
																									.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
																											| Intent.FLAG_ACTIVITY_NEW_TASK));
																						} else {
																							Toast.makeText(
																									getApplicationContext(),
																									e.getMessage(),
																									Toast.LENGTH_SHORT)
																									.show();
																						}

																					}

																				});

																	}
																});

															}
														});
													}
												});

									}
								});
							}
						});

					}

				});

			}

		});
	}
}
