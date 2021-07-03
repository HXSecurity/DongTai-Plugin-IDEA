package cn.huoxian.dongtai.plugin.pojo;

/**
 * @author niuerzhuang@huoxian.cn
 **/
public class TaintConvert {
    public static Object[] convert(Taint taint) {
        Object[] row = new String[6];
        row[0] = taint.getUrl();
        row[1] = taint.getHttp_method() + "出现" + taint.getType() + "漏洞";
        row[2] = taint.getLevel();
        row[3] = taint.getTop_stack();
        row[4] = taint.getBottom_stack();
        return row;
    }
}
