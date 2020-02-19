package example.meter.sound.soundmeter;

public interface PlayerAdapter {
    void initializeProgressCallback();

    boolean isPlaying();

    void loadMedia(int i);

    void pause();

    void play();

    void release();

    void reset();

    void seekTo(int i);
}
