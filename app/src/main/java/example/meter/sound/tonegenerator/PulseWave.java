package example.meter.sound.tonegenerator;

public class PulseWave extends Wave {
    protected float dutyCycle;

    public PulseWave(float f, float f2, float f3, float f4) {
        super(f, f2, f3);
        this.dutyCycle = f4;
    }

    public float getDutyCycle() {
        return this.dutyCycle;
    }

    public float getSample(float f) {
        if (this.frequency <= 0.0f) {
            return 0.0f;
        }
        if (getPhase(f) / getPeriod() < this.dutyCycle) {
            return this.amplitude;
        }
        return -this.amplitude;
    }
}
