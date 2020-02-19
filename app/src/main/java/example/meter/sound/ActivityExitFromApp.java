package example.meter.sound;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build.VERSION;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.view.View.OnClickListener;

import android.view.Window;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;


public class ActivityExitFromApp extends AppCompatActivity implements OnClickListener {


    int ads_const;

    LinearLayout btnNo;
    LinearLayout btnYes;
    LinearLayout lin_see_all;
    ListView listView;
    SharedPreferences spref;
    int success = 0;
    TextView txt_privacy;
    private Context mContext;

    public void onBackPressed() {
    }



//    private void populateContentAdView(NativeContentAd nativeContentAd, NativeContentAdView nativeContentAdView) {
//        nativeContentAd.getVideoController().setVideoLifecycleCallbacks(new VideoLifecycleCallbacks() {
//            public void onVideoEnd() {
//                super.onVideoEnd();
//            }
//        });
//        nativeContentAdView.setMediaView((MediaView) nativeContentAdView.findViewById(R.id.appinstall_media));
//        nativeContentAdView.setHeadlineView(nativeContentAdView.findViewById(R.id.contentad_headline));
//        nativeContentAdView.setBodyView(nativeContentAdView.findViewById(R.id.contentad_body));
//        nativeContentAdView.setCallToActionView(nativeContentAdView.findViewById(R.id.contentad_call_to_action));
//        nativeContentAdView.setLogoView(nativeContentAdView.findViewById(R.id.contentad_logo));
//        nativeContentAdView.setAdvertiserView(nativeContentAdView.findViewById(R.id.contentad_advertiser));
//        ((TextView) nativeContentAdView.getHeadlineView()).setText(nativeContentAd.getHeadline());
//        ((TextView) nativeContentAdView.getBodyView()).setText(nativeContentAd.getBody());
//        ((TextView) nativeContentAdView.getCallToActionView()).setText(nativeContentAd.getCallToAction());
//        ((TextView) nativeContentAdView.getAdvertiserView()).setText(nativeContentAd.getAdvertiser());
//        Image logo = nativeContentAd.getLogo();
//        if (logo == null) {
//            nativeContentAdView.getLogoView().setVisibility(4);
//        } else {
//            ((ImageView) nativeContentAdView.getLogoView()).setImageDrawable(logo.getDrawable());
//            nativeContentAdView.getLogoView().setVisibility(0);
//        }
//        nativeContentAdView.setNativeAd(nativeContentAd);
//    }

//    private void populateAppInstallAdView(NativeAppInstallAd nativeAppInstallAd, NativeAppInstallAdView nativeAppInstallAdView) {
//        nativeAppInstallAd.getVideoController().setVideoLifecycleCallbacks(new VideoLifecycleCallbacks() {
//            public void onVideoEnd() {
//                super.onVideoEnd();
//            }
//        });
//        nativeAppInstallAdView.setMediaView((MediaView) nativeAppInstallAdView.findViewById(R.id.appinstall_media));
//        nativeAppInstallAdView.setHeadlineView(nativeAppInstallAdView.findViewById(R.id.appinstall_headline));
//        nativeAppInstallAdView.setBodyView(nativeAppInstallAdView.findViewById(R.id.appinstall_body));
//        nativeAppInstallAdView.setCallToActionView(nativeAppInstallAdView.findViewById(R.id.appinstall_call_to_action));
//        nativeAppInstallAdView.setIconView(nativeAppInstallAdView.findViewById(R.id.appinstall_app_icon));
//        nativeAppInstallAdView.setPriceView(nativeAppInstallAdView.findViewById(R.id.appinstall_price));
//        nativeAppInstallAdView.setStarRatingView(nativeAppInstallAdView.findViewById(R.id.appinstall_stars));
//        nativeAppInstallAdView.setStoreView(nativeAppInstallAdView.findViewById(R.id.appinstall_store));
//        ((TextView) nativeAppInstallAdView.getHeadlineView()).setText(nativeAppInstallAd.getHeadline());
//        ((TextView) nativeAppInstallAdView.getBodyView()).setText(nativeAppInstallAd.getBody());
//        ((Button) nativeAppInstallAdView.getCallToActionView()).setText(nativeAppInstallAd.getCallToAction());
//        ((ImageView) nativeAppInstallAdView.getIconView()).setImageDrawable(nativeAppInstallAd.getIcon().getDrawable());
//        if (nativeAppInstallAd.getPrice() == null) {
//            nativeAppInstallAdView.getPriceView().setVisibility(View.INVISIBLE);
//        } else {
//            nativeAppInstallAdView.getPriceView().setVisibility(View.VISIBLE);
//            ((TextView) nativeAppInstallAdView.getPriceView()).setText(nativeAppInstallAd.getPrice());
//        }
//        if (nativeAppInstallAd.getStore() == null) {
//            nativeAppInstallAdView.getStoreView().setVisibility(4);
//        } else {
//            nativeAppInstallAdView.getStoreView().setVisibility(0);
//            ((TextView) nativeAppInstallAdView.getStoreView()).setText(nativeAppInstallAd.getStore());
//        }
//        if (nativeAppInstallAd.getStarRating() == null) {
//            nativeAppInstallAdView.getStarRatingView().setVisibility(4);
//        } else {
//            ((RatingBar) nativeAppInstallAdView.getStarRatingView()).setRating(nativeAppInstallAd.getStarRating().floatValue());
//            nativeAppInstallAdView.getStarRatingView().setVisibility(0);
//        }
//        nativeAppInstallAdView.setNativeAd(nativeAppInstallAd);
//    }


    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.addFlags(Integer.MIN_VALUE);
            window.clearFlags(67108864);
            window.setStatusBarColor(getResources().getColor(R.color.status_color_tap));
        }
        setContentView((int) R.layout.exit_layout);

        this.mContext = this;
        setContentView();
    }

    /* Access modifiers changed, original: 0000 */

    private void setContentView() {
        this.btnYes = (LinearLayout) findViewById(R.id.btnyes);
        this.btnNo = (LinearLayout) findViewById(R.id.btnno);
        this.btnYes.setOnClickListener(this);
        this.btnNo.setOnClickListener(this);


    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnno /*2131361867*/:
                finish();
                return;
            case R.id.btnyes /*2131361868*/:
                sendBroadcast(new Intent(ActivityClickForStart.ACTION_CLOSE));
                finish();
                return;
            default:
                return;
        }
    }


    public boolean isOnline() {
        NetworkInfo activeNetworkInfo = ((ConnectivityManager) getSystemService("connectivity")).getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }
}
