package club.wello.mtracker.apiUtil;

/**
 * for storing constants
 * Created by maweihao on 2017/10/9.
 */

public class Constants {

    //电商ID
    private String EBusinessID="请到快递鸟官网申请http://www.kdniao.com/ServiceApply.aspx";
    //电商加密私钥，快递鸟提供，注意保管，不要泄漏
    private String AppKey="请到快递鸟官网申请http://www.kdniao.com/ServiceApply.aspx";
    //请求url
    private String ReqURL="http://api.kdniao.cc/Ebusiness/EbusinessOrderHandle.aspx";

    public static final int SCANNING_REQUEST_CODE = 101;

    public static final String package_number = "PACKAGE_NUMBER";
    public static final String package_title = "PACKAGE_TITLE";
    public static final String trace_info = "TRACE_INFO";

}
