package cn.huoxian.dongtai.plugin.dialog;

import cn.huoxian.dongtai.plugin.util.GetJson;
import cn.huoxian.dongtai.plugin.util.TaintConstant;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import static cn.huoxian.dongtai.plugin.util.TaintUtil.*;

/**
 * @author niuerzhuang@huoxian.cn
 */
public class TaintConfigDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOk;
    private JButton buttonCancel;
    private JLabel ruleSetLabel;
    private JLabel ruleTypeLabel;
    private JLabel ruleDetailLabel;
    private JLabel taintSourceLabel;
    private JLabel inheritanceDepthLabel;
    private JLabel taintGoLabel;
    private JPanel inheritanceDepthRadioButtonGroup;
    private JRadioButton inheritanceDepthRadioButton1;
    private JRadioButton inheritanceDepthRadioButton3;
    private JRadioButton inheritanceDepthRadioButton2;
    private JComboBox<String> ruleTypeComboBox;
    private JComboBox<String> ruleSetComboBox;
    private JTextField ruleDetailTextField;
    private JLabel taintSourceAddLabel;
    private JPanel taintSourcePanel;
    private JComboBox<String> taintSourceComboBox;
    private JTextField taintSourceTextField;
    private JComboBox<String> taintGoCombobox;
    private JTextField taintGoTextField;
    private JLabel taintGoAddLabel;
    private JPanel taintSourceAddPanel;
    private JLabel taintSourceDelLabel;
    private JPanel taintGoAddPanel;
    private JLabel taintGoDelPanel;
    private JComboBox<String> taintSourceAddComboBox1;
    private JComboBox<String> taintSourceAddComboBox2;
    private JTextField taintSourceAddTextField;
    private JComboBox<String> taintGoAddComboBox1;
    private JComboBox<String> taintGoAddComboBox2;
    private JTextField taintGoAddTextField;
    private final JSONObject json = new JSONObject();
    private String url;
    private String token;
    private int i = 0;
    private int j = 0;

    public TaintConfigDialog(String methodSignature, String classKind) {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOk);
        taintSourceTextField.setVisible(false);
        taintGoTextField.setVisible(false);
        String taintSourceMethodRules = GetJson.getRuleJson(1);
        String propagationMethodRules = GetJson.getRuleJson(2);
        String filterMethodRules = GetJson.getRuleJson(3);
        String dangerousMethodRules = GetJson.getRuleJson(4);
        if (taintSourceMethodRules == null || "".equals(taintSourceMethodRules)){
            notificationError(TaintConstant.NOTIFICATION_CONTENT_ERROR_CONNECT);
        }
        Map<String, Integer> ruleMap = new HashMap<>();
        setCenter();
        if (TaintConstant.CLASS_TYPE_INTERFACE.equals(classKind)) {
            inheritanceDepthRadioButton2.setSelected(true);
        } else {
            inheritanceDepthRadioButton1.setSelected(true);
        }
        taintSourceTextField.setEditable(false);
        taintGoTextField.setEditable(false);
        ruleDetailTextField.setText(methodSignature);
        ruleSetComboBox.addActionListener(e -> {
            try {
                String selectedItem = (String) ruleSetComboBox.getSelectedItem();
                if (TaintConstant.RULE_STAIN_SOURCE.equals(selectedItem)) {
                    ruleTypeComboBox.removeAllItems();
                    Map<String, Integer> propagationMethodRulesMap = setRuleSource(propagationMethodRules);
                    for (Map.Entry<String, Integer> entry : propagationMethodRulesMap.entrySet()) {
                        ruleTypeComboBox.addItem(entry.getKey());
                    }
                    ruleMap.putAll(propagationMethodRulesMap);
                } else if (TaintConstant.RULE_SPREAD.equals(selectedItem)) {
                    ruleTypeComboBox.removeAllItems();
                    Map<String, Integer> taintSourceMethodMap = setRuleSource(taintSourceMethodRules);
                    for (Map.Entry<String, Integer> entry : taintSourceMethodMap.entrySet()) {
                        ruleTypeComboBox.addItem(entry.getKey());
                    }
                    ruleMap.putAll(taintSourceMethodMap);
                } else if (TaintConstant.RULE_FILTER.equals(selectedItem)) {
                    ruleTypeComboBox.removeAllItems();
                    Map<String, Integer> filterMethodRulesMap = setRuleSource(filterMethodRules);
                    for (Map.Entry<String, Integer> entry : filterMethodRulesMap.entrySet()) {
                        ruleTypeComboBox.addItem(entry.getKey());
                    }
                    ruleMap.putAll(filterMethodRulesMap);
                } else if (TaintConstant.RULE_DANGER.equals(selectedItem)) {
                    setVisible(false);
                    ruleTypeComboBox.removeAllItems();
                    Map<String, Integer> dangerousMethodRulesMap = setRuleSource(dangerousMethodRules);
                    for (Map.Entry<String, Integer> entry : ruleMap.entrySet()) {
                        ruleTypeComboBox.addItem(entry.getKey());
                    }
                    ruleMap.putAll(dangerousMethodRulesMap);
                    TaintConfigDialogAdd dialog = new TaintConfigDialogAdd(methodSignature, classKind, dangerousMethodRulesMap);
                    dialog.pack();
                    dialog.setVisible(true);
                }
            }catch (Exception ignore){
                notificationError(TaintConstant.NOTIFICATION_CONTENT_ERROR_RULE);
                this.setVisible(false);
            }
        });
        taintSourceComboBox.addActionListener(e -> {
            String selectedItem = (String) taintSourceComboBox.getSelectedItem();
            if (TaintConstant.SOURCE_TYPE_OBJECT.equals(selectedItem)) {
                taintSourceTextField.setVisible(false);
            } else if (TaintConstant.SOURCE_TYPE_RETURN.equals(selectedItem)) {
                taintSourceTextField.setVisible(false);
            } else if (TaintConstant.SOURCE_TYPE_PARAMETER.equals(selectedItem)) {
                taintSourceTextField.setVisible(true);
                taintSourceTextField.setEditable(true);
                TaintConfigDialog.this.pack();
            } else {
                taintSourceTextField.setVisible(false);
            }
        });
        taintSourceAddPanel.setLayout(new BoxLayout(taintSourceAddPanel, BoxLayout.Y_AXIS));
        taintSourceAddLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                JPanel jPanel = new JPanel(new FlowLayout());
                jPanel.setVisible(true);
                taintSourceAddPanel.add(jPanel);
                i++;
                taintSourceAddComboBox1 = new JComboBox<>();
                taintSourceAddComboBox2 = new JComboBox<>();
                taintSourceAddComboBox1.addItem("和");
                taintSourceAddComboBox1.addItem("或");
                taintSourceAddComboBox2.addItem("对象");
                taintSourceAddComboBox2.addItem("返回值");
                taintSourceAddComboBox2.addItem("参数");
                taintSourceAddTextField = new JTextField("参数编号");
                taintSourceAddTextField.setToolTipText("参数编号，从\"1\"开始");
                taintSourceAddTextField.setVisible(false);
                taintSourceAddComboBox2.addActionListener(ex -> {
                    String selectedItem = (String) taintSourceAddComboBox2.getSelectedItem();
                    if (TaintConstant.SOURCE_TYPE_OBJECT.equals(selectedItem)) {
                        taintSourceAddTextField.setVisible(false);
                    } else if (TaintConstant.SOURCE_TYPE_RETURN.equals(selectedItem)) {
                        taintSourceAddTextField.setVisible(false);
                    } else if (TaintConstant.SOURCE_TYPE_PARAMETER.equals(selectedItem)) {
                        taintSourceAddTextField.setVisible(true);
                        pa();
                    } else {
                        taintSourceAddTextField.setVisible(false);
                    }
                });
                taintSourceAddTextField.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        super.mouseClicked(e);
                        String str = "参数编号";
                        if(str.equals(taintSourceTextField.getText())){
                            taintSourceAddTextField.setText("");
                        }
                    }
                });
                jPanel.add(taintSourceAddComboBox1);
                jPanel.add(taintSourceAddComboBox2);
                jPanel.add(taintSourceAddTextField);
                pa();
            }
        });
        taintSourceTextField.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                taintSourceTextField.setText("");
            }
        });
        taintSourceDelLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                try{
                    taintSourceAddPanel.remove(i - 1);
                }catch (Exception ignored){
                }
                i = i - 1;
                pa();
            }
        });
        taintGoCombobox.addActionListener(e -> {
            String selectedItem = (String) taintGoCombobox.getSelectedItem();
            if (TaintConstant.SOURCE_TYPE_OBJECT.equals(selectedItem)) {
                taintGoTextField.setVisible(false);
            } else if (TaintConstant.SOURCE_TYPE_RETURN.equals(selectedItem)) {
                taintGoTextField.setVisible(false);
            } else if (TaintConstant.SOURCE_TYPE_PARAMETER.equals(selectedItem)) {
                taintGoTextField.setVisible(true);
                taintGoTextField.setEditable(true);
                TaintConfigDialog.this.pack();
            } else {
                taintGoTextField.setVisible(false);
            }
        });
        taintGoAddPanel.setLayout(new BoxLayout(taintGoAddPanel, BoxLayout.Y_AXIS));
        taintGoAddLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                JPanel jPanel = new JPanel(new FlowLayout());
                jPanel.setVisible(true);
                taintGoAddPanel.add(jPanel);
                j++;
                taintGoAddComboBox1 = new JComboBox<>();
                taintGoAddComboBox2 = new JComboBox<>();
                taintGoAddComboBox1.addItem("和");
                taintGoAddComboBox1.addItem("或");
                taintGoAddComboBox2.addItem("对象");
                taintGoAddComboBox2.addItem("返回值");
                taintGoAddComboBox2.addItem("参数");
                taintGoAddTextField = new JTextField("参数编号");
                taintGoAddTextField.setToolTipText("参数编号，从\"1\"开始");
                taintGoAddTextField.setVisible(false);
                taintGoAddComboBox2.addActionListener(ex -> {
                    String selectedItem = (String) taintGoAddComboBox2.getSelectedItem();
                    if (TaintConstant.SOURCE_TYPE_OBJECT.equals(selectedItem)) {
                        taintGoAddTextField.setVisible(false);
                    } else if (TaintConstant.SOURCE_TYPE_RETURN.equals(selectedItem)) {
                        taintGoAddTextField.setVisible(false);
                    } else if (TaintConstant.SOURCE_TYPE_PARAMETER.equals(selectedItem)) {
                        taintGoAddTextField.setVisible(true);
                        pa();
                    } else {
                        taintGoAddTextField.setVisible(false);
                    }
                });
                taintGoAddTextField.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        super.mouseClicked(e);
                        String str = "参数编号";
                        if(str.equals(taintGoAddTextField.getText())){
                            taintGoAddTextField.setText("");
                        }
                    }
                });
                jPanel.add(taintGoAddComboBox1);
                jPanel.add(taintGoAddComboBox2);
                jPanel.add(taintGoAddTextField);
                pa();
            }
        });
        taintGoTextField.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                taintGoTextField.setText("");
            }
        });
        taintGoDelPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                try {
                    taintGoAddPanel.remove(j - 1);
                }catch (Exception ignored){
                }
                j = j - 1;
                pa();
            }
        });
        buttonOk.addActionListener(e -> {
            url = config("URL");
            String lastString = url.substring(url.length() - 1);
            if ("/".equals(lastString)){
                url = url.substring(0,url.length() - 1);
            }
            token = config("TOKEN");
            json.put("rule_type_id", ruleMap.get(ruleTypeComboBox.getSelectedItem()));
            json.put("rule_value", methodSignature);
            StringBuilder s1 = new StringBuilder();
            String selectedItem = (String) taintSourceComboBox.getSelectedItem();
            if (TaintConstant.SOURCE_TYPE_OBJECT.equals(selectedItem)) {
                s1 = new StringBuilder("O");
            } else if (TaintConstant.SOURCE_TYPE_RETURN.equals(selectedItem)) {
                s1 = new StringBuilder("R");
            } else if (TaintConstant.SOURCE_TYPE_PARAMETER.equals(selectedItem)) {
                String parameterNum = taintSourceTextField.getText();
                s1 = new StringBuilder("P" + parameterNum);
            }
            for (int k = 0; k < i; k++) {
                JPanel component = (JPanel) taintSourceAddPanel.getComponent(k);
                JComboBox<String> wdly1 = (JComboBox<String>) component.getComponent(0);
                JComboBox<String> wdly2 = (JComboBox<String>) component.getComponent(1);
                JTextField wdlyText = (JTextField) component.getComponent(2);
                String re = (String) wdly1.getSelectedItem();
                String wdly = (String) wdly2.getSelectedItem();
                String par = wdlyText.getText();
                s1.append(taintAdd(re, wdly, par));
            }
            json.put("rule_source", s1.toString());
            StringBuilder s2 = new StringBuilder();
            String selectedItem1 = (String) taintGoCombobox.getSelectedItem();
            if (TaintConstant.SOURCE_TYPE_OBJECT.equals(selectedItem1)) {
                s2 = new StringBuilder("O");
            } else if (TaintConstant.SOURCE_TYPE_RETURN.equals(selectedItem1)) {
                s2 = new StringBuilder("R");
            } else if (TaintConstant.SOURCE_TYPE_PARAMETER.equals(selectedItem1)) {
                String parameterNum = taintGoTextField.getText();
                json.put("rule_target", "P" + parameterNum);
                s2 = new StringBuilder("P" + parameterNum);
            }
            for (int k = 1; k < i; k++) {
                JPanel component = (JPanel) taintGoAddPanel.getComponent(k);
                JComboBox<String> wdqx1 = (JComboBox<String>) component.getComponent(0);
                JComboBox<String> wdqx2 = (JComboBox<String>) component.getComponent(1);
                JTextField wdlyText = (JTextField) component.getComponent(2);
                String re = (String) wdqx1.getSelectedItem();
                String wdly = (String) wdqx2.getSelectedItem();
                String par = wdlyText.getText();
                s2.append(taintAdd(re, wdly, par));
            }
            json.put("rule_target", s2.toString());
            if (inheritanceDepthRadioButton1.isSelected()) {
                json.put("inherit", "false");
            } else if (inheritanceDepthRadioButton2.isSelected()) {
                json.put("inherit", "true");
            } else if (inheritanceDepthRadioButton3.isSelected()) {
                json.put("inherit", "all");
            }
            json.put("track", "false");
            String s = JSON.toJSONString(json);
            requestJson(url + TaintConstant.RULESET_API_RULE_ADD);
            onOk();
        });
        buttonCancel.addActionListener(e -> onCancel());
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });
        contentPane.registerKeyboardAction(e -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }


    private void onOk() {
        dispose();
    }

    private void onCancel() {
        dispose();
    }

    private void requestJson(String url) {
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost post = new HttpPost(url);
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(1000).setConnectTimeout(1000).build();
        post.setConfig(requestConfig);
        try {
            post.addHeader("Content-type", "application/json; charset=utf-8");
            post.setHeader("Accept", "application/json");
            post.addHeader("Authorization", "Token " + token);
            post.setEntity(new StringEntity(json.toString(), StandardCharsets.UTF_8));
            HttpResponse httpResponse = client.execute(post);
            HttpEntity entity = httpResponse.getEntity();
            System.err.println("状态:" + httpResponse.getStatusLine());
            System.err.println("参数:" + EntityUtils.toString(entity));
            notificationInfo(TaintConstant.NOTIFICATION_CONTENT_INFO_SUCCESS);
        } catch (IOException e1) {
            notificationError(TaintConstant.NOTIFICATION_CONTENT_ERROR_FAILURE);
        }
    }

    public void setRuleSet(int num) {
        ruleSetComboBox.setSelectedIndex(num);
    }

    public Map<String, Integer> setRuleSource(String json) {

        JsonParser jp = new JsonParser();
        JsonObject jo = jp.parse(json).getAsJsonObject();
        JsonArray data = jo.get("data").getAsJsonArray();
        Map<String, Integer> ruleSourceMap = new HashMap<>();
        for (int i = 0; i < data.size(); i++) {
            JsonElement jsonElement = data.get(i);
            JsonElement id = jsonElement.getAsJsonObject().get("id");
            JsonElement name = jsonElement.getAsJsonObject().get("name");
            int asInt = id.getAsInt();
            String asString = name.getAsString();
            ruleSourceMap.put(asString, asInt);
        }
        return ruleSourceMap;
    }

    /**
     * 自适应大小
     */
    public void pa() {
        this.pack();
    }

    public String taintAdd(String re, String wd, String par) {
        String taintAddResult = "";
        String and = "和";
        if (and.equals(re)) {
            taintAddResult += "&";
        } else {
            taintAddResult += "|";
        }
        String parNum = "参数编号";
        if (parNum.equals(par)) {
            par = "";
        }
        if (TaintConstant.SOURCE_TYPE_OBJECT.equals(wd)) {
            taintAddResult += "O";
        } else if (TaintConstant.SOURCE_TYPE_RETURN.equals(wd)) {
            taintAddResult += "R";
        } else if (TaintConstant.SOURCE_TYPE_PARAMETER.equals(wd)) {
            taintAddResult = taintAddResult + "P" + par;
        }
        return taintAddResult;
    }

    /**
     * 将 Dialog 居中
     */
    public void setCenter(){
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int width = TaintConstant.DIALOG_SIZE_WIDTH;
        int height = TaintConstant.DIALOG_SIZE_HEIGHT;
        setBounds((dimension.width-width)/2, (dimension.height-height)/2, width, height);
    }

    public TaintConfigDialog() {
        ruleSetLabel.setVisible(true);
        ruleTypeLabel.setVisible(true);
        ruleDetailLabel.setVisible(true);
        taintSourceLabel.setVisible(true);
        inheritanceDepthLabel.setVisible(true);
        taintGoLabel.setVisible(true);
        inheritanceDepthRadioButtonGroup.setVisible(true);
        taintSourcePanel.setVisible(true);
        taintSourceAddComboBox1.setVisible(true);
        taintSourceAddComboBox2.setVisible(true);
        taintSourceAddTextField.setVisible(true);
        taintGoAddComboBox1.setVisible(true);
        taintGoAddComboBox2.setVisible(true);
    }

}
