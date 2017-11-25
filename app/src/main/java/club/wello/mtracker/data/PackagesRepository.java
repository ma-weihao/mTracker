package club.wello.mtracker.data;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.WorkerThread;
import android.util.Log;

import com.google.gson.Gson;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import club.wello.mtracker.apiUtil.DaoMaster;
import club.wello.mtracker.apiUtil.DaoSession;
import club.wello.mtracker.apiUtil.TrackInfo;
import club.wello.mtracker.apiUtil.TrackInfoDao;
import club.wello.mtracker.mvp.packageList.PackageFragment;
import club.wello.mtracker.util.HttpUtil;
import club.wello.mtracker.util.Utility;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * Created by maweihao on 2017/10/20.
 */

public class PackagesRepository implements PackageModel {

    private static final String TAG = PackageFragment.class.getSimpleName();
    private static PackagesRepository instance;
    private TrackInfoDao trackInfoDao;
//    private Context context;

    private Map<String, TrackInfo> cachedPackages;

    private PackagesRepository(Context context) {
//        this.context = context;
        DaoMaster.DevOpenHelper devOpenHelper = new DaoMaster.DevOpenHelper(context, "trace.db", null);
        DaoMaster daoMaster = new DaoMaster(devOpenHelper.getWritableDb());
        DaoSession daoSession = daoMaster.newSession();
        trackInfoDao = daoSession.getTrackInfoDao();
    }

    public static PackagesRepository getInstance(Context context) {
        if (instance == null) {
            instance = new PackagesRepository(context);
        }
        return instance;
    }

    public static void destroyInstance() {
        instance = null;
    }

    @Override
    public Observable<List<TrackInfo>> getPackages() {
        if (cachedPackages == null) {
            final Gson gson = new Gson();
            cachedPackages = new LinkedHashMap<>();
//            final QueryBuilder<TrackInfo> qb = trackInfoDao.queryBuilder();
//            qb.where(qb.or(TrackInfoDao.Properties.State.notEq(3), TrackInfoDao.Properties.Readable.eq(true)));
            return Observable.create(new ObservableOnSubscribe<List<TrackInfo>>() {
                @Override
                public void subscribe(@io.reactivex.annotations.NonNull ObservableEmitter<List<TrackInfo>> e) throws Exception {
//                    List<TrackInfo> trackInfoList = qb.orderDesc(TrackInfoDao.Properties.CreatedTime).list();
                    List<TrackInfo> trackInfoList = trackInfoDao.loadAll();
                    if (trackInfoList == null) {
                        e.onError(null);
                    } else {
                        for (int i = 0; i < trackInfoList.size(); i++) {
                            TrackInfo trackInfo = trackInfoList.get(i);
                            trackInfo = gson.fromJson(trackInfo.getJsonString(), TrackInfo.class);
                            trackInfo.setCreatedTime(Utility.parseFirstTime(trackInfo.getTraces()));
                            trackInfoList.set(i, trackInfo);
                            // TODO: 21/11/2017 may need to deal with something
                            cachedPackages.put(trackInfo.getShipperCode(), trackInfo);
                        }
                        e.onNext(trackInfoList);
                        e.onComplete();
                    }
                }
            });
        } else {
            return Observable.create(new ObservableOnSubscribe<List<TrackInfo>>() {
                @Override
                public void subscribe(@io.reactivex.annotations.NonNull ObservableEmitter<List<TrackInfo>> e) throws Exception {
                    e.onNext(new ArrayList<>(cachedPackages.values()));
                }
            });
        }
    }

    @Override
    public Observable<TrackInfo> getPackage(@NonNull String packNumber) {
        TrackInfo trackInfo = getCachedPackageByNumber(packNumber);
        if (trackInfo == null) {
            return getStoredPackageByNumber(packNumber);
        } else {
            return Observable.just(trackInfo);
        }
    }

    @Override
    public void savePackage(@NonNull TrackInfo trackInfo) {
        cachedPackages.put(trackInfo.getShipperCode(), trackInfo);
        if (isPackageExistInCache(trackInfo.getLogisticCode())) {
            trackInfoDao.update(trackInfo);
        } else {
            trackInfoDao.insert(trackInfo);
        }
    }

    @Override
    public void deletePackage(@NonNull String packageId) {
        cachedPackages.remove(packageId);
        final QueryBuilder<TrackInfo> qb = trackInfoDao.queryBuilder();
        qb.where(TrackInfoDao.Properties.LogisticCode.eq(packageId));
        TrackInfo trackInfo = qb.unique();
        trackInfoDao.delete(trackInfo);
    }

    @Override
    public Observable<List<TrackInfo>> refreshPackages() {
        // TODO: 21/11/2017 this function is too small that i think it has some unconcerned conditions
        if (cachedPackages != null) {
           return Observable.fromIterable(cachedPackages.values())
                   .flatMap(new Function<TrackInfo, ObservableSource<TrackInfo>>() {
                       @Override
                       public ObservableSource<TrackInfo> apply(@io.reactivex.annotations.NonNull TrackInfo trackInfo) throws Exception {
                           return refreshPackage(trackInfo.getLogisticCode());
                       }
                   })
                   .toList()
                   .toObservable();
        } else {
            return null;
        }
    }

    @Override
    public Observable<TrackInfo> refreshPackage(@NonNull String packageId) {
        return HttpUtil.getTrackInfoDirectly(packageId)
                .doOnNext(new Consumer<TrackInfo>() {
                    @Override
                    public void accept(TrackInfo trackInfo) throws Exception {
                        TrackInfo oldTrackInfo = cachedPackages.get(trackInfo.getLogisticCode());
                        if (oldTrackInfo.getTraces().size() != trackInfo.getTraces().size()) {
                            trackInfo.setReadable(false);
                            // TODO: 21/11/2017 need to deal with pushable
                        }
                        savePackage(trackInfo);
                    }
                });
    }

    @Override
    public void setAllPackagesRead() {
        //TODO
    }

    @Override
    public void setPackageReadable(@NonNull String packageId, boolean readable) {
        // TODO: 22/11/2017
    }

    @Override
    public boolean isPackageExistInCache(@NonNull String packageId) {
        return cachedPackages.containsKey(packageId);
    }

    @Override
    public void updatePackageName(@NonNull String packageId, @NonNull String name) {
        //TODO
    }

    @Override
    public Observable<List<TrackInfo>> searchPackages(@NonNull String keyWords) {
        // TODO: 21/11/2017
        return null;
    }

    private TrackInfo getCachedPackageByNumber(@NonNull String number) {
        if (cachedPackages == null || cachedPackages.isEmpty()) {
            return null;
        } else {
            return cachedPackages.get(number);
        }
    }

    @WorkerThread
    private Observable<TrackInfo> getStoredPackageByNumber(@NonNull final String number) {
        Log.d(TAG, "getStoredPackageByNumber: " + number);
        return Observable.create(new ObservableOnSubscribe<TrackInfo>() {
            @Override
            public void subscribe(@io.reactivex.annotations.NonNull ObservableEmitter<TrackInfo> e) throws Exception {
                final QueryBuilder<TrackInfo> qb = trackInfoDao.queryBuilder();
                qb.where(TrackInfoDao.Properties.LogisticCode.eq(number));
                TrackInfo trackInfo = Utility.unpackPackage(qb.unique());
                e.onNext(trackInfo);
                cachedPackages.put(trackInfo.getShipperCode(), trackInfo);
            }
        });
    }
}
