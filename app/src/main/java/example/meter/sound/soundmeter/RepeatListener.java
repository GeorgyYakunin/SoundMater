package example.meter.sound.soundmeter;

import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;

public class RepeatListener implements OnTouchListener {
    public final OnClickListener clickListener;
    public final int normalInterval;
    public Handler handler = new Handler();
    public View touchedView;
    public Runnable handlerRunnable = new Runnable() {
        public void run() {
            if (RepeatListener.this.touchedView.isEnabled()) {
                RepeatListener.this.handler.postDelayed(this, (long) RepeatListener.this.normalInterval);
                RepeatListener.this.clickListener.onClick(RepeatListener.this.touchedView);
                return;
            }
            RepeatListener.this.handler.removeCallbacks(RepeatListener.this.handlerRunnable);
            RepeatListener.this.touchedView.setPressed(false);
            RepeatListener.this.touchedView = null;
        }
    };
    private int initialInterval;

    public RepeatListener(int i, int i2, OnClickListener onClickListener) {
        if (onClickListener == null) {
            throw new IllegalArgumentException("null runnable");
        } else if (i < 0 || i2 < 0) {
            throw new IllegalArgumentException("negative interval");
        } else {
            this.initialInterval = i;
            this.normalInterval = i2;
            this.clickListener = onClickListener;
        }
    }

    public boolean onTouch(View view, MotionEvent motionEvent) {
        int action = motionEvent.getAction();
        if (action == 0) {
            this.handler.removeCallbacks(this.handlerRunnable);
            this.handler.postDelayed(this.handlerRunnable, (long) this.initialInterval);
            this.touchedView = view;
            this.touchedView.setPressed(true);
            this.clickListener.onClick(view);
            return true;
        } else if (action != 1 && action != 3) {
            return false;
        } else {
            this.handler.removeCallbacks(this.handlerRunnable);
            this.touchedView.setPressed(false);
            this.touchedView = null;
            return true;
        }
    }
}
