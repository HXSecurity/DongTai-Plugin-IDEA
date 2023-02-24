package cn.huoxian.dongtai.plugin.action;

import cn.huoxian.dongtai.plugin.dialog.RemoteConfigDialog;
import cn.huoxian.dongtai.plugin.util.TaintConstant;
import cn.huoxian.dongtai.plugin.util.TaintUtil;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;

import java.io.File;

/**
 * 文件描述
 *
 * @author tanqiansheng@huoxian.cn
 */
public class ClearCacheAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        // TODO: insert action logic here
        RemoteConfigDialog.isNewToken = true;
        deleteJar();
    }

    private void deleteJar() {
        File file = new File( TaintConstant.AGENT_PATH + "agent.jar");
        if (file.exists()){
            boolean delete = file.delete();
            if (delete){
                TaintUtil.notificationInfo("清空缓存成功！");
            }
            else{
                TaintUtil.notificationInfo("文件已经被占用，重启项目将清空缓存！");
            }
        }
    }
}
