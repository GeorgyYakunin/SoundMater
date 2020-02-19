package example.meter.sound.tonegenerator;

import android.content.Context;
import androidx.annotation.Nullable;
import android.util.AttributeSet;

public class SawtoothWaveView extends SimpleWaveView {
    public SawtoothWaveView(Context context) {
        super(context);
    }

    public SawtoothWaveView(Context context, @Nullable AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public Wave createWave() {
        return new SawtoothWave(getSampleRate(), this.frequency, this.amplitude);
    }
}
