package projects.solucionescolabora.com.metodoasesores;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class LauncherActivity extends Activity {

    private ImageView imgLauch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);

        imgLauch = (ImageView) findViewById(R.id.imgLaucher);
        Animation animation = AnimationUtils.loadAnimation(LauncherActivity.this, R.anim.transparentar);

        imgLauch.setAnimation(animation);

        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                finish();
                startActivity(new Intent(LauncherActivity.this, LogOrRegActivity.class));
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });


    }
}
