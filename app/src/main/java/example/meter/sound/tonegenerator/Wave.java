package example.meter.sound.tonegenerator;

public abstract class Wave {
    protected float amplitude;
    protected float frequency;
    protected float sampleRate;

    public Wave(float f, float f2, float f3) {
        this.sampleRate = f;
        this.frequency = f2;
        this.amplitude = f3;
    }



    public final float[] generate(float f) {
        if (f < 0.0f || Float.isNaN(f) || Float.isInfinite(f)) {
            StringBuilder sb = new StringBuilder();
            sb.append("Invalid duration: ");
            sb.append(f);
            throw new RuntimeException(sb.toString());
        }
        int i = (int) (f * this.sampleRate);
        float[] fArr = new float[i];
        for (int i2 = 0; i2 < i; i2++) {
            fArr[i2] = getSample(((float) i2) / this.sampleRate);
        }
        return fArr;
    }




    public abstract float getSample(float f);

    public final float[] generatePeriod() {
        return generate(getPeriod());
    }

    public final float getPhase(float f) {
        return f % getPeriod();
    }

    public final float getSampleRate() {
        return this.sampleRate;
    }

    public final float getFrequency() {
        return this.frequency;
    }

    public final float getAmplitude() {
        return this.amplitude;
    }

    public float getPeriod() {
        return 1.0f / getFrequency();
    }
}
