package id.hikki.ngakakabiez;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.VideoView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatDelegate;
import com.bumptech.glide.Glide;

import java.io.IOException;

public class SplashActivity extends AppCompatActivity {

    ProgressBar progress;
    Button skip;
    VideoView v;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        v = findViewById(R.id.lordArul);
        progress = findViewById(R.id.progres);
        skip = findViewById(R.id.skip);
        SharedPreferences sp = getSharedPreferences("mode",MODE_PRIVATE);
        boolean a = sp.getBoolean("dark",false);
        if(a){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
        v.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.lord));
        v.requestFocus();
        v.start();
        v.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                progress.setVisibility(View.GONE);
                Animation anim = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.animasi);
                skip.setVisibility(View.VISIBLE);
                skip.startAnimation(anim);
                skip.setOnClickListener(v->{
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                });
            }
        },13000);
    }

}