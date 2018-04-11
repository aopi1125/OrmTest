package com.harbor.db;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.media.AudioManager;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;


/**
 * Created by chenwenfeng on 2017/6/1.
 * 生成后视频的播放
 */

public class MediaPlayerActivity extends Activity implements SurfaceHolder.Callback,
        View.OnClickListener, MediaPlayer.OnPreparedListener,
        MediaPlayer.OnBufferingUpdateListener, MediaPlayer.OnCompletionListener {

    public static void startSelf(Context context, String mp4){
        Intent intent = new Intent(context, MediaPlayerActivity.class);
        intent.putExtra(EXTRA_PLAY_PATH, mp4);
        context.startActivity(intent);
    }

    private static final String EXTRA_PLAY_PATH = "extra_mp4_path";
    private MediaPlayer mPlayer;
    private SurfaceView mSurface;
    private SurfaceHolder mSurfaceHolder;
    private View mViewBack, mViewDown, mViewShare;
    private SeekBar mProgressBar;
    private View mStartView;
    private String mVideoPath;
    private int mPlayLength;
    private TextView mTvPlayed;
    private TextView mTvLength;
    private ImageView mIvThumb;
    private View mContentView;

    private Looper mLooper;
    private Handler mHandler;

    private Bitmap mThumbBitmap;
    private boolean bFirsHide = true;
    private String mSavedPath = null;
    private boolean bIsNew = false;
    private Runnable mDelayRunnable = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_video);
        initParams();
        initViews();
    }

    private void initParams() {
        mVideoPath = getIntent().getStringExtra(EXTRA_PLAY_PATH);
    }

    private void initViews() {
        mViewBack = findViewById(R.id.iv_back);
        mViewDown = findViewById(R.id.iv_down);
        mViewShare = findViewById(R.id.iv_share);
        mContentView = findViewById(R.id.rl_content);
        mProgressBar = (SeekBar) findViewById(R.id.skb_video);
        mProgressBar.setProgress(0);
        mSurface = (SurfaceView) findViewById(R.id.video_surface);

        int width =  getResources().getDisplayMetrics().widthPixels;
        ViewGroup.LayoutParams lp = mSurface.getLayoutParams();
        lp.height = width;
        lp.width = width;
        mSurface.setLayoutParams(lp);

        mStartView = findViewById(R.id.iv_start_play);
        mStartView.setVisibility(View.GONE);
        mSurfaceHolder = mSurface.getHolder();
        mSurfaceHolder.setFormat(PixelFormat.TRANSPARENT);
        mSurfaceHolder.setKeepScreenOn(true);
        mSurfaceHolder.addCallback(this);
        mViewBack.setOnClickListener(this);
        mViewDown.setOnClickListener(this);
        mViewShare.setOnClickListener(this);
        mStartView.setOnClickListener(this);
        mTvLength = (TextView) findViewById(R.id.tv_play_all);
        mTvPlayed = (TextView) findViewById(R.id.tv_played);
        mTvPlayed.setText("00:00");

        mIvThumb = (ImageView) findViewById(R.id.iv_thumb);
        ViewGroup.LayoutParams lpTh = mIvThumb.getLayoutParams();
        lpTh.height = width;
        lpTh.width = width;
        mIvThumb.setLayoutParams(lpTh);

        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            retriever.setDataSource(mVideoPath);

            mThumbBitmap = retriever.getFrameAtTime(0);
        } catch (IllegalArgumentException ex) {
            // Assume this is a corrupt video file
        } catch (RuntimeException ex) {
            // Assume this is a corrupt video file.
        } finally {
            try {
                retriever.release();
            } catch (RuntimeException ex) {
                // Ignore failures while cleaning up.
            }
        }

        if(mThumbBitmap == null || mThumbBitmap.isRecycled()){
            mThumbBitmap = ThumbnailUtils.createVideoThumbnail(mVideoPath, MediaStore.Video.Thumbnails.MINI_KIND);
        }

        if(mThumbBitmap != null && !mThumbBitmap.isRecycled()){
            mIvThumb.setImageBitmap(mThumbBitmap);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
    }

    @Override
    public void surfaceCreated(SurfaceHolder arg0) {
        prepare();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder arg0) {
        stop();
        release();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stop();
        mIvThumb.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onDestroy() {
        if(mDelayRunnable != null){
            mViewBack.removeCallbacks(mDelayRunnable);
        }
        super.onDestroy();
        if(mHandler != null){
            mHandler.sendEmptyMessage(ServiceHandler.FINISH);
        }
        stop();
        release();
        if(mThumbBitmap != null && !mThumbBitmap.isRecycled()){
            mThumbBitmap.recycle();
        }
    }

    /**
     * 初始化相关
     */
    private void prepare() {
        //必须在surface创建后才能初始化MediaPlayer,否则不会显示图像
        mPlayer = new MediaPlayer();
        mPlayer.reset();
        mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        //设置显示视频显示在SurfaceView上
        mPlayer.setDisplay(mSurfaceHolder);
        try {
//            mPlayer.setVideoScalingMode(MediaPlayer.VIDEO_SCALING_MODE_SCALE_TO_FIT);
            mPlayer.setDataSource(mVideoPath);
            mPlayer.setOnPreparedListener(this);
            mPlayer.setOnCompletionListener(this);
            mPlayer.setOnBufferingUpdateListener(this);
            mPlayer.setOnInfoListener(new MediaPlayer.OnInfoListener() {
                @Override
                public boolean onInfo(MediaPlayer mp, int what, int extra) {
                    if(what == mp.MEDIA_INFO_VIDEO_RENDERING_START){
                        //隐藏缩略图
                        mIvThumb.setVisibility(View.GONE);
                        bFirsHide = false;
                    }
                    return false;
                }
            });
            mPlayer.prepareAsync();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 开始播放
     */
    private void start() {
        mStartView.setVisibility(View.GONE);
        if(!bFirsHide){
            mIvThumb.setVisibility(View.GONE);
        }
        try {
            if (mPlayer == null || isPlaying()) {
                return;
            }
            mPlayer.start();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
        if(mHandler != null){
            mHandler.sendEmptyMessage(ServiceHandler.COUNTING);
        }
    }

    /**
     * 暂停播放
     */
    private void pause() {
        try {
            if (mPlayer == null) {
                return;
            }
            if(isPlaying())
                mPlayer.pause();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    /**
     * 停止播放
     */
    private void stop() {
        if(mHandler != null){
            mHandler.removeMessages(ServiceHandler.COUNTING);
        }
        mTvPlayed.removeCallbacks(mUpdateRunnable);
        try {
            if (mPlayer == null) {
                return;
            }
            if(isPlaying()){
                mPlayer.pause();
            }
            mPlayer.seekTo(0);
            mProgressBar.setProgress(0);
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
//        mIvThumb.setVisibility(View.VISIBLE);
        mStartView.setVisibility(View.VISIBLE);
        mProgressBar.setProgress(0);
        mTvPlayed.setText("00:00");
    }

    private boolean isPlaying() {
        boolean playing = false;
        try {
            if (mPlayer == null) {
                return playing;
            }
            if (mPlayer.isPlaying()) {
                playing = true;
            }
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
        return playing;
    }

    /**
     * 释放资源
     */
    private void release() {
        if (mPlayer == null) {
            return;
        }
        mPlayer.release();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                //返回
                break;
            case R.id.iv_down:
                //保存到相册
                break;
            case R.id.iv_share:
                //分享
                break;
            case R.id.iv_start_play:
                //开始播放
                start();
                break;
        }
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mStartView.setVisibility(View.VISIBLE);
        mIvThumb.setVisibility(View.VISIBLE);
        if (mPlayer != null) {
            if(mPlayLength <= 0){
                mPlayLength = mPlayer.getDuration();

            }
            mTvLength.setText(secToTime(Math.round(mPlayLength * 1.0f / 1000)));
            mProgressBar.setMax(mPlayLength);
            mProgressBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                int progress;
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    this.progress = progress * mPlayer.getDuration()/ seekBar.getMax();
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    mPlayer.seekTo(progress);
                }
            });
        }
        if (mLooper == null || mHandler == null) {
            HandlerThread thread = new HandlerThread("play video");
            thread.start();
            mLooper = thread.getLooper();
            mHandler = new ServiceHandler(mLooper);
        }
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        mProgressBar.setSecondaryProgress(percent);
//        int currentProgress = mProgressBar.getMax() * mPlayer.getCurrentPosition() / mPlayer.getDuration();
//        Log.e("currentProgress", "currentProgress----->" + currentProgress);
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        if(mProgressBar != null){
            mProgressBar.setProgress(mPlayLength);
        }
        stop();
    }

    private Runnable mUpdateRunnable = new Runnable() {
        @Override
        public void run() {
            try {
                int playPosition = mPlayer.getCurrentPosition();
                mTvPlayed.setText(secToTime(Math.round(playPosition * 1.0f / 1000)));
                if (mPlayLength != 0) {
                    mProgressBar.setProgress(playPosition);
                }
                if (playPosition >= mPlayLength) {
                    mProgressBar.setProgress(mPlayLength);
                    mHandler.removeMessages(ServiceHandler.COUNTING);
                } else {
                    mHandler.sendEmptyMessageDelayed(ServiceHandler.COUNTING, 100);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    // a integer to xx:xx
    public static String secToTime(int time) {
        String timeStr = null;
        int hour = 0;
        int minute = 0;
        int second = 0;
        if (time <= 0)
            return "00:00";
        else {
            minute = time / 60;
            if (minute < 60) {
                second = time % 60;
                timeStr = unitFormat(minute) + ":" + unitFormat(second);
            } else {
                hour = minute / 60;
                if (hour > 99)
                    return "99:59:59";
                minute = minute % 60;
                second = time - hour * 3600 - minute * 60;
                timeStr = unitFormat(minute) + ":" + unitFormat(second);
            }
        }
        return timeStr;
    }

    private static String unitFormat(int i) {
        String retStr = null;
        if (i >= 0 && i < 10)
            retStr = "0" + Integer.toString(i);
        else
            retStr = "" + i;
        return retStr;
    }


    //开始播放、停止、计时
    private final class ServiceHandler extends Handler {
        private static final int COUNTING = 1;
        private static final int FINISH = 2;
        public ServiceHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case COUNTING:
                    mTvPlayed.post(mUpdateRunnable);
                    break;
                case FINISH:
                    removeMessages(ServiceHandler.COUNTING);
                    mLooper.quit();
                    break;
            }
        }
    }
}
