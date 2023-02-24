package cn.huoxian.dongtai.plugin.util;/**
 * 文件描述
 *
 * @author Alex@huoxian.cn
 */

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *Author:tanqiansheng
 * @date 2023年02月08日 10:33
 *Description:乾坤未定，你我皆是黑马。
 */
public class UnicodeUtil {
    /**
     * @Title: unicodeEncode
     * @Description: unicode编码 将中文字符转换成Unicode字符
     * @param string
     * @return
     */
    public static String unicodeEncode(String string) {
        char[] utfBytes = string.toCharArray();
        String unicodeBytes = "";
        for (int i = 0; i < utfBytes.length; i++) {
            String hexB = Integer.toHexString(utfBytes[i]);
            if (hexB.length() <= 2) {
                hexB = "00" + hexB;
            }
            unicodeBytes = unicodeBytes + "\\u" + hexB;
        }
        return unicodeBytes;
    }
    /**
     * @param string
     * @return 转换之后的内容
     * @Title: unicodeDecode
     * @Description: unicode解码 将Unicode的编码转换为中文
     */
    public  static String unicodeDecode(String string) {
        Pattern pattern = Pattern.compile("(\\\\u(\\p{XDigit}{4}))");
        Matcher matcher = pattern.matcher(string);
        char ch;
        while (matcher.find()) {
            ch = (char) Integer.parseInt(matcher.group(2), 16);
            string = string.replace(matcher.group(1), ch + "");
        }
        return string;
    }
}
