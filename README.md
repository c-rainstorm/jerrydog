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

- [静态资源服务器](#静态资源服务器)
- [简易 Servlet 容器](#简易-servlet-容器)
- [分离 Connector 模块](#分离-connector-模块)
- [独立 Container 模块](#独立-container-模块)

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

## 分离 Connector 模块

- 对应仓库 `TAG`： 还有些地方不理解，暂未打 `TAG`。
- 主要思路：分离出 `Bootstrap` 类用于启动 `Connector`, `Connector` 负责开启服务器监听并将作作相应处理，对 `Request` 和 `Response` 的处理较之前的两个版本更加细化。
- 测试 URL：
    1. `http://localhost:9704/index.html`
    1. `http://localhost:9704/servlet/TestServlet`

## 独立 Container 模块

- 对应仓库 `TAG`： `container`
- 主要思路： `container` 模块使用 `pipeline` 和 `valve` 来对 `request` 和 `response` 进行处理，`pipeline` 意为流水线，`valve` 意为阀，他们对请求的处理类似与过滤链和过滤器。`valve` 中有一个特殊的 `basic valve`, `Wrapper` 中的用来加载 `servlet` 和调用 `service` 方法，`Context` 中的用来映射到相应的 `Wrapper`。
- 测试 URL(因为使用了 `Tomcat` 的连接器，所以默认端口号为 8080；)：
    1. `http://localhost:8080/Primitive`
    1. `http://localhost:8080/Modern`
