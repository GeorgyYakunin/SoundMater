package example.meter.sound.tonegenerator;

public class SineWave extends Wave {
    private float angularFrequency;

    public SineWave(float f, float f2, float f3) {
        super(f, f2, f3);
        double d = (double) f2;
        Double.isNaN(d);
        this.angularFrequency = (float) (d * 6.283185307179586d);
    }

    public float getSample(float f) {
        if (this.frequency <= 0.0f) {
            return 0.0f;
        }
        return this.amplitude * ((float) Math.sin((double) (this.angularFrequency * f)));
    }
}
