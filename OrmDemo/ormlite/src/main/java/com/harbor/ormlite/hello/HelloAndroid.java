package com.harbor.ormlite.hello;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.widget.TextView;

import com.j256.ormlite.android.apptools.OrmLiteBaseActivity;
import com.j256.ormlite.dao.RuntimeExceptionDao;

import java.util.List;
import java.util.Random;

/**
 * Created by Harbor on 2017/7/6.
 */

public class HelloAndroid extends OrmLiteBaseActivity<DatabaseHelper>{

    private final String LOG_TAG = getClass().getSimpleName();
    private final static int MAX_NUM_TO_CREATE = 8;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(LOG_TAG, "creating " + getClass() + " at " + System.currentTimeMillis());
        TextView tv = new TextView(this);
        tv.setMovementMethod(new ScrollingMovementMethod());
        doSampleDatabaseStuff("onCreate", tv);
        setContentView(tv);
    }

    /**
     * Do our sample database stuff.
     */
    private void doSampleDatabaseStuff(String action, TextView tv){
        RuntimeExceptionDao<SimpleData, Integer> simpleDao = getHelper().getSimpleDataDao();
        List<SimpleData> list = simpleDao.queryForAll();
        StringBuilder sb = new StringBuilder();
        sb.append("Found ").append(list.size()).append(" entries in DB in ").append(action).append("()\n");

        // if we already have items in the database
        int simpleC = 1;
        for (SimpleData simple : list) {
            sb.append('#').append(simpleC).append(": ").append(simple).append('\n');
            simpleC++;
        }
        sb.append("------------------------------------------\n");
        sb.append("Deleted ids:");
        for (SimpleData simple : list) {
            simpleDao.delete(simple);
            sb.append(' ').append(simple.id);
            Log.i(LOG_TAG, "deleting simple(" + simple.id + ")");
            simpleC++;
        }
        sb.append('\n');
        sb.append("------------------------------------------\n");

        int createNum;
        do {
            createNum = new Random().nextInt(MAX_NUM_TO_CREATE) + 1;
        } while (createNum == list.size());
        sb.append("Creating ").append(createNum).append(" new entries:\n");
        for (int i = 0; i < createNum; i++) {
            // create a new simple object
            long millis = System.currentTimeMillis();
            SimpleData simple = new SimpleData(millis);
            // store it in the database
            simpleDao.create(simple);
            Log.i(LOG_TAG, "created simple(" + millis + ")");
            // output it
            sb.append('#').append(i + 1).append(": ");
            sb.append(simple).append('\n');
            try {
                Thread.sleep(5);
            } catch (InterruptedException e) {
                // ignore
            }
        }

        tv.setText(sb.toString());
        Log.i(LOG_TAG, "Done with page at " + System.currentTimeMillis());
    }
}
