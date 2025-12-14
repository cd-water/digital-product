<template>
  <el-card shadow="hover">
    <div class="header-part">
      <div class="main-msg">
        <el-image
          :src="profileMsg.avatar"
          :preview-src-list="[profileMsg.avatar]"
          alt="avatar"
          class="avatar"
          fit="cover"
          :preview-teleported="true"
        />
        <div>
          <div class="nickname">{{ profileMsg.nickname }}</div>
          <div class="role">
            <span v-if="profileMsg.role === 0">管理员</span>
            <span v-else-if="profileMsg.role === 1">店铺</span>
            <span v-else-if="profileMsg.role === 2">普通用户</span>
          </div>
        </div>
      </div>
      <el-button icon="Edit" type="success" :loading="editLoading" @click="handleEdit">修改信息</el-button>
    </div>
    <el-descriptions title="我的个人资料" direction="vertical" border :column="4">
      <el-descriptions-item label="账号">{{ profileMsg.username }}</el-descriptions-item>
      <el-descriptions-item label="电话">{{ profileMsg.phone }}</el-descriptions-item>
      <el-descriptions-item label="邮箱">{{ profileMsg.email }}</el-descriptions-item>
      <el-descriptions-item label="注册时间">
        {{ profileMsg.createTime ? profileMsg.createTime.split(' ')[0] : '' }}
      </el-descriptions-item>
    </el-descriptions>
  </el-card>

  <el-dialog
    v-model="dialogState.visible"
    title="编辑个人信息"
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
      <el-form-item prop="avatar" label="头像" style="align-items: center">
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
import { queryAdminApi, editAdminApi } from '@/api/manager/admin'
import { uploadApi } from '@/api/common'
import { useAuthStore } from '@/stores'
import { ElMessage } from 'element-plus'

const authStore = useAuthStore()
const profileMsg = ref({})
const editLoading = ref(false)
const saveLoading = ref(false)

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

const dialogRef = ref(null)
const dialogState = ref({
  visible: false,
  form: {},
  rules: {
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

const handleEdit = async () => {
  if (editLoading.value || dialogState.value.visible) return
  editLoading.value = true
  try {
    dialogRef.value?.resetFields?.()
    const res = await queryAdminApi(authStore.authMsg.id)
    if (res.code === 200) {
      dialogState.value.form = res.data
      dialogState.value.visible = true
    }
  } finally {
    editLoading.value = false
  }
}

const handleSave = async () => {
  if (saveLoading.value || !dialogState.value.visible) return
  saveLoading.value = true
  try {
    await dialogRef.value?.validate?.()
    const res = await editAdminApi(dialogState.value.form)
    if (res.code === 200) {
      ElMessage.success('保存成功')
      authStore.authMsg.avatar = dialogState.value.form.avatar
      authStore.authMsg.nickname = dialogState.value.form.nickname
      getProfile()
      dialogState.value.visible = false
    }
  } catch {
    ElMessage.warning('请检查输入')
  } finally {
    saveLoading.value = false
  }
}

const getProfile = async () => {
  const res = await queryAdminApi(authStore.authMsg.id)
  if (res.code === 200) {
    profileMsg.value = res.data
  }
}

onMounted(() => {
  getProfile()
})
</script>

<style scoped>
.header-part {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: var(--spacing-md);
  margin: var(--spacing-md) 0;
  background: linear-gradient(135deg, #4fc3f7 0%, #1976d2 100%);
  border-radius: var(--border-radius-lg);
}
.main-msg {
  display: flex;
  align-items: center;
  gap: var(--spacing-lg);
}
.avatar {
  width: 80px;
  height: 80px;
  border-radius: var(--border-radius-xl);
  transition: box-shadow 0.3s;
  box-shadow: 0 0 0 0 rgba(255, 99, 132, 0.6);
}
.avatar:hover {
  box-shadow:
    0 0 16px 4px #ffb3c6,
    0 0 32px 8px #ff6384;
  border: 2px solid #ffb3c6;
}
.nickname {
  font-size: var(--font-size-xl);
  color: var(--text-white);
}
.role {
  background: var(--warning-color);
  padding: var(--spacing-xs);
  border-radius: var(--border-radius-md);
  max-width: 70px;
}
.price {
  color: var(--danger-color);
  font-size: var(--font-size-lg);
  font-weight: bold;
}
.plus-icon {
  font-size: 50px;
  width: 80px;
  height: 80px;
  border: 2px dashed var(--info-color);
  border-radius: var(--border-radius-xl);
}
</style>
