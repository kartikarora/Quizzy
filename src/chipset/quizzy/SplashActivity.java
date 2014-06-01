package chipset.quizzy;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);

		new Handler().postDelayed(new Runnable() {

			// Using handler with postDelayed called runnable run method

			@Override
			public void run() {
				Intent gotoLogin = new Intent(getApplication(),
						LoginActivity.class);
				startActivity(gotoLogin);
				finish();
			}
		}, 2000); // wait for 2 seconds
	}

}
