package club.wello.mtracker.mvp.addPackage;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import club.wello.mtracker.R;
import club.wello.mtracker.zxing.CaptureActivity;

import static android.app.Activity.RESULT_OK;
import static club.wello.mtracker.util.Constants.SCANNING_REQUEST_CODE;

public class AddPackageFragment extends Fragment implements AddPackageContract.View{

    @BindView(R.id.fb_add)
    FloatingActionButton addButton;
    @BindView(R.id.et_number)
    TextInputEditText numberEditText;
    @BindView(R.id.et_title)
    TextInputEditText titleEditText;
    @BindView(R.id.ib_scan)
    ImageButton scanIV;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.ns)
    NestedScrollView scrollView;

    public final static int REQUEST_CAMERA_PERMISSION_CODE = 0;

    private static final String TAG = AddPackageFragment.class.getSimpleName();
    private AddPackageContract.Presenter presenter;

    public AddPackageFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add, container, false);
        ButterKnife.bind(this, view);
        initView();

        addLayoutListener(scrollView, titleEditText);
        setHasOptionsMenu(true);
        return view;
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
    }

    private void initView() {
        scanIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkPermissionOrToScan();
            }
        });
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideImm();
                String number = numberEditText.getText().toString();
                String title = titleEditText.getText().toString();
                if (!checkNumberFormat(number)) {
                    numberEditText.setError(getResources().getString(R.string.number_format_wrong));
                    return;
                }
                presenter.savePackage(number, title);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            getActivity().onBackPressed();
        }
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case SCANNING_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            String num = data.getExtras().getString("result");
                            if (checkNumberFormat(num)) {
                                numberEditText.setText(num);
                                addButton.callOnClick();
                            } else {
                                numberEditText.setError(getResources().getString(R.string.number_format_wrong));
                            }
                        }
                    });
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CAMERA_PERMISSION_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    scanCode();
                } else {
                    hideImm();
                    AlertDialog dialog = new AlertDialog.Builder(getContext())
                            .setTitle(R.string.permission_denied)
                            .setMessage(R.string.require_permission)
                            .setPositiveButton(R.string.go_to_settings, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent();
                                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                    Uri uri = Uri.fromParts("package", getContext().getPackageName(), null);
                                    intent.setData(uri);
                                    startActivity(intent);
                                    dialog.dismiss();
                                }
                            })
                            .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .create();
                    dialog.show();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void showNumberExistError() {
        Snackbar.make(addButton, R.string.number_exist_error, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void showNumberError() {
        Snackbar.make(addButton, R.string.wrong_number_and_check, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void setProgressIndicator(boolean loading) {
        if (loading) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void showPackagesList() {
        getActivity().setResult(Activity.RESULT_OK);
        getActivity().finish();
    }

    @Override
    public void showNetworkError() {
        Snackbar.make(addButton, R.string.network_error, Snackbar.LENGTH_LONG)
                .setAction(R.string.go_to_settings, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent().setAction(Settings.ACTION_SETTINGS));
                    }
                })
                .show();
    }

    @Override
    public void setPresenter(AddPackageContract.Presenter presenter) {
        this.presenter = presenter;
    }

    private void addLayoutListener(final View main, final View scroll) {
        main.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect rect = new Rect();
                main.getWindowVisibleDisplayFrame(rect);
                int mainInvisibleHeight = main.getRootView().getHeight() - rect.bottom;
                if (mainInvisibleHeight > 150) {
                    int[] location = new int[2];
                    scroll.getLocationInWindow(location);
                    int scrollHeight = (location[1] + scroll.getHeight()) - rect.bottom;
                    main.scrollTo(0, scrollHeight);
                } else {
                    main.scrollTo(0, 0);
                }
            }
        });
    }

    /**
     * hide the keyboard
     */
    private void hideImm() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null && imm.isActive()) {
            imm.hideSoftInputFromWindow(addButton.getWindowToken(), 0);
        }
    }

    private void scanCode() {
        Intent intent = new Intent(getActivity(), CaptureActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivityForResult(intent, SCANNING_REQUEST_CODE);
    }

    private void checkPermissionOrToScan() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[] { Manifest.permission.CAMERA }, REQUEST_CAMERA_PERMISSION_CODE);
        } else {
            scanCode();
        }
    }

    private boolean checkNumberFormat(String number) {
        if (number.length() < 5 || number.replace(" ", "").isEmpty()) {
            showNumberError();
            return false;
        }
        for (char c : number.toCharArray()) {
            if (!Character.isLetterOrDigit(c)) {
                showNumberError();
                return false;
            }
        }
        return true;
    }

}
