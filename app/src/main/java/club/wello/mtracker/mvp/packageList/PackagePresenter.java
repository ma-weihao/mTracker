package club.wello.mtracker.mvp.packageList;

import android.support.annotation.NonNull;
import android.util.Log;

import java.util.List;

import club.wello.mtracker.apiUtil.TrackInfo;
import club.wello.mtracker.data.PackageModel;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * presenter of PackageFragment
 * Created by maweihao on 2017/10/20.
 */

public class PackagePresenter implements PackageContract.presenter {

    private static final String TAG = PackagePresenter.class.getSimpleName();
    private final PackageContract.view view;
    private final PackageModel model;
    private CompositeDisposable compositeDisposable;

    private TrackInfo removedPackage;

    public PackagePresenter(PackageContract.view view, PackageModel model) {
        this.view = view;
        this.model = model;
        compositeDisposable = new CompositeDisposable();
    }

    @Override
    public void loadPackages() {
        compositeDisposable.clear();
        Disposable disposable = model
                .getPackages().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<TrackInfo>>() {
            @Override
            public void accept(List<TrackInfo> list) throws Exception {
                if (list != null || list.size() > 0) {
                    view.showPackages(list);
                } else {
                    view.showEmptyView(true);
                }
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                view.showError("loadPackages error");
                Log.e(TAG, "loadPackages error");
            }
        });

        compositeDisposable.add(disposable);
    }

    @Override
    public void refreshPackages() {
        Disposable disposable = model
                .refreshPackages()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<TrackInfo>>() {
                    @Override
                    public void accept(List<TrackInfo> list) throws Exception {
                        if (list != null && list.size() > 0) {
                            view.showPackages(list);
                        } else {
                            view.showEmptyView(true);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        view.showError("refreshPackages error");
                        Log.e(TAG, "refreshPackages error");
                    }
                });
        compositeDisposable.add(disposable);
    }

    @Override
    public void markAllPacksRead() {
        //TODO
    }

    @Override
    public void setPackageReadable(@NonNull String packageId, boolean readable) {
        //TODO
    }

    @Override
    public void deletePackage(int position) {
        // TODO: 21/11/2017
    }

    @Override
    public void setShareData(@NonNull String packageId) {
    }

    @Override
    public void recoverPackage() {
        if (removedPackage != null) {
            model.savePackage(removedPackage);
            removedPackage = null;
        }
        loadPackages();
    }

    @Override
    public void subscribe() {
        loadPackages();
    }

    @Override
    public void unSubscribe() {
        compositeDisposable.clear();
    }
}
