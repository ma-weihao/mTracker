package club.wello.mtracker.util;

import android.util.Log;

import club.wello.mtracker.apiUtil.KdApi;
import club.wello.mtracker.apiUtil.KdniaoUtil;
import club.wello.mtracker.apiUtil.TrackCompanyInfo;
import club.wello.mtracker.apiUtil.TrackInfo;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * utility class for network
 * Created by maweihao on 2017/10/9.
 */

public class HttpUtil {

    private static final String TAG = HttpUtil.class.getSimpleName();

    private static KdApi kdApi;
    //请求url
    private static String reqURL = "http://api.kdniao.cc/Ebusiness/EbusinessOrderHandle.aspx/";

    private static KdApi getKdApi() {
        if (kdApi == null) {
            kdApi = getService(reqURL).create(KdApi.class);
        }
        return kdApi;
    }

    private static Retrofit getService(String baseUrl) {
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    /**
     *
     * @param code 快递公司编号
     * @param number 运单号
     * @return observable
     */
    public static Observable<TrackInfo> getTrackInfo(String code, String number) {
        return getKdApi().getTrackInfo(KdniaoUtil.getOrderTracesByJson(code, number))
                .subscribeOn(Schedulers.io());
    }

    public static Observable<TrackCompanyInfo> getTrackCompanyInfo(String number) {
        return getKdApi().getTrackCompany(KdniaoUtil.getOrderTracesByJson(number))
                .subscribeOn(Schedulers.io());
    }

    public static Observable<TrackInfo> getTrackInfoDirectly(final String number) {
        return getTrackCompanyInfo(number)
                .flatMap(new Function<TrackCompanyInfo, ObservableSource<TrackInfo>>() {
                    @Override
                    public ObservableSource<TrackInfo> apply(@NonNull TrackCompanyInfo trackCompanyInfo) throws Exception {
                        if (trackCompanyInfo.isSuccess()) {
                            return getTrackInfo(trackCompanyInfo.getShippers().get(0).getShipperCode(), number);
                        } else {
                            Log.e(TAG, "apply: no company found " + trackCompanyInfo.getReason());
                            return null;
                        }
                    }
                });
    }
}
