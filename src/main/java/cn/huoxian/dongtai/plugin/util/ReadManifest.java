package cn.huoxian.dongtai.plugin.util;/**
 * 文件描述
 *
 * @author Alex@huoxian.cn
 */

import cn.huoxian.dongtai.plugin.agent.AgentMassage;

import java.io.*;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 *Author:tanqiansheng@huoxian.cn
 * @date 2023年02月10日 15:56
 *Description:乾坤未定，你我皆是黑马。
 */
public class ReadManifest {

    public static String readJarFile(String FILE_PATH,String content)  {
        try {
            JarFile localJarFile = new JarFile(new File(FILE_PATH));
            Enumeration<JarEntry> entries = localJarFile.entries();
            while (entries.hasMoreElements()) {
                JarEntry jarEntry = entries.nextElement();
                String innerPath = jarEntry.getName();
                if (innerPath.endsWith("MANIFEST.MF")) {
                    InputStream inputStream = localJarFile.getInputStream(jarEntry);
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    while (bufferedReader.ready()) {
                        String line = bufferedReader.readLine();
                        if (line.startsWith(content)) {
                            String[] split = line.split(":");
                            return split[1].trim();
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
    public static String readJarFileIastProperties(String content)  {
        String osName = System.getProperty("os.name");
        String url;
        if (AgentMassage.MAC_PATTERN.matcher(osName).find()) {
            String home = System.getProperty("user.home");
            url=home + TaintConstant.JAR_FILENAME_MAC;
        } else {
            url=TaintConstant.JAR_FILENAME_WINDOWS;
        }
        try {
            JarFile localJarFile = new JarFile(new File(url));
            Enumeration<JarEntry> entries = localJarFile.entries();
            while (entries.hasMoreElements()) {
                JarEntry jarEntry = entries.nextElement();
                String innerPath = jarEntry.getName();
                if (innerPath.endsWith("iast.properties")) {
                    InputStream inputStream = localJarFile.getInputStream(jarEntry);
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    while (bufferedReader.ready()) {
                        String line = bufferedReader.readLine();
                        if (line.startsWith(content)) {
                            String[] split = line.split("=");
                            return split[1].trim();
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
