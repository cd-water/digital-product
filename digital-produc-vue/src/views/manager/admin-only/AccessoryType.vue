<template>
  <el-form :inline="true" :model="tableState.searchForm" @submit.prevent>
    <el-form-item>
      <el-input v-model="tableState.searchForm.name" placeholder="请输入类型名称" clearable @keyup.enter="handlePage" />
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
      <el-empty description="暂无数码配件类型信息" />
    </template>
    <el-table-column type="selection" align="center" />
    <el-table-column label="序号" type="index" align="center" width="50" />
    <el-table-column label="类型名称" prop="name" align="center" show-overflow-tooltip width="850" />
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
    width="400px"
    align-center
    :show-close="false"
  >
    <el-form ref="dialogRef" :model="dialogState.form" :rules="dialogState.rules" label-width="80px">
      <el-form-item prop="name" label="类型名称">
        <el-input v-model="dialogState.form.name" placeholder="请输入类型名称" />
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
import { ref, onMounted } from 'vue'
import {
  addAccessoryTypeApi,
  removeAccessoryTypeApi,
  removeBatchAccessoryTypeApi,
  editAccessoryTypeApi,
  queryAccessoryTypeApi,
  pageAccessoryTypeApi,
} from '@/api/manager/accessoryType'
import { ElMessageBox, ElMessage } from 'element-plus'

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

const nameRules = [
  {
    required: true,
    message: '请输入类型名称',
    trigger: ['blur', 'change'],
  },
  {
    min: 2,
    max: 20,
    message: '类型名称长度需为2-20个字符',
    trigger: ['blur', 'change'],
  },
]

const dialogState = ref({
  visible: false,
  form: {},
  title: '',
  rules: {
    name: nameRules,
  },
})

const handlePage = async () => {
  if (pageLoading.value) return
  pageLoading.value = true
  try {
    const res = await pageAccessoryTypeApi(
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

const handleAdd = () => {
  dialogState.value.title = '新增数码配件类型'
  dialogRef.value?.resetFields?.()
  dialogState.value.form = {}
  dialogState.value.visible = true
}

const handleEdit = async (row) => {
  if (editLoading.value || dialogState.value.visible) return
  editLoading.value = true
  try {
    dialogState.value.title = '编辑数码配件类型'
    dialogRef.value?.resetFields?.()
    const res = await queryAccessoryTypeApi(row.id)
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
        const res = await removeAccessoryTypeApi(id)
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
        const res = await removeBatchAccessoryTypeApi(tableState.value.ids)
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
      res = await editAccessoryTypeApi(dialogState.value.form)
    } else {
      res = await addAccessoryTypeApi(dialogState.value.form)
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
})
</script>

<style scoped></style>
