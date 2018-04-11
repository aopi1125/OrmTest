package com.harbor.db;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.harbor.annotation.DIActivity;
import com.harbor.annotation.DIVIew;
import com.harbor.annotation.Test;
import com.harbor.greendao.NoteActivity;
import com.harbor.objectbox.ReactiveNoteActivity;
import com.harbor.ormlite.clickcounter.ClickConfig;
import com.harbor.ormlite.hello.HelloAndroid;
import com.harbor.ormlite.notifyservice.MyActivity;
import com.harbor.realm.RealmMainActivity;

@Test
@DIActivity
public class MainActivity extends AppCompatActivity {

    @DIVIew(R.id.sample_text)
    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        // Example of a call to a native method
//        TextView tv = (TextView) findViewById(R.id.sample_text);
        DIMainActivity.bindView(this);
        tv.setText("Hello Harbor二瓜");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, RealmMainActivity.class));
            return true;
        }else if(id == R.id.action_settings2){
//            startActivity(new Intent(this, HelloAndroid.class));
//            startActivity(new Intent(this, ClickConfig.class));
            startActivity(new Intent(this, MyActivity.class));
        }else if(id == R.id.action_settings3){
            startActivity(new Intent(this, NoteActivity.class));
        }else if(id == R.id.action_settings4){
            startActivity(new Intent(this, ReactiveNoteActivity.class));
        }else if(id == R.id.action_settings5){
            String url = "http://down068.tw11a.filemail.xunlei.com/httpdown?g=33AC84E0FB2015410BF66EB6398F02E53CF405EB&c=4669FF3464DC2513B75AF8D33185E1D258F21517&s=12763136&t=1506419762&ver=2382430211&tid=9295fd5a8371a926462e51bf02d07773&ui=209830049&e=1507024562&ms=10485760&ak=0:0:0:0&pk=filemail&aid=3bc014fd7a22f8e2286930e1ee95faf6";
//            url = "http://down065.tw11a.filemail.xunlei.com/httpdown?g=144C8FB0568370EA1ADB38E0E04A1EB15C6EE12F&c=68F9F68FF9D7ACFE6061398FD0E5FC2A8565E462&s=4612284&t=1506421481&ver=2382430211&tid=50abd4352ab66313b3b04160d691e4ce&ui=209830049&e=1507026281&ms=10485760&ak=0:0:0:0&pk=filemail&aid=dfca293f909c892b43b158172d6da59d";
            ClipboardManager cmb = (ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);
            cmb.setText(url);
            MediaPlayerActivity.startSelf(this, url);
        }

        return super.onOptionsItemSelected(item);
    }
}
