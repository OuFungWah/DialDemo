package com.example.daildemo;

import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.daildemo.view.DialView;
import com.example.daildemo.view.OnColorChangeListener;

import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, OnColorChangeListener{

    private DialView dialView;
    private Random random = new Random();
    private RelativeLayout relativeLayout;
    private FloatingActionButton fab_refresh;
    private FloatingActionButton fab_max;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dialView = (DialView)findViewById(R.id.dial_view);
        relativeLayout = (RelativeLayout)findViewById(R.id.back);
        fab_refresh = (FloatingActionButton)findViewById(R.id.fab_refresh);
        fab_max = (FloatingActionButton)findViewById(R.id.fab_max);
        dialView.setOnClickListener(this);
        fab_refresh.setOnClickListener(this);
        fab_max.setOnClickListener(this);
        dialView.setOnColorChangeListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.dial_view:
                dialView.changeAngle(random.nextInt(300));
                break;
            case R.id.fab_refresh:
                dialView.changeAngle(1);
                break;
            case R.id.fab_max:
                dialView.changeAngle(300);
                break;
        }

    }

    @Override
    public void onColorChange(final int red, final int green) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                relativeLayout.setBackgroundColor(Color.argb(75,red,green,0));
            }
        });

    }
}
