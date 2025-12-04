<template>
  <el-form :inline="true" :model="tableState.searchForm" @submit.prevent>
    <el-form-item>
      <el-input
        v-model="tableState.searchForm.productName"
        placeholder="请输入数码产品名称"
        clearable
        @keyup.enter="handlePage"
      />
    </el-form-item>
    <el-form-item>
      <el-input
        v-model="tableState.searchForm.shopName"
        placeholder="请输入店铺昵称"
        clearable
        @keyup.enter="handlePage"
      />
    </el-form-item>
    <el-form-item>
      <el-button icon="Search" type="primary" :loading="pageLoading" @click="handlePage">查询</el-button>
      <el-button icon="Refresh" type="warning" @click="handleClear">重置</el-button>
    </el-form-item>
  </el-form>

  <div>
    <el-button icon="Plus" type="success" @click="handleAdd">新增</el-button>
    <el-button icon="Delete" type="danger" :loading="removeLoading" @click="handleRemoveBatch">批量删除</el-button>
  </div>

  <el-table
    v-loading="pageLoading"
    :data="tableState.dataList"
    height="595"
    @selection-change="(rows) => (tableState.ids = rows.map((v) => v.id))"
  >
    <template #empty>
      <el-empty description="暂无轮播图信息" />
    </template>
    <el-table-column type="selection" align="center" />
    <el-table-column label="序号" type="index" align="center" width="50" />
    <el-table-column label="数码产品名称" prop="productName" align="center" show-overflow-tooltip width="250" />
    <el-table-column label="店铺昵称" prop="shopName" align="center" show-overflow-tooltip width="250" />
    <el-table-column label="展示图片" prop="img" align="center" width="330">
      <template #default="{ row }">
        <el-image
          :src="row.img"
          :preview-src-list="[row.img]"
          :preview-teleported="true"
          style="border-radius: var(--border-radius-lg); height: 169px; width: 300px"
        />
      </template>
    </el-table-column>
    <el-table-column label="操作" align="center" fixed="right" min-width="200">
      <template #default="{ row }">
        <el-button icon="Edit" type="primary" :loading="editLoading" @click="handleEdit(row)">编辑</el-button>
        <el-button icon="Delete" type="danger" :loading="removeLoading" @click="handleRemove(row.id)">删除</el-button>
      </template>
    </el-table-column>
  </el-table>

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

  <el-dialog
    v-model="dialogState.visible"
    :title="dialogState.title"
    :close-on-click-modal="false"
    width="500px"
    align-center
    :show-close="false"
  >
    <el-form ref="dialogRef" :model="dialogState.form" :rules="dialogState.rules" label-width="80px">
      <el-form-item prop="productId" label="关联产品">
        <el-cascader
          v-model="shopAndProduct"
          placeholder="选择关联数码产品"
          :options="cascaderOptions"
          filterable
          clearable
          @change="handleShopChange"
        />
      </el-form-item>
      <el-form-item prop="img" label="轮播图" style="align-items: center; height: 197px">
        <el-upload :http-request="uploadFile" :show-file-list="false" :before-upload="beforeUpload">
          <img v-if="dialogState.form.img" :src="dialogState.form.img" class="slideshow-img" />
          <el-icon v-else class="plus-icon"><Plus /></el-icon>
        </el-upload>
      </el-form-item>
    </el-form>
    <template #footer>
      <div>
        <el-button type="warning" @click="dialogState.visible = false">取消</el-button>
        <el-button type="primary" :loading="saveLoading" @click="handleSave">确定</el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import {
  addSlideshowApi,
  removeSlideshowApi,
  removeBatchSlideshowApi,
  editSlideshowApi,
  querySlideshowApi,
  pageSlideshowApi,
} from '@/api/manager/slideshow'
import { uploadApi } from '@/api/common'
import { groupProductApi } from '@/api/manager/product'
import { ElMessage, ElMessageBox } from 'element-plus'

const shopProductOptions = ref([])
const shopAndProduct = ref([])

const cascaderOptions = computed(() => {
  return shopProductOptions.value.map((shop) => ({
    value: shop.shopId,
    label: shop.shopName,
    children: (shop.productList || []).map((product) => ({
      value: product.productId,
      label: product.productName,
    })),
  }))
})

const handleShopChange = (val) => {
  dialogState.value.form.shopId = val[0]
  dialogState.value.form.productId = val[1]
}

const getGroupProduct = async () => {
  const res = await groupProductApi()
  if (res.code === 200) {
    shopProductOptions.value = res.data
  }
}

const tableState = ref({
  searchForm: {},
  pageNum: 1,
  pageSize: 10,
  total: 0,
  dataList: [],
  ids: [],
})

const dialogRef = ref(null)
const dialogState = ref({
  visible: false,
  form: {},
  title: '',
  rules: {
    productId: [
      {
        required: true,
        message: '请选择关联数码产品',
        trigger: ['blur', 'change'],
      },
    ],
    img: [
      {
        required: true,
        message: '请上传展示图片',
        trigger: ['blur', 'change'],
      },
    ],
  },
})

const pageLoading = ref(false)
const saveLoading = ref(false)
const editLoading = ref(false)
const removeLoading = ref(false)

const beforeUpload = (file) => {
  const isAllowedType =
    file.type === 'image/jpeg' || file.type === 'image/png' || file.type === 'image/webp' || file.type === 'image/gif'
  const isLt2M = file.size / 1024 / 1024 < 2
  if (!isAllowedType) {
    ElMessage.error('只允许上传jpeg、png、webp、gif格式的图片')
    return false
  }
  if (!isLt2M) {
    ElMessage.error('图片大小不能超过2MB')
    return false
  }
  return true
}

const uploadFile = async (options) => {
  const { file, onSuccess, onError } = options
  const res = await uploadApi(file)
  if (res.code === 200) {
    ElMessage.success('上传成功')
    dialogState.value.form.img = res.data
    onSuccess(res)
  } else {
    ElMessage.error('上传失败')
    onError(res)
  }
}

const handlePage = async () => {
  if (pageLoading.value) return
  pageLoading.value = true
  try {
    const res = await pageSlideshowApi(tableState.value.searchForm, tableState.value.pageNum, tableState.value.pageSize)
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

const handleAdd = () => {
  dialogState.value.title = '新增轮播图'
  dialogRef.value?.resetFields?.()
  dialogState.value.form = {}
  shopAndProduct.value = []
  dialogState.value.visible = true
}

const handleEdit = async (row) => {
  if (editLoading.value || dialogState.value.visible) return
  editLoading.value = true
  try {
    dialogState.value.title = '编辑轮播图'
    dialogRef.value?.resetFields?.()
    const res = await querySlideshowApi(row.id)
    if (res.code === 200) {
      dialogState.value.form = res.data
      shopAndProduct.value = [res.data.shopId, res.data.productId]
      dialogState.value.visible = true
    }
  } finally {
    editLoading.value = false
  }
}

const handleRemove = (id) => {
  if (removeLoading.value) return
  removeLoading.value = true
  ElMessageBox.confirm('删除操作不可逆，是否继续？', 'Warning', {
    confirmButtonText: '是',
    cancelButtonText: '否',
    type: 'warning',
  })
    .then(async () => {
      try {
        const res = await removeSlideshowApi(id)
        if (res.code === 200) {
          ElMessage.success('删除成功')
          handlePage()
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

const handleRemoveBatch = () => {
  if (removeLoading.value) return
  removeLoading.value = true
  if (!tableState.value.ids.length) {
    ElMessage.warning('请选择数据')
    removeLoading.value = false
    return
  }
  ElMessageBox.confirm('删除操作不可逆，是否继续？', 'Warning', {
    confirmButtonText: '是',
    cancelButtonText: '否',
    type: 'warning',
  })
    .then(async () => {
      try {
        const res = await removeBatchSlideshowApi(tableState.value.ids)
        if (res.code === 200) {
          ElMessage.success('删除成功')
          handlePage()
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

const handleSave = async () => {
  if (saveLoading.value || !dialogState.value.visible) return
  saveLoading.value = true
  try {
    await dialogRef.value?.validate?.()
    let res
    if (dialogState.value.form.id) {
      res = await editSlideshowApi(dialogState.value.form)
    } else {
      res = await addSlideshowApi(dialogState.value.form)
    }
    if (res.code === 200) {
      ElMessage.success('保存成功')
      dialogState.value.visible = false
      handlePage()
    }
  } catch {
    ElMessage.warning('请检查输入')
  } finally {
    saveLoading.value = false
  }
}

onMounted(() => {
  handlePage()
  getGroupProduct()
})
</script>

<style scoped>
.slideshow-img {
  width: 350px;
  height: 197px;
  border-radius: var(--border-radius-lg);
}
.plus-icon {
  font-size: 50px;
  width: 350px;
  height: 197px;
  border: 2px dashed var(--info-color);
  border-radius: var(--border-radius-lg);
}
</style>
