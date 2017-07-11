package com.example.daildemo;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.example.daildemo.view.DialView;
import com.example.daildemo.view.OnColorChangeListener;

import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, OnColorChangeListener{

    private DialView dialView;
    private Random random = new Random();
    private LinearLayout linearLayout ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dialView = (DialView)findViewById(R.id.dial_view);
        linearLayout = (LinearLayout)findViewById(R.id.back);
        dialView.setOnClickListener(this);
        dialView.setOnColorChangeListener(this);

    }

    @Override
    public void onClick(View v) {
        dialView.changeAngle(random.nextInt(300));
    }

    @Override
    public void onColorChange(final int red, final int green) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                linearLayout.setBackgroundColor(Color.argb(65,red,green,0));
            }
        });

    }
}
