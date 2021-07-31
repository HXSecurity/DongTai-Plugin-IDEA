# DongTai-Plugin-IDEA

[![DongTai-project](https://img.shields.io/badge/DongTai%20versions-beta-green)](https://github.com/huoxianclub/DongTai)
[![DongTai--Plugin--IDEA](https://img.shields.io/badge/DongTai--Plugin--IDEA-v1.0.0-lightgrey)](https://github.com/HXSecurity/DongTai-Plugin-IDEA)
[![license GPL-3.0](https://img.shields.io/github/license/HXSecurity/DongTai-agent-java)](https://github.com/HXSecurity/DongTai-agent-java/blob/main/LICENSE)

- [中文版本(Chinese version)](README_CN.md)

## Project Introduction

Dongtai-plugin-idea is an IDEA plug-in developed by DongTai team for Java Web application developers. This plugin provides functions such as vulnerability detection and code audit during application development, enabling developers to find application vulnerabilities more intuitively, quickly and in real time during application development. Specific functions of the plugin:

1. Detecting application vulnerabilities
2. Show vulnerability list
3. Add HOOK rules quickly

## Deploy

Basic environment: Intellij IDEA、Gradle

### Plugin Package to Deploy

1. Download DongTai IAST IDEA plugin

    - [Download](https://github.com/HXSecurity/DongTai-Plugin-IDEA/releases/download/v1.0/DongTai-Plugin-IDEA.zip) the installation package offline.
   notice：Don't unzip dongtai-idea-plugin.zip.

2. Install DongTai IAST IDEA plugin

   - Open IDEA Settings and click Plugins.
   - To install IDEA locally and install DongTai IAST plugin installation package **dongtai-idea-plugin.zip**.
   - Apply the plugin: Find the plugin in the plugin list and check it.

###  Source Code to Deploy

1. Fork [DongTai-Plugin-IDEA](https://github.com/HXSecurity/DongTai-Plugin-IDEA) and clone:

   ```shell
   git clone https://github.com/<your-username>/DongTai-Plugin-IDEA.git
   ```

2. Use Intellij IDEA to open the DongTai-Plugin-IDEA and use Gradle to parse it.

3. If you need to secondary development of the plug-in, you can modify the source code, and then use Gradle Tasks Intellij component `runIde` command to debug the plug-in function, if there is no need to skip this step.

4. Use the Gradle Tasks Intellij component's 'buildPlugin' command to build the package. The package will be in the build/ review folder at the root of the project. The directory structure is:

   ```shell
   build/distributions
   └── DongTai-Plugin-IDEA-2021.1.2.zip
   ```

5. Install DongTai IAST IDEA plugin
   - Open IDEA Settings and click Plugins.
   - To install IDEA locally and install DongTai IAST plugin installation package **dongtai-idea-plugin.zip**.
   - Apply the plugin: Find the plugin in the plugin list and check it.

## Contributing

Contributions are welcomed and greatly appreciated. See [CONTRIBUTING.md](https://github.com/HXSecurity/DongTai/blob/main/CONTRIBUTING.md) for details on submitting patches and the contribution workflow.

Any questions? Let's discuss in #sig-migrate in [#DongTai discussions](https://github.com/HXSecurity/DongTai/discussions)

## More resources

- [DongTai IDEA Plugin Quick Start](https://hxsecurity.github.io/DongTaiDoc/#/doc/tutorial/plugin)
- [Documentation](https://hxsecurity.github.io/DongTai-Doc/#/)
- [DongTai WebSite](https://iast.huoxian.cn/)


