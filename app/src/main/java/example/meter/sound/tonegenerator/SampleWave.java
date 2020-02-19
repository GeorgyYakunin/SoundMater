package example.meter.sound.tonegenerator;

public class SampleWave extends Wave {
    private float baseFrequency;
    private float duration;
    private float[] samples;
    private float speedFactor;

    public SampleWave(float f, float f2, float f3, float[] fArr, float f4) {
        super(f, f2, f3);
        this.samples = fArr;
        this.baseFrequency = f4;
        this.speedFactor = f2 / f4;
        this.duration = (((float) fArr.length) / f) / this.speedFactor;
    }

    public float getSample(float f) {
        if (f > getPeriod()) {
            return 0.0f;
        }
        return getInterpolatedSample(this.speedFactor * f);
    }

    private float getInterpolatedSample(float f) {
        f *= this.sampleRate;
        int i = (int) f;
        int i2 = i + 1;
        try {
            float f2 = this.samples[i];
            return this.amplitude * (f2 + ((f - ((float) i)) * (this.samples[i2] - f2)));
        } catch (Exception unused) {
            return 0.0f;
        }
    }

    public float getPeriod() {
        return this.duration;
    }

    public float getBaseFrequency() {
        return this.baseFrequency;
    }
}
