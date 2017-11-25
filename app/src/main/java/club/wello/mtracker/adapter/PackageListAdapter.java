package club.wello.mtracker.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import club.wello.mtracker.R;
import club.wello.mtracker.apiUtil.TrackInfo;

/**
 * recyclerView adapter for packages list
 * Created by maweihao on 2017/10/10.
 */

public class PackageListAdapter extends RecyclerView.Adapter<PackageListAdapter.ViewHolder> {

    private static final String TAG = PackageListAdapter.class.getSimpleName();

    private ArrayList<TrackInfo> trackInfoArrayList;
    private Context context;
    private Gson gson;
    private OnRecyclerViewItemClickListener listener;

    public PackageListAdapter(Context context, List<TrackInfo> trackInfoLinkedList) {
        this.context = context;
        this.trackInfoArrayList = (ArrayList<TrackInfo>) trackInfoLinkedList;
        gson = new Gson();
    }

    public void setListener(OnRecyclerViewItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_package_list, parent, false);
        return new ViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final TrackInfo trackInfo = trackInfoArrayList.get(position);
        final String title = trackInfo.getTitle();
        holder.bigTitle.setText(TextUtils.isEmpty(title) ? "" : title.substring(0, 1));
        holder.titleName.setText(TextUtils.isEmpty(title) ? "" : title);
        List<TrackInfo.TracesBean> list = trackInfo.getTraces();
        if (list != null && list.size() > 0) {
            holder.traceInfo.setText(trackInfo.getTraces().get(0).getAcceptStation());
            holder.time.setText(trackInfo.getTraces().get(0).getAcceptTime());
        } else {
            holder.traceInfo.setText("Unknown places");
            holder.time.setText("now");
        }

    }

    @Override
    public int getItemCount() {
        return trackInfoArrayList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView bigTitle;
        TextView titleName;
        TextView traceInfo;
        TextView time;
        CardView cardView;

        private OnRecyclerViewItemClickListener listener;

        public ViewHolder(View itemView, OnRecyclerViewItemClickListener listener) {
            super(itemView);
            bigTitle = itemView.findViewById(R.id.tv_big_title);
            titleName = itemView.findViewById(R.id.tv_title_name);
            traceInfo = itemView.findViewById(R.id.tv_trace_info);
            time = itemView.findViewById(R.id.tv_time);
            cardView = itemView.findViewById(R.id.cv);
            this.listener = listener;

            cardView.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {
            if (this.listener != null) {
                listener.OnItemClick(view, getLayoutPosition());
            }
        }
    }

    public void updateData(@NonNull List<TrackInfo> list) {
        trackInfoArrayList.clear();
        trackInfoArrayList.addAll(list);
        notifyDataSetChanged();
    }
}
