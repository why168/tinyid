# Tinyid（重构版）

[![license](http://img.shields.io/badge/license-Apache2.0-brightgreen.svg?style=flat)](./LICENSE)
[![JDK](https://img.shields.io/badge/JDK-17+-green.svg)](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.x-brightgreen.svg)](https://spring.io/projects/spring-boot)

> 🚀 基于 [didi/tinyid](https://github.com/didi/tinyid) 的现代化重构版本

Tinyid 是一个高性能的分布式 ID 生成服务，提供 REST API 和 Java 客户端。使用 Java 客户端时，单实例 QPS 可达 **1000 万+**。

---

## ✨ 重构亮点

本项目 Fork 自 [didi/tinyid](https://github.com/didi/tinyid)，进行了以下现代化升级：

| 特性              | 原版本                  | 重构版本              |
|-----------------|----------------------|-------------------|
| **JDK**         | JDK 8                | JDK 17+           |
| **Spring Boot** | 1.x / 2.x            | 3.5.x             |
| **Web 容器**      | Tomcat               | Undertow（高性能）     |
| **MySQL 驱动**    | mysql-connector-java | mysql-connector-j |
| **日志框架**        | Log4j                | Log4j2            |

### 为什么选择这个版本？

- ✅ **现代化技术栈** - 使用最新的 Spring Boot 3.5.x 和 JDK 17
- ✅ **更高性能** - Undertow 容器提供更好的并发性能
- ✅ **安全性** - 依赖库更新至最新版本，修复已知安全漏洞
- ✅ **长期维护** - 基于 LTS 版本的 JDK，获得长期支持

---

## 🚀 快速开始

### 1. 克隆代码

```bash
git clone https://github.com/why168/tinyid.git
cd tinyid
```

### 2. 创建数据库表

```bash
cd tinyid-server
mysql -u root -p < db.sql
```

### 3. 配置数据源

编辑 `tinyid-server/src/main/resources/offline/application.properties`：

```properties
datasource.tinyid.names=primary

datasource.tinyid.primary.driver-class-name=com.mysql.cj.jdbc.Driver
datasource.tinyid.primary.url=jdbc:mysql://localhost:3306/tinyid?autoReconnect=true&useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai
datasource.tinyid.primary.username=root
datasource.tinyid.primary.password=your_password
```

### 4. 构建并启动

```bash
cd tinyid-server
sh build.sh offline
java -jar output/tinyid-server-1.0.0-SNAPSHOT.jar
```

服务默认运行在 `http://localhost:9999`

---

## 📡 REST API

### 1. 获取单个 ID

```bash
curl 'http://localhost:9999/tinyid/id/nextId?bizType=test&token=0f673adf80504e2eaa552f5d791b644c'
```

响应：

```json
{
  "data": [
    2
  ],
  "code": 200,
  "message": ""
}
```

### 2. 获取单个 ID（简单格式）

```bash
curl 'http://localhost:9999/tinyid/id/nextIdSimple?bizType=test&token=0f673adf80504e2eaa552f5d791b644c'
```

响应：`3`

### 3. 批量获取 ID

```bash
curl 'http://localhost:9999/tinyid/id/nextIdSimple?bizType=test&token=0f673adf80504e2eaa552f5d791b644c&batchSize=10'
```

响应：`4,5,6,7,8,9,10,11,12,13`

### 4. 获取奇数序列 ID

配置 `bizType=test_odd`（delta=2, remainder=1）：

```bash
curl 'http://localhost:9999/tinyid/id/nextIdSimple?bizType=test_odd&batchSize=10&token=0f673adf80504e2eaa552f5d791b644c'
```

响应：`3,5,7,9,11,13,15,17,19,21`

---

## 📦 Java 客户端（推荐）

### Maven 依赖

```xml
<dependency>
    <groupId>com.xiaoju.uemc.tinyid</groupId>
    <artifactId>tinyid-client</artifactId>
    <version>1.0.0-SNAPSHOT</version>
</dependency>
```

### 配置文件

在 classpath 下创建 `tinyid_client.properties`：

```properties
tinyid.server=localhost:9999
tinyid.token=0f673adf80504e2eaa552f5d791b644c
# 多服务器配置示例
# tinyid.server=ip1:port1,ip2:port2
```

### 代码示例

```java
import com.xiaoju.uemc.tinyid.client.utils.TinyId;

// 获取单个 ID
Long id = TinyId.nextId("test");

        // 批量获取 ID
List<Long> ids = TinyId.nextId("test", 10);
```

---

## 🏗️ 项目结构

```
tinyid/
├── tinyid-base/      # 核心基础模块
├── tinyid-client/    # Java 客户端
├── tinyid-server/    # HTTP 服务端
└── doc/              # 文档资源
```

---

## 📚 相关文档

- [原项目 Wiki](https://github.com/didi/tinyid/wiki) - 详细的设计文档和原理说明
- [原项目地址](https://github.com/didi/tinyid) - 滴滴开源的原始版本

---

## 🤝 贡献

欢迎提交 Issue 或 Pull Request！

---

## 📄 许可证

本项目基于 [Apache License 2.0](./LICENSE) 开源。

---

## ⚠️ 声明

本项目是基于 [didi/tinyid](https://github.com/didi/tinyid) 的个人 Fork 版本，非官方维护版本。原项目版权归滴滴出行所有。
