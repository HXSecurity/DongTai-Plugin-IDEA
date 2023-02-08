package cn.huoxian.dongtai.plugin.agent;

import cn.huoxian.dongtai.plugin.util.TaintConstant;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Properties;
import java.util.regex.Pattern;

/**
 * @author niuerzhuang@huoxian.cn
 **/
public class AgentMassage {

    private static String AGENT_NAME = null;
    private static String engineName;
    private final static Pattern MAC_PATTERN = Pattern.compile("Mac.*");

    public static String getAgentToken() {
        if (AGENT_NAME == null || AGENT_NAME.length() < 40) {
            String osName = System.getProperty("os.name");
            String hostname = getInternalHostName();
            AGENT_NAME = osName + "-" + hostname + "-" + TaintConstant.AGENT_VERSION_VALUE + "-" + getEngineName();
        }
        return AGENT_NAME;
    }

    public static String getEngineName() {
        //每次启动的agentname都重新获取,idea的项目可能启动多次
//        if (null == engineName || engineName.length() < 2) {
            engineName = config("engine.name");
//        }
        return engineName;
    }

    /**
     * 获取主机名
     */
    public static String getInternalHostName() {
        if (System.getenv("COMPUTERNAME") != null) {
            return System.getenv("COMPUTERNAME");
        } else {
            return getHostNameForLinux();
        }
    }

    /**
     * 获取linux主机名
     */
    private static String getHostNameForLinux() {
        try {
            return (InetAddress.getLocalHost()).getHostName();
        } catch (UnknownHostException uhe) {
            String host = uhe.getMessage();
            if (host != null) {
                int colon = host.indexOf(':');
                if (colon > 0) {
                    return host.substring(0, colon);
                }
            }
            return "UnknownHost";
        }
    }

    /**
     * 获取配置文件内容
     */
    public static Properties configRead() {
        try {
            File file;
            String osName = System.getProperty("os.name");
            if (MAC_PATTERN.matcher(osName).find()) {
                file = new File(TaintConstant.AGENT_CONFIG_PATH_MAC);
            } else {
                file = new File(TaintConstant.AGENT_CONFIG_PATH_WINDOWS);
            }
            if (!file.exists()) {
                System.out.println("请使用 Run With IAST 启动项目");
                System.out.println("no file.............");
            }
            InputStream in;
            if (MAC_PATTERN.matcher(osName).find()) {
                in = new BufferedInputStream(new FileInputStream(TaintConstant.AGENT_CONFIG_PATH_MAC));
            } else {
                in = new BufferedInputStream(new FileInputStream(TaintConstant.AGENT_CONFIG_PATH_WINDOWS));
            }
            Properties p = new Properties();
            p.load(in);
            return p;
        } catch (Exception ignored) {
        }
        return null;
    }

    /**
     * 解析配置文件内容
     */
    public static String config(String configName) {
        Properties properties = configRead();
        String property = null;
        try {
            property = properties.getProperty(configName);
        } catch (Exception e) {
            property = "";
        }
        return property;
    }
}
