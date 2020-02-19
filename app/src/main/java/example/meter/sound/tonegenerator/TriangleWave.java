package example.meter.sound.tonegenerator;

public class TriangleWave extends Wave {
    public TriangleWave(float f, float f2, float f3) {
        super(f, f2, f3);
    }

    public float getSample(float f) {
        f = getPhase(f);
        float period = getPeriod();
        float f2 = (this.amplitude * 4.0f) / period;
        if (f < 0.25f * period) {
            return f2 * f;
        }
        if (f < period * 0.75f) {
            return (this.amplitude * 2.0f) - (f2 * f);
        }
        return (f2 * f) - (this.amplitude * 4.0f);
    }
}
