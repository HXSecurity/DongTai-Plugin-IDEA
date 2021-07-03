package cn.huoxian.dongtai.plugin.pojo;

import java.io.Serializable;
import java.util.List;

/**
 * @author niuerzhuang@huoxian.cn
 **/
public class ResponseTaint implements Serializable {

    private String status;
    private String msg;
    private List<Taint> data;

    public ResponseTaint() {
    }

    public ResponseTaint(String status, String msg, List<Taint> data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
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

    public List<Taint> getData() {
        return data;
    }

    public void setData(List<Taint> data) {
        this.data = data;
    }
}
