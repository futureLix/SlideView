package com.lsd.slideview;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

import com.example.custom.CustomGestureDetector;
import com.example.custom.CustomView;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener {
    private GestureDetector mGestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        init();
    }

    private void init() {
        String[] name = new String[]{"延时摄影", "慢动作", "视频", "照片", "正方形", "全景"};
        final CustomView customView = findViewById(R.id.custom);
        CustomGestureDetector releaseGestureDetector = new CustomGestureDetector();
        releaseGestureDetector.setCustomView(customView);
        mGestureDetector = new GestureDetector(this, releaseGestureDetector);
        customView.setBackGround(ContextCompat.getColor(this, android.R.color.holo_orange_light));
        customView.setForeGround(ContextCompat.getColor(this, android.R.color.white));
        customView.setTextSize(15);
        customView.addIndicator(name);
        customView.setOnTouchListener(this);
        customView.setOnClickText(new CustomView.onClickText() {
            @Override
            public void onClick(int i, int last) {
                customView.scrollTo(i, last);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        return mGestureDetector.onTouchEvent(motionEvent);
    }
}
