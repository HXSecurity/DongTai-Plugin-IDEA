# DongTai-Plugin-IDEA

[![DongTai-project](https://img.shields.io/badge/DongTai%20versions-beta-green)](https://github.com/huoxianclub/DongTai)
[![DongTai--Plugin--IDEA](https://img.shields.io/badge/DongTai--Plugin--IDEA-v1.0.0-lightgrey)](https://github.com/HXSecurity/DongTai-Plugin-IDEA)
[![license GPL-3.0](https://img.shields.io/github/license/HXSecurity/DongTai-agent-java)](https://github.com/HXSecurity/DongTai-agent-java/blob/main/LICENSE)

- [English document](README.md)

## 项目介绍

DongTai-Plugin-IDEA 是洞态团队为 Java web应用开发人员开发的一款 IDEA 插件。该插件为应用开发时提供漏洞检测、代码审计等功能，使开发人员在应用开发阶段更加直观、快速、实时的发现应用漏洞。该插件具体功能：

1. 检测应用漏洞
2. 展示漏洞列表
3. 为某个方法快速添加 HOOK 规则

## 部署方案

基础环境：Intellij IDEA、Gradle

### 插件包部署

1. 下载 DongTai IAST 插件

    - [下载插件包](https://github.com/HXSecurity/DongTai-Plugin-IDEA/releases/download/v1.0/DongTai-Plugin-IDEA.zip)
   
      注：请勿解压 dongtai-idea-plugin.zip

2. 安装 DongTai IAST

   - 打开 IDEA 设置，点击 Plugins
   - 选择从本地安装 IDEA 插件，安装DongTai IAST插件安装包 **dongtai-idea-plugin.zip**
   - 应用插件：在插件列表中找到插件并打勾

### 源码部署

1. Fork [DongTai-Plugin-IDEA](https://github.com/HXSecurity/DongTai-Plugin-IDEA) 到自己的 GitHub 仓库并 clone: 

   ```shell
   git clone https://github.com/<your-username>/DongTai-Plugin-IDEA.git
   ```

2. 使用 Intellij IDEA 打开插件项目，使用 Gradle 解析项目

3. 如需要二次开发插件可修改源码，然后使用 Gradle Tasks intellij 组件的 `runIde` 命令调试插件功能，若无需求本步骤可跳过

4. 使用 Gradle Tasks intellij 组件的 `buildPlugin` 命令进行构建打包，插件包会在项目根目录下的build/distributions文件夹中，其目录结构为：
   
   ```shell
   build/distributions
   └── DongTai-Plugin-IDEA-2021.1.2.zip
   ```
   
5. 安装 DongTai IAST IDEA 插件
   - 打开 IDEA 设置，点击 Plugins
   - 选择从本地安装 IDEA 插件，安装DongTai IAST插件安装包 **dongtai-idea-plugin.zip**
   - 应用插件：在插件列表中找到插件并打勾

## 文档

- [插件快速使用](https://hxsecurity.github.io/DongTaiDoc/#/doc/tutorial/plugin)
- [官方文档](https://hxsecurity.github.io/DongTai-Doc/#/)
- [洞态官网](https://iast.huoxian.cn/)
