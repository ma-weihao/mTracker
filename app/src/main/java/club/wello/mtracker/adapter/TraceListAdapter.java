package club.wello.mtracker.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import club.wello.mtracker.R;
import club.wello.mtracker.apiUtil.TrackInfo;
import club.wello.mtracker.view.VerticalLineDotView;

/**
 * recyclerView adapter for traces list
 * Created by maweihao on 2017/10/11.
 */

public class TraceListAdapter extends RecyclerView.Adapter<TraceListAdapter.ViewHolder> {

    private static final String TAG = TraceListAdapter.class.getSimpleName();

    private Context context;
    private List<TrackInfo.TracesBean> tracesBeanList;

    public TraceListAdapter(Context context, List<TrackInfo.TracesBean> tracesBeanList) {
        this.context = context;
        this.tracesBeanList = tracesBeanList;
    }

    @Override
    public TraceListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_package_trace, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TraceListAdapter.ViewHolder holder, int position) {
        TrackInfo.TracesBean tracesBean = tracesBeanList.get(position);
        holder.traceInfo.setText(tracesBean.getAcceptStation());
        holder.time.setText(tracesBean.getAcceptTime());
        if (position == 0) {
            holder.verticalLineDotView.setSpecial(true);
            holder.verticalLineDotView.setUpDown(false, true);
        } else if (position == tracesBeanList.size() - 1) {
            holder.verticalLineDotView.setUpDown(true, false);
        }
    }

    @Override
    public int getItemCount() {
        return tracesBeanList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        VerticalLineDotView verticalLineDotView;
        TextView traceInfo;
        TextView time;

        public ViewHolder(View itemView) {
            super(itemView);
            verticalLineDotView = itemView.findViewById(R.id.line_dot_view);
            traceInfo = itemView.findViewById(R.id.tv_title_name);
            time = itemView.findViewById(R.id.tv_time);
        }
    }
}
