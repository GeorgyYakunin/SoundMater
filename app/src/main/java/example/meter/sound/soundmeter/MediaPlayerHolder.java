package example.meter.sound.soundmeter;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;

import java.io.File;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public final class MediaPlayerHolder implements PlayerAdapter {
    private static final int PLAYBACK_POSITION_REFRESH_INTERVAL_MS = 200;
    private static final String TAG = "MediaPlayerHolder";
    private final Context mContext;
    public PlaybackInfoListener mPlaybackInfoListener;
    private ScheduledExecutorService mExecutor;
    private String mFileName;
    private Handler mHandlerUpdateColourArcProgressBar = new Handler();
    private MediaPlayer mMediaPlayer;
    private int mResourceId;

    public MediaPlayerHolder(Context context) {
        this.mContext = context.getApplicationContext();
    }

    private void initializeMediaPlayer() {
        if (this.mMediaPlayer == null) {
            this.mMediaPlayer = new MediaPlayer();
            this.mMediaPlayer.setAudioStreamType(3);
            this.mMediaPlayer.setOnCompletionListener(new OnCompletionListener() {
                public void onCompletion(MediaPlayer mediaPlayer) {
                    MediaPlayerHolder.this.stopUpdatingCallbackWithPosition(true);
                    Log.d(MediaPlayerHolder.TAG, "onCompletion: MediaPlayer playback completed");
                    if (MediaPlayerHolder.this.mPlaybackInfoListener != null) {
                        MediaPlayerHolder.this.mPlaybackInfoListener.onStateChanged(3);
                        MediaPlayerHolder.this.mPlaybackInfoListener.onPlaybackCompleted();
                    }
                }
            });
            Log.d(TAG, "initializeMediaPlayer: mMediaPlayer = new MediaPlayer()");
        }
    }

    public void setPlaybackInfoListener(PlaybackInfoListener playbackInfoListener) {
        this.mPlaybackInfoListener = playbackInfoListener;
    }

    public void loadMedia(int i) {
        String str = TAG;
        this.mResourceId = i;
        initializeMediaPlayer();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("android.resource://");
        stringBuilder.append(this.mContext.getPackageName());
        stringBuilder.append("/");
        stringBuilder.append(this.mResourceId);
        String stringBuilder2 = stringBuilder.toString();
        try {
            Log.d(str, "loadMedia: load() {1. setDataSource}");
            StringBuilder stringBuilder3 = new StringBuilder();
            stringBuilder3.append("loadMedia: Trying to load file at ");
            stringBuilder3.append(stringBuilder2);
            Log.d(str, stringBuilder3.toString());
            this.mMediaPlayer.setDataSource(this.mContext, Uri.parse(stringBuilder2));
        } catch (Exception e) {
            Log.e(str, "loadMedia: cannot load media");
            e.printStackTrace();
        }
        try {
            Log.d(str, "loadMedia: load() {2. prepare}");
            this.mMediaPlayer.prepare();
        } catch (Exception e2) {
            Log.e(str, "loadMedia: prepare failed");
            e2.printStackTrace();
        }
        initializeProgressCallback();
        Log.d(str, "loadMedia: initializeProgressCallback()");
    }

    public void release() {
        if (this.mMediaPlayer != null) {
            Log.d(TAG, "release: release() and mMediaPlayer = null");
            this.mMediaPlayer.release();
            this.mMediaPlayer = null;
        }
    }

    public boolean isPlaying() {
        MediaPlayer mediaPlayer = this.mMediaPlayer;
        return mediaPlayer != null ? mediaPlayer.isPlaying() : false;
    }

    public void play() {
        MediaPlayer mediaPlayer = this.mMediaPlayer;
        if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("play: playbackStart() ");
            stringBuilder.append(this.mContext.getFilesDir().getPath());
            stringBuilder.append(File.separator);
            stringBuilder.append(this.mFileName);
            stringBuilder.append(".amr");
            Log.d(TAG, stringBuilder.toString());
            this.mMediaPlayer.start();
            PlaybackInfoListener playbackInfoListener = this.mPlaybackInfoListener;
            if (playbackInfoListener != null) {
                playbackInfoListener.onStateChanged(0);
            }
            updateColourArcProgressBar();
        }
    }

    public void reset() {
        if (this.mMediaPlayer != null) {
            Log.d(TAG, "reset: playbackReset()");
            this.mMediaPlayer.reset();
            loadMedia(this.mFileName, false);
            PlaybackInfoListener playbackInfoListener = this.mPlaybackInfoListener;
            if (playbackInfoListener != null) {
                playbackInfoListener.onStateChanged(2);
            }
            stopUpdatingCallbackWithPosition(true);
        }
    }

    public void pause() {
        MediaPlayer mediaPlayer = this.mMediaPlayer;
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            this.mMediaPlayer.pause();
            PlaybackInfoListener playbackInfoListener = this.mPlaybackInfoListener;
            if (playbackInfoListener != null) {
                playbackInfoListener.onStateChanged(1);
            }
            Log.d(TAG, "pause: playbackPause()");
        }
    }

    public void seekTo(int i) {
        if (this.mMediaPlayer != null) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("seekTo: seekTo() %d ms");
            stringBuilder.append(i);
            Log.d(TAG, stringBuilder.toString());
            this.mMediaPlayer.seekTo(i);
        }
    }

    public void loadMedia(String str, final boolean z) {
        String str2 = TAG;
        this.mFileName = str;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(this.mContext.getApplicationContext().getFilesDir().getPath());
        stringBuilder.append(File.separator);
        stringBuilder.append(str);
        stringBuilder.append(".amr");
        Uri fromFile = Uri.fromFile(new File(stringBuilder.toString()));
        initializeMediaPlayer();
        try {
            Log.d(str2, "loadMedia: {1. setDataSource}");
            stringBuilder = new StringBuilder();
            stringBuilder.append("loadMedia: Trying to load file at ");
            stringBuilder.append(fromFile.toString());
            Log.d(str2, stringBuilder.toString());
            this.mMediaPlayer.setDataSource(fromFile.toString());
        } catch (Exception e) {
            Log.e(str2, "loadMedia: load error");
            e.printStackTrace();
        }
        try {
            Log.d(str2, "loadMedia: {2. prepare}");
            this.mMediaPlayer.prepareAsync();
        } catch (Exception e2) {
            Log.e(str2, "loadMedia: prepare error");
            e2.printStackTrace();
        }
        this.mMediaPlayer.setOnPreparedListener(new OnPreparedListener() {
            public void onPrepared(MediaPlayer mediaPlayer) {
                if (z) {
                    MediaPlayerHolder.this.initializeProgressCallback();
                    Log.d(MediaPlayerHolder.TAG, "onPrepared: initializeProgressCallback()");
                    mediaPlayer.start();
                    if (MediaPlayerHolder.this.mPlaybackInfoListener != null) {
                        MediaPlayerHolder.this.mPlaybackInfoListener.onStateChanged(0);
                    }
                    MediaPlayerHolder.this.updateColourArcProgressBar();
                }
            }
        });
    }

    public void updateColourArcProgressBar() {
        this.mHandlerUpdateColourArcProgressBar.postDelayed(new Runnable() {
            public void run() {
                MediaPlayerHolder.this.updateProgressCallbackTask();
                MediaPlayerHolder.this.updateColourArcProgressBar();
            }
        }, 200);
    }

    public void stopUpdatingCallbackWithPosition(boolean z) {
        ScheduledExecutorService scheduledExecutorService = this.mExecutor;
        if (scheduledExecutorService != null) {
            scheduledExecutorService.shutdownNow();
            this.mExecutor = null;
            this.mHandlerUpdateColourArcProgressBar = null;
            if (z) {
                PlaybackInfoListener playbackInfoListener = this.mPlaybackInfoListener;
                if (playbackInfoListener != null) {
                    playbackInfoListener.onPositionChanged(0);
                }
            }
        }
    }

    public void updateProgressCallbackTask() {
        MediaPlayer mediaPlayer = this.mMediaPlayer;
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            int currentPosition = this.mMediaPlayer.getCurrentPosition();
            PlaybackInfoListener playbackInfoListener = this.mPlaybackInfoListener;
            if (playbackInfoListener != null) {
                playbackInfoListener.onPositionChanged(currentPosition);
            }
        }
    }

    public void initializeProgressCallback() {
        int duration = this.mMediaPlayer.getDuration();
        PlaybackInfoListener playbackInfoListener = this.mPlaybackInfoListener;
        if (playbackInfoListener != null) {
            playbackInfoListener.onDurationChanged(duration);
            this.mPlaybackInfoListener.onPositionChanged(0);
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("firing setPlaybackDuration(%d sec)");
            stringBuilder.append(TimeUnit.MILLISECONDS.toSeconds((long) duration));
            String stringBuilder2 = stringBuilder.toString();
            String str = TAG;
            Log.d(str, stringBuilder2);
            Log.d(str, "initializeProgressCallback: firing setPlaybackPosition(0)");
        }
    }
}
