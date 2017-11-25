package club.wello.mtracker.mvp.addPackage;

import android.text.TextUtils;
import android.util.Log;

import club.wello.mtracker.apiUtil.TrackCompanyInfo;
import club.wello.mtracker.apiUtil.TrackInfo;
import club.wello.mtracker.data.PackageModel;
import club.wello.mtracker.util.HttpUtil;
import club.wello.mtracker.util.Utility;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * MVP presenter for
 * Created by maweihao on 2017/10/23.
 */

public class AddPackagePresenter implements AddPackageContract.Presenter {

    private static final String TAG = AddPackagePresenter.class.getSimpleName();
    private PackageModel model;
    private AddPackageContract.View view;
    private CompositeDisposable compositeDisposable;

    public AddPackagePresenter(AddPackageContract.View view, PackageModel model) {
        this.model = model;
        this.view = view;
        compositeDisposable = new CompositeDisposable();
    }

    @Override
    public void savePackage(final String number, final String name) {
        if (model.isPackageExistInCache(number)) {
            view.showNumberExistError();
            return;
        }
        view.setProgressIndicator(true);
        Disposable disposable = HttpUtil.getTrackCompanyInfo(number)
                .flatMap(new Function<TrackCompanyInfo, ObservableSource<TrackInfo>>() {
                    @Override
                    public ObservableSource<TrackInfo> apply(@NonNull TrackCompanyInfo trackCompanyInfo) throws Exception {
                        if (trackCompanyInfo.isSuccess()) {
                            String shipperName = trackCompanyInfo.getShippers().get(0).getShipperName();
                            String shipperCode = trackCompanyInfo.getShippers().get(0).getShipperCode();
                            String newName = TextUtils.isEmpty(name) ?
                                    (shipperName + number.substring(number.length()-3)) : name;
                            return HttpUtil.getTrackInfo(shipperCode, number);
                        } else {
                            Log.e(TAG, "savePackage: get company failed" + trackCompanyInfo.getReason() + number);
                            view.showNetworkError();
                            return Observable.error(new RuntimeException("get company failed"));
                        }
                    }
                }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<TrackInfo>() {
                    @Override
                    public void accept(TrackInfo trackInfo) throws Exception {
                        if (trackInfo.isSuccess()) {
                            TrackInfo newTrackInfo = Utility.packPackage(trackInfo, name);
                            model.savePackage(trackInfo);
                            view.showPackagesList();
//                            Intent intent = new Intent(AddActivity.this, PackageDetailActivity.class);
//                            intent.putExtra(package_title, title);
//                            intent.putExtra(trace_info, gson.toJson(tracesBeanList));
//                            startActivity(intent);
                        } else {
                            Log.e(TAG, "accept: get trace failed" + trackInfo.getReason() +
                                    number + "'" + trackInfo.getShipperCode());
                            view.showNetworkError();
                        }
                    }
                });
        compositeDisposable.add(disposable);
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unSubscribe() {
        compositeDisposable.clear();
    }
}
