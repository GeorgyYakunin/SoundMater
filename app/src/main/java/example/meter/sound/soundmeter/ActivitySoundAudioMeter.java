package example.meter.sound.soundmeter;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaMetadataRetriever;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat.Builder;
import androidx.core.content.ContextCompat;
import androidx.core.internal.view.SupportMenu;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.rm.rmswitch.RMSwitch;
import com.rm.rmswitch.RMSwitch.RMSwitchObserver;

import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import example.meter.sound.R;

public class ActivitySoundAudioMeter extends AppCompatActivity {
    public static final String MEDIA_PATH;
    public static final String SCALE = "scale";
    private static final String TAG = "ActivitySoundAudioMeter";

    static {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(Environment.getExternalStorageDirectory());
        stringBuilder.append(File.separator);
        stringBuilder.append("Decibel - Saved Recordings");
        MEDIA_PATH = stringBuilder.toString();
    }

    @SuppressLint({"HandlerLeak"})
    final Handler handler;
    public boolean bListener;
    public DatabaseHelper f57db;
    public String fileNameToSave;
    public GraphView graphView;
    public TextView guessSurroundingTextView;
    public boolean ismRecordingThreadRun;
    public ArrayList<AudioDetails> mAudioDetailsArrayList = new ArrayList();
    public MyMediaRecorder mRecorder;
    public Thread mRecordingThread;
    public Boolean notificationSoundEnabled;
    public RMSwitch notificationSoundSwitch;
    public RMSwitch notificationThresholdSwitch;
    public RMSwitch notificationVibrateSwitch;
    public Boolean notificationVibrationEnabled;
    public long startTime;
    public Boolean thresholdEnabled;
    public ImageView threshold_icon;
    LinearLayout calibrateButton;
    ImageView calibrateImageView;
    int calibration;
    BottomSheetDialog calibrationDialog;
    TextView calibrationValueTextView;
    ColorArcProgressBar colorArcProgressBar;
    Context context;
    long currentTime;
    ImageView currentlyActiveImageView;
    TextView currentlyActiveTextView;
    Dialog examineDialog;
    ImageView examineImageView;
    ImageView examinePopupArrow;
    LinearLayout examinePopupChildLinearLayout;
    LinearLayout examinePopupRanges;
    TextView examinePopupText;
    String[] guessSurrounding;
    LinearLayout historyButton;
    ImageView historyImageView;
    boolean isChart;
    int noOfTimesThresholdCrossed;
    LinearLayout playPauseButton;
    ImageView playPauseImageView;
    TextView playPauseTextView;
    int refresh;
    LinearLayout refreshButton;
    ImageView refreshImageView;
    boolean refreshed;
    ImageView saveAudioImageView;
    int scale = 8;
    TextView textViewAvgValue;
    TextView textViewMaxValue;
    TextView textViewMinValue;
    int threshold;
    LinearLayout thresholdButton;
    Boolean thresholdCrossed;
    BottomSheetDialog thresholdDialog;
    ImageView thresholdImageView;
    int thresholdTemp;
    TextView thresholdValueTextView;
    Toolbar toolbar;
    float volume;
    private RecyclerView recyclerViewHistoryList;
    private List samples;
    private Bundle savedInstanceStateNew;
    private TextView txt;

    public ActivitySoundAudioMeter() {
        Boolean valueOf = Boolean.valueOf(false);
        this.refreshed = false;
        this.currentTime = 0;
        this.isChart = false;
        Boolean valueOf2 = Boolean.valueOf(true);
        this.bListener = true;
        this.ismRecordingThreadRun = true;
        this.volume = 10000.0f;
        this.refresh = 0;
        this.startTime = 0;
        this.thresholdEnabled = valueOf;
        this.notificationSoundEnabled = valueOf2;
        this.notificationVibrationEnabled = valueOf2;
        this.guessSurrounding = new String[]{"Threshold of pain, Thunder", "Concerts, Screaming child", "Motorcycle, Blow dryer", "Diesel truck, Power mower", "Loud music, Alarm clocks", "Busy traffic, Vacuum cleaner", "Normal conversation at 3 ft.", "Quiet office, Moderate rainfall", "Quiet library, Bird calls", "Whisper, Quiet rural area", "Rustling leaves, Ticking watch", "Almost quiet, Breathing"};
        this.calibration = 0;
        this.threshold = 80;
        this.thresholdTemp = 0;
        this.noOfTimesThresholdCrossed = 0;
        this.thresholdCrossed = valueOf;
        this.handler = new Handler() {
            public void handleMessage(Message message) {
                super.handleMessage(message);
                DecimalFormat decimalFormat = new DecimalFormat("####.0");
                if (message.what == 1) {
                    ActivitySoundAudioMeter activitySoundAudioMeter;
                    ActivitySoundAudioMeter.this.colorArcProgressBar.setCurrentValues(World.dbCount < 0.0f ? 0.0f : World.dbCount);
                    double d = 0.0d;
                    ActivitySoundAudioMeter.this.txt.setText(decimalFormat.format(World.avgDB1 < 0.0f ? 0.0d : (double) ((long) World.avgDB1)));
                    ActivitySoundAudioMeter.this.textViewMinValue.setText(decimalFormat.format(World.minDB < 0.0f ? 0.0d : (double) World.minDB));
                    ActivitySoundAudioMeter.this.textViewAvgValue.setText(decimalFormat.format(World.avgDB < 0.0f ? 0.0d : (double) World.avgDB));
                    TextView textView = ActivitySoundAudioMeter.this.textViewMaxValue;
                    if (World.maxDB >= 0.0f) {
                        d = (double) World.maxDB;
                    }
                    textView.setText(decimalFormat.format(d));
                    try {
                        ActivitySoundAudioMeter.this.guessSurroundingTextView.setText(ActivitySoundAudioMeter.this.guessSurrounding[12 - (((int) World.dbCount) / 10)]);
                    } catch (ArrayIndexOutOfBoundsException unused) {
                        ActivitySoundAudioMeter.this.guessSurroundingTextView.setText(ActivitySoundAudioMeter.this.guessSurrounding[11]);
                    }
                    if (ActivitySoundAudioMeter.this.calibrationDialog.isShowing()) {
                        if (ActivitySoundAudioMeter.this.calibration < 0) {
                            ActivitySoundAudioMeter.this.calibrationValueTextView.setText(String.format("(%.2f - %d)", new Object[]{Float.valueOf(World.dbCount), Integer.valueOf(Math.abs(ActivitySoundAudioMeter.this.calibration))}));
                        } else {
                            ActivitySoundAudioMeter.this.calibrationValueTextView.setText(String.format("(%.2f + %d)", new Object[]{Float.valueOf(World.dbCount), Integer.valueOf(ActivitySoundAudioMeter.this.calibration)}));
                        }
                    }
                    if (ActivitySoundAudioMeter.this.thresholdEnabled.booleanValue()) {
                        if (World.dbCount >= ((float) ActivitySoundAudioMeter.this.threshold) && !ActivitySoundAudioMeter.this.thresholdCrossed.booleanValue()) {
                            ActivitySoundAudioMeter.this.thresholdCrossed = Boolean.valueOf(true);
                            activitySoundAudioMeter = ActivitySoundAudioMeter.this;
                            activitySoundAudioMeter.noOfTimesThresholdCrossed++;
                            String str = "dB ";
                            String str2 = "Sound exceeded threshold i.e. ";
                            StringBuilder stringBuilder;
                            if (ActivitySoundAudioMeter.this.noOfTimesThresholdCrossed > 1) {
                                ActivitySoundAudioMeter activitySoundAudioMeter2 = ActivitySoundAudioMeter.this;
                                stringBuilder = new StringBuilder();
                                stringBuilder.append(str2);
                                stringBuilder.append(ActivitySoundAudioMeter.this.threshold);
                                stringBuilder.append(str);
                                stringBuilder.append(ActivitySoundAudioMeter.this.noOfTimesThresholdCrossed);
                                stringBuilder.append(" times");
                                activitySoundAudioMeter2.sendNotification("Threshold Crossed", stringBuilder.toString(), "Sound threshold crossed", ActivitySoundAudioMeter.this.notificationSoundEnabled.booleanValue(), ActivitySoundAudioMeter.this.notificationVibrationEnabled.booleanValue());
                            } else {
                                ActivitySoundAudioMeter activitySoundAudioMeter3 = ActivitySoundAudioMeter.this;
                                stringBuilder = new StringBuilder();
                                stringBuilder.append(str2);
                                stringBuilder.append(ActivitySoundAudioMeter.this.threshold);
                                stringBuilder.append(str);
                                stringBuilder.append(ActivitySoundAudioMeter.this.noOfTimesThresholdCrossed);
                                stringBuilder.append(" time");
                                activitySoundAudioMeter3.sendNotification("Threshold Crossed", stringBuilder.toString(), "Sound threshold crossed", ActivitySoundAudioMeter.this.notificationSoundEnabled.booleanValue(), ActivitySoundAudioMeter.this.notificationVibrationEnabled.booleanValue());
                            }
                        }
                        if (World.dbCount < ((float) ActivitySoundAudioMeter.this.threshold)) {
                            ActivitySoundAudioMeter.this.thresholdCrossed = Boolean.valueOf(false);
                        }
                    }
                    if (ActivitySoundAudioMeter.this.examineDialog.isShowing()) {
                        try {
                            ActivitySoundAudioMeter.this.examinePopupText.setTextColor(ContextCompat.getColor(ActivitySoundAudioMeter.this, R.color.decibel));
                            ActivitySoundAudioMeter.this.examinePopupArrow.setColorFilter(ContextCompat.getColor(ActivitySoundAudioMeter.this, R.color.decibel));
                            ActivitySoundAudioMeter.this.examinePopupChildLinearLayout = (LinearLayout) ActivitySoundAudioMeter.this.examinePopupRanges.getChildAt(12 - (((int) World.dbCount) / 10));
                            ActivitySoundAudioMeter.this.examinePopupArrow = (ImageView) ActivitySoundAudioMeter.this.examinePopupChildLinearLayout.getChildAt(0);
                            ActivitySoundAudioMeter.this.examinePopupArrow.setColorFilter(ContextCompat.getColor(ActivitySoundAudioMeter.this, R.color.colorRed));
                            ActivitySoundAudioMeter.this.examinePopupText = (TextView) ActivitySoundAudioMeter.this.examinePopupChildLinearLayout.getChildAt(1);
                            ActivitySoundAudioMeter.this.examinePopupText.setTextColor(ContextCompat.getColor(ActivitySoundAudioMeter.this, R.color.colorRed));
                        } catch (Exception unused2) {
                            Log.e(ActivitySoundAudioMeter.TAG, "handleMessage: error opening examine dialog box");
                        }
                    }
                    if (ActivitySoundAudioMeter.this.refresh == 1) {
                        ActivitySoundAudioMeter.this.refresh = 0;
                    } else {
                        activitySoundAudioMeter = ActivitySoundAudioMeter.this;
                        activitySoundAudioMeter.refresh++;
                    }
                }
            }
        };
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_meter_of_sound);
        this.toolbar = (Toolbar) findViewById(R.id.toolBar);
        setSupportActionBar(this.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        this.context = this;
        this.f57db = new DatabaseHelper(this);
        World.minDB = 200.0f;
        World.maxDB = 0.0f;
        this.calibrationDialog = new BottomSheetDialog(this);
        this.thresholdDialog = new BottomSheetDialog(this);
        this.examineDialog = new Dialog(this);
        this.textViewMinValue = (TextView) findViewById(R.id.textViewMinValue);
        this.txt = (TextView) findViewById(R.id.txt);
        this.textViewAvgValue = (TextView) findViewById(R.id.textViewAvgValue);
        this.textViewMaxValue = (TextView) findViewById(R.id.textViewMaxValue);
        this.guessSurroundingTextView = (TextView) findViewById(R.id.guessSurroundingTextView);
        this.calibrateImageView = (ImageView) findViewById(R.id.imageViewCalibrate);
        this.refreshButton = (LinearLayout) findViewById(R.id.refresh);
        this.refreshImageView = (ImageView) findViewById(R.id.imageViewRefresh);
        this.recyclerViewHistoryList = (RecyclerView) findViewById(R.id.recyclerViewHistoryList);
        this.refreshButton.setOnClickListener(new OnClickListener() {
            @SuppressLint({"ResourceAsColor"})
            public void onClick(View view) {
                ActivitySoundAudioMeter activitySoundAudioMeter = ActivitySoundAudioMeter.this;
                activitySoundAudioMeter.refreshed = true;
                activitySoundAudioMeter.refreshRecorder();
                ActivitySoundAudioMeter.this.currentlyActiveImageView.setColorFilter(ContextCompat.getColor(ActivitySoundAudioMeter.this, R.color.playtxtcolor));
                ActivitySoundAudioMeter.this.currentlyActiveImageView.getLayoutParams().width = (int) ActivitySoundAudioMeter.this.getResources().getDimension(R.dimen.imageViewNotActiveWidth);
                ActivitySoundAudioMeter.this.refreshImageView.setColorFilter(ContextCompat.getColor(ActivitySoundAudioMeter.this, R.color.colorNotActive));
                ActivitySoundAudioMeter.this.refreshImageView.getLayoutParams().width = (int) ActivitySoundAudioMeter.this.getResources().getDimension(R.dimen.imageViewActiveWidth);
                activitySoundAudioMeter = ActivitySoundAudioMeter.this;
                activitySoundAudioMeter.currentlyActiveImageView = activitySoundAudioMeter.refreshImageView;
                activitySoundAudioMeter = ActivitySoundAudioMeter.this;
            }
        });
        this.playPauseButton = (LinearLayout) findViewById(R.id.playPause);
        this.playPauseTextView = (TextView) findViewById(R.id.textViewPlayPause);
        this.playPauseImageView = (ImageView) findViewById(R.id.imageViewPlayPause);
        this.currentlyActiveImageView = this.playPauseImageView;
        this.currentlyActiveTextView = this.playPauseTextView;
        this.playPauseButton.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                ActivitySoundAudioMeter activitySoundAudioMeter;
                if (ActivitySoundAudioMeter.this.bListener) {
                    ActivitySoundAudioMeter.this.onPauseNew();
                    ActivitySoundAudioMeter.this.currentlyActiveImageView.setColorFilter(ContextCompat.getColor(ActivitySoundAudioMeter.this, R.color.colorNotActive));
                    ActivitySoundAudioMeter.this.currentlyActiveImageView.getLayoutParams().width = (int) ActivitySoundAudioMeter.this.getResources().getDimension(R.dimen.imageViewNotActiveWidth);
                    ActivitySoundAudioMeter.this.currentlyActiveTextView.setTextColor(ContextCompat.getColor(ActivitySoundAudioMeter.this, R.color.colorNotActive));
                    ActivitySoundAudioMeter.this.currentlyActiveTextView.setTextSize(2, 18.0f);
                    ActivitySoundAudioMeter.this.playPauseImageView.setImageResource(R.drawable.pause_icon);
                    ActivitySoundAudioMeter.this.playPauseImageView.setColorFilter(ContextCompat.getColor(ActivitySoundAudioMeter.this, R.color.colorActive));
                    ActivitySoundAudioMeter.this.playPauseImageView.getLayoutParams().width = (int) ActivitySoundAudioMeter.this.getResources().getDimension(R.dimen.imageViewActiveWidth);
                    ActivitySoundAudioMeter.this.playPauseTextView.setText(R.string.play);
                    ActivitySoundAudioMeter.this.playPauseTextView.setTextColor(ContextCompat.getColor(ActivitySoundAudioMeter.this, R.color.playtxtcolor));
                    ActivitySoundAudioMeter.this.playPauseTextView.setTextSize(2, 18.0f);
                    activitySoundAudioMeter = ActivitySoundAudioMeter.this;
                    activitySoundAudioMeter.currentlyActiveImageView = activitySoundAudioMeter.playPauseImageView;
                    activitySoundAudioMeter = ActivitySoundAudioMeter.this;
                    activitySoundAudioMeter.currentlyActiveTextView = activitySoundAudioMeter.playPauseTextView;
                    return;
                }
                ActivitySoundAudioMeter.this.onResumeNew();
                ActivitySoundAudioMeter.this.currentlyActiveImageView.setColorFilter(ContextCompat.getColor(ActivitySoundAudioMeter.this, R.color.colorNotActive));
                ActivitySoundAudioMeter.this.currentlyActiveImageView.getLayoutParams().width = (int) ActivitySoundAudioMeter.this.getResources().getDimension(R.dimen.imageViewNotActiveWidth);
                ActivitySoundAudioMeter.this.currentlyActiveTextView.setTextColor(ContextCompat.getColor(ActivitySoundAudioMeter.this, R.color.playtxtcolor));
                ActivitySoundAudioMeter.this.currentlyActiveTextView.setTextSize(2, 18.0f);
                ActivitySoundAudioMeter.this.playPauseTextView.setText(R.string.pause);
                ActivitySoundAudioMeter.this.playPauseImageView.setImageResource(R.drawable.play_icon);
                activitySoundAudioMeter = ActivitySoundAudioMeter.this;
                activitySoundAudioMeter.currentlyActiveImageView = activitySoundAudioMeter.playPauseImageView;
                activitySoundAudioMeter = ActivitySoundAudioMeter.this;
                activitySoundAudioMeter.currentlyActiveTextView = activitySoundAudioMeter.playPauseTextView;
            }
        });
        this.thresholdButton = (LinearLayout) findViewById(R.id.threshold);
        this.thresholdImageView = (ImageView) findViewById(R.id.imageViewThreshold);
        this.historyButton = (LinearLayout) findViewById(R.id.history);
        this.historyImageView = (ImageView) findViewById(R.id.imageViewHistory);
        this.calibrateButton = (LinearLayout) findViewById(R.id.calibrate);
        this.calibrateButton.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                final Dialog dialog = new Dialog(ActivitySoundAudioMeter.this);
                dialog.setContentView(R.layout.dialog_calibration_by_user);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                ActivitySoundAudioMeter.this.refreshImageView.setColorFilter(ContextCompat.getColor(ActivitySoundAudioMeter.this, R.color.colorActive));
                ActivitySoundAudioMeter.this.calibrateImageView.setImageResource(R.drawable.calibrate_icon1);
                ActivitySoundAudioMeter.this.calibrateImageView.getLayoutParams().width = (int) ActivitySoundAudioMeter.this.getResources().getDimension(R.dimen.imageViewActiveWidth);
                ActivitySoundAudioMeter.this.calibrationValueTextView = (TextView) dialog.findViewById(R.id.calibrationValueTextView);
                ActivitySoundAudioMeter.this.calibration = World.calibration;
                if (ActivitySoundAudioMeter.this.calibration < 0) {
                    ActivitySoundAudioMeter.this.calibrationValueTextView.setText(String.format("(%.2f - %d)", new Object[]{Float.valueOf(World.dbCount), Integer.valueOf(Math.abs(ActivitySoundAudioMeter.this.calibration))}));
                } else {
                    ActivitySoundAudioMeter.this.calibrationValueTextView.setText(String.format("(%.2f + %d)", new Object[]{Float.valueOf(World.dbCount), Integer.valueOf(ActivitySoundAudioMeter.this.calibration)}));
                }
                ((LinearLayout) dialog.findViewById(R.id.cancelButton)).setOnClickListener(new OnClickListener() {
                    public void onClick(View view) {
                        dialog.dismiss();
                        ActivitySoundAudioMeter.this.calibrateImageView.setImageResource(R.drawable.calibrate_icon);
                        ActivitySoundAudioMeter.this.calibrateImageView.getLayoutParams().width = (int) ActivitySoundAudioMeter.this.getResources().getDimension(R.dimen.imageViewNotActiveWidth);
                    }
                });
                ((LinearLayout) dialog.findViewById(R.id.calibrateButton)).setOnClickListener(new OnClickListener() {
                    public void onClick(View view) {
                        dialog.dismiss();
                        World.calibration = ActivitySoundAudioMeter.this.calibration;
                        ActivitySoundAudioMeter.this.calibrateImageView.getLayoutParams().width = (int) ActivitySoundAudioMeter.this.getResources().getDimension(R.dimen.imageViewNotActiveWidth);
                        ActivitySoundAudioMeter.this.calibrateImageView.setImageResource(R.drawable.calibrate_icon);
                    }
                });
                ((LinearLayout) dialog.findViewById(R.id.addImageView)).setOnTouchListener(new RepeatListener(400, 100, new OnClickListener() {
                    public void onClick(View view) {
                        ActivitySoundAudioMeter activitySoundAudioMeter = ActivitySoundAudioMeter.this;
                        activitySoundAudioMeter.calibration++;
                        if (ActivitySoundAudioMeter.this.calibration < 0) {
                            ActivitySoundAudioMeter.this.calibrationValueTextView.setText(String.format("(%.2f - %d)", new Object[]{Float.valueOf(World.dbCount), Integer.valueOf(Math.abs(ActivitySoundAudioMeter.this.calibration))}));
                            return;
                        }
                        ActivitySoundAudioMeter.this.calibrationValueTextView.setText(String.format("(%.2f + %d)", new Object[]{Float.valueOf(World.dbCount), Integer.valueOf(ActivitySoundAudioMeter.this.calibration)}));
                    }
                }));
                ((LinearLayout) dialog.findViewById(R.id.subtractImageView)).setOnTouchListener(new RepeatListener(400, 100, new OnClickListener() {
                    public void onClick(View view) {
                        ActivitySoundAudioMeter activitySoundAudioMeter = ActivitySoundAudioMeter.this;
                        activitySoundAudioMeter.calibration--;
                        if (ActivitySoundAudioMeter.this.calibration < 0) {
                            ActivitySoundAudioMeter.this.calibrationValueTextView.setText(String.format("(%.2f - %d)", new Object[]{Float.valueOf(World.dbCount), Integer.valueOf(Math.abs(ActivitySoundAudioMeter.this.calibration))}));
                            return;
                        }
                        ActivitySoundAudioMeter.this.calibrationValueTextView.setText(String.format("(%.2f + %d)", new Object[]{Float.valueOf(World.dbCount), Integer.valueOf(ActivitySoundAudioMeter.this.calibration)}));
                    }
                }));
                dialog.setOnDismissListener(new OnDismissListener() {
                    public void onDismiss(DialogInterface dialogInterface) {
                        ActivitySoundAudioMeter.this.calibrateImageView.setImageResource(R.drawable.calibrate_icon);
                        ActivitySoundAudioMeter.this.calibrateImageView.getLayoutParams().width = (int) ActivitySoundAudioMeter.this.getResources().getDimension(R.dimen.imageViewNotActiveWidth);
                    }
                });
                dialog.show();
            }
        });
        this.thresholdButton.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                TextView textView;
                StringBuilder stringBuilder;
                final Dialog dialog = new Dialog(ActivitySoundAudioMeter.this);
                dialog.setContentView(R.layout.dialog_thres_hold);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                if (ActivitySoundAudioMeter.this.bListener) {
                    ActivitySoundAudioMeter.this.thresholdImageView.setColorFilter(ContextCompat.getColor(ActivitySoundAudioMeter.this.context, R.color.colorActive));
                }
                ActivitySoundAudioMeter.this.refreshImageView.setColorFilter(ContextCompat.getColor(ActivitySoundAudioMeter.this, R.color.colorActive));
                ActivitySoundAudioMeter.this.thresholdImageView.setColorFilter(ContextCompat.getColor(ActivitySoundAudioMeter.this.context, R.color.colorNotActive));
                ActivitySoundAudioMeter.this.notificationThresholdSwitch = (RMSwitch) dialog.findViewById(R.id.enableThreshold);
                ActivitySoundAudioMeter.this.thresholdValueTextView = (TextView) dialog.findViewById(R.id.thresholdValueTextView);
                ActivitySoundAudioMeter.this.notificationSoundSwitch = (RMSwitch) dialog.findViewById(R.id.sound);
                ActivitySoundAudioMeter.this.notificationVibrateSwitch = (RMSwitch) dialog.findViewById(R.id.vibrate);
                ActivitySoundAudioMeter.this.threshold_icon = (ImageView) dialog.findViewById(R.id.threshold_icon);
                if (ActivitySoundAudioMeter.this.thresholdEnabled.booleanValue()) {
                    ActivitySoundAudioMeter.this.notificationThresholdSwitch.setChecked(true);
                    ActivitySoundAudioMeter activitySoundAudioMeter = ActivitySoundAudioMeter.this;
                    activitySoundAudioMeter.thresholdTemp = activitySoundAudioMeter.threshold;
                    textView = ActivitySoundAudioMeter.this.thresholdValueTextView;
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("");
                    stringBuilder.append(ActivitySoundAudioMeter.this.thresholdTemp);
                    textView.setText(stringBuilder.toString());
                }
                if (!ActivitySoundAudioMeter.this.notificationThresholdSwitch.isChecked()) {
                    ActivitySoundAudioMeter.this.notificationSoundSwitch.setClickable(false);
                    ActivitySoundAudioMeter.this.notificationVibrateSwitch.setClickable(false);
                }
                if (ActivitySoundAudioMeter.this.notificationThresholdSwitch.isChecked()) {
                    ActivitySoundAudioMeter.this.notificationSoundSwitch.setChecked(ActivitySoundAudioMeter.this.notificationSoundEnabled.booleanValue());
                    ActivitySoundAudioMeter.this.notificationVibrateSwitch.setChecked(ActivitySoundAudioMeter.this.notificationVibrationEnabled.booleanValue());
                }
                ActivitySoundAudioMeter.this.notificationThresholdSwitch.addSwitchObserver(new RMSwitchObserver() {
                    public void onCheckStateChange(RMSwitch rMSwitch, boolean z) {
                        ActivitySoundAudioMeter activitySoundAudioMeter;
                        TextView textView;
                        StringBuilder stringBuilder;
                        if (z) {
                            ActivitySoundAudioMeter.this.thresholdEnabled = Boolean.valueOf(true);
                            activitySoundAudioMeter = ActivitySoundAudioMeter.this;
                            activitySoundAudioMeter.thresholdTemp = activitySoundAudioMeter.threshold;
                            textView = ActivitySoundAudioMeter.this.thresholdValueTextView;
                            stringBuilder = new StringBuilder();
                            stringBuilder.append("");
                            stringBuilder.append(ActivitySoundAudioMeter.this.thresholdTemp);
                            textView.setText(stringBuilder.toString());
                            ActivitySoundAudioMeter.this.notificationSoundSwitch.setClickable(true);
                            ActivitySoundAudioMeter.this.notificationVibrateSwitch.setClickable(true);
                            ActivitySoundAudioMeter.this.notificationSoundSwitch.setChecked(ActivitySoundAudioMeter.this.notificationSoundEnabled.booleanValue());
                            ActivitySoundAudioMeter.this.notificationVibrateSwitch.setChecked(ActivitySoundAudioMeter.this.notificationVibrationEnabled.booleanValue());
                            return;
                        }
                        ActivitySoundAudioMeter.this.thresholdEnabled = Boolean.valueOf(false);
                        activitySoundAudioMeter = ActivitySoundAudioMeter.this;
                        activitySoundAudioMeter.thresholdTemp = 0;
                        textView = activitySoundAudioMeter.thresholdValueTextView;
                        stringBuilder = new StringBuilder();
                        stringBuilder.append("");
                        stringBuilder.append(ActivitySoundAudioMeter.this.thresholdTemp);
                        textView.setText(stringBuilder.toString());
                        ActivitySoundAudioMeter.this.notificationSoundSwitch.setClickable(false);
                        ActivitySoundAudioMeter.this.notificationVibrateSwitch.setClickable(false);
                        ActivitySoundAudioMeter.this.notificationSoundSwitch.setChecked(false);
                        ActivitySoundAudioMeter.this.notificationVibrateSwitch.setChecked(false);
                    }
                });
                ActivitySoundAudioMeter.this.notificationSoundSwitch.addSwitchObserver(new RMSwitchObserver() {
                    public void onCheckStateChange(RMSwitch rMSwitch, boolean z) {
                        if (z) {
                            ActivitySoundAudioMeter.this.thresholdEnabled = Boolean.valueOf(true);
                            ActivitySoundAudioMeter.this.notificationSoundEnabled = Boolean.valueOf(true);
                            return;
                        }
                        ActivitySoundAudioMeter.this.notificationSoundEnabled = Boolean.valueOf(false);
                    }
                });
                ActivitySoundAudioMeter.this.notificationVibrateSwitch.addSwitchObserver(new RMSwitchObserver() {
                    public void onCheckStateChange(RMSwitch rMSwitch, boolean z) {
                        if (z) {
                            ActivitySoundAudioMeter.this.thresholdEnabled = Boolean.valueOf(true);
                            ActivitySoundAudioMeter.this.notificationVibrationEnabled = Boolean.valueOf(true);
                            return;
                        }
                        ActivitySoundAudioMeter.this.notificationVibrationEnabled = Boolean.valueOf(false);
                    }
                });
                textView = ActivitySoundAudioMeter.this.thresholdValueTextView;
                stringBuilder = new StringBuilder();
                stringBuilder.append("");
                stringBuilder.append(ActivitySoundAudioMeter.this.thresholdTemp);
                textView.setText(stringBuilder.toString());
                LinearLayout linearLayout = (LinearLayout) dialog.findViewById(R.id.setButton);
                ((LinearLayout) dialog.findViewById(R.id.cancelButton)).setOnClickListener(new OnClickListener() {
                    public void onClick(View view) {
                        dialog.dismiss();
                        ActivitySoundAudioMeter.this.thresholdImageView.setColorFilter(ContextCompat.getColor(ActivitySoundAudioMeter.this.context, R.color.colorActive));
                        ActivitySoundAudioMeter.this.thresholdImageView.getLayoutParams().width = (int) ActivitySoundAudioMeter.this.getResources().getDimension(R.dimen.imageViewNotActiveWidth);
                    }
                });
                linearLayout.setOnClickListener(new OnClickListener() {
                    public void onClick(View view) {
                        dialog.dismiss();
                        ActivitySoundAudioMeter.this.thresholdImageView.setColorFilter(ContextCompat.getColor(ActivitySoundAudioMeter.this.context, R.color.colorActive));
                        if (ActivitySoundAudioMeter.this.threshold != ActivitySoundAudioMeter.this.thresholdTemp) {
                            ActivitySoundAudioMeter.this.noOfTimesThresholdCrossed = 0;
                        }
                        ActivitySoundAudioMeter activitySoundAudioMeter = ActivitySoundAudioMeter.this;
                        activitySoundAudioMeter.threshold = activitySoundAudioMeter.thresholdTemp;
                        ActivitySoundAudioMeter.this.thresholdImageView.getLayoutParams().width = (int) ActivitySoundAudioMeter.this.getResources().getDimension(R.dimen.imageViewNotActiveWidth);
                    }
                });
                ((LinearLayout) dialog.findViewById(R.id.addImageView)).setOnTouchListener(new RepeatListener(400, 100, new OnClickListener() {
                    public void onClick(View view) {
                        if (ActivitySoundAudioMeter.this.notificationThresholdSwitch.isChecked()) {
                            ActivitySoundAudioMeter activitySoundAudioMeter = ActivitySoundAudioMeter.this;
                            activitySoundAudioMeter.thresholdTemp++;
                            TextView textView = ActivitySoundAudioMeter.this.thresholdValueTextView;
                            StringBuilder stringBuilder = new StringBuilder();
                            stringBuilder.append("");
                            stringBuilder.append(ActivitySoundAudioMeter.this.thresholdTemp);
                            textView.setText(stringBuilder.toString());
                        }
                    }
                }));
                ((LinearLayout) dialog.findViewById(R.id.subtractImageView)).setOnTouchListener(new RepeatListener(400, 100, new OnClickListener() {
                    public void onClick(View view) {
                        if (ActivitySoundAudioMeter.this.notificationThresholdSwitch.isChecked()) {
                            ActivitySoundAudioMeter activitySoundAudioMeter = ActivitySoundAudioMeter.this;
                            activitySoundAudioMeter.thresholdTemp--;
                            TextView textView = ActivitySoundAudioMeter.this.thresholdValueTextView;
                            StringBuilder stringBuilder = new StringBuilder();
                            stringBuilder.append("");
                            stringBuilder.append(ActivitySoundAudioMeter.this.thresholdTemp);
                            textView.setText(stringBuilder.toString());
                        }
                    }
                }));
                dialog.setOnDismissListener(new OnDismissListener() {
                    public void onDismiss(DialogInterface dialogInterface) {
                        ActivitySoundAudioMeter.this.thresholdImageView.setColorFilter(ContextCompat.getColor(ActivitySoundAudioMeter.this.context, R.color.colorActive));
                        ActivitySoundAudioMeter.this.thresholdImageView.getLayoutParams().width = (int) ActivitySoundAudioMeter.this.getResources().getDimension(R.dimen.imageViewNotActiveWidth);
                    }
                });
                dialog.show();
            }
        });
        this.historyButton.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                ActivitySoundAudioMeter.this.currentlyActiveImageView.setColorFilter(ContextCompat.getColor(ActivitySoundAudioMeter.this, R.color.playtxtcolor));
                ActivitySoundAudioMeter.this.currentlyActiveImageView.getLayoutParams().width = (int) ActivitySoundAudioMeter.this.getResources().getDimension(R.dimen.imageViewNotActiveWidth);
                ActivitySoundAudioMeter.this.historyImageView.setColorFilter(ContextCompat.getColor(ActivitySoundAudioMeter.this, R.color.colorNotActive));
                ActivitySoundAudioMeter.this.historyImageView.getLayoutParams().width = (int) ActivitySoundAudioMeter.this.getResources().getDimension(R.dimen.imageViewActiveWidth);
                ActivitySoundAudioMeter activitySoundAudioMeter = ActivitySoundAudioMeter.this;
                activitySoundAudioMeter.currentlyActiveImageView = activitySoundAudioMeter.historyImageView;
                ActivitySoundAudioMeter.this.graphView.stopPlotting();
                if (ActivitySoundAudioMeter.this.mRecorder.isRecording || ActivitySoundAudioMeter.this.mRecorder != null) {
                    ActivitySoundAudioMeter.this.mRecorder.stopRecording();
                    activitySoundAudioMeter = ActivitySoundAudioMeter.this;
                    activitySoundAudioMeter.bListener = false;
                    activitySoundAudioMeter.ismRecordingThreadRun = false;
                    activitySoundAudioMeter.mRecordingThread = null;
                }
                activitySoundAudioMeter = ActivitySoundAudioMeter.this;
                activitySoundAudioMeter.startActivity(new Intent(activitySoundAudioMeter, ActivityAudioHistory.class));
                ActivitySoundAudioMeter.this.finish();
            }
        });
        this.saveAudioImageView = (ImageView) findViewById(R.id.saveAudioImageView);
        this.saveAudioImageView.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                ActivitySoundAudioMeter activitySoundAudioMeter;
                ActivitySoundAudioMeter.this.saveAudioImageView.setColorFilter(ContextCompat.getColor(ActivitySoundAudioMeter.this, R.color.playtxtcolor));
                if (view.getScaleX() == 1.0f) {
                    view.setScaleX(1.12f);
                    view.setScaleY(1.12f);
                }
                ActivitySoundAudioMeter.this.graphView.stopPlotting();
                boolean z = ActivitySoundAudioMeter.this.bListener;
                String str = ActivitySoundAudioMeter.TAG;
                if (!z) {
                    ActivitySoundAudioMeter.this.onResumeNew();
                    Log.d(str, "onClick: resumed recording to stop and save file!");
                }
                boolean z2 = false;
                if (ActivitySoundAudioMeter.this.mRecorder.isRecording || ActivitySoundAudioMeter.this.mRecorder != null) {
                    ActivitySoundAudioMeter.this.mRecorder.stopRecording();
                    activitySoundAudioMeter = ActivitySoundAudioMeter.this;
                    activitySoundAudioMeter.bListener = false;
                    activitySoundAudioMeter.ismRecordingThreadRun = false;
                    activitySoundAudioMeter.mRecordingThread = null;
                }
                File file = new File(ActivitySoundAudioMeter.this.getApplicationContext().getFilesDir().getPath());
                File file2 = new File(file, "temp.amr");
                File file3 = new File(file, ActivitySoundAudioMeter.this.fileNameToSave);
                if (file3.exists()) {
                    Log.d(str, "onClick: cannot rename as file with same name already exists");
                } else {
                    boolean renameTo = file2.renameTo(file3);
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("onClick: dirName: ");
                    stringBuilder.append(file.getName());
                    Log.d(str, stringBuilder.toString());
                    StringBuilder stringBuilder2 = new StringBuilder();
                    stringBuilder2.append("onClick: old file name: ");
                    stringBuilder2.append(file2.getName());
                    Log.d(str, stringBuilder2.toString());
                    stringBuilder2 = new StringBuilder();
                    stringBuilder2.append("onClick: new file name: ");
                    stringBuilder2.append(file3.getName());
                    Log.d(str, stringBuilder2.toString());
                    stringBuilder2 = new StringBuilder();
                    stringBuilder2.append("onClick: isRenamed: ");
                    stringBuilder2.append(renameTo);
                    Log.d(str, stringBuilder2.toString());
                    if (renameTo) {
                        Log.d(str, "onClick: file successfully renamed and saved");
                        String str2 = file3.getName().split(".amr")[0];
                        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
                        mediaMetadataRetriever.setDataSource(ActivitySoundAudioMeter.this, Uri.fromFile(file3));
                        String extractMetadata = mediaMetadataRetriever.extractMetadata(5);
                        String extractMetadata2 = mediaMetadataRetriever.extractMetadata(9);
                        stringBuilder2 = new StringBuilder();
                        stringBuilder2.append("onClick: date: ");
                        stringBuilder2.append(extractMetadata);
                        Log.d(str, stringBuilder2.toString());
                        int i = 11;
                        try {
                            int i2 = 12 - (((int) World.dbCount) / 10);
                            if (i2 < 0) {
                                i = 0;
                            } else if (i2 < 12) {
                                i = i2;
                            }
                        } catch (ArrayIndexOutOfBoundsException unused) {
                        }
                        DecimalFormat decimalFormat = new DecimalFormat("#.#####");
                        DatabaseHelper databaseHelper = ActivitySoundAudioMeter.this.f57db;
                        float f = 0.0f;
                        String valueOf = String.valueOf(World.minDB < 0.0f ? 0.0f : World.minDB);
                        String valueOf2 = String.valueOf(World.avgDB < 0.0f ? Integer.valueOf(0) : decimalFormat.format((double) World.avgDB));
                        if (World.maxDB >= 0.0f) {
                            f = World.maxDB;
                        }
                        z2 = databaseHelper.insertData(str2, extractMetadata, extractMetadata2, valueOf, valueOf2, String.valueOf(f), String.valueOf(i));
                        if (z2) {
                            Log.d(str, "onClick: Successfully inserted data into database");
                        } else {
                            Log.d(str, "onClick: Cannot insert data into database!");
                        }
                    } else {
                        Log.d(str, "File not renamed!");
                    }
                }
                if (z2) {
                    activitySoundAudioMeter = ActivitySoundAudioMeter.this;
                    activitySoundAudioMeter.startActivity(new Intent(activitySoundAudioMeter, ActivityAudioHistory.class));
                    ActivitySoundAudioMeter.this.finish();
                }
            }
        });
        this.examineImageView = (ImageView) findViewById(R.id.imageViewExamine);
        this.graphView = (GraphView) findViewById(R.id.graphView);
        this.savedInstanceStateNew = bundle;
        if (checkRecordPermission()) {
            init();
        } else {
            requestPermissions();
        }
    }

    @SuppressLint({"WrongConstant"})
    public void sendNotification(String str, String str2, String str3, boolean z, boolean z2) {
        System.out.println("notification test 0");
        Notification notification = new Notification();
        NotificationManager notificationManager = (NotificationManager) getSystemService("notification");
        String str4 = "M_CH_ID";
        if (VERSION.SDK_INT >= 26) {
            NotificationChannel notificationChannel = new NotificationChannel(str4, "Working", 3);
            notificationChannel.setDescription("Working Description");
            notificationManager.createNotificationChannel(notificationChannel);
        }
        Builder builder = new Builder(this, str4);
        builder.setDefaults(notification.defaults);
        if (z) {
            builder.setSound(RingtoneManager.getDefaultUri(2));
        }
        if (z2) {
            try {
                ((Vibrator) getSystemService("vibrator")).vibrate(1000);
            } catch (Exception unused) {
                Log.e(TAG, "sendNotification: cannot set vibration");
            }
        }
        builder.setSmallIcon(R.drawable.icon).setAutoCancel(true).setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.icon)).setLights(SupportMenu.CATEGORY_MASK, 3000, 3000).setContentTitle(str).setContentText(str2).setAutoCancel(true).setTicker(str3).setNumber(this.noOfTimesThresholdCrossed);
        notificationManager.notify(0, builder.build());
        System.out.println("notification test 2");
    }

    private void init() {
        this.graphView.setGraphColor(ContextCompat.getColor(this, R.color.colorBlueMax));
        this.graphView.setCanvasColor(ContextCompat.getColor(this, R.color.colorBackgroundGraph));
        this.graphView.setMarkerColor(ContextCompat.getColor(this, R.color.colorBackgroundGraph));
        this.graphView.setNeedleColor(ContextCompat.getColor(this, R.color.colorBlueMax));
        this.graphView.setTimeColor(ContextCompat.getColor(this, R.color.colorBlueMid));
        this.mRecorder = new MyMediaRecorder();
        Bundle bundle = this.savedInstanceStateNew;
        if (bundle != null) {
            this.scale = bundle.getInt(SCALE);
            this.graphView.setWaveLengthPX(this.scale);
            if (!this.mRecorder.isRecording) {
                this.samples = this.mRecorder.getSamples();
                this.graphView.showFullGraph(this.samples);
            }
        }
        this.colorArcProgressBar = (ColorArcProgressBar) findViewById(R.id.bar1);
        this.colorArcProgressBar.vTextPaint = new Paint();
        this.colorArcProgressBar.vTextPaint.setColor(Color.parseColor("#000000"));
        this.fileNameToSave = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss'.amr'", Locale.US).format(new Date());
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("init: path : ");
        stringBuilder.append(getFilesDir());
        Log.d(TAG, stringBuilder.toString());
        startRecord(new File(getFilesDir(), "temp.amr"));
        onResumeNew();
    }

    @SuppressLint({"WrongConstant"})
    public void onRequestPermissionsResult(int i, String[] strArr, int[] iArr) {
        if (i == 0) {
            if (iArr.length <= 0 || iArr[0] != 0) {
                Toast.makeText(this, "Permission denied to read your External storage", 0).show();
            } else {
                init();
            }
        }
    }

    public void ShowExaminePopup(View view) {
        this.examineImageView.setColorFilter(ContextCompat.getColor(this, R.color.playtxtcolor));
        this.examineImageView.setScaleX(1.22f);
        this.examineImageView.setScaleY(1.22f);
        this.examineDialog.setContentView(R.layout.dialog_decibeling_scale_table);
        this.examineDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        this.examinePopupRanges = (LinearLayout) this.examineDialog.findViewById(R.id.ranges);
        this.examinePopupChildLinearLayout = (LinearLayout) this.examinePopupRanges.getChildAt(0);
        this.examinePopupArrow = (ImageView) this.examinePopupChildLinearLayout.getChildAt(0);
        this.examinePopupText = (TextView) this.examinePopupChildLinearLayout.getChildAt(1);
        ((TextView) this.examineDialog.findViewById(R.id.txtclose)).setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                ActivitySoundAudioMeter.this.examineDialog.dismiss();
                ActivitySoundAudioMeter.this.examineImageView.setColorFilter(ContextCompat.getColor(ActivitySoundAudioMeter.this, R.color.decibel));
            }
        });
        this.examineDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        this.examineDialog.setOnDismissListener(new OnDismissListener() {
            public void onDismiss(DialogInterface dialogInterface) {
                ActivitySoundAudioMeter.this.examineImageView.setColorFilter(ContextCompat.getColor(ActivitySoundAudioMeter.this, R.color.decibel));
                ActivitySoundAudioMeter.this.examineImageView.setScaleX(1.0f);
                ActivitySoundAudioMeter.this.examineImageView.setScaleY(1.0f);
            }
        });
        this.examineDialog.show();
    }

    private void startListenAudio() {
        this.mRecorder.pointList.clear();
        this.startTime = System.currentTimeMillis();
        this.mRecordingThread = new Thread(new Runnable() {
            public void run() {
                while (ActivitySoundAudioMeter.this.ismRecordingThreadRun) {
                    try {
                        if (ActivitySoundAudioMeter.this.bListener) {
                            ActivitySoundAudioMeter.this.volume = ActivitySoundAudioMeter.this.mRecorder.getMaxAmplitude();
                            if (ActivitySoundAudioMeter.this.volume > 0.0f && ActivitySoundAudioMeter.this.volume < 1000000.0f) {
                                World.setDbCount((((float) Math.log10((double) ActivitySoundAudioMeter.this.volume)) * 20.0f) + ((float) World.calibration));
                                ActivitySoundAudioMeter.this.mRecorder.pointList.add(new WaveSample(System.currentTimeMillis() - ActivitySoundAudioMeter.this.startTime, (int) ActivitySoundAudioMeter.this.volume));
                                Message message = new Message();
                                message.what = 1;
                                ActivitySoundAudioMeter.this.handler.sendMessage(message);
                            }
                        }
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        ActivitySoundAudioMeter.this.bListener = false;
                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }
                }
            }
        });
        this.mRecordingThread.start();
    }

    @SuppressLint({"WrongConstant"})
    public void startRecord(File file) {
        try {
            this.mRecorder.setMyRecAudioFile(file);
            if (this.mRecorder.startRecorder()) {
                startListenAudio();
                this.mRecorder.startPlotting(this.graphView);
                return;
            }
            Toast.makeText(this, getString(R.string.activity_recStartErr), 0).show();
        } catch (Exception e) {
            Toast.makeText(this, getString(R.string.activity_recBusyErr), 0).show();
            e.printStackTrace();
        }
    }

    public void refreshRecorder() {
        this.colorArcProgressBar.setCurrentValues(0.0f);
        World.minDB = 200.0f;
        World.dbCount = 0.0f;
        World.lastDbCount = 0.0f;
        World.maxDB = 0.0f;
        onPauseNew();
        this.mRecorder.reset();
        this.mRecorder.pointList.clear();
        this.fileNameToSave = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss'.amr'", Locale.US).format(new Date());
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("init: path : ");
        stringBuilder.append(getFilesDir());
        Log.d(TAG, stringBuilder.toString());
        this.mRecorder.setMyRecAudioFile(new File(getFilesDir(), "temp.amr"));
        this.mRecorder.startRecorder();
        onResumeNew();
    }

    public void onResumeNew() {
        this.mRecorder.isRecording = true;
        this.bListener = true;
        GraphView graphView = this.graphView;
        if (graphView != null) {
            graphView.startPlotting();
        }
    }

    public void onResume() {
        super.onResume();
    }

    public void onPause() {
        super.onPause();
    }

    public void onPauseNew() {
        this.bListener = false;
        MyMediaRecorder myMediaRecorder = this.mRecorder;
        if (myMediaRecorder != null) {
            myMediaRecorder.isRecording = false;
        }
        GraphView graphView = this.graphView;
        if (graphView != null) {
            graphView.stopPlotting();
            this.samples = this.mRecorder.stopRecording_list();
            this.graphView.showFullGraph(this.samples);
            this.isChart = false;
        }
    }

    public void onDestroy() {
        if (this.mRecordingThread != null) {
            this.ismRecordingThreadRun = false;
            this.mRecordingThread = null;
        }
        MyMediaRecorder myMediaRecorder = this.mRecorder;
        if (myMediaRecorder != null) {
            myMediaRecorder.delete();
        }
        super.onDestroy();
    }

    public void onBackPressed() {
        super.onBackPressed();
        this.graphView.stopPlotting();
        if (this.mRecorder.isRecording) {
            this.mRecorder.stopRecording();
        }
        MyMediaRecorder myMediaRecorder = this.mRecorder;
        if (myMediaRecorder != null) {
            myMediaRecorder.stopRecording();
        }
        finish();
    }

    public void onSaveInstanceState(Bundle bundle) {
        bundle.putInt(SCALE, this.scale);
        super.onSaveInstanceState(bundle);
    }

    public void requestPermissions() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, "android.permission.RECORD_AUDIO")) {
            ActivityCompat.requestPermissions(this, new String[]{"android.permission.RECORD_AUDIO"}, 0);
            return;
        }
        ActivityCompat.requestPermissions(this, new String[]{"android.permission.RECORD_AUDIO"}, 0);
    }

    private boolean checkRecordPermission() {
        return ContextCompat.checkSelfPermission(this, "android.permission.RECORD_AUDIO") == 0;
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        Intent intent;
        StringBuilder stringBuilder;
        Toast makeText;
        switch (menuItem.getItemId()) {
            case 16908332:
                finish();
                return true;

            case R.id.contact /*2131361913*/:
                Intent more = new Intent(
                        "android.intent.action.VIEW",
                        Uri.parse("https://play.google.com/store/apps/developer?id="+getString(R.string.moreads)));
                try {
                    startActivity(more);

                } catch (ActivityNotFoundException e) {
                    Toast.makeText(
                            ActivitySoundAudioMeter.this,
                            "you_don_t_have_google_play_installed_or_internet_connection", Toast.LENGTH_LONG).show();
                }
                return true;

            case R.id.share /*2131362156*/:
                if (isOnline()) {
                    intent = new Intent("android.intent.action.SEND");
                    intent.setType("text/plain");
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("Hi! I'm using a Sound Meter Application. Check it out: https://play.google.com/store/apps/details?id=");
                    stringBuilder.append(getApplicationContext().getPackageName());
                    intent.putExtra("android.intent.extra.TEXT", stringBuilder.toString());
                    intent.addFlags(67108864);
                    startActivity(Intent.createChooser(intent, "Share with Friends"));
                } else {
                    makeText = Toast.makeText(getApplicationContext(), "No Internet Connection..", 0);
                    makeText.setGravity(17, 0, 0);
                    makeText.show();
                }
                return true;
            default:
                return super.onOptionsItemSelected(menuItem);
        }
    }

    public boolean isOnline() {
        NetworkInfo activeNetworkInfo = ((ConnectivityManager) getSystemService("connectivity")).getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }
}
