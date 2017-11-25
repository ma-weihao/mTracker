package club.wello.mtracker.mvp.packageList;

import android.support.annotation.NonNull;

import java.util.List;

import club.wello.mtracker.apiUtil.TrackInfo;
import club.wello.mtracker.mvp.BasePresenter;
import club.wello.mtracker.mvp.BaseView;

/**
 * mvp Contract for MainActivity
 * Created by maweihao on 2017/10/20.
 */

public interface PackageContract {

    interface view extends BaseView<presenter> {
        void setLoadingIndicator(boolean active);

        void showEmptyView(boolean toShow);

        void showPackages(@NonNull List<TrackInfo> list);

//        void shareTo(@NonNull TrackInfo pack);

        void showPackageRemovedMsg(String packageName);

        void copyPackageNumber();

        void showNetworkError();

        void showError(String message);

    }

    interface presenter extends BasePresenter{

        void loadPackages();

        void refreshPackages();

        void markAllPacksRead();

//        void setFiltering(@NonNull PackageFilterType requestType);

//        PackageFilterType getFiltering();

        void setPackageReadable(@NonNull String packageId, boolean readable);

        void deletePackage(int position);

        void setShareData(@NonNull String packageId);

        void recoverPackage();
    }
}
