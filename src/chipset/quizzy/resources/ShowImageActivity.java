package chipset.quizzy.resources;

import static chipset.quizzy.resources.Constants.KEY_PICTURE_PATH;
import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import chipset.quizzy.R;

import com.squareup.picasso.Picasso;

public class ShowImageActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_image);

		String picturePath = getIntent().getExtras()
				.getString(KEY_PICTURE_PATH);
		ImageView imageViewer = (ImageView) findViewById(R.id.imageViewer);
		Picasso.with(getApplicationContext()).load(picturePath)
				.into(imageViewer);

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == android.R.id.home) {
			onBackPressed();
		}
		return super.onOptionsItemSelected(item);
	}

}
