package org.odk.collect.android.login;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import org.odk.collect.android.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class AccueilSplashScreen extends Activity {

    ProgressBar progressBar;
    CircleImageView circleImageView;
    Thread splashTread;

    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        Window window = getWindow();
        window.setFormat(PixelFormat.RGBA_8888);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accueil_splash_screen);
        StartAnimations();

        progressBar =(ProgressBar)findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);


    }

    private void StartAnimations(){

        Animation anim = AnimationUtils.loadAnimation(this, R.anim.alpha);
        anim.reset();

        RelativeLayout rel = (RelativeLayout)findViewById(R.id.activity_accueil_splash_screen);
        rel.clearAnimation();
        rel.startAnimation(anim);

        anim = AnimationUtils.loadAnimation(this, R.anim.translate);
        anim.reset();

        circleImageView = (CircleImageView)findViewById(R.id.logo);
        circleImageView.clearAnimation();
        circleImageView.startAnimation(anim);
        progressBar.setVisibility(View.VISIBLE);

        splashTread = new Thread(){
            @Override
            public void run() {
                try {
                    int waited = 0;
                    // Splash screen pause time
                    while (waited < 3500) {
                        sleep(100);
                        waited += 100;
                        if(waited%2 ==0){
                            progressBar.getIndeterminateDrawable().setColorFilter(0xFFFF0000, android.graphics.PorterDuff.Mode.MULTIPLY);
                        }else
                            progressBar.getIndeterminateDrawable().setColorFilter(0xF4ED0000, android.graphics.PorterDuff.Mode.MULTIPLY);
                    }
                    Intent intent = new Intent(AccueilSplashScreen.this,
                            Login.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                    AccueilSplashScreen.this.finish();
                } catch (InterruptedException e) {
                    // do nothing
                } finally {
                    AccueilSplashScreen.this.finish();
                }

            }
        };
        splashTread.start();

    }

}
