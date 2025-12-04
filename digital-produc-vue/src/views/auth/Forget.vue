<template>
  <div class="bg">
    <div class="title">数码产品在线购物系统</div>
    <el-card class="forget-card">
      <template #header>
        <div class="card-title">找回密码</div>
      </template>
      <el-form ref="forgetFormRef" :model="forgetForm" :rules="forgetRules">
        <el-form-item prop="phone">
          <el-input v-model="forgetForm.phone" placeholder="请输入手机号" :prefix-icon="Iphone" class="card-input" />
        </el-form-item>
        <el-form-item prop="code">
          <div class="card-code">
            <el-input v-model="forgetForm.code" placeholder="请输入验证码" :prefix-icon="Key" class="card-input" />
            <el-button :disabled="codeBtnDisabled" class="code-btn" @click="getCode">
              {{ codeBtnText }}
            </el-button>
          </div>
        </el-form-item>
        <el-form-item prop="newPassword">
          <el-input
            v-model="forgetForm.newPassword"
            placeholder="请输入新密码"
            show-password
            :prefix-icon="Lock"
            class="card-input"
          />
        </el-form-item>
        <el-form-item prop="confirmNewPassword">
          <el-input
            v-model="forgetForm.confirmNewPassword"
            placeholder="请确认新密码"
            show-password
            :prefix-icon="Lock"
            class="card-input"
          />
        </el-form-item>
        <el-form-item prop="role">
          <el-select v-model="forgetForm.role" placeholder="请选择身份" class="card-select">
            <el-option label="普通用户" :value="2" />
            <el-option label="店铺商家" :value="1" />
            <el-option label="管理员" :value="0" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button :loading="submitting" :disabled="submitting" class="confirm-btn" @click="handleForget">
            确 认
          </el-button>
        </el-form-item>
        <el-form-item>
          <div class="two-side">
            <RouterLink to="/auth/login" class="link card-link">返回登录</RouterLink>
            <span>
              没有账号？前往
              <RouterLink to="/auth/register" class="link card-link">注 册</RouterLink>
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
import { Lock, Iphone, Key } from '@element-plus/icons-vue'
import { ref, computed } from 'vue'
import { forgetApi, getCodeApi } from '@/api/auth'
import { ElMessage } from 'element-plus'
import { useRouter } from 'vue-router'

const submitting = ref(false)
const router = useRouter()

const forgetForm = ref({
  phone: '',
  code: '',
  newPassword: '',
  confirmNewPassword: '',
  role: 2,
})

const forgetFormRef = ref()

const specialChars = '!@#$%^&*()_+-=[]{},.<>/?;:\'"|\\'

const countCharTypes = (value) => {
  let types = 0
  if (/[A-Z]/.test(value)) types++
  if (/[a-z]/.test(value)) types++
  if (/\d/.test(value)) types++
  if (new RegExp(`[${specialChars.replace(/[-[\]{}()*+?.,\\^$|#\s]/g, '\\$&')}]`).test(value)) types++
  return types
}

const forgetRules = {
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
        if (value !== forgetForm.value.newPassword) {
          callback(new Error('两次输入的新密码不一致'))
          return
        }
        callback()
      },
      trigger: 'blur',
    },
  ],
}

const codeBtnDisabled = ref(false)
const countdown = ref(60)
let timer = null
const codeBtnText = computed(() => {
  return codeBtnDisabled.value ? `${countdown.value}秒后重试` : '获取验证码'
})

//获取验证码
const getCode = async () => {
  if (codeBtnDisabled.value) return
  if (!forgetRules.phone[1].pattern.test(forgetForm.value.phone)) {
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
  const res = await getCodeApi(forgetForm.value.phone)
  ElMessage.success(res.msg + ':' + res.data)
}

const handleForget = async () => {
  if (submitting.value) return
  submitting.value = true
  try {
    await forgetFormRef.value?.validate?.()
    const res = await forgetApi(forgetForm.value)
    if (res.code === 200) {
      ElMessage.success('密码重置成功')
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
.forget-card {
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
.forget-card:hover {
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
.confirm-btn {
  width: 100%;
  height: 45px;
  border-radius: var(--border-radius-md);
  background: #ff8bbd;
  color: var(--text-white);
  font-size: var(--font-size-lg);
  font-weight: bold;
  border: none;
}
.confirm-btn:hover,
.confirm-btn:focus {
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
