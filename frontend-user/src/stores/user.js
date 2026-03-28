import { defineStore } from 'pinia'
import { ref } from 'vue'
import { userApi } from '../api'

export const useUserStore = defineStore('user', () => {
  const userInfo = ref(null)

  const fetchUserInfo = async () => {
    const res = await userApi.getInfo()
    userInfo.value = res.data
    return res.data
  }

  const updateUserInfo = async (data) => {
    const res = await userApi.updateInfo(data)
    userInfo.value = res.data
    return res.data
  }

  const logout = () => {
    userInfo.value = null
    localStorage.removeItem('token')
  }

  return { userInfo, fetchUserInfo, updateUserInfo, logout }
})
