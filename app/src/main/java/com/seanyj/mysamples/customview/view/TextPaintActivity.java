package com.seanyj.mysamples.customview.view;

import android.graphics.Typeface;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.seanyj.mysamples.R;

public class TextPaintActivity extends AppCompatActivity {

    private TextView mTextView;
    private TextView mTextView2;
    private boolean mReverseMeasure = false;
    private String mTestedString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paint);
        mTextView = (TextView) findViewById(R.id.textView);
        mTextView2 = (TextView) findViewById(R.id.textView2);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_paint, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_strike_through:
                Log.e("hello", "strike through");
                mTextView.getPaint().setFlags(0);
                mTextView.getPaint().setTextSkewX(0);
                mTextView.getPaint().setStrikeThruText(true);
                mTextView.invalidate();
                break;
            case R.id.action_underLine:
                mTextView.getPaint().setFlags(0);
                mTextView.getPaint().setTextSkewX(0);
                mTextView.getPaint().setUnderlineText(true);
                mTextView.invalidate();
                break;
            case R.id.action_skewX:
                mTextView.getPaint().setFlags(0);
                mTextView.getPaint().setTextSkewX(0.25f);
                mTextView.invalidate();
                break;
            case R.id.action_measure_size:
                if (mReverseMeasure) {
                    String s = "123456789abcdefg";
                    mTestedString = mTextView2.getText().toString();
                    float[] actualWidth = new float[1];
                    int cnt = mTextView2.getPaint().breakText("123456789abcdefg", 0, s.length(), true,
                            mTextView2.getWidth(), actualWidth);
                    mTextView.setText("can contain at most " + cnt + " chars, actual width measured is " + actualWidth[0]);
                    mTextView2.setText(s);
                } else {
                    float w = mTextView2.getPaint().measureText(mTextView2.getText().toString());
                    mTextView.setText("measured width: " + w);
                }
                mReverseMeasure = !mReverseMeasure;
                break;
            case R.id.action_serif_italic:
                Typeface typeface = Typeface.create(Typeface.SERIF, Typeface.ITALIC);
                mTextView.getPaint().setTypeface(typeface);
                mTextView.invalidate();
                break;
            case R.id.external_font:
                Typeface typeface1 = Typeface.createFromAsset(getAssets(), "fonts/samplefont.ttf");
                mTextView.getPaint().setTypeface(typeface1);
                mTextView.invalidate();
                break;
        }
        return true;
    }
}
