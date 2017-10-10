package club.wello.mtracker.apiUtil;

import java.util.List;

/**
 * track company info json entity class
 * http://www.kdniao.com/v2/API/Recognise.aspx
 * Created by maweihao on 2017/10/9.
 */

public class TrackCompanyInfo {

    /**
     * EBusinessID : 1305220
     * Success : true
     * LogisticCode : 3967950525457
     * Shippers : [{"ShipperCode":"YD","ShipperName":"韵达快递"}]
     */

    private String EBusinessID;
    private boolean Success;
    private String LogisticCode;
    private int Code;
    private List<ShippersBean> Shippers;
    private String Reason;  //only exists when Success is false

    public String getReason() {
        return Reason;
    }

    public void setReason(String reason) {
        Reason = reason;
    }

    public String getEBusinessID() {
        return EBusinessID;
    }

    public void setEBusinessID(String EBusinessID) {
        this.EBusinessID = EBusinessID;
    }

    public boolean isSuccess() {
        return Success;
    }

    public void setSuccess(boolean Success) {
        this.Success = Success;
    }

    public String getLogisticCode() {
        return LogisticCode;
    }

    public void setLogisticCode(String LogisticCode) {
        this.LogisticCode = LogisticCode;
    }

    public int getCode() {
        return Code;
    }

    public void setCode(int code) {
        Code = code;
    }

    public List<ShippersBean> getShippers() {
        return Shippers;
    }

    public void setShippers(List<ShippersBean> Shippers) {
        this.Shippers = Shippers;
    }

    public static class ShippersBean {
        /**
         * ShipperCode : YD
         * ShipperName : 韵达快递
         */

        private String ShipperCode;
        private String ShipperName;

        public String getShipperCode() {
            return ShipperCode;
        }

        public void setShipperCode(String ShipperCode) {
            this.ShipperCode = ShipperCode;
        }

        public String getShipperName() {
            return ShipperName;
        }

        public void setShipperName(String ShipperName) {
            this.ShipperName = ShipperName;
        }
    }
}
