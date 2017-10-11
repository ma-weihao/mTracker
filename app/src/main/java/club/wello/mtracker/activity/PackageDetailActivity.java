package club.wello.mtracker.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import club.wello.mtracker.R;
import club.wello.mtracker.adapter.TraceListAdapter;
import club.wello.mtracker.apiUtil.TrackInfo;

import static club.wello.mtracker.apiUtil.Constants.package_title;
import static club.wello.mtracker.apiUtil.Constants.trace_info;

public class PackageDetailActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.rv_traces)
    RecyclerView recyclerView;

    private static final String TAG = PackageDetailActivity.class.getSimpleName();
    private List<TrackInfo.TracesBean> tracesBeanList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_package_detail);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        initData();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    private void initData() {
        Intent intent = getIntent();
        String title = intent.getStringExtra(package_title);
        String traceInfo = intent.getStringExtra(trace_info);
        toolbar.setTitle(title);
        Gson gson = new Gson();
        tracesBeanList = gson.fromJson(traceInfo, new TypeToken<List<TrackInfo.TracesBean>>(){}.getType());

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        TraceListAdapter traceListAdapter = new TraceListAdapter(this, tracesBeanList);
        recyclerView.setAdapter(traceListAdapter);
    }
}
