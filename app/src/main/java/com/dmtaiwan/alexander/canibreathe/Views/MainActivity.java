package com.dmtaiwan.alexander.canibreathe.Views;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.dmtaiwan.alexander.canibreathe.Models.AQStation;
import com.dmtaiwan.alexander.canibreathe.Presenters.MainPresenter;
import com.dmtaiwan.alexander.canibreathe.Presenters.MainPresenterImpl;
import com.dmtaiwan.alexander.canibreathe.R;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements MainView{

    private MainPresenter mPresenter;
    private List<AQStation> mAQStationList;

    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    @Bind(R.id.fab)
    FloatingActionButton mFab;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        mPresenter = new MainPresenterImpl(this, this);
        mPresenter.requestAQData();
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
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDataReturned(List<AQStation> aqStations) {
        mAQStationList = aqStations;
        for (int i = 0; i < mAQStationList.size(); i++) {
            AQStation aqStation = mAQStationList.get(i);
            String county = PreferenceManager.getDefaultSharedPreferences(this).getString(getString(R.string.pref_key_county), getString(R.string.pref_county_taipei_city));
            if (aqStation.getCounty().equals(county)) {
                Log.i("SITE NAME", aqStation.getSiteName());
            }

        }
    }
}
