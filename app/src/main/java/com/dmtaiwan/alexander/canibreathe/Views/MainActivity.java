package com.dmtaiwan.alexander.canibreathe.Views;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

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

    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    private MainPresenter mPresenter;
    private List<AQStation> mAQStationList;
    private AQStationAdapter mAdapter;

    @Bind(R.id.empty_view)
    View mEmptyView;

    @Bind(R.id.aq_recycler_view)
    RecyclerView mRecyclerView;

    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    @Bind(R.id.toolbar_progress)
    ProgressBar mProgressBar;

    @Bind(R.id.coordinator_layout)
    CoordinatorLayout mCoordinatorLayout;



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
        if (mPresenter != null) {
            mPresenter.requestAQData();
            Log.i(LOG_TAG, "onCreate update");
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

        if (id == R.id.action_refresh) {
            mPresenter.requestAQData();
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
        mProgressBar.setVisibility(View.INVISIBLE);

    }

    @Override
    public void onErrorReturned(String errorMessage) {
        mProgressBar.setVisibility(View.INVISIBLE);
        Snackbar snackbar = Snackbar.make(mCoordinatorLayout, errorMessage, Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    @Override
    public void onNetworkDataSuccess() {
        Snackbar snackbar = Snackbar.make(mCoordinatorLayout, getString(R.string.update_success), Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    @Override
    public void showProgressBar() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressBar() {
        mProgressBar.setVisibility(View.INVISIBLE);
    }

    @Subscribe
    public void onSettingsChanged(SettingsEvent event) {
        if (mPresenter != null) {
            mPresenter.requestCountyChange();
        }
    }


    @Override
    public void onRecyclerClick(AQStation aqStation) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra(Utilities.EXTRA_AQ_STATION, aqStation);
        startActivity(intent);
    }
}
