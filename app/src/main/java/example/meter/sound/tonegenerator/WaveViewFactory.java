package example.meter.sound.tonegenerator;

import android.content.Context;

public class WaveViewFactory {
    public static WaveView createWaveView(int i, Context context, float f, float f2, float f3, float f4, float f5, float f6, float f7) {
        WaveView sineWaveView;
        Context context2 = context;
        switch (i) {
            case 0:
                sineWaveView = new SineWaveView(context);
                break;
            case 1:
                sineWaveView = new SquareWaveView(context);
                break;
            case 2:
                sineWaveView = new PulseWaveView(context);
                break;
            case 3:
                sineWaveView = new TriangleWaveView(context);
                break;
            case 4:
                sineWaveView = new SawtoothWaveView(context);
                break;
            default:
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Invalid wave type: ");
                stringBuilder.append(i);
                throw new RuntimeException(stringBuilder.toString());
        }
        sineWaveView.init(f, f2, f3, f4, f5, f6, f7);
        return sineWaveView;
    }
}
