<template>
  <el-form :inline="true" :model="tableState.searchForm" @submit.prevent>
    <el-form-item>
      <el-input
        v-model="tableState.searchForm.name"
        placeholder="请输入配件名称"
        clearable
        style="width: 180px"
        @keyup.enter="handlePage"
      />
    </el-form-item>
    <el-form-item v-if="authStore.authMsg.role === 0">
      <el-input
        v-model="tableState.searchForm.shopName"
        placeholder="请输入店铺昵称"
        clearable
        style="width: 180px"
        @keyup.enter="handlePage"
      />
    </el-form-item>
    <el-form-item>
      <el-select
        v-model="tableState.searchForm.typeId"
        placeholder="请选择配件类型"
        clearable
        style="width: 150px"
        @change="handlePage"
      >
        <el-option v-for="item in accessoryTypeList" :key="item.id" :label="item.name" :value="item.id" />
      </el-select>
    </el-form-item>
    <el-form-item>
      <el-select
        v-model="tableState.searchForm.saleStatus"
        placeholder="请选择售卖状态"
        clearable
        style="width: 150px"
        @change="handlePage"
      >
        <el-option label="已售罄" :value="0" />
        <el-option label="售卖中" :value="1" />
        <el-option label="未上架" :value="2" />
      </el-select>
    </el-form-item>
    <el-form-item>
      <el-button icon="Search" type="primary" :loading="pageLoading" @click="handlePage">查询</el-button>
      <el-button icon="Refresh" type="warning" @click="handleClear">重置</el-button>
    </el-form-item>
  </el-form>

  <div v-if="authStore.authMsg.role === 1">
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
      <el-empty description="暂无数码配件信息" />
    </template>
    <el-table-column v-if="authStore.authMsg.role === 1" type="selection" align="center" />
    <el-table-column label="序号" type="index" align="center" width="50" />
    <el-table-column label="图片" prop="img" align="center" width="80">
      <template #default="{ row }">
        <el-image
          :src="row.img"
          :preview-src-list="[row.img]"
          fit="cover"
          :preview-teleported="true"
          style="border-radius: var(--border-radius-lg)"
        />
      </template>
    </el-table-column>
    <el-table-column label="名称" prop="name" align="center" show-overflow-tooltip width="160" />
    <el-table-column label="价格" prop="price" align="center" width="150">
      <template #default="{ row }">
        <span class="price"> ￥{{ row.price }} </span>
      </template>
    </el-table-column>
    <el-table-column label="剩余数量" prop="store" align="center" width="80" />
    <el-table-column label="简介" prop="introduce" align="center" show-overflow-tooltip width="200" />
    <el-table-column label="销量" prop="saleVolume" align="center" width="80" />
    <el-table-column label="售卖状态" prop="saleStatus" align="center" width="80">
      <template #default="{ row }">
        <el-tag v-if="row.saleStatus === 0" type="info" effect="dark">已售罄</el-tag>
        <el-tag v-else-if="row.saleStatus === 1" type="danger" effect="dark">售卖中</el-tag>
        <el-tag v-else-if="row.saleStatus === 2" type="warning" effect="dark">未上架</el-tag>
      </template>
    </el-table-column>
    <el-table-column
      v-if="authStore.authMsg.role === 0"
      label="所属店铺"
      prop="shopName"
      align="center"
      show-overflow-tooltip
      width="150"
    />
    <el-table-column label="所属分类" prop="typeName" align="center" show-overflow-tooltip width="100" />
    <el-table-column v-if="authStore.authMsg.role === 1" label="操作" align="center" fixed="right" min-width="200">
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
    width="700px"
    align-center
    :show-close="false"
  >
    <el-form ref="dialogRef" :model="dialogState.form" :rules="dialogState.rules" label-width="80px">
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item prop="typeId" label="配件类型">
            <el-select v-model="dialogState.form.typeId" placeholder="请选择数码配件类型">
              <el-option v-for="item in accessoryTypeList" :key="item.id" :label="item.name" :value="item.id" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item prop="name" label="配件名称">
            <el-input v-model="dialogState.form.name" placeholder="请输入数码配件名称" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item prop="price" label="配件价格">
            <el-input
              v-model="dialogState.form.price"
              placeholder="请输入数码配件价格"
              type="number"
              :step="0.01"
              min="0"
            >
              <template #prefix>￥</template>
            </el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item prop="store" label="剩余数量">
            <el-input-number
              v-model="dialogState.form.store"
              placeholder="请输入剩余数量"
              :precision="0"
              :min="0"
              style="width: 100%"
            />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item prop="saleStatus" label="售卖状态">
            <el-radio-group v-model="dialogState.form.saleStatus">
              <el-radio-button :label="0" :value="0">已售罄</el-radio-button>
              <el-radio-button :label="1" :value="1">售卖中</el-radio-button>
              <el-radio-button :label="2" :value="2">未上架</el-radio-button>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="销量">
            <span>{{ dialogState.form.saleVolume }}</span>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item prop="img" label="配件图片" style="align-items: center; height: 100px">
            <el-upload :http-request="uploadFile" :show-file-list="false" :before-upload="beforeUpload">
              <img v-if="dialogState.form.img" :src="dialogState.form.img" class="accessory-img" />
              <el-icon v-else class="plus-icon"><Plus /></el-icon>
            </el-upload>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="24">
          <el-form-item prop="introduce" label="配件简介">
            <el-input
              v-model="dialogState.form.introduce"
              type="textarea"
              :rows="6"
              placeholder="请输入数码配件简介"
              maxlength="250"
              show-word-limit
            />
          </el-form-item>
        </el-col>
      </el-row>
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
import { ref, onMounted } from 'vue'
import {
  pageAccessoryApi,
  addAccessoryApi,
  removeAccessoryApi,
  removeBatchAccessoryApi,
  editAccessoryApi,
  queryAccessoryApi,
} from '@/api/manager/accessory'
import { listAccessoryTypeApi } from '@/api/manager/accessoryType'
import { uploadApi } from '@/api/common'
import { ElMessage, ElMessageBox } from 'element-plus'
import { useAuthStore } from '@/stores'

const authStore = useAuthStore()
const accessoryTypeList = ref([])
const pageLoading = ref(false)
const removeLoading = ref(false)
const editLoading = ref(false)
const saveLoading = ref(false)

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
    typeId: [{ required: true, message: '数码配件类型不能为空', trigger: 'change' }],
    name: [
      { required: true, message: '数码配件名称不能为空', trigger: ['blur', 'change'] },
      { min: 2, max: 20, message: '数码配件名称长度需为2-20个字', trigger: ['blur', 'change'] },
    ],
    img: [{ required: true, message: '请上传数码配件图片', trigger: 'change' }],
    price: [{ required: true, message: '数码配件价格不能为空', trigger: 'blur' }],
    store: [{ required: true, message: '剩余数量不能为空', trigger: 'blur' }],
    introduce: [{ required: true, message: '数码配件简介不能为空', trigger: ['blur', 'change'] }],
    saleStatus: [{ required: true, message: '售卖状态不能为空', trigger: 'change' }],
  },
})

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

const getAccessoryTypeList = async () => {
  const res = await listAccessoryTypeApi()
  if (res.code === 200) {
    accessoryTypeList.value = res.data
  }
}

const handlePage = async () => {
  if (pageLoading.value) return
  pageLoading.value = true
  try {
    const res = await pageAccessoryApi(tableState.value.searchForm, tableState.value.pageNum, tableState.value.pageSize)
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

const handleAdd = async () => {
  dialogState.value.title = '新增数码配件'
  dialogRef.value?.resetFields?.()
  dialogState.value.form = {}
  dialogState.value.visible = true
}

const handleEdit = async (row) => {
  if (editLoading.value || dialogState.value.visible) return
  editLoading.value = true
  try {
    dialogState.value.title = '编辑数码配件'
    dialogRef.value?.resetFields?.()
    const res = await queryAccessoryApi(row.id)
    if (res.code === 200) {
      dialogState.value.form = res.data
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
        const res = await removeAccessoryApi(id)
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
        const res = await removeBatchAccessoryApi(tableState.value.ids)
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
      res = await editAccessoryApi(dialogState.value.form)
    } else {
      res = await addAccessoryApi(dialogState.value.form)
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
  getAccessoryTypeList()
  handlePage()
})
</script>

<style scoped>
.accessory-img {
  width: 100px;
  height: 100px;
  border-radius: var(--border-radius-lg);
}
.price {
  color: var(--danger-color);
  font-size: var(--font-size-lg);
  font-weight: bold;
}
.plus-icon {
  font-size: 50px;
  width: 100px;
  height: 100px;
  border: 2px dashed var(--info-color);
  border-radius: var(--border-radius-lg);
}
</style>
