package cn.huoxian.dongtai.plugin.util;/**
 * 文件描述
 *
 * @author Alex@huoxian.cn
 */

import cn.huoxian.dongtai.plugin.dialog.RemoteConfigDialog;
import com.intellij.execution.configurations.JavaParameters;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.openapi.application.ApplicationInfo;
import com.intellij.openapi.application.ex.ApplicationInfoEx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static cn.huoxian.dongtai.plugin.util.TaintUtil.notificationWarning;

/**
 *Author:Alex@huoxian.cn
 * @date 2023年02月15日 16:23
 *Description:乾坤未定，你我皆是黑马。
 */
public class ConfigUtil {
    private static final Logger logger = LoggerFactory.getLogger(GetJson.class);
    public  static List<String> ideaNotGetVMList = new ArrayList<>();
    public  static Map<String, String> VMProperties=new HashMap<>();
    static {
     /*
        确保版本兼容，需要特殊处理的项目名称
        最低支持版本2019.2
        2020.1系列  2019.3 系列 需要兼容

        2020.3.4 正常
        2020.2.1 正常
        2019.2.4  正常
        2019.2.3  正常

        2020.1.4  不能设置项目名称
        2020.1.2  不能设置项目名称
        2019.3.5  不能设置项目名称
        2019.3.9  不能设置项目名称

        2019.1.4 安装运行按钮都没有
        2018.3.6 安装运行按钮都没有
        2018.1.8 安装运行按钮都没有
        */

        ideaNotGetVMList.add("2020.1");
        ideaNotGetVMList.add("2019.3");
    }
    public  static ExecutionEnvironment env;
    public  static String projectName;

    public static String projectVersion;
    public  static String getOpenApiToken(){
        String token = VMProperties.get("dongtai.server.token");
        if (!("").equals(token)&&token!=null){
           return token.trim();
        }
        else{
            String openapiToken = TaintUtil.config("OPENAPITOKEN");
            if (openapiToken!=null&&!("").equals(openapiToken)){
                return openapiToken;
            }else {
                return ReadManifest.readJarFileIastProperties("iast.server.token");
            }
        }
    }
    public  static String getJavaAgent(){
        String javaagent = VMProperties.get("javaagent");
        if (!("").equals(javaagent)&&javaagent!=null){
           return javaagent.trim();
        }
        else{
            return "-javaagent:" + TaintConstant.AGENT_PATH + "agent.jar";
        }
    }
    public  static String getURL(){
        String url = VMProperties.get("dongtai.server.url");
        if (!("").equals(url)&&url!=null){
            return url.trim();
        }
        else{
            String URL = TaintUtil.config("URL");
            if (URL!=null&&!("").equals(URL)){
                return URL.trim();
            }else {
                return TaintConstant.URL;
            }
        }

    }
    public  static String getLoglevel(){
        String level = VMProperties.get("dongtai.log.level");
        if (!("").equals(level)&&level!=null){
            level= level.trim();
        }
        else{
            level= TaintUtil.config("LOGLEVEL");
        }
        //打印通知
        if (level.equals("trace")){
            TaintUtil.isTrace=true;
        }
        else{
            TaintUtil.isTrace=false;
        }
        return level;
    }
    public static String getProjectVersion(){
        String r_version;
        String version = TaintUtil.config("PROJECTVERSION");
        logger.info("Get Project Version: " + version);
        if (!("").equals(version)&&version!=null){
            r_version = version;
        }
        else {
            r_version = "v1.0";
            TaintUtil.notificationWarning("未在配置中指定项目版本，当前使用默认版本v1.0");
        }
        ConfigUtil.projectVersion = r_version;
        logger.info("Return Project Version" + r_version);
        System.out.println("Get Project Version: " + r_version);
        return r_version;
    }
    public static String getProjectName(ExecutionEnvironment env, JavaParameters parametersList){
        String projectName =env.getProject().getName();
        //todo 不兼容的版本
        if (!isNormal()){
             if (projectName!=null&&("").equals(projectName)){
                 projectName = TaintUtil.config("PROJECTNAME");
             }
        }
        // todo 兼容的版本
        else{
            String VMProjectName = VMProperties.get("dongtai.app.name");
            if (VMProjectName!=null&&!("").equals(VMProjectName)){
                projectName=VMProjectName.trim();
            }
            else{
               String name= TaintUtil.config("PROJECTNAME");
               if (name!=null&&!("").equals(name)){
                   projectName=name;
               }
            }
            //解决一个项目使用不同的agent ，原因是刚运行后，又重新下载agent运行
            if(RemoteConfigDialog.isNewTokenToProject){
                if (projectName.equals(ConfigUtil.projectName)){
                    projectName=projectName+"01";
                    RemoteConfigDialog.isNewTokenToProject=false;
                }
            }
        }
        ConfigUtil.projectName=projectName;
        return ConfigUtil.projectName;
    }
    public  static  String getRunerIdeaVersion(){
        ApplicationInfoEx applicationInfo = (ApplicationInfoEx) ApplicationInfo.getInstance();
        String version = applicationInfo.getFullVersion();
        return version;
    }
    //解决无法 改项目名称的idea 目前的话 没用了 目前使用远程配置
    public static String generateProjectName(String projectType) {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HHmmss");
        String time = now.format(formatter);
        return projectType + "-" + time;
    }

    public static Boolean isNormal() {
        String runerIdeaVersion = getRunerIdeaVersion();
        for (String item : ideaNotGetVMList) {
            if (runerIdeaVersion.startsWith(item)){
                return false;
            }
        }
       return true;
    }

    public static void loadProperties(JavaParameters parameters) {
           VMProperties = parameters.getVMParametersList().getProperties();
           TaintUtil.infoToIdeaDubug("vm options的参数：");
           for (String item : parameters.getVMParametersList().getParameters()) {
                logger.info(item);
               TaintUtil.infoToIdeaDubug(item);
                if (item.contains("javaagent")){
                    if (!("").equals(item)&&item!=null){
                        VMProperties.put("javaagent",item);
                    }
                    break;
                }
            }
    }
    public static void showRemoteConfigDialog(){
        TaintUtil.notificationError("download fail");
        TaintUtil.notificationError("agent.jar有误，项目启动失败，请重新检查配置或选择清空agent缓存");
        notificationWarning(TaintConstant.NOTIFICATION_CONTENT_ERROR_FAILURE);
        RemoteConfigDialog remoteConfigDialog = new RemoteConfigDialog();
        remoteConfigDialog.pack();
        remoteConfigDialog.setTitle(TaintConstant.NAME_DONGTAI_IAST_RULE);
        remoteConfigDialog.setVisible(true);
    }
}
