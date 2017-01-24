package org.odk.collect.android.login;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.odk.collect.android.R;
import org.odk.collect.android.widgets.ImageWebViewWidget;

import de.hdodenhof.circleimageview.CircleImageView;

public class AccueilSplashScreen extends Activity {

    ProgressBar progressBar;
    TextView title;
    ImageView circleImageView;
    Thread splashTread;
    int barProgress =0;

    /*public void onAttachedToWindow() {
        super.onAttachedToWindow();
        Window window = getWindow();
        window.setFormat(PixelFormat.RGBA_8888);
    }*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accueil_splash_screen);

       StartAnimations();

    }

    private void StartAnimations(){

       Animation anim = AnimationUtils.loadAnimation(this, R.anim.alpha);
        Animation anim2 = AnimationUtils.loadAnimation(this, R.anim.alpha);
        anim.reset();
        anim2.reset();

        RelativeLayout rel = (RelativeLayout)findViewById(R.id.activity_accueil_splash_screen);
        rel.clearAnimation();
        rel.startAnimation(anim);

        anim = AnimationUtils.loadAnimation(this, R.anim.translate);
        anim2 = AnimationUtils.loadAnimation(this, R.anim.translatetitle);
        anim.reset();
        anim2.reset();

        circleImageView = (ImageView)findViewById(R.id.logo);
        progressBar =(ProgressBar)findViewById(R.id.progressBar1);
        title = (TextView)findViewById(R.id.titre);
        title.clearAnimation();
        progressBar.clearAnimation();
        circleImageView.clearAnimation();
        circleImageView.startAnimation(anim);
        title.startAnimation(anim);
        progressBar.startAnimation(anim);
        splashTread = new Thread(){
            @Override
            public void run() {
                try {
                    int waited = 0;
                    // Splash screen pause time
                    while (waited < 7500 ) {
                        sleep(100);
                        waited+=100;
                    }
                    Intent intent = new Intent(AccueilSplashScreen.this,
                            Login.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
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
