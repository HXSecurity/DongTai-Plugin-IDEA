package cn.huoxian.dongtai.plugin.action;

import cn.huoxian.dongtai.plugin.dialog.TaintConfigDialog;
import cn.huoxian.dongtai.plugin.util.TaintUtil;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.psi.*;
import com.intellij.psi.impl.compiled.ClsFileImpl;
import com.intellij.psi.impl.source.PsiJavaFileImpl;

import java.util.Objects;

import static cn.huoxian.dongtai.plugin.util.TaintConstant.NAME_DONGTAI_IAST_RULE_ADD;
import static cn.huoxian.dongtai.plugin.util.TaintConstant.NOTIFICATION_CONTENT_WARNING_METHOD;

/**
 * @author niuerzhuang@huoxian.cn
 */
public class TaintConfigAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        showDialog(e);
    }

    private void showDialog(AnActionEvent e) {
        try {
            StringBuilder hook = new StringBuilder();
            PsiFile psiFile = e.getData(CommonDataKeys.PSI_FILE);
            PsiElement psiElement = e.getData(LangDataKeys.PSI_ELEMENT);
            PsiMethod psiMethod = (PsiMethod) psiElement;
            String packageName = getPackageName(psiFile);
            String className = getClassName(psiFile);
            String classKind = getClassKind(psiFile);
            String methodName = getMethodName(psiMethod, className);
            String parameterTypes = getParameterTypes(psiMethod);
            hook.append(packageName).append(".").append(className).append(".").append(methodName).append("(").append(parameterTypes);
            String method = String.valueOf(hook);
            TaintConfigDialog dialog = new TaintConfigDialog(method, classKind);
            dialog.pack();
            dialog.setSize(700, 300);
            dialog.setTitle(NAME_DONGTAI_IAST_RULE_ADD);
            dialog.setVisible(true);
        } catch (Exception ignore) {
            TaintUtil.notificationWarning(NOTIFICATION_CONTENT_WARNING_METHOD);
        }
    }

    private String getPackageName(PsiFile psiFile) {
        try {
            return ((PsiJavaFileImpl) psiFile).getPackageName();
        } catch (Exception ignore) {
            if (psiFile instanceof ClsFileImpl) {
                return ((ClsFileImpl) psiFile).getPackageName();
            }
        }
        return "<no package!>";
    }

    private String getClassName(PsiFile psiFile) {
        try {
            return ((PsiJavaFileImpl) psiFile).getClasses()[0].getName();
        } catch (Exception ignore) {
            if (psiFile instanceof ClsFileImpl) {
                return ((ClsFileImpl) psiFile).getClasses()[0].getName();
            }
        }
        return "<no class name!>";
    }

    private String getClassKind(PsiFile psiFile) {
        try {
            return ((PsiJavaFileImpl) psiFile).getClasses()[0].getClassKind().name();
        } catch (Exception ignore) {
            if (psiFile instanceof ClsFileImpl) {
                return ((ClsFileImpl) psiFile).getClasses()[0].getClassKind().name();
            }
        }
        return "<no class kind!>";
    }

    private String getMethodName(PsiMethod psiMethod, String className) {
        String methodName = psiMethod.getName();
        if (className.equals(methodName)) {
            methodName = "<init>";
        }
        return methodName;
    }

    private String getParameterTypes(PsiMethod psiMethod) {
        StringBuilder str = new StringBuilder();
        PsiType[] parameterTypes = psiMethod.getSignature(PsiSubstitutor.EMPTY).getParameterTypes();
        for (PsiType psiType : parameterTypes
        ) {
            str.append(psiType.getInternalCanonicalText()).append(",");
        }
        String superfluousWords = ",";
        if (str.lastIndexOf(superfluousWords) != -1) {
            str.deleteCharAt(str.lastIndexOf(",")).append(")");
        } else {
            str.append(")");
        }
        return String.valueOf(str);
    }
}
