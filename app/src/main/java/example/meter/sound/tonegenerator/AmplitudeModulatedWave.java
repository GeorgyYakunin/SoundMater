package example.meter.sound.tonegenerator;

public class AmplitudeModulatedWave extends ModulatedWave {
    public AmplitudeModulatedWave(Wave wave, Wave wave2) {
        super(wave, wave2);
    }

    public float getSample(float f) {
        if (this.frequency <= 0.0f) {
            return 0.0f;
        }
        return ((this.modulatorWave.getSample(f) + this.modulatorWave.getAmplitude()) * 0.5f) * this.carrierWave.getSample(f);
    }
}
