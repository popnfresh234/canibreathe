package com.dmtaiwan.alexander.canibreathe.Views;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.dmtaiwan.alexander.canibreathe.Models.AQStation;
import com.dmtaiwan.alexander.canibreathe.R;
import com.dmtaiwan.alexander.canibreathe.Utilities.AQDetailsAdapter;
import com.dmtaiwan.alexander.canibreathe.Utilities.DividerItemDecoration;
import com.dmtaiwan.alexander.canibreathe.Utilities.Utilities;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Alexander on 2/27/2016.
 */
public class AQStationDetailFragment extends Fragment{

    private static final String LOG_TAG = DetailActivity.class.getSimpleName();

    private AQStation mAQStation;
    private AQDetailsAdapter mAdapter;

    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    @Bind(R.id.collapsing_toolbar)
    CollapsingToolbarLayout mCollapsingToolbar;

    @Bind(R.id.collapsing_image)
    ImageView mCollapsingImage;

    @Bind(R.id.aq_detail_recycler_view)
    RecyclerView mRecyclerView;


    public static AQStationDetailFragment newInstance(int page, String title) {
        AQStationDetailFragment fragment = new AQStationDetailFragment();
        Bundle args = new Bundle();
        args.putInt("pageInt", page);
        args.putString("title", title);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        ButterKnife.bind(this, rootView);

        if (getArguments() != null) {
            mAQStation = getArguments().getParcelable(Utilities.EXTRA_AQ_STATION);
        }

        ((AppCompatActivity)getActivity()).setSupportActionBar(mToolbar);
        mCollapsingToolbar.setTitle(mAQStation.getSiteName());
        mCollapsingToolbar.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));
        int header = Utilities.getDetailHeader(Utilities.aqiCalc(mAQStation.getPM25()), getActivity());
        Glide.with(this).load(header).fitCenter().into(mCollapsingImage);

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(llm);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
        mAdapter = new AQDetailsAdapter(getActivity(), mAQStation);
        mRecyclerView.setAdapter(mAdapter);
        return rootView;
    }
}
