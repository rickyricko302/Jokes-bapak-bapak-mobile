package id.hikki.ngakakabiez;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.*;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityOptionsCompat;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.vimalcvs.switchdn.DayNightSwitch;
import com.vimalcvs.switchdn.DayNightSwitchListener;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    List<String> dataJokes;
    TextView jokesText, status, textZoom;
    FloatingActionButton fab;
    MaterialCardView card,cards;
    DataRepository repo;
    RequestListener rl;
    RelativeLayout root;
    ImageView imageView, loading;
    DayNightSwitch dn;
    Switch sw;
    String url;
    boolean night = false;
    SharedPreferences sp;
    Button buttonJokes1, buttonJokes2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        /*
        if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_NO){
            night = false;
            setTheme(R.style.LightTheme);
        }
        else if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES){
            night = true;
            setTheme(R.style.DarkTheme);
        }

         */

        super.onCreate(savedInstanceState);
        supportPostponeEnterTransition();
        setContentView(R.layout.activity_main);

        fab = findViewById(R.id.floatingActionButton);
        card = findViewById(R.id.materialCardView);
        buttonJokes1 = findViewById(R.id.jokes1);
        loading = findViewById(R.id.imageLoading);
        buttonJokes2 = findViewById(R.id.jokes2);
        imageView = findViewById(R.id.imageJokes);
        status = findViewById(R.id.status);
        textZoom = findViewById(R.id.textZoom);
        cards = findViewById(R.id.cardImg);
        repo = new DataRepository(getApplicationContext());
        dataJokes = repo.getData();
        jokesText = findViewById(R.id.jokes_text);
        root = findViewById(R.id.root);


        AndroidNetworking.initialize(getApplicationContext());
        if(dataJokes != null){
            setJokesText();
        }
        fab.setOnClickListener(v->{
            Animation anim = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.animasi);
            card.startAnimation(anim);
            setJokesText();

        });

        dn = findViewById(R.id.switer);
        ColorDrawable cd = (ColorDrawable) root.getBackground();
        if(cd.getColor() == Color.BLACK){
            dn.setIsNight(true);
        }
        SharedPreferences.Editor se = getSharedPreferences("mode",0).edit();

        dn.setListener(new DayNightSwitchListener() {
            @Override
            public void onSwitch(boolean is_night) {
                if(is_night){
                    se.putBoolean("dark",true);
                    se.commit();
                   // getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                 //   reset();
                }
                else{
                    se.putBoolean("dark",false);
                    se.commit();
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                  //  reset();
                }

            }
        });
        /*
        sw = findViewById(R.id.switer);
        sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                   AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                }
                else{
                   AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

                }
           //     finish();
             //   startActivity(new Intent(MainActivity.this, MainActivity.this.getClass()));
            }
        });

         */
        rl = new RequestListener() {

            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target target, boolean isFirstResource) {
                url = null;
                imageView.setVisibility(View.VISIBLE);
                loading.setVisibility(View.GONE);
               // imageView.setBackgroundColor(Color.parseColor("#c8c8c8"));
                status.setVisibility(View.GONE);
              //  status.setText("Gagal memuat gambar pack");
                textZoom.setVisibility(View.GONE);
                showBar("Gagal memuat gambar pack!");
                return false;
            }

            @Override
            public boolean onResourceReady(Object resource, Object model, Target target, DataSource dataSource, boolean isFirstResource) {
                supportStartPostponedEnterTransition();
                imageView.setVisibility(View.VISIBLE);
                //imageView.setBackgroundColor(Color.parseColor("#c8c8c8"));
                loading.setVisibility(View.GONE);
                status.setVisibility(View.GONE);
                textZoom.setVisibility(View.VISIBLE);
                return false;
            }
        };
        buttonJokes1.setOnClickListener(v-> {
            textZoom.setVisibility(View.GONE);
            status.setText("Mendapatkan jokes... sabar pack");
            status.setVisibility(View.VISIBLE);
            loading.setVisibility(View.VISIBLE);
            imageView.setVisibility(View.GONE);
            Glide.with(getApplicationContext()).asGif().load(R.raw.loading).into(loading);
            AndroidNetworking.get("https://candaan-api.vercel.app/api/image/random")
                    .setPriority(Priority.MEDIUM)
                    .build().getAsJSONObject(new JSONObjectRequestListener() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        if (response.get("status").toString().equals("200")) {
                            url = response.getJSONObject("data").getString("url");
                            Glide.with(getApplicationContext()).load(url).dontTransform().addListener(rl).into(imageView);
                        } else {
                            url = null;
                            status.setText("Error code not 200");
                            textZoom.setVisibility(View.GONE);
                        }
                    } catch (JSONException e) {
                        url = null;
                        status.setText("Error : " + e.getMessage());
                        textZoom.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onError(ANError e) {
                    url = null;
                    if(e.getMessage().contains("resolve host")) {
                        status.setText("");
                        imageView.setVisibility(View.VISIBLE);
                        loading.setVisibility(View.GONE);
                        status.setVisibility(View.GONE);
                        textZoom.setVisibility(View.GONE);
                        showBar("Cek internetmu pack!");

                    }
                    else {
                        status.setText("Error : " + e.getMessage());
                        textZoom.setVisibility(View.GONE);
                    }
                }

            });
        });
        buttonJokes2.setOnClickListener(v->{
            textZoom.setVisibility(View.GONE);
            status.setText("Mendapatkan jokes... sabar pack");
            status.setVisibility(View.VISIBLE);
      //      imageView.setBackgroundColor(Color.parseColor("#000000"));
            imageView.setVisibility(View.GONE);
            loading.setVisibility(View.VISIBLE);
            Glide.with(getApplicationContext()).asGif().load(R.raw.loading).into(loading);
            url = "https://jokesbapak2.herokuapp.com/?"+String.valueOf(new Random().nextInt(99999));
            Glide.with(this).load(url).dontTransform().addListener(rl).into(imageView);

        });
        cards.setOnClickListener(v->{
            if(textZoom.getVisibility() == View.GONE){
                Toast.makeText(this, "Belum dapat gambar pack", Toast.LENGTH_SHORT).show();
            }
            else{
                Intent intent = new Intent(MainActivity.this,DetailActivity.class)
                        .putExtra("url",url);
                imageView.setTransitionName(url);
                startActivity(intent, ActivityOptionsCompat.makeSceneTransitionAnimation(MainActivity.this,imageView,imageView.getTransitionName()).toBundle());
            }
        });
    }

    private void reset() {
        Intent in = getIntent();
        finish();
        startActivity(in);
    }

    private void showBar(String s) {
        Snackbar bar = Snackbar.make(root, s, 2500);
        bar.setAnimationMode(Snackbar.ANIMATION_MODE_SLIDE);
        bar.setBackgroundTint(Color.parseColor("#FF4081"));
        bar.setTextColor(Color.WHITE);
        bar.show();
    }

    private void setJokesText(){
        String jokes = dataJokes.get(new Random().nextInt(dataJokes.size()));
        jokesText.setText(jokes);
    }

}