<template>
  <el-form :inline="true" :model="tableState.searchForm" @submit.prevent>
    <el-form-item>
      <el-input v-model="tableState.searchForm.nickname" placeholder="请输入昵称" clearable @keyup.enter="handlePage" />
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
      <el-empty description="暂无管理员信息" />
    </template>
    <el-table-column type="selection" align="center" />
    <el-table-column label="序号" type="index" align="center" width="50" />
    <el-table-column label="昵称" prop="nickname" align="center" show-overflow-tooltip width="160" />
    <el-table-column label="头像" prop="avatar" align="center" width="80">
      <template #default="{ row }">
        <el-image
          :src="row.avatar"
          :preview-src-list="[row.avatar]"
          fit="cover"
          :preview-teleported="true"
          style="border-radius: var(--border-radius-xl)"
        />
      </template>
    </el-table-column>
    <el-table-column label="账号" prop="username" align="center" show-overflow-tooltip width="180" />
    <el-table-column label="角色" prop="role" align="center" width="80">
      <template #default="{ row }">
        <span v-if="row.role === 0">管理员</span>
        <span v-else-if="row.role === 1">店铺</span>
        <span v-else-if="row.role === 2">用户</span>
      </template>
    </el-table-column>
    <el-table-column label="电话" prop="phone" align="center" width="150" />
    <el-table-column label="邮箱" prop="email" align="center" show-overflow-tooltip width="200" />
    <el-table-column label="注册时间" prop="createTime" align="center" sortable width="180" />
    <el-table-column label="修改时间" prop="updateTime" align="center" sortable width="180" />
    <el-table-column label="操作" align="center" fixed="right" min-width="200">
      <template #default="{ row }">
        <el-button
          icon="Edit"
          :disabled="row.role === 0 && authStore.authMsg.id === row.id"
          type="primary"
          :loading="editLoading"
          @click="handleEdit(row)"
        >
          编辑
        </el-button>
        <el-button
          icon="Delete"
          :disabled="row.role === 0 && authStore.authMsg.id === row.id"
          type="danger"
          :loading="removeLoading"
          @click="handleRemove(row.id)"
        >
          删除
        </el-button>
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
      <el-form-item prop="username" label="账号">
        <el-input v-model="dialogState.form.username" placeholder="请输入账号" :disabled="dialogState.form.id" />
      </el-form-item>
      <el-form-item prop="nickname" label="昵称">
        <el-input v-model="dialogState.form.nickname" placeholder="请输入昵称" />
      </el-form-item>
      <el-form-item prop="avatar" label="头像" style="align-items: center; height: 80px">
        <el-upload :http-request="uploadFile" :show-file-list="false" :before-upload="beforeAvatarUpload">
          <img v-if="dialogState.form.avatar" :src="dialogState.form.avatar" class="avatar" />
          <el-icon v-else class="plus-icon"><Plus /></el-icon>
        </el-upload>
      </el-form-item>
      <el-form-item prop="phone" label="电话">
        <el-input v-model="dialogState.form.phone" placeholder="请输入电话" />
      </el-form-item>
      <el-form-item prop="email" label="邮箱">
        <el-input v-model="dialogState.form.email" placeholder="请输入邮箱" />
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
  addAdminApi,
  removeAdminApi,
  removeBatchAdminApi,
  editAdminApi,
  queryAdminApi,
  pageAdminApi,
} from '@/api/manager/admin'
import { uploadApi } from '@/api/common'
import { ElMessageBox, ElMessage } from 'element-plus'
import { useAuthStore } from '@/stores'

const authStore = useAuthStore()

const usernameRules = [
  {
    required: true,
    message: '请输入账号',
    trigger: ['blur', 'change'],
  },
  {
    pattern: /^[a-zA-Z0-9_]{5,20}$/,
    message: '账号需由5-20位字母、数字或下划线组成',
    trigger: ['blur', 'change'],
  },
]
const nicknameRules = [
  {
    required: true,
    message: '请输入昵称',
    trigger: ['blur', 'change'],
  },
  {
    min: 2,
    max: 20,
    message: '昵称长度需为2-20个字符',
    trigger: ['blur', 'change'],
  },
]
const avatarRules = [
  {
    required: true,
    message: '请上传头像',
    trigger: ['blur', 'change'],
  },
]
const phoneRules = [
  {
    required: true,
    message: '请输入手机号',
    trigger: ['blur', 'change'],
  },
  {
    pattern: /^1[3-9]\d{9}$/,
    message: '请输入有效的手机号',
    trigger: ['blur', 'change'],
  },
]
const emailRules = [
  {
    required: true,
    message: '请输入邮箱',
    trigger: ['blur', 'change'],
  },
  {
    type: 'email',
    message: '请输入有效的邮箱地址',
    trigger: ['blur', 'change'],
  },
]

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
    username: usernameRules,
    nickname: nicknameRules,
    avatar: avatarRules,
    phone: phoneRules,
    email: emailRules,
  },
})

const beforeAvatarUpload = (file) => {
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
    dialogState.value.form.avatar = res.data
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
    const res = await pageAdminApi(tableState.value.searchForm, tableState.value.pageNum, tableState.value.pageSize)
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
  dialogState.value.title = '注册管理员'
  dialogRef.value?.resetFields?.()
  dialogState.value.form = {}
  dialogState.value.visible = true
}

const handleEdit = async (row) => {
  if (editLoading.value || dialogState.value.visible) return
  editLoading.value = true
  try {
    dialogState.value.title = '编辑管理员'
    dialogRef.value?.resetFields?.()
    const res = await queryAdminApi(row.id)
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
        const res = await removeAdminApi(id)
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
        const res = await removeBatchAdminApi(tableState.value.ids)
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
      res = await editAdminApi(dialogState.value.form)
    } else {
      res = await addAdminApi(dialogState.value.form)
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

<style scoped>
.avatar {
  width: 80px;
  height: 80px;
  border-radius: var(--border-radius-xl);
}
.plus-icon {
  font-size: 50px;
  width: 80px;
  height: 80px;
  border: 2px dashed var(--info-color);
  border-radius: var(--border-radius-xl);
}
</style>
