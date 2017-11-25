package club.wello.mtracker.mvp.packageDetail;

import android.support.annotation.NonNull;

import club.wello.mtracker.apiUtil.TrackInfo;
import club.wello.mtracker.mvp.BasePresenter;
import club.wello.mtracker.mvp.BaseView;

/**
 * mvp contract for PackageDetailActivity
 * Created by maweihao on 22/11/2017.
 */

public interface PackageDetailContract {

    interface View extends BaseView<Presenter> {
        void setLoadingIndicator(boolean loading);

        void showNetworkError();

        void showPackageDetails(@NonNull TrackInfo trackInfo);

//        void setToolbarBackground(@DrawableRes int resId);

//        void shareTo(@NonNull TrackInfo pack);

        void copyPackageNumber(@NonNull String packageId);

        void exit();

//        void exit();
    }

    interface Presenter extends BasePresenter {

        void setPackageUnread();

        void refreshPackage();

        void deletePackage();

        void copyPackageNumber();

        void shareTo();

        String getPackageName();

        void updatePackageName(String newName);
    }
}
