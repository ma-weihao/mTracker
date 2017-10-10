package club.wello.mtracker.util;

import java.util.Map;

import club.wello.mtracker.apiUtil.KdApi;
import club.wello.mtracker.apiUtil.TrackCompanyInfo;
import club.wello.mtracker.apiUtil.TrackInfo;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
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

    public static Observable<TrackInfo> getTrackInfo(Map<String, String> map) {
        return getKdApi().getTrackInfo(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static Observable<TrackCompanyInfo> getTrackCompanyInfo(Map<String, String> map) {
        return getKdApi().getTrackCompany(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
