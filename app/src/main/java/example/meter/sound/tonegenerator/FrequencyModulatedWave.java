package example.meter.sound.tonegenerator;

public class FrequencyModulatedWave extends ModulatedWave {
    public FrequencyModulatedWave(Wave wave, Wave wave2) {
        super(wave, wave2);
    }

    public float getSample(float f) {
        if (this.frequency <= 0.0f) {
            return 0.0f;
        }
        return this.carrierWave.getSample(((this.carrierWave.getFrequency() + this.modulatorWave.getSample(f)) / this.carrierWave.getFrequency()) * f);
    }
}
