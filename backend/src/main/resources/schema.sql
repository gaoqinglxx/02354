-- 用户管理系统数据库初始化脚本
CREATE DATABASE IF NOT EXISTS user_mgmt DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE user_mgmt;

-- 用户表
DROP TABLE IF EXISTS sys_user;
CREATE TABLE sys_user (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    username VARCHAR(50) NOT NULL UNIQUE COMMENT '用户名',
    password VARCHAR(100) NOT NULL COMMENT '密码(BCrypt加密)',
    gender TINYINT DEFAULT 1 COMMENT '性别:0-女,1-男',
    age INT DEFAULT NULL COMMENT '年龄',
    profession VARCHAR(50) DEFAULT NULL COMMENT '职业',
    address VARCHAR(200) DEFAULT NULL COMMENT '住址',
    version INT DEFAULT 0 COMMENT '乐观锁版本号',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_username (username)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';

-- 测试账号需要通过注册接口创建，或手动插入正确格式的密码
-- 密码格式: BCrypt (由 Spring Security BCryptPasswordEncoder 生成)
