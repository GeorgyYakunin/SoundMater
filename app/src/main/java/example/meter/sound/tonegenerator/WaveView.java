package example.meter.sound.tonegenerator;

import android.content.Context;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;

public abstract class WaveView extends LinearLayout {
    protected float amplitude;
    protected float frequency;
    protected OnWaveChangeListener listener;
    protected Wave wave;
    private boolean initialized = false;
    private float maxAmplitude;
    private float maxFrequency;
    private float minAmplitude;
    private float minFrequency;
    private float sampleRate;

    public WaveView(Context context) {
        super(context);
    }

    public WaveView(Context context, @Nullable AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public abstract Wave createWave();

    public abstract void initView();

    public void init(float f, float f2, float f3, float f4, float f5, float f6, float f7) {
        this.sampleRate = f;
        this.minFrequency = f2;
        this.maxFrequency = f3;
        this.frequency = f4;
        this.minAmplitude = f5;
        this.maxAmplitude = f6;
        this.amplitude = f7;
        this.initialized = true;
        initView();
        updateWave();
    }

    public void setOnWaveChangeListener(OnWaveChangeListener onWaveChangeListener) {
        this.listener = onWaveChangeListener;
        onWaveChangeListener.onWaveChanged(this.wave);
    }

    public float getSampleRate() {
        return this.sampleRate;
    }

    public float getMinFrequency() {
        return this.minFrequency;
    }

    public float getMaxFrequency() {
        return this.maxFrequency;
    }

    public float getMinAmplitude() {
        return this.minAmplitude;
    }

    public float getMaxAmplitude() {
        return this.maxAmplitude;
    }

    public Wave getWave() {
        return this.wave;
    }

    public void updateWave() {
        if (this.initialized) {
            this.wave = createWave();
            OnWaveChangeListener onWaveChangeListener = this.listener;
            if (onWaveChangeListener != null) {
                onWaveChangeListener.onWaveChanged(this.wave);
            }
        }
    }

    public interface OnWaveChangeListener {
        void onWaveChanged(Wave wave);
    }
}
