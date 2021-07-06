package cn.huoxian.dongtai.plugin.pojo;

import javax.swing.*;
import java.io.Serializable;

/**
 * @author niuerzhuang@huoxian.cn
 **/
public class Taint implements Serializable {
    private String id;
    private String type;
    private String level;
    private String url;
    private String http_method;
    private String top_stack;
    private String bottom_stack;
    private String detail;
    private JButton detailButton;

    public Taint() {
    }

    public Taint(String id, String type, String level, String url, String http_method, String top_stack, String bottom_stack, String detail) {
        this.id = id;
        this.type = type;
        this.level = level;
        this.url = url;
        this.http_method = http_method;
        this.top_stack = top_stack;
        this.bottom_stack = bottom_stack;
        this.detail = detail;
    }

    public JButton getDetailButton() {
        return detailButton;
    }

    public void setDetailButton(JButton detailButton) {
        this.detailButton = detailButton;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getHttp_method() {
        return http_method;
    }

    public void setHttp_method(String http_method) {
        this.http_method = http_method;
    }

    public String getTop_stack() {
        return top_stack;
    }

    public void setTop_stack(String top_stack) {
        this.top_stack = top_stack;
    }

    public String getBottom_stack() {
        return bottom_stack;
    }

    public void setBottom_stack(String bottom_stack) {
        this.bottom_stack = bottom_stack;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }
}
