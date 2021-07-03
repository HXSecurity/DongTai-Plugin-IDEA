package cn.huoxian.dongtai.plugin.dialog;

import cn.huoxian.dongtai.plugin.pojo.ResponseTaint;
import cn.huoxian.dongtai.plugin.pojo.ResponseTaintCount;
import cn.huoxian.dongtai.plugin.pojo.Taint;
import cn.huoxian.dongtai.plugin.pojo.TaintConvert;
import cn.huoxian.dongtai.plugin.util.GetJson;
import cn.huoxian.dongtai.plugin.util.TaintConstant;
import cn.huoxian.dongtai.plugin.util.TaintUtil;
import com.google.gson.Gson;
import org.apache.commons.lang.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

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
                refresh();
                timeTaskNotice(90000, 10000);
                TaintUtil.notificationInfo("已刷新");
            }
        });
        detailButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                try {
                    int selectedRow = contentTable.getSelectedRow();
                    Taint taint = taints.get(selectedRow);
                    String detail = taint.getDetail();
                    Desktop desktop = Desktop.getDesktop();
                    URI uri = new URI(detail);
                    desktop.browse(uri);
                } catch (Exception e) {
                    TaintUtil.notificationWarning("请在列表中选择漏洞");
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
        removeAll();
        json = GetJson.getTaintsJson();
        taints = getTaints(json);
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

    public void timeTaskNotice(Integer delay, Integer period) {
        Timer timer = new Timer();
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
        Gson gson = new Gson();
        ResponseTaint responseTaint = null;
        try {
            responseTaint = gson.fromJson(json, ResponseTaint.class);
            return responseTaint.getData();
        } catch (Exception exception) {
            return new ArrayList<>();
        }
    }

    public Integer getTaintsCount(String json) {
        Gson gson = new Gson();
        ResponseTaintCount responseTaintCount = null;
        try {
            responseTaintCount = gson.fromJson(json, ResponseTaintCount.class);
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
            for (Taint url : urls
            ) {
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
}
