package cn.huoxian.dongtai.plugin.dialog;/**
 * 文件描述
 *
 * @author Alex@huoxian.cn
 */

import com.intellij.openapi.ui.DialogWrapper;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;

/**
 *Author:Alex@huoxian.cn
 * @date 2023年02月15日 15:50
 *Description:乾坤未定，你我皆是黑马。
 */
public class WarnDialog  extends DialogWrapper {

    private  String content;



    public WarnDialog( String content) {
        super(true);
        this.content = content;
        init();//初始化dialog
        setTitle("警告");//设置对话框标题标题

    }

    /**
     * 创建对话框中间的内容面板
     * @return
     */
    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        //创建一个面板，设置其布局为边界布局
        JPanel centerPanel = new JPanel(new BorderLayout());
        //创建一个文字标签，来承载内容
        JLabel label = new JLabel(content);
        //设置首先大小
        label.setPreferredSize(new Dimension(100,100));
        //将文字标签添加的面板的正中间
        centerPanel.add(label,BorderLayout.CENTER);
        return centerPanel;
    }

    @Override
    protected void doOKAction() {
        this.dispose();
    }

    @Override
    public void doCancelAction() {
        this.dispose();
    }
    /**
     * 校验数据
     * @return 通过必须返回null，不通过返回一个 ValidationInfo 信息
     */
   /* @Nullable
    @Override
    protected ValidationInfo doValidate() {
        if((url==null||url.equals(""))||(serverToken==null||serverToken.equals(""))) {
            return new ValidationInfo("校验不通过");

        } else {
            return null;
        }
    }*/

}
