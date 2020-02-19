package example.meter.sound.tonegenerator;

import android.content.Context;
import androidx.annotation.Nullable;
import android.util.AttributeSet;

public class TriangleWaveView extends SimpleWaveView {
    public TriangleWaveView(Context context) {
        super(context);
    }

    public TriangleWaveView(Context context, @Nullable AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public Wave createWave() {
        return new TriangleWave(getSampleRate(), this.frequency, this.amplitude);
    }
}
