package example.meter.sound.tonegenerator;

import android.content.Context;
import androidx.annotation.Nullable;
import android.util.AttributeSet;

import example.meter.sound.R;
import example.meter.sound.tonegenerator.CustomSeekBar.OnValueChangeListener;
import example.meter.sound.tonegenerator.CustomSeekBar.Scale;

public class PulseWaveView extends WaveView {
    public float dutyCycle = 0.5f;
    private CustomSeekBar amplitudeSeekBar;
    private CustomSeekBar dutyCycleSeekBar;
    private CustomSeekBar frequencySeekBar;
    private float maxDutyCycle = 1.0f;
    private float minDutyCycle = 0.0f;

    public PulseWaveView(Context context) {
        super(context);
    }

    public PulseWaveView(Context context, @Nullable AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public void initView() {
        inflate(getContext(), R.layout.view_pulse_wave, this);
        this.frequencySeekBar = (CustomSeekBar) findViewById(R.id.pulseWave_frequencySeekBar);
        this.dutyCycleSeekBar = (CustomSeekBar) findViewById(R.id.pulseWave_dutyCycleSeekBar);
        this.amplitudeSeekBar = (CustomSeekBar) findViewById(R.id.pulseWave_amplitudeSeekBar);
        this.frequencySeekBar.setTitle("Frequency");
        this.frequencySeekBar.setMinValue((double) getMinFrequency());
        this.frequencySeekBar.setMaxValue((double) getMaxFrequency());
        this.frequencySeekBar.setNumSteps(100);
        this.frequencySeekBar.setNumDecimalPlaces(0);
        this.frequencySeekBar.setScale(Scale.LOGARITHMIC);
        this.frequencySeekBar.setValue((double) this.frequency);
        this.frequencySeekBar.setOnValueChangeListener(new OnValueChangeListener() {
            public void onValueChanged(double d) {
                PulseWaveView pulseWaveView = PulseWaveView.this;
                pulseWaveView.frequency = (float) d;
                pulseWaveView.updateWave();
            }
        });
        this.dutyCycleSeekBar.setTitle("Duty Cycle");
        this.dutyCycleSeekBar.setMinValue((double) this.minDutyCycle);
        this.dutyCycleSeekBar.setMaxValue((double) this.maxDutyCycle);
        this.dutyCycleSeekBar.setNumSteps(100);
        this.dutyCycleSeekBar.setNumDecimalPlaces(2);
        this.dutyCycleSeekBar.setScale(Scale.LINEAR);
        this.dutyCycleSeekBar.setValue((double) this.dutyCycle);
        this.dutyCycleSeekBar.setOnValueChangeListener(new OnValueChangeListener() {
            public void onValueChanged(double d) {
                PulseWaveView pulseWaveView = PulseWaveView.this;
                pulseWaveView.dutyCycle = (float) d;
                pulseWaveView.updateWave();
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
                PulseWaveView pulseWaveView = PulseWaveView.this;
                pulseWaveView.amplitude = (float) d;
                pulseWaveView.updateWave();
            }
        });
    }

    public Wave createWave() {
        return new PulseWave(getSampleRate(), this.frequency, this.amplitude, this.dutyCycle);
    }
}
