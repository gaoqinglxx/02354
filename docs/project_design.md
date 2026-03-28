# 用户管理系统 - 项目设计文档

## 1. 系统架构

```mermaid
flowchart TD
    subgraph Frontend["前端 (Vue3 + Element Plus)"]
        A[登录页面] --> B[注册页面]
        A --> C[用户信息页面]
    end
    
    subgraph Backend["后端 (Spring Boot 3)"]
        D[AuthController] --> E[AuthService]
        F[UserController] --> G[UserService]
        E --> H[UserMapper]
        G --> H
    end
    
    subgraph Database["数据库 (MySQL 8)"]
        I[(sys_user)]
    end
    
    Frontend -->|HTTP/REST| Backend
    H --> Database
```

## 2. ER 图

```mermaid
erDiagram
    SYS_USER {
        bigint id PK "主键"
        varchar username UK "用户名"
        varchar password "密码(BCrypt)"
        tinyint gender "性别:0女1男"
        int age "年龄"
        varchar profession "职业"
        varchar address "住址"
        datetime create_time "创建时间"
        datetime update_time "更新时间"
    }
```

## 3. 接口清单

### AuthController - 认证接口
| 方法 | 路径 | 描述 |
|------|------|------|
| POST | /api/auth/register | 用户注册 |
| POST | /api/auth/login | 用户登录 |

### UserController - 用户接口
| 方法 | 路径 | 描述 |
|------|------|------|
| GET | /api/user/info | 获取当前用户信息 |
| PUT | /api/user/info | 更新用户信息 |

## 4. UI/UX 规范

- **主色调**: #409EFF (Element Plus 默认蓝)
- **成功色**: #67C23A
- **警告色**: #E6A23C
- **危险色**: #F56C6C
- **字体**: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto
- **圆角**: 8px (卡片), 4px (按钮/输入框)
- **间距**: 8px / 16px / 24px
- **阴影**: 0 2px 12px rgba(0,0,0,0.1)
