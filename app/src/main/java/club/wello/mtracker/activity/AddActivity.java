package club.wello.mtracker.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import club.wello.mtracker.R;
import club.wello.mtracker.apiUtil.DaoMaster;
import club.wello.mtracker.apiUtil.DaoSession;
import club.wello.mtracker.apiUtil.KdniaoUtil;
import club.wello.mtracker.apiUtil.TrackCompanyInfo;
import club.wello.mtracker.apiUtil.TrackInfo;
import club.wello.mtracker.apiUtil.TrackInfoDao;
import club.wello.mtracker.util.HttpUtil;
import club.wello.mtracker.zxing.CaptureActivity;
import io.reactivex.functions.Consumer;

import static club.wello.mtracker.apiUtil.Constants.SCANNING_REQUEST_CODE;
import static club.wello.mtracker.apiUtil.Constants.package_title;
import static club.wello.mtracker.apiUtil.Constants.trace_info;

public class AddActivity extends AppCompatActivity {

    private static final String TAG = AddActivity.class.getSimpleName();
    private TrackInfoDao trackInfoDao;
    private Gson gson;

    @BindView(R.id.fb_add)
    FloatingActionButton floatingActionButton;
    @BindView(R.id.tv_number)
    EditText numberEditText;
    @BindView(R.id.tv_title)
    EditText titleEditText;
    @BindView(R.id.ib_scan)
    ImageButton scanIV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        ButterKnife.bind(this);

        initData();
        initView();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case SCANNING_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    final Bundle bundle = data.getExtras();
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            numberEditText.setText(titleEditText.getText().toString());
                            getCompany(bundle.getString("result"), titleEditText.getText().toString());
                        }
                    });

                }
                break;
            default:
                break;
        }
    }

    private void initData() {
        DaoMaster.DevOpenHelper devOpenHelper = new DaoMaster.DevOpenHelper(AddActivity.this, "trace.db", null);
        DaoMaster daoMaster = new DaoMaster(devOpenHelper.getWritableDb());
        DaoSession daoSession = daoMaster.newSession();
        trackInfoDao = daoSession.getTrackInfoDao();
    }

    private void initView() {
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String number = numberEditText.getText().toString();
                String title = titleEditText.getText().toString();
                getCompany(number, title);
            }
        });
        scanIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddActivity.this, CaptureActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivityForResult(intent, SCANNING_REQUEST_CODE);
            }
        });
    }

    private void getCompany(final String number, final String title) {
        HttpUtil.getTrackCompanyInfo(KdniaoUtil.getOrderTracesByJson(number)).subscribe(new Consumer<TrackCompanyInfo>() {
            @Override
            public void accept(TrackCompanyInfo trackCompanyInfo) throws Exception {
                if (trackCompanyInfo.isSuccess()) {
                    String newTitle = title;
                    if (TextUtils.isEmpty(title)) {
                        newTitle = trackCompanyInfo.getShippers().get(0).getShipperName() + number.substring(number.length() - 2);
                    }
                    getTrace(trackCompanyInfo.getShippers().get(0).getShipperCode(), number, newTitle);
                } else {
                    Toast.makeText(AddActivity.this, "cannot recognize company", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "accept: get company failed" + trackCompanyInfo.getReason());
                }
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                Toast.makeText(AddActivity.this, "network error", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "onError: get company failed");
            }
        });
    }

    private void getTrace(String code, String number, final String title) {
        HttpUtil.getTrackInfo(KdniaoUtil.getOrderTracesByJson(code, number)).subscribe(new Consumer<TrackInfo>() {
            @Override
            public void accept(TrackInfo trackInfo) throws Exception {
                if (trackInfo.isSuccess()) {
                    gson = new Gson();
                    List<TrackInfo.TracesBean> tracesBeanList = trackInfo.getTraces();

                    trackInfo.setCreatedTime(System.currentTimeMillis());
                    trackInfo.setJsonString(gson.toJson(trackInfo));
                    trackInfo.setTitle(title);
//                    trackInfoDao.insert(trackInfo);

                    Log.d(TAG, "success: " + tracesBeanList.get(0).getAcceptStation());

                    Intent intent = new Intent(AddActivity.this, PackageDetailActivity.class);
                    intent.putExtra(package_title, title);
                    intent.putExtra(trace_info, gson.toJson(tracesBeanList));
                    startActivity(intent);
                } else {
                    Toast.makeText(AddActivity.this, "get trace failed", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "accept: get trace failed" + trackInfo.getReason());
                }
            }
        });
    }

}
