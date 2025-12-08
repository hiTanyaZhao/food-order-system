# 餐厅点餐管理系统 (Food Order Management System)

> 🏆 **CS5200 Database Management Systems 课程项目**  
> 一个完整的餐厅点餐管理系统，实现了所有6个数据库表的完整CRUD功能

[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://www.oracle.com/java/)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-12+-blue.svg)](https://www.postgresql.org/)
[![Maven](https://img.shields.io/badge/Maven-3.6+-red.svg)](https://maven.apache.org/)
[![License](https://img.shields.io/badge/License-MIT-green.svg)](LICENSE)

## 🎯 项目概述

这是一个采用 **MVC架构** 设计的完整餐厅管理系统，使用 **Java 17 + PostgreSQL** 开发。系统涵盖了餐厅运营的核心业务流程：菜单管理、客户管理、员工管理、订单处理等功能。

### ✨ 核心特性

- 🍽️ **完整的菜单管理** - 分类浏览、智能搜索、统计分析
- 📦 **订单全生命周期管理** - 创建、修改、状态跟踪、自动分配
- 👥 **客户关系管理** - 注册、登录、历史订单查询
- 👨‍💼 **员工管理系统** - 信息管理、状态控制、工作负载统计
- 📊 **数据分析报告** - 销售统计、热门菜品、业务洞察

## 🚀 快速开始

### 环境要求

- **Java 17+**
- **PostgreSQL 12+** 
- **Maven 3.6+**

### 安装步骤

1. **克隆项目**
   ```bash
   git clone <repository-url>
   cd food-order-system
   ```

2. **数据库设置**
   ```sql
   -- 创建数据库
   CREATE DATABASE restaurant_db;
   
   -- 执行数据库脚本
   psql -d restaurant_db -f restaurant_db.sql
   ```

3. **配置数据库连接**
   
   编辑 `src/main/java/com/foodorder/config/DatabaseConnection.java`：
   ```java
   private static final String URL = "jdbc:postgresql://localhost:5432/restaurant_db";
   private static final String USERNAME = "your_username";
   private static final String PASSWORD = "your_password";
   ```

4. **编译运行**
   ```bash
   # 编译项目
   mvn compile
   
   # 运行系统
   mvn exec:java -Dexec.mainClass="com.foodorder.app.Main"
   ```

## 📊 数据库设计

### 表结构 (6个核心表)

| 表名 | 描述 | 主要字段 |
|------|------|----------|
| **Category** | 菜品分类 | `category_id`, `name` |
| **MenuItem** | 菜品信息 | `item_id`, `category_id`, `item_name`, `current_price`, `is_active` |
| **Customer** | 客户信息 | `customer_id`, `name`, `email`, `phone` |
| **Employee** | 员工信息 | `employee_id`, `name`, `phone`, `availability_status` |
| **Orders** | 订单信息 | `order_id`, `customer_id`, `employee_id`, `order_time`, `total_amount`, `current_status` |
| **OrderItem** | 订单详情 | `order_id`, `item_id`, `quantity` |

### 关系图
```
Category (1) ──── (N) MenuItem (N) ──── (M) OrderItem (M) ──── (1) Orders
                                                                    │
Customer (1) ────────────────────────────────────────────────────┘
                                                                    │
Employee (1) ────────────────────────────────────────────────────┘
```

## 🏗️ 系统架构

```
📁 src/main/java/com/foodorder/
├── 📁 app/           # 应用入口
├── 📁 config/        # 配置管理
├── 📁 model/         # 数据模型 (6个实体类)
├── 📁 dao/           # 数据访问层 (5个DAO类)
├── 📁 service/       # 业务逻辑层 (5个Service类)
└── 📁 controller/    # 控制层 (4个Controller类)
```

**严格的MVC三层架构**：
- **Model层** - 实体类，映射数据库表结构
- **DAO层** - 数据访问对象，封装SQL操作
- **Service层** - 业务逻辑处理，数据验证
- **Controller层** - 用户交互控制，界面管理

## 🎮 功能演示

### 主菜单界面
```
==============================================================
              🏪  餐厅点餐管理系统  🏪
==============================================================
1. 📋 菜单浏览与搜索
2. 📦 订单管理  
3. 👥 客户管理
4. 👨‍💼 员工管理
5. 📊 系统统计
6. 🚪 退出系统
==============================================================
```

### 核心功能模块

#### 1. 📋 菜单管理
- ✅ 按分类浏览所有菜品
- ✅ 智能搜索 (名称/分类/价格范围)
- ✅ 高级组合搜索
- ✅ 推荐菜品算法
- ✅ 菜单统计分析

#### 2. 📦 订单管理  
- ✅ 创建订单 (自动/手动分配员工)
- ✅ 订单状态管理 (`PENDING` → `ACCEPTED` → `PREPARING` → `COMPLETED`)
- ✅ 订单项增删改查
- ✅ 订单历史查询
- ✅ 实时金额计算

#### 3. 👥 客户管理
- ✅ 客户注册/登录
- ✅ 信息管理 (CRUD)
- ✅ 订单历史追踪
- ✅ 邮箱唯一性验证

#### 4. 👨‍💼 员工管理
- ✅ 员工信息管理
- ✅ 可用状态控制
- ✅ 工作负载统计
- ✅ 自动订单分配

#### 5. 📊 数据分析
- ✅ 系统运营概览
- ✅ 销售数据统计
- ✅ 热门菜品排行
- ✅ 员工绩效分析

## 💡 业务流程示例

### 完整订单处理流程

1. **客户注册** → 2. **浏览菜单** → 3. **创建订单** → 4. **添加菜品** → 5. **员工处理** → 6. **订单完成**

```bash
# 1. 客户管理 → 注册新客户
姓名: 张三
邮箱: zhangsan@email.com  
电话: 138****1234

# 2. 菜单浏览 → 查看可用菜品
分类: Main Course
菜品: Grilled Steak ($24.99)

# 3. 订单管理 → 创建新订单  
客户: 张三
员工: 自动分配 → Chef Marco Rossi
状态: PENDING

# 4. 添加订单项
菜品: Grilled Steak × 2
总金额: $49.98

# 5. 状态更新
PENDING → ACCEPTED → PREPARING → COMPLETED
```

## 🔧 技术特点

### 数据库设计
- ✅ **规范化设计** - 符合第三范式(3NF)
- ✅ **完整性约束** - 外键关系、数据验证
- ✅ **索引优化** - 提升查询性能
- ✅ **事务处理** - 保证数据一致性

### 代码质量
- ✅ **MVC架构** - 清晰的分层设计
- ✅ **设计模式** - 单例模式、DAO模式
- ✅ **异常处理** - 完善的错误处理机制
- ✅ **代码规范** - 详细注释、统一命名

### 用户体验
- ✅ **直观界面** - 清晰的菜单导航
- ✅ **操作反馈** - 详细的成功/错误提示
- ✅ **数据展示** - 格式化的表格显示
- ✅ **输入验证** - 友好的参数检查

## 📈 项目亮点

1. **🎯 完整性** - 涵盖餐厅业务全流程，6表完整CRUD
2. **🏗️ 架构性** - 严格MVC分层，22个类文件组织清晰  
3. **💼 实用性** - 真实业务场景，可直接部署使用
4. **🔒 健壮性** - 完善异常处理，数据验证机制
5. **📊 分析性** - 丰富统计功能，业务洞察支持
6. **🚀 扩展性** - 良好架构设计，便于功能扩展

## 📚 文档说明

| 文档 | 描述 |
|------|------|
| **README.md** | 项目概述和快速开始 (本文件) |
| **TECHNICAL_GUIDE.md** | 详细技术文档和使用指南 |
| **PROJECT_STRUCTURE.md** | 项目结构和架构说明 |
| **MENU_USAGE.md** | 菜单功能专项说明 |
| **demo_menu.md** | 菜单功能演示文档 |

## 🤝 贡献指南

欢迎提交 Issue 和 Pull Request 来改进项目！

## 📄 许可证

本项目采用 MIT 许可证 - 查看 [LICENSE](LICENSE) 文件了解详情

## 👨‍💻 作者

CS5200 Database Management Systems 课程项目

---

⭐ 如果这个项目对你有帮助，请给它一个 Star！
