<template>
  <div class="register-page">
    <!-- 左侧品牌区域 -->
    <div class="brand-section">
      <div class="brand-content">
        <div class="logo">
          <div class="logo-icon">
            <svg viewBox="0 0 48 48" fill="none" xmlns="http://www.w3.org/2000/svg">
              <circle cx="24" cy="24" r="20" stroke="currentColor" stroke-width="2"/>
              <path d="M24 14v20M14 24h20" stroke="currentColor" stroke-width="2" stroke-linecap="round"/>
              <circle cx="24" cy="24" r="8" stroke="currentColor" stroke-width="2"/>
            </svg>
          </div>
          <span class="logo-text">UserHub</span>
        </div>
        <h1 class="brand-title">加入我们</h1>
        <p class="brand-desc">创建您的账户<br/>开启全新体验</p>
        <div class="features">
          <div class="feature-item">
            <div class="feature-icon">🔐</div>
            <span>安全加密</span>
          </div>
          <div class="feature-item">
            <div class="feature-icon">⚡</div>
            <span>极速响应</span>
          </div>
          <div class="feature-item">
            <div class="feature-icon">🛡️</div>
            <span>隐私保护</span>
          </div>
        </div>
      </div>
      <div class="brand-decoration">
        <div class="circle circle-1"></div>
        <div class="circle circle-2"></div>
        <div class="circle circle-3"></div>
      </div>
    </div>

    <!-- 右侧注册表单 -->
    <div class="form-section">
      <div class="form-wrapper">
        <div class="form-header">
          <h2>创建账户</h2>
          <p>填写以下信息完成注册</p>
        </div>

        <el-form ref="formRef" :model="form" :rules="rules" class="register-form">
          <div class="form-row">
            <div class="input-group">
              <label>用户名</label>
              <el-input 
                v-model="form.username" 
                placeholder="3-20位字符" 
                size="large"
                :prefix-icon="User"
              />
            </div>
            <div class="input-group">
              <label>密码</label>
              <el-input 
                v-model="form.password" 
                type="password" 
                placeholder="6-20位字符" 
                size="large" 
                show-password
                :prefix-icon="Lock"
              />
            </div>
          </div>
          <div class="form-row">
            <div class="input-group">
              <label>性别</label>
              <el-radio-group v-model="form.gender" class="gender-group">
                <el-radio :value="1">男</el-radio>
                <el-radio :value="0">女</el-radio>
              </el-radio-group>
            </div>
            <div class="input-group">
              <label>年龄</label>
              <el-input-number v-model="form.age" :min="1" :max="150" size="large" class="age-input" />
            </div>
          </div>
          <div class="input-group">
            <label>职业</label>
            <el-input 
              v-model="form.profession" 
              placeholder="请输入职业" 
              size="large"
              :prefix-icon="Briefcase"
            />
          </div>
          <div class="input-group">
            <label>住址</label>
            <el-input 
              v-model="form.address" 
              placeholder="请输入住址" 
              size="large"
              :prefix-icon="Location"
            />
          </div>
          <el-button 
            type="primary" 
            class="submit-btn" 
            :loading="loading" 
            @click="handleRegister"
          >
            <span v-if="!loading">注 册</span>
            <span v-else>注册中...</span>
          </el-button>
        </el-form>

        <div class="form-footer">
          <span>已有账号？</span>
          <router-link to="/login">立即登录</router-link>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { User, Lock, Briefcase, Location } from '@element-plus/icons-vue'
import { authApi } from '../api'

const router = useRouter()
const formRef = ref()
const loading = ref(false)

const form = reactive({
  username: '',
  password: '',
  gender: 1,
  age: null,
  profession: '',
  address: ''
})

const rules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 3, max: 20, message: '用户名长度3-20位', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, max: 20, message: '密码长度6-20位', trigger: 'blur' }
  ]
}

const handleRegister = async () => {
  await formRef.value.validate()
  loading.value = true
  try {
    await authApi.register(form)
    ElMessage.success('注册成功，请登录')
    router.push('/login')
  } finally {
    loading.value = false
  }
}
</script>

<style lang="scss" scoped>
.register-page {
  min-height: 100vh;
  display: flex;
  background: #f8fafc;
}

// 左侧品牌区域
.brand-section {
  flex: 0 0 420px;
  background: linear-gradient(135deg, #4f6ef7 0%, #6366f1 50%, #8b5cf6 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  position: relative;
  overflow: hidden;
  padding: 40px;

  @media (max-width: 900px) {
    display: none;
  }
}

.brand-content {
  position: relative;
  z-index: 2;
  color: white;
  max-width: 320px;
}

.logo {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 48px;

  .logo-icon {
    width: 48px;
    height: 48px;
    color: white;
  }

  .logo-text {
    font-size: 28px;
    font-weight: 700;
    letter-spacing: -0.5px;
  }
}

.brand-title {
  font-size: 42px;
  font-weight: 700;
  margin-bottom: 16px;
  line-height: 1.2;
}

.brand-desc {
  font-size: 18px;
  opacity: 0.9;
  line-height: 1.7;
  margin-bottom: 48px;
}

.features {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.feature-item {
  display: flex;
  align-items: center;
  gap: 12px;
  font-size: 15px;
  opacity: 0.9;

  .feature-icon {
    font-size: 20px;
  }
}

.brand-decoration {
  position: absolute;
  inset: 0;
  overflow: hidden;

  .circle {
    position: absolute;
    border-radius: 50%;
    background: rgba(255, 255, 255, 0.1);

    &.circle-1 {
      width: 400px;
      height: 400px;
      top: -100px;
      right: -100px;
    }

    &.circle-2 {
      width: 300px;
      height: 300px;
      bottom: -50px;
      left: -50px;
    }

    &.circle-3 {
      width: 200px;
      height: 200px;
      top: 50%;
      left: 50%;
      transform: translate(-50%, -50%);
      background: rgba(255, 255, 255, 0.05);
    }
  }
}

// 右侧表单区域
.form-section {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 40px;
  background: white;
  overflow-y: auto;

  @media (max-width: 900px) {
    min-height: 100vh;
  }
}

.form-wrapper {
  width: 100%;
  max-width: 500px;
}

.form-header {
  margin-bottom: 32px;

  h2 {
    font-size: 28px;
    font-weight: 700;
    color: #1e293b;
    margin-bottom: 8px;
  }

  p {
    color: #64748b;
    font-size: 15px;
  }
}

.register-form {
  .form-row {
    display: flex;
    gap: 20px;

    .input-group {
      flex: 1;
    }

    @media (max-width: 500px) {
      flex-direction: column;
      gap: 0;
    }
  }

  .input-group {
    margin-bottom: 20px;

    label {
      display: block;
      font-size: 14px;
      font-weight: 500;
      color: #374151;
      margin-bottom: 8px;
    }

    :deep(.el-input) {
      .el-input__wrapper {
        border-radius: 12px;
        padding: 4px 16px;
        box-shadow: 0 0 0 1px #e5e7eb;
        transition: all 0.2s;

        &:hover {
          box-shadow: 0 0 0 1px #c7d2fe;
        }

        &.is-focus {
          box-shadow: 0 0 0 2px #4f6ef7;
        }
      }

      .el-input__inner {
        height: 44px;
      }

      .el-input__prefix {
        color: #9ca3af;
      }
    }

    .gender-group {
      height: 52px;
      display: flex;
      align-items: center;
      padding-left: 8px;
    }

    .age-input {
      width: 100%;

      :deep(.el-input-number__decrease),
      :deep(.el-input-number__increase) {
        border-radius: 8px;
      }
    }
  }
}

.submit-btn {
  width: 100%;
  height: 50px;
  font-size: 16px;
  font-weight: 600;
  border-radius: 12px;
  background: linear-gradient(135deg, #4f6ef7 0%, #6366f1 100%);
  border: none;
  margin-top: 8px;
  transition: all 0.3s;

  &:hover {
    transform: translateY(-2px);
    box-shadow: 0 10px 30px rgba(79, 110, 247, 0.35);
  }

  &:active {
    transform: translateY(0);
  }
}

.form-footer {
  text-align: center;
  margin-top: 28px;
  font-size: 14px;
  color: #64748b;

  a {
    color: #4f6ef7;
    font-weight: 500;
    text-decoration: none;
    margin-left: 4px;

    &:hover {
      text-decoration: underline;
    }
  }
}
</style>
