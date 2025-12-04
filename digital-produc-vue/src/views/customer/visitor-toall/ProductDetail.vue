<template>
  <el-card shadow="hover">
    <template #header>
      <div class="top-part">
        <span class="product-name">{{ productDetail.name }}</span>
        <div>
          <span>收货地址：</span>
          <el-select
            v-model="buyForm.addressId"
            placeholder="请选择收货地址"
            clearable
            style="width: 500px"
            @change="(val) => (selectAddress = addressList.find((item) => item.id === val))"
          >
            <el-option
              v-for="item in addressList"
              :key="item.id"
              :value="item.id"
              :label="`${item.consignee} ${item.phoneNumber} 
              ${[item.provinceCode, item.cityCode, item.districtCode]
                .map((code) => codeToText[code])
                .filter(Boolean)
                .join(' ')} ${item.detailAddress}`"
            >
              <div class="two-side">
                <span>
                  <el-tag v-if="item.tag">{{ item.tag }}</el-tag>
                  &nbsp;{{ item.consignee }}&nbsp;{{ item.phoneNumber }}
                </span>
                <span>
                  {{
                    [item.provinceCode, item.cityCode, item.districtCode]
                      .map((code) => codeToText[code])
                      .filter(Boolean)
                      .join(' ')
                  }}&nbsp;{{ item.detailAddress }}
                </span>
              </div>
            </el-option>
          </el-select>
        </div>
        <RouterLink :to="`/customer/visitor-toall/digitalShopDetail?id=${productDetail.shopId}`" class="link shop-link">
          <span>点击查看更多数码产品 </span>
          <el-icon><ArrowRight /></el-icon>
          <img :src="productDetail.shopAvatar" alt="shop" class="img-shop" />
          <span>{{ productDetail.shopName }}</span>
        </RouterLink>
      </div>
    </template>
    <el-row :gutter="24">
      <el-col :span="5" class="part-img">
        <img :src="productDetail.img" alt="product" class="img-product" />
      </el-col>
      <el-col :span="19" class="part-msg">
        <div>
          <span class="text-type">数码产品类型：{{ productDetail.typeName }}</span>
        </div>
        <div class="tag-price">
          <div class="tag">
            <el-tag v-if="productDetail.used === 0" type="danger" effect="dark" round>全新</el-tag>
            <el-tag v-else-if="productDetail.used === 1" type="primary" effect="dark" round>二手</el-tag>
            <el-tag v-if="productDetail.saleStatus === 0" type="info" effect="dark">已售罄</el-tag>
            <el-tag v-else-if="productDetail.saleStatus === 1" type="danger" effect="dark">售卖中</el-tag>
            <el-tag v-else-if="productDetail.saleStatus === 2" type="warning" effect="dark">未上架</el-tag>
            <el-tag v-if="productDetail.store > 0" type="success" effect="dark">有货</el-tag>
            <el-tag v-else type="info" effect="dark">缺货</el-tag>
          </div>
          <span class="price">￥{{ productDetail.price }}</span>
        </div>
        <div>
          <el-button
            v-if="!productDetail.hasCollect"
            type="primary"
            :loading="joinCollectLoading"
            icon="Star"
            @click="joinCollect(productDetail.id)"
          >
            加入收藏
          </el-button>
          <el-button
            v-else
            type="warning"
            :loading="outCollectLoading"
            icon="StarFilled"
            @click="outCollect(productDetail.id)"
          >
            取消收藏
          </el-button>
          <el-button
            type="danger"
            :disabled="productDetail.store < 1"
            :loading="buyLoading"
            icon="ShoppingCartFull"
            @click="handleBuy()"
          >
            立即购买
          </el-button>
        </div>
        <div>
          <el-divider content-position="left">数码产品简介</el-divider>
          <p>{{ productDetail.introduce }}</p>
        </div>
      </el-col>
    </el-row>
  </el-card>

  <el-card shadow="hover">
    <template #header>
      <span>数码产品详情</span>
    </template>
    <el-row :gutter="24">
      <el-col :span="18">
        <div v-dompurify-html="productDetail.content"></div>
      </el-col>
      <el-col :span="6">
        <div class="more-product">
          <RouterLink to="/customer/visitor-toall/product" class="link text-more">更多数码产品</RouterLink>
          <el-icon><ArrowRight /></el-icon>
        </div>
        <el-row :gutter="24">
          <el-col v-for="item in recommendProductList" :key="item.id" :span="12" class="show-item">
            <RouterLink :to="`/customer/visitor-toall/productDetail?id=${item.id}`" class="link">
              <div>
                <img :src="item.img" alt="product" class="img-recommend" />
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
    </el-row>
  </el-card>

  <el-drawer v-model="paymentVisible" :before-close="handleClose" size="40%">
    <template #title>
      <div class="payment-title">
        <span>等待付款...</span>
        <el-countdown :value="Date.now() + 10 * 60 * 1000" format="mm:ss" />
      </div>
    </template>
    <el-card>
      <template #header>
        <div class="address-header">
          <span>{{ selectAddress.consignee }}</span>
          <span>{{ selectAddress.phoneNumber }}</span>
          <el-tag v-if="selectAddress.isDefault === 1" type="danger">默认</el-tag>
          <el-tag v-if="selectAddress.tag">{{ selectAddress.tag }}</el-tag>
        </div>
      </template>
      <div>
        {{
          [selectAddress.provinceCode, selectAddress.cityCode, selectAddress.districtCode]
            .map((code) => codeToText[code])
            .filter(Boolean)
            .join(' ')
        }}&nbsp;{{ selectAddress.detailAddress }}
      </div>
    </el-card>
    <el-card>
      <el-row :gutter="24">
        <el-col :span="12">
          <img :src="productDetail.img" alt="product" class="img-product" />
        </el-col>
        <el-col :span="12" class="msg-product">
          <div>{{ productDetail.name }}</div>
          <div class="price">￥{{ orderMsg.orderAmount }}</div>
        </el-col>
      </el-row>
    </el-card>
    <el-card>
      <template #header>
        <span>订单信息&nbsp;</span>
        <el-tag v-if="orderMsg.orderStatus === 0" effect="dark" type="warning">待付款</el-tag>
        <el-tag v-else-if="orderMsg.orderStatus === 1" effect="dark" type="warning">待接单</el-tag>
        <el-tag v-else-if="orderMsg.orderStatus === 2" effect="dark" type="primary">派送中</el-tag>
        <el-tag v-else-if="orderMsg.orderStatus === 3" effect="dark" type="success">已送达</el-tag>
        <el-tag v-else-if="orderMsg.orderStatus === 4" effect="dark" type="success">已完成</el-tag>
        <el-tag v-else-if="orderMsg.orderStatus === 5" effect="dark" type="danger">已取消</el-tag>
      </template>
      <div class="two-side">
        <div>订单号</div>
        <div>{{ orderMsg.orderNo }}</div>
      </div>
      <div class="two-side">
        <div>下单时间</div>
        <div>{{ orderMsg.orderTime }}</div>
      </div>
    </el-card>
    <div class="order-btn">
      <el-button type="info" size="large" :loading="cancelLoading" @click="handleCancel(orderMsg.orderNo)">
        取消订单
      </el-button>
      <el-button type="danger" size="large" :loading="paymentLoading" @click="handlePayment(orderMsg.orderNo)">
        立即支付 ￥{{ orderMsg.orderAmount }}
      </el-button>
    </div>
  </el-drawer>

  <el-dialog
    v-model="topupVisible"
    title="充值窗口"
    :close-on-click-modal="false"
    width="500px"
    align-center
    :show-close="false"
  >
    <el-card>
      <el-input v-model="topupAmount" placeholder="请输入充值金额" clearable type="text" @input="handleAmountInput">
        <template #prefix>￥</template>
      </el-input>
      <el-radio-group v-model="topupAmount">
        <el-radio-button label="￥ 100" :value="100" />
        <el-radio-button label="￥ 200" :value="200" />
        <el-radio-button label="￥ 500" :value="500" />
        <el-radio-button label="￥ 1000" :value="1000" />
      </el-radio-group>
    </el-card>
    <template #footer>
      <div>
        <el-button type="warning" @click="topupVisible = false">取消</el-button>
        <el-button type="primary" :loading="topupLoading" @click="handleTopup(topupAmount)">充值</el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue'
import { detailProductApi, recommendProductApi } from '@/api/customer/product'
import { joinCollectApi, outCollectApi } from '@/api/customer/collect'
import { placeProductOrdersApi, cancelProductOrdersApi, paymentProductOrdersApi } from '@/api/customer/productOrders'
import { listAddressApi } from '@/api/customer/address'
import { topupApi } from '@/api/customer/user'
import { useRoute } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { useAuthStore } from '@/stores'
import { codeToText } from 'element-china-area-data'

const authStore = useAuthStore()
const addressList = ref([])
const route = useRoute()
const productDetail = ref({})
const recommendProductList = ref([])
const joinCollectLoading = ref(false)
const outCollectLoading = ref(false)
const buyLoading = ref(false)
const cancelLoading = ref(false)
const paymentLoading = ref(false)
const topupLoading = ref(false)
const buyForm = ref({})

const orderMsg = ref({})
const paymentVisible = ref(false)

const selectAddress = ref({})

const topupAmount = ref()
const topupVisible = ref(false)

const handleAmountInput = (value) => {
  // 先移除非数字和非小数点的字符
  let formattedValue = value.replace(/[^\d.]/g, '')

  // 处理多个小数点的情况
  const parts = formattedValue.split('.')
  if (parts.length > 2) {
    // 如果有多个小数点，只保留第一个
    formattedValue = parts[0] + '.' + parts.slice(1).join('')
  }

  // 确保小数点后不超过两位
  if (parts.length === 2 && parts[1].length > 2) {
    formattedValue = parts[0] + '.' + parts[1].substring(0, 2)
  }

  topupAmount.value = formattedValue
}

const joinCollect = async (productId) => {
  if (joinCollectLoading.value) return
  joinCollectLoading.value = true
  try {
    const userId = authStore.authMsg.id
    const res = await joinCollectApi({ userId, productId })
    if (res.code === 200) {
      productDetail.value.hasCollect = true
      ElMessage.success('成功加入收藏')
    }
  } finally {
    joinCollectLoading.value = false
  }
}

const outCollect = async (productId) => {
  if (outCollectLoading.value) return
  outCollectLoading.value = true
  try {
    const userId = authStore.authMsg.id
    const res = await outCollectApi(userId, productId)
    if (res.code === 200) {
      productDetail.value.hasCollect = false
      ElMessage.success('已取消收藏')
    }
  } finally {
    outCollectLoading.value = false
  }
}

const handleBuy = async () => {
  if (buyLoading.value) return
  buyLoading.value = true
  try {
    const res = await placeProductOrdersApi(buyForm.value)
    if (res.code === 200) {
      orderMsg.value = res.data
      paymentVisible.value = true
    }
  } finally {
    buyLoading.value = false
  }
}

const handlePayment = async (orderNo) => {
  if (paymentLoading.value) return
  paymentLoading.value = true
  ElMessageBox.confirm('确认要支付该订单吗？', '提醒', {
    confirmButtonText: '确认支付',
    cancelButtonText: '我再想想',
    type: 'warning',
  })
    .then(async () => {
      try {
        const res = await paymentProductOrdersApi(orderNo)
        if (res.code === 200) {
          ElMessage.success('支付成功')
          paymentVisible.value = false
        } else if (res.code === 888) {
          //跳出充值弹窗
          topupVisible.value = true
          topupAmount.value = ''
        }
      } finally {
        paymentLoading.value = false
      }
    })
    .catch(() => {
      paymentLoading.value = false
    })
}

const handleCancel = async (orderNo) => {
  if (cancelLoading.value) return
  cancelLoading.value = true
  ElMessageBox.confirm('确认要取消该订单吗？', '提醒', {
    confirmButtonText: '确认取消',
    cancelButtonText: '我再想想',
    type: 'warning',
  })
    .then(async () => {
      try {
        const res = await cancelProductOrdersApi(orderNo)
        if (res.code === 200) {
          ElMessage.success('订单已取消')
          paymentVisible.value = false
        }
      } finally {
        cancelLoading.value = false
      }
    })
    .catch(() => {
      cancelLoading.value = false
    })
}

const handleTopup = async (topupAmount) => {
  //TODO 后续拓展为二维码充值
  if (topupLoading.value) return
  topupLoading.value = true
  try {
    const res = await topupApi({ userId: authStore.authMsg.id, topupAmount: topupAmount })
    if (res.code === 200) {
      ElMessage.success('充值成功')
      topupVisible.value = false
    }
  } finally {
    topupLoading.value = false
  }
}

const handleClose = () => {
  ElMessageBox.confirm('放弃本次支付？', '提醒', {
    confirmButtonText: '放弃支付',
    cancelButtonText: '继续支付',
    type: 'warning',
  }).then(() => {
    paymentVisible.value = false
  })
}

const handleDetail = async () => {
  const productId = route.query.id
  const userId = authStore.authMsg.id
  const role = authStore.authMsg.role
  const res = await detailProductApi(productId, userId, role)
  if (res.code === 200) {
    productDetail.value = res.data
    buyForm.value.userId = userId
    buyForm.value.shopId = productDetail.value.shopId
    buyForm.value.productId = productId
  }
}

const getRecommendProduct = async () => {
  const res = await recommendProductApi()
  if (res.code === 200) {
    recommendProductList.value = res.data
  }
}

const listAddress = async () => {
  const role = authStore.authMsg.role
  if (role !== 2) return
  const res = await listAddressApi()
  if (res.code === 200) {
    addressList.value = res.data
    const defaultAddress = addressList.value.find((item) => item.isDefault === 1)
    if (defaultAddress) {
      buyForm.value.addressId = defaultAddress.id
      selectAddress.value = defaultAddress
    }
  }
}

onMounted(() => {
  listAddress()
  handleDetail()
  getRecommendProduct()
})

watch(
  () => route.query.id,
  (newId, oldId) => {
    if (newId !== oldId) {
      handleDetail()
    }
  },
)
</script>

<style scoped>
.top-part,
.two-side {
  display: flex;
  align-items: center;
  justify-content: space-between;
}
.shop-link {
  display: flex;
  align-items: center;
  gap: var(--spacing-sm);
  font-size: var(--font-size-lg);
}
.shop-link:hover {
  color: var(--primary-color);
}
.img-product {
  height: 190px;
  border-radius: var(--border-radius-lg);
}
.img-shop {
  height: 50px;
  width: 50px;
  border-radius: var(--border-radius-xl);
}
.product-name {
  font-size: 30px;
  color: var(--primary-color);
  border-bottom: 2px solid var(--warning-color);
  padding-bottom: var(--spacing-sm);
}
.text-type {
  font-size: var(--font-size-lg);
}
.tag-price {
  display: flex;
  align-items: center;
  gap: var(--spacing-xl);
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
.part-img {
  display: flex;
  align-items: center;
}
.part-msg {
  display: flex;
  flex-direction: column;
  gap: var(--spacing-sm);
}
.text-more {
  color: var(--primary-color);
}
.more-product {
  display: flex;
  justify-content: flex-end;
  align-items: center;
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
.img-recommend {
  height: 110px;
  border-radius: var(--border-radius-md);
}
.address-header {
  font-size: var(--font-size-lg);
  display: flex;
  gap: var(--spacing-md);
}
.msg-product {
  display: flex;
  align-items: center;
  gap: var(--spacing-md);
}
.payment-title {
  text-align: center;
  font-size: var(--font-size-xl);
  font-weight: bold;
}
.order-btn {
  margin-top: var(--spacing-md);
  display: flex;
  justify-content: center;
  align-items: center;
}
</style>
