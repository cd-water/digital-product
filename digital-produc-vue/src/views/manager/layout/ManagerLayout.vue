<template>
  <el-container class="container">
    <el-header class="header">
      <div class="two-side">
        <div class="logo-title">
          <img src="@/assets/image/logo.svg" alt="logo" />
          <span>数码产品在线购物系统</span>
        </div>
        <el-breadcrumb separator="/">
          <el-breadcrumb-item
            :to="{ path: authStore.authMsg.role === 0 ? '/manager/admin-only/home' : '/manager/digitalshop-only/home' }"
          >
            <span class="bread-text">首页</span>
          </el-breadcrumb-item>
          <el-breadcrumb-item>
            <span class="bread-text">{{ $route.meta.title }}</span>
          </el-breadcrumb-item>
        </el-breadcrumb>
      </div>
      <el-dropdown :hide-on-click="false" @command="handleCommand">
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
            <el-dropdown-item v-if="authStore.authMsg.role === 1" command="shopApply">
              <el-icon><Stamp /></el-icon>认证申请
            </el-dropdown-item>
            <el-dropdown-item command="logout" :disabled="logoutLoading">
              <el-icon><SwitchButton /></el-icon>退出登录
            </el-dropdown-item>
          </el-dropdown-menu>
        </template>
      </el-dropdown>
    </el-header>
    <el-container>
      <el-aside class="aside-part">
        <el-menu router :default-openeds="['mall', 'system']" :default-active="$route.path">
          <el-menu-item
            :index="authStore.authMsg.role === 0 ? '/manager/admin-only/home' : '/manager/digitalshop-only/home'"
          >
            <el-icon><HomeFilled /></el-icon>工作台
          </el-menu-item>
          <el-sub-menu index="mall">
            <template #title>
              <el-icon><Shop /></el-icon>商城管理
            </template>
            <el-menu-item v-if="authStore.authMsg.role === 0" index="/manager/admin-only/productType">
              <el-icon><Collection /></el-icon>数码产品类型
            </el-menu-item>
            <el-menu-item v-if="authStore.authMsg.role === 0" index="/manager/admin-only/accessoryType">
              <el-icon><ShoppingBag /></el-icon>数码配件类型
            </el-menu-item>
            <el-menu-item index="/manager/share-both/product">
              <el-icon><Tickets /></el-icon>数码产品管理
            </el-menu-item>
            <el-menu-item index="/manager/share-both/accessory">
              <el-icon><Box /></el-icon>数码配件管理
            </el-menu-item>
            <el-menu-item index="/manager/share-both/productOrders">
              <el-icon><Document /></el-icon>
              <el-badge v-if="authStore.authMsg.role === 1" :value="pendingAcceptProductOrderNum"
                >数码产品订单</el-badge
              >
              <span v-else>数码产品订单</span>
            </el-menu-item>
            <el-menu-item index="/manager/share-both/accessoryOrders">
              <el-icon><Document /></el-icon>
              <el-badge v-if="authStore.authMsg.role === 1" :value="pendingAcceptAccessoryOrderNum"
                >数码配件订单</el-badge
              >
              <span v-else>数码配件订单</span>
            </el-menu-item>
            <el-menu-item v-if="authStore.authMsg.role === 0" index="/manager/admin-only/slideshow">
              <el-icon><Picture /></el-icon>轮播图
            </el-menu-item>
            <el-menu-item v-if="authStore.authMsg.role === 0" index="/manager/admin-only/notice">
              <el-icon><Bell /></el-icon>系统公告
            </el-menu-item>
          </el-sub-menu>
          <el-sub-menu v-if="authStore.authMsg.role === 0" index="system">
            <template #title>
              <el-icon><Setting /></el-icon>系统管理
            </template>
            <el-menu-item index="/manager/admin-only/user">
              <el-icon><User /></el-icon>用户管理
            </el-menu-item>
            <el-menu-item index="/manager/admin-only/digitalShop">
              <el-icon><OfficeBuilding /></el-icon>店铺管理
            </el-menu-item>
            <el-menu-item index="/manager/admin-only/admin">
              <el-icon><UserFilled /></el-icon>管理员管理
            </el-menu-item>
          </el-sub-menu>
        </el-menu>
      </el-aside>
      <el-main class="main">
        <RouterView />
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { useRouter } from 'vue-router'
import { useAuthStore } from '@/stores'
import { logoutApi } from '@/api/auth'
import { ref, onMounted, onBeforeUnmount } from 'vue'
import { ElNotification } from 'element-plus'
import { getProductOrderNumApi } from '@/api/manager/productOrders'
import { getAccessoryOrderNumApi } from '@/api/manager/accessoryOrders'

const router = useRouter()
const authStore = useAuthStore()
const logoutLoading = ref(false)

const pendingAcceptProductOrderNum = ref(0)
const pendingAcceptAccessoryOrderNum = ref(0)

const checkPAProductOrderNum = async () => {
  const role = authStore.authMsg.role
  if (role != 1) return
  const shopId = authStore.authMsg.id
  const res = await getProductOrderNumApi(shopId)
  if (res.code === 200) {
    pendingAcceptProductOrderNum.value = res.data
  }
}

const checkPAAccessoryOrderNum = async () => {
  const role = authStore.authMsg.role
  if (role != 1) return
  const shopId = authStore.authMsg.id
  const res = await getAccessoryOrderNumApi(shopId)
  if (res.code === 200) {
    pendingAcceptAccessoryOrderNum.value = res.data
  }
}

//websocket相关
let ws = null

const connectWebSocket = () => {
  //店铺建立连接
  const role = authStore.authMsg.role
  if (role != 1) return
  const shopId = authStore.authMsg.id
  const wsUrl = `ws://localhost:9090/ws/${shopId}`

  ws = new WebSocket(wsUrl)

  ws.onopen = () => {
    console.log('WebSocket连接已建立')
  }

  ws.onmessage = (event) => {
    //监听用户下单提醒
    const data = JSON.parse(event.data)
    const type = data.type
    if (type === 'product-order') {
      ElNotification({
        title: '数码产品订单待处理',
        message: `订单号：${data.orderNo}<br>下单时间：${data.orderTime}`,
        duration: 60000,
        dangerouslyUseHTMLString: true,
      })
      checkPAProductOrderNum()
    } else if (type === 'accessory-order') {
      ElNotification({
        title: '数码配件订单待处理',
        message: `订单号：${data.orderNo}<br>下单时间：${data.orderTime}`,
        duration: 60000,
        dangerouslyUseHTMLString: true,
      })
      checkPAAccessoryOrderNum()
    }
  }

  ws.onclose = () => {
    console.log('WebSocket连接已断开')
  }
}

onMounted(() => {
  checkPAProductOrderNum()
  checkPAAccessoryOrderNum()
  connectWebSocket()
})

onBeforeUnmount(() => {
  if (ws) {
    ws.close()
    ws = null
  }
})

const handleCommand = async (command) => {
  if (command === 'profile') {
    router.push(authStore.authMsg.role === 0 ? '/manager/admin-only/profile' : '/manager/digitalshop-only/profile')
  } else if (command === 'change') {
    router.push(authStore.authMsg.role === 0 ? '/manager/admin-only/change' : '/manager/digitalshop-only/change')
  } else if (command === 'shopApply') {
    router.push('/manager/digitalshop-only/shopApply')
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
  background: linear-gradient(100deg, #4f8cff 0%, #1fd1f9 50%, #005bea 100%);
  box-shadow: 0 4px 18px 0 rgba(79, 140, 255, 0.18);
  border-bottom: 3px solid #4f8cff;
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
.logo-title > :nth-child(2) {
  font-size: var(--font-size-xl);
  color: var(--text-black);
}
.aside-part {
  width: 200px;
}
:deep(.el-menu),
:deep(.el-menu .el-menu-item) {
  border-radius: var(--border-radius-md);
  color: var(--text-black);
  font-size: var(--font-size-md);
  transition:
    background 0.2s,
    color 0.2s;
}
:deep(.el-menu .el-menu-item) {
  height: 49px;
}

:deep(.el-menu .el-menu-item.is-active),
:deep(.el-menu .el-menu-item:hover) {
  background: linear-gradient(90deg, #ffb347 0%, #ffcc80 100%);
  border-radius: var(--border-radius-lg);
  box-shadow: 0 2px 8px 0 rgba(255, 179, 71, 0.12);
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
  background: linear-gradient(90deg, #ffb347 0%, #ffcc80 100%);
  color: var(--text-white);
  font-weight: bold;
}
.bread-text {
  font-size: var(--font-size-lg);
  color: var(--text-black);
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
  border: 2px solid #ffb300;
}
.two-side {
  display: flex;
  align-items: center;
  gap: var(--spacing-xl);
}
.main {
  background-color: #e3f2fd;
  border-radius: var(--border-radius-md);
  display: flex;
  flex-direction: column;
  gap: var(--spacing-md);
  min-width: 1200px;
  min-height: 800px;
  overflow-x: auto;
}
</style>
