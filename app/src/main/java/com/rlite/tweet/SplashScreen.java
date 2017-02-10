package com.rlite.tweet;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;

import io.fabric.sdk.android.Fabric;

public class SplashScreen extends AppCompatActivity {

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "s1utoyn5UgXhA5AGMMsUec6VN";
    private static final String TWITTER_SECRET = "MGgO7Fb0iMLI5GfiFCOrmdxnaoi7xcjIb6uYhJKK0AnC1V24rO";

    private static int SPLASH_TIME_OUT = 2500;
    Context context;

    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));
        setContentView(R.layout.activity_splash_screen);

        textView = (TextView) findViewById(R.id.data);
        final boolean installed = appInstalledOrNot("com.twitter.android");

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                context = getApplicationContext();
                if (installed) {
                    Intent i = new Intent(SplashScreen.this, LoginActivity.class);
                    startActivity(i);
                    finish();
                }
                else
                {
                    textView.setText("Twitter App is not installed on your device");
                }
            }
        }, SPLASH_TIME_OUT);

    }

    private boolean appInstalledOrNot(String uri) {
        PackageManager pm = getPackageManager();
        boolean app_installed;
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            app_installed = true;
        }
        catch (PackageManager.NameNotFoundException e) {
            app_installed = false;
        }
        return app_installed;
    }

}
