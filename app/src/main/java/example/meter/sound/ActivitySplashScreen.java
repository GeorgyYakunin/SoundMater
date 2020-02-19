package example.meter.sound;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.graphics.PorterDuff.Mode;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Handler;
import android.os.Process;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.widget.ProgressBar;

import com.kaopiz.kprogresshud.KProgressHUD;

public class ActivitySplashScreen extends AppCompatActivity {
    private static final String TAG = "ActivitySplashScreen";
    boolean Ad_Show = false;
    int ads_const;
    Editor editor;
    ProgressBar progressBar;
    SharedPreferences spref;
    private Context mContext;
    KProgressHUD hud;


    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (VERSION.SDK_INT >= 21) {
            getWindow().setFlags(67108864, 67108864);
        }
        setContentView(R.layout.activity_splash_screen);
        Log.e(TAG, "onCreate");
        hud = KProgressHUD.create(ActivitySplashScreen.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Showing Ads...");


        this.spref = getSharedPreferences("pref_ads", 0);
        this.editor = this.spref.edit();
        this.mContext = this;
        this.progressBar = (ProgressBar) findViewById(R.id.progressBar);
        this.progressBar.getIndeterminateDrawable().setColorFilter(Color.parseColor("#ffffff"), Mode.MULTIPLY);
        if (isOnline()) {
            ActivitySplashScreen.this.editor.putInt("ads_const", 0);
            ActivitySplashScreen.this.editor.commit();
            ActivitySplashScreen.this.HomeScreen();
            Log.e(TAG, "isOnline");
        } else {
            Log.e(TAG, "else");
            ContinueWithoutAdsProcess();
        }

    }

    public boolean isOnline() {
        NetworkInfo activeNetworkInfo = ((ConnectivityManager) getSystemService("connectivity")).getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }


    public void ContinueWithoutAdsProcess() {
        new Handler().postDelayed(new Runnable() {
            public void run() {
                ActivitySplashScreen.this.HomeScreen();
            }
        }, 3000);
    }

    public void HomeScreenWithoutFinish() {
        this.Ad_Show = false;
        startActivity(new Intent(this, ActivityClickForStart.class));
    }

    public void HomeScreen() {
        this.Ad_Show = false;
        startActivity(new Intent(this, ActivityClickForStart.class));
        finish();
    }

    public void onBackPressed() {
        super.onBackPressed();
        ExitApp();
    }

    public void ExitApp() {
        moveTaskToBack(true);
        finish();
        Process.killProcess(Process.myPid());
        System.exit(0);
    }
}
