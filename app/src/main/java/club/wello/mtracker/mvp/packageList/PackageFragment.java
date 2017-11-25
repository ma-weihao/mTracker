package club.wello.mtracker.mvp.packageList;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import club.wello.mtracker.R;
import club.wello.mtracker.adapter.OnRecyclerViewItemClickListener;
import club.wello.mtracker.adapter.PackageListAdapter;
import club.wello.mtracker.apiUtil.TrackInfo;
import club.wello.mtracker.mvp.addPackage.AddActivity;
import club.wello.mtracker.mvp.packageDetail.PackageDetailActivity;

public class PackageFragment extends Fragment implements PackageContract.view {

    @BindView(R.id.rv_lists)
    RecyclerView recyclerView;
    @BindView(R.id.group_no_item)
    RelativeLayout noItemView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.srl)
    SwipeRefreshLayout swipeRefreshLayout;

    private static final String TAG = PackageFragment.class.getSimpleName();
    private SharedPreferences preferences;
    private PackageListAdapter adapter;
    private PackageContract.presenter presenter;

    public PackageFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_package, container, false);
        initView(view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        presenter.loadPackages();
        setHasOptionsMenu(true);
        return view;
    }

    private void initView(View view) {
        ButterKnife.bind(this, view);
        ((MainActivity)getActivity()).setSupportActionBar(toolbar);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.refreshPackages();
            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), AddActivity.class));
            }
        });
    }

    @Override
    public void setLoadingIndicator(final boolean active) {
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(active);
            }
        });
    }

    @Override
    @MainThread
    public void showEmptyView(boolean toShow) {
        setLoadingIndicator(false);
        swipeRefreshLayout.setVisibility(toShow ? View.INVISIBLE : View.VISIBLE);
        noItemView.setVisibility(toShow ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    public void showPackages(@NonNull final List<TrackInfo> list) {
        if (adapter == null) {
            adapter = new PackageListAdapter(getContext(), list);
            adapter.setListener(new OnRecyclerViewItemClickListener() {
                @Override
                public void OnItemClick(View view, int position) {
                    Intent intent = new Intent(getContext(), PackageDetailActivity.class);
                    intent.putExtra(PackageDetailActivity.PACKAGE_ID, list.get(position).getLogisticCode());
                    startActivity(intent);
                }
            });
            recyclerView.setAdapter(adapter);
        } else {
            adapter.updateData(list);
        }
        showEmptyView(list.isEmpty());
    }

    @Override
    public void showPackageRemovedMsg(String packageName) {
        String msg = packageName
                + " "
                + getString(R.string.package_removed_msg);
        Snackbar.make(fab, msg, Snackbar.LENGTH_LONG)
                .setAction(R.string.undo, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        presenter.recoverPackage();
                    }
                })
                .show();
    }

    @Override
    public void copyPackageNumber() {
        // TODO: 2017/10/22
    }

    @Override
    public void showNetworkError() {
        Snackbar.make(fab, R.string.network_error, Snackbar.LENGTH_SHORT)
                .setAction(R.string.go_to_settings, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent().setAction(Settings.ACTION_SETTINGS));
                    }
                })
                .show();
    }

    @Override
    public void showError(String message) {
        if (message == null) {
            message = getResources().getString(R.string.error);
        }
        Snackbar.make(fab, message, Snackbar.LENGTH_SHORT)
                .setAction(R.string.go_to_settings, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent().setAction(Settings.ACTION_SETTINGS));
                    }
                })
                .show();
    }

    @Override
    public void setPresenter(PackageContract.presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (presenter != null) {
            presenter.subscribe();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.unSubscribe();
        setLoadingIndicator(false);
    }

    @Override
    public void onAttachFragment(Fragment childFragment) {
        super.onAttachFragment(childFragment);
    }
}
