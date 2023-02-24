package cn.huoxian.dongtai.plugin.dialog;

import cn.huoxian.dongtai.plugin.pojo.ResponseTaint;
import cn.huoxian.dongtai.plugin.pojo.ResponseTaintCount;
import cn.huoxian.dongtai.plugin.pojo.Taint;
import cn.huoxian.dongtai.plugin.pojo.TaintConvert;
import cn.huoxian.dongtai.plugin.util.ConfigUtil;
import cn.huoxian.dongtai.plugin.util.GetJson;
import cn.huoxian.dongtai.plugin.util.TaintConstant;
import cn.huoxian.dongtai.plugin.util.TaintUtil;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.SystemUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Logger;

/**
 * @author niuerzhuang@huoxian.cn
 **/
public class TaintListWindow {
    private JPanel jContent;
    private JButton refreshListButton;
    private JTextField searchTextField;
    private JButton searchConfirmButton;
    private JButton searchResetButton;
    private JTable contentTable;
    private JComboBox<String> searchCriteria;
    private JButton detailButton;
    private JTextField detailTextField;
    private List<Taint> taints;
    private String json = "";
    private int size;
    Timer timer ;
    private static final Logger logger = Logger.getLogger(TaintListWindow.class.getName());

    public TaintListWindow() {
        init();
        searchConfirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                confirm((String) searchCriteria.getSelectedItem());
            }
        });
        searchResetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                searchTextField.setText("");
                refresh();
            }
        });
        refreshListButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if(ConfigUtil.env==null){
                    TaintUtil.notificationWarning("请使用 Run With IAST 启动项目");
                    if (timer!=null){
                        timer.cancel();
                    }
                }
                else {
                    refresh();
                    if (timer==null){
                        timer=new Timer();
                        timeTaskNotice(90000, 1000*60*20);
                    }
                    TaintUtil.notificationWarning("已刷新");
                }

            }
        });
        detailButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                try {
                    int selectedRow = contentTable.getSelectedRow();
                    Taint taint = taints.get(selectedRow);
                    String detail = taint.getDetail();
                    browseURL(detail);
                } catch (Exception e) {
                    TaintUtil.notificationError("sorry，系统不支持打开浏览器！");
                }
            }
        });
        contentTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                int selectedRow = contentTable.getSelectedRow();
                int selectedColumn = contentTable.getSelectedColumn();
                String valueAt = (String) contentTable.getValueAt(selectedRow, selectedColumn);
                detailTextField.setText(valueAt);
            }
        });
    }

    public void init() {
        contentTable.setModel(TaintConstant.TABLE_MODEL);
        contentTable.setEnabled(true);
        detailTextField.setEditable(false);
        refresh();
    }

    public void refresh() {
        int count=0;
        removeAll();
        json = GetJson.getTaintsJson();
        taints = getTaints(json);
        logger.info("taints->"+taints);
        TaintUtil.infoToIdeaDubug("taints->"+taints);
        try {
            size = taints.size();
            for (Taint taint : taints
            ) {

                taint.setDetail( TaintUtil.fixUrl()+TaintConstant.TAINT_DETAIL + taint.getId());
                TaintConstant.TABLE_MODEL.addRow(TaintConvert.convert(taint));
            }
        } catch (Exception e) {
            TaintUtil.infoToIdeaDubug("长度为"+size);
            size = 0;

        }
    }

    public void timeTaskNotice(Integer delay, Integer period) {


        timer.schedule(new TimerTask() {
            public void run() {
                json = GetJson.getTaintsJson();
                String newJson = GetJson.getTaintsCountJson();
                int newSize;
                try {
                    newSize = getTaintsCount(newJson);
                } catch (Exception e) {
                    newSize = size;
                }
                if (size != newSize) {
                    TaintUtil.notificationWarning("发现新漏洞");
                    List<Taint> newTaints = getTaints(json);
                    refreshWithJson(newTaints);
                }
            }
        }, delay, period);
    }

    public void refreshWithJson(List<Taint> newTaints) {
        removeAll();
        taints = newTaints;
        try {
            size = taints.size();
            for (Taint taint : taints
            ) {
                taint.setDetail(TaintConstant.TAINT_DETAIL + taint.getId());
                TaintConstant.TABLE_MODEL.addRow(TaintConvert.convert(taint));
            }
        } catch (Exception e) {
            size = 0;
        }
    }

    public List<Taint> getTaints(String json) {
        ResponseTaint responseTaint = null;
        try {
             responseTaint = JSONObject.parseObject(json, ResponseTaint.class);
            if (responseTaint.getStatus().equals(TaintConstant.REQUEST_JSON_ERROR_STATUS)){
                if(responseTaint.getMsg().equals("responseTaint.getMsg()")){
                    TaintUtil.notificationWarning("Iast云端配制有误，Agent不存在！");
                }
               else {
                    TaintUtil.notificationWarning(responseTaint.getMsg());
                }
            }
            return responseTaint.getData();
        } catch (Exception exception) {
            return new ArrayList<>();
        }
    }

    public Integer getTaintsCount(String json) {
        ResponseTaintCount responseTaintCount = null;
        try {
            responseTaintCount = JSONObject.parseObject(json, ResponseTaintCount.class);
            if (responseTaintCount.getStatus().equals(TaintConstant.REQUEST_JSON_ERROR_STATUS)){
                if(responseTaintCount.getMsg().equals("responseTaint.getMsg()")){
                    TaintUtil.notificationWarning("Iast云端配制有误，Agent不存在！");
                }
                else {
                    TaintUtil.notificationWarning(responseTaintCount.getMsg());
                }
                return null;
            }
            return responseTaintCount.getCount();
        } catch (Exception exception) {
            return null;
        }
    }

    public JPanel getJContent() {
        return jContent;
    }

    public List<Taint> searchUrl(String requirement) {
        List<Taint> urls = new ArrayList<>();
        for (Taint taint : taints
        ) {
            String url = taint.getUrl();
            if (StringUtils.containsIgnoreCase(url, requirement)) {
                urls.add(taint);
            }
        }
        return urls;
    }

    public List<Taint> searchLevel(String requirement) {
        List<Taint> levels = new ArrayList<>();
        for (Taint taint : taints
        ) {

            String level = taint.getLevel();
            if (StringUtils.containsIgnoreCase(level, requirement)) {
                levels.add(taint);
            }
        }
        return levels;
    }

    public void removeAll() {
        while (TaintConstant.TABLE_MODEL.getRowCount() > 0) {
            TaintConstant.TABLE_MODEL.removeRow(0);
        }
    }

    public void confirm(String confirm) {
        String urlConfirm = "url";
        String levelConfirm = "等级";
        if (urlConfirm.equals(confirm)) {
            removeAll();
            List<Taint> urls = searchUrl(searchTextField.getText());
            for (Taint url : urls) {
                TaintConstant.TABLE_MODEL.addRow(TaintConvert.convert(url));
            }
        }
        if (levelConfirm.equals(confirm)) {
            removeAll();
            List<Taint> levels = searchLevel(searchTextField.getText());
            for (Taint level : levels
            ) {
                TaintConstant.TABLE_MODEL.addRow(TaintConvert.convert(level));
            }
        }
    }
    public void browseURL(String urlString) {

        try {
            if (SystemUtils.IS_OS_LINUX) {
                if (Runtime.getRuntime().exec(new String[] { "which", "xdg-open" }).getInputStream().read() != -1) {
                    Runtime.getRuntime().exec(new String[] { "xdg-open", urlString });
                } else {
                    TaintUtil.notificationError("sorry，系统不支持打开浏览器！");
                }
            } else {
                if (Desktop.isDesktopSupported())
                {
                    Desktop.getDesktop().browse(new URI(urlString));
                } else {
                    TaintUtil.notificationError("sorry，系统不支持打开浏览器！");
                }
            }

        } catch (IOException | URISyntaxException e) {
            TaintUtil.notificationError("sorry，系统不支持打开浏览器！");
        }
    }
}
