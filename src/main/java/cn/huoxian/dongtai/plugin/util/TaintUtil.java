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

import static cn.huoxian.dongtai.plugin.dialog.RemoteConfigDialog.isNewToken;

/**
 * @author niuerzhuang@huoxian.cn
 **/
public class TaintUtil {

    /**
     * 写入配置文件内容
     */
    public static void configWrite(Map<String, String> maps) {
        Properties properties = new Properties();
        try {
            File file = new File(TaintConstant.CONFIG_FILENAME);
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
     * 获取配置文件内容
     */
    public static Properties configRead() {
        try {
            File file = new File(TaintConstant.CONFIG_FILENAME);
            File path = new File(TaintConstant.IAST_AGENT_PATH);
            if (!path.isDirectory()) {
                path.mkdirs();
            }
            if (!file.exists()) {
                file.createNewFile();
            }
            InputStream in = new BufferedInputStream(new FileInputStream(TaintConstant.CONFIG_FILENAME));
            Properties p = new Properties();
            p.load(in);
            return p;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 解析配置文件内容
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
     * 下载 agrnt.jar
     */
    public static void downloadAgent(String url, String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            file.mkdirs();
        }
        File file1 = new File(filePath + File.separator + TaintConstant.AGENT_NAME);
        if ((!file1.exists()) || isNewToken) {
            isNewToken = false;
            FileOutputStream fileOut = null;
            HttpURLConnection conn = null;
            InputStream inputStream = null;
            try {
                URL httpUrl = new URL(url);
                conn = (HttpURLConnection) httpUrl.openConnection();
                conn.setRequestProperty("Content-type", "application/json; charset=utf-8");
                String token = config("TOKEN");
                conn.setRequestProperty("Authorization", "Token " + token);
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setUseCaches(false);
                conn.connect();
                inputStream = conn.getInputStream();
                BufferedInputStream bis = new BufferedInputStream(inputStream);
                fileOut = new FileOutputStream(filePath + File.separator + TaintConstant.AGENT_NAME);
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
            } catch (Exception e) {
                notificationError(TaintConstant.NOTIFICATION_CONTENT_ERROR_FAILURE);
                RemoteConfigDialog remoteConfigDialog = new RemoteConfigDialog();
                remoteConfigDialog.pack();
                remoteConfigDialog.setTitle(TaintConstant.NAME_DONGTAI_IAST_RULE);
                remoteConfigDialog.setVisible(true);
            }
        }
    }

    /**
     * url规范化
     */
    public static String fixUrl() {
        String url = "";
        try {
            url = TaintUtil.config("AGENTURL");
            String lastString = url.substring(url.length() - 1);
            if ("/".equals(lastString)) {
                url = url.substring(0, url.length() - 1);
            }
        } catch (Exception e) {
            url = TaintConstant.DEFAULT_AGENT_URL;
        }
        return url;
    }
}
