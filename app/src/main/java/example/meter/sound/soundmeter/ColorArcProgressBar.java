package example.meter.sound.soundmeter;

import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import example.meter.sound.R;

public class ColorArcProgressBar extends View {
    private final int DEGREE_PROGRESS_DISTANCE = dipToPx(8.0f);
    public float currentAngle = 0.0f;
    public float currentValues = 0.0f;
    public float f54k;
    public float textSize = 100.0f;
    public Paint vTextPaint;
    private Paint allArcPaint;
    private int aniSpeed = 0;
    private int bgArcColor = getResources().getColor(R.color.progressBarColor);
    private float bgArcWidth = ((float) dipToPx(2.0f));
    private RectF bgRect;
    private float centerX;
    private float centerY;
    private int[] colors = new int[]{R.color.colorAccent, R.color.colorAccent, R.color.colorAccent, R.color.colorAccent};
    private Paint curSpeedPaint;
    private float curSpeedSize = ((float) dipToPx(13.0f));
    private Paint degreePaint;
    private int diameter = 500;
    private int hintColor = -10000537;
    private Paint hintPaint;
    private float hintSize = ((float) dipToPx(15.0f));
    private String hintString;
    private boolean isAutoTextSize = true;
    private boolean isNeedContent;
    private boolean isNeedDial;
    private boolean isNeedTitle;
    private boolean isNeedUnit;
    private boolean isShowCurrentSpeed = true;
    private float lastAngle;
    private OnSeekArcChangeListener listener;
    private float longDegree = ((float) dipToPx(13.0f));
    private int longDegreeColor = -15658735;
    private PaintFlagsDrawFilter mDrawFilter;
    private int mHeight;
    private float mTouchInvalidateRadius;
    private int mWidth;
    private float maxValues = 60.0f;
    private ValueAnimator progressAnimator;
    private Paint progressPaint;
    private float progressWidth = ((float) dipToPx(10.0f));
    private Matrix rotateMatrix;
    private boolean seekEnable;
    private float shortDegree = ((float) dipToPx(5.0f));
    private int shortDegreeColor = -15658735;
    private float startAngle = 135.8f;
    private float sweepAngle = 270.0f;
    private SweepGradient sweepGradient;
    private String titleString;

    public ColorArcProgressBar(Context context) {
        super(context, null);
        initView(context);
    }

    public ColorArcProgressBar(Context context, AttributeSet attributeSet) {
        super(context, attributeSet, 0);
        initConfig(context, attributeSet);
        initView(context);
    }

    public ColorArcProgressBar(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        initConfig(context, attributeSet);
        initView(context);
    }

    private void initConfig(Context context, AttributeSet attributeSet) {
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.ColorArcProgressBar);
        int color = obtainStyledAttributes.getColor(4, Color.parseColor("#671e99"));
        int color2 = obtainStyledAttributes.getColor(5, Color.parseColor("#00000000"));
        int color3 = obtainStyledAttributes.getColor(6, Color.parseColor("#ff8614"));
        this.bgArcColor = obtainStyledAttributes.getColor(0, Color.parseColor("#112d42"));
        this.longDegreeColor = obtainStyledAttributes.getColor(18, Color.parseColor("#f86d01"));
        this.shortDegreeColor = obtainStyledAttributes.getColor(19, Color.parseColor("#ff4081"));
        this.hintColor = obtainStyledAttributes.getColor(8, Color.parseColor("#000000"));
        this.colors = new int[]{color, color2, color3, color3};
        this.sweepAngle = (float) obtainStyledAttributes.getInteger(17, 270);
        this.bgArcWidth = obtainStyledAttributes.getDimension(1, (float) dipToPx(2.0f));
        this.progressWidth = obtainStyledAttributes.getDimension(7, (float) dipToPx(10.0f));
        this.seekEnable = obtainStyledAttributes.getBoolean(13, false);
        this.isNeedTitle = obtainStyledAttributes.getBoolean(11, false);
        this.isNeedContent = obtainStyledAttributes.getBoolean(9, false);
        this.isNeedUnit = obtainStyledAttributes.getBoolean(12, false);
        this.isNeedDial = obtainStyledAttributes.getBoolean(10, false);
        this.hintString = obtainStyledAttributes.getString(16);
        this.titleString = obtainStyledAttributes.getString(15);
        this.currentValues = obtainStyledAttributes.getFloat(13, 0.0f);
        this.maxValues = obtainStyledAttributes.getFloat(14, 100.0f);
        setCurrentValues(this.currentValues);
        setMaxValues(this.maxValues);
        obtainStyledAttributes.recycle();
    }

    public void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i, i2, i3, i4);
        this.mWidth = i;
        this.mHeight = i2;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("onSizeChanged: mWidth:");
        stringBuilder.append(this.mWidth);
        stringBuilder.append(" mHeight:");
        stringBuilder.append(this.mHeight);
        String str = "ColorArcProgressBar";
        Log.v(str, stringBuilder.toString());
        this.diameter = (int) (((float) Math.min(this.mWidth, this.mHeight)) - (((this.longDegree + ((float) this.DEGREE_PROGRESS_DISTANCE)) + (this.progressWidth / 2.0f)) * 2.0f));
        stringBuilder = new StringBuilder();
        stringBuilder.append("onSizeChanged: diameter:");
        stringBuilder.append(this.diameter);
        Log.v(str, stringBuilder.toString());
        this.bgRect = new RectF();
        RectF rectF = this.bgRect;
        float f = this.longDegree;
        float f2 = (float) this.DEGREE_PROGRESS_DISTANCE;
        float f3 = this.progressWidth / 2.0f;
        float f4 = (f2 + f) + f3;
        rectF.top = f4;
        rectF.left = f4;
        f4 = (float) this.diameter;
        rectF.right = ((f4 + f3) + f) + f2;
        rectF.bottom = ((f4 + f) + f3) + f2;
        stringBuilder = new StringBuilder();
        stringBuilder.append("initView: ");
        stringBuilder.append(this.diameter);
        Log.v(str, stringBuilder.toString());
        float f5 = ((((((float) this.DEGREE_PROGRESS_DISTANCE) + this.longDegree) + (this.progressWidth / 2.0f)) * 2.0f) + ((float) this.diameter)) / 2.0f;
        this.centerX = f5;
        this.centerY = f5;
        this.sweepGradient = new SweepGradient(this.centerX, this.centerY, this.colors, null);
        this.mTouchInvalidateRadius = ((((float) (Math.max(this.mWidth, this.mHeight) / 2)) - this.longDegree) - ((float) this.DEGREE_PROGRESS_DISTANCE)) - (this.progressWidth * 2.0f);
        if (this.isAutoTextSize) {
            i = this.diameter;
            this.textSize = 20.0f;
            this.hintSize = 20.0f;
            this.curSpeedSize = 20.0f;
            this.vTextPaint.setTextSize(this.textSize);
            this.vTextPaint.setColor(Color.parseColor("#000000"));
            this.hintPaint.setTextSize(this.hintSize);
            this.hintPaint.setColor(Color.parseColor("#000000"));
            this.curSpeedPaint.setTextSize(this.curSpeedSize);
        }
    }

    private void initView(Context context) {
        this.degreePaint = new Paint();
        this.degreePaint.setColor(Color.parseColor("#000000"));
        this.allArcPaint = new Paint();
        this.allArcPaint.setAntiAlias(true);
        this.allArcPaint.setStyle(Style.STROKE);
        this.allArcPaint.setStrokeWidth(this.bgArcWidth);
        this.allArcPaint.setColor(this.bgArcColor);
        this.progressPaint = new Paint();
        this.progressPaint.setAntiAlias(true);
        this.progressPaint.setStyle(Style.STROKE);
        this.progressPaint.setStrokeWidth(this.progressWidth);
        this.progressPaint.setColor(Color.parseColor("#000000"));
        this.vTextPaint = new Paint();
        this.vTextPaint.setColor(getResources().getColor(R.color.progressBarColor));
        this.vTextPaint.setTextAlign(Align.CENTER);
        this.hintPaint = new Paint();
        this.hintPaint.setTextSize(this.hintSize);
        this.hintPaint.setColor(getResources().getColor(R.color.progressBarColor));
        this.hintPaint.setTextAlign(Align.CENTER);
        this.curSpeedPaint = new Paint();
        this.curSpeedPaint.setTextSize(this.curSpeedSize);
        this.curSpeedPaint.setColor(Color.parseColor("#000000"));
        this.curSpeedPaint.setTextAlign(Align.CENTER);
        this.mDrawFilter = new PaintFlagsDrawFilter(0, 3);
        this.rotateMatrix = new Matrix();
    }

    public void onDraw(Canvas canvas) {
        canvas.setDrawFilter(this.mDrawFilter);
        if (this.isNeedDial) {
            int i = 0;
            while (i < 40) {
                if (i <= 15 || i >= 25) {
                    float f;
                    float f2;
                    if (i % 5 == 0) {
                        this.degreePaint.setStrokeWidth((float) dipToPx(2.0f));
                        this.degreePaint.setColor(Color.parseColor("#000000"));
                        f = this.centerX;
                        f2 = ((this.centerY - ((float) (this.diameter / 2))) - (this.progressWidth / 2.0f)) - ((float) this.DEGREE_PROGRESS_DISTANCE);
                        canvas.drawLine(f, f2, f, f2 - this.longDegree, this.degreePaint);
                    } else {
                        this.degreePaint.setStrokeWidth((float) dipToPx(1.4f));
                        this.degreePaint.setColor(Color.parseColor("#000000"));
                        f = this.centerX;
                        float f3 = ((this.centerY - ((float) (this.diameter / 2))) - (this.progressWidth / 2.0f)) - ((float) this.DEGREE_PROGRESS_DISTANCE);
                        float f4 = this.longDegree;
                        float f5 = this.shortDegree;
                        f2 = f3 - ((f4 - f5) / 2.0f);
                        canvas.drawLine(f, f2, f, f2 - f5, this.degreePaint);
                    }
                    canvas.rotate(9.0f, this.centerX, this.centerY);
                } else {
                    canvas.rotate(9.0f, this.centerX, this.centerY);
                }
                i++;
            }
        }
        canvas.drawArc(this.bgRect, this.startAngle, this.sweepAngle, false, this.allArcPaint);
        this.rotateMatrix.setRotate(130.0f, this.centerX, this.centerY);
        this.sweepGradient.setLocalMatrix(this.rotateMatrix);
        this.progressPaint.setShader(this.sweepGradient);
        canvas.drawArc(this.bgRect, this.startAngle, this.currentAngle, false, this.progressPaint);
        if (this.isNeedContent) {
            this.vTextPaint.setColor(getResources().getColor(R.color.progressBarColor));
            canvas.drawText(String.format("%.1f", new Object[]{Float.valueOf(this.currentValues)}), this.centerX, this.centerY + (this.textSize / 3.0f), this.vTextPaint);
        }
        if (this.isNeedUnit) {
            canvas.drawText(this.hintString, this.centerX, this.centerY + ((this.textSize * 2.0f) / 3.0f), this.hintPaint);
        }
        if (this.isNeedTitle) {
            canvas.drawText(this.titleString, this.centerX, this.centerY - ((this.textSize * 20.0f) / 3.0f), this.curSpeedPaint);
        }
        invalidate();
    }

    public void setMaxValues(float f) {
        this.maxValues = f;
        this.f54k = this.sweepAngle / f;
    }

    public void setCurrentValues(float f) {
        float f2 = this.maxValues;
        if (f > f2) {
            f = f2;
        }
        if (f < 0.0f) {
            f = 0.0f;
        }
        this.currentValues = f;
        this.lastAngle = this.currentAngle;
        setAnimation(this.lastAngle, f * this.f54k, this.aniSpeed);
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (!this.seekEnable) {
            return false;
        }
        getParent().requestDisallowInterceptTouchEvent(true);
        int action = motionEvent.getAction();
        if (action == 0) {
            onStartTrackingTouch();
            updateOnTouch(motionEvent);
        } else if (action == 1) {
            onStopTrackingTouch();
            setPressed(false);
            getParent().requestDisallowInterceptTouchEvent(false);
        } else if (action == 2) {
            updateOnTouch(motionEvent);
        } else if (action == 3) {
            onStopTrackingTouch();
            setPressed(false);
            getParent().requestDisallowInterceptTouchEvent(false);
        }
        return true;
    }

    private void onStartTrackingTouch() {
        OnSeekArcChangeListener onSeekArcChangeListener = this.listener;
        if (onSeekArcChangeListener != null) {
            onSeekArcChangeListener.onStartTrackingTouch(this);
        }
    }

    private void onStopTrackingTouch() {
        OnSeekArcChangeListener onSeekArcChangeListener = this.listener;
        if (onSeekArcChangeListener != null) {
            onSeekArcChangeListener.onStopTrackingTouch(this);
        }
    }

    private void updateOnTouch(MotionEvent motionEvent) {
        if (validateTouch(motionEvent.getX(), motionEvent.getY())) {
            setPressed(true);
            int angleToProgress = angleToProgress(getTouchDegrees(motionEvent.getX(), motionEvent.getY()));
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("updateOnTouch: ");
            stringBuilder.append(angleToProgress);
            Log.v("ColorArcProgressBar", stringBuilder.toString());
            onProgressRefresh(angleToProgress, true);
        }
    }

    private boolean validateTouch(float f, float f2) {
        f -= this.centerX;
        f2 -= this.centerY;
        float sqrt = (float) Math.sqrt((double) ((f * f) + (f2 * f2)));
        double toDegrees = Math.toDegrees((Math.atan2((double) f2, (double) f) + 1.5707963267948966d) - Math.toRadians(225.0d));
        if (toDegrees < 0.0d) {
            toDegrees += 360.0d;
        }
        boolean z = sqrt > this.mTouchInvalidateRadius && toDegrees >= 0.0d && toDegrees <= 280.0d;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("validateTouch: ");
        stringBuilder.append(toDegrees);
        Log.v("ColorArcProgressBar", stringBuilder.toString());
        return z;
    }

    private double getTouchDegrees(float f, float f2) {
        double toDegrees = Math.toDegrees((Math.atan2((double) (f2 - this.centerY), (double) (f - this.centerX)) + 1.5707963267948966d) - Math.toRadians(225.0d));
        if (toDegrees < 0.0d) {
            toDegrees += 360.0d;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("getTouchDegrees: ");
        stringBuilder.append(toDegrees);
        Log.v("ColorArcProgressBar", stringBuilder.toString());
        return toDegrees;
    }

    private int angleToProgress(double d) {
        double valuePerDegree = (double) valuePerDegree();
        Double.isNaN(valuePerDegree);
        int round = (int) Math.round(valuePerDegree * d);
        if (round < 0) {
            round = 0;
        }
        float f = (float) round;
        float f2 = this.maxValues;
        return f > f2 ? (int) f2 : round;
    }

    private float valuePerDegree() {
        return this.maxValues / this.sweepAngle;
    }

    private void onProgressRefresh(int i, boolean z) {
        updateProgress(i, z);
    }

    private void updateProgress(int i, boolean z) {
        float f = (float) i;
        this.currentValues = f;
        OnSeekArcChangeListener onSeekArcChangeListener = this.listener;
        if (onSeekArcChangeListener != null) {
            onSeekArcChangeListener.onProgressChanged(this, i, z);
        }
        this.currentAngle = (f / this.maxValues) * this.sweepAngle;
        this.lastAngle = this.currentAngle;
        invalidate();
    }

    public void setArcWidth(int i) {
        this.bgArcWidth = (float) i;
    }

    public void setProgressWidth(int i) {
        this.progressWidth = (float) i;
    }

    public void setTextSize(int i) {
        this.textSize = (float) i;
    }

    public void setHintSize(int i) {
        this.hintSize = (float) i;
    }

    public void setUnit(String str) {
        this.hintString = str;
        invalidate();
    }

    public void setDiameter(int i) {
        this.diameter = dipToPx((float) i);
    }

    private void setTitle(String str) {
        this.titleString = str;
    }

    private void setIsNeedTitle(boolean z) {
        this.isNeedTitle = z;
    }

    private void setIsNeedUnit(boolean z) {
        this.isNeedUnit = z;
    }

    private void setIsNeedDial(boolean z) {
        this.isNeedDial = z;
    }

    private void setAnimation(float f, float f2, int i) {
        this.progressAnimator = ValueAnimator.ofFloat(new float[]{f, f2});
        this.progressAnimator.setDuration((long) i);
        this.progressAnimator.setTarget(Float.valueOf(this.currentAngle));
        this.progressAnimator.addUpdateListener(new AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                ColorArcProgressBar.this.currentAngle = ((Float) valueAnimator.getAnimatedValue()).floatValue();
                ColorArcProgressBar colorArcProgressBar = ColorArcProgressBar.this;
                colorArcProgressBar.currentValues = colorArcProgressBar.currentAngle / ColorArcProgressBar.this.f54k;
            }
        });
        this.progressAnimator.start();
    }

    public void setSeekEnable(boolean z) {
        this.seekEnable = z;
    }

    public void setOnSeekArcChangeListener(OnSeekArcChangeListener onSeekArcChangeListener) {
        this.listener = onSeekArcChangeListener;
    }

    private int dipToPx(float f) {
        return (int) ((getContext().getResources().getDisplayMetrics().density * f) + (((float) (f >= 0.0f ? 1 : -1)) * 0.5f));
    }

    private int getScreenWidth() {
        WindowManager windowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }

    public interface OnSeekArcChangeListener {
        void onProgressChanged(ColorArcProgressBar colorArcProgressBar, int i, boolean z);

        void onStartTrackingTouch(ColorArcProgressBar colorArcProgressBar);

        void onStopTrackingTouch(ColorArcProgressBar colorArcProgressBar);
    }
}
