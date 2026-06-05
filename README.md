# CloudNest - 公寓租赁管理平台

<div align="center">

![CloudNest](https://img.shields.io/badge/CloudNest-v1.0-blue)
![Java](https://img.shields.io/badge/Language-Java-orange)
![License](https://img.shields.io/badge/License-MIT-green)
![Status](https://img.shields.io/badge/Status-Active-success)

**一个功能完整的公寓租赁平台，包含移动端应用和后台管理系统**

[功能特性](#功能特性) • [项目结构](#项目结构) • [快速开始](#快速开始) • [技术栈](#技术栈) • [贡献指南](#贡献指南)

</div>

---

## 📋 项目介绍

**CloudNest** 是一个专业的公寓租赁管理平台项目，为房东、租客和管理员提供完整的服务。项目由两个主要模块组成：

- **📱 移动端应用** - 租客使用的移动应用，提供浏览房源、提交申请等功能
- **🖥️ 后台管理系统** - 管理员专用平台，管理公寓、租赁和用户信息

---

## ✨ 功能特性

### 管理员后台管理系统
- **公寓（房源）管理**
  - 新增、编辑、删除公寓信息
  - 公寓图片上传和管理
  - 公寓状态管理（出租中、已出租、维护中等）

- **租赁管理**
  - 租赁合同创建和管理
  - 租赁期限管理

- **用户管理**
  - 用户账户管理
  - 权限分配和控制

### 移动端应用
- 公寓房源浏览
- 房源详情查看
- 租赁申请提交
- 个人账户管理

---

## 🏗️ 项目结构

```
CloudNest/
├── backend/                    # 后台管理系统（Java）
│   ├── src/
│   ├── pom.xml
│   └── ...
├── front-end/
│   ├── rentHouseAdmin/        # 后台管理前端（Vue3）
│   ├── rentHouseMobile/       # 移动端应用
│   └── ...
├── docs/                       # 项目文档
├── README.md                   # 项目说明文档
└── ...
```

---

## 🚀 快速开始

### 前置要求

- Java 8 或更高版本
- Maven 3.6.0 或更高版本
- MySQL 5.7 或更高版本
- Node.js 14.0 或更高版本
- npm 或 yarn
- Git

### 后端安装步骤

#### 1. 克隆仓库

```bash
git clone https://github.com/Abvolate-bernice/CloudNest.git
cd CloudNest
```

#### 2. 配置数据库

```bash
# 创建数据库
CREATE DATABASE cloudnest;

# 导入数据库脚本
mysql -u root -p cloudnest < docs/database/schema.sql
```

#### 3. 配置应用

编辑后端 `application.properties` 或 `application.yml` 文件：

```properties
# 数据库配置
spring.datasource.url=jdbc:mysql://localhost:3306/cloudnest
spring.datasource.username=root
spring.datasource.password=your_password
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
```

#### 4. 构建项目

```bash
cd backend
mvn clean install
```

#### 5. 运行应用

```bash
mvn spring-boot:run
```

应用将在 `http://localhost:8080` 启动

### 前端安装步骤

#### 后台管理系统

```bash
cd front-end/rentHouseAdmin

# 安装依赖
npm install

# 开发模式运行
npm run dev

# 生产环境构建
npm run build:prod
```


---

## 🛠️ 技术栈

### 后端技术
- **Java 8+** - 编程语言
- **Spring Boot** - 应用框架
- **Spring Data JPA** - 数据持久化
- **Spring Security** - 身份���证和授权
- **MySQL** - 关系数据库
- **Maven** - 项目构建管理

### 前端后台管理系统技术
- **Vue 3.2.45** - 前端框架
- **TypeScript** - 类型安全语言
- **Vite 4.0** - 项目构建工具
- **Pinia** - 状态管理
- **Element Plus** - UI 组件库
- **Vue Router 4** - 路由管理
- **Axios** - HTTP 请求库


### 开发工具
- IDE：IntelliJ IDEA / Eclipse / VSCode
- 版本控制：Git
- API 测试：Postman
- 构建工具：Maven、npm

---

## 📚 API 文档

### 公寓管理 API

#### 获取所有公寓
```
GET /api/apartments
Response: List<Apartment>
```

#### 获取公寓详情
```
GET /api/apartments/{id}
Response: Apartment
```

#### 创建公寓
```
POST /api/apartments
Body: Apartment
Response: Apartment
```

#### 更新公寓
```
PUT /api/apartments/{id}
Body: Apartment
Response: Apartment
```

#### 删除公寓
```
DELETE /api/apartments/{id}
Response: 204 No Content
```

更多 API 文档请查看 [API 文档](docs/API.md)

---

## 👥 使用场景

### 房东/管理员
1. 登录管理后台
2. 添加公寓房源信息
3. 管理租赁合同
4. 追踪租金账单
5. 管理租客信息

### 租客
1. 下载/访问移动应用
2. 注册/登录账户
3. 浏览公寓房源
4. 提交租赁申请
5. 管理租赁合同

---

## 🔐 安全性

- 密码加密存储
- JWT 令牌认证
- 请求参数验证
- SQL 注入防护
- 用户权限控制

---

## 📝 许可证

本项目采用 MIT 许可证。详见 [LICENSE](LICENSE) 文件。

---

## 🤝 贡献指南

欢迎贡献！请遵循以下步骤：

1. Fork 本仓库
2. 创建特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 开启 Pull Request

### 代码规范
- 后端：遵循 Java 命名规范
- 前端：遵循 Vue3 + TypeScript 代码规范
- 添加必要的注释和文档
- 确保代码通过测试

---

## 📞 联系方式

如有问题或建议，请通过以下方式联系：

- 📧 提交 [Issue](https://github.com/Abvolate-bernice/CloudNest/issues)
- 💬 发起 [Discussion](https://github.com/Abvolate-bernice/CloudNest/discussions)
- 👤 访问 [个人主页](https://github.com/Abvolate-bernice)

---

## 📊 项目统计

- 主要语言：Java
- 前端框架：Vue 3 + TypeScript
- 数据库：MySQL
- 最后更新：2026-06-05

---

<div align="center">

⭐ 如果这个项目对你有帮助，请给个 Star 支持一下！

[返回顶部](#cloudnest---公寓租赁管理平台)

</div>
