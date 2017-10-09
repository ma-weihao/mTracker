package club.wello.mtracker.apiUtil;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * retrofit entity class for kdniao api
 * Created by maweihao on 2017/10/9.
 */

public interface KdApi {

    @FormUrlEncoded
    @POST("./")
    Observable<TrackInfo> getTrackInfo(@FieldMap Map<String, String> fields);

    @FormUrlEncoded
    @POST("./")
    Observable<TrackCompanyInfo> getTrackCompany(@FieldMap Map<String, String> fields);
}
