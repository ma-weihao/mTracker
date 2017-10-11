package club.wello.mtracker.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.List;
import java.util.Map;

import club.wello.mtracker.R;
import club.wello.mtracker.apiUtil.DaoMaster;
import club.wello.mtracker.apiUtil.DaoSession;
import club.wello.mtracker.apiUtil.KdniaoUtil;
import club.wello.mtracker.apiUtil.TrackCompanyInfo;
import club.wello.mtracker.apiUtil.TrackInfo;
import club.wello.mtracker.apiUtil.TrackInfoDao;
import club.wello.mtracker.util.HttpUtil;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

import static club.wello.mtracker.apiUtil.Constants.SCANNING_REQUEST_CODE;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private TrackInfoDao trackInfoDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddActivity.class);
                startActivity(intent);
//                Intent intent = new Intent(MainActivity.this, CaptureActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivityForResult(intent, SCANNING_REQUEST_CODE);
            }
        });

//        doSomeThing();
        doAnotherThing();
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
                            Toast.makeText(MainActivity.this, bundle.getString("result"), Toast.LENGTH_LONG).show();
                        }
                    });
                }
                break;
            default:
                break;
        }
    }

    private void doAnotherThing() {
        DaoMaster.DevOpenHelper devOpenHelper = new DaoMaster.DevOpenHelper(this, "trace.db", null);
        DaoMaster daoMaster = new DaoMaster(devOpenHelper.getWritableDb());
        DaoSession daoSession = daoMaster.newSession();
        trackInfoDao = daoSession.getTrackInfoDao();
        List<TrackInfo> trackInfoList = trackInfoDao.loadAll();
        Log.d(TAG, "doAnotherThing: " + trackInfoList.size());
    }

    private void doSomeThing() {
        Map<String, String> map = KdniaoUtil.getOrderTracesByJson("3967950525457");
        HttpUtil.getTrackCompanyInfo(map).subscribe(new Observer<TrackCompanyInfo>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
            }

            @Override
            public void onNext(@NonNull TrackCompanyInfo trackCompanyInfo) {
                Log.d(TAG, "onNext: " + trackCompanyInfo.getShippers().get(0).getShipperName());
            }

            @Override
            public void onError(@NonNull Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
