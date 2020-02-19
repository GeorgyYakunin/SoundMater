package example.meter.sound.tonegenerator;

public abstract class ModulatedWave extends Wave {
    protected Wave carrierWave;
    protected Wave modulatorWave;

    public ModulatedWave(Wave wave, Wave wave2) {
        super(wave.getSampleRate(), wave.getFrequency(), wave.getAmplitude());
        this.carrierWave = wave;
        this.modulatorWave = wave2;
    }

    public float getPeriod() {
        return Math.max(this.carrierWave.getPeriod(), this.modulatorWave.getPeriod());
    }
}
