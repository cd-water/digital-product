<template>
  <el-form :inline="true" :model="tableState.searchForm" @submit.prevent>
    <el-form-item>
      <el-input v-model="tableState.searchForm.nickname" placeholder="请输入昵称" clearable @keyup.enter="handlePage" />
    </el-form-item>
    <el-form-item>
      <el-select
        v-model="tableState.searchForm.auditStatus"
        placeholder="请选择审核状态"
        style="width: 180px"
        clearable
        @change="handlePage"
      >
        <el-option label="待提交" :value="0" />
        <el-option label="待审核" :value="1" />
        <el-option label="已过审" :value="2" />
        <el-option label="未过审" :value="3" />
      </el-select>
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
      <el-empty description="暂无宠物店信息" />
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
    <el-table-column label="介绍" prop="introduce" align="center" show-overflow-tooltip width="200" />
    <el-table-column label="营业执照" prop="practiceLicense" align="center" width="130">
      <template #default="{ row }">
        <el-image
          :src="row.practiceLicense"
          :preview-src-list="[row.practiceLicense]"
          :preview-teleported="true"
          style="border-radius: var(--border-radius-md); height: 56px; width: 100px"
        />
      </template>
    </el-table-column>
    <el-table-column label="负责人姓名" prop="principalName" align="center" show-overflow-tooltip width="100" />
    <el-table-column label="身份证号" prop="principalNo" align="center" width="200" />
    <el-table-column label="审核状态" prop="auditStatus" align="center" width="120">
      <template #default="{ row }">
        <el-tag v-if="row.auditStatus === 0" type="warning" effect="dark" class="tag">待提交</el-tag>
        <el-tag v-else-if="row.auditStatus === 1" type="primary" effect="dark" class="tag">待审核</el-tag>
        <el-tag v-else-if="row.auditStatus === 2" type="success" effect="dark" class="tag">审核通过</el-tag>
        <el-tag v-else-if="row.auditStatus === 3" type="danger" effect="dark" class="tag">审核不通过</el-tag>
      </template>
    </el-table-column>
    <el-table-column label="注册时间" prop="createTime" align="center" sortable width="180" />
    <el-table-column label="修改时间" prop="updateTime" align="center" sortable width="180" />
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
    width="700px"
    align-center
    :show-close="false"
  >
    <el-form ref="dialogRef" :model="dialogState.form" :rules="dialogState.rules" label-width="80px">
      <div class="top-part">
        <div>
          <el-form-item prop="username" label="账号">
            <el-input v-model="dialogState.form.username" placeholder="请输入账号" :disabled="dialogState.form.id" />
          </el-form-item>
          <el-form-item prop="nickname" label="昵称">
            <el-input v-model="dialogState.form.nickname" placeholder="请输入昵称" />
          </el-form-item>
          <el-form-item prop="avatar" label="头像" style="align-items: center; height: 80px">
            <el-upload
              :http-request="(options) => uploadFile(options, 'avatar')"
              :show-file-list="false"
              :before-upload="beforeUpload"
            >
              <img v-if="dialogState.form.avatar" :src="dialogState.form.avatar" class="avatar" />
              <el-icon v-else class="plus-icon-avatar"><Plus /></el-icon>
            </el-upload>
          </el-form-item>
          <el-form-item prop="phone" label="电话">
            <el-input v-model="dialogState.form.phone" placeholder="请输入电话" />
          </el-form-item>
          <el-form-item prop="email" label="邮箱">
            <el-input v-model="dialogState.form.email" placeholder="请输入邮箱" />
          </el-form-item>
          <el-form-item prop="auditStatus" label="审核状态">
            <el-select v-model="dialogState.form.auditStatus" placeholder="请选择审核状态">
              <el-option label="待提交" :value="0" />
              <el-option label="待审核" :value="1" />
              <el-option label="已过审" :value="2" />
              <el-option label="未过审" :value="3" />
            </el-select>
          </el-form-item>
        </div>
        <div>
          <el-form-item label="省市区">
            <el-cascader
              v-model="regionCode"
              :options="regionData"
              placeholder="请选择省市区"
              clearable
              @change="handleRegionChange"
            />
          </el-form-item>
          <el-form-item prop="detailAddress" label="详细地址">
            <el-input
              v-model="dialogState.form.detailAddress"
              placeholder="请输入详细地址"
              maxlength="30"
              show-word-limit
            />
          </el-form-item>
          <el-form-item prop="principalName" label="负责人姓名">
            <el-input v-model="dialogState.form.principalName" placeholder="请输入负责人姓名" />
          </el-form-item>
          <el-form-item prop="principalNo" label="身份证号">
            <el-input v-model="dialogState.form.principalNo" placeholder="请输入负责人身份证号" />
          </el-form-item>
          <el-form-item prop="practiceLicense" label="营业执照" style="align-items: center; height: 140px">
            <el-upload
              :http-request="(options) => uploadFile(options, 'practiceLicense')"
              :show-file-list="false"
              :before-upload="beforeUpload"
            >
              <img
                v-if="dialogState.form.practiceLicense"
                :src="dialogState.form.practiceLicense"
                class="practiceLicense"
              />
              <el-icon v-else class="plus-icon-practiceLicense"><Plus /></el-icon>
            </el-upload>
          </el-form-item>
        </div>
      </div>
      <el-form-item prop="introduce" label="介绍">
        <el-input
          v-model="dialogState.form.introduce"
          type="textarea"
          placeholder="请输入介绍"
          :rows="8"
          maxlength="300"
          show-word-limit
        />
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
import { regionData, codeToText } from 'element-china-area-data'
import {
  addDigitalShopApi,
  removeDigitalShopApi,
  removeBatchDigitalShopApi,
  editDigitalShopApi,
  queryDigitalShopApi,
  pageDigitalShopApi,
} from '@/api/manager/digitalShop'
import { uploadApi } from '@/api/common'
import { ElMessage, ElMessageBox } from 'element-plus'

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
const principalNoRules = [
  {
    required: false,
    validator: (rule, value, callback) => {
      const id18Reg = /^[1-9]\d{5}(19|20)\d{2}(0[1-9]|1[0-2])(0[1-9]|[12]\d|3[01])\d{3}[\dXx]$/
      if (value && !id18Reg.test(value)) {
        callback(new Error('请输入18位有效的中国身份证号'))
      } else {
        callback()
      }
    },
    trigger: ['blur', 'change'],
  },
]
const auditStatusRules = [
  {
    required: true,
    message: '请选择审核状态',
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
    principalNo: principalNoRules,
    auditStatus: auditStatusRules,
  },
})

const regionCode = ref([])
const handleRegionChange = (val) => {
  dialogState.value.form.provinceCode = val[0]
  dialogState.value.form.cityCode = val[1]
  dialogState.value.form.districtCode = val[2]
}

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

const uploadFile = async (options, field) => {
  const { file, onSuccess, onError } = options
  const res = await uploadApi(file)
  if (res.code === 200) {
    ElMessage.success('上传成功')
    if (field === 'avatar') {
      dialogState.value.form.avatar = res.data
    } else if (field === 'practiceLicense') {
      dialogState.value.form.practiceLicense = res.data
    }
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
    const res = await pageDigitalShopApi(
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
  dialogState.value.title = '注册店铺'
  dialogRef.value?.resetFields?.()
  dialogState.value.form = {}
  regionCode.value = []
  dialogState.value.visible = true
}

const handleEdit = async (row) => {
  if (editLoading.value || dialogState.value.visible) return
  editLoading.value = true
  try {
    dialogState.value.title = '编辑店铺'
    dialogRef.value?.resetFields?.()
    const res = await queryDigitalShopApi(row.id)
    if (res.code === 200) {
      dialogState.value.form = res.data
      regionCode.value = [res.data.provinceCode, res.data.cityCode, res.data.districtCode]
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
        const res = await removeDigitalShopApi(id)
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
        const res = await removeBatchDigitalShopApi(tableState.value.ids)
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
      res = await editDigitalShopApi(dialogState.value.form)
    } else {
      res = await addDigitalShopApi(dialogState.value.form)
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
.tag {
  font-size: var(--font-size-md);
}
.avatar {
  width: 80px;
  height: 80px;
  border-radius: var(--border-radius-xl);
}
.plus-icon-avatar {
  font-size: 50px;
  width: 80px;
  height: 80px;
  border: 2px dashed var(--info-color);
  border-radius: var(--border-radius-xl);
}
.practiceLicense {
  border-radius: var(--border-radius-md);
  width: 250px;
  height: 140px;
}
.plus-icon-practiceLicense {
  font-size: 50px;
  width: 250px;
  height: 140px;
  border: 2px dashed var(--info-color);
  border-radius: var(--border-radius-md);
}
.top-part {
  display: flex;
  gap: var(--spacing-xl);
  flex: 1;
}
</style>
