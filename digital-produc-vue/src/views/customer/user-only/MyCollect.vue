<template>
  <el-scrollbar height="760px">
    <el-card shadow="hover" class="cart-card">
      <template #header>
        <div class="two-side">
          <div class="card-title">我的收藏</div>
          <RouterLink to="/customer/visitor-toall/product" class="link goto-collect">
            <span>去收藏更多数码产品</span>
            <el-icon><ArrowRight /></el-icon>
          </RouterLink>
        </div>
      </template>
      <el-row v-show="collectList && collectList.length > 0" :gutter="24">
        <el-col v-for="item in collectList" :key="item.id" :span="4.8" class="show-item">
          <RouterLink :to="`/customer/visitor-toall/productDetail?id=${item.productId}`" class="link">
            <div>
              <img :src="item.productImg" alt="product" class="img-product" />
            </div>
            <div>{{ item.productName }}</div>
            <div class="tag">
              <el-tag v-if="item.productused === 0" type="danger" effect="dark" round>全新</el-tag>
              <el-tag v-else-if="item.productused === 1" type="primary" effect="dark" round>二手</el-tag>
              <el-tag v-if="item.productSaleStatus === 0" type="info" effect="dark">已售罄</el-tag>
              <el-tag v-else-if="item.productSaleStatus === 1" type="danger" effect="dark">售卖中</el-tag>
              <el-tag v-else-if="item.productSaleStatus === 2" type="warning" effect="dark">未上架</el-tag>
              <el-tag v-if="item.productStore > 0" type="success" effect="dark">有货</el-tag>
              <el-tag v-else type="info" effect="dark">缺货</el-tag>
            </div>
            <div class="two-side">
              <div class="price">￥{{ item.productPrice }}</div>
              <el-button
                type="warning"
                size="small"
                icon="Delete"
                :loading="outCollectLoading"
                @click.stop.prevent="outCollect(item.productId)"
              >
                取消收藏
              </el-button>
            </div>
          </RouterLink>
        </el-col>
      </el-row>
      <el-empty v-if="!collectList || collectList.length === 0" description="收藏夹空空如也~" />
    </el-card>
  </el-scrollbar>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { listCollectApi, outCollectApi } from '@/api/customer/collect'
import { useAuthStore } from '@/stores'
import { ElMessage } from 'element-plus'

const authStore = useAuthStore()
const collectList = ref([])
const outCollectLoading = ref(false)

const listCollect = async () => {
  const res = await listCollectApi()
  if (res.code === 200) {
    collectList.value = res.data
  }
}

const outCollect = async (productId) => {
  if (outCollectLoading.value) return
  outCollectLoading.value = true
  try {
    const userId = authStore.authMsg.id
    const res = await outCollectApi(userId, productId)
    if (res.code === 200) {
      ElMessage.success('已取消收藏')
      listCollect()
    }
  } finally {
    outCollectLoading.value = false
  }
}

onMounted(() => {
  listCollect()
})
</script>

<style scoped>
.cart-card {
  border-radius: var(--border-radius-lg);
}
.card-title {
  font-size: var(--font-size-xl);
  font-weight: bold;
}
.show-item {
  margin: var(--spacing-sm);
  padding: var(--spacing-sm) 0;
  border-radius: var(--border-radius-md);
  background: var(--item-color);
  width: 100%;
  max-width: 250px;
}
.show-item:hover {
  box-shadow: inset 0 0 0 2px var(--primary-color);
  border-radius: var(--border-radius-md);
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
.two-side {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.goto-collect {
  display: flex;
  align-items: center;
  gap: var(--spacing-sm);
  font-size: var(--font-size-lg);
}
.goto-collect:hover {
  color: var(--primary-color);
}
</style>
