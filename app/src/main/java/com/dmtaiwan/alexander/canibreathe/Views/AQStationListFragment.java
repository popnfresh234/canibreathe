package com.dmtaiwan.alexander.canibreathe.Views;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dmtaiwan.alexander.canibreathe.Models.AQStation;
import com.dmtaiwan.alexander.canibreathe.R;
import com.dmtaiwan.alexander.canibreathe.Utilities.AQStationAdapter;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Alexander on 1/19/2016.
 */
public class AQStationListFragment extends Fragment {

    private AQStationAdapter mAdapter;

    @Bind(R.id.empty_view)
    View mEmptyView;

    @Bind(R.id.aq_recycler_view)
    RecyclerView mRecyclerView;

    public static AQStationListFragment newInstance(int page, String title) {
        AQStationListFragment fragment = new AQStationListFragment();
        Bundle args = new Bundle();
        args.putInt("pageInt", page);
        args.putString("title", title);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, rootView);
        //Get page number
        Bundle args = getArguments();
        int page = args.getInt("pageInt");
        //        Set Layout Manager
        mAdapter = new AQStationAdapter(getActivity(), mEmptyView, (MainActivity)getActivity(), page);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(llm);

        mRecyclerView.setAdapter(mAdapter);
        return rootView;
    }

    public void setData(List<AQStation> aqStationList) {
        if (mAdapter != null) {
            mAdapter.udpateData(aqStationList);
        }

    }
}