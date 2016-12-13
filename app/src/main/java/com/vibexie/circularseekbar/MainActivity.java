package com.vibexie.circularseekbar;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewTreeObserver;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        CircularSeekbar circularSeekbar = (CircularSeekbar) this.findViewById(R.id.seekBar);
//        circularSeekbar.setOnProgressChangeListener(new CircularSeekbar.OnProgressChangeListener() {
//            @Override
//            public void onProgressBack(int progress) {
//                Log.e("test", "seek= " + progress);
//            }
//        });
//        circularSeekbar.setProgress(80);
    }
}
