package club.wello.mtracker.mvp.packageList;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import club.wello.mtracker.R;
import club.wello.mtracker.mvp.setting.SettingsActivity;
import club.wello.mtracker.mvp.search.SearchActivity;
import club.wello.mtracker.data.PackagesRepository;

import static club.wello.mtracker.util.Constants.SCANNING_REQUEST_CODE;

public class MainActivity extends AppCompatActivity {


    private static final String TAG = MainActivity.class.getSimpleName();
    private PackageFragment packageFragment;
    private PackagePresenter packagePresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        if (savedInstanceState == null) {
            packageFragment = (PackageFragment) getSupportFragmentManager().findFragmentById(R.id.fragment);
            if (packageFragment == null) {
                packageFragment = new PackageFragment();
            }
        } else {
            packageFragment = (PackageFragment) getSupportFragmentManager().getFragment(savedInstanceState, "PackageFragment");
        }

        if (!packageFragment.isAdded()) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment, packageFragment, "PackageFragment")
                    .commit();
        }

        PackagesRepository.destroyInstance();
        packagePresenter = new PackagePresenter(packageFragment, PackagesRepository.getInstance(MainActivity.this));
        packageFragment.setPresenter(packagePresenter);

    }

    private void initView() {

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.action_settings:
                intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_search:
                intent = new Intent(MainActivity.this, SearchActivity.class);
                startActivity(intent);
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (packageFragment.isAdded()) {
            getSupportFragmentManager().putFragment(outState, "PackageFragment", packageFragment);
        }
    }

}
