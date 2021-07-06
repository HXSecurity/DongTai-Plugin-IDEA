package cn.huoxian.dongtai.plugin.pojo;

import java.io.Serializable;
import java.util.List;

/**
 * @author niuerzhuang@huoxian.cn
 **/
public class ResponseTaintCount implements Serializable {
    private String status;
    private String msg;
    private Integer count;

    public ResponseTaintCount() {
    }

    public ResponseTaintCount(String status, String msg, Integer count) {
        this.status = status;
        this.msg = msg;
        this.count = count;
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

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
