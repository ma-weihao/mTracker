package club.wello.mtracker.util;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import club.wello.mtracker.R;
import club.wello.mtracker.apiUtil.TrackInfo;

/**
 * utility class
 * Created by maweihao on 2017/10/20.
 */

public class Utility {

    private static final String TAG = Utility.class.getSimpleName();

    /**
     *
     * @param timeString e.g "2017-10-04 17:26:25"
     * @return long time in mills
     */
    public static long parseTime(String timeString) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        try {
            return format.parse(timeString).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            Log.e(TAG, "parseTime: failed, using cTIM instead. raw: " + timeString);
            return System.currentTimeMillis();
        }
    }

    public static long parseFirstTime(List<TrackInfo.TracesBean> list) {
        if (list != null && list.size() > 0) {
            return parseTime(list.get(0).getAcceptTime());
        } else {
            Log.d(TAG, "parseFirstTime: list empty, using cTIM instead");
            return System.currentTimeMillis();
        }
    }

    public static TrackInfo packPackage(TrackInfo trackInfo, String title) {
        Gson gson = new Gson();
        List<TrackInfo.TracesBean> tracesBeanList = trackInfo.getTraces();
        Log.d(TAG, "packPackage: here" + gson.toJson(trackInfo, TrackInfo.class));

        if (tracesBeanList.size() == 0) {
            trackInfo.setCreatedTime(System.currentTimeMillis());
        } else {
            //ATTENTION: if parse time failed, current time in mills will be stored instead
            trackInfo.setCreatedTime(Utility.parseTime(tracesBeanList.get(tracesBeanList.size() - 1).getAcceptTime()));
        }
        trackInfo.setJsonString(gson.toJson(trackInfo));
        trackInfo.setTitle(title);
        trackInfo.setPushable(false);
        trackInfo.setReadable(false);
        trackInfo.setJsonString(gson.toJson(trackInfo));
        return trackInfo;
    }

    public static TrackInfo unpackPackage(TrackInfo trackInfo) {
        Gson gson = new Gson();
        return gson.fromJson(trackInfo.getJsonString(), TrackInfo.class);
    }

    public static String parseState(Context context, int code) {
        switch (code) {
            case 0:
                return context.getResources().getString(R.string.state_0);
            case 1:
                return context.getResources().getString(R.string.state_1);
            case 2:
                return context.getResources().getString(R.string.state_2);
            case 3:
                return context.getResources().getString(R.string.state_3);
            case 4:
                return context.getResources().getString(R.string.state_4);
            default:
                return "unknown state" + code;
        }
    }
}
