package com.dmtaiwan.alexander.canibreathe.Utilities;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dmtaiwan.alexander.canibreathe.Models.AQStation;
import com.dmtaiwan.alexander.canibreathe.R;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Alexander on 11/12/2015.
 */
public class AQStationAdapter extends RecyclerView.Adapter<AQStationAdapter.ViewHolder> {

    private static final String LOG_TAG = AQStationAdapter.class.getSimpleName();
    private Context mContext;
    final private View mEmptyView;
    private List<AQStation> mStationList;

    public AQStationAdapter(Context context, View emptyView) {
        this.mContext = context;
        this.mEmptyView = emptyView;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_aqstation, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        AQStation aqStation = mStationList.get(position);
        holder.mStationImage.setImageResource(R.drawable.aq_poor);
        holder.mStationName.setText(aqStation.getSiteName());
        holder.mAqi.setText(aqStation.getPM25());
    }

    @Override
    public int getItemCount() {
        if (mStationList != null) {
            return mStationList.size();
        }else {
            return 0;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @Bind(R.id.aq_station_background_image)
        ImageView mStationImage;

        @Bind(R.id.aq_station_name)
        TextView mStationName;

        @Bind(R.id.aq_pm25)
        TextView mAqi;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

        }
    }

    public void udpateData(List<AQStation> stationList) {
        Log.i(LOG_TAG, "updating data");
        mStationList = stationList;
        notifyDataSetChanged();
        mEmptyView.setVisibility(mStationList.size() == 0 ? View.VISIBLE : View.GONE);
    }
}
