<template>
  <el-card shadow="hover" class="cart-card">
    <el-form :inline="true" :model="buyForm">
      <el-form-item>
        <template #label>
          <div class="big-text">全部商品（{{ tableState.dataList.length }}件）</div>
        </template>
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
      </el-form-item>
      <el-form-item>
        <template #label>
          <div class="big-text">已选（{{ tableState.ids.length }}件）</div>
          <div class="big-text">
            <span>合计：</span>
            <span class="price">￥{{ totalAmount.toFixed(2) }}</span>
          </div>
        </template>
        <el-button icon="ShoppingCartFull" type="danger" :loading="checkoutLoading" @click="handleOrder">
          去结算
        </el-button>
      </el-form-item>
    </el-form>

    <el-table
      v-loading="listLoading"
      :data="tableState.dataList"
      height="670"
      :row-key="(row) => row.id"
      @selection-change="selectChange"
    >
      <template #empty>
        <el-empty description="购物车空空如也~" />
      </template>
      <el-table-column
        type="selection"
        align="center"
        :selectable="(row) => row.accessoryStore > 0 && row.accessorySaleStatus === 1"
      />
      <el-table-column label="商品图片" prop="accessoryImg" align="center" width="300">
        <template #default="{ row }">
          <el-image
            :src="row.accessoryImg"
            :preview-src-list="[row.accessoryImg]"
            fit="cover"
            :preview-teleported="true"
            class="accessory-img"
            :style="row.accessoryStore <= 0 || row.accessorySaleStatus != 1 ? 'filter: grayscale(100%);' : ''"
          />
        </template>
      </el-table-column>
      <el-table-column label="商品名称" prop="accessoryName" align="center" show-overflow-tooltip width="300">
        <template #default="{ row }">
          <div class="tag">
            <el-tag v-if="row.accessorySaleStatus === 0" type="info" effect="dark" size="large">已售罄</el-tag>
            <el-tag v-else-if="row.accessorySaleStatus === 1" type="danger" effect="dark" size="large">售卖中</el-tag>
            <el-tag v-else-if="row.accessorySaleStatus === 2" type="warning" effect="dark" size="large">未上架</el-tag>
            <el-tag v-if="row.accessoryStore > 0" type="success" effect="dark" size="large">有货</el-tag>
            <el-tag v-else type="info" effect="dark" size="large">缺货</el-tag>
          </div>
          <el-divider />
          <div class="big-text">{{ row.accessoryName }}</div>
        </template>
      </el-table-column>
      <el-table-column label="商品单价" prop="accessoryPrice" align="center" width="200">
        <template #default="{ row }">
          <span class="price">￥{{ row.accessoryPrice }}</span>
        </template>
      </el-table-column>
      <el-table-column label="选择数量" prop="quantity" align="center" width="200">
        <template #default="{ row }">
          <el-input-number
            v-model="row.quantity"
            :precision="0"
            :min="1"
            style="width: 100%"
            @change="() => selectChange(tableState.dataList.filter((item) => tableState.ids.includes(item.id)))"
          />
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" min-width="200">
        <template #default="{ row }">
          <el-button icon="Delete" type="danger" :loading="removeLoading" @click="handleRemove(row.accessoryId)">
            移除购物车
          </el-button>
        </template>
      </el-table-column>
    </el-table>
  </el-card>

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
      <div v-for="(accessory, idx) in showAccessory" :key="accessory.id" class="accessory-msg">
        <div>{{ accessory.accessoryName }}</div>
        <div>￥{{ accessory.accessoryPrice }} × {{ accessory.quantity }}</div>
        <div class="price">￥{{ orderMsg[idx].orderAmount }}</div>
        <div>订单号: {{ orderMsg[idx].orderNo }}</div>
      </div>
    </el-card>
    <el-card>
      <template #header>
        <span>订单信息&nbsp;</span>
        <el-tag v-if="orderMsg[0].orderStatus === 0" effect="dark" type="warning">待付款</el-tag>
        <el-tag v-else-if="orderMsg[0].orderStatus === 1" effect="dark" type="warning">待接单</el-tag>
        <el-tag v-else-if="orderMsg[0].orderStatus === 2" effect="dark" type="primary">派送中</el-tag>
        <el-tag v-else-if="orderMsg[0].orderStatus === 3" effect="dark" type="success">已送达</el-tag>
        <el-tag v-else-if="orderMsg[0].orderStatus === 4" effect="dark" type="success">已完成</el-tag>
        <el-tag v-else-if="orderMsg[0].orderStatus === 5" effect="dark" type="danger">已取消</el-tag>
      </template>
      <div class="two-side">
        <div>下单时间</div>
        <div>{{ orderMsg[0].orderTime }}</div>
      </div>
      <div class="two-side">
        <div>总价</div>
        <div class="price">￥{{ Number(totalMoney).toFixed(2) }}</div>
      </div>
    </el-card>
    <div class="order-btn">
      <el-button type="info" size="large" @click="handleClose">稍后再付</el-button>
      <el-button type="danger" size="large" :loading="paymentLoading" @click="handlePayment(orderMsg)">
        立即支付 ￥{{ totalMoney.toFixed(2) }}
      </el-button>
    </div>
  </el-drawer>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { listCartApi, outCartApi } from '@/api/customer/cart'
import { listAddressApi } from '@/api/customer/address'
import { placeAccessoryOrdersApi, paymentAccessoryOrdersApi } from '@/api/customer/accessoryOrders'
import { ElMessage, ElMessageBox } from 'element-plus'
import { topupApi } from '@/api/customer/user'
import { codeToText } from 'element-china-area-data'
import { useAuthStore } from '@/stores'

const authStore = useAuthStore()

const addressList = ref([])

const checkoutLoading = ref(false)
const topupLoading = ref(false)
const listLoading = ref(false)
const removeLoading = ref(false)
const paymentLoading = ref(false)
const buyForm = ref({
  selectAccessories: [],
})

const selectAddress = ref({})

const orderMsg = ref({})
const paymentVisible = ref(false)

const tableState = ref({
  pageNum: 1,
  pageSize: 10,
  dataList: [],
  ids: [],
})

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

const totalAmount = ref(0)
const showAccessory = ref([])

const totalMoney = ref(0)

const selectChange = (rows) => {
  buyForm.value.selectAccessories = rows.map((item) => ({
    accessoryId: item.accessoryId,
    quantity: Number(item.quantity),
  }))

  showAccessory.value = rows

  tableState.value.ids = rows.map((item) => item.id)
  totalAmount.value = rows.reduce((sum, item) => {
    const quantity = Number(item.quantity) || 1
    const price = Number(item.accessoryPrice) || 0
    return sum + quantity * price
  }, 0)
}

const handleList = async () => {
  if (listLoading.value) return
  listLoading.value = true
  try {
    const userId = authStore.authMsg.id
    const res = await listCartApi()
    if (res.code === 200) {
      tableState.value.dataList = res.data
      buyForm.value.userId = userId
    }
  } finally {
    listLoading.value = false
  }
}

const listAddress = async () => {
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

const handleOrder = async () => {
  if (checkoutLoading.value) return
  checkoutLoading.value = true
  try {
    const res = await placeAccessoryOrdersApi(buyForm.value)
    if (res.code === 200) {
      orderMsg.value = res.data
      totalMoney.value = orderMsg.value.reduce((sum, item) => sum + Number(item.orderAmount || 0), 0)
      paymentVisible.value = true
    }
  } finally {
    checkoutLoading.value = false
  }
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

const handleRemove = (accessoryId) => {
  if (removeLoading.value) return
  removeLoading.value = true
  ElMessageBox.confirm('删除操作不可逆，是否继续？', 'Warning', {
    confirmButtonText: '是',
    cancelButtonText: '否',
    type: 'warning',
  })
    .then(async () => {
      try {
        const userId = authStore.authMsg.id
        const res = await outCartApi(userId, accessoryId)
        if (res.code === 200) {
          ElMessage.success('删除成功')
          handleList()
        }
      } catch {
        ElMessage.error('删除失败')
      } finally {
        removeLoading.value = false
      }
    })
    .catch(() => {
      removeLoading.value = false
    })
}

const handlePayment = async (orderMsg) => {
  if (paymentLoading.value) return
  paymentLoading.value = true
  ElMessageBox.confirm('确认要支付该订单吗？', '提醒', {
    confirmButtonText: '确认支付',
    cancelButtonText: '我再想想',
    type: 'warning',
  })
    .then(async () => {
      try {
        const orderNoArray = orderMsg.map((item) => item.orderNo)
        const res = await paymentAccessoryOrdersApi(orderNoArray)
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

onMounted(() => {
  listAddress()
  handleList()
})
</script>

<style scoped>
.two-side {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.cart-card {
  border-radius: var(--border-radius-lg);
}
.big-text {
  font-size: var(--font-size-lg);
  color: var(--text-black);
}
.accessory-img {
  border-radius: var(--border-radius-lg);
  width: 100%;
  height: 100%;
  max-width: 200px;
  max-height: 150px;
}
.tag {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: var(--spacing-sm);
}
.price {
  color: var(--danger-color);
  font-size: var(--font-size-lg);
  font-weight: bold;
}
.order-btn {
  margin-top: var(--spacing-md);
  display: flex;
  justify-content: center;
  align-items: center;
}
.address-header {
  font-size: var(--font-size-lg);
  display: flex;
  gap: var(--spacing-md);
}
.payment-title {
  text-align: center;
  font-size: var(--font-size-xl);
  font-weight: bold;
}
.accessory-msg {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>
