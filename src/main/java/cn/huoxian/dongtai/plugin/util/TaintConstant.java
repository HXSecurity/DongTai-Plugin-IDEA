package cn.huoxian.dongtai.plugin.util;

import javax.swing.table.DefaultTableModel;

/**
 * @author niuerzhuang@huoxian.cn
 **/
public interface TaintConstant {

    int DIALOG_SIZE_WIDTH = 320;

    int DIALOG_SIZE_HEIGHT = 360;

    String PROPERTY = System.getProperty("user.home");

    String RULESET_API_RULE_TYPE = "/api/v1/engine/hook/rule_types?type=";

    String RULESET_API_RULE_ADD = "/api/v1/engine/hook/rule/add";

    String TAINTS_API_GET = "/api/v1/plugin/vuln/list";

    String TAINTS_COUNT_API_GET = "/api/v1/plugin/vuln/count";

    String CONFIG_FILENAME_WINDOWS = PROPERTY + "/Library/iastagent/DongTaiConfig.properties";

    String CONFIG_FILENAME_MAC = "/Library/iastagent/DongTaiConfig.properties";

    String RULE_STAIN_SOURCE = "污点源方法规则";

    String RULE_SPREAD = "传播方法规则";

    String RULE_FILTER = "过滤方法规则";

    String RULE_DANGER = "危险方法规则";

    String CLASS_TYPE_INTERFACE = "INTERFACE";

    String SOURCE_TYPE_RETURN = "返回值";

    String SOURCE_TYPE_OBJECT = "对象";

    String SOURCE_TYPE_PARAMETER = "参数";

    String NAME_DONGTAI_IAST_RULE = "DongTai IAST 配置云端";

    String NAME_DONGTAI_IAST_RULE_ADD = "添加 HOOK 规则";

    String NOTIFICATION_CONTENT_ERROR_RULE = "规则详情未获取成功，请检查 DongTai IAST 配置云端 中的配置是否正确！";

    String NOTIFICATION_CONTENT_ERROR_CONNECT = "连接失败，请检查网络连接或 DongTai IAST 配置云端  中的配置！";

    String NOTIFICATION_CONTENT_ERROR_COMPLETE = "请求未发送成功，请检查规则信息填写是否完整！";

    String NOTIFICATION_CONTENT_ERROR_FAILURE = "请求未发送成功，请检查 DongTai IAST 配置云端 中的配置是否正确！";

    String NOTIFICATION_CONTENT_WARNING_METHOD = "您需要选择一个方法的方法名！";

    String NOTIFICATION_CONTENT_INFO_SUCCESS = "请求发送成功";

    String AGENT_NAME = "agent.jar";

    String URL = TaintUtil.fixUrl();

    String AGENT_URL = URL + "/api/v1/agent/download?url=" + URL + "&language=java";

    String AGENT_PATH_MAC = PROPERTY + "/Library/iastagent/";

    String AGENT_PATH_WINDOWS = PROPERTY + "/Library/iastagent/";

    String DEFAULT_URL = "https://iast.huoxian.cn";

    String DEFAULT_AGENT_URL = "http://openapi.iast.huoxian.cn:8000";

    String AGENT_CONFIG_PATH_MAC = AGENT_PATH_MAC + "config/iast.properties";

    String AGENT_CONFIG_PATH_WINDOWS = AGENT_PATH_WINDOWS + "config/iast.properties";

    String AGENT_VERSION_VALUE = "v1.0.0";

    String[] COLUMN_NAME = {"url", "漏洞类型", "等级", "source点", "sink点"};

    DefaultTableModel TABLE_MODEL = new DefaultTableModel(null, COLUMN_NAME);

    String TAINT_DETAIL = "https://iast.huoxian.cn/vuln/vulnDetail/1/";

    String AGENT_PATH = TaintUtil.os();

    String REQUEST_JSON_ERROR_STATUS = "202";

}
