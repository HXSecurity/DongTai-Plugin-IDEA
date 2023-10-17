package cn.huoxian.dongtai.plugin.dialog;

import cn.huoxian.dongtai.plugin.util.TaintConstant;
import cn.huoxian.dongtai.plugin.util.TaintUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

import static cn.huoxian.dongtai.plugin.util.TaintUtil.configWrite;

/**
 * @author tanqiansheng@huoxian.cn
 */
public class RemoteConfigDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField openApiTokenTextArea;
    private JTextField serverTokenTextArea;
    private JTextField agentUrl;
    private JComboBox logLevelcomboBox;
    private JTextField projectVersionTextArea;
    private JTextField projectNameTextArea;
    public static boolean isNewToken = false;
    public static boolean isNewTokenToProject = false;
    RemoteConfigDialog remoteConfigDialog = this;
    public RemoteConfigDialog() {

        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        setCenter();
        Properties properties = TaintUtil.configRead();
        String agentUrlStr = properties.getProperty("URL");
        if (agentUrlStr == null || "".equals(agentUrlStr)) {
            agentUrl.setText(TaintConstant.DEFAULT_AGENT_URL);
        } else {
            agentUrl.setText(agentUrlStr);
        }
        String openApiToken = properties.getProperty("OPENAPITOKEN");
        if (openApiToken == null || ("").equals(openApiToken)) {
            openApiTokenTextArea.setText(TaintConstant.OPENAPITOKEN);
        } else {
            openApiTokenTextArea.setText(openApiToken);
        }
        String serverToken =properties.getProperty("TOKEN");
        if (serverToken == null || "".equals(serverToken)) {
            serverTokenTextArea.setText(TaintConstant.TOKEN);
        } else {
            serverTokenTextArea.setText(serverToken);
        }
        String projectname = properties.getProperty("PROJECTNAME");
        if (projectname != null &&!"".equals(projectname)) {
            projectNameTextArea.setText(projectname);
        }
        String logLevel = properties.getProperty("LOGLEVEL");
        if (logLevel != null &&!"".equals(logLevel)) {
            logLevelcomboBox.setSelectedItem(logLevel);
        }
        // UzzJu.com
        String projectVersion = properties.getProperty("PROJECTVERSION");
        if (logLevel != null &&!"".equals(projectVersion)) {
            logLevelcomboBox.setSelectedItem(projectVersion);
        }
        buttonOK.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String serverToken = serverTokenTextArea.getText();
                String openApitoken = openApiTokenTextArea.getText();
                String url = agentUrl.getText();
                String projectName = projectNameTextArea.getText();
                String projectVersion = projectVersionTextArea.getText();
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
                if (openApitoken==null||openApitoken.equals("")){
                    new MsgTPDialog(remoteConfigDialog, "IAST云端提示", true, "Sorry! openApitoken必须填写，请重配置IAST云端");
                    TaintUtil.notificationWarning("openApitoken不能为空，请重配置IAST云端！");
                    return;
                }
                Map<String, String> remoteConfig = new LinkedHashMap<>(8);
                remoteConfig.put("URL", url);
                remoteConfig.put("TOKEN", serverToken);
                remoteConfig.put("OPENAPITOKEN", openApitoken);
                remoteConfig.put("PROJECTNAME", projectName);
                remoteConfig.put("PROJECTVERSION", projectVersion);
                String logLevel = String.valueOf(logLevelcomboBox.getSelectedItem());
                if (!logLevel.equals("请选择等级类型")){
                    remoteConfig.put("LOGLEVEL", logLevel);
                }
                else{
                    remoteConfig.put("LOGLEVEL", "info");
                }
                //打印通知
                if (logLevel.equals("trace")){
                    TaintUtil.isTrace=true;
                }
                else{
                    TaintUtil.isTrace=false;
                }
                configWrite(remoteConfig);
                TaintUtil.infoToIdeaDubug("写入DongTaiConfig.properties文件--->"+remoteConfig);
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
        // 确认用户是否想要关闭对话框
        int option = JOptionPane.showConfirmDialog(this, "确定要关闭配置云端吗？配置有误项目将无法启动！", "关闭配置云端", JOptionPane.YES_NO_OPTION);
        if (option == JOptionPane.YES_OPTION) {
            // 关闭对话框
            TaintUtil.isErrorStop=true;
            dispose();
        }

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
