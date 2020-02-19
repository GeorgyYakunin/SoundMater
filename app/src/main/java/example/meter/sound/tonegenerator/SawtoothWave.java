package example.meter.sound.tonegenerator;

public class SawtoothWave extends Wave {
    public SawtoothWave(float f, float f2, float f3) {
        super(f, f2, f3);
    }

    public float getSample(float f) {
        f = getPhase(f);
        float period = getPeriod();
        float f2 = (this.amplitude * 2.0f) / period;
        if (f < period * 0.5f) {
            return f2 * f;
        }
        return (f2 * f) - (this.amplitude * 2.0f);
    }
}
