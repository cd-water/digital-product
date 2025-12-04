<template>
  <el-container class="container">
    <el-header class="header">
      <div class="logo-title">
        <img src="@/assets/image/logo.svg" alt="logo" />
        <span>数码产品在线购物系统</span>
      </div>
      <el-menu router mode="horizontal" :ellipsis="false" :default-active="$route.path">
        <el-menu-item index="/customer/visitor-toall/home">
          <el-icon><House /></el-icon>首页
        </el-menu-item>
        <el-menu-item index="/customer/visitor-toall/digitalShop">
          <el-icon><Shop /></el-icon>数码店铺
        </el-menu-item>
        <el-menu-item index="/customer/visitor-toall/product">
          <el-icon><Tickets /></el-icon>数码产品
        </el-menu-item>
        <el-menu-item index="/customer/visitor-toall/accessory">
          <el-icon><ShoppingBag /></el-icon>数码配件
        </el-menu-item>
        <el-menu-item index="/customer/user-only/myCart">
          <el-icon><ShoppingCart /></el-icon>购物车
        </el-menu-item>
        <el-menu-item index="/customer/user-only/myOrders">
          <el-icon><Document /></el-icon>我的订单
        </el-menu-item>
      </el-menu>
      <div v-if="!authStore.authMsg.role" class="login-register">
        <RouterLink :to="'/auth/login'">登录</RouterLink>
        <RouterLink :to="'/auth/register'">注册</RouterLink>
      </div>
      <el-dropdown v-else :hide-on-click="false" @command="handleCommand">
        <div class="avatar-nickname">
          <img :src="authStore.authMsg.avatar" alt="avatar" />
          <span>
            {{ authStore.authMsg.nickname }}
            <el-icon><ArrowDown /></el-icon>
          </span>
        </div>
        <template #dropdown>
          <el-dropdown-menu>
            <el-dropdown-item command="profile">
              <el-icon><User /></el-icon>个人资料
            </el-dropdown-item>
            <el-dropdown-item command="change">
              <el-icon><Lock /></el-icon>修改密码
            </el-dropdown-item>
            <el-dropdown-item command="myCollect">
              <el-icon><Star /></el-icon>我的收藏
            </el-dropdown-item>
            <el-dropdown-item command="myAddress">
              <el-icon><Location /></el-icon>地址管理
            </el-dropdown-item>
            <el-dropdown-item command="logout" :disabled="logoutLoading">
              <el-icon><SwitchButton /></el-icon>退出登录
            </el-dropdown-item>
          </el-dropdown-menu>
        </template>
      </el-dropdown>
    </el-header>
    <el-main class="main">
      <RouterView />
    </el-main>
  </el-container>
</template>

<script setup>
import { useAuthStore } from '@/stores'
import { useRouter } from 'vue-router'
import { logoutApi } from '@/api/auth'
import { ref } from 'vue'

const authStore = useAuthStore()
const router = useRouter()
const logoutLoading = ref(false)

const handleCommand = async (command) => {
  if (command === 'profile') {
    router.push('/customer/user-only/profile')
  } else if (command === 'change') {
    router.push('/customer/user-only/change')
  } else if (command === 'myCollect') {
    router.push('/customer/user-only/myCollect')
  } else if (command === 'myAddress') {
    router.push('/customer/user-only/myAddress')
  } else if (command === 'logout') {
    if (logoutLoading.value) return
    logoutLoading.value = true
    try {
      const res = await logoutApi()
      if (res.code === 200) {
        authStore.removeAuthMsg()
        router.push('/auth/login')
      }
    } finally {
      logoutLoading.value = false
    }
  }
}
</script>

<style scoped>
.header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  background: linear-gradient(100deg, #ffb347 0%, #ffcc80 50%, #ff914d 100%);
  box-shadow: 0 4px 18px 0 rgba(255, 140, 0, 0.18);
  border-bottom: 3px solid #ffd180;
  border-radius: var(--border-radius-lg);
  height: 70px;
  min-width: 1400px;
}
.logo-title {
  display: flex;
  align-items: center;
  gap: var(--spacing-md);
}
.logo-title > :first-child {
  width: 50px;
  height: 50px;
  border-radius: var(--border-radius-lg);
  margin-left: var(--spacing-md);
}
.logo-title > :last-child {
  font-size: var(--font-size-xl);
  color: var(--text-black);
}

:deep(.el-menu),
:deep(.el-menu .el-menu-item) {
  background: transparent !important;
  border: none !important;
  color: var(--text-black);
  font-size: var(--font-size-md);
  transition:
    background 0.2s,
    color 0.2s;
}

:deep(.el-menu .el-menu-item.is-active),
:deep(.el-menu .el-menu-item:hover) {
  background: linear-gradient(90deg, #4f8cff 0%, #1fd1f9 100%) !important;
  color: var(--text-white) !important;
  border-radius: var(--border-radius-lg);
  box-shadow: 0 2px 8px 0 rgba(79, 140, 255, 0.12);
  font-weight: bold;
  transition:
    background 0.2s,
    color 0.2s;
}

:deep(.el-dropdown-menu__item) {
  transition:
    background 0.2s,
    color 0.2s;
  border-radius: var(--border-radius-md);
  font-size: var(--font-size-md);
  color: var(--text-black);
}

:deep(.el-dropdown-menu__item:hover),
:deep(.el-dropdown-menu__item:focus) {
  background: linear-gradient(90deg, #4f8cff 0%, #1fd1f9 100%);
  color: var(--text-white);
  font-weight: bold;
}

.login-register {
  display: flex;
  align-items: center;
  gap: var(--spacing-md);
}
.login-register > :first-child {
  font-size: var(--font-size-lg);
  color: var(--text-black);
  text-decoration: none;
}
.login-register > :last-child {
  font-size: var(--font-size-lg);
  color: var(--text-white);
  text-decoration: none;
  margin-right: var(--spacing-md);
}

.avatar-nickname {
  display: flex;
  align-items: center;
  gap: var(--spacing-md);
}
.avatar-nickname > :first-child {
  width: 50px;
  height: 50px;
  border-radius: var(--border-radius-xl);
}
.avatar-nickname > :last-child {
  font-size: var(--font-size-lg);
  margin-right: var(--spacing-md);
  color: var(--text-black);
}
.avatar-nickname:hover > :first-child,
.avatar-nickname > :first-child:active {
  box-shadow: 0 0 0 4px #4f8cff33;
  transform: scale(1.08);
  border: 2px solid #4f8cff;
}

.main {
  background-color: #fff8e1;
  border-radius: var(--border-radius-md);
  display: flex;
  flex-direction: column;
  gap: var(--spacing-md);
  min-width: 1400px;
  min-height: 800px;
  overflow-x: auto;
}
</style>
