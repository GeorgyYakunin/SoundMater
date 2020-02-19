package example.meter.sound.tonegenerator;

import android.content.Context;
import androidx.annotation.Nullable;
import android.util.AttributeSet;

public class SquareWaveView extends SimpleWaveView {
    public SquareWaveView(Context context) {
        super(context);
    }

    public SquareWaveView(Context context, @Nullable AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public Wave createWave() {
        return new SquareWave(getSampleRate(), this.frequency, this.amplitude);
    }
}
