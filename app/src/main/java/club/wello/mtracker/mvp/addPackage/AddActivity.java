package club.wello.mtracker.mvp.addPackage;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;
import club.wello.mtracker.R;
import club.wello.mtracker.data.PackagesRepository;

public class AddActivity extends AppCompatActivity {

    private static final String TAG = AddActivity.class.getSimpleName();
    AddPackageFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        ButterKnife.bind(this);

        if (savedInstanceState != null) {
            fragment = (AddPackageFragment) getSupportFragmentManager().getFragment(savedInstanceState, "AddPackageFragment");
        } else {
            fragment = new AddPackageFragment();
        }

        if (!fragment.isAdded()) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment, fragment, "AddPackageFragment")
                    .commit();
        }

        fragment.setPresenter(new AddPackagePresenter(fragment, PackagesRepository.getInstance(getApplicationContext())));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        getSupportFragmentManager().putFragment(outState, "AddPackageFragment", fragment);
    }

}
