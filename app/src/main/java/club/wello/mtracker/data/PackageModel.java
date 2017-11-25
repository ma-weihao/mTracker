package club.wello.mtracker.data;

import android.support.annotation.NonNull;

import java.util.List;

import club.wello.mtracker.apiUtil.TrackInfo;
import io.reactivex.Observable;

/**
 * Created by maweihao on 2017/10/18.
 */

public interface PackageModel {

    Observable<List<TrackInfo>> getPackages();

    Observable<TrackInfo> getPackage(@NonNull final String packNumber);

    void savePackage(@NonNull TrackInfo trackInfo);

    void deletePackage(@NonNull String packageId);

    Observable<List<TrackInfo>> refreshPackages();

    Observable<TrackInfo> refreshPackage(@NonNull String packageId);

    void setAllPackagesRead();

    void setPackageReadable(@NonNull String packageId, boolean readable);  //haven't used in mTrack

    boolean isPackageExistInCache(@NonNull String packageId);

    void updatePackageName(@NonNull String packageId, @NonNull String name);

    Observable<List<TrackInfo>> searchPackages(@NonNull String keyWords);
}
