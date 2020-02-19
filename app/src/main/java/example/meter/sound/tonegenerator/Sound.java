package example.meter.sound.tonegenerator;

import android.media.AudioTrack;

public class Sound {
    private int actualBufferSize;
    private short[] buffer;
    private int maxNumSamples;
    private int samplesPerFrame;
    private AudioTrack track;

    public Sound(int i, boolean z, float f) {
        int i2 = z ? 12 : 4;
        this.samplesPerFrame = z ? 2 : 1;
        this.maxNumSamples = (int) ((f * ((float) i)) * ((float) this.samplesPerFrame));
        this.actualBufferSize = Math.max(this.maxNumSamples * 2, AudioTrack.getMinBufferSize(i, i2, 2));
        this.track = new AudioTrack(3, i, i2, 2, this.actualBufferSize, 0);
    }

    public void fillBuffer(float[] fArr) {
        this.buffer = new short[Math.min(fArr.length, this.maxNumSamples)];
        int i = 0;
        while (true) {
            short[] sArr = this.buffer;
            if (i < sArr.length) {
                sArr[i] = (short) ((int) (fArr[i] * 32767.0f));
                i++;
            } else {
                this.track.write(sArr, 0, sArr.length);
                return;
            }
        }
    }

    public void play() {
        try {
            this.track.play();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    public void loop() {
        try {
            this.track.stop();
            this.track.setLoopPoints(0, this.buffer.length / this.samplesPerFrame, -1);
            this.track.play();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    public void pause() {
        try {
            this.track.pause();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        try {
            this.track.stop();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    public short[] getBuffer() {
        return this.buffer;
    }

    public int getActualBufferSize() {
        return this.actualBufferSize;
    }
}
