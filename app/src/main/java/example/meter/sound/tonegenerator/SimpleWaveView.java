package example.meter.sound.tonegenerator;

import android.content.Context;
import androidx.annotation.Nullable;
import android.util.AttributeSet;

import example.meter.sound.R;
import example.meter.sound.tonegenerator.CustomSeekBar.OnValueChangeListener;
import example.meter.sound.tonegenerator.CustomSeekBar.Scale;

public abstract class SimpleWaveView extends WaveView {
    private CustomSeekBar amplitudeSeekBar;
    private CustomSeekBar frequencySeekBar;

    public SimpleWaveView(Context context) {
        super(context);
    }

    public SimpleWaveView(Context context, @Nullable AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public void initView() {
        inflate(getContext(), R.layout.view_simple_wave, this);
        this.frequencySeekBar = (CustomSeekBar) findViewById(R.id.simpleWave_frequencySeekBar);
        this.amplitudeSeekBar = (CustomSeekBar) findViewById(R.id.simpleWave_amplitudeSeekBar);
        this.frequencySeekBar.setTitle("Frequency");
        this.frequencySeekBar.setMinValue((double) getMinFrequency());
        this.frequencySeekBar.setMaxValue((double) getMaxFrequency());
        this.frequencySeekBar.setNumSteps(100);
        this.frequencySeekBar.setNumDecimalPlaces(0);
        this.frequencySeekBar.setScale(Scale.LOGARITHMIC);
        this.frequencySeekBar.setValue((double) this.frequency);
        this.frequencySeekBar.setOnValueChangeListener(new OnValueChangeListener() {
            public void onValueChanged(double d) {
                SimpleWaveView simpleWaveView = SimpleWaveView.this;
                simpleWaveView.frequency = (float) d;
                simpleWaveView.updateWave();
            }
        });
        this.amplitudeSeekBar.setTitle("Amplitude");
        this.amplitudeSeekBar.setMinValue((double) getMinAmplitude());
        this.amplitudeSeekBar.setMaxValue((double) getMaxAmplitude());
        this.amplitudeSeekBar.setNumSteps(100);
        this.amplitudeSeekBar.setNumDecimalPlaces(2);
        this.amplitudeSeekBar.setScale(Scale.LINEAR);
        this.amplitudeSeekBar.setValue((double) this.amplitude);
        this.amplitudeSeekBar.setOnValueChangeListener(new OnValueChangeListener() {
            public void onValueChanged(double d) {
                SimpleWaveView simpleWaveView = SimpleWaveView.this;
                simpleWaveView.amplitude = (float) d;
                simpleWaveView.updateWave();
            }
        });
    }
}
