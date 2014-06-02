package chipset.quizzy.menu;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.util.Log;
import chipset.quizzy.R;
import chipset.quizzy.resources.Functions;

public class SettingsActivity extends PreferenceActivity {

	Functions functions = new Functions();

	@Override
	protected void onPause() {
		super.onPause();
		Log.i("onPasue", "called");
		functions.closeParse(getApplicationContext());
		Log.i("onPasue", "closed");
	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.i("onResume", "called");
		functions.initParse(getApplicationContext());
		Log.i("onResume", "closed");
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Display the fragment as the main content.
		getFragmentManager().beginTransaction()
				.replace(android.R.id.content, new SettingsFragment()).commit();
	}

	public static class SettingsFragment extends PreferenceFragment {
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);

			// Load the preferences from an XML resource
			addPreferencesFromResource(R.xml.settings);
		}

	}
}