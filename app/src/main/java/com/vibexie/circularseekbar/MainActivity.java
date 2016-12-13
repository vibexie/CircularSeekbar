package com.vibexie.circularseekbar;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CircularSeekbar circularSeekbar = (CircularSeekbar) this.findViewById(R.id.seekBar);
        circularSeekbar.setOnProgressChangeListener(new CircularSeekbar.OnProgressChangeListener() {
            @Override
            public void onProgressBack(int progress) {
                Log.e(TAG, "progress = " + progress);
            }
        });
        circularSeekbar.setProgress(24);

        CircularSeekbarSE circularSeekbarSE = (CircularSeekbarSE) this.findViewById(R.id.seekBarSE);
        circularSeekbarSE.setOnProgressChangeListener(new CircularSeekbarSE.OnProgressChangeListener() {
            @Override
            public void onProgressBack(float start, float end) {
                Log.e(TAG, "start = " + start + "   end = " + end);
            }
        });
        circularSeekbarSE.setProgress(99, 290);
    }
}
