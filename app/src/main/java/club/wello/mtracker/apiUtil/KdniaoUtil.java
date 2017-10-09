package club.wello.mtracker.apiUtil;

import android.support.annotation.NonNull;
import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;

/**
 * utility class for kdniao api
 * Created by maweihao on 2017/10/9.
 */

public class KdniaoUtil {

    //电商ID
    private static String EBusinessID = "1305220";
    //电商加密私钥，快递鸟提供，注意保管，不要泄漏
    private static String AppKey = "78871c1c-5ad7-4774-bbce-017ac0a50108";


    /**
     * 生成 post 数据
     * @param expCode  快递公司编号
     * @param expNo 快递单号
     * @return Map
     */
    public static Map<String, String> getOrderTracesByJson(String expCode, String expNo) {
        String requestData= "{'OrderCode':'','ShipperCode':'" + expCode + "','LogisticCode':'" + expNo + "'}";
        Map<String, String> params = new HashMap<>();

        try {
            params.put("RequestData", urlEncoder(requestData, "UTF-8"));
            params.put("EBusinessID", EBusinessID);
            params.put("RequestType", "1002");
            String dataSign= null;
            dataSign = encrypt(requestData, AppKey, "UTF-8");
            params.put("DataSign", urlEncoder(dataSign, "UTF-8"));
            params.put("DataType", "2");
        } catch (UnsupportedEncodingException | NoSuchAlgorithmException e) {
            e.printStackTrace();
            Log.e(TAG, "getOrderTracesByJson: generate map for track info failed");
        }

        return params;
    }

    /**
     * Json方式 单号识别
     * @param expNo 快递单号
     * @return Map
     */
    public static Map<String, String> getOrderTracesByJson(String expNo){
        String requestData = "{'LogisticCode':'" + expNo + "'}";
        Map<String, String> params = new HashMap<>();

        try {
            params.put("RequestData", urlEncoder(requestData, "UTF-8"));
            params.put("EBusinessID", EBusinessID);
            params.put("RequestType", "2002");
            String dataSign = null;
            dataSign = encrypt(requestData, AppKey, "UTF-8");
            params.put("DataSign", urlEncoder(dataSign, "UTF-8"));
            params.put("DataType", "2");
        } catch (UnsupportedEncodingException | NoSuchAlgorithmException e) {
            e.printStackTrace();
            Log.e(TAG, "getOrderTracesByJson: generate map for track info failed");
        }

        return params;
    }

    @Deprecated
    public static String map2Json(@NonNull Map<String, String> map) {
        StringBuilder param = new StringBuilder();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            if (param.length() > 0) {
                param.append("&");
            }
            param.append(entry.getKey());
            param.append("=");
            param.append(entry.getValue());
        }
        return param.toString();
    }

    /**
     * base64编码
     * @param str 内容
     * @param charset 编码方式
     * @throws UnsupportedEncodingException exception
     */
    private static String base64(String str, String charset) throws UnsupportedEncodingException{
        return base64Encode(str.getBytes(charset));
    }

    private static String urlEncoder(String str, String charset) throws UnsupportedEncodingException {
        return URLEncoder.encode(str, charset);
    }

    /**
     * 电商Sign签名生成
     * @param content 内容
     * @param keyValue Appkey
     * @param charset 编码方式
     * @throws UnsupportedEncodingException ,Exception
     * @return DataSign签名
     */
    @SuppressWarnings("unused")
    private static String encrypt (String content, String keyValue, String charset) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        if (keyValue != null)
        {
            return base64(MD5(content + keyValue, charset), charset);
        }
        return base64(MD5(content, charset), charset);
    }

    /**
     * MD5加密
     * @param str 内容
     * @param charset 编码方式
     * @throws Exception NoSuchAlgorithmException, UnsupportedEncodingException
     */
    @SuppressWarnings("unused")
    private static String MD5(String str, String charset) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(str.getBytes(charset));
        byte[] result = md.digest();
        StringBuilder sb = new StringBuilder(32);
        for (byte aResult : result) {
            int val = aResult & 0xff;
            if (val <= 0xf) {
                sb.append("0");
            }
            sb.append(Integer.toHexString(val));
        }
        return sb.toString().toLowerCase();
    }

    private static char[] base64EncodeChars = new char[] {
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H',
            'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P',
            'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X',
            'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f',
            'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n',
            'o', 'p', 'q', 'r', 's', 't', 'u', 'v',
            'w', 'x', 'y', 'z', '0', '1', '2', '3',
            '4', '5', '6', '7', '8', '9', '+', '/' };

    private static String base64Encode(byte[] data) {
        StringBuilder sb = new StringBuilder();
        int len = data.length;
        int i = 0;
        int b1, b2, b3;
        while (i < len) {
            b1 = data[i++] & 0xff;
            if (i == len)
            {
                sb.append(base64EncodeChars[b1 >>> 2]);
                sb.append(base64EncodeChars[(b1 & 0x3) << 4]);
                sb.append("==");
                break;
            }
            b2 = data[i++] & 0xff;
            if (i == len)
            {
                sb.append(base64EncodeChars[b1 >>> 2]);
                sb.append(base64EncodeChars[((b1 & 0x03) << 4) | ((b2 & 0xf0) >>> 4)]);
                sb.append(base64EncodeChars[(b2 & 0x0f) << 2]);
                sb.append("=");
                break;
            }
            b3 = data[i++] & 0xff;
            sb.append(base64EncodeChars[b1 >>> 2]);
            sb.append(base64EncodeChars[((b1 & 0x03) << 4) | ((b2 & 0xf0) >>> 4)]);
            sb.append(base64EncodeChars[((b2 & 0x0f) << 2) | ((b3 & 0xc0) >>> 6)]);
            sb.append(base64EncodeChars[b3 & 0x3f]);
        }
        return sb.toString();
    }

    public static class TrackRequest {
        public String RequestData;
        public String EBusinessID;
        public String RequestType;
        public String DataSign;
        public String DataType;

        public TrackRequest() {
        }

        public TrackRequest(String requestData, String EBusinessID, String requestType, String dataSign, String dataType) {
            RequestData = requestData;
            this.EBusinessID = EBusinessID;
            RequestType = requestType;
            DataSign = dataSign;
            DataType = dataType;
        }

        public TrackRequest(String requestData, String EBusinessID, String requestType, String dataSign) {
            RequestData = requestData;
            this.EBusinessID = EBusinessID;
            RequestType = requestType;
            DataSign = dataSign;
        }
    }
}
