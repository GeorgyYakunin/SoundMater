package example.meter.sound.soundmeter;

public class AudioDetails {
    private String mAvgValue;
    private String mCreationDate;
    private String mDuration;
    private int mEnvironment;
    private String mId;
    private String mMaxValue;
    private String mMinValue;
    private String mName;

    public AudioDetails(String str, String str2, String str3, String str4, String str5, String str6, String str7, int i) {
        this.mId = str;
        this.mName = str2;
        this.mCreationDate = str3;
        this.mMinValue = str5;
        this.mAvgValue = str6;
        this.mMaxValue = str7;
        this.mDuration = str4;
        this.mEnvironment = i;
    }

    public String getId() {
        return this.mId;
    }

    public void setId(String str) {
        this.mId = str;
    }

    public String getName() {
        return this.mName;
    }

    public void setName(String str) {
        this.mName = str;
    }

    public String getCreationDate() {
        return this.mCreationDate;
    }

    public void setCreationDate(String str) {
        this.mCreationDate = str;
    }

    public String getMinValue() {
        return this.mMinValue;
    }

    public void setMinValue(String str) {
        this.mMinValue = str;
    }

    public String getAvgValue() {
        return this.mAvgValue;
    }

    public void setAvgValue(String str) {
        this.mAvgValue = str;
    }

    public String getMaxValue() {
        return this.mMaxValue;
    }

    public void setMaxValue(String str) {
        this.mMaxValue = str;
    }

    public String getDuration() {
        return this.mDuration;
    }

    public void setDuration(String str) {
        this.mDuration = str;
    }

    public int getEnvironment() {
        return this.mEnvironment;
    }

    public void setEnvironment(int i) {
        this.mEnvironment = i;
    }
}
