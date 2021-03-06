package example.meter.sound;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.Toast;


import com.kaopiz.kprogresshud.KProgressHUD;

import example.meter.sound.soundmeter.ActivitySoundAudioMeter;
import example.meter.sound.tonegenerator.ActivityGeneratorOfWaveTone;

public class ActivityMain extends AppCompatActivity {
    int ad_code;
    int ads_const;
    LinearLayout btn_sound_info;
    LinearLayout btn_sound_meter;
    LinearLayout btn_tone_generator;
    Context context;
    SharedPreferences spref;
    Toolbar toolbar;
    KProgressHUD hud;



    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_main);
        if (VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.addFlags(Integer.MIN_VALUE);
            window.clearFlags(67108864);
            window.setStatusBarColor(getResources().getColor(R.color.status_color));
        }

        hud = KProgressHUD.create(ActivityMain.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Showing Ads...");


        this.toolbar = (Toolbar) findViewById(R.id.toolBar);
        setSupportActionBar(this.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        this.context = this;
        this.btn_sound_meter = (LinearLayout) findViewById(R.id.btn_sound_meter);
        this.btn_tone_generator = (LinearLayout) findViewById(R.id.btn_tone_generator);
        this.btn_sound_info = (LinearLayout) findViewById(R.id.btn_sound_info);
        this.btn_sound_meter.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                ActivityMain activityMain = ActivityMain.this;
                activityMain.ad_code = 1;
                    ActivityMain.this.startActivity(new Intent(ActivityMain.this, ActivitySoundAudioMeter.class));

            }
        });
        this.btn_tone_generator.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                ActivityMain activityMain = ActivityMain.this;
                activityMain.ad_code = 2;

                    ActivityMain.this.startActivity(new Intent(ActivityMain.this, ActivityGeneratorOfWaveTone.class));
            }
        });
        this.btn_sound_info.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                ActivityMain activityMain = ActivityMain.this;
                activityMain.ad_code = 3;
                    ActivityMain.this.startActivity(new Intent(ActivityMain.this, ActivityInfoAboutSound.class));

            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void onBackPressed() {
        super.onBackPressed();
        finish();
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
                        Uri.parse("https://play.google.com/store/apps/developer?id=" + getString(R.string.moreads)));
                try {
                    startActivity(more);

                } catch (ActivityNotFoundException e) {
                    Toast.makeText(
                            ActivityMain.this,
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
