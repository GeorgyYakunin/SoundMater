package example.meter.sound.tonegenerator;

import android.content.Context;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.github.mikephil.charting.utils.Utils;

import java.util.Locale;

import example.meter.sound.R;

public class CustomSeekBar extends LinearLayout {
    public OnValueChangeListener listener;
    public double maxValue;
    public double minValue;
    public double value;
    private EditText editText;
    private int numDecimalPlaces;
    private int numSteps;
    private String numberFormat;
    private Scale scale = Scale.LINEAR;
    private SeekBar seekBar;
    private TextView textView;

    public CustomSeekBar(Context context) {
        super(context);
        init();
    }

    public CustomSeekBar(Context context, @Nullable AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    private static double mapLinear(double d, double d2, double d3, double d4, double d5) {
        return (((d - d2) / (d3 - d2)) * (d5 - d4)) + d4;
    }

    private static double mapExponential(double d, double d2, double d3, double d4, double d5) {
        return Math.exp(Math.log(d5 / d4) * ((d - d2) / (d3 - d2))) * d4;
    }

    private static double mapLogarithmic(double d, double d2, double d3, double d4, double d5) {
        return ((Math.log(d / d2) / Math.log(d3 / d2)) * (d5 - d4)) + d4;
    }

    private void init() {
        inflate(getContext(), R.layout.seekbar_customize, this);
        this.textView = (TextView) findViewById(R.id.textView);
        this.seekBar = (SeekBar) findViewById(R.id.seekBar);
        this.editText = (EditText) findViewById(R.id.editText);
        this.seekBar.setOnSeekBarChangeListener(new SeekBarHandler());
        this.editText.setOnEditorActionListener(new EditTextHandler());
        setNumDecimalPlaces(1);
    }

    public void setOnValueChangeListener(OnValueChangeListener onValueChangeListener) {
        this.listener = onValueChangeListener;
        onValueChangeListener.onValueChanged(this.value);
    }

    public String getTitle() {
        return this.textView.getText().toString();
    }

    public void setTitle(String str) {
        this.textView.setText(str);
    }

    public double getMinValue() {
        return this.minValue;
    }

    public void setMinValue(double d) {
        this.minValue = d;
    }

    public double getMaxValue() {
        return this.maxValue;
    }

    public void setMaxValue(double d) {
        this.maxValue = d;
    }

    public int getNumSteps() {
        return this.numSteps;
    }

    public void setNumSteps(int i) {
        this.numSteps = i;
        this.seekBar.setMax(i);
    }

    public int getNumDecimalPlaces() {
        return this.numDecimalPlaces;
    }

    public void setNumDecimalPlaces(int i) {
        this.numDecimalPlaces = i;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("%.");
        stringBuilder.append(i);
        stringBuilder.append("f");
        this.numberFormat = stringBuilder.toString();
    }

    public Scale getScale() {
        return this.scale;
    }

    public void setScale(Scale scale) {
        this.scale = scale;
    }

    public double getValue() {
        return this.value;
    }

    public void setValue(double d) {
        this.value = d;
        updateSeekBar();
        updateEditText();
        OnValueChangeListener onValueChangeListener = this.listener;
        if (onValueChangeListener != null) {
            onValueChangeListener.onValueChanged(this.value);
        }
    }

    public void updateSeekBar() {
        this.seekBar.setProgress(valueToProgress(this.value));
    }

    public void updateEditText() {
        this.editText.setText(valueToString(this.value));
    }

    public double progressToValue(int i) {
        if (this.scale == Scale.LINEAR) {
            return mapLinear((double) i, Utils.DOUBLE_EPSILON, (double) this.numSteps, this.minValue, this.maxValue);
        }
        if (this.scale == Scale.LOGARITHMIC) {
            return mapExponential((double) i, Utils.DOUBLE_EPSILON, (double) this.numSteps, this.minValue, this.maxValue);
        }
        throw new RuntimeException("Invalid scale!");
    }

    private int valueToProgress(double d) {
        if (this.scale == Scale.LINEAR) {
            return (int) (mapLinear(d, this.minValue, this.maxValue, Utils.DOUBLE_EPSILON, (double) this.numSteps) + 0.5d);
        } else if (this.scale == Scale.LOGARITHMIC) {
            return (int) (mapLogarithmic(d, this.minValue, this.maxValue, Utils.DOUBLE_EPSILON, (double) this.numSteps) + 0.5d);
        } else {
            throw new RuntimeException("Invalid scale!");
        }
    }

    public String valueToString(double d) {
        return String.format(Locale.ENGLISH, this.numberFormat, new Object[]{Double.valueOf(d)});
    }

    public enum Scale {
        LINEAR,
        LOGARITHMIC
    }

    public interface OnValueChangeListener {
        void onValueChanged(double d);
    }

    private class EditTextHandler implements OnEditorActionListener {
        private EditTextHandler() {
        }

        public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
            if (i != 5 && i != 6) {
                return false;
            }
            try {
                double parseDouble = Double.parseDouble(textView.getText().toString());
                if (parseDouble < CustomSeekBar.this.minValue || parseDouble > CustomSeekBar.this.maxValue) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Please enter a number between ");
                    stringBuilder.append(CustomSeekBar.this.valueToString(CustomSeekBar.this.minValue));
                    stringBuilder.append(" and ");
                    stringBuilder.append(CustomSeekBar.this.valueToString(CustomSeekBar.this.maxValue));
                    stringBuilder.append(".");
                    Toast.makeText(CustomSeekBar.this.getContext(), stringBuilder.toString(), Toast.LENGTH_SHORT).show();
                    CustomSeekBar.this.updateEditText();
                    return true;
                }
                CustomSeekBar.this.value = parseDouble;
                CustomSeekBar.this.updateSeekBar();
                if (CustomSeekBar.this.listener != null) {
                    CustomSeekBar.this.listener.onValueChanged(CustomSeekBar.this.value);
                }
                return true;
            } catch (NumberFormatException unused) {
                Toast.makeText(CustomSeekBar.this.getContext(), "Please enter a number.", Toast.LENGTH_SHORT).show();
                CustomSeekBar.this.updateEditText();
            }
            return false;
        }
    }

    private class SeekBarHandler implements OnSeekBarChangeListener {
        private SeekBarHandler() {
        }

        public void onStartTrackingTouch(SeekBar seekBar) {
        }

        public void onStopTrackingTouch(SeekBar seekBar) {
        }

        public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
            if (z) {
                CustomSeekBar customSeekBar = CustomSeekBar.this;
                customSeekBar.value = customSeekBar.progressToValue(i);
                CustomSeekBar.this.updateEditText();
                if (CustomSeekBar.this.listener != null) {
                    CustomSeekBar.this.listener.onValueChanged(CustomSeekBar.this.value);
                }
            }
        }
    }
}
