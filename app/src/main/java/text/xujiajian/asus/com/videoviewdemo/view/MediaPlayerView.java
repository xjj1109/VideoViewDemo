package text.xujiajian.asus.com.videoviewdemo.view;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import text.xujiajian.asus.com.videoviewdemo.R;
import text.xujiajian.asus.com.videoviewdemo.bean.MediaTimeInfo;
import text.xujiajian.asus.com.videoviewdemo.interfac.OnMediaPlayerListener;
import text.xujiajian.asus.com.videoviewdemo.manager.MeiaManager;

/**
 * Created by asus on 2017/2/5.
 */

public class MediaPlayerView extends FrameLayout implements View.OnClickListener, SeekBar.OnSeekBarChangeListener, SurfaceHolder.Callback, OnMediaPlayerListener
{
    private TextView tv_current;
    private TextView tv_total;
    private ImageView iv_start;
    private ImageView iv_fullscreen;
    private SeekBar sb_progress;
    private ProgressBar pb_loading;
    private SurfaceView surface_container;
    private boolean isPlay = true;
    private MeiaManager meidaManager;
    public static final String TAG = "MeidaView";
    private Uri uri;
    private boolean isVertical = true;
    private View view;
    private boolean isFirst = true;
    private int width;
    private int height;

    public MediaPlayerView(Context context) {
        super(context);
        init();
    }

    private void init() {
        meidaManager = new MeiaManager(getContext());
        meidaManager.setOnMediaPlayerListener(this);

        view = View.inflate(getContext(), R.layout.playerview, this);
        //找空间
        tv_current = (TextView) view.findViewById(R.id.tv_current);
        tv_total = (TextView) view.findViewById(R.id.tv_total);
        iv_start = (ImageView) view.findViewById(R.id.iv_start);
        iv_fullscreen = (ImageView) view.findViewById(R.id.iv_fullscreen);
        pb_loading = (ProgressBar) view.findViewById(R.id.pb_loading);
        sb_progress = (SeekBar) view.findViewById(R.id.sb_progress);
        surface_container = (SurfaceView) view.findViewById(R.id.surface_container);
        iv_start.setOnClickListener(this);
        iv_fullscreen.setOnClickListener(this);
        sb_progress.setOnSeekBarChangeListener(this);

        //播放在什么位置
        SurfaceHolder holder = surface_container.getHolder();
        holder.addCallback(this);

        this.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onGlobalLayout() {
                if (isFirst) {
                    width = getWidth();
                    height = getHeight();
                    isFirst = false;
                }
                MediaPlayerView.this.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });

    }

    public MediaPlayerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public MediaPlayerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);


        Log.i(TAG, "width---" + width + "height---" + height);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_start:
                //播放逻辑
                if (isPlay) {
                    //播放
                    meidaManager.start(uri);
                } else {
                    meidaManager.pause();
                }
                isPlay = !isPlay;
                break;
            case R.id.iv_fullscreen:
                Activity context = (Activity) getContext();
                //横竖屏切换--全都执行一遍生命周期
                if (isVertical) {
                    context.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                    RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
                    this.setLayoutParams(layoutParams);
                } else {
                    context.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(width, height);
                    this.setLayoutParams(layoutParams);
                }
                isVertical = !isVertical;
                break;
        }

    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        //如果是人为拖动
        if (b) {
            //改变时候，设置当前是视频到该位置
            meidaManager.setCurrentPosition(i);
        }

    }

    /**
     * 开始拖动
     *
     * @param seekBar
     */
    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    /**
     * 停止拖动
     *
     * @param seekBar
     */
    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    /**
     * 正在播放回调的参数
     *
     * @param meidaTimeInfo
     */
    @Override
    public void onPlayInfo(MediaTimeInfo meidaTimeInfo) {
        sb_progress.setMax(meidaTimeInfo.getDuration());
        sb_progress.setProgress(meidaTimeInfo.getCurrentPosition());
        tv_total.setText(meidaTimeInfo.getTotalTime());
        tv_current.setText(meidaTimeInfo.getCurrentTime());
    }

    /**
     * 缓冲的进度回调
     *
     * @param i
     */
    @Override
    public void onBufferingUpdate(int i) {
        if (sb_progress.getMax() == 100) {
            sb_progress.setSecondaryProgress(i);
        } else {
            sb_progress.setSecondaryProgress(i * sb_progress.getMax() / 100);
        }

    }

    @Override
    public void onCompletion() {
        Toast.makeText(getContext(), "播放结束", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onError() {
        Toast.makeText(getContext(), "播放失败", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void halt(boolean halt) {
        if (halt) {
            pb_loading.setVisibility(View.VISIBLE);
        } else {
            pb_loading.setVisibility(View.GONE);
        }
    }

    /**
     * surface创建
     *
     * @param surfaceHolder
     */
    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        Log.i(TAG, "surfaceCreated---------");
        //设置给MediaManger
        meidaManager.setDisplayHoloder(surfaceHolder);
    }

    /**
     * 界面大小改变
     *
     * @param surfaceHolder
     * @param i
     * @param i1
     * @param i2
     */
    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        //改变
        Log.i(TAG, "surfaceChanged");
    }

    /**
     * 界面销毁
     *
     * @param surfaceHolder
     */
    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        //界面--视频正在播放
        meidaManager.release();
    }

    public void setPlayUri(Uri uri) {
        this.uri = uri;
    }
}
