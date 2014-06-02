package chipset.quizzy;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class HomeActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		final LinearLayout layoutHome = (LinearLayout) findViewById(R.id.layoutHome);
		layoutHome.setVisibility(View.GONE);

		Button loginIntent = (Button) findViewById(R.id.loginIntentHome);
		Button registerIntent = (Button) findViewById(R.id.registerIntentHome);

		loginIntent.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(getApplication(), LoginActivity.class));

			}
		});

		registerIntent.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(getApplication(),
						RegisterActivity.class));

			}
		});

		new Handler().postDelayed(new Runnable() {

			// Using handler with postDelayed called runnable run method

			@Override
			public void run() {
				TextView homeTitle = (TextView) findViewById(R.id.homeTitle);
				Animation animMove = AnimationUtils.loadAnimation(
						getApplicationContext(), R.anim.animation_move);
				homeTitle.startAnimation(animMove);

				Animation animFadeIn = AnimationUtils.loadAnimation(
						getApplicationContext(), R.anim.animation_fade_in);

				layoutHome.setVisibility(View.VISIBLE);
				layoutHome.startAnimation(animFadeIn);
			}
		}, 2000); // wait for 2 seconds
	}
}
