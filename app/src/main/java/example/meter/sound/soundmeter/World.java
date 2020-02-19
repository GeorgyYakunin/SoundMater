package example.meter.sound.soundmeter;

public class World {
    public static float avgDB = 0.0f;
    public static float avgDB1 = 20.0f;
    public static int calibration = 0;
    public static float dbCount = 40.0f;
    public static float lastDbCount = dbCount;
    public static float maxDB = 0.0f;
    public static float minDB = 200.0f;
    public static float noOfRecordings;
    public static double sumDB;
    public static double sumDB1;
    private static float min = 0.5f;
    private static float value = 0.0f;

    public static void setDbCount(float f) {
        float f2 = lastDbCount;
        if (f > f2) {
            f -= f2;
            f2 = min;
            if (f <= f2) {
                f = f2;
            }
            value = f;
        } else {
            f -= f2;
            f2 = -min;
            if (f >= f2) {
                f = f2;
            }
            value = f;
        }
        dbCount = lastDbCount + (value * 0.2f);
        f = dbCount;
        lastDbCount = f;
        noOfRecordings += 1.0f;
        double d = 0.0d;
        sumDB += f < 0.0f ? 0.0d : (double) f;
        double d2 = sumDB1;
        if (f >= 20.0f) {
            d = (double) f;
        }
        sumDB1 = d2 + d;
        f = (float) sumDB;
        f2 = noOfRecordings;
        avgDB = f / f2;
        avgDB1 = ((float) sumDB1) / f2;
        f = dbCount;
        if (f < minDB) {
            minDB = f;
        }
        f = dbCount;
        if (f > maxDB) {
            maxDB = f;
        }
    }
}
