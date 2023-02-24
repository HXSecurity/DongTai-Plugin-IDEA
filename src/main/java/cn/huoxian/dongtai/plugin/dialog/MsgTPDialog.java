package cn.huoxian.dongtai.plugin.dialog;/**
 * 文件描述
 *
 * @author Alex@huoxian.cn
 */

import cn.huoxian.dongtai.plugin.util.TaintUtil;

import javax.swing.*;

/**
 *Author:tanqiansheng@huoxian.cn
 * @date 2023年02月16日 14:42
 *Description:乾坤未定，你我皆是黑马。
 */
public class MsgTPDialog extends JDialog {

    public MsgTPDialog(JDialog jf, String title, boolean flag, String info) {

        super(jf, title, flag); //调用父类的构造器
        TaintUtil.notificationError(title+"："+info);
        JLabel jl = new JLabel(info);
        add(jl);
        setSize(350, 150);//大小

        setLocationRelativeTo(null);//居中

        setVisible(true);

        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);//点击关闭时,销毁对话框

    }

}