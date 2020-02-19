package example.meter.sound.tonegenerator;

import android.content.Context;
import androidx.annotation.Nullable;
import android.util.AttributeSet;

public class SineWaveView extends SimpleWaveView {
    public SineWaveView(Context context) {
        super(context);
    }

    public SineWaveView(Context context, @Nullable AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public Wave createWave() {
        return new SineWave(getSampleRate(), this.frequency, this.amplitude);
    }
}
