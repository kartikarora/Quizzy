package chipset.quizzy.resources;

import static chipset.quizzy.resources.Constants.APPLICATION_ID;
import static chipset.quizzy.resources.Constants.CLIENT_KEY;
import android.content.Context;

import chipset.quizzy.R;

import com.parse.Parse;

public class Functions {

	public void initParse(Context context) {
		Parse.initialize(context, APPLICATION_ID, CLIENT_KEY);
	}

}
