package cn.huoxian.dongtai.plugin.dialog;

import cn.huoxian.dongtai.plugin.util.TaintConstant;
import cn.huoxian.dongtai.plugin.util.TaintUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.Map;

import static cn.huoxian.dongtai.plugin.util.TaintUtil.config;
import static cn.huoxian.dongtai.plugin.util.TaintUtil.configWrite;

/**
 * @author niuerzhuang@huoxian.cn
 */
public class RemoteConfigDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField openApiTokenTextArea;
    private JTextField serverTokenTextArea;
    private JTextField agentUrl;
    private JComboBox logLevelcomboBox;
    public static boolean isNewToken = false;
    public static boolean isNewTokenToProject = false;
    RemoteConfigDialog remoteConfigDialog = this;
    public RemoteConfigDialog() {

        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        setCenter();
        String agentUrlStr = config("URL");
        if (agentUrlStr == null || "".equals(agentUrlStr)) {
            agentUrl.setText(TaintConstant.DEFAULT_AGENT_URL);
        } else {
            agentUrl.setText(agentUrlStr);
        }
        String openApiToken = config("OPENAPITOKEN");
        openApiTokenTextArea.setText(openApiToken);
        String serverToken = config("TOKEN");
        if (openApiToken == null || "".equals(serverToken)) {
            serverTokenTextArea.setText(TaintConstant.TOKEN);
        } else {
            serverTokenTextArea.setText(serverToken);
        }
        buttonOK.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String serverToken = serverTokenTextArea.getText();
                String openApitoken = openApiTokenTextArea.getText();
                String url = agentUrl.getText();
                if (serverToken==null||serverToken.equals("")){
                    TaintUtil.notificationWarning("serverToker不能为空，请重配置IAST云端！");
                    new MsgTPDialog(remoteConfigDialog, "IAST云端提示", true, "Sorry! serverToker必须填写，请重配置IAST云端！!");
                    return;
                }
                if (url==null||url.equals("")){
                    new MsgTPDialog(remoteConfigDialog, "IAST云端提示", true, "Sorry! url必须填写，请重配置IAST云端");
                    TaintUtil.notificationWarning("url不能为空，请重配置IAST云端！");
                    return;
                }
                Map<String, String> remoteConfig = new HashMap<>(2);
                remoteConfig.put("URL", url);
                remoteConfig.put("TOKEN", serverToken);
                remoteConfig.put("OPENAPITOKEN", openApitoken);
                String logLevel = String.valueOf(logLevelcomboBox.getSelectedItem());
                if (!logLevel.equals("请选择等级类型")){
                    remoteConfig.put("LOGLEVEL", logLevel);
                }
                else{
                    remoteConfig.put("LOGLEVEL", "info");
                }

                configWrite(remoteConfig);
                onOK();
            }
        });
        buttonCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });
        contentPane.registerKeyboardAction(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onOK() {
        isNewTokenToProject=true;
        isNewToken = true;
        dispose();
    }

    private void onCancel() {
        dispose();
    }

    /**
     * 将 Dialog 居中
     */
    public void setCenter() {
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int width = TaintConstant.DIALOG_SIZE_WIDTH;
        int height = TaintConstant.DIALOG_SIZE_HEIGHT;
        setBounds((dimension.width - width) / 2, (dimension.height - height) / 2, width, height);
    }
}
