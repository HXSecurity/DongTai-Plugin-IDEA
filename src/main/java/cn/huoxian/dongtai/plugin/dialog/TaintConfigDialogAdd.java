package cn.huoxian.dongtai.plugin.dialog;

import com.alibaba.fastjson.JSONObject;
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
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import static cn.huoxian.dongtai.plugin.util.TaintConstant.*;
import static cn.huoxian.dongtai.plugin.util.TaintUtil.config;
import static cn.huoxian.dongtai.plugin.util.TaintUtil.notificationError;

/**
 * @author niuerzhuang@huoxian.cn
 */
public class TaintConfigDialogAdd extends JDialog {
    private JPanel contentPane;
    private JButton buttonOk;
    private JButton buttonCancel;
    private JLabel ruleSetLabel;
    private JLabel ruleSourceLabel;
    private JLabel ruleDetailLabel;
    private JLabel taintSourceLabel;
    private JLabel inheritanceDepthLabel;
    private JLabel taintTrackLabel;
    private JPanel taintTrackRadioButtonGroup;
    private JRadioButton taintTrackRadioButton2;
    private JRadioButton taintTrackRadioButton1;
    private JPanel inheritanceDepthRadioButtonGroup;
    private JRadioButton inheritanceDepthRadioButton1;
    private JRadioButton inheritanceDepthRadioButton3;
    private JRadioButton inheritanceDepthRadioButton2;
    private JComboBox<String> ruleSetComboBox;
    private JTextField ruleDetailTextField;
    private JComboBox<String> taintSourceComboBox;
    private JLabel taintSourceAddLabel;
    private JLabel taintSourceDelLabel;
    private JPanel taintSourcePanel;
    private JTextField taintSourceTextField;
    private JComboBox<String> ruleTypeComboBox;
    private JPanel taintSourceAddPanel;
    private JComboBox<String> taintSourceAddComboBox1;
    private JComboBox<String> taintSourceAddComboBox2;
    private JTextField taintSourceAddTextField;
    private int i = 0;
    private String url;
    private String token;
    private final JSONObject json = new JSONObject();

    public TaintConfigDialogAdd(String methodSignature, String classKind, Map<String, Integer> map) {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOk);
        taintSourceTextField.setVisible(false);
        setCenter();
        if (CLASS_TYPE_INTERFACE.equals(classKind)) {
            inheritanceDepthRadioButton2.setSelected(true);
        } else {
            inheritanceDepthRadioButton1.setSelected(true);
        }
        taintSourceTextField.setEditable(false);
        ruleDetailTextField.setText(methodSignature);
        ruleSetComboBox.addActionListener(e -> {
            String selectedItem = (String) ruleSetComboBox.getSelectedItem();
            if (RULE_STAIN_SOURCE.equals(selectedItem)) {
                setVisible(false);
                TaintConfigDialog dialog = new TaintConfigDialog(methodSignature, classKind);
                dialog.setRuleSet(1);
                dialog.pack();
                dialog.setVisible(true);
            } else if (RULE_SPREAD.equals(selectedItem)) {
                setVisible(false);
                TaintConfigDialog dialog = new TaintConfigDialog(methodSignature, classKind);
                dialog.setRuleSet(2);
                dialog.pack();
                dialog.setVisible(true);
            } else if (RULE_FILTER.equals(selectedItem)) {
                setVisible(false);
                TaintConfigDialog dialog = new TaintConfigDialog(methodSignature, classKind);
                dialog.setRuleSet(3);
                dialog.pack();
                dialog.setVisible(true);
            }
        });
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            ruleTypeComboBox.addItem(entry.getKey());
        }
        ruleDetailTextField.setText(methodSignature);
        taintSourceComboBox.addActionListener(e -> {
            String selectedItem = (String) taintSourceComboBox.getSelectedItem();
            if (SOURCE_TYPE_OBJECT.equals(selectedItem)) {
                taintSourceTextField.setVisible(false);
            } else if (SOURCE_TYPE_RETURN.equals(selectedItem)) {
                taintSourceTextField.setVisible(false);
            } else if (SOURCE_TYPE_PARAMETER.equals(selectedItem)) {
                taintSourceTextField.setVisible(true);
                taintSourceTextField.setEditable(true);
                TaintConfigDialogAdd.this.pack();
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
                    if (SOURCE_TYPE_OBJECT.equals(selectedItem)) {
                        taintSourceAddTextField.setVisible(false);
                    } else if (SOURCE_TYPE_RETURN.equals(selectedItem)) {
                        taintSourceAddTextField.setVisible(false);
                    } else if (SOURCE_TYPE_PARAMETER.equals(selectedItem)) {
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
                        if (str.equals(taintSourceAddTextField.getText())) {
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
        taintSourceDelLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                taintSourceAddPanel.remove(i - 1);
                i = i - 1;
                pa();
            }
        });
        buttonOk.addActionListener(e -> {
            url = config("URL");
            String lastString = url.substring(url.length() - 1);
            if ("/".equals(lastString)) {
                url = url.substring(0, url.length() - 1);
            }
            token = config("TOKEN");
            json.put("rule_type_id", map.get(ruleTypeComboBox.getSelectedItem()));
            json.put("rule_value", methodSignature);
            StringBuilder s1 = new StringBuilder();
            String selectedItem = (String) taintSourceComboBox.getSelectedItem();
            if (SOURCE_TYPE_OBJECT.equals(selectedItem)) {
                s1 = new StringBuilder("O");
            } else if (SOURCE_TYPE_RETURN.equals(selectedItem)) {
                s1 = new StringBuilder("R");
            } else if (SOURCE_TYPE_PARAMETER.equals(selectedItem)) {
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
                s1.append(wdAdd(re, wdly, par));
            }
            json.put("rule_source", s1.toString());
            if (taintTrackRadioButton1.isSelected()) {
                json.put("track", "true");
            } else if (taintTrackRadioButton2.isSelected()) {
                json.put("track", "false");
            }
            if (inheritanceDepthRadioButton1.isSelected()) {
                json.put("inherit", "false");
            } else if (inheritanceDepthRadioButton2.isSelected()) {
                json.put("inherit", "true");
            } else if (inheritanceDepthRadioButton3.isSelected()) {
                json.put("inherit", "all");
            }
            json.put("rule_target", "");
            requestJson(url + RULESET_API_RULE_ADD);
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

    /**
     * 自适应大小
     */
    public void pa() {
        this.pack();
    }

    public String wdAdd(String re, String wd, String par) {
        String result = "";
        String and = "和";
        if (and.equals(re)) {
            result += "&";
        } else {
            result += "|";
        }
        String parNum = "参数编号";
        if (parNum.equals(par)) {
            par = "";
        }
        if (SOURCE_TYPE_OBJECT.equals(wd)) {
            result += "O";
        } else if (SOURCE_TYPE_RETURN.equals(wd)) {
            result += "R";
        } else if (SOURCE_TYPE_PARAMETER.equals(wd)) {
            result = result + "P" + par;
        }
        return result;
    }

    /**
     * 将 Dialog 居中
     */
    public void setCenter() {
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        int width = DIALOG_SIZE_WIDTH;
        int height = DIALOG_SIZE_HEIGHT;
        setBounds((d.width - width) / 2, (d.height - height) / 2, width, height);
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
            StringBuilder document = new StringBuilder();
            BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent(), StandardCharsets.UTF_8));
            String line;
            while ((line = reader.readLine()) != null) {
                document.append(line);
            }
            reader.close();
            JSONObject jsonObject = JSONObject.parseObject(document.toString());
            String parameter = (String) jsonObject.get("status");
            if (REQUEST_JSON_ERROR_STATUS.equals(parameter)) {
                notificationError(NOTIFICATION_CONTENT_ERROR_COMPLETE);
            }
        } catch (IOException e1) {
            String content = "请求未发送成功，请检查 DongTai IAST自定义规则 中的配置是否正确！";
            notificationError(content);
        }
    }
}
