<template>
  <div class="profile-container">
    <div class="profile-card">
      <div class="header">
        <h1 class="title">个人信息</h1>
        <el-button type="danger" plain @click="handleLogout">退出登录</el-button>
      </div>

      <div v-if="!editing" class="info-section">
        <div class="info-item">
          <span class="label">用户名</span>
          <span class="value">{{ userInfo?.username || '-' }}</span>
        </div>
        <div class="info-item">
          <span class="label">性别</span>
          <span class="value">{{ userInfo?.gender === 1 ? '男' : '女' }}</span>
        </div>
        <div class="info-item">
          <span class="label">年龄</span>
          <span class="value">{{ userInfo?.age || '-' }}</span>
        </div>
        <div class="info-item">
          <span class="label">职业</span>
          <span class="value">{{ userInfo?.profession || '-' }}</span>
        </div>
        <div class="info-item">
          <span class="label">住址</span>
          <span class="value">{{ userInfo?.address || '-' }}</span>
        </div>
        <div class="info-item">
          <span class="label">注册时间</span>
          <span class="value">{{ formatTime(userInfo?.createTime) }}</span>
        </div>
        <div class="btn-group">
          <el-button type="primary" @click="startEdit">编辑信息</el-button>
        </div>
      </div>

      <el-form v-else ref="formRef" :model="form" :rules="rules" label-position="top" class="edit-form">
        <el-form-item label="性别" prop="gender">
          <el-radio-group v-model="form.gender">
            <el-radio :value="1">男</el-radio>
            <el-radio :value="0">女</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="年龄" prop="age">
          <el-input-number v-model="form.age" :min="1" :max="150" style="width: 100%" />
        </el-form-item>
        <el-form-item label="职业" prop="profession">
          <el-input v-model="form.profession" placeholder="请输入职业" />
        </el-form-item>
        <el-form-item label="住址" prop="address">
          <el-input v-model="form.address" placeholder="请输入住址" />
        </el-form-item>
        <div class="btn-group">
          <el-button type="primary" :loading="loading" @click="handleSave">保存</el-button>
          <el-button @click="editing = false">取消</el-button>
        </div>
      </el-form>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { useUserStore } from '../stores/user'

const router = useRouter()
const userStore = useUserStore()
const formRef = ref()
const editing = ref(false)
const loading = ref(false)
const userInfo = ref(null)

const form = reactive({
  gender: 1,
  age: null,
  profession: '',
  address: ''
})

const rules = {
  age: [{ type: 'number', min: 1, max: 150, message: '年龄范围1-150', trigger: 'blur' }]
}

onMounted(async () => {
  try {
    userInfo.value = await userStore.fetchUserInfo()
  } catch (e) {
    router.push('/login')
  }
})

const formatTime = (time) => {
  if (!time) return '-'
  return new Date(time).toLocaleString('zh-CN')
}

const startEdit = () => {
  form.gender = userInfo.value?.gender ?? 1
  form.age = userInfo.value?.age
  form.profession = userInfo.value?.profession || ''
  form.address = userInfo.value?.address || ''
  editing.value = true
}

const handleSave = async () => {
  await formRef.value.validate()
  loading.value = true
  try {
    userInfo.value = await userStore.updateUserInfo(form)
    ElMessage.success('保存成功')
    editing.value = false
  } finally {
    loading.value = false
  }
}

const handleLogout = () => {
  userStore.logout()
  ElMessage.success('已退出登录')
  router.push('/login')
}
</script>
