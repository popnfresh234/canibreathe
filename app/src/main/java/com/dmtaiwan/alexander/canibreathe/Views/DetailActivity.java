package com.dmtaiwan.alexander.canibreathe.Views;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.dmtaiwan.alexander.canibreathe.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Alexander on 11/13/2015.
 */
public class DetailActivity extends AppCompatActivity {
    private static final String LOG_TAG = DetailActivity.class.getSimpleName();

    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    @Bind(R.id.collapsing_toolbar)
    CollapsingToolbarLayout mCollapsingToolbar;

    @Bind(R.id.collapsing_image)
    ImageView mCollapsingImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        mCollapsingToolbar.setTitle("TEST");
        mCollapsingToolbar.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));
        Glide.with(this).load(R.drawable.aq_good).fitCenter().into(mCollapsingImage);
    }

}
