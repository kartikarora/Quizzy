package chipset.quizzy.game;

import static chipset.quizzy.resources.Constants.KEY_LAST_LEVEL;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import chipset.quizzy.R;
import chipset.quizzy.resources.Functions;

public class LevelTransitionActivity extends Activity {

	TextView levelFrom, levelTo, levelFromSmall, levelToSmall;
	Button switchLevel, shareProgress, leaderboardDo;
	RelativeLayout theBox;
	int levelNumber;
	Functions functions = new Functions();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_level_transition);
		functions.updateLeaderboard(getApplicationContext());
		levelNumber = getIntent().getExtras().getInt(KEY_LAST_LEVEL);
		levelFrom = (TextView) findViewById(R.id.levelSwitchNumberFrom);
		levelTo = (TextView) findViewById(R.id.levelSwitchNumberTo);
		switchLevel = (Button) findViewById(R.id.levelSwitchDo);
		shareProgress = (Button) findViewById(R.id.shareProgressDo);
		leaderboardDo = (Button) findViewById(R.id.leaderboardDo);
		levelFromSmall = (TextView) findViewById(R.id.levelSwitchNumberFromSmall);
		levelToSmall = (TextView) findViewById(R.id.levelSwitchNumberToSmall);
		theBox = (RelativeLayout) findViewById(R.id.theBox);

		levelFrom.setText(String.valueOf(levelNumber));
		levelTo.setText(String.valueOf(levelNumber + 1));
		levelFromSmall.setText(String.valueOf(levelNumber));
		levelToSmall.setText(String.valueOf(levelNumber + 1));

		levelTo.setRotationY(-90f);

		AccelerateInterpolator accelerator = new AccelerateInterpolator();
		DecelerateInterpolator decelerator = new DecelerateInterpolator();

		ObjectAnimator visToInvis = ObjectAnimator.ofFloat(levelFrom,
				"rotationY", 0f, 90f);
		visToInvis.setDuration(1000);
		visToInvis.setInterpolator(accelerator);
		final ObjectAnimator invisToVis = ObjectAnimator.ofFloat(levelTo,
				"rotationY", -90f, 0f);
		invisToVis.setDuration(1000);
		invisToVis.setInterpolator(decelerator);
		invisToVis.addListener(new AnimatorListenerAdapter() {
			@Override
			public void onAnimationEnd(Animator anim) {
				Animation animFadeIn = AnimationUtils.loadAnimation(
						getApplicationContext(), R.anim.animation_fade_in);
				Animation animFadeOut = AnimationUtils.loadAnimation(
						getApplicationContext(), R.anim.animation_fade_out);
				levelTo.startAnimation(animFadeOut);
				theBox.setVisibility(View.VISIBLE);
				theBox.startAnimation(animFadeIn);

			}
		});
		visToInvis.addListener(new AnimatorListenerAdapter() {
			@Override
			public void onAnimationEnd(Animator anim) {
				levelFrom.setVisibility(View.GONE);
				invisToVis.start();
				levelTo.setVisibility(View.VISIBLE);
			}
		});
		visToInvis.start();

		switchLevel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent toNextQuestion = new Intent(getApplication(),
						QuestionActivity.class);
				toNextQuestion.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
						| Intent.FLAG_ACTIVITY_NEW_TASK);
				toNextQuestion.putExtra(KEY_LAST_LEVEL, levelNumber);
				startActivity(toNextQuestion);

			}
		});

		shareProgress.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent share = new Intent(Intent.ACTION_SEND);
				share.setType("text/plain");
				share.putExtra(
						Intent.EXTRA_TEXT,
						"I just completed Level "
								+ String.valueOf(levelNumber)
								+ " and reached Level "
								+ String.valueOf(levelNumber + 1)
								+ " on Quizzy!\n\n"
								+ "Learn new things on Quizzy!\nhttps://play.google.com/store/apps/details?id=chipset.quizzy");
				startActivity(share);

			}
		});

		leaderboardDo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				startActivity(new Intent(getApplication(),
						LeaderboardActivity.class));

			}
		});

	}
}
