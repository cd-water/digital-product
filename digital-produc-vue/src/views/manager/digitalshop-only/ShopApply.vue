<template>
  <div class="bg">
    <el-card shadow="hover" class="apply-card">
      <template #header>
        <div class="header-part">认证申请</div>
      </template>
      <el-form ref="auditFormRef" :model="auditForm" :rules="auditRules" label-width="100px">
        <el-form-item prop="principalName" label="负责人姓名">
          <el-input v-model="auditForm.principalName" placeholder="请输入负责人姓名" />
        </el-form-item>
        <el-form-item prop="principalNo" label="身份证号">
          <el-input v-model="auditForm.principalNo" placeholder="请输入负责人身份证号" />
        </el-form-item>
        <el-form-item prop="practiceLicense" label="营业执照" style="align-items: center; height: 197px">
          <el-upload :http-request="uploadFile" :show-file-list="false" :before-upload="beforeUpload">
            <img v-if="auditForm.practiceLicense" :src="auditForm.practiceLicense" class="practiceLicense" />
            <el-icon v-else class="plus-icon"><Plus /></el-icon>
          </el-upload>
        </el-form-item>
        <div class="two-side">
          <el-form-item prop="auditStatus" label="审核状态">
            <el-tag v-if="auditForm.auditStatus === 0" type="warning" effect="dark" class="tag">待提交</el-tag>
            <el-tag v-else-if="auditForm.auditStatus === 1" type="primary" effect="dark" class="tag">待审核</el-tag>
            <el-tag v-else-if="auditForm.auditStatus === 2" type="success" effect="dark" class="tag">审核通过</el-tag>
            <el-tag v-else-if="auditForm.auditStatus === 3" type="danger" effect="dark" class="tag">审核不通过</el-tag>
          </el-form-item>
          <el-form-item>
            <el-button type="warning" :loading="submitLoading" :disabled="submitLoading" @click="handleSubmit">
              提交申请
            </el-button>
          </el-form-item>
        </div>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { queryDigitalShopApi, editDigitalShopApi } from '@/api/manager/digitalShop'
import { uploadApi } from '@/api/common'
import { useAuthStore } from '@/stores'
import { ElMessage, ElMessageBox } from 'element-plus'

const authStore = useAuthStore()

const auditForm = ref({})
const auditFormRef = ref(null)
const submitLoading = ref(false)

const auditRules = {
  practiceLicense: [
    {
      required: true,
      message: '请上传营业执照',
      trigger: 'blur',
    },
  ],
  principalName: [
    {
      required: true,
      message: '请输入负责人姓名',
      trigger: ['blur', 'change'],
    },
  ],
  principalNo: [
    {
      required: true,
      message: '请输入负责人身份证号',
      trigger: ['blur', 'change'],
    },
    {
      validator: (rule, value, callback) => {
        const id18Reg = /^[1-9]\d{5}(19|20)\d{2}(0[1-9]|1[0-2])(0[1-9]|[12]\d|3[01])\d{3}[\dXx]$/
        if (!id18Reg.test(value)) {
          callback(new Error('请输入18位有效的身份证号'))
        } else {
          callback()
        }
      },
      trigger: ['blur', 'change'],
    },
  ],
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

const uploadFile = async (options) => {
  const { file, onSuccess, onError } = options
  const res = await uploadApi(file)
  if (res.code === 200) {
    ElMessage.success('上传成功')
    auditForm.value.practiceLicense = res.data
    onSuccess(res)
  } else {
    ElMessage.error('上传失败')
    onError(res)
  }
}

const handleSubmit = () => {
  if (submitLoading.value) return
  submitLoading.value = true
  ElMessageBox.confirm('是否确认提交申请？', 'Info', {
    confirmButtonText: '是',
    cancelButtonText: '否',
    type: 'info',
  })
    .then(async () => {
      try {
        await auditFormRef.value?.validate?.()
        auditForm.value.auditStatus = 1
        const res = await editDigitalShopApi(auditForm.value)
        if (res.code === 200) {
          ElMessage.success('保存成功')
          getAuditMsg()
        }
      } catch {
        ElMessage.warning('请检查输入')
      } finally {
        submitLoading.value = false
      }
    })
    .catch(() => {
      submitLoading.value = false
    })
}

const getAuditMsg = async () => {
  const res = await queryDigitalShopApi(authStore.authMsg.id)
  if (res.code === 200) {
    auditForm.value = res.data
  }
}

onMounted(() => {
  getAuditMsg()
})
</script>

<style scoped>
.bg {
  background: url('@/assets/image/bg2.png') center/cover no-repeat;
  height: 760px;
  display: flex;
  align-items: center;
  border-radius: var(--border-radius-md);
}
.apply-card {
  max-width: 480px;
  margin: auto 200px;
  width: 100%;
}
.header-part {
  font-size: var(--font-size-xl);
  color: var(--primary-color);
  display: flex;
  justify-content: center;
  align-items: center;
}
.tag {
  font-size: var(--font-size-lg);
}
.practiceLicense {
  border-radius: var(--border-radius-md);
  width: 350px;
  height: 197px;
}
.two-side {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.plus-icon {
  font-size: 50px;
  width: 350px;
  height: 197px;
  border: 2px dashed var(--info-color);
  border-radius: var(--border-radius-md);
}
</style>
