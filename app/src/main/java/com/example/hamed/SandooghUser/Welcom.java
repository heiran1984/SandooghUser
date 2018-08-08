package com.example.hamed.SandooghUser;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

public class Welcom extends AppCompatActivity {

    TextView wellcom;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

/*
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(ContextCompat.getColor(this, R.color.colorPrimary));
        }

*/

      // if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
       //     Window w=getWindow();

        //   w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
       // }
        setContentView(R.layout.activity_welcom);
        Typeface typeface=Typeface.createFromAsset(getAssets(),"fonts/IranNastaliq.ttf");

        wellcom=(TextView)findViewById(R.id.wellcom);
         wellcom.setTypeface(typeface);
      // FullScreencall();

      Thread mySplash=new Thread(){
            @Override
            public void run() {
                try {
                    sleep(1000);
                    if(SharedPrefManager.getInstance(getApplicationContext()).isLoggedIn()){
                       Intent i=new Intent(getApplicationContext(),TabLayoutActivity.class);
                       startActivity(i);
                       finish();
                    }
                    else{
                        Intent i=new Intent(getApplicationContext(),LoginActivity.class);
                        startActivity(i);
                        finish();
                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        mySplash.start();
    }

    public void FullScreencall() {
        if(Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower api
            View v = this.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else if(Build.VERSION.SDK_INT >= 19) {
            //for new api versions.
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }
}
