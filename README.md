# vt-boot

> A virtual-thread-first backend framework built on Spring Boot 3 and Java 21.

**vt-boot** 是一个以 **Java 虚拟线程（Virtual Threads）** 为核心设计理念的  
现代化 Spring Boot 单体后端框架。

它坚持 **同步编程模型**，但提供接近异步 / Reactive 的并发能力，  
让你无需 WebFlux，也能轻松构建高吞吐、高并发的后端应用。

---

## ✨ 核心特性

- 🚀 **Virtual Thread First**
    - Web / Async / Schedule 全链路虚拟线程
    - 官方支持，无 hack

- 🧱 **现代化技术栈**
    - Spring Boot 3.5.10
    - Java 21
    - Jakarta EE
    - Record / Pattern Matching / Sealed Class

- 🧠 **同步代码，异步级别性能**
    - 不引入 Reactive 心智负担
    - 保持传统 MVC 编程体验

- 🧩 **模块化 & Starter 化**
    - 用什么，引什么
    - 不强绑定、不强侵入

- 🏗️ **单体优先，工程化拉满**
    - 非 Spring Cloud
    - 适合新项目 / 中小型系统 / 创业项目

---

## ❌ vt-boot 不是做什么的

在使用之前，请确认你认同以下设计理念：

- ❌ 不是 WebFlux / Reactive 框架
- ❌ 不兼容 Spring Boot 2.x / JDK 8
- ❌ 不用于老项目升级
- ❌ 不强推微服务架构

👉 vt-boot **只为 Java 21+ 新项目而生**

---

## ⚙️ 技术栈要求

| 技术          | 版本             |
|-------------|----------------|
| Java        | 21+            |
| Spring Boot | 3.5.10         |
| Servlet 容器  | Tomcat / Jetty |

---

## 🚀 快速开始（5 分钟）

```bash
git clone git@github.com:Utomcat/vt-boot.git
cd vt-boot