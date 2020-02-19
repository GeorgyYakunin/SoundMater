package example.meter.sound.soundmeter;

public class WaveSample {
    private long amplitude;
    private long time;

    public WaveSample(long j, int i) {
        this.time = j;
        this.amplitude = (long) i;
    }

    public long getTime() {
        return this.time;
    }

    public void setTime(long j) {
        this.time = j;
    }

    public long getAmplitude() {
        return this.amplitude;
    }

    public void setAmplitude(long j) {
        this.amplitude = j;
    }
}
