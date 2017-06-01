# jerrydog

从 0 搭建自己的简易版 Tomcat。

本仓库主要参考《How tomcat works》一书源码，有略微修改。可作参考。推荐直接看原书，原汁原味，而且讲的更详细。

当对服务器进行升级后，会将新版本打上 `TAG`，方便回溯。

项目使用 Maven 支持，若未使用过，可以自行下载 `pom.xml` 中的依赖库，添加到 `classpath` 中。

- 需要进行的配置：
    1. `clone` 下本仓库后, 配置环境变量 `JERRYDOG_HOME` 为仓库根目录。
- 其他：
    1. 静态资源文件存放在 `webroot` 目录下(第一个版本放在 `webapps` 下)。
    1. 服务器监听端口为 `9704`(没有女票，只能用我生日了^^)。

<!-- TOC -->

- [jerrydog](#jerrydog)
    - [静态资源服务器](#静态资源服务器)
    - [简易 Servlet 容器](#简易-servlet-容器)

<!-- /TOC -->

---

## 静态资源服务器

- 对应仓库 `TAG`： `static-resource-server`
- 主要思路： 使用 Java 网络编程 API，监听一个端口。当客户端连接时获取客户端的 `socket`，从该 `socket` 的 `inputstream` 中读取 `HTTP` 相应内容并解析，根据 `URI` 确定服务器静态文件位置，读取文件写入 `socket` 的 `outputstream` 中。
- 测试 URL： `http://localhost:9704/index.html`

## 简易 Servlet 容器

- 对应仓库 `TAG`： `simple-servlet-container`
- 主要思路: 使用 `URLClassloader` 加载 `servlet` 类并调用其 `service` 方法。使用了门面模式对 `Request` 和 `Response` 进行了封装，避免程序员获取过多权限。
- 测试 URL： `http://localhost:9704/servlet/TestServlet`
