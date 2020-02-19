package example.meter.sound.tonegenerator;

public class ModulatedWaveFactory {
    public static Wave createModulatedWave(int i, Wave wave, Wave wave2) {
        if (wave == null) {
            return null;
        }
        if (wave2 == null) {
            return wave;
        }
        switch (i) {
            case 0:
                return new RingModulatedWave(wave, wave2);
            case 1:
                return new AmplitudeModulatedWave(wave, wave2);
            case 2:
                return new FrequencyModulatedWave(wave, wave2);
            default:
                return null;
        }
    }
}
