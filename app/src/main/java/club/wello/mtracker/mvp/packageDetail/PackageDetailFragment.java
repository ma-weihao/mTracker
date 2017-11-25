package club.wello.mtracker.mvp.packageDetail;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import club.wello.mtracker.R;
import club.wello.mtracker.adapter.TraceListAdapter;
import club.wello.mtracker.apiUtil.TrackInfo;
import club.wello.mtracker.util.Utility;

public class PackageDetailFragment extends Fragment implements PackageDetailContract.View {

    private static final String TAG = PackageDetailFragment.class.getSimpleName();

    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.tv_package_name)
    TextView nameText;
    @BindView(R.id.tv_state)
    TextView stateText;
    @BindView(R.id.tv_number)
    TextView numberText;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private TraceListAdapter adapter;
    private PackageDetailContract.Presenter presenter;

    public PackageDetailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_package_detail, container, false);
        initView(view);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.subscribe();
    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.unSubscribe();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.package_details, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home:
                exit();
                break;
            case R.id.action_delete:
                presenter.deletePackage();
                break;
            case R.id.action_rename:
                rename();
                break;
            case R.id.action_unread:
                presenter.setPackageUnread();
                break;
            default:
                break;
        }
        return true;
    }

    private void rename() {
        AlertDialog dialog = new AlertDialog.Builder(getContext()).create();
        dialog.setTitle(getString(R.string.edit_name));

        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_edit_package_name, null);
        final AppCompatEditText editText = view.findViewById(R.id.editTextName);
        editText.setText(presenter.getPackageName());
        dialog.setView(view);

        dialog.setButton(DialogInterface.BUTTON_POSITIVE, getString(android.R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String input = editText.getText().toString();
                if (input.isEmpty()) {
                    Snackbar.make(fab, R.string.input_empty, Snackbar.LENGTH_SHORT).show();
                } else {
                    presenter.updatePackageName(input);
                }
                dialog.dismiss();
            }
        });
        dialog.setButton(DialogInterface.BUTTON_NEGATIVE, getString(android.R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void initView(View view) {
        ButterKnife.bind(this, view);
        ((PackageDetailActivity)getActivity()).setSupportActionBar(toolbar);
        ((PackageDetailActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.shareTo();
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.refreshPackage();
            }
        });
    }

    @Override
    public void setPresenter(PackageDetailContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void setLoadingIndicator(final boolean loading) {
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(loading);
            }
        });
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
    public void showPackageDetails(@NonNull TrackInfo trackInfo) {
        List<TrackInfo.TracesBean> list = trackInfo.getTraces();
        Log.d(TAG, "showPackageDetails: " + (list == null));
        nameText.setText(trackInfo.getTitle());
        numberText.setText(trackInfo.getLogisticCode());
        stateText.setText(Utility.parseState(getContext(), Integer.parseInt(trackInfo.getState())));
        if (adapter == null) {
            adapter = new TraceListAdapter(getContext(), list);
            recyclerView.setAdapter(adapter);
        } else {
            adapter.updateData(list);
        }
    }

    @Override
    public void copyPackageNumber(@NonNull String packageId) {
        ClipboardManager manager = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData data = ClipData.newPlainText("text", packageId);
        if (manager != null) {
            manager.setPrimaryClip(data);
            Snackbar.make(fab, R.string.package_number_copied, Snackbar.LENGTH_SHORT).show();
        }
    }

    @Override
    public void exit() {
        getActivity().onBackPressed();
    }
}
