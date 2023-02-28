package cn.huoxian.dongtai.plugin.util;

import cn.huoxian.dongtai.plugin.dialog.RemoteConfigDialog;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationDisplayType;
import com.intellij.notification.NotificationGroup;
import com.intellij.notification.Notifications;
import com.intellij.openapi.ui.MessageType;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import static cn.huoxian.dongtai.plugin.dialog.RemoteConfigDialog.isNewToken;

/**
 * @author tanqiansheng@huoxian.cn
 **/
public class TaintUtil {
    private final static Pattern MAC_PATTERN = Pattern.compile("Mac.*");
    private static final Logger logger = Logger.getLogger(TaintUtil.class.getName());
    public static  boolean isTrace=false;
    public static  boolean isErrorStop=false;
    static {
        String level= TaintUtil.config("LOGLEVEL");
        if (level!=null&&!("trace").equals(level)){
            isTrace=true;
        }
    }
    /**
     * 写入配置文件内容,RemoteConfigDialog添加的配置文件
     */
    public static void configWrite(Map<String, String> maps) {

        Properties properties = new Properties();
        try {
            File file;
            String osName = System.getProperty("os.name");
            if (MAC_PATTERN.matcher(osName).find()) {
                String home = System.getProperty("user.home");
                file = new File(home + TaintConstant.CONFIG_FILENAME_MAC);
            } else {
                file = new File(TaintConstant.CONFIG_FILENAME_WINDOWS);
            }
            if (!file.exists()) {
                file.createNewFile();
            }
            for (String key : maps.keySet()) {
                properties.setProperty(key, maps.get(key));
            }
            FileOutputStream fos = new FileOutputStream(file);
            properties.store(fos, null);
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取配置文件内容 RemoteConfigDialog添加的配置文件
     */
    public static Properties configRead() {
        try {
            File file;
            File path;
            String osName = System.getProperty("os.name");
            String home = System.getProperty("user.home");
            if (MAC_PATTERN.matcher(osName).find()) {
                path = new File(home + "/Library/iastagent");
                file = new File(home + TaintConstant.CONFIG_FILENAME_MAC);
            } else {
                path = new File(home + "/Library/iastagent");
                file = new File(TaintConstant.CONFIG_FILENAME_WINDOWS);
            }
            if (!path.isDirectory()) {
                path.mkdirs();
            }
            if (!file.exists()) {
                file.createNewFile();
            }
            InputStream in;
            if (MAC_PATTERN.matcher(osName).find()) {
                in = new BufferedInputStream(new FileInputStream(home + TaintConstant.CONFIG_FILENAME_MAC));
            } else {
                in = new BufferedInputStream(new FileInputStream(TaintConstant.CONFIG_FILENAME_WINDOWS));
            }
            Properties p = new Properties();
            p.load(in);
            return p;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 解析配置文件内容,每次读取文件
     */
    public static String config(String configName) {

        Properties properties = configRead();
        return properties.getProperty(configName);
    }

    /**
     * 危险通知
     */
    public static void notificationError(String content) {
        NotificationGroup notificationGroup = new NotificationGroup("Custom Notification Group", NotificationDisplayType.BALLOON, true);
        Notification notification = notificationGroup.createNotification(content, MessageType.ERROR);
        Notifications.Bus.notify(notification);
    }

    /**
     * 警告通知
     */
    public static void notificationWarning(String content) {
        NotificationGroup notificationGroup = new NotificationGroup("Custom Notification Group", NotificationDisplayType.BALLOON, true);
        Notification notification = notificationGroup.createNotification(content, MessageType.WARNING);
        Notifications.Bus.notify(notification);
    }

    /**
     * 普通通知
     */
    public static void notificationInfo(String content) {
        NotificationGroup notificationGroup = new NotificationGroup("Custom Notification Group", NotificationDisplayType.BALLOON, true);
        Notification notification = notificationGroup.createNotification(content, MessageType.INFO);
        Notifications.Bus.notify(notification);
    }
    /**
     * 普通通知 Trace生效
     */
    public static void infoToIdeaDubug(String content) {
        if (isTrace){
            NotificationGroup notificationGroup = new NotificationGroup("Custom Notification Group", NotificationDisplayType.BALLOON, true);
            Notification notification = notificationGroup.createNotification(content, MessageType.INFO);
            Notifications.Bus.notify(notification);
        }

    }

    /**
     * 下载 agrnt.jar
     */
    public static void downloadAgent(String url, String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            file.mkdirs();
        }
        File file1 = new File(filePath + "agent.jar");
        if ((!file1.exists()) || file1.length() == 0 || isNewToken) {
            isNewToken = false;
            FileOutputStream fileOut = null;
            HttpURLConnection conn = null;
            InputStream inputStream = null;
            try {
                if (file1.exists()){
                    file1.delete();
                }
                infoToIdeaDubug("Start download");
                URL httpUrl = new URL(url);
                infoToIdeaDubug("下载的url："+url);
                infoToIdeaDubug("下载到的路径：："+filePath);
                logger.log(Level.OFF,"下载的url："+url);
                conn = (HttpURLConnection) httpUrl.openConnection();
                conn.setRequestProperty("Content-type", "application/json; charset=utf-8");
                String token = config("TOKEN");
                conn.setRequestProperty("Authorization", "Token " + token);
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setUseCaches(false);
                conn.connect();
                inputStream = conn.getInputStream();
                if (inputStream==null){
                    notificationWarning(TaintConstant.NOTIFICATION_CONTENT_ERROR_FAILURE);
                }
                BufferedInputStream bis = new BufferedInputStream(inputStream);
                fileOut = new FileOutputStream(filePath + TaintConstant.AGENT_NAME);
                infoToIdeaDubug("重新下载Agent包："+filePath + TaintConstant.AGENT_NAME);
                BufferedOutputStream bos = new BufferedOutputStream(fileOut);
                byte[] buf = new byte[4096];
                int length = bis.read(buf);
                while (length != -1) {
                    bos.write(buf, 0, length);
                    length = bis.read(buf);
                }
                bos.close();
                bis.close();
                conn.disconnect();
                infoToIdeaDubug("End download");
                if (!file1.exists() || file1.length() <= 0){
                    ConfigUtil.showRemoteConfigDialog();
                    if (!isErrorStop){
                        downloadAgent(url,filePath);

                    }else{
                        //停止循环
                        isErrorStop=false;
                       return;
                    }

                }
            } catch (Exception e) {
                ConfigUtil.showRemoteConfigDialog();
                if (!isErrorStop){
                    downloadAgent(url,filePath);
                }else{
                    //停止循环
                    isErrorStop=false;
                    return;
                }
            }
        }
    }

    /**
     * url规范化
     */
    public static String fixUrl() {
        String url = "";
        try {
            url = TaintUtil.config("URL");
            String lastString = url.substring(url.length() - 1);
            if ("/".equals(lastString)) {
                url = url.substring(0, url.length() - 1);
            }
        } catch (Exception e) {
            url = "http://iast.io";
        }
        return url;
    }

    /**
     * 辨别操作系统
     */
    public static String os() {
        String osName = System.getProperty("os.name");
        if (MAC_PATTERN.matcher(osName).find()) {
            return TaintConstant.AGENT_PATH_MAC;
        } else {
            return TaintConstant.AGENT_PATH_WINDOWS;
        }
    }

    public static boolean isNull(String str) {
        return str != null || !("").equals(str);
    }
    public static boolean isNumeric(String str) {
        if (str == null) {
            return false;
        }
        Pattern pattern = Pattern.compile("-?\\d+(\\.\\d+)?");
        return pattern.matcher(str).matches();
    }
}
