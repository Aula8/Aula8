package unegdevelop.paintfragments.aula8.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import unegdevelop.paintfragments.R;
import unegdevelop.paintfragments.Servidor;


public class Splash extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        final ImageView iv = (ImageView) findViewById(R.id.imageView);
        final Animation an = AnimationUtils.loadAnimation(getBaseContext(), 
                                                            R.anim.rotate);
        final Animation an2 = AnimationUtils.loadAnimation(getBaseContext(), 
                                                            R.anim.abc_fade_out);
        iv.startAnimation(an);
        Servidor.start();
        an.setAnimationListener(new Animation.AnimationListener()
        {
            @Override
            public void onAnimationStart(Animation animation)
            {

            }

            @Override
            public void onAnimationEnd(Animation animation)
            {


                if(Servidor.isConnected())
                {
                    iv.startAnimation(an2);
                    finish();
                    Intent intent = new Intent(getBaseContext(), LoginActivity.class);
                    startActivity(intent);
                }
                else
                {
                    an.reset();
                    iv.startAnimation(an);
                    //finish();
                }

            }

            @Override
            public void onAnimationRepeat(Animation animation)
            {

            }
        });
    }
}