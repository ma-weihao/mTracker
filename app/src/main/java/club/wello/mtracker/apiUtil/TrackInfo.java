package club.wello.mtracker.apiUtil;

import java.util.List;

/**
 * track info json entity class
 * document: http://www.kdniao.com/v2/API/Track.aspx
 * Created by maweihao on 2017/10/9.
 */

public class TrackInfo {


    /**
     * LogisticCode : 3908741386124
     * ShipperCode : YD
     * Traces : [{"AcceptStation":"到达：浙江乐清市乐成公司 已揽件","AcceptTime":"2017-10-04 17:26:25"},{"AcceptStation":"到达：浙江乐清市乐成公司 发往：江苏江都市公司","AcceptTime":"2017-10-04 17:53:38"},{"AcceptStation":"到达：浙江乐清市乐成公司 已揽件","AcceptTime":"2017-10-04 18:00:45"},{"AcceptStation":"到达：浙江温州分拨中心","AcceptTime":"2017-10-04 20:36:13"},{"AcceptStation":"到达：浙江温州分拨中心 发往：江苏南京分拨中心","AcceptTime":"2017-10-04 20:39:57"},{"AcceptStation":"到达：江苏南京分拨中心 上级站点：浙江温州分拨中心","AcceptTime":"2017-10-05 07:43:28"},{"AcceptStation":"到达：江苏南京分拨中心 发往：江苏江都市公司","AcceptTime":"2017-10-05 07:52:24"},{"AcceptStation":"到达：江苏江都市公司 发往：江苏江都市公司吴桥便利店分部(15262279110)","AcceptTime":"2017-10-06 08:05:30"},{"AcceptStation":"到达：江苏江都市公司吴桥便利店分部 指定：吴方青(15262279110) 派送","AcceptTime":"2017-10-06 12:53:06"},{"AcceptStation":"已签收，签收人：本人，感谢使用韵达，期待再次为您服务","AcceptTime":"2017-10-06 16:35:29"}]
     * State : 3
     * EBusinessID : 1305220
     * Success : true
     */

    private String LogisticCode;
    private String ShipperCode;
    private String State;
    private String EBusinessID;
    private String Reason;

    public String getReason() {
        return Reason;
    }

    public void setReason(String reason) {
        Reason = reason;
    }

    private boolean Success;
    private List<TracesBean> Traces;

    public String getLogisticCode() {
        return LogisticCode;
    }

    public void setLogisticCode(String LogisticCode) {
        this.LogisticCode = LogisticCode;
    }

    public String getShipperCode() {
        return ShipperCode;
    }

    public void setShipperCode(String ShipperCode) {
        this.ShipperCode = ShipperCode;
    }

    public String getState() {
        return State;
    }

    public void setState(String State) {
        this.State = State;
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

    public List<TracesBean> getTraces() {
        return Traces;
    }

    public void setTraces(List<TracesBean> Traces) {
        this.Traces = Traces;
    }

    public static class TracesBean {
        /**
         * AcceptStation : 到达：浙江乐清市乐成公司 已揽件
         * AcceptTime : 2017-10-04 17:26:25
         */

        private String AcceptStation;
        private String AcceptTime;

        public String getAcceptStation() {
            return AcceptStation;
        }

        public void setAcceptStation(String AcceptStation) {
            this.AcceptStation = AcceptStation;
        }

        public String getAcceptTime() {
            return AcceptTime;
        }

        public void setAcceptTime(String AcceptTime) {
            this.AcceptTime = AcceptTime;
        }
    }
}
