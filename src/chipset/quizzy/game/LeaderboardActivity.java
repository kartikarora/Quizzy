package chipset.quizzy.game;

import static chipset.quizzy.resources.Constants.KEY_ADMIN;
import static chipset.quizzy.resources.Constants.KEY_CROSSED_AT;
import static chipset.quizzy.resources.Constants.KEY_DEVICE;
import static chipset.quizzy.resources.Constants.KEY_LAST_LEVEL;
import static chipset.quizzy.resources.Constants.KEY_LEADER_CLASS;
import static chipset.quizzy.resources.Constants.KEY_NAME;
import static chipset.quizzy.resources.Constants.KEY_RANK;
import static chipset.quizzy.resources.Constants.KEY_USERNAME;

import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
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
import com.parse.ParseObject;
import com.parse.ParseQuery;

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
		leaderList = (ListView) findViewById(R.id.leaderList);
		leaderboardRefresh = (SwipeRefreshLayout) findViewById(R.id.leaderboardRefresh);
		leaderboardRefresh.setColorScheme(R.color.alizarin,
				R.color.peter_river, R.color.sun_flower, R.color.carrot);
		leaderboardRefresh
				.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

					@Override
					public void onRefresh() {
						getUserList();

					}

				});
		getUserList();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.leaderboard, menu);

		// SearchManager searchManager = (SearchManager)
		// getSystemService(Context.SEARCH_SERVICE);
		// SearchView searchView = (SearchView)
		// menu.findItem(R.id.action_search)
		// .getActionView();
		// searchView.setSearchableInfo(searchManager
		// .getSearchableInfo(getComponentName()));
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
		} else if (id == R.id.action_my_details) {
			functions.getMyDetails(LeaderboardActivity.this);

		}
		return super.onOptionsItemSelected(item);
	}

	public void getUserList() {
		leaderboardRefresh.setRefreshing(true);
		functions.updateLeaderboard(getApplicationContext());
		ParseQuery<ParseObject> query = ParseQuery.getQuery(KEY_LEADER_CLASS);
		query.addAscendingOrder(KEY_RANK);
		query.whereNotEqualTo(KEY_ADMIN, true);
		query.findInBackground(new FindCallback<ParseObject>() {

			@Override
			public void done(final List<ParseObject> users, ParseException e) {
				leaderboardRefresh.setRefreshing(false);
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
									ParseObject clickedUser = users
											.get(position);
									int lastLevel, rank;
									String name, message, username, device, ll, at;
									Date crossedAt;
									username = clickedUser
											.getString(KEY_USERNAME);
									name = clickedUser.getString(KEY_NAME);
									crossedAt = clickedUser
											.getDate(KEY_CROSSED_AT);
									rank = clickedUser.getInt(KEY_RANK);
									device = clickedUser.getString(KEY_DEVICE);
									lastLevel = clickedUser
											.getInt(KEY_LAST_LEVEL);
									ll = String.valueOf(lastLevel);
									at = "At: ";
									if (lastLevel < 1) {
										ll = "Not Played Yet";
										at = "Joined On: ";
									}
									message = "Name: " + name
											+ "\nLast Level Completed: " + ll
											+ "\n" + at
											+ String.valueOf(crossedAt)
											+ "\nOn: " + device + "\nRank: "
											+ String.valueOf(rank);
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
