package cn.huoxian.dongtai.plugin.agent;

import cn.huoxian.dongtai.plugin.util.ReadManifest;
import cn.huoxian.dongtai.plugin.util.TaintConstant;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.regex.Pattern;

/**
 * @author niuerzhuang@huoxian.cn
 **/
public class AgentMassage {

    public final static Pattern MAC_PATTERN = Pattern.compile("Mac.*");
    private static String AGENT_NAME = null;
    private static String agentVersion;

    public static String getAgentToken() {
//        if (AGENT_NAME == null || AGENT_NAME.equals("")) {
        String osName = System.getProperty("os.name");
        if (osName.contains("Windows")) {
            String version = System.getProperty("os.version");
            osName = osName.substring(0, osName.indexOf(" ") + 1) + version.substring(0, version.indexOf("."));
        }
        String hostname = getInternalHostName();
        AGENT_NAME = osName + "-" + hostname + "-v" + getVersion() + "-" + getEngineName();
//        }
        return AGENT_NAME;
    }

    /**
     * agent端的openapi的Token
     *
     * @return
     */
    public static String getEngineName() {
//            engineName = config("engine.name");

        String agentName = ReadManifest.readJarFileIastProperties("engine.name");
        return agentName;
    }

    public static String getVersion() {
        if (agentVersion == null || agentVersion.equals("")) {
            agentVersion = readMfFile("Agent-Version");
        }
        return agentVersion;
    }

    public static String readMfFile(String content) {
        String osName = System.getProperty("os.name");
        String url;
        if (MAC_PATTERN.matcher(osName).find()) {
            String home = System.getProperty("user.home");
            url = home + TaintConstant.JAR_FILENAME_MAC;
        } else {
            url = TaintConstant.JAR_FILENAME_WINDOWS;
        }
        String name = ReadManifest.readJarFile(url, content);
        return name;
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

    /* *//**
     * 获取配置文件内容，读取的是agent端的iast.properties,openapiToken,现在弃用 直接读取jar包里面的iast.peopertites
     *//*
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
    }*/

    /**
     * 解析配置文件内容
     */
/*    public static String config(String configName) {
        Properties properties = configRead();
        String property = null;
        try {
            property = properties.getProperty(configName);
        } catch (Exception e) {
            property = "";
        }
        return property;
    }*/
}
