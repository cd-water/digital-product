<template>
  <el-form :inline="true" :model="searchForm">
    <el-form-item>
      <el-input v-model="searchForm.nickname" placeholder="请输入店铺昵称" clearable @keyup.enter="handleSearch" />
    </el-form-item>
    <el-form-item>
      <el-cascader
        v-model="regionCheck"
        :props="{ checkStrictly: true }"
        :options="regionData"
        placeholder="请选择省市区"
        clearable
        @change="handleRegionChange"
        @clear="handleRegionClear"
      />
    </el-form-item>
    <el-form-item>
      <el-button icon="Search" type="primary" :loading="searchLoading" @click="handleSearch">搜索</el-button>
      <el-button icon="Refresh" type="warning" @click="handleClear">重置</el-button>
    </el-form-item>
  </el-form>

  <el-row :gutter="24">
    <el-col v-for="item in shopList" :key="item.id" :span="6" class="show-item">
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
    </el-col>
  </el-row>

  <el-pagination
    v-show="total > 0"
    v-model:current-page="pageNum"
    v-model:page-size="pageSize"
    :total="total"
    :page-sizes="[12, 24, 48, 96]"
    layout="total,sizes,prev,pager,next,jumper"
    @current-change="handleSearch"
    @size-change="
      () => {
        pageNum = 1
        handleSearch()
      }
    "
  />

  <el-empty v-if="total === 0" description="暂无店铺信息" />
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { regionData, codeToText } from 'element-china-area-data'
import { pageDigitalShopApi } from '@/api/customer/digitalShop'

const searchForm = ref({})
const shopList = ref([])
const searchLoading = ref(false)
const pageNum = ref(1)
const pageSize = ref(12)
const total = ref(0)

const regionCheck = ref([])

const handleRegionChange = (val) => {
  searchForm.value.provinceCode = val[0]
  searchForm.value.cityCode = val[1]
  searchForm.value.districtCode = val[2]
  handleSearch()
}
const handleRegionClear = () => {
  searchForm.value.provinceCode = ''
  searchForm.value.cityCode = ''
  searchForm.value.districtCode = ''
  handleSearch()
}

const handleSearch = async () => {
  if (searchLoading.value) return
  searchLoading.value = true
  try {
    const res = await pageDigitalShopApi(searchForm.value, pageNum.value, pageSize.value)
    if (res.code === 200) {
      shopList.value = res.data.list
      total.value = res.data.total
    }
  } finally {
    searchLoading.value = false
  }
}

const handleClear = () => {
  searchForm.value = {}
  regionCheck.value = ['', '', '']
  pageNum.value = 1
  pageSize.value = 12
  handleSearch()
}

onMounted(() => {
  handleSearch()
})
</script>

<style scoped>
.two-side {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.img-shop {
  width: 60px;
  border-radius: var(--border-radius-xl);
  margin: 0 var(--spacing-sm);
}
.shop-name {
  color: var(--primary-color);
  font-size: var(--font-size-xl);
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
</style>
