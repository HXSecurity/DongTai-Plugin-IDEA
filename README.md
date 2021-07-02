# DongTai-Plugin-IDEA
### 项目介绍

“火线～洞态IAST”是一款专为甲方安全人员、甲乙代码审计工程师和0 Day漏洞挖掘人员量身打造的辅助工具，可用于集成devops环境进行漏洞检测、作为代码审计的辅助工具和自动化挖掘0 Day。

“火线～洞态IAST”具有六大模块，分别是`DongTai-webapi`、`DongTai-openapi`、`DongTai-engine`、`DongTai-web`、`agent`、`DongTai-Plugin-IDEA`，其中：

- `DongTai-webapi`用于与`DongTai-web`交互，负责用户相关的API请求；
- `DongTai-openapi`用于与`agent`交互，处理agent上报的数据，向agent下发策略，控制agent的运行等
- `DongTai-engine`用于对`DongTai-openapi`接收到的数据进行分析、处理，计算存在的漏洞和可用的污点调用链等
- `DongTai-web`为“火线～洞态IAST”的前端项目，负责页面展示
- `agent`为各语言的数据采集端，从安装探针的项目中采集相对应的数据，发送至`DongTai-openapi`服务
- `DongTai-Plugin-IDEA`用于IDEA启动项目时，更加直观、快速、实时的查看项目漏洞、添加HOOK规则等

#### 应用场景

为甲方人员使用IDEA运行项目提供辅助，添加HOOK规则、查看项目漏洞等功能均在IDEA中就可实现。

#### 部署方案

1. 下载 DongTai IAST 插件

   - 离线下载安装包。[离线下载]()
   - IntelliJ IDEA plugins在线搜索：DongTai IAST（正在审核）

2. 安装 DongTai IAST

   1）离线

   - 打开 IDEA 设置，选择从本地安装 IDEA 插件，安装DongTai IAST插件安装包**dongtai-idea-plugin.zip**
   - 重启 IDEA 

   2）在线搜索（正在审核）
   
   - 打开 IDEA 设置，选择 Plugins，搜索 DongTai IAST，下载插件
   - 重启 IDEA


#### 文档

- [官方文档](https://hxsecurity.github.io/DongTaiDoc/#/doc/tutorial/plugin)
