package com.harbor.db;

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

import com.harbor.greendao.NoteActivity;
import com.harbor.objectbox.ReactiveNoteActivity;
import com.harbor.ormlite.clickcounter.ClickConfig;
import com.harbor.ormlite.hello.HelloAndroid;
import com.harbor.ormlite.notifyservice.MyActivity;
import com.harbor.realm.RealmMainActivity;

public class MainActivity extends AppCompatActivity {

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
        TextView tv = (TextView) findViewById(R.id.sample_text);
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
        }

        return super.onOptionsItemSelected(item);
    }
}
