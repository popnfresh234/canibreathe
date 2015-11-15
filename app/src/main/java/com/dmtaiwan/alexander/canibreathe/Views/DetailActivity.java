package com.dmtaiwan.alexander.canibreathe.Views;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.dmtaiwan.alexander.canibreathe.Models.AQStation;
import com.dmtaiwan.alexander.canibreathe.Presenters.DetailPresenterImpl;
import com.dmtaiwan.alexander.canibreathe.R;
import com.dmtaiwan.alexander.canibreathe.Utilities.AQDetailsAdapter;
import com.dmtaiwan.alexander.canibreathe.Utilities.DividerItemDecoration;
import com.dmtaiwan.alexander.canibreathe.Utilities.Utilities;

import butterknife.Bind;
import butterknife.ButterKnife;
import lecho.lib.hellocharts.model.ColumnChartData;

/**
 * Created by Alexander on 11/13/2015.
 */
public class DetailActivity extends AppCompatActivity implements DetailView{
    private static final String LOG_TAG = DetailActivity.class.getSimpleName();

    private AQStation mAQStation;
    private AQDetailsAdapter mAdapter;
    private DetailPresenterImpl mPresenter;

    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    @Bind(R.id.collapsing_toolbar)
    CollapsingToolbarLayout mCollapsingToolbar;

    @Bind(R.id.collapsing_image)
    ImageView mCollapsingImage;

    @Bind(R.id.aq_detail_recycler_view)
    RecyclerView mRecyclerView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        if (getIntent() != null) {
            mAQStation = getIntent().getParcelableExtra(Utilities.EXTRA_AQ_STATION);
        }

        setSupportActionBar(mToolbar);
        mCollapsingToolbar.setTitle(mAQStation.getSiteName());
        mCollapsingToolbar.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));
        int header = Utilities.getDetailHeader(Utilities.aqiCalc(mAQStation.getPM25()), this);
        Glide.with(this).load(header).fitCenter().into(mCollapsingImage);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(llm);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        mAdapter = new AQDetailsAdapter(this, mAQStation);
        mRecyclerView.setAdapter(mAdapter);

        //Create presenter
        mPresenter = new DetailPresenterImpl(this, this);
        if (mPresenter != null) {
            mPresenter.requestParseData(mAQStation.getSiteName());
        }
    }



    @Override
    public void showLoading() {

    }

    @Override
    public void onDataReturned(ColumnChartData chartData) {
        Log.i(LOG_TAG, "dataReturned");
        mAdapter.setChartData(chartData);
    }
}
