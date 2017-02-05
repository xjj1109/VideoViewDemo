package text.xujiajian.asus.com.videoviewdemo.interfac;

import text.xujiajian.asus.com.videoviewdemo.bean.MediaTimeInfo;

/**
 * Created by asus on 2017/2/5.
 */

public interface OnMediaPlayerListener {
    public void onPlayInfo(MediaTimeInfo meidaTimeInfo);

    void onBufferingUpdate(int i);

    void onCompletion();

    void onError();

    void halt(boolean halt);
}
