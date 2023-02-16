package cn.huoxian.dongtai.plugin.util;/**
 * 文件描述
 *
 * @author Alex@huoxian.cn
 */

import com.intellij.execution.runners.ExecutionEnvironment;

/**
 *Author:Alex@huoxian.cn
 * @date 2023年02月15日 16:23
 *Description:乾坤未定，你我皆是黑马。
 */
public class ConfigUtil {
    public  static ExecutionEnvironment env;
    public  static String projectName;
    public  static String getOpenApiToken(){
        String openapiToken = TaintUtil.config("OPENAPITOKEN");
        if (openapiToken!=null&&!openapiToken.equals("")){
           return openapiToken;
        }else {
        return ReadManifest.readJarFileIastProperties("iast.server.token");
        }
    }
    public  static String getURL(){
        String URL = TaintUtil.config("URL");
        if (URL!=null&&!URL.equals("")){
           return URL;
        }else {
            return TaintConstant.URL;
        }
    }
    public  static String getLoglevel(){
        return TaintUtil.config("LOGLEVEL");
    }

}
