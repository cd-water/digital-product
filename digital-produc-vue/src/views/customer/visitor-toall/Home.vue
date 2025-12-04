<template>
  <div class="top-part">
    <div class="top-left">
      <div class="big-text">
        <el-icon><Menu /></el-icon>
        <span>热门数码产品类型</span>
      </div>
      <div class="hot-tag">
        <RouterLink
          v-for="item in hotProductTypeList"
          :key="item.id"
          :to="`/customer/visitor-toall/product?typeId=${item.id}`"
        >
          <el-tag effect="dark" round>
            {{ item.name }}
          </el-tag>
        </RouterLink>
      </div>
      <div class="big-text">
        <el-icon><Menu /></el-icon>
        <span>热门数码配件类型</span>
      </div>
      <div class="hot-tag">
        <RouterLink
          v-for="item in hotAccessoryTypeList"
          :key="item.id"
          :to="`/customer/visitor-toall/accessory?typeId=${item.id}`"
        >
          <el-tag effect="dark" round type="success">
            {{ item.name }}
          </el-tag>
        </RouterLink>
      </div>
      <div class="big-text">
        <el-icon><Search /></el-icon>
        <span>大家都在搜</span>
      </div>
      <div class="hot-search">
        <div v-for="item in recommendProductList" :key="item.id">
          <RouterLink :to="`/customer/visitor-toall/productDetail?id=${item.id}`" class="link hot-search-link">
            {{ item.name }}
          </RouterLink>
        </div>
      </div>
    </div>
    <el-carousel arrow="hover" :interval="4000" height="480px" class="slideshow">
      <el-carousel-item v-for="item in slideshowList" :key="item.id">
        <RouterLink :to="`/customer/visitor-toall/productDetail?id=${item.productId}`">
          <img :src="item.img" alt="slideshow" />
        </RouterLink>
      </el-carousel-item>
    </el-carousel>
    <div class="top-right">
      <div class="big-text">系统公告</div>
      <div class="notice-msg">
        <span v-if="latestNotice && latestNotice.title">{{ latestNotice.title }}</span>
        <span v-else>暂无公告</span>
        <span v-if="latestNotice && (latestNotice.title || latestNotice.content)" class="text-more" @click="openNotice">
          更多
        </span>
      </div>
      <div class="service-item">
        <div class="big-text">
          <el-icon><User /></el-icon>
          <span>客服信息</span>
        </div>
        <div>在线客服：7×24小时</div>
        <div>客服电话：400-XXX-XXXX</div>
      </div>
      <div class="distribution-item">
        <div class="big-text">
          <el-icon><Van /></el-icon>
          <span>物流信息</span>
        </div>
        <div>全国包邮（部分偏远地区除外）</div>
        <div>支持多种配送方式</div>
      </div>
      <div class="aftersale-item">
        <div class="big-text">
          <el-icon><Medal /></el-icon>
          <span>售后保障</span>
        </div>
        <div>7天无理由退换货</div>
        <div>正品保障</div>
      </div>
    </div>
  </div>

  <el-card shadow="hover">
    <template #header>
      <el-row :gutter="24">
        <el-col :span="17">
          <div class="two-side">
            <span>推荐数码产品</span>
            <div>
              <RouterLink to="/customer/visitor-toall/product" class="link text-more">更多数码产品</RouterLink>
              <el-icon><ArrowRight /></el-icon>
            </div>
          </div>
        </el-col>
        <el-col :span="7">
          <div class="two-side">
            <span>热门数码店铺</span>
            <div>
              <RouterLink to="/customer/visitor-toall/digitalShop" class="link text-more">更多数码店铺</RouterLink>
              <el-icon><ArrowRight /></el-icon>
            </div>
          </div>
        </el-col>
      </el-row>
    </template>
    <el-row :gutter="24">
      <el-col :span="17">
        <el-row :gutter="24">
          <el-col v-for="item in recommendProductList" :key="item.id" :span="6" class="show-item">
            <RouterLink :to="`/customer/visitor-toall/productDetail?id=${item.id}`" class="link">
              <div>
                <img :src="item.img" alt="product" class="img-product" />
              </div>
              <div>{{ item.name }}</div>
              <div class="tag">
                <el-tag v-if="item.used === 0" type="danger" effect="dark" round>全新</el-tag>
                <el-tag v-else-if="item.used === 1" type="primary" effect="dark" round>二手</el-tag>
                <el-tag v-if="item.saleStatus === 0" type="info" effect="dark">已售罄</el-tag>
                <el-tag v-else-if="item.saleStatus === 1" type="danger" effect="dark">售卖中</el-tag>
                <el-tag v-else-if="item.saleStatus === 2" type="warning" effect="dark">未上架</el-tag>
                <el-tag v-if="item.store > 0" type="success" effect="dark">有货</el-tag>
                <el-tag v-else type="info" effect="dark">缺货</el-tag>
              </div>
              <div class="price">￥{{ item.price }}</div>
            </RouterLink>
          </el-col>
        </el-row>
      </el-col>
      <el-col :span="7">
        <div v-for="item in hotDigitalShopList" :key="item.id" class="show-item">
          <RouterLink :to="`/customer/visitor-toall/digitalShopDetail?id=${item.id}`" class="link">
            <div class="two-side">
              <img :src="item.avatar" alt="shop" class="img-shop" />
              <div>
                <div class="shop-name">{{ item.nickname }}</div>
                <div>
                  店铺地址：{{
                    [item.provinceCode, item.cityCode, item.districtCode]
                      .map((code) => codeToText[code])
                      .filter(Boolean)
                      .join(' ')
                  }}&nbsp;{{ item.detailAddress }}
                </div>
                <div>联系电话：{{ item.phone }}</div>
              </div>
            </div>
          </RouterLink>
        </div>
      </el-col>
    </el-row>
  </el-card>

  <el-card shadow="hover">
    <template #header>
      <div class="two-side">
        <span>热销数码配件</span>
        <div>
          <RouterLink to="/customer/visitor-toall/accessory" class="link text-more">更多数码配件</RouterLink>
          <el-icon><ArrowRight /></el-icon>
        </div>
      </div>
    </template>
    <el-row :gutter="24">
      <el-col v-for="item in hotAccessoryList" :key="item.id" :span="4" class="show-item">
        <div>
          <img :src="item.img" alt="accessory" class="img-accessory" />
        </div>
        <div>
          <div>{{ item.name }}</div>
          <div class="two-side">
            <div class="tag">
              <el-tag v-if="item.saleStatus === 0" type="info" effect="dark">已售罄</el-tag>
              <el-tag v-else-if="item.saleStatus === 1" type="danger" effect="dark">售卖中</el-tag>
              <el-tag v-else-if="item.saleStatus === 2" type="warning" effect="dark">未上架</el-tag>
              <el-tag v-if="item.store > 0" type="success" effect="dark">有货</el-tag>
              <el-tag v-else type="info" effect="dark">缺货</el-tag>
            </div>
            <div class="price">￥{{ item.price }}</div>
          </div>
          <div class="two-side">
            <el-button type="primary" size="small" icon="View" @click="viewAccessoryDetail(item)">查看详情</el-button>
            <el-button
              type="success"
              size="small"
              :loading="joinCartLoading"
              icon="ShoppingCart"
              @click="joinCart(item.id)"
            >
              加入购物车
            </el-button>
          </div>
        </div>
      </el-col>
    </el-row>
  </el-card>

  <Footer />

  <el-dialog
    v-model="dialogState.visible"
    width="600px"
    destroy-on-close
    :close-on-click-modal="false"
    align-center
    :show-close="false"
  >
    <template #header>
      <span class="dialog-title">数码配件详情</span>
    </template>
    <el-row :gutter="24">
      <el-col :span="10">
        <img :src="dialogState.form.img" alt="accessory" class="dialog-img-accessory" />
      </el-col>
      <el-col :span="14">
        <el-row class="dialog-row">
          <el-col :span="24">
            <span class="text-dialog-name">{{ dialogState.form.name }}</span>
            <div class="dialog-tag-price">
              <div class="tag">
                <el-tag v-if="dialogState.form.saleStatus === 0" type="info" effect="dark">已售罄</el-tag>
                <el-tag v-else-if="dialogState.form.saleStatus === 1" type="danger" effect="dark">售卖中</el-tag>
                <el-tag v-else-if="dialogState.form.saleStatus === 2" type="warning" effect="dark">未上架</el-tag>
                <el-tag v-if="dialogState.form.store > 0" type="success" effect="dark">有货</el-tag>
                <el-tag v-else type="info" effect="dark">缺货</el-tag>
              </div>
              <span class="price">￥{{ dialogState.form.price }}</span>
            </div>
          </el-col>
        </el-row>
        <el-row :gutter="24" class="dialog-row">
          <el-col :span="12">
            <span>销售店铺：{{ dialogState.form.shopName }}</span>
          </el-col>
          <el-col :span="12">
            <span>数码配件类型：{{ dialogState.form.typeName }}</span>
          </el-col>
        </el-row>
        <el-row :gutter="24" class="dialog-row">
          <el-col :span="12">
            <span>数码配件销量：{{ formatSalesVolume(dialogState.form.saleVolume) }}</span>
          </el-col>
          <el-col :span="12">
            <span>数码配件库存：{{ formatInventory(dialogState.form.store) }}</span>
          </el-col>
        </el-row>
      </el-col>
    </el-row>
    <el-divider content-position="left">数码配件简介</el-divider>
    <el-row class="dialog-row">
      <el-col :span="24">
        <p>{{ dialogState.form.introduce }}</p>
      </el-col>
    </el-row>
    <template #footer>
      <el-button type="warning" @click="dialogState.visible = false">关闭</el-button>
    </template>
  </el-dialog>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { listSlideshowApi } from '@/api/customer/slideshow'
import { recommendProductApi } from '@/api/customer/product'
import { hotDigitalShopApi } from '@/api/customer/digitalShop'
import { hotAccessoryApi } from '@/api/customer/accessory'
import { joinCartApi } from '@/api/customer/cart'
import { hotProductTypeApi } from '@/api/customer/productType'
import { hotAccessoryTypeApi } from '@/api/customer/accessoryType'
import { latestNoticeApi } from '@/api/customer/notice'
import { codeToText } from 'element-china-area-data'
import { useAuthStore } from '@/stores'
import { ElMessage } from 'element-plus'
import { ElNotification } from 'element-plus'
import Footer from '@/components/Footer.vue'

const authStore = useAuthStore()
const latestNotice = ref({})
const hotProductTypeList = ref([])
const hotAccessoryTypeList = ref([])
const slideshowList = ref([])
const recommendProductList = ref([])
const hotDigitalShopList = ref([])
const hotAccessoryList = ref([])
const joinCartLoading = ref(false)

const dialogState = ref({
  visible: false,
  form: {},
})

const openNotice = () => {
  ElNotification({
    title: latestNotice.value.title,
    message: latestNotice.value.content,
  })
}

const viewAccessoryDetail = (detail) => {
  dialogState.value.visible = true
  dialogState.value.form = detail
}

const joinCart = async (accessoryId) => {
  if (joinCartLoading.value) return
  joinCartLoading.value = true
  try {
    const userId = authStore.authMsg.id
    const res = await joinCartApi({ userId, accessoryId })
    if (res.code === 200) {
      ElMessage.success('加入购物车成功')
    }
  } finally {
    joinCartLoading.value = false
  }
}

// 格式化销量显示
const formatSalesVolume = (volume) => {
  if (!volume || volume === 0) return '暂无销量'
  if (volume < 100) return `${volume}+`
  const hundreds = Math.floor(volume / 100)
  return `${hundreds * 100}+`
}

// 格式化库存显示
const formatInventory = (store) => {
  if (!store || store === 0) return '缺货'
  if (store >= 100) return '有现货'
  return '库存不多'
}

const getLatestNotice = async () => {
  const res = await latestNoticeApi()
  if (res.code === 200) {
    latestNotice.value = res.data
  }
}
const getHotProductType = async () => {
  const res = await hotProductTypeApi()
  if (res.code === 200) {
    hotProductTypeList.value = res.data
  }
}
const getHotAccessoryType = async () => {
  const res = await hotAccessoryTypeApi()
  if (res.code === 200) {
    hotAccessoryTypeList.value = res.data
  }
}
const listSlideshow = async () => {
  const res = await listSlideshowApi()
  if (res.code === 200) {
    slideshowList.value = res.data
  }
}
const getRecommendProduct = async () => {
  const res = await recommendProductApi()
  if (res.code === 200) {
    recommendProductList.value = res.data
  }
}
const getHotDigitalShop = async () => {
  const res = await hotDigitalShopApi()
  if (res.code === 200) {
    hotDigitalShopList.value = res.data
  }
}
const getHotAccessory = async () => {
  const res = await hotAccessoryApi()
  if (res.code === 200) {
    hotAccessoryList.value = res.data
  }
}

onMounted(() => {
  getLatestNotice()
  getHotProductType()
  getHotAccessoryType()
  listSlideshow()
  getRecommendProduct()
  getHotDigitalShop()
  getHotAccessory()
})
</script>

<style scoped>
.top-part {
  height: 480px;
  display: flex;
  gap: var(--spacing-md);
}
.top-left,
.top-right {
  width: 250px;
  background: var(--text-white);
  border-radius: var(--border-radius-md);
  padding: var(--spacing-md);
}
.big-text {
  font-size: var(--font-size-lg);
  display: flex;
  align-items: center;
  gap: var(--spacing-sm);
}
.hot-search {
  display: flex;
  flex-wrap: wrap;
  gap: var(--spacing-md);
}
.hot-search-link {
  color: #1677ff;
}
.hot-search-link:hover {
  text-decoration: underline wavy #faad14;
  text-underline-offset: 2px;
}
.hot-tag {
  display: flex;
  gap: var(--spacing-sm);
  flex-wrap: wrap;
}
.notice-msg {
  height: 80px;
  margin-bottom: var(--spacing-md);
}
.service-item {
  background: var(--primary-color);
  border-radius: var(--border-radius-md);
  color: var(--text-white);
  padding: var(--spacing-sm) 0;
  margin-bottom: var(--spacing-md);
  display: flex;
  flex-direction: column;
  align-items: center;
}
.distribution-item {
  background: var(--success-color);
  border-radius: var(--border-radius-md);
  color: var(--text-white);
  padding: var(--spacing-sm) 0;
  margin-bottom: var(--spacing-md);
  display: flex;
  flex-direction: column;
  align-items: center;
}
.aftersale-item {
  background: var(--warning-color);
  border-radius: var(--border-radius-md);
  color: var(--text-white);
  padding: var(--spacing-sm) 0;
  margin-bottom: var(--spacing-md);
  display: flex;
  flex-direction: column;
  align-items: center;
}
.slideshow {
  width: 853px;
  border-radius: var(--border-radius-md);
}
.two-side {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.text-more {
  color: var(--primary-color);
  cursor: pointer;
  border-bottom: 1px solid var(--primary-color);
}
.img-product {
  height: 160px;
  border-radius: var(--border-radius-lg);
}
.tag {
  display: flex;
  gap: var(--spacing-sm);
}
.price {
  color: var(--danger-color);
  font-size: var(--font-size-lg);
  font-weight: bold;
}
.show-item {
  margin: var(--spacing-sm) 0;
  padding: var(--spacing-sm) 0;
  border-radius: var(--border-radius-md);
  background: var(--item-color);
}
.show-item:hover {
  box-shadow: inset 0 0 0 2px var(--primary-color);
  border-radius: var(--border-radius-md);
}
.img-shop {
  width: 60px;
  border-radius: var(--border-radius-xl);
  margin: 0 var(--spacing-sm);
}
.dialog-title,
.shop-name {
  color: var(--primary-color);
  font-size: var(--font-size-xl);
}
.img-accessory {
  height: 160px;
  border-radius: var(--border-radius-lg);
}
.dialog-img-accessory {
  height: 200px;
  width: 200px;
  border-radius: var(--border-radius-lg);
}
.text-dialog-name {
  color: var(--text-black);
  font-size: var(--font-size-xl);
}
.dialog-tag-price {
  display: flex;
  align-items: center;
  gap: var(--spacing-lg);
}
.dialog-row {
  margin: var(--spacing-md) 0;
  font-size: var(--font-size-md);
  color: var(--text-black);
}
</style>
