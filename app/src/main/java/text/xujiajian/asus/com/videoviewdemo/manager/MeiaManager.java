package text.xujiajian.asus.com.videoviewdemo.manager;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.SurfaceHolder;

import java.io.IOException;

import text.xujiajian.asus.com.videoviewdemo.bean.MediaTimeInfo;
import text.xujiajian.asus.com.videoviewdemo.interfac.OnMediaPlayerListener;
import text.xujiajian.asus.com.videoviewdemo.utils.TimeUtils;

/**
 * Created by asus on 2017/2/5.
 */

public class MeiaManager implements MediaPlayer.OnBufferingUpdateListener, MediaPlayer.OnErrorListener, MediaPlayer.OnCompletionListener{
    private final Context context;
    private final MediaPlayer mediaplayer;
    private OnMediaPlayerListener onMediaPlayerListener;
    private int lastPosition = -1;
    private boolean isFirst = true;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                MediaTimeInfo mediaTimeInfo = (MediaTimeInfo) msg.obj;
                //回调给界面进行展示
                if (onMediaPlayerListener != null) {
                    onMediaPlayerListener.onPlayInfo(mediaTimeInfo);
                }
                int currentPosition = mediaplayer.getCurrentPosition();
                if (currentPosition == lastPosition) {
                    //卡顿
                    onMediaPlayerListener.halt(true);
                } else {
                    //不卡顿
                    onMediaPlayerListener.halt(false);
                }
                lastPosition = currentPosition;

                if (mediaplayer.isPlaying())
                    //获取到了信息
                    getPlayInfo();
            }
        }
    };

    public MeiaManager(Context context) {
        this.context = context;
        mediaplayer = new MediaPlayer();
        mediaplayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaplayer.setOnBufferingUpdateListener(this);
        mediaplayer.setOnErrorListener(this);
        mediaplayer.setOnCompletionListener(this);
    }

    //缓冲监听
    @Override
    public void onBufferingUpdate(MediaPlayer mediaPlayer, int i) {
        if (onMediaPlayerListener != null) {
            onMediaPlayerListener.onBufferingUpdate(i);
        }
    }

    //失败监听
    @Override
    public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {

        if (onMediaPlayerListener != null) {
            onMediaPlayerListener.onError();
        }
        return false;
    }

    public void setCurrentPosition(int i) {
        mediaplayer.seekTo(i);
    }



    //播放结束的监听
    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        if (onMediaPlayerListener != null) {
            onMediaPlayerListener.onCompletion();
        }

    }

    /**
     * 设置播放的位置
     *
     * @param surfaceHolder
     */
    public void setDisplayHoloder(SurfaceHolder surfaceHolder) {
        mediaplayer.setDisplay(surfaceHolder);
    }

    /**
     * 设置播放器的监听
     * / * @param onMediaPlayerListener
     * /
     */
    public void setOnMediaPlayerListener(OnMediaPlayerListener onMediaPlayerListener) {
        this.onMediaPlayerListener = onMediaPlayerListener;
    }

    public void start(Uri uri) {
        try {
            Log.i("aaaaa", "start: ....."+uri);
            mediaplayer.setDataSource(context, uri);
            mediaplayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mediaplayer.start();
        getPlayInfo();
    }

    private void getPlayInfo() {
        //播放的进度
        int duration = mediaplayer.getDuration();
        int currentPosition = mediaplayer.getCurrentPosition();
        String totalTime = TimeUtils.formatTime(duration);
        String currentTime = TimeUtils.formatTime(currentPosition);
        MediaTimeInfo mediaTimeInfo = new MediaTimeInfo(duration, currentPosition, totalTime, currentTime);
        Message msg = Message.obtain();
        msg.obj = mediaTimeInfo;
        msg.what = 0;
        handler.sendMessageDelayed(msg, 500);
    }

    /**
     * 暂停
     */
    public void pause() {
        mediaplayer.pause();
    }

    /**
     * 释放
     */
    public void release() {
        if (mediaplayer.isPlaying()) {
            mediaplayer.stop();
        }
        mediaplayer.release();
    }



}
