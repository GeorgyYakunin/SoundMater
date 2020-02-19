package example.meter.sound.tonegenerator;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.List;

import example.meter.sound.R;
import example.meter.sound.tonegenerator.WaveView.OnWaveChangeListener;

public class ActivityGeneratorOfWaveTone extends AppCompatActivity {
    private static final float DEFAULT_AMPLITUDE = 0.3f;
    private static final float DEFAULT_FREQUENCY = 440.0f;
    private static final float MIN_FREQUENCY = 16.0f;
    private static final int SAMPLE_RATE = 44100;
    public Wave carrierWave;
    public int carrierWaveForm = 0;
    public boolean modulationActivated = false;
    public int modulationType = 0;
    public Wave modulatorWave;
    public int modulatorWaveForm = 0;
    public boolean playing = false;
    public Sound sound;
    Context context;
    SharedPreferences spref;
    Toolbar toolbar;
    private LinearLayout btnPlay;
    private LinearLayout btnStop;
    private LinearLayout carrierWaveLayout;
    private WaveView carrierWaveView;
    private CheckBox cbActivateModulation;
    private LineChart graph;
    private LinearLayout modulatorWaveLayout;
    private WaveView modulatorWaveView;
    private Spinner spCarrierWaveForm;
    private Spinner spModulationType;
    private Spinner spModulatorWaveForm;
    private Wave wave;



    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_generator_for_wave_and_tone);
        this.toolbar = (Toolbar) findViewById(R.id.toolBar);
        setSupportActionBar(this.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        this.context = this;
        findViews();
        initViews();
        this.sound = new Sound(SAMPLE_RATE, false, 1.0f);
    }

    private void findViews() {
        this.carrierWaveLayout = (LinearLayout) findViewById(R.id.carrierWaveLayout);
        this.spCarrierWaveForm = (Spinner) findViewById(R.id.spCarrierWaveForm);
        this.modulatorWaveLayout = (LinearLayout) findViewById(R.id.modulatorWaveLayout);
        this.spModulationType = (Spinner) findViewById(R.id.spModulationType);
        this.spModulatorWaveForm = (Spinner) findViewById(R.id.spModulatorWaveForm);
        this.cbActivateModulation = (CheckBox) findViewById(R.id.cbActivateModulation);
        this.btnPlay = (LinearLayout) findViewById(R.id.btnPlay);
        this.btnStop = (LinearLayout) findViewById(R.id.btnStop);
        this.graph = (LineChart) findViewById(R.id.graph);
    }

    private void initViews() {
        ArrayAdapter createFromResource = ArrayAdapter.createFromResource(this, R.array.wave_forms, R.layout.spinner_text);
        createFromResource.setDropDownViewResource(17367049);
        this.spCarrierWaveForm.setAdapter(createFromResource);
        this.spCarrierWaveForm.setOnItemSelectedListener(new OnItemSelectedListener() {
            public void onNothingSelected(AdapterView<?> adapterView) {
            }

            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long j) {
                ActivityGeneratorOfWaveTone activityGeneratorOfWaveTone = ActivityGeneratorOfWaveTone.this;
                activityGeneratorOfWaveTone.carrierWaveForm = i;
                activityGeneratorOfWaveTone.updateCarrierWaveView();
            }
        });
        ArrayAdapter createFromResource2 = ArrayAdapter.createFromResource(this, R.array.wave_forms, R.layout.spinner_text);
        createFromResource2.setDropDownViewResource(17367049);
        this.spModulatorWaveForm.setAdapter(createFromResource2);
        this.spModulatorWaveForm.setOnItemSelectedListener(new OnItemSelectedListener() {
            public void onNothingSelected(AdapterView<?> adapterView) {
            }

            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long j) {
                ActivityGeneratorOfWaveTone activityGeneratorOfWaveTone = ActivityGeneratorOfWaveTone.this;
                activityGeneratorOfWaveTone.modulatorWaveForm = i;
                activityGeneratorOfWaveTone.updateModulatorWaveView();
            }
        });
        createFromResource2 = ArrayAdapter.createFromResource(this, R.array.modulation_types, R.layout.spinner_text);
        createFromResource2.setDropDownViewResource(17367049);
        this.spModulationType.setAdapter(createFromResource2);
        this.spModulationType.setOnItemSelectedListener(new OnItemSelectedListener() {
            public void onNothingSelected(AdapterView<?> adapterView) {
            }

            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long j) {
                ActivityGeneratorOfWaveTone activityGeneratorOfWaveTone = ActivityGeneratorOfWaveTone.this;
                activityGeneratorOfWaveTone.modulationType = i;
                activityGeneratorOfWaveTone.updateWave();
            }
        });
        this.cbActivateModulation.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                ActivityGeneratorOfWaveTone activityGeneratorOfWaveTone = ActivityGeneratorOfWaveTone.this;
                activityGeneratorOfWaveTone.modulationActivated = z;
                activityGeneratorOfWaveTone.updateWave();
            }
        });
        this.btnPlay.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (ActivityGeneratorOfWaveTone.this.sound != null) {
                    ActivityGeneratorOfWaveTone.this.sound.loop();
                    ActivityGeneratorOfWaveTone.this.playing = true;
                }
            }
        });
        this.btnStop.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (ActivityGeneratorOfWaveTone.this.sound != null) {
                    ActivityGeneratorOfWaveTone.this.sound.stop();
                    ActivityGeneratorOfWaveTone.this.playing = false;
                }
            }
        });
    }

    public void updateCarrierWaveView() {
        this.carrierWaveLayout.removeView(this.carrierWaveView);
        this.carrierWaveView = WaveViewFactory.createWaveView(this.carrierWaveForm, getBaseContext(), 44100.0f, MIN_FREQUENCY, 16000.0f, DEFAULT_FREQUENCY, 0.0f, 1.0f, DEFAULT_AMPLITUDE);
        this.carrierWaveView.setOnWaveChangeListener(new OnWaveChangeListener() {
            public void onWaveChanged(Wave wave) {
                ActivityGeneratorOfWaveTone activityGeneratorOfWaveTone = ActivityGeneratorOfWaveTone.this;
                activityGeneratorOfWaveTone.carrierWave = wave;
                activityGeneratorOfWaveTone.updateWave();
            }
        });
        this.carrierWaveLayout.addView(this.carrierWaveView);
    }

    public void updateModulatorWaveView() {
        this.modulatorWaveLayout.removeView(this.modulatorWaveView);
        this.modulatorWaveView = WaveViewFactory.createWaveView(this.modulatorWaveForm, getBaseContext(), 44100.0f, 1.0f, 16000.0f, 1.0f, 0.0f, 1.0f, 1.0f);
        this.modulatorWaveView.setOnWaveChangeListener(new OnWaveChangeListener() {
            public void onWaveChanged(Wave wave) {
                ActivityGeneratorOfWaveTone activityGeneratorOfWaveTone = ActivityGeneratorOfWaveTone.this;
                activityGeneratorOfWaveTone.modulatorWave = wave;
                activityGeneratorOfWaveTone.updateWave();
            }
        });
        this.modulatorWaveLayout.addView(this.modulatorWaveView);
    }

    @SuppressLint({"WrongConstant"})
    public void updateWave() {
        Wave wave = this.carrierWave;
        if (wave != null) {
            if (this.modulationActivated) {
                this.wave = ModulatedWaveFactory.createModulatedWave(this.modulationType, wave, this.modulatorWave);
            } else {
                this.wave = wave;
            }
            if (this.wave == null) {
                Toast.makeText(this, "Invalid wave!", 0).show();
                return;
            }
            updateSound();
            updateGraph();
        }
    }

    private void updateSound() {
        float[] generatePeriod;
        Wave wave = this.wave;
        if (wave instanceof SampleWave) {
            generatePeriod = wave.generatePeriod();
        } else {
            generatePeriod = wave.generate(1.0f);
        }
        this.sound.stop();
        this.sound.fillBuffer(generatePeriod);
        if (this.playing) {
            this.sound.loop();
        }
    }

    private void updateGraph() {
        float[] generatePeriod = this.wave.generatePeriod();
        float sampleRate = 1.0f / this.wave.getSampleRate();
        ArrayList arrayList = new ArrayList();
        float f = 0.0f;
        for (float entry : generatePeriod) {
            arrayList.add(new Entry(f, entry));
            f += sampleRate;
        }
        List arrayList2 = new ArrayList();
        LineDataSet lineDataSet = new LineDataSet(arrayList, "Amplitude");
        lineDataSet.setDrawHighlightIndicators(false);
        lineDataSet.setColor(Color.parseColor("#FC863E"));
        lineDataSet.setDrawFilled(true);
        lineDataSet.setFillDrawable(new GradientDrawable(Orientation.TOP_BOTTOM, new int[]{Color.parseColor("#37c0ff"), Color.parseColor("#00FA5544")}));
        arrayList2.add(lineDataSet);
        this.graph.setData(new LineData(arrayList2));
        this.graph.setVisibleXRangeMaximum(this.wave.getPeriod());
        Description description = new Description();
        description.setText("");
        this.graph.setDescription(description);
        this.graph.setBackgroundColor(Color.parseColor("#051c2d"));
        this.graph.setGridBackgroundColor(Color.parseColor("#051c2d"));
        this.graph.setBorderColor(-1);
        this.graph.setScaleEnabled(false);
        this.graph.notifyDataSetChanged();
        this.graph.invalidate();
        XAxis xAxis = this.graph.getXAxis();
        xAxis.setAvoidFirstLastClipping(true);
        xAxis.setTextColor(-1);
        this.graph.getAxisLeft().setTextColor(-1);
        YAxis axisRight = this.graph.getAxisRight();
        axisRight.setEnabled(true);
        axisRight.setTextColor(-1);
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
                        Uri.parse("https://play.google.com/store/apps/developer?id="+getString(R.string.moreads)));
                try {
                    startActivity(more);

                } catch (ActivityNotFoundException e) {
                    Toast.makeText(
                            ActivityGeneratorOfWaveTone.this,
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
