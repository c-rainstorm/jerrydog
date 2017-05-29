# jerrydog
从 0 搭建自己的简易版 Tomcat

## 静态资源服务器

- 主要思路： 使用 Java 网络编程 API，监听一个端口。当客户端连接时获取客户端的 `socket`，从该 `socket` 的 `inputstream` 中读取 `HTTP` 相应内容并解析，根据 `URI` 确定服务器静态文件位置，读取文件写入 `socket` 的 `outputstream` 中。
- 需要进行的配置：
    1. `clone` 下本仓库后, 配置环境变量 `JERRYDOG_HOME` 为仓库根目录
- 其他：
    1. 静态资源文件存放在 `webapps` 目录下
    1. 服务器监听端口为 `9704`
