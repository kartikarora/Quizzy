package chipset.quizzy.game;

import static chipset.quizzy.resources.Constants.*;
import static chipset.quizzy.resources.Constants.KEY_DEVICE;
import static chipset.quizzy.resources.Constants.KEY_LAST_LEVEL;
import static chipset.quizzy.resources.Constants.KEY_NAME;
import static chipset.quizzy.resources.Constants.KEY_UPDATED_AT;

import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import chipset.quizzy.R;
import chipset.quizzy.resources.Functions;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class LeaderboardActivity extends Activity {

	ListView leaderList;
	SwipeRefreshLayout leaderboardRefresh;

	Functions functions = new Functions();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_leaderboard);

		getActionBar().setIcon(R.drawable.ic_launcher_activity);
		getActionBar().setHomeButtonEnabled(true);
		getActionBar().setDisplayHomeAsUpEnabled(true);

		getUserList();

		leaderList = (ListView) findViewById(R.id.leaderList);
		leaderboardRefresh = (SwipeRefreshLayout) findViewById(R.id.leaderboardRefresh);

		leaderboardRefresh.setColorScheme(R.color.alizarin,
				R.color.peter_river, R.color.sun_flower, R.color.carrot);
		leaderboardRefresh
				.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

					@Override
					public void onRefresh() {
						leaderboardRefresh.setRefreshing(true);
						getUserList();
						new Handler().postDelayed(new Runnable() {

							@Override
							public void run() {
								leaderboardRefresh.setRefreshing(false);

							}
						}, 3000);

					}

				});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.leaderboard, menu);
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
		} else if (id == android.R.id.home) {
			onBackPressed();
		}
		return super.onOptionsItemSelected(item);
	}

	public void getUserList() {

		ParseQuery<ParseUser> query = ParseUser.getQuery();
		query.addDescendingOrder(KEY_LAST_LEVEL);
		query.addAscendingOrder(KEY_UPDATED_AT);
		query.whereEqualTo(KEY_EMAIL_VERFIFIED, true);
		query.whereNotEqualTo(KEY_ADMIN, 1);
		query.findInBackground(new FindCallback<ParseUser>() {

			@Override
			public void done(final List<ParseUser> users, ParseException e) {
				if (e == null) {
					String[] listName = new String[users.size()];
					for (int i = 0; i < users.size(); i++) {
						listName[i] = users.get(i).getString(KEY_NAME);
					}
					ArrayAdapter<String> adapter = new ArrayAdapter<String>(
							getApplicationContext(),
							R.layout.leaderboard_list_view, listName);
					leaderList.setAdapter(adapter);

					leaderList
							.setOnItemClickListener(new OnItemClickListener() {

								@Override
								public void onItemClick(AdapterView<?> parent,
										View view, int position, long id) {
									ParseUser clickedUser = users.get(position);
									int lastLevel;
									String name, message, username, device, ll, at;
									Date crossedAt;
									username = clickedUser.getUsername();
									name = clickedUser.getString(KEY_NAME);
									crossedAt = clickedUser.getUpdatedAt();
									device = clickedUser.getString(KEY_DEVICE);
									lastLevel = clickedUser
											.getInt(KEY_LAST_LEVEL);
									ll = String.valueOf(lastLevel);
									at = "At";
									if (lastLevel < 1) {
										ll = "Not Played Yet";
										at = "Joined On:";
									}
									message = "Name: " + name
											+ "\nLast Level Completed: " + ll
											+ "\n+" + at
											+ String.valueOf(crossedAt)
											+ "\nOn: " + device;
									AlertDialog.Builder builder = new AlertDialog.Builder(
											LeaderboardActivity.this);
									builder.setTitle("Username: " + username);
									builder.setMessage(message);
									builder.setCancelable(false);
									builder.setNeutralButton(
											android.R.string.ok, null);
									builder.create();
									builder.show();

								}
							});

				} else {
					Toast.makeText(getApplicationContext(), e.getMessage(),
							Toast.LENGTH_SHORT).show();
				}

			}
		});
	}
}
