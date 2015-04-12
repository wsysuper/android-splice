package sjtu.android.splice;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;

public class ServerImageActivity extends Activity {
	public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.screen);
        Intent intent = getIntent();
        byte[] imageBytes = intent.getByteArrayExtra("image");
        Bitmap bmp = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
		ImageView imageView = (ImageView)findViewById(R.id.iv01);
		imageView.setImageBitmap(bmp);
    }
}
