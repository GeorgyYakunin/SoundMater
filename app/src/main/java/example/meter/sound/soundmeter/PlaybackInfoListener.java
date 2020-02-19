package example.meter.sound.soundmeter;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public abstract class PlaybackInfoListener {

    public static String convertStateToString(int i) {
        return i != -1 ? i != 0 ? i != 1 ? i != 2 ? i != 3 ? "N/A" : "COMPLETED" : "RESET" : "PAUSED" : "PLAYING" : "INVALID";
    }

    public void onDurationChanged(int i) {
    }

    public void onLogUpdated(String str) {
    }

    public void onPlaybackCompleted() {
    }

    public void onPositionChanged(int i) {
    }

    public void onStateChanged(int i) {
    }

    @Retention(RetentionPolicy.SOURCE)
    @interface State {
        public static final int COMPLETED = 3;
        public static final int INVALID = -1;
        public static final int PAUSED = 1;
        public static final int PLAYING = 0;
        public static final int RESET = 2;
    }
}
