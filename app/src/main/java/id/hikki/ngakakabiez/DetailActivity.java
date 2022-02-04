package id.hikki.ngakakabiez;

import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

public class DetailActivity extends AppCompatActivity {

    ImageView img;
    String url;
    RequestListener rl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        supportPostponeEnterTransition();
        setContentView(R.layout.activity_detail);
        img = findViewById(R.id.img2);
        img.setTransitionName(url);
        url = getIntent().getStringExtra("url");
        rl = new RequestListener() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target target, boolean isFirstResource) {
                Toast.makeText(DetailActivity.this, "Error : "+e.getMessage(), Toast.LENGTH_SHORT).show();
                return false;
            }

            @Override
            public boolean onResourceReady(Object resource, Object model, Target target, DataSource dataSource, boolean isFirstResource) {
                supportStartPostponedEnterTransition();
                return false;
            }
        };
        Glide.with(getApplicationContext()).load(url).onlyRetrieveFromCache(true).listener(rl).into(img);

    }
}