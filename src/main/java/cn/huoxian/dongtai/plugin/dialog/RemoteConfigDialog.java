package cn.huoxian.dongtai.plugin.dialog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.Map;

import static cn.huoxian.dongtai.plugin.util.TaintConstant.*;
import static cn.huoxian.dongtai.plugin.util.TaintUtil.*;

/**
 * @author niuerzhuang@huoxian.cn
 */
public class RemoteConfigDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField tokenTextArea;
    private JTextField urlTextField;
    private JTextField agentUrl;

    public static boolean isNewToken = false;

    public RemoteConfigDialog() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        setCenter();
        String agentUrlStr = config("AGENTURL");
        if (agentUrlStr == null || "".equals(agentUrlStr)) {
            agentUrl.setText(DEFAULT_AGENT_URL);
        } else {
            agentUrl.setText(agentUrlStr);
        }
        String urlStr = config("URL");
        if (urlStr == null || "".equals(urlStr)) {
            urlTextField.setText(DEFAULT_URL);
        } else {
            urlTextField.setText(urlStr);
        }
        tokenTextArea.setText(config("TOKEN"));
        buttonOK.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String url = urlTextField.getText();
                String token = tokenTextArea.getText();
                String agent = agentUrl.getText();
                Map<String, String> map = new HashMap<>(2);
                map.put("URL", url);
                map.put("TOKEN", token);
                map.put("AGENTURL", agent);
                configWrite(map);
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
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        int width = DIALOG_SIZE_WIDTH;
        int height = DIALOG_SIZE_HEIGHT;
        setBounds((d.width - width) / 2, (d.height - height) / 2, width, height);
    }
}
