package com.dmtaiwan.alexander.canibreathe.Utilities;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dmtaiwan.alexander.canibreathe.Models.AQStation;
import com.dmtaiwan.alexander.canibreathe.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.view.ColumnChartView;

/**
 * Created by Alexander on 11/13/2015.
 */
public class AQDetailsAdapter extends RecyclerView.Adapter<AQDetailsAdapter.ViewHolder> {

    private Context mContext;
    private AQStation mAQStation;
    private ColumnChartData mChartData;

    public AQDetailsAdapter(Context context, AQStation aqStation) {
        this.mContext = context;
        this.mAQStation = aqStation;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        if (viewType == 0) {
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_detail, parent, false);
            return new ViewHolder(itemView);
        } else {
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_chart, parent, false);
            return new ViewHolder(itemView);
        }



    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (position != 4) {
            holder.mTitle.setText(Utilities.getAQDetailTitle(position, mContext));
            holder.mData.setText(Utilities.getAqData(position, mAQStation));
            holder.mIcon.setImageResource(Utilities.getAqIcon(position, mContext));
        }
        if (position == 1) {
            float angle = Utilities.getWindDegreeForRotate(mAQStation.getWindDirec());
            holder.mIcon.setRotation(angle);
        }
        if (position == 4) {
            if (mChartData != null) {
                holder.mChart.setColumnChartData(mChartData);
            }
        }
    }

    @Override
    public int getItemCount() {
        return 5;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 4) {
            return 1;
        } else {
            return 0;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @Nullable
        @Bind(R.id.aq_detail_title)
        TextView mTitle;

        @Nullable
        @Bind(R.id.aq_detail_data)
        TextView mData;

        @Nullable
        @Bind(R.id.aq_detail_icon)
        ImageView mIcon;

        @Nullable
        @Bind(R.id.columnChart)
        ColumnChartView mChart;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public void setChartData(ColumnChartData chartData) {
        mChartData = chartData;
        notifyDataSetChanged();
    }
}
