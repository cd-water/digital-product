<template>
  <el-form :inline="true" :model="searchForm">
    <el-form-item>
      <el-select
        v-model="searchForm.typeId"
        placeholder="请选择数码产品类型"
        clearable
        style="width: 180px"
        :value-key="'id'"
        @change="handleSearch"
      >
        <el-option v-for="item in productTypeList" :key="item.id" :label="item.name" :value="item.id" />
      </el-select>
    </el-form-item>
    <el-form-item>
      <el-input v-model="searchForm.name" placeholder="请输入数码产品名称" clearable @keyup.enter="handleSearch" />
    </el-form-item>
    <el-form-item>
      <el-switch v-model="searchForm.onlyInStock" active-text="仅看有货" inactive-text="全部" @change="handleSearch" />
    </el-form-item>
    <el-form-item label="价格区间">
      <el-slider
        v-model="priceRange"
        range
        :min="0"
        :max="10000"
        style="width: 250px; margin: 0 var(--spacing-md)"
        show-stops
        :marks="{
          0: '0',
          2000: '2k',
          5000: '5k',
          8000: '8k',
          10000: '10k',
        }"
        @change="handlePriceChange"
        @keyup.enter="handleSearch"
      />
    </el-form-item>
    <el-form-item>
      <el-button icon="Search" type="primary" :loading="searchLoading" @click="handleSearch">搜索</el-button>
      <el-button icon="Refresh" type="warning" @click="handleClear">重置</el-button>
    </el-form-item>
  </el-form>

  <el-row
    v-show="total > 0"
    v-infinite-scroll="handleLoad"
    :gutter="24"
    class="show-area"
    :infinite-scroll-disabled="searchLoading || noMore"
    :infinite-scroll-distance="100"
  >
    <el-col v-for="item in productList" :key="item.id" :span="4.8" class="show-item">
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
  <el-empty v-if="total === 0" description="暂无数码产品信息" />
  <el-backtop :right="30" :bottom="30" target=".show-area" />
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { allProductTypeApi } from '@/api/customer/productType'
import { pageProductApi } from '@/api/customer/product'
import { useRoute, useRouter } from 'vue-router'

const route = useRoute()
const router = useRouter()
const searchForm = ref({})
const productTypeList = ref([])
const productList = ref([])
const searchLoading = ref(false)
const noMore = ref(false)
const pageNum = ref(1)
const pageSize = ref(25)
const total = ref(0)

const priceRange = ref([0, 10000])

const getAllProductType = async () => {
  const res = await allProductTypeApi()
  if (res.code === 200) {
    productTypeList.value = res.data
  }
}

const handleSearch = async () => {
  if (searchLoading.value) return
  searchLoading.value = true
  try {
    pageNum.value = 1
    noMore.value = false
    const newQuery = { ...route.query }
    if (searchForm.value.typeId) {
      newQuery.typeId = String(searchForm.value.typeId)
    } else {
      delete newQuery.typeId
    }
    router.replace({ path: route.path, query: newQuery })
    const res = await pageProductApi(searchForm.value, pageNum.value, pageSize.value)
    if (res.code === 200) {
      productList.value = res.data.list
      total.value = res.data.total
      if (productList.value.length >= total.value) {
        noMore.value = true
      }
    }
  } finally {
    searchLoading.value = false
  }
}

const handlePriceChange = (val) => {
  searchForm.value.priceMin = val[0]
  searchForm.value.priceMax = val[1]
}

const handleLoad = async () => {
  if (searchLoading.value || noMore.value) return
  searchLoading.value = true
  try {
    pageNum.value += 1
    const res = await pageProductApi(searchForm.value, pageNum.value, pageSize.value)
    if (res.code === 200) {
      const loadList = res.data.list
      total.value = res.data.total
      if (loadList.length > 0) {
        productList.value = productList.value.concat(loadList)
        if (productList.value.length >= total.value) {
          noMore.value = true
        }
      } else {
        noMore.value = true
      }
    }
  } finally {
    searchLoading.value = false
  }
}

const handleClear = () => {
  searchForm.value = {}
  priceRange.value = [0, 10000]
  pageNum.value = 1
  pageSize.value = 25
  const newQuery = { ...route.query }
  delete newQuery.typeId
  router.replace({ path: route.path, query: newQuery })
  handleSearch()
}

onMounted(() => {
  getAllProductType()
  const typeId = route.query.typeId
  if (typeId) {
    searchForm.value.typeId = Number(typeId)
  }
  handleSearch()
})
</script>

<style scoped>
.show-area {
  height: 680px;
  overflow-y: auto;
  align-items: center;
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
</style>
