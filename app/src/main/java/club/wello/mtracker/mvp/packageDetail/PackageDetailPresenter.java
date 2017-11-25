package club.wello.mtracker.mvp.packageDetail;

import android.util.Log;

import club.wello.mtracker.apiUtil.TrackInfo;
import club.wello.mtracker.data.PackagesRepository;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * mvp presenter for PackageDetailActivity
 * Created by maweihao on 22/11/2017.
 */

public class PackageDetailPresenter implements PackageDetailContract.Presenter {

    private static final String TAG = PackageDetailPresenter.class.getSimpleName();

    private PackageDetailContract.View view;
    private PackagesRepository packagesRepository;
    private CompositeDisposable compositeDisposable;

    private String packageName;
    private String packageId;

    public PackageDetailPresenter(PackageDetailContract.View view, PackagesRepository packagesRepository, String packageId) {
        this.view = view;
        this.packagesRepository = packagesRepository;
        this.packageId = packageId;
        compositeDisposable = new CompositeDisposable();
    }

    @Override
    public void subscribe() {
        Log.d(TAG, "subscribe: HERE");
        loadTraces();
    }

    private void loadTraces() {
        Disposable disposable = packagesRepository
                .getPackage(packageId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<TrackInfo>() {

                    @Override
                    public void accept(TrackInfo trackInfo) throws Exception {
                        packageName = trackInfo.getTitle();
//                        Log.d(TAG, "accept: " + new Gson().toJson(trackInfo, TrackInfo.class));
                        view.showPackageDetails(trackInfo);

                        packagesRepository.setPackageReadable(packageId, false);
                    }
                });

        compositeDisposable.add(disposable);
    }

    @Override
    public void unSubscribe() {
        compositeDisposable.clear();
    }

    @Override
    public void setPackageUnread() {
        packagesRepository.setPackageReadable(packageId, true);
        view.exit();
    }

    @Override
    public void refreshPackage() {
        view.setLoadingIndicator(true);
        packagesRepository.setPackageReadable(packageId, true);
        Disposable disposable = packagesRepository
                .refreshPackage(packageId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<TrackInfo>() {

                    @Override
                    public void onNext(TrackInfo trackInfo) {
                        view.setLoadingIndicator(false);
                        view.showPackageDetails(trackInfo);
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.setLoadingIndicator(false);
                        view.showNetworkError();
                    }

                    @Override
                    public void onComplete() {

                    }
                });

        compositeDisposable.add(disposable);
    }

    @Override
    public void deletePackage() {
        packagesRepository.deletePackage(packageId);
        view.exit();
    }

    @Override
    public void copyPackageNumber() {
        view.copyPackageNumber(packageId);
    }

    @Override
    public void shareTo() {
        // TODO: 22/11/2017
    }

    @Override
    public String getPackageName() {
        return packageName;
    }

    @Override
    public void updatePackageName(String newName) {
        packagesRepository.updatePackageName(packageId, newName);
        // TODO: 25/11/2017 finish the method
    }
}
