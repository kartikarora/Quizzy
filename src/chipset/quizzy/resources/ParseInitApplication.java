package chipset.quizzy.resources;

import static chipset.quizzy.resources.Constants.APPLICATION_ID;
import static chipset.quizzy.resources.Constants.CLIENT_KEY;
import android.app.Application;
import chipset.quizzy.HomeActivity;
import chipset.quizzy.R;

import com.parse.Parse;
import com.parse.PushService;

public class ParseInitApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		Parse.initialize(this, APPLICATION_ID, CLIENT_KEY);
		PushService.setDefaultPushCallback(this, HomeActivity.class,
				R.drawable.ic_notification);

	}

}