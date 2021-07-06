package cn.huoxian.dongtai.plugin.action;

import cn.huoxian.dongtai.plugin.dialog.RemoteConfigDialog;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;

import static cn.huoxian.dongtai.plugin.util.TaintConstant.NAME_DONGTAI_IAST_RULE;

/**
 * @author niuerzhuang@huoxian.cn
 */
public class RemoteConfigAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        showDialog();
    }

    private void showDialog() {
        RemoteConfigDialog remoteConfigDialog = new RemoteConfigDialog();
        remoteConfigDialog.pack();
        remoteConfigDialog.setTitle(NAME_DONGTAI_IAST_RULE);
        remoteConfigDialog.setVisible(true);
    }


}