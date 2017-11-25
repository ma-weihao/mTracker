package club.wello.mtracker.mvp.packageDetail;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import club.wello.mtracker.R;
import club.wello.mtracker.data.PackagesRepository;

public class PackageDetailActivity extends AppCompatActivity {

    public static final String PACKAGE_ID = "PACKAGE_ID";

    private static final String TAG = PackageDetailActivity.class.getSimpleName();
    private PackageDetailFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_package_detail);

        // Restore the status.
        if (savedInstanceState != null) {
            fragment = (PackageDetailFragment) getSupportFragmentManager()
                    .getFragment(savedInstanceState, "PackageDetailsFragment");
        } else {
            fragment = new PackageDetailFragment();
        }

        fragment.setPresenter(new PackageDetailPresenter(fragment,
                PackagesRepository.getInstance(PackageDetailActivity.this),
                getIntent().getStringExtra(PACKAGE_ID)));

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fl_fragment, fragment)
                .commit();

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        getSupportFragmentManager().putFragment(outState, "PackageDetailsFragment", fragment);
    }
}
