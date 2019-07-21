package com.satyajit.gamex.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ValueAnimator;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;

import com.satyajit.gamex.R;

public class SplashActivity extends AppCompatActivity {

    private ValueAnimator valueAnim;

    private ImageView img1;
    private ImageView img2;
    Button enter, playStore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        initUI();

        addListeners();

        valueAnim.start(); //Start the animation.

        }

        void initUI(){

            img1 =  findViewById(R.id.imageBg1);
            img2 =  findViewById(R.id.imageBg2);

            enter = findViewById(R.id.enter);
            playStore = findViewById(R.id.follow);


            valueAnim = ValueAnimator.ofFloat(0.0f, 1.0f);
            valueAnim.setRepeatCount(-1);
            valueAnim.setInterpolator(new LinearInterpolator());
            valueAnim.setDuration(120000);

        }

        void addListeners(){

            valueAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    float width = (float) img1.getWidth();
                    float floatValue = (Float) valueAnimator.getAnimatedValue() * width;
                    img1.setTranslationX(floatValue);
                    img2.setTranslationX(floatValue - width);
                }
            });


            enter.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    finish();
                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }
            });
            playStore.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    Intent intent = new Intent("android.intent.action.VIEW", Uri.parse("https://play.google.com/store/apps/developer?id=SatyaJit+Pradhan"));
                    intent.setPackage("com.instagram.android");
                    try {
                        startActivity(intent);

                    } catch (ActivityNotFoundException unused) {
                        startActivity(new Intent("android.intent.action.VIEW", Uri.parse("https://play.google.com/store/apps/developer?id=SatyaJit+Pradhan")));
                    }

                }



            });



        }
}
