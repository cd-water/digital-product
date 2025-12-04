<template>
  <el-tabs type="border-card">
    <el-tab-pane label="数码产品订单">
      <el-tabs v-model="productStatus" type="card">
        <el-tab-pane v-for="tab in orderTabs" :key="tab.name" :label="tab.label" :name="tab.name">
          <div class="search-part">
            <el-input v-model="orderNo" placeholder="请输入订单号搜索" clearable />
            <el-button icon="Search" type="primary" @click="productOrderNoSearch">搜索</el-button>
          </div>
          <template v-if="productStatusList[tab.name] && productStatusList[tab.name].length > 0">
            <el-card v-for="item in productStatusList[tab.name]" :key="item.id">
              <template #header>
                <RouterLink :to="`/customer/visitor-toall/digitalShopDetail?id=${item.shopId}`" class="link shop-link">
                  <span>{{ item.shopName }}</span>
                  <el-icon><ArrowRight /></el-icon>
                </RouterLink>
              </template>
              <el-row :gutter="24">
                <el-col :span="5">
                  <img :src="item.productImg" alt="product" class="product-img" />
                </el-col>
                <el-col :span="19">
                  <el-card>
                    <template #header>
                      <div class="two-side">
                        <div>
                          <span>订单信息&nbsp;</span>
                          <el-tag v-if="item.orderStatus === 0" effect="dark" type="warning">待付款</el-tag>
                          <el-tag v-else-if="item.orderStatus === 1" effect="dark" type="warning">待接单</el-tag>
                          <el-tag v-else-if="item.orderStatus === 2" effect="dark" type="primary">派送中</el-tag>
                          <el-tag v-else-if="item.orderStatus === 3" effect="dark" type="success">已送达</el-tag>
                          <el-tag v-else-if="item.orderStatus === 4" effect="dark" type="success">已完成</el-tag>
                          <el-tag v-else-if="item.orderStatus === 5" effect="dark" type="danger">已取消</el-tag>
                        </div>
                        <div>
                          <el-button
                            v-show="item.orderStatus === 0 || item.orderStatus === 1"
                            type="warning"
                            :loading="cancelLoading"
                            @click="handleproductCancel(item.orderNo)"
                          >
                            取消订单
                          </el-button>
                          <el-button
                            v-show="item.orderStatus === 3"
                            type="primary"
                            :loading="completedLoading"
                            @click="handleproductCompleted(item.orderNo)"
                          >
                            完成订单
                          </el-button>
                          <RouterLink :to="`/customer/visitor-toall/productDetail?id=${item.productId}`">
                            <el-button v-show="item.orderStatus === 4 || item.orderStatus === 5" type="success">
                              再次购买
                            </el-button>
                          </RouterLink>
                        </div>
                      </div>
                    </template>
                    <div class="two-side">
                      <div>{{ item.productName }}</div>
                      <div class="price">￥{{ item.productPrice }}</div>
                    </div>
                    <div class="two-side">
                      <div>订单号</div>
                      <div>{{ item.orderNo }}</div>
                    </div>
                    <div class="two-side">
                      <div>下单时间</div>
                      <div>{{ item.orderTime }}</div>
                    </div>
                  </el-card>
                </el-col>
              </el-row>
            </el-card>
          </template>
          <el-empty v-else description="暂无订单" />
        </el-tab-pane>
      </el-tabs>
    </el-tab-pane>
    <el-tab-pane label="数码配件订单">
      <el-tabs v-model="accessoryStatus" type="card">
        <el-tab-pane v-for="tab in orderTabs" :key="tab.name" :label="tab.label" :name="tab.name">
          <div class="search-part">
            <el-input v-model="orderNo" placeholder="请输入订单号搜索" clearable />
            <el-button icon="Search" type="primary" @click="accessoryOrderNoSearch">搜索</el-button>
          </div>
          <template v-if="accessoryStatusList[tab.name] && accessoryStatusList[tab.name].length > 0">
            <el-card v-for="item in accessoryStatusList[tab.name]" :key="item.id">
              <template #header>
                <RouterLink :to="`/customer/visitor-toall/digitalShopDetail?id=${item.shopId}`" class="link shop-link">
                  <span>{{ item.shopName }}</span>
                  <el-icon><ArrowRight /></el-icon>
                </RouterLink>
              </template>
              <el-row :gutter="24">
                <el-col :span="5">
                  <img :src="item.accessoryImg" alt="accessory" class="accessory-img" />
                </el-col>
                <el-col :span="19">
                  <el-card>
                    <template #header>
                      <div class="two-side">
                        <div>
                          <span>订单信息&nbsp;</span>
                          <el-tag v-if="item.orderStatus === 0" effect="dark" type="warning">待付款</el-tag>
                          <el-tag v-else-if="item.orderStatus === 1" effect="dark" type="warning">待接单</el-tag>
                          <el-tag v-else-if="item.orderStatus === 2" effect="dark" type="primary">派送中</el-tag>
                          <el-tag v-else-if="item.orderStatus === 3" effect="dark" type="success">已送达</el-tag>
                          <el-tag v-else-if="item.orderStatus === 4" effect="dark" type="success">已完成</el-tag>
                          <el-tag v-else-if="item.orderStatus === 5" effect="dark" type="danger">已取消</el-tag>
                        </div>
                        <div>
                          <el-button
                            v-show="item.orderStatus === 0 || item.orderStatus === 1"
                            type="warning"
                            :loading="cancelLoading"
                            @click="handleAccessoryCancel(item.orderNo)"
                          >
                            取消订单
                          </el-button>
                          <el-button
                            v-show="item.orderStatus === 3"
                            type="primary"
                            :loading="completedLoading"
                            @click="handleAccessoryCompleted(item.orderNo)"
                          >
                            完成订单
                          </el-button>
                        </div>
                      </div>
                    </template>
                    <div class="two-side">
                      <div>{{ item.accessoryName }}</div>
                      <div>￥{{ item.accessoryPrice }} × {{ item.quantity }}</div>
                      <div class="price">￥{{ item.totalPrice }}</div>
                    </div>
                    <div class="two-side">
                      <div>订单号</div>
                      <div>{{ item.orderNo }}</div>
                    </div>
                    <div class="two-side">
                      <div>下单时间</div>
                      <div>{{ item.orderTime }}</div>
                    </div>
                  </el-card>
                </el-col>
              </el-row>
            </el-card>
          </template>
          <el-empty v-else description="暂无订单" />
        </el-tab-pane>
      </el-tabs>
    </el-tab-pane>
  </el-tabs>

  <el-dialog
    v-model="searchproductVisible"
    title="订单信息"
    :close-on-click-modal="false"
    width="500px"
    align-center
    :show-close="false"
  >
    <el-descriptions :column="1" border>
      <el-descriptions-item label="订单号">{{ searchproductObj.orderNo }}</el-descriptions-item>
      <el-descriptions-item label="订单状态">
        <el-tag v-if="searchproductObj.orderStatus === 0" effect="dark" type="warning">待付款</el-tag>
        <el-tag v-else-if="searchproductObj.orderStatus === 1" effect="dark" type="warning">待接单</el-tag>
        <el-tag v-else-if="searchproductObj.orderStatus === 2" effect="dark" type="primary">派送中</el-tag>
        <el-tag v-else-if="searchproductObj.orderStatus === 3" effect="dark" type="success">已送达</el-tag>
        <el-tag v-else-if="searchproductObj.orderStatus === 4" effect="dark" type="success">已完成</el-tag>
        <el-tag v-else-if="searchproductObj.orderStatus === 5" effect="dark" type="danger">已取消</el-tag>
      </el-descriptions-item>
      <el-descriptions-item label="下单时间">{{ searchproductObj.orderTime }}</el-descriptions-item>
      <el-descriptions-item label="数码产品名称">{{ searchproductObj.productName }}</el-descriptions-item>
      <el-descriptions-item label="数码产品图片">
        <el-image
          :src="searchproductObj.productImg"
          fit="cover"
          :preview-teleported="true"
          :preview-src-list="[searchproductObj.productImg]"
          class="product-img"
        />
      </el-descriptions-item>
      <el-descriptions-item label="价格">
        <span class="price">￥{{ searchproductObj.productPrice }}</span>
      </el-descriptions-item>
      <el-descriptions-item label="店铺昵称">{{ searchproductObj.shopName }}</el-descriptions-item>
    </el-descriptions>
    <template #footer>
      <el-button type="warning" @click="searchproductVisible = false">关闭</el-button>
    </template>
  </el-dialog>

  <el-dialog
    v-model="searchAccessoryVisible"
    title="订单信息"
    :close-on-click-modal="false"
    width="500px"
    align-center
    :show-close="false"
  >
    <el-descriptions :column="1" border>
      <el-descriptions-item label="订单号">{{ searchAccessoryObj.orderNo }}</el-descriptions-item>
      <el-descriptions-item label="订单状态">
        <el-tag v-if="searchAccessoryObj.orderStatus === 0" effect="dark" type="warning">待付款</el-tag>
        <el-tag v-else-if="searchAccessoryObj.orderStatus === 1" effect="dark" type="warning">待接单</el-tag>
        <el-tag v-else-if="searchAccessoryObj.orderStatus === 2" effect="dark" type="primary">派送中</el-tag>
        <el-tag v-else-if="searchAccessoryObj.orderStatus === 3" effect="dark" type="success">已送达</el-tag>
        <el-tag v-else-if="searchAccessoryObj.orderStatus === 4" effect="dark" type="success">已完成</el-tag>
        <el-tag v-else-if="searchAccessoryObj.orderStatus === 5" effect="dark" type="danger">已取消</el-tag>
      </el-descriptions-item>
      <el-descriptions-item label="下单时间">{{ searchAccessoryObj.orderTime }}</el-descriptions-item>
      <el-descriptions-item label="数码配件名称">{{ searchAccessoryObj.accessoryName }}</el-descriptions-item>
      <el-descriptions-item label="数码配件图片">
        <el-image
          :src="searchAccessoryObj.accessoryImg"
          fit="cover"
          :preview-teleported="true"
          :preview-src-list="[searchAccessoryObj.accessoryImg]"
          class="accessory-img"
        />
      </el-descriptions-item>
      <el-descriptions-item label="数码配件单价">
        <span class="price">￥{{ searchAccessoryObj.accessoryPrice }}</span>
      </el-descriptions-item>
      <el-descriptions-item label="购买数量">{{ searchAccessoryObj.quantity }}</el-descriptions-item>
      <el-descriptions-item label="数码配件总价">
        <span class="price">￥{{ searchAccessoryObj.totalPrice }}</span>
      </el-descriptions-item>
      <el-descriptions-item label="店铺昵称">{{ searchAccessoryObj.shopName }}</el-descriptions-item>
    </el-descriptions>
    <template #footer>
      <el-button type="warning" @click="searchAccessoryVisible = false">关闭</el-button>
    </template>
  </el-dialog>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { listProductOrdersApi, cancelProductOrdersApi, completedProductOrdersApi } from '@/api/customer/productOrders'
import {
  listAccessoryOrdersApi,
  cancelAccessoryOrdersApi,
  completedAccessoryOrdersApi,
} from '@/api/customer/accessoryOrders'
import { ElMessage, ElMessageBox } from 'element-plus'

const cancelLoading = ref(false)
const completedLoading = ref(false)

const orderNo = ref()

const productStatus = ref(6)
const accessoryStatus = ref(6)

const orderTabs = [
  { label: '全部', name: 6 },
  { label: '待付款', name: 0 },
  { label: '待接单', name: 1 },
  { label: '派送中', name: 2 },
  { label: '已送达', name: 3 },
  { label: '已完成', name: 4 },
  { label: '已取消', name: 5 },
]

const searchproductVisible = ref(false)
const searchproductObj = ref({})
const productOrderNoSearch = () => {
  const found = productStatusList.value[6]?.find((item) => item.orderNo === orderNo.value)
  if (!found) return
  searchproductObj.value = found
  searchproductVisible.value = true
}

const searchAccessoryVisible = ref(false)
const searchAccessoryObj = ref({})
const accessoryOrderNoSearch = () => {
  const found = accessoryStatusList.value[6]?.find((item) => item.orderNo === orderNo.value)
  if (!found) return
  searchAccessoryObj.value = found
  searchAccessoryVisible.value = true
}

const productStatusList = ref([])

const allproductSearch = async () => {
  const res = await listProductOrdersApi()
  if (res.code === 200) {
    const allData = res.data
    const arr = [[], [], [], [], [], [], []]
    allData.forEach((item) => {
      if (item.orderStatus >= 0 && item.orderStatus <= 5) {
        arr[item.orderStatus].push(item)
      }
    })
    arr[6] = allData
    productStatusList.value = arr
  }
}

const accessoryStatusList = ref([])

const allAccessorySearch = async () => {
  const res = await listAccessoryOrdersApi()
  if (res.code === 200) {
    const allData = res.data
    const arr = [[], [], [], [], [], [], []]
    allData.forEach((item) => {
      if (item.orderStatus >= 0 && item.orderStatus <= 5) {
        arr[item.orderStatus].push(item)
      }
    })
    arr[6] = allData
    accessoryStatusList.value = arr
  }
}

const handleproductCancel = (orderNo) => {
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
          allproductSearch()
        }
      } finally {
        cancelLoading.value = false
      }
    })
    .catch(() => {
      cancelLoading.value = false
    })
}

const handleAccessoryCancel = (orderNo) => {
  if (cancelLoading.value) return
  cancelLoading.value = true
  ElMessageBox.confirm('确认要取消该订单吗？', '提醒', {
    confirmButtonText: '确认取消',
    cancelButtonText: '我再想想',
    type: 'warning',
  })
    .then(async () => {
      try {
        const res = await cancelAccessoryOrdersApi(orderNo)
        if (res.code === 200) {
          ElMessage.success('订单已取消')
          allAccessorySearch()
        }
      } finally {
        cancelLoading.value = false
      }
    })
    .catch(() => {
      cancelLoading.value = false
    })
}

const handleproductCompleted = (orderNo) => {
  if (completedLoading.value) return
  completedLoading.value = true
  ElMessageBox.confirm('该订单是否已完成？', '提醒', {
    confirmButtonText: '是',
    cancelButtonText: '否',
    type: 'warning',
  })
    .then(async () => {
      try {
        const res = await completedProductOrdersApi(orderNo)
        if (res.code === 200) {
          ElMessage.success('订单已完成')
          allproductSearch()
        }
      } finally {
        completedLoading.value = false
      }
    })
    .catch(() => {
      completedLoading.value = false
    })
}

const handleAccessoryCompleted = (orderNo) => {
  if (completedLoading.value) return
  completedLoading.value = true
  ElMessageBox.confirm('该订单是否已完成？', '提醒', {
    confirmButtonText: '是',
    cancelButtonText: '否',
    type: 'warning',
  })
    .then(async () => {
      try {
        const res = await completedAccessoryOrdersApi(orderNo)
        if (res.code === 200) {
          ElMessage.success('订单已完成')
          allAccessorySearch()
        }
      } finally {
        completedLoading.value = false
      }
    })
    .catch(() => {
      completedLoading.value = false
    })
}

onMounted(() => {
  allproductSearch()
  allAccessorySearch()
})
</script>

<style scoped>
.shop-link {
  color: var(--primary-color);
  cursor: pointer;
  font-size: var(--font-size-lg);
}
.accessory-img,
.product-img {
  height: 180px;
  border-radius: var(--border-radius-lg);
}
.two-side {
  display: flex;
  align-items: center;
  justify-content: space-between;
}
.price {
  color: var(--danger-color);
  font-size: var(--font-size-lg);
  font-weight: bold;
}
.search-part {
  display: flex;
  gap: var(--spacing-xl);
  margin-bottom: var(--spacing-md);
}
</style>
