<template>
  <el-form :inline="true" :model="tableState.searchForm" @submit.prevent>
    <el-form-item>
      <el-input
        v-model="tableState.searchForm.orderNo"
        placeholder="请输入订单号查询"
        clearable
        @keyup.enter="handlePage"
      />
    </el-form-item>
    <el-form-item>
      <el-select
        v-model="tableState.searchForm.orderStatus"
        placeholder="请选择订单状态"
        clearable
        style="width: 180px"
        @change="handlePage"
      >
        <el-option label="待付款" :value="0" />
        <el-option label="待接单" :value="1" />
        <el-option label="派送中" :value="2" />
        <el-option label="已送达" :value="3" />
        <el-option label="已完成" :value="4" />
        <el-option label="已取消" :value="5" />
      </el-select>
    </el-form-item>
    <el-form-item label="下单时间查询">
      <el-date-picker
        v-model="tableState.searchForm.orderTime"
        type="datetime"
        format="YYYY-MM-DD HH:mm:ss"
        value-format="YYYY-MM-DD HH:mm:ss"
        placeholder="请选择订单时间~至今"
        @keydown.enter="handlePage"
      />
    </el-form-item>
    <el-form-item>
      <el-button icon="Search" type="primary" :loading="pageLoading" @click="handlePage">查询</el-button>
      <el-button icon="Refresh" type="warning" @click="handleClear">重置</el-button>
    </el-form-item>
  </el-form>

  <el-table v-loading="pageLoading" :data="tableState.dataList" height="595">
    <template #empty>
      <el-empty description="暂无订单信息" />
    </template>
    <el-table-column label="序号" type="index" align="center" width="50" />
    <el-table-column label="订单号" prop="orderNo" align="center" width="250" />
    <el-table-column label="订单状态" prop="orderStatus" align="center" width="120">
      <template #default="{ row }">
        <el-tag v-if="row.orderStatus === 0" effect="dark" type="warning">待付款</el-tag>
        <el-tag v-else-if="row.orderStatus === 1" effect="dark" type="warning">待接单</el-tag>
        <el-tag v-else-if="row.orderStatus === 2" effect="dark" type="primary">派送中</el-tag>
        <el-tag v-else-if="row.orderStatus === 3" effect="dark" type="success">已送达</el-tag>
        <el-tag v-else-if="row.orderStatus === 4" effect="dark" type="success">已完成</el-tag>
        <el-tag v-else-if="row.orderStatus === 5" effect="dark" type="danger">已取消</el-tag>
      </template>
    </el-table-column>
    <el-table-column label="下单时间" prop="orderTime" align="center" sortable width="180" />
    <el-table-column label="配件名称" prop="accessoryName" align="center" show-overflow-tooltip width="160" />
    <el-table-column label="配件图片" prop="accessoryImg" align="center" width="80">
      <template #default="{ row }">
        <el-image
          :src="row.accessoryImg"
          :preview-src-list="[row.accessoryImg]"
          fit="cover"
          :preview-teleported="true"
          style="border-radius: var(--border-radius-lg)"
        />
      </template>
    </el-table-column>
    <el-table-column label="配件单价" prop="accessoryPrice" align="center" width="150">
      <template #default="{ row }">
        <span class="price">￥{{ row.accessoryPrice }}</span>
      </template>
    </el-table-column>
    <el-table-column label="购买数量" prop="quantity" align="center" width="100" />
    <el-table-column label="配件总价" prop="totalPrice" align="center" width="150">
      <template #default="{ row }">
        <span class="price">￥{{ row.totalPrice }}</span>
      </template>
    </el-table-column>
    <el-table-column label="收货人" prop="consignee" align="center" show-overflow-tooltip width="160" />
    <el-table-column label="手机号" prop="phoneNumber" align="center" width="150" />
    <el-table-column label="省市区" align="center" show-overflow-tooltip width="200">
      <template #default="{ row }">
        <span>
          {{
            [row.provinceCode, row.cityCode, row.districtCode]
              .map((code) => codeToText[code])
              .filter(Boolean)
              .join(' ')
          }}
        </span>
      </template>
    </el-table-column>
    <el-table-column label="详细地址" prop="detailAddress" align="center" show-overflow-tooltip width="200" />
    <el-table-column label="用户昵称" prop="userNickname" align="center" show-overflow-tooltip width="160" />
    <el-table-column label="店铺昵称" prop="shopName" align="center" show-overflow-tooltip width="160" />
    <el-table-column v-if="authStore.authMsg.role === 1" label="操作" align="center" fixed="right" min-width="300">
      <template #default="{ row }">
        <el-button
          v-show="row.orderStatus === 1"
          icon="Check"
          type="primary"
          :loading="acceptLoading"
          @click="acceptOrder(row.orderNo)"
        >
          接单
        </el-button>
        <el-button
          v-show="row.orderStatus === 2"
          icon="Location"
          type="success"
          :loading="deliveryLoading"
          @click="deliveryOrder(row.orderNo)"
        >
          送达
        </el-button>
        <el-button
          v-show="[0, 1, 2].includes(row.orderStatus)"
          icon="CircleClose"
          type="danger"
          :loading="cancelLoading"
          @click="cancelOrder(row.orderNo)"
        >
          取消
        </el-button>
        <el-button
          v-show="[3, 4].includes(row.orderStatus)"
          icon="RefreshLeft"
          type="warning"
          :loading="refundLoading"
          @click="refundOrder(row.orderNo)"
        >
          售后退款
        </el-button>
        <el-button
          icon="View"
          type="primary"
          @click="
            () => {
              orderDetail = row
              dialogVisible = true
            }
          "
        >
          查看
        </el-button>
      </template>
    </el-table-column>
  </el-table>

  <el-dialog
    v-model="dialogVisible"
    title="订单详情"
    width="500px"
    :close-on-click-modal="false"
    align-center
    :show-close="false"
  >
    <el-descriptions :column="1" border>
      <el-descriptions-item label="订单号">{{ orderDetail.orderNo }}</el-descriptions-item>
      <el-descriptions-item label="订单状态">
        <el-tag v-if="orderDetail.orderStatus === 0" effect="dark" type="warning">待付款</el-tag>
        <el-tag v-else-if="orderDetail.orderStatus === 1" effect="dark" type="warning">待接单</el-tag>
        <el-tag v-else-if="orderDetail.orderStatus === 2" effect="dark" type="primary">派送中</el-tag>
        <el-tag v-else-if="orderDetail.orderStatus === 3" effect="dark" type="success">已送达</el-tag>
        <el-tag v-else-if="orderDetail.orderStatus === 4" effect="dark" type="success">已完成</el-tag>
        <el-tag v-else-if="orderDetail.orderStatus === 5" effect="dark" type="danger">已取消</el-tag>
      </el-descriptions-item>
      <el-descriptions-item label="下单时间">{{ orderDetail.orderTime }}</el-descriptions-item>
      <el-descriptions-item label="配件名称">{{ orderDetail.accessoryName }}</el-descriptions-item>
      <el-descriptions-item label="配件图片">
        <el-image
          :src="orderDetail.accessoryImg"
          fit="cover"
          :preview-teleported="true"
          :preview-src-list="[orderDetail.accessoryImg]"
          class="accessory-img"
        />
      </el-descriptions-item>
      <el-descriptions-item label="配件单价">
        <span class="price">￥{{ orderDetail.accessoryPrice }}</span>
      </el-descriptions-item>
      <el-descriptions-item label="购买数量">{{ orderDetail.quantity }}</el-descriptions-item>
      <el-descriptions-item label="配件总价">
        <span class="price">￥{{ orderDetail.totalPrice }}</span>
      </el-descriptions-item>
      <el-descriptions-item label="收货人">{{ orderDetail.consignee }}</el-descriptions-item>
      <el-descriptions-item label="手机号">{{ orderDetail.phoneNumber }}</el-descriptions-item>
      <el-descriptions-item label="省市区">
        <span>
          {{
            [orderDetail.provinceCode, orderDetail.cityCode, orderDetail.districtCode]
              .map((code) => codeToText[code])
              .filter(Boolean)
              .join(' ')
          }}
        </span>
      </el-descriptions-item>
      <el-descriptions-item label="详细地址">{{ orderDetail.detailAddress }}</el-descriptions-item>
      <el-descriptions-item label="用户昵称">{{ orderDetail.userNickname }}</el-descriptions-item>
      <el-descriptions-item label="店铺昵称">{{ orderDetail.shopName }}</el-descriptions-item>
    </el-descriptions>
    <template #footer>
      <el-button type="warning" @click="dialogVisible = false">关闭</el-button>
    </template>
  </el-dialog>

  <el-pagination
    v-show="tableState.total > 0"
    v-model:current-page="tableState.pageNum"
    v-model:page-size="tableState.pageSize"
    :total="tableState.total"
    :page-sizes="[10, 20, 50, 100]"
    layout="total,sizes,prev,pager,next,jumper"
    @current-change="handlePage"
    @size-change="
      () => {
        tableState.pageNum = 1
        handlePage()
      }
    "
  />
</template>

<script setup>
import { ref, onMounted } from 'vue'
import {
  pageAccessoryOrdersApi,
  acceptAccessoryOrdersApi,
  deliveryAccessoryOrdersApi,
  cancelAccessoryOrdersApi,
  refundAccessoryOrdersApi,
} from '@/api/manager/accessoryOrders'
import { useAuthStore } from '@/stores'
import { ElMessage, ElMessageBox } from 'element-plus'
import { codeToText } from 'element-china-area-data'

const authStore = useAuthStore()

const pageLoading = ref(false)
const acceptLoading = ref(false)
const deliveryLoading = ref(false)
const cancelLoading = ref(false)
const refundLoading = ref(false)

const tableState = ref({
  searchForm: {},
  pageNum: 1,
  pageSize: 10,
  total: 0,
  dataList: [],
})

const orderDetail = ref({})
const dialogVisible = ref(false)

const acceptOrder = async (orderNo) => {
  if (acceptLoading.value) return
  acceptLoading.value = true
  ElMessageBox.confirm('是否接单？', '提醒', {
    confirmButtonText: '是',
    cancelButtonText: '否',
    type: 'warning',
  })
    .then(async () => {
      try {
        const res = await acceptAccessoryOrdersApi(orderNo)
        if (res.code === 200) {
          handlePage()
        }
      } finally {
        acceptLoading.value = false
      }
    })
    .catch(() => {
      acceptLoading.value = false
    })
}

const deliveryOrder = async (orderNo) => {
  if (deliveryLoading.value) return
  deliveryLoading.value = true
  ElMessageBox.confirm('是否送达？', '提醒', {
    confirmButtonText: '是',
    cancelButtonText: '否',
    type: 'warning',
  })
    .then(async () => {
      try {
        const res = await deliveryAccessoryOrdersApi(orderNo)
        if (res.code === 200) {
          handlePage()
        }
      } finally {
        deliveryLoading.value = false
      }
    })
    .catch(() => {
      deliveryLoading.value = false
    })
}

const cancelOrder = async (orderNo) => {
  if (cancelLoading.value) return
  cancelLoading.value = true
  ElMessageBox.confirm('是否要取消该订单吗？', '提醒', {
    confirmButtonText: '是',
    cancelButtonText: '否',
    type: 'warning',
  })
    .then(async () => {
      try {
        const res = await cancelAccessoryOrdersApi(orderNo)
        if (res.code === 200) {
          ElMessage.success('订单已取消')
          handlePage()
        }
      } finally {
        cancelLoading.value = false
      }
    })
    .catch(() => {
      cancelLoading.value = false
    })
}

const refundOrder = async (orderNo) => {
  if (refundLoading.value) return
  refundLoading.value = true
  ElMessageBox.confirm('是否要进行售后退款操作？', '提醒', {
    confirmButtonText: '是',
    cancelButtonText: '否',
    type: 'warning',
  })
    .then(async () => {
      try {
        const res = await refundAccessoryOrdersApi(orderNo)
        if (res.code === 200) {
          ElMessage.success('退款成功')
          handlePage()
        }
      } finally {
        refundLoading.value = false
      }
    })
    .catch(() => {
      refundLoading.value = false
    })
}

const handlePage = async () => {
  if (pageLoading.value) return
  pageLoading.value = true
  try {
    const res = await pageAccessoryOrdersApi(
      tableState.value.searchForm,
      tableState.value.pageNum,
      tableState.value.pageSize,
    )
    if (res.code === 200) {
      tableState.value.dataList = res.data.list
      tableState.value.total = res.data.total
    }
  } finally {
    pageLoading.value = false
  }
}

const handleClear = () => {
  tableState.value.searchForm = {}
  tableState.value.pageNum = 1
  tableState.value.pageSize = 10
  handlePage()
}

onMounted(() => {
  handlePage()
})
</script>

<style scoped>
.price {
  color: var(--danger-color);
  font-size: var(--font-size-lg);
  font-weight: bold;
}
.accessory-img {
  width: 100px;
  height: 100px;
  border-radius: var(--border-radius-lg);
}
</style>
