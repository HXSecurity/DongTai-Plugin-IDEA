package cn.huoxian.dongtai.plugin.executor;

import com.intellij.execution.executors.DefaultRunExecutor;
import com.intellij.openapi.util.IconLoader;
import com.sun.istack.NotNull;

import javax.swing.*;

/**
 * @author niuerzhuang@huoxian.cn
 **/
public class DebugExecutor extends DefaultRunExecutor {

    public static final String DEBUG_ID = "Debug with IAST";
    public static final String DEBUG_NAME = "Debug with IAST";

    @NotNull
    @Override
    public String getId() {
        return DEBUG_ID;
    }

    @NotNull
    @Override
    public String getActionName() {
        return DEBUG_NAME;
    }

    @Override
    public String getDescription() {
        return DEBUG_ID;
    }

    @NotNull
    @Override
    public String getStartActionText() {
        return DEBUG_ID;
    }

    @NotNull
    @Override
    public String getToolWindowId() {
        return getId();
    }

    @NotNull
    @Override
    public Icon getIcon() {
        return IconLoader.getIcon("/assets/debug-16.svg");
    }

    @NotNull
    @Override
    public Icon getToolWindowIcon() {
        return IconLoader.getIcon("/assets/debug-13.svg");
    }

    @Override
    public Icon getDisabledIcon() {
        return null;
    }

    @Override
    public String getContextActionId() {
        return getId() + " context-action-does-not-exist";
    }

    @Override
    public String getHelpId() {
        return null;
    }

}
