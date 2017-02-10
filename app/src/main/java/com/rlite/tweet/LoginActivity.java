package com.rlite.tweet;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

public class LoginActivity extends AppCompatActivity {

    private TwitterLoginButton loginButton;
    TwitterSession session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        session = TwitterCore.getInstance().getSessionManager()
                .getActiveSession();

        loginButton = (TwitterLoginButton) findViewById(R.id.twitter_login_button);

        if (session != null)
        {
            final Intent intent = new Intent(LoginActivity.this,MainActivity.class);
            intent.putExtra("Username",session.getUserName());
            startActivity(intent);
            finish();
        }


        loginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                // The TwitterSession is also available through:
                // Twitter.getInstance().core.getSessionManager().getActiveSession()
                //TwitterSession session = result.data;
                session = TwitterCore.getInstance().getSessionManager()
                        .getActiveSession();

                String msg = "Welcome @" + session.getUserName()+"!!!!";
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                loginButton.setVisibility(View.GONE);

                final Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                intent.putExtra("Username",session.getUserName());
                startActivity(intent);
                finish();

            }
            @Override
            public void failure(TwitterException exception) {
                Log.d("TwitterKit", "Login with Twitter failure", exception);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        loginButton.onActivityResult(requestCode, resultCode, data);
    }
}
