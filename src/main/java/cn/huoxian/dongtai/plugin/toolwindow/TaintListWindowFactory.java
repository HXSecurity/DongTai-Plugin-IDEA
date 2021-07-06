package cn.huoxian.dongtai.plugin.toolwindow;

import cn.huoxian.dongtai.plugin.dialog.TaintListWindow;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import org.jetbrains.annotations.NotNull;

/**
 * @author niuerzhuang@huoxian.cn
 **/
public class TaintListWindowFactory implements ToolWindowFactory {
    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        TaintListWindow noteListWindow = new TaintListWindow();
        ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();
        Content content = contentFactory.createContent(noteListWindow.getJContent(), "", false);
        toolWindow.getContentManager().addContent(content);
    }
}
