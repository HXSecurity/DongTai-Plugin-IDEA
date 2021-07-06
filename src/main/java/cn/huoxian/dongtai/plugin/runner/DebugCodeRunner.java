package cn.huoxian.dongtai.plugin.runner;

import cn.huoxian.dongtai.plugin.dialog.RemoteConfigDialog;
import cn.huoxian.dongtai.plugin.executor.DebugExecutor;
import com.intellij.debugger.impl.GenericDebuggerRunner;
import com.intellij.execution.ExecutionException;
import com.intellij.execution.configurations.*;
import com.intellij.execution.jar.JarApplicationConfiguration;
import com.intellij.execution.remote.RemoteConfiguration;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.execution.ui.RunContentDescriptor;
import org.jetbrains.annotations.NotNull;

import static cn.huoxian.dongtai.plugin.util.TaintConstant.*;
import static cn.huoxian.dongtai.plugin.util.TaintUtil.*;

/**
 * @author niuerzhuang@huoxian.cn
 **/
public class DebugCodeRunner extends GenericDebuggerRunner {

    @NotNull
    @Override
    public String getRunnerId() {
        return DebugExecutor.DEBUG_ID;
    }

    @Override
    public boolean canRun(@NotNull String executorId, @NotNull RunProfile profile) {
        return executorId.equals(DebugExecutor.DEBUG_ID) && (profile instanceof ModuleRunProfile || profile instanceof JarApplicationConfiguration)
                && !(profile instanceof RemoteConfiguration);
    }

    @Override
    public void execute(@NotNull ExecutionEnvironment env) throws ExecutionException {
        super.execute(env);
    }

    @Override
    protected RunContentDescriptor doExecute(@NotNull RunProfileState state, @NotNull ExecutionEnvironment env) throws ExecutionException {
        try {
            downloadAgent(AGENT_URL, AGENT_PATH);
        } catch (Exception e) {
            notificationWarning(NOTIFICATION_CONTENT_ERROR_FAILURE);
            RemoteConfigDialog remoteConfigDialog = new RemoteConfigDialog();
            remoteConfigDialog.pack();
            remoteConfigDialog.setTitle(NAME_DONGTAI_IAST_RULE);
            remoteConfigDialog.setVisible(true);
        }
        String name = env.getProject().getName();
        JavaParameters parameters = ((JavaCommandLine) state).getJavaParameters();
        ParametersList parametersList = parameters.getVMParametersList();
        parametersList.add("-javaagent:" + AGENT_PATH + "agent.jar");
        parametersList.add("-Dproject.name=" + name);
        return super.doExecute(state, env);
    }
}
