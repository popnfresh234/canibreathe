package com.dmtaiwan.alexander.canibreathe.Views;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.dmtaiwan.alexander.canibreathe.Bus.EventBus;
import com.dmtaiwan.alexander.canibreathe.Bus.SettingsEvent;
import com.dmtaiwan.alexander.canibreathe.Models.AQStation;
import com.dmtaiwan.alexander.canibreathe.Presenters.MainPresenter;
import com.dmtaiwan.alexander.canibreathe.Presenters.MainPresenterImpl;
import com.dmtaiwan.alexander.canibreathe.R;
import com.dmtaiwan.alexander.canibreathe.Utilities.AQStationAdapter;
import com.dmtaiwan.alexander.canibreathe.Utilities.Utilities;
import com.squareup.otto.Subscribe;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements MainView, AQStationAdapter.RecyclerClickListener{

    private MainPresenter mPresenter;
    private List<AQStation> mAQStationList;
    private AQStationAdapter mAdapter;

    @Bind(R.id.empty_view)
    View mEmptyView;

    @Bind(R.id.aq_recycler_view)
    RecyclerView mRecyclerView;

    @Bind(R.id.toolbar)
    Toolbar mToolbar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        EventBus.getInstance().register(this);
        //Set Layout Manager
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(llm);
        mAdapter = new AQStationAdapter(this, mEmptyView, this);
        mRecyclerView.setAdapter(mAdapter);

        //ActionBar
        setSupportActionBar(mToolbar);

        mPresenter = new MainPresenterImpl(this, this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mPresenter != null) {
            mPresenter.requestAQData();
        }
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
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getInstance().unregister(this);
    }

    @Override
    public void onDataReturned(List<AQStation> aqStationList) {
        if (mAdapter != null) {
            mAdapter.udpateData(aqStationList);
        }
    }

    @Subscribe
    public void onSettingsChanged(SettingsEvent event) {
        if (mPresenter != null) {
            mPresenter.requestAQData();
        }
    }


    @Override
    public void onRecyclerClick(AQStation aqStation) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra(Utilities.EXTRA_AQ_STATION, aqStation);
        startActivity(intent);
    }
}
