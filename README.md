# 用户管理系统

基于 Spring Boot 3 + Vue 3 + MySQL 8 的用户注册、登录、信息管理系统。

## How to Run

```bash
# 首次启动（或需要重新初始化数据库时）
docker-compose down -v          # 清除旧容器和数据卷
docker-compose up --build -d    # 重新构建并启动

# 查看日志
docker-compose logs -f

# 停止服务（保留数据）
docker-compose down

# 停止服务并清除数据（下次启动会重新初始化数据库）
docker-compose down -v
```

## Services

| 服务  | 地址                  | 说明            |
| ----- | --------------------- | --------------- |
| 前端  | http://localhost:8081 | Vue3 用户界面   |
| 后端  | http://localhost:8080 | Spring Boot API |
| MySQL | localhost:3306        | 数据库          |

## 测试账号

由于密码使用动态盐加密，请通过注册页面创建新账号进行测试。

注册流程：

1. 访问 http://localhost:8081/register
2. 填写用户名（3-20位）、密码（6-20位）及其他信息
3. 注册成功后跳转登录页面

## 题目内容

帮我用java写一个用户注册、登录、查看用户信息的web程序，用户信息包括用户名、密码、性别、年龄、职业、住址等，可以运行。

---

## 功能特性

- 用户注册：支持用户名、密码、性别、年龄、职业、住址
- 用户登录：JWT Token 认证
- 个人信息：查看和编辑用户信息
- 安全特性：BCrypt 密码加密、JWT 认证、全局异常处理、参数校验

## 技术栈

- 后端：Java 17 + Spring Boot 3.2 + MyBatis-Plus + MySQL 8
- 前端：Vue 3 + Vite + Element Plus + Pinia + Axios
- 部署：Docker + Docker Compose
