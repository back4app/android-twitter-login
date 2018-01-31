package com.example.back4app.twitterlogin;

import android.support.v7.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import android.widget.Button;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseTwitterUtils;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.lang.String;


public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Parse.initialize(this);
        // don't forget to change the line below with your Consumer Key and Consumer Secret of your Twitter App
        ParseTwitterUtils.initialize("INSERT_YOUR_CONSUMER_KEY", "INSERT_YOUR_CONSUMER_SECRET");
        setContentView(R.layout.activity_login);

        final Button login_button = findViewById(R.id.login_button);
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final ProgressDialog dlg = new ProgressDialog(LoginActivity.this);
                dlg.setTitle("Please, wait a moment.");
                dlg.setMessage("Logging in...");
                dlg.show();

                ParseTwitterUtils.logIn(LoginActivity.this, new LogInCallback() {

                    @Override
                    public void done(final ParseUser user, ParseException err) {
                        if (err != null) {
                            dlg.dismiss();
                            ParseUser.logOut();
                            Log.e("err", "err", err);
                        }
                        if (user == null) {
                            dlg.dismiss();
                            ParseUser.logOut();
                            Toast.makeText(LoginActivity.this, "The user cancelled the Twitter login.", Toast.LENGTH_LONG).show();
                            Log.d("MyApp", "Uh oh. The user cancelled the Twitter login.");
                        } else if (user.isNew()) {
                            dlg.dismiss();
                            Toast.makeText(LoginActivity.this, "User signed up and logged in through Twitter.", Toast.LENGTH_LONG).show();
                            Log.d("MyApp", "User signed up and logged in through Twitter!");
                            user.setUsername(ParseTwitterUtils.getTwitter().getScreenName());
                            user.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(ParseException e) {
                                    if (null == e) {
                                        alertDisplayer("First time login!", "Welcome!");
                                    } else {
                                        ParseUser.logOut();
                                        Toast.makeText(LoginActivity.this, "It was not possible to save your username.", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        } else {
                            dlg.dismiss();
                            Toast.makeText(LoginActivity.this, "User logged in through Twitter.", Toast.LENGTH_LONG).show();
                            Log.d("MyApp", "User logged in through Twitter!");
                            alertDisplayer("Oh, you!","Welcome back!");
                        }
                    }
                });
            }
        });
    }

    private void alertDisplayer(String title,String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        Intent intent = new Intent(LoginActivity.this, LogoutActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                });
        AlertDialog ok = builder.create();
        ok.show();
    }

}