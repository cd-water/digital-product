<template>
  <div class="bg">
    <el-card shadow="hover" class="change-card">
      <template #header>
        <div class="header-part">修改密码</div>
      </template>
      <el-form ref="changeFormRef" :model="changeForm" :rules="changeRules">
        <el-form-item prop="oldPassword">
          <el-input v-model="changeForm.oldPassword" placeholder="请输入原密码" show-password :prefix-icon="Lock" />
        </el-form-item>
        <el-form-item prop="newPassword">
          <el-input v-model="changeForm.newPassword" placeholder="请输入新密码" show-password :prefix-icon="Lock" />
        </el-form-item>
        <el-form-item prop="confirmNewPassword">
          <el-input
            v-model="changeForm.confirmNewPassword"
            placeholder="请确认新密码"
            show-password
            :prefix-icon="Lock"
          />
        </el-form-item>
        <el-form-item>
          <div class="two-side">
            <RouterLink to="/auth/forget" class="link forget-link">忘记密码</RouterLink>
            <el-button
              :loading="changeLoading"
              :disabled="changeLoading"
              type="success"
              icon="Edit"
              @click="handleChange"
            >
              修改密码
            </el-button>
          </div>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { changeApi } from '@/api/auth'
import { ElMessage } from 'element-plus'
import { useRouter } from 'vue-router'
import { useAuthStore } from '@/stores'
import { Lock } from '@element-plus/icons-vue'

const authStore = useAuthStore()

const changeForm = ref({
  oldPassword: '',
  newPassword: '',
  confirmNewPassword: '',
  id: authStore.authMsg.id,
  role: authStore.authMsg.role,
})

const changeFormRef = ref()

const specialChars = '!@#$%^&*()_+-=[]{},.<>/?;:\'"|\\'

const countCharTypes = (value) => {
  let types = 0
  if (/[A-Z]/.test(value)) types++
  if (/[a-z]/.test(value)) types++
  if (/\d/.test(value)) types++
  if (new RegExp(`[${specialChars.replace(/[-[\]{}()*+?.,\\^$|#\s]/g, '\\$&')}]`).test(value)) types++
  return types
}

const changeRules = {
  oldPassword: [
    {
      validator: (rule, value, callback) => {
        if (!value) {
          callback(new Error('请输入原密码'))
          return
        }
        if (value.length < 8 || value.length > 32) {
          callback(new Error('密码长度需为8-32位'))
          return
        }
        const specialChars = '!@#$%^&*()_+-=[]{},.<>/?;:\'"|\\'
        const allowedPattern = new RegExp(`^[A-Za-z0-9${specialChars.replace(/[-[\]{}()*+?.,\\^$|#\s]/g, '\\$&')}]+$`)
        if (!allowedPattern.test(value)) {
          callback(new Error('密码只能包含大写字母、小写字母、数字和特殊字符'))
          return
        }
        callback()
      },
      trigger: 'blur',
    },
  ],
  newPassword: [
    {
      validator: (rule, value, callback) => {
        if (!value) {
          callback(new Error('请输入新密码'))
          return
        }
        if (value.length < 8 || value.length > 32) {
          callback(new Error('密码长度需为8-32位'))
          return
        }
        const allowedPattern = new RegExp(`^[A-Za-z0-9${specialChars.replace(/[-[\]{}()*+?.,\\^$|#\s]/g, '\\$&')}]+$`)
        if (!allowedPattern.test(value)) {
          callback(new Error('密码只能包含大写字母、小写字母、数字和特殊字符'))
          return
        }
        if (countCharTypes(value) < 3) {
          callback(new Error('密码必须包含大写字母、小写字母、数字和特殊字符中的至少三种'))
          return
        }
        callback()
      },
      trigger: 'blur',
    },
  ],
  confirmNewPassword: [
    {
      validator: (rule, value, callback) => {
        if (!value) {
          callback(new Error('请确认新密码'))
          return
        }
        if (value !== changeForm.value.newPassword) {
          callback(new Error('两次输入的新密码不一致'))
          return
        }
        callback()
      },
      trigger: 'blur',
    },
  ],
}

const changeLoading = ref(false)
const router = useRouter()

const handleChange = async () => {
  if (changeLoading.value) return
  changeLoading.value = true
  try {
    await changeFormRef.value?.validate?.()
    const res = await changeApi(changeForm.value)
    if (res.code === 200) {
      ElMessage.success('密码修改成功')
      router.push('/auth/login')
    }
  } catch {
    ElMessage.warning('请检查输入')
  } finally {
    changeLoading.value = false
  }
}
</script>

<style scoped>
.bg {
  background: url('@/assets/image/bg2.jpg') center/cover no-repeat;
  height: 760px;
  display: flex;
  align-items: center;
  border-radius: var(--border-radius-md);
}
.change-card {
  max-width: 480px;
  margin: auto 200px;
  width: 100%;
}
.header-part {
  font-size: var(--font-size-xl);
  color: var(--primary-color);
  display: flex;
  justify-content: center;
  align-items: center;
}

:deep(.el-input__wrapper) {
  border-radius: var(--border-radius-lg);
  border: 1.5px solid var(--primary-color);
}
.two-side {
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: 100%;
}
.forget-link {
  color: var(--warning-color);
  font-size: var(--font-size-md);
}
.forget-link:hover {
  text-decoration: underline wavy;
  font-weight: bold;
}
</style>
