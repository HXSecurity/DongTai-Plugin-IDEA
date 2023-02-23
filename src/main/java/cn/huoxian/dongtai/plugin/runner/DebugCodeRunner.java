package cn.huoxian.dongtai.plugin.runner;

import cn.huoxian.dongtai.plugin.dialog.RemoteConfigDialog;
import cn.huoxian.dongtai.plugin.executor.DebugExecutor;
import cn.huoxian.dongtai.plugin.util.ConfigUtil;
import cn.huoxian.dongtai.plugin.util.TaintConstant;
import cn.huoxian.dongtai.plugin.util.TaintUtil;
import com.intellij.debugger.impl.GenericDebuggerRunner;
import com.intellij.execution.ExecutionException;
import com.intellij.execution.configurations.*;
import com.intellij.execution.jar.JarApplicationConfiguration;
import com.intellij.execution.remote.RemoteConfiguration;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.execution.target.TargetEnvironmentAwareRunProfileState;
import com.intellij.execution.ui.RunContentDescriptor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.concurrency.Promise;

import java.util.logging.Logger;

import static cn.huoxian.dongtai.plugin.util.TaintUtil.downloadAgent;
import static cn.huoxian.dongtai.plugin.util.TaintUtil.notificationWarning;

/**
 * @author niuerzhuang@huoxian.cn
 **/
public class DebugCodeRunner extends GenericDebuggerRunner {
    private static final Logger logger = Logger.getLogger(RunCodeRunner.class.getName());
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
        JavaParameters parameters = ((JavaCommandLine) state).getJavaParameters();
        ParametersList parametersList = parameters.getVMParametersList();
        ConfigUtil.loadProperties(parameters);
        try {
            downloadAgent(TaintConstant.AGENT_URL, TaintConstant.AGENT_PATH);
        } catch (Exception e) {
            notificationWarning(TaintConstant.NOTIFICATION_CONTENT_ERROR_FAILURE);
            RemoteConfigDialog remoteConfigDialog = new RemoteConfigDialog();
            remoteConfigDialog.pack();
            remoteConfigDialog.setTitle(TaintConstant.NAME_DONGTAI_IAST_RULE);
            remoteConfigDialog.setVisible(true);
        }
        parametersList.add( ConfigUtil.getJavaAgent());
        parametersList.add("-Ddongtai.app.name=" +   ConfigUtil.getProjectName(env,parameters));
        parametersList.add("-Ddongtai.server.token=" +ConfigUtil.getOpenApiToken() );
        parametersList.add("-Ddongtai.log.level="+ConfigUtil.getLoglevel());
        parametersList.add("-Ddongtai.server.url=" +ConfigUtil.getURL());
        ConfigUtil.env=env;
        logger.info("IDEA"+ConfigUtil.getRunerIdeaVersion()+"Run With IAST 启动项目："   +ConfigUtil.projectName);
        TaintUtil.notificationWarning("IDEA："+ConfigUtil.getRunerIdeaVersion()+"Run With IAST 启动项目："   +ConfigUtil.projectName);
        return super.doExecute(state, env);

    }
    @Override
    protected @NotNull Promise<@Nullable RunContentDescriptor> doExecuteAsync(@NotNull TargetEnvironmentAwareRunProfileState state, @NotNull ExecutionEnvironment env) throws ExecutionException {
        System.out.println("start run doExecuteAsync");
        JavaParameters parameters = ((JavaCommandLine) state).getJavaParameters();
        ParametersList parametersList = parameters.getVMParametersList();
        ConfigUtil.loadProperties(parameters);
        try {

            downloadAgent(TaintConstant.AGENT_URL, TaintConstant.AGENT_PATH);
        } catch (Exception e) {
            notificationWarning(TaintConstant.NOTIFICATION_CONTENT_ERROR_FAILURE);
            RemoteConfigDialog remoteConfigDialog = new RemoteConfigDialog();
            remoteConfigDialog.pack();
            remoteConfigDialog.setTitle(TaintConstant.NAME_DONGTAI_IAST_RULE);
            remoteConfigDialog.setVisible(true);
        }

        parametersList.add(ConfigUtil.getJavaAgent());
        parametersList.add("-Ddongtai.app.name=" +   ConfigUtil.getProjectName(env,parameters));
        parametersList.add("-Ddongtai.server.token=" +ConfigUtil.getOpenApiToken() );
        parametersList.add("-Ddongtai.log.level="+ConfigUtil.getLoglevel());
        parametersList.add("-Ddongtai.server.url=" +ConfigUtil.getURL());
        ConfigUtil.env=env;
        logger.info("IDEA"+ConfigUtil.getRunerIdeaVersion()+"Run With IAST 启动项目："   +ConfigUtil.projectName);
        TaintUtil.notificationWarning("IDEA："+ConfigUtil.getRunerIdeaVersion()+"Run With IAST 启动项目："   +ConfigUtil.projectName);
        return super.doExecuteAsync(state, env);
    }
}
