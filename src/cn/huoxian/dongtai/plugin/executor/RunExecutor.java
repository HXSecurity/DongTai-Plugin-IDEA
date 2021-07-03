package cn.huoxian.dongtai.plugin.executor;

import com.intellij.execution.executors.DefaultRunExecutor;
import com.intellij.openapi.util.IconLoader;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

/**
 * @author niuerzhuang@huoxian.cn
 **/
public class RunExecutor extends DefaultRunExecutor {

    public static final String RUN_ID = "Run with IAST";
    public static final String RUN_NAME = "Run with IAST";

    @NotNull
    @Override
    public String getId() {
        return RUN_ID;
    }

    @NotNull
    @Override
    public String getActionName() {
        return RUN_NAME;
    }

    @Override
    public String getDescription() {
        return RUN_ID;
    }

    @NotNull
    @Override
    public String getStartActionText() {
        return RUN_ID;
    }

    @NotNull
    @Override
    public String getToolWindowId() {
        return getId();
    }

    @NotNull
    @Override
    public Icon getIcon() {
        return IconLoader.getIcon("/assets/run-16.svg");
    }

    @NotNull
    @Override
    public Icon getToolWindowIcon() {
        return IconLoader.getIcon("/assets/run-13.svg");
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
