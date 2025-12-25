<template>
  <div class="bg">
    <div class="title">数码产品在线购物系统</div>
    <el-card class="register-card">
      <template #header>
        <div class="card-title">欢迎注册</div>
      </template>
      <el-form ref="registerFormRef" :model="registerForm" :rules="registerRules">
        <el-form-item prop="username">
          <el-input v-model="registerForm.username" placeholder="请输入账号" :prefix-icon="User" class="card-input" />
        </el-form-item>
        <el-form-item prop="password">
          <el-input
            v-model="registerForm.password"
            placeholder="请输入密码"
            show-password
            :prefix-icon="Lock"
            class="card-input"
          />
        </el-form-item>
        <el-form-item prop="confirmPassword">
          <el-input
            v-model="registerForm.confirmPassword"
            placeholder="请确认密码"
            show-password
            :prefix-icon="Lock"
            class="card-input"
          />
        </el-form-item>
        <el-form-item prop="role">
          <el-select v-model="registerForm.role" placeholder="请选择身份" class="card-select">
            <el-option label="普通用户" :value="2" />
            <el-option label="店铺商家" :value="1" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button :loading="submitting" :disabled="submitting" class="register-btn" @click="handleRegister">
            注 册
          </el-button>
        </el-form-item>
        <el-form-item>
          <div class="two-side">
            <RouterLink to="/customer/visitor-toall/home" class="link card-link">游客访问</RouterLink>
            <span>
              已有账号？前往
              <RouterLink to="/auth/login" class="link card-link">登 录</RouterLink>
            </span>
          </div>
        </el-form-item>
      </el-form>
    </el-card>
    <footer class="copyright">
      <p>Copyright©2025 CD_water. All rights reserved.</p>
    </footer>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { User, Lock } from '@element-plus/icons-vue'
import { registerApi } from '@/api/auth'
import { ElMessage } from 'element-plus'
import { useRouter } from 'vue-router'
import avatar1 from '@/assets/image/avatar1.png'
import avatar2 from '@/assets/image/avatar2.png'
import avatar3 from '@/assets/image/avatar3.png'
import avatar4 from '@/assets/image/avatar4.png'

const router = useRouter()
const submitting = ref(false)

//生成随机头像
const getRandomAvatar = () => {
  const defaultAvatars = [avatar1, avatar2, avatar3, avatar4]
  const idx = Math.floor(Math.random() * defaultAvatars.length)
  return defaultAvatars[idx]
}

const registerForm = ref({
  username: '',
  password: '',
  confirmPassword: '',
  role: 2,
  avatar: getRandomAvatar(),
})
const registerFormRef = ref()

const specialChars = '!@#$%^&*()_+-=[]{},.<>/?;:\'"|\\'

const countCharTypes = (value) => {
  let types = 0
  if (/[A-Z]/.test(value)) types++
  if (/[a-z]/.test(value)) types++
  if (/\d/.test(value)) types++
  if (new RegExp(`[${specialChars.replace(/[-[\]{}()*+?.,\\^$|#\s]/g, '\\$&')}]`).test(value)) types++
  return types
}

const registerRules = {
  username: [
    {
      required: true,
      message: '请输入账号',
      trigger: 'blur',
    },
    {
      pattern: /^[a-zA-Z0-9_]{5,20}$/,
      message: '账号需由5-20位字母、数字或下划线组成',
      trigger: 'blur',
    },
  ],
  password: [
    {
      validator: (rule, value, callback) => {
        if (!value) {
          callback(new Error('请输入密码'))
          return
        }
        if (value.length < 8 || value.length > 32) {
          callback(new Error('密码长度需为8-32位'))
          return
        }
        // 允许的特殊字符
        const allowedPattern = new RegExp(`^[A-Za-z0-9${specialChars.replace(/[-[\]{}()*+?.,\\^$|#\s]/g, '\\$&')}]+$`)
        if (!allowedPattern.test(value)) {
          callback(new Error('密码只能包含大写字母、小写字母、数字和特殊字符'))
          return
        }
        // 至少包含三种类型
        if (countCharTypes(value) < 3) {
          callback(new Error('密码必须包含大写字母、小写字母、数字和特殊字符中的至少三种'))
          return
        }
        callback()
      },
      trigger: 'blur',
    },
  ],
  confirmPassword: [
    {
      validator: (rule, value, callback) => {
        if (!value) {
          callback(new Error('请确认密码'))
          return
        }
        if (value !== registerForm.value.password) {
          callback(new Error('两次输入的密码不一致'))
          return
        }
        callback()
      },
      trigger: 'blur',
    },
  ],
}

const handleRegister = async () => {
  if (submitting.value) return
  submitting.value = true
  try {
    await registerFormRef.value?.validate?.()
    const res = await registerApi(registerForm.value)
    if (res.code === 200) {
      ElMessage.success('注册成功')
      router.push('/auth/login')
    }
  } catch {
    ElMessage.warning('请检查输入')
  } finally {
    submitting.value = false
  }
}
</script>

<style scoped>
.bg {
  position: fixed;
  top: 0;
  left: 0;
  width: 100vw;
  height: 100vh;
  background: url('@/assets/image/bg.jpg') center/cover no-repeat;
}
.title {
  position: absolute;
  top: 40px;
  left: 40px;
  font-size: 6rem;
  color: #ff9e3d;
  font-weight: bold;
  user-select: none;
  cursor: pointer;
}
.title:hover {
  color: #ffb6d5;
  text-shadow:
    0 8px 24px #fff6,
    0 2px 0 #fff,
    0 0 0 #fff,
    0 0 18px #ffe0f0,
    2px 2px 0 #ffb6d5;
}
.register-card {
  position: absolute;
  top: 50%;
  right: 4vw;
  transform: translateY(-50%);
  max-width: 440px;
  width: 100%;
  border-radius: 40px 40px 60px 60px/38px 38px 80px 80px;
  box-shadow:
    0 16px 48px 0 #ffb6d577,
    0 2px 12px 0 #fff8,
    0 0 0 10px #fffbe7cc;
  background: linear-gradient(135deg, #fffbe7 60%, #ffe5ec 100%);
  border: 3px solid #ffb6d5;
  padding: 0 38px 38px 38px;
  animation: cardIn 0.8s cubic-bezier(0.4, 2, 0.6, 1.2);
  overflow: visible;
  transition:
    box-shadow 0.3s,
    background 0.3s,
    transform 0.18s,
    border-color 0.3s;
}
.register-card:hover {
  box-shadow:
    0 24px 64px 0 #ffb6d5bb,
    0 4px 24px 0 #fff8,
    0 0 0 16px #ffe0f0cc;
  background: linear-gradient(135deg, #fffbe7 40%, #ffe0f0 100%);
  border-color: #ff8bbd;
  transform: translateY(-54%) scale(1.025) rotate(-1deg);
}
@keyframes cardIn {
  0% {
    opacity: 0;
    transform: translateY(80px) scale(0.95) rotate(2deg);
    filter: blur(8px);
  }
  100% {
    opacity: 1;
    transform: translateY(-50%) scale(1) rotate(0);
    filter: blur(0);
  }
}
.card-title {
  font-size: 3rem;
  color: #ff8e7d;
  text-align: center;
  user-select: none;
  text-shadow:
    0 2px 8px #fff6,
    0 1px 0 #fff,
    0 0 0 #fff,
    0 0 8px #ffe0f0,
    1px 1px 0 #ffb6d5;
  filter: drop-shadow(0 2px 0 #fff6) drop-shadow(0 0 4px #ffb6d5aa);
}
.card-input :deep(.el-input__wrapper) {
  border-radius: var(--border-radius-lg);
  min-height: 45px;
  border: 2px solid #ffd600;
  font-size: var(--font-size-lg);
}
.card-input :deep(.el-input__wrapper):focus-within {
  border: 2px solid #409eff;
}
.card-select :deep(.el-select__wrapper) {
  border-radius: var(--border-radius-lg);
  min-height: 45px;
  border: 2px solid #ffd600;
  font-size: var(--font-size-lg);
}
.card-select :deep(.el-select__wrapper):focus-within {
  border: 2px solid #409eff;
}
.two-side {
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: 100%;
}
.card-link {
  color: #ff9e3d;
  font-size: var(--font-size-md);
}
.card-link:hover {
  text-decoration: underline wavy #ff9e3d;
}
.register-btn {
  width: 100%;
  height: 45px;
  border-radius: var(--border-radius-md);
  background: #ff8bbd;
  color: var(--text-white);
  font-size: var(--font-size-lg);
  font-weight: bold;
  border: none;
}
.register-btn:hover,
.register-btn:focus {
  background: #ff9e3d;
}
.copyright {
  position: fixed;
  bottom: 0;
  left: 0;
  width: 100vw;
  text-align: center;
  z-index: 100;
  font-size: var(--font-size-xl);
  color: var(--text-white);
  background: rgba(255, 102, 0, 0.75);
  border-radius: var(--border-radius-md);
}
@media (max-width: 768px) {
  .title {
    font-size: 2rem;
  }
  .copyright {
    font-size: var(--font-size-md);
  }
}
</style>
