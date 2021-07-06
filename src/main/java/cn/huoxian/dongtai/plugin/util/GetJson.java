package cn.huoxian.dongtai.plugin.util;

import cn.huoxian.dongtai.plugin.agent.AgentMassage;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import static cn.huoxian.dongtai.plugin.util.TaintConstant.*;
import static cn.huoxian.dongtai.plugin.util.TaintUtil.config;


/**
 * @author niuerzhuang@huoxian.cn
 **/
public class GetJson {
    /**
     * 获取规则集的 Json 字符串
     */
    public static String getRuleJson(int type) {
        String dongTaiUrlStr = config("URL") + RULESET_API_RULE_TYPE + type;
        CloseableHttpClient client = HttpClients.createDefault();
        HttpGet get = new HttpGet(dongTaiUrlStr);
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(1000).setConnectTimeout(1000).build();
        get.setConfig(requestConfig);
        try {
            get.addHeader("Content-type", "application/json; charset=utf-8");
            get.addHeader("Authorization", "Token " + config("TOKEN"));
            HttpResponse httpResponse = client.execute(get);
            HttpEntity entity = httpResponse.getEntity();
            StringBuilder document = new StringBuilder();
            BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent(), StandardCharsets.UTF_8));
            String line;
            while ((line = reader.readLine()) != null) {
                document.append(line);
            }
            reader.close();
            JSONObject jsonObject = JSONObject.parseObject(document.toString());
            return jsonObject.toJSONString();
        } catch (IOException ignore) {
        }
        return "";
    }

    public static String getTaintsJson() {
        try {
            String utf8 = "UTF-8";
            String agentName = URLEncoder.encode(AgentMassage.getAgentToken(), utf8);
            String taintsAPI = config("URL") + TAINTS_API_GET + "?name=" + agentName;
            CloseableHttpClient client = HttpClients.createDefault();
            HttpGet get = new HttpGet(taintsAPI);
            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(1000).setConnectTimeout(1000).build();
            get.setConfig(requestConfig);
            get.addHeader("Content-type", "application/json; charset=utf-8");
            get.addHeader("Authorization", "Token " + config("TOKEN"));
            HttpResponse httpResponse = client.execute(get);
            HttpEntity entity = httpResponse.getEntity();
            StringBuilder document = new StringBuilder();
            BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent(), StandardCharsets.UTF_8));
            String line;
            while ((line = reader.readLine()) != null) {
                document.append(line);
            }
            reader.close();
            return document.toString();
        } catch (IOException ignore) {
            return "";
        }
    }

    public static String getTaintsCountJson() {
        try {
            String utf8 = "UTF-8";
            String agentName = URLEncoder.encode(AgentMassage.getAgentToken(), utf8);
            String taintsAPI = config("URL") + TAINTS_COUNT_API_GET + "?name=" + agentName;
            CloseableHttpClient client = HttpClients.createDefault();
            HttpGet get = new HttpGet(taintsAPI);
            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(1000).setConnectTimeout(1000).build();
            get.setConfig(requestConfig);
            get.addHeader("Content-type", "application/json; charset=utf-8");
            get.addHeader("Authorization", "Token " + config("TOKEN"));
            HttpResponse httpResponse = client.execute(get);
            HttpEntity entity = httpResponse.getEntity();
            StringBuilder document = new StringBuilder();
            BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent(), StandardCharsets.UTF_8));
            String line;
            while ((line = reader.readLine()) != null) {
                document.append(line);
            }
            reader.close();
            return document.toString();
        } catch (IOException ignore) {
            return "";
        }
    }


}
