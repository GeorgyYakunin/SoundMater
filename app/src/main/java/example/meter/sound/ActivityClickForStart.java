package example.meter.sound;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.kaopiz.kprogresshud.KProgressHUD;


public class ActivityClickForStart extends AppCompatActivity {
    public static final String ACTION_CLOSE = "ACTION_CLOSE";
    private static String INSTALL_PREF = "install_pref";
    int ads_const;
    LinearLayout btn_start;
    LinearLayout lin_see_all;
    LinearLayout ll_start;
    String result = "";
    SharedPreferences spref;
    int success = 0;
    Toolbar toolbar;
    TextView txt_privacyPolicy;
    private FirstReceiver firstReceiver;
    private Context mContext;
    private PrefManager prefManager;
    KProgressHUD hud;




    @SuppressLint({"WrongConstant"})
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        setContentView((int) R.layout.activity_click_to_start);
        this.mContext = this;

        hud = KProgressHUD.create(ActivityClickForStart.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Showing Ads...");
        this.toolbar = (Toolbar) findViewById(R.id.anim_toolbar);
        setSupportActionBar(this.toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        this.btn_start = (LinearLayout) findViewById(R.id.btn_start);
        this.prefManager = new PrefManager(this);
        this.btn_start.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {

                    ActivityClickForStart.this.startActivity(new Intent(ActivityClickForStart.this, ActivityMain.class));


            }
        });
        IntentFilter intentFilter = new IntentFilter(ACTION_CLOSE);
        this.firstReceiver = new FirstReceiver();
        registerReceiver(this.firstReceiver, intentFilter);

    }

    public boolean isOnline() {
        NetworkInfo activeNetworkInfo = ((ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }

    private boolean checkNewInstall() {
        Context context = this.mContext;
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getPackageName(), 0);
        boolean z = sharedPreferences.getBoolean(INSTALL_PREF, false);
        if (!z) {
            sharedPreferences.edit().putBoolean(INSTALL_PREF, true).commit();
        }
        return z;
    }

    public void onBackPressed() {

        startActivity(new Intent(this, ActivityExitFromApp.class));

    }


    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(this.firstReceiver);
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

                startActivity(new Intent(this, ActivityExitFromApp.class));

                return true;

            case R.id.contact /*2131361913*/:
                Intent more = new Intent(
                        "android.intent.action.VIEW",
                        Uri.parse("https://play.google.com/store/apps/developer?id=" + getString(R.string.moreads)));
                try {
                    startActivity(more);

                } catch (ActivityNotFoundException e) {
                    Toast.makeText(
                            ActivityClickForStart.this,
                            "you_don_t_have_google_play_installed_or_internet_connection", Toast.LENGTH_LONG).show();
                }
                return true;

            case R.id.share /*2131362156*/:
                if (isOnline()) {
                    intent = new Intent("android.intent.action.SEND");
                    intent.setType("text/plain");
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("Hi! I'm using a Sound Meter application. Check it out: http://play.google.com/store/apps/details?id=");
                    stringBuilder.append(getPackageName());
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


    class FirstReceiver extends BroadcastReceiver {
        FirstReceiver() {
        }

        public void onReceive(Context context, Intent intent) {
            Log.e("FirstReceiver", "FirstReceiver");
            if (intent.getAction().equals(ActivityClickForStart.ACTION_CLOSE)) {
                ActivityClickForStart.this.finish();
            }
        }
    }
}
