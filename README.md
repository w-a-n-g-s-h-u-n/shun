# 微服务快速开发脚手架
    基于Spring Cloud Hoxton.RELEASE的微服务开发脚手架
## 项目结构
``` lua
shun
├── shun-dependencies --外部依赖版本管理
├── shun-starter-parent -- 工具包 
├    ├── shun-starter-core -- 核心包
├    ├── shun-starter-web -- web项目
├    ├── shun-starter-db -- 数据操作功能
├    ├── shun-starter-cache -- 缓存功能
├    ├── shun-starter-cloud -- 云服务支持
├    ├── shun-starter-lcn -- 分布式事务
├    ├── shun-starter-sso -- web单点登录
├    ├── shun-starter-sso-webflux -- webflux单点登录
├    └── shun-starter-resource-server -- 受保护资源
└── shun-parent -- spring-boot-parent增强
```
## 特性
- Eureka服务注册中心
- Apollo配置中心
- OAuth2.0授权服务端
- SpringGateway网关
- Swagger聚合API文档
- SpringAdmin监控
- Skywalking链路追踪
- LCN分布式管理
- OSS大容量对象存储
- 分布式任务调度
- 消息推送中心（短信、邮件、社交）
- Jenkins CI/CD、Github Actions
- 容器部署
## 主要三方库版本
| 三方库 | 版本 |
| -- |-- |
| Spring Boot | 2.2.1.RELEASE |
| Spring Cloud | Hoxton.RELEASE |
| Fastjson | 1.2.58 |
| Mybatis Plus | 3.2.0 |
| Hutool | 5.0.6 |
| TX-LCN | 5.0.2.RELEASE |
| Apollo | 1.5.0 |
| SkyWalking | 6.2.0 |
| ShardingJdbc | 4.0.0-RC3 |
## 仓库地址
``` xml
<repositories>
    <repository>
        <id>shun</id>
        <name>shun</name>
        <url>https://raw.githubusercontent.com/w-a-n-g-s-h-u-n/shun/mvn-repo</url>
        <snapshots>
            <enabled>false</enabled>
        </snapshots>
    </repository>
</repositories>
```