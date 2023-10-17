package cn.huoxian.dongtai.plugin.runner;

import cn.huoxian.dongtai.plugin.executor.RunExecutor;
import cn.huoxian.dongtai.plugin.util.ConfigUtil;
import cn.huoxian.dongtai.plugin.util.TaintConstant;
import cn.huoxian.dongtai.plugin.util.TaintUtil;
import com.intellij.execution.ExecutionException;
import com.intellij.execution.configurations.*;
import com.intellij.execution.impl.DefaultJavaProgramRunner;
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

/**
 * @author tanqiansheng@huoxian.cn
 **/
public class RunCodeRunner extends DefaultJavaProgramRunner {
    private static final Logger logger = Logger.getLogger(RunCodeRunner.class.getName());

    @NotNull
    @Override
    public String getRunnerId() {
        return RunExecutor.RUN_ID;
    }

    @Override
    public boolean canRun(@NotNull String executorId, @NotNull RunProfile profile) {
        return (executorId.equals(RunExecutor.RUN_ID) && (profile instanceof ModuleRunProfile || profile instanceof JarApplicationConfiguration) && !(profile instanceof RemoteConfiguration));
    }

    @Override
    public void execute(@NotNull ExecutionEnvironment env) throws ExecutionException {
        super.execute(env);
    }

    @Override
    protected RunContentDescriptor doExecute(@NotNull RunProfileState state, @NotNull ExecutionEnvironment env) throws ExecutionException {
        System.out.println("start run");
        logger.info("start run");
        JavaParameters parameters = ((JavaCommandLine) state).getJavaParameters();
        ParametersList parametersList = parameters.getVMParametersList();
        ConfigUtil.loadProperties(parameters);
        downloadAgent(TaintConstant.AGENT_URL, TaintConstant.AGENT_PATH);
        parametersList.add(ConfigUtil.getJavaAgent());
        parametersList.add("-Ddongtai.app.name=" +   ConfigUtil.getProjectName(env,parameters));
        parametersList.add("-Ddongtai.server.token=" +ConfigUtil.getOpenApiToken() );
        parametersList.add("-Ddongtai.log.level="+ConfigUtil.getLoglevel());
        parametersList.add("-Ddongtai.server.url=" +ConfigUtil.getURL());
        parametersList.add("-Ddongtai.app.version=" +ConfigUtil.getProjectVersion());
        ConfigUtil.env=env;
        logger.info("IDEA"+ConfigUtil.getRunerIdeaVersion()+"Run With IAST 启动项目："   +ConfigUtil.projectName + "项目版本: " + ConfigUtil.projectVersion);
        TaintUtil.notificationWarning("IDEA："+ConfigUtil.getRunerIdeaVersion()+"Run With IAST 启动项目："   +ConfigUtil.projectName + "项目版本: " + ConfigUtil.projectVersion);
        RunContentDescriptor runContentDescriptor=null;
        try {
            runContentDescriptor = super.doExecute(state, env);
        }
        catch (Exception e){
            TaintUtil.infoToIdeaDubug(e.getMessage());
            TaintUtil.notificationWarning("IDEA："+ConfigUtil.getRunerIdeaVersion()+"Run With IAST 启动项目："   +ConfigUtil.projectName+"失败，请检查云端配制！");
        }
        return runContentDescriptor;
    }

    @Override
    protected @NotNull Promise<@Nullable RunContentDescriptor> doExecuteAsync(@NotNull TargetEnvironmentAwareRunProfileState state, @NotNull ExecutionEnvironment env) throws ExecutionException {
        System.out.println("start run doExecuteAsync");
        JavaParameters parameters = ((JavaCommandLine) state).getJavaParameters();
        ParametersList parametersList = parameters.getVMParametersList();
        ConfigUtil.loadProperties(parameters);
        downloadAgent(TaintConstant.AGENT_URL, TaintConstant.AGENT_PATH);
        parametersList.add( ConfigUtil.getJavaAgent());
        parametersList.add("-Ddongtai.app.name=" +   ConfigUtil.getProjectName(env,parameters));
        parametersList.add("-Ddongtai.server.token=" +ConfigUtil.getOpenApiToken() );
        parametersList.add("-Ddongtai.log.level="+ConfigUtil.getLoglevel());
        parametersList.add("-Ddongtai.server.url=" +ConfigUtil.getURL());
        parametersList.add("-Ddongtai.app.version=" +ConfigUtil.getProjectVersion());
        ConfigUtil.env=env;
        logger.info("IDEA"+ConfigUtil.getRunerIdeaVersion()+"Run With IAST 启动项目："   +ConfigUtil.projectName + "项目版本: " + ConfigUtil.projectVersion);
        TaintUtil.notificationWarning("IDEA："+ConfigUtil.getRunerIdeaVersion()+"Run With IAST 启动项目："   +ConfigUtil.projectName + "项目版本: " + ConfigUtil.projectVersion);
        Promise  promise =null;
        try {
            promise = super.doExecuteAsync(state, env);
        }
        catch (Exception e){
            TaintUtil.infoToIdeaDubug(e.getMessage());
            TaintUtil.notificationWarning("IDEA："+ConfigUtil.getRunerIdeaVersion()+"Run With IAST 启动项目："   +ConfigUtil.projectName+"失败，请检查云端配制！");
        }
        return promise;
    }


}
