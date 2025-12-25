import { createRouter, createWebHistory, RouterView } from 'vue-router'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    { path: '/', redirect: '/customer/visitor-toall/home' },
    {
      path: '/auth',
      component: RouterView,
      children: [
        { path: 'login', component: () => import('@/views/auth/Login.vue') },
        { path: 'register', component: () => import('@/views/auth/Register.vue') },
        { path: 'forget', component: () => import('@/views/auth/Forget.vue') },
      ],
    },
    {
      path: '/common',
      component: RouterView,
      children: [
        { path: '403', component: () => import('@/views/common/403.vue') },
        { path: '404', component: () => import('@/views/common/404.vue') },
      ],
    },
    {
      path: '/customer',
      component: () => import('@/views/customer/layout/CustomerLayout.vue'),
      children: [
        {
          path: 'user-only',
          component: RouterView,
          meta: { roles: [2] },
          children: [
            { path: 'myAddress', component: () => import('@/views/customer/user-only/MyAddress.vue') },
            { path: 'myCart', component: () => import('@/views/customer/user-only/MyCart.vue') },
            { path: 'myCollect', component: () => import('@/views/customer/user-only/MyCollect.vue') },
            { path: 'myOrders', component: () => import('@/views/customer/user-only/MyOrders.vue') },
            { path: 'profile', component: () => import('@/views/customer/user-only/Profile.vue') },
            { path: 'change', component: () => import('@/views/auth/Change.vue') },
          ],
        },
        {
          path: 'visitor-toall',
          component: RouterView,
          children: [
            { path: 'home', component: () => import('@/views/customer/visitor-toall/Home.vue') },
            { path: 'digitalShop', component: () => import('@/views/customer/visitor-toall/DigitalShop.vue') },
            {
              path: 'digitalShopDetail',
              component: () => import('@/views/customer/visitor-toall/DigitalShopDetail.vue'),
            },
            { path: 'product', component: () => import('@/views/customer/visitor-toall/Product.vue') },
            { path: 'productDetail', component: () => import('@/views/customer/visitor-toall/ProductDetail.vue') },
            { path: 'accessory', component: () => import('@/views/customer/visitor-toall/Accessory.vue') },
            { path: 'expert', component: () => import('@/views/customer/visitor-toall/Expert.vue') },
          ],
        },
      ],
    },
    {
      path: '/manager',
      component: () => import('@/views/manager/layout/ManagerLayout.vue'),
      meta: { roles: [0, 1] },
      children: [
        {
          path: 'admin-only',
          component: RouterView,
          meta: { roles: [0] },
          children: [
            { path: 'home', component: () => import('@/views/manager/admin-only/Home.vue'), meta: { title: '工作台' } },
            {
              path: 'productType',
              component: () => import('@/views/manager/admin-only/ProductType.vue'),
              meta: { title: '数码产品分类' },
            },
            {
              path: 'accessoryType',
              component: () => import('@/views/manager/admin-only/AccessoryType.vue'),
              meta: { title: '数码配件分类' },
            },
            {
              path: 'slideshow',
              component: () => import('@/views/manager/admin-only/Slideshow.vue'),
              meta: { title: '轮播图' },
            },
            {
              path: 'notice',
              component: () => import('@/views/manager/admin-only/Notice.vue'),
              meta: { title: '系统公告' },
            },
            {
              path: 'user',
              component: () => import('@/views/manager/admin-only/User.vue'),
              meta: { title: '用户管理' },
            },
            {
              path: 'digitalShop',
              component: () => import('@/views/manager/admin-only/DigitalShop.vue'),
              meta: { title: '店铺管理' },
            },
            {
              path: 'admin',
              component: () => import('@/views/manager/admin-only/Admin.vue'),
              meta: { title: '管理员管理' },
            },
            {
              path: 'profile',
              component: () => import('@/views/manager/admin-only/Profile.vue'),
            },
            {
              path: 'change',
              component: () => import('@/views/auth/Change.vue'),
            },
          ],
        },
        {
          path: 'digitalshop-only',
          component: RouterView,
          meta: { roles: [1] },
          children: [
            {
              path: 'home',
              component: () => import('@/views/manager/digitalshop-only/Home.vue'),
              meta: { title: '工作台' },
            },
            {
              path: 'profile',
              component: () => import('@/views/manager/digitalshop-only/Profile.vue'),
            },
            {
              path: 'change',
              component: () => import('@/views/auth/Change.vue'),
            },
            {
              path: 'shopApply',
              component: () => import('@/views/manager/digitalshop-only/ShopApply.vue'),
            },
          ],
        },
        {
          path: 'share-both',
          component: RouterView,
          meta: { roles: [0, 1] },
          children: [
            {
              path: 'accessory',
              component: () => import('@/views/manager/share-both/Accessory.vue'),
              meta: { title: '数码配件管理' },
            },
            {
              path: 'accessoryOrders',
              component: () => import('@/views/manager/share-both/AccessoryOrders.vue'),
              meta: { title: '数码配件订单' },
            },
            {
              path: 'product',
              component: () => import('@/views/manager/share-both/Product.vue'),
              meta: { title: '数码产品管理' },
            },
            {
              path: 'productOrders',
              component: () => import('@/views/manager/share-both/ProductOrders.vue'),
              meta: { title: '数码产品订单' },
            },
          ],
        },
      ],
    },
    {
      path: '/:pathMatch(.*)*',
      name: 'notFound',
      component: () => import('@/views/common/404.vue'),
      meta: { roles: [0, 1, 2] },
    },
  ],
  //页面跳转后自动回到顶部
  scrollBehavior(to, from, savedPosition) {
    // 如果有保存的滚动位置（比如浏览器前进后退），则返回该位置
    if (savedPosition) {
      return savedPosition
    } else {
      // 否则滚动到顶部
      return { left: 0, top: 0 }
    }
  },
})

import { useAuthStore } from '@/stores/auth'
import { ElMessage } from 'element-plus'

const whiteList = [
  '/',
  '/auth/login',
  '/auth/register',
  '/auth/forget',
  '/common/403',
  '/common/404',
  '/customer/visitor-toall/home',
  '/customer/visitor-toall/digitalShop',
  '/customer/visitor-toall/digitalShopDetail',
  '/customer/visitor-toall/product',
  '/customer/visitor-toall/productDetail',
  '/customer/visitor-toall/accessory',
  '/customer/visitor-toall/expert',
]

router.beforeEach((to, from, next) => {
  const authStore = useAuthStore()
  const { token, role } = authStore.authMsg
  if (whiteList.includes(to.path)) return next()
  if (!token) {
    ElMessage.error('凭证无效或已过期')
    return next('/auth/login')
  }
  const routeRoles = to.meta && to.meta.roles
  if (routeRoles) {
    if (!routeRoles.includes(role)) return next('/common/403')
  }
  next()
})

export default router
