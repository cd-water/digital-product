<template>
  <div class="bg">
    <div class="title">数码产品在线购物系统</div>
    <el-card class="login-card">
      <template #header>
        <div class="card-title">欢迎登录</div>
      </template>
      <el-tabs v-model="loginMethod">
        <el-tab-pane label="账号登录" name="account">
          <el-form ref="loginFormRef" :model="loginForm" :rules="loginRules">
            <el-form-item prop="username">
              <el-input v-model="loginForm.username" placeholder="请输入账号" :prefix-icon="User" class="card-input" />
            </el-form-item>
            <el-form-item prop="password">
              <el-input
                v-model="loginForm.password"
                placeholder="请输入密码"
                show-password
                :prefix-icon="Lock"
                class="card-input"
              />
            </el-form-item>
            <el-form-item prop="role">
              <el-select v-model="loginForm.role" placeholder="请选择身份" class="card-select">
                <el-option label="普通用户" :value="2" />
                <el-option label="店铺商家" :value="1" />
                <el-option label="管理员" :value="0" />
              </el-select>
            </el-form-item>
            <el-form-item>
              <div class="two-side">
                <el-checkbox v-model="remember" class="remember-checkbox">记住我</el-checkbox>
                <RouterLink to="/auth/forget" class="link card-link">忘记密码</RouterLink>
              </div>
            </el-form-item>
            <el-form-item>
              <el-button :loading="submitting" :disabled="submitting" class="login-btn" @click="handleLogin">
                登 录
              </el-button>
            </el-form-item>
            <el-form-item>
              <div class="two-side">
                <RouterLink to="/customer/visitor-toall/home" class="link card-link">游客访问</RouterLink>
                <span>
                  还没有账号？前往
                  <RouterLink to="/auth/register" class="link card-link">注 册</RouterLink>
                </span>
              </div>
            </el-form-item>
          </el-form>
        </el-tab-pane>
        <el-tab-pane label="手机号登录" name="phone">
          <el-form ref="phoneLoginFormRef" :model="phoneLoginForm" :rules="phoneLoginRules">
            <el-form-item prop="phone">
              <el-input
                v-model="phoneLoginForm.phone"
                placeholder="请输入手机号"
                :prefix-icon="Iphone"
                class="card-input"
              />
            </el-form-item>
            <el-form-item prop="code">
              <div class="card-code">
                <el-input
                  v-model="phoneLoginForm.code"
                  placeholder="请输入验证码"
                  :prefix-icon="Key"
                  class="card-input"
                />
                <el-button :disabled="codeBtnDisabled" class="code-btn" @click="getCode">
                  {{ codeBtnText }}
                </el-button>
              </div>
            </el-form-item>
            <el-form-item prop="role" class="card-select">
              <el-select v-model="phoneLoginForm.role" placeholder="请选择身份">
                <el-option label="普通用户" :value="2" />
                <el-option label="店铺商家" :value="1" />
                <el-option label="管理员" :value="0" />
              </el-select>
            </el-form-item>
            <el-form-item>
              <el-button :loading="submitting" :disabled="submitting" class="login-btn" @click="handlePhoneLogin">
                登 录
              </el-button>
            </el-form-item>
            <el-form-item>
              <div class="two-side">
                <RouterLink to="/customer/visitor-toall/home" class="link card-link">游客访问</RouterLink>
                <span>
                  还没有账号？前往
                  <RouterLink to="/auth/register" class="link card-link">注 册</RouterLink>
                </span>
              </div>
            </el-form-item>
          </el-form>
        </el-tab-pane>
      </el-tabs>
    </el-card>
    <footer class="copyright">
      <p>Copyright©2025 CD_water. All rights reserved.</p>
    </footer>
  </div>
</template>

<script setup>
import { Lock, User, Iphone, Key } from '@element-plus/icons-vue'
import { ref, computed, onMounted } from 'vue'
import { loginApi, phoneLoginApi, getCodeApi } from '@/api/auth'
import { ElMessage } from 'element-plus'
import { useAuthStore, useRememberStore } from '@/stores'
import { useRouter } from 'vue-router'

const remember = ref(false)
const submitting = ref(false)
const router = useRouter()
const authStore = useAuthStore()
const rememberStore = useRememberStore()

//记住密码
onMounted(() => {
  if (rememberStore.rememberMsg.username) {
    loginForm.value.username = rememberStore.rememberMsg.username
    loginForm.value.password = rememberStore.rememberMsg.password
    remember.value = true
  }
})

const loginMethod = ref('account')

//账号登录------------------------------------------------------------------------------------------------
const loginForm = ref({
  username: '',
  password: '',
  role: 2,
})
const loginFormRef = ref()
const loginRules = {
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
}

const handleLogin = async () => {
  if (submitting.value) return
  submitting.value = true
  try {
    await loginFormRef.value?.validate?.()
    const res = await loginApi(loginForm.value)
    if (res.code === 200) {
      ElMessage.success('登录成功')
      authStore.setAuthMsg(res.data)
      if (remember.value) {
        rememberStore.setRememberMsg({
          username: loginForm.value.username,
          password: loginForm.value.password,
        })
      } else {
        rememberStore.removeRememberMsg()
      }
      const role = res.data.role
      let path = '/customer/visitor-toall/home'
      if (role === 0) {
        path = '/manager/admin-only/home'
      } else if (role === 1) {
        path = '/manager/digitalshop-only/home'
      } else if (role === 2) {
        path = '/customer/visitor-toall/home'
      }
      router.push(path)
    }
  } catch {
    ElMessage.warning('请检查输入')
  } finally {
    submitting.value = false
  }
}

//手机号登录------------------------------------------------------------------------------------------------
const phoneLoginForm = ref({
  phone: '',
  code: '',
  role: 2,
})
const phoneLoginFormRef = ref()
const phoneLoginRules = {
  phone: [
    {
      required: true,
      message: '请输入手机号',
      trigger: ['blur', 'change'],
    },
    {
      pattern: /^1[3-9]\d{9}$/,
      message: '请输入有效的手机号',
      trigger: ['blur', 'change'],
    },
  ],
  code: [
    {
      required: true,
      message: '请输入验证码',
      trigger: 'blur',
    },
    {
      pattern: /^\d{6}$/,
      message: '验证码必须为6位数字',
      trigger: 'blur',
    },
  ],
}
const codeBtnDisabled = ref(false)
const countdown = ref(60)
let timer = null
const codeBtnText = computed(() => (codeBtnDisabled.value ? `${countdown.value}秒后重试` : '获取验证码'))

//获取验证码
const getCode = async () => {
  if (codeBtnDisabled.value) return
  if (!phoneLoginRules.phone[1].pattern.test(phoneLoginForm.value.phone)) {
    ElMessage.warning('请输入有效的手机号')
    return
  }
  codeBtnDisabled.value = true
  countdown.value = 60
  timer && clearInterval(timer)
  timer = setInterval(() => {
    if (countdown.value > 1) {
      countdown.value--
    } else {
      clearInterval(timer)
      codeBtnDisabled.value = false
    }
  }, 1000)
  const res = await getCodeApi(phoneLoginForm.value.phone)
  ElMessage.success(res.msg + ':' + res.data)
}

const handlePhoneLogin = async () => {
  if (submitting.value) return
  submitting.value = true
  try {
    await phoneLoginFormRef.value?.validate?.()
    const res = await phoneLoginApi(phoneLoginForm.value)
    if (res.code === 200) {
      ElMessage.success('登录成功')
      authStore.setAuthMsg(res.data)
      const role = res.data.role
      let path = '/customer/visitor-toall/home'
      if (role === 0) {
        path = '/manager/admin-only/home'
      } else if (role === 1) {
        path = '/manager/digitalshop-only/home'
      } else if (role === 2) {
        path = '/customer/visitor-toall/home'
      }
      router.push(path)
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
.login-card {
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
.login-card:hover {
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
.remember-checkbox :deep(.el-checkbox__label) {
  color: #409eff;
  font-size: var(--font-size-md);
}
.remember-checkbox :deep(.el-checkbox__input.is-checked .el-checkbox__inner) {
  background: #ff8e7d;
}
.remember-checkbox :deep(.el-checkbox__input .el-checkbox__inner) {
  border: 2px solid #ff8e7d;
}
.card-code {
  display: flex;
  align-items: center;
  gap: var(--spacing-md);
}
.code-btn {
  border-radius: var(--border-radius-md);
  background: #409eff;
  color: var(--text-white);
  font-size: var(--font-size-md);
  font-weight: bold;
  border: none;
  height: 45px;
}
.code-btn:disabled,
.code-btn[disabled] {
  background: #409eff;
  color: var(--text-white);
  opacity: 0.7;
  cursor: not-allowed;
}
.login-btn {
  width: 100%;
  height: 45px;
  border-radius: var(--border-radius-md);
  background: #ff8bbd;
  color: var(--text-white);
  font-size: var(--font-size-lg);
  font-weight: bold;
  border: none;
}
.login-btn:hover,
.login-btn:focus {
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
