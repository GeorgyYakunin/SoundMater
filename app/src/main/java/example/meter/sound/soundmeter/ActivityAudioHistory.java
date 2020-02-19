package example.meter.sound.soundmeter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Environment;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;


import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import example.meter.sound.R;

public class ActivityAudioHistory extends AppCompatActivity {
    public static final String MEDIA_PATH;
    public static final String TAG = "ActivityAudioHistory";
    public static ImageView imageViewIncludeViewAudioProgressBar;
    public static ColorArcProgressBar1 mColorArcProgressBar;
    public static MediaPlayerHolder mPlayerAdapter;

    static {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(Environment.getExternalStorageDirectory());
        stringBuilder.append(File.separator);
        stringBuilder.append("Decibel - Saved Recordings");
        MEDIA_PATH = stringBuilder.toString();
    }

    public DatabaseHelper f56db;
    public ArrayList<AudioDetails> mAudioDetailsArrayList = new ArrayList();
    public HistoryAdapter mRecyclerViewAdapter;
    public boolean mUserIsSeeking = false;
    public int order = 1;
    public RecyclerView recyclerViewHistoryList;
    Context context;
    SharedPreferences spref;
    Toolbar toolbar;

    public static String formatMediaDate(String str) {
        String str2 = "UTC";
        SimpleDateFormat simpleDateFormat;
        try {
            simpleDateFormat = new SimpleDateFormat("yyyyMMdd'T'HHmmss", Locale.getDefault());
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone(str2));
            str = new SimpleDateFormat("MM/dd/yy h:mm a", Locale.getDefault()).format(simpleDateFormat.parse(str));
            return str;
        } catch (Exception e) {
            String str3 = "error parsing date: ";
            String str4 = TAG;
            Log.w(str4, str3, e);
            try {
                simpleDateFormat = new SimpleDateFormat("yyyy MM dd", Locale.getDefault());
                simpleDateFormat.setTimeZone(TimeZone.getTimeZone(str2));
                return new SimpleDateFormat("MM/dd/yy", Locale.getDefault()).format(simpleDateFormat.parse(str));
            } catch (Exception e2) {
                Log.e(str4, str3, e2);
                return "";
            }
        }
    }



    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_audio_history);
        this.toolbar = (Toolbar) findViewById(R.id.toolBar);
        setSupportActionBar(this.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        this.context = this;

        this.f56db = new DatabaseHelper(this);
        initPlaybackController();
        initUI();
        Log.d(TAG, "onCreate: finished");
    }

    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: create MediaPlayer");
    }

    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: called.");
        mPlayerAdapter.pause();
    }

    public void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart: called.");
    }

    public void onStop() {
        super.onStop();
        String str = TAG;
        Log.d(str, "onStop: called.");
        if (isChangingConfigurations() && mPlayerAdapter.isPlaying()) {
            Log.d(str, "onStop: don't release MediaPlayer as screen is rotating & playing");
        }
        Log.d(str, "onStop: do not release MediaPlayer on home button press so that it can be resumed when app is launched again from recent apps");
    }

    public void onDestroy() {
        super.onDestroy();
        if (mPlayerAdapter != null) {
            String str = TAG;
            Log.d(str, "onDestroy: called.");
            Log.d(str, "onDestroy: release MediaPlayer");
            mPlayerAdapter.release();
        }
    }

    public ArrayList<AudioDetails> getAudioDetailsSortedByColumn(String str, int i) {
        ArrayList arrayList = new ArrayList();
        Cursor allDataOrderByColumn = this.f56db.getAllDataOrderByColumn(str, i);
        int count = allDataOrderByColumn.getCount();
        String str2 = TAG;
        if (count == 0) {
            Log.d(str2, "getAudioDetailsSortedByDate: No records to display");
            return arrayList;
        }
        while (allDataOrderByColumn.moveToNext()) {
            long parseInt = (long) Integer.parseInt(allDataOrderByColumn.getString(3));
            String replaceAll = String.format("%02d:%02d:%02d", new Object[]{Long.valueOf(TimeUnit.MILLISECONDS.toHours(parseInt)), Long.valueOf(TimeUnit.MILLISECONDS.toMinutes(parseInt) % TimeUnit.HOURS.toMinutes(1)), Long.valueOf(TimeUnit.MILLISECONDS.toSeconds(parseInt) % TimeUnit.MINUTES.toSeconds(1))}).replaceAll("^00:", "");
            String str3 = "%.1f";
            AudioDetails audioDetails = new AudioDetails(allDataOrderByColumn.getString(0), allDataOrderByColumn.getString(1), formatMediaDate(allDataOrderByColumn.getString(2)), replaceAll, String.format(str3, new Object[]{Float.valueOf(allDataOrderByColumn.getFloat(4))}), String.format(str3, new Object[]{Float.valueOf(allDataOrderByColumn.getFloat(5))}), String.format(str3, new Object[]{Float.valueOf(allDataOrderByColumn.getFloat(6))}), allDataOrderByColumn.getInt(7));
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("getAudioDetailsSortedByDate: name : ");
            stringBuilder.append(audioDetails.getName());
            Log.d(str2, stringBuilder.toString());
            stringBuilder = new StringBuilder();
            stringBuilder.append("getAudioDetailsSortedByDate: creationDate : ");
            stringBuilder.append(audioDetails.getCreationDate());
            Log.d(str2, stringBuilder.toString());
            stringBuilder = new StringBuilder();
            stringBuilder.append("getAudioDetailsSortedByDate: duration : ");
            stringBuilder.append(audioDetails.getDuration());
            Log.d(str2, stringBuilder.toString());
            arrayList.add(audioDetails);
        }
        return arrayList;
    }

    private void initUI() {
        Log.d(TAG, "initUI: initialize RecyclerView");
        this.recyclerViewHistoryList = (RecyclerView) findViewById(R.id.recyclerViewHistoryList);
        DatabaseHelper databaseHelper = this.f56db;
        this.mAudioDetailsArrayList = getAudioDetailsSortedByColumn(DatabaseHelper.COL_3, -1);
        this.mRecyclerViewAdapter = new HistoryAdapter(this, this.f56db, this.mAudioDetailsArrayList);
        this.recyclerViewHistoryList.setAdapter(this.mRecyclerViewAdapter);
        this.recyclerViewHistoryList.setLayoutManager(new LinearLayoutManager(this));
    }

    private void initPlaybackController() {
        MediaPlayerHolder mediaPlayerHolder = new MediaPlayerHolder(this);
        String str = TAG;
        Log.d(str, "initPlaybackController: created MediaPlayerHolder");
        mediaPlayerHolder.setPlaybackInfoListener(new PlaybackListener());
        mPlayerAdapter = mediaPlayerHolder;
        Log.d(str, "initPlaybackController: MediaPlayerHolder progress callback set");
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_delete, menu);
        return true;
    }

    @RequiresApi(api = 21)
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int itemId = menuItem.getItemId();
        if (itemId == 16908332) {

            startActivity(new Intent(this, ActivitySoundAudioMeter.class));
            finish();

            return true;
        } else if (itemId != R.id.iocn_delete) {
            return super.onOptionsItemSelected(menuItem);
        } else {
            try {
                mPlayerAdapter.pause();
                new AlertDialog.Builder(this).setView(R.layout.dialog_delete_all_recordings_from_history).setPositiveButton(R.string.yes, new OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Cursor allData = ActivityAudioHistory.this.f56db.getAllData();
                        int count = allData.getCount();
                        String str = ActivityAudioHistory.TAG;
                        if (count == 0) {
                            Log.d(str, "getAudioDetailsSortedByDate: No records to display");
                            dialogInterface.dismiss();
                            return;
                        }
                        while (allData.moveToNext()) {
                            String string = allData.getString(0);
                            String string2 = allData.getString(1);
                            File filesDir = ActivityAudioHistory.this.getFilesDir();
                            StringBuilder stringBuilder = new StringBuilder();
                            stringBuilder.append(string2);
                            stringBuilder.append(".amr");
                            boolean delete = new File(filesDir, stringBuilder.toString()).delete();
                            String str2 = "onClick: imageViewDeleteAllRecord :  Failed to delete record with id = ";
                            StringBuilder stringBuilder2;
                            if (!delete) {
                                stringBuilder2 = new StringBuilder();
                                stringBuilder2.append(str2);
                                stringBuilder2.append(string);
                                Log.e(str, stringBuilder2.toString());
                            } else if (ActivityAudioHistory.this.f56db.deleteData(string) == 0) {
                                stringBuilder2 = new StringBuilder();
                                stringBuilder2.append(str2);
                                stringBuilder2.append(string);
                                stringBuilder2.append(" from Database");
                                Log.e(str, stringBuilder2.toString());
                            }
                        }
                        ActivityAudioHistory.this.mRecyclerViewAdapter.clear();
                    }
                }).setNegativeButton(R.string.no, (OnClickListener) null).setOnDismissListener(new OnDismissListener() {
                    public void onDismiss(DialogInterface dialogInterface) {
                    }
                }).create().show();
            } catch (Exception unused) {
                Log.e(TAG, "Something went wrong while deleting!");
            }
            return true;
        }
    }

    public void onBackPressed() {
        super.onBackPressed();

        startActivity(new Intent(this, ActivitySoundAudioMeter.class));
        finish();

    }

    public class PlaybackListener extends PlaybackInfoListener {
        public void onDurationChanged(int i) {
            ActivityAudioHistory.mColorArcProgressBar.setMaxValues((float) i);
            long j = (long) i;
            String.format("%02d:%02d:%02d", new Object[]{Long.valueOf(TimeUnit.MILLISECONDS.toHours(j)), Long.valueOf(TimeUnit.MILLISECONDS.toMinutes(j) % TimeUnit.HOURS.toMinutes(1)), Long.valueOf(TimeUnit.MILLISECONDS.toSeconds(j) % TimeUnit.MINUTES.toSeconds(1))}).replaceAll("^00:", "");
            Log.d(ActivityAudioHistory.TAG, String.format("setPlaybackDuration: setMaxValues(%d)", new Object[]{Integer.valueOf(i)}));
        }

        public void onPositionChanged(int i) {
            if (!ActivityAudioHistory.this.mUserIsSeeking) {
                ActivityAudioHistory.mColorArcProgressBar.setCurrentValues((float) i);
                long j = (long) i;
                String.format("%02d:%02d:%02d", new Object[]{Long.valueOf(TimeUnit.MILLISECONDS.toHours(j)), Long.valueOf(TimeUnit.MILLISECONDS.toMinutes(j) % TimeUnit.HOURS.toMinutes(1)), Long.valueOf(TimeUnit.MILLISECONDS.toSeconds(j) % TimeUnit.MINUTES.toSeconds(1))}).replaceAll("^00:", "");
            }
        }

        public void onStateChanged(int i) {
            String convertStateToString = PlaybackInfoListener.convertStateToString(i);
            if (i == 0) {
                ActivityAudioHistory.imageViewIncludeViewAudioProgressBar.setImageResource(R.drawable.play_icon);
            } else if (i == 1) {
                ActivityAudioHistory.imageViewIncludeViewAudioProgressBar.setImageResource(R.drawable.pause_icon);
            } else if (i == 2) {
                ActivityAudioHistory.mColorArcProgressBar.setCurrentValues(0.0f);
                ActivityAudioHistory.imageViewIncludeViewAudioProgressBar.setImageResource(R.drawable.pause_icon);
            }
            onLogUpdated(String.format("onStateChanged(%s)", new Object[]{convertStateToString}));
        }

        public void onPlaybackCompleted() {
            ActivityAudioHistory.mColorArcProgressBar.setCurrentValues(0.0f);
            ActivityAudioHistory.imageViewIncludeViewAudioProgressBar.setImageResource(R.drawable.pause_icon);
        }
    }
}
