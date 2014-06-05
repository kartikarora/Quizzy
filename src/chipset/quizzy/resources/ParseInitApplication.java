package chipset.quizzy.resources;

import static chipset.quizzy.resources.Constants.APPLICATION_ID;
import static chipset.quizzy.resources.Constants.CLIENT_KEY;
import android.app.Application;

import com.parse.Parse;

public class ParseInitApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();

		Parse.initialize(this, APPLICATION_ID, CLIENT_KEY);

	}

}
