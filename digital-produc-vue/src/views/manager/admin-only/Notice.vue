<template>
  <el-form :inline="true" :model="tableState.searchForm" @submit.prevent>
    <el-form-item>
      <el-input
        v-model="tableState.searchForm.title"
        placeholder="请输入公告标题"
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
      <el-empty description="暂无公告信息" />
    </template>
    <el-table-column type="selection" align="center" />
    <el-table-column label="序号" type="index" align="center" width="50" />
    <el-table-column label="公告标题" prop="title" align="center" show-overflow-tooltip width="250" />
    <el-table-column label="公告内容" prop="content" align="left" show-overflow-tooltip width="400" />
    <el-table-column label="发布时间" prop="releaseTime" align="center" sortable width="180" />
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
    width="650px"
    align-center
    :show-close="false"
  >
    <el-form ref="dialogRef" :model="dialogState.form" :rules="dialogState.rules" label-width="80px">
      <el-form-item prop="title" label="公告标题">
        <el-input v-model="dialogState.form.title" placeholder="请输入公告标题" maxlength="30" show-word-limit />
      </el-form-item>
      <el-form-item prop="content" label="公告内容">
        <el-input
          v-model="dialogState.form.content"
          placeholder="请输入公告内容"
          type="textarea"
          :rows="8"
          maxlength="300"
          show-word-limit
        />
        <span v-if="dialogState.form.id">发布时间：{{ dialogState.form.releaseTime }}</span>
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
  addNoticeApi,
  removeNoticeApi,
  removeBatchNoticeApi,
  editNoticeApi,
  queryNoticeApi,
  pageNoticeApi,
} from '@/api/manager/notice'
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

const titleRules = [
  {
    required: true,
    message: '请输入公告标题',
    trigger: ['blur', 'change'],
  },
  {
    max: 30,
    message: '公告标题不能超过30字',
    trigger: ['blur', 'change'],
  },
]
const contentRules = [
  {
    required: true,
    message: '请输入公告内容',
    trigger: ['blur', 'change'],
  },
  {
    max: 300,
    message: '公告内容不能超过300字',
    trigger: ['blur', 'change'],
  },
]

const dialogState = ref({
  visible: false,
  form: {},
  title: '',
  rules: {
    title: titleRules,
    content: contentRules,
  },
})

const handlePage = async () => {
  if (pageLoading.value) return
  pageLoading.value = true
  try {
    const res = await pageNoticeApi(tableState.value.searchForm, tableState.value.pageNum, tableState.value.pageSize)
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
  dialogState.value.title = '发布公告'
  dialogRef.value?.resetFields?.()
  dialogState.value.form = {}
  dialogState.value.visible = true
}

const handleEdit = async (row) => {
  if (editLoading.value || dialogState.value.visible) return
  editLoading.value = true
  try {
    dialogState.value.title = '编辑公告'
    dialogRef.value?.resetFields?.()
    const res = await queryNoticeApi(row.id)
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
        const res = await removeNoticeApi(id)
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
        const res = await removeBatchNoticeApi(tableState.value.ids)
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
      res = await editNoticeApi(dialogState.value.form)
    } else {
      res = await addNoticeApi(dialogState.value.form)
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
