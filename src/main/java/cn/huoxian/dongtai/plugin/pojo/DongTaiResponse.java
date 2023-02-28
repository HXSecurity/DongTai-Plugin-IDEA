package cn.huoxian.dongtai.plugin.pojo;

import java.io.Serializable;

/**
 * Author:Alex@huoxian.cn
 **/
public class DongTaiResponse implements Serializable {
    private String status;
    private String msg;

    public DongTaiResponse(String status, String msg) {
        this.status = status;
        this.msg = msg;
    }

    public DongTaiResponse() {
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "DongTaiResponse{" +
                "status='" + status + '\'' +
                ", msg='" + msg + '\'' +
                '}';
    }
}
