package text.xujiajian.asus.com.videoviewdemo;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;

import text.xujiajian.asus.com.videoviewdemo.view.MediaPlayerView;

public class MainActivity extends AppCompatActivity {

    private MediaPlayerView media;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        media = (MediaPlayerView) findViewById(R.id.media);
        Uri uri = Uri.parse("http://video.jiecao.fm/8/16/%E9%B8%AD%E5%AD%90.mp4");
         media.setPlayUri(uri);
    }
}
