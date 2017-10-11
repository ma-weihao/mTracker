package club.wello.mtracker.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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

    private ArrayList<TrackInfo> trackInfoLinkedList;
    private Context context;

    public PackageListAdapter(Context context, List<TrackInfo> trackInfoLinkedList) {
        this.context = context;
        this.trackInfoLinkedList = (ArrayList<TrackInfo>) trackInfoLinkedList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_package_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        TrackInfo trackInfo = trackInfoLinkedList.get(position);
        String title = trackInfo.getTitle();
        holder.bigTitle.setText(TextUtils.isEmpty(title) ? "" : title.substring(0, 1));
        holder.titleName.setText(TextUtils.isEmpty(title) ? "" : title);
        holder.traceInfo.setText(trackInfo.getTraces().get(0).getAcceptStation());
        holder.time.setText(trackInfo.getTraces().get(0).getAcceptTime());
    }

    @Override
    public int getItemCount() {
        return trackInfoLinkedList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView bigTitle;
        TextView titleName;
        TextView traceInfo;
        TextView time;

        public ViewHolder(View itemView) {
            super(itemView);
            bigTitle = itemView.findViewById(R.id.tv_big_title);
            titleName = itemView.findViewById(R.id.tv_title_name);
            traceInfo = itemView.findViewById(R.id.tv_trace_info);
            time = itemView.findViewById(R.id.tv_time);
        }
    }
}
