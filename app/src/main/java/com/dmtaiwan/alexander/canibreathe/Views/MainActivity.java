package com.dmtaiwan.alexander.canibreathe.Views;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
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
    private FragmentPagerAdapter mPagerAdapter;



    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    @Bind(R.id.toolbar_progress)
    ProgressBar mProgressBar;

    @Bind(R.id.coordinator_layout)
    CoordinatorLayout mCoordinatorLayout;

    @Bind(R.id.tab_layout)
    TabLayout mTabLayout;

    @Bind(R.id.view_pager)
    ViewPager mViewPager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        EventBus.getInstance().register(this);


        mPagerAdapter = new PagerAdapter(getSupportFragmentManager(), this);
        mViewPager.setAdapter(mPagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);


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
        AQStationListFragment fragment = (AQStationListFragment) mPagerAdapter.instantiateItem(mViewPager, 0);
        AQStationListFragment secondaryFragment = (AQStationListFragment) mPagerAdapter.instantiateItem(mViewPager, 1);
        fragment.setData(aqStationList);
        secondaryFragment.setData(aqStationList);

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

            //Update tabs
            TabLayout.Tab tab0 = mTabLayout.getTabAt(0);
            TabLayout.Tab tab1 = mTabLayout.getTabAt(1);
            tab0.setText(Utilities.getTabTitle(this));
            tab1.setText(PreferenceManager.getDefaultSharedPreferences(this).getString(this.getString(R.string.pref_key_secondary_county), this.getString(R.string.pref_county_taipei_city)));
        }
    }


    @Override
    public void onRecyclerClick(AQStation aqStation) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra(Utilities.EXTRA_AQ_STATION, aqStation);
        startActivity(intent);
    }

    public static class PagerAdapter extends FragmentPagerAdapter {
        private static int NUM_ITEMS = 2;
        private Context mContext;

        public PagerAdapter(FragmentManager fragmentManager, Context context) {
            super(fragmentManager);
            mContext = context;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    //Favorites fragment
                    return AQStationListFragment.newInstance(0, "Title 1");
                case 1:
                    return AQStationListFragment.newInstance(1, "Title 2");
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return NUM_ITEMS;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return Utilities.getTabTitle(mContext);
                case 1:
                    return PreferenceManager.getDefaultSharedPreferences(mContext).getString(mContext.getString(R.string.pref_key_secondary_county), mContext.getString(R.string.pref_county_taipei_city));
                default:
                    return null;
            }
        }

    }
}
