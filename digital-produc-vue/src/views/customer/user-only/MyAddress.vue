<template>
  <el-scrollbar height="760px">
    <el-card shadow="hover" class="address-card">
      <template #header>
        <div class="two-side">
          <div class="card-title">收货地址</div>
          <el-button icon="Plus" type="success" @click="handleAdd">新增收货地址</el-button>
        </div>
      </template>
      <template v-if="!addressList || addressList.length === 0">
        <el-empty description="暂无收货地址" />
      </template>
      <template v-else>
        <el-card v-for="item in addressList" :key="item.id" class="address-item">
          <template #header>
            <div class="item-header">
              <span>{{ item.consignee }}</span>
              <span>{{ item.phoneNumber }}</span>
              <el-tag v-if="item.isDefault === 1" type="danger">默认</el-tag>
              <el-tag v-if="item.tag">{{ item.tag }}</el-tag>
            </div>
          </template>
          <div>
            {{
              [item.provinceCode, item.cityCode, item.districtCode]
                .map((code) => codeToText[code])
                .filter(Boolean)
                .join(' ')
            }}&nbsp;{{ item.detailAddress }}
          </div>
          <template #footer>
            <div class="two-side">
              <el-checkbox
                v-model="item.isDefault"
                :true-label="1"
                :false-label="0"
                :disabled="setDefaultLoading"
                @change="handleSetDefault(item)"
              >
                <span v-if="item.isDefault === 1">已默认</span>
                <span v-else>设置为默认地址</span>
              </el-checkbox>
              <div>
                <el-button icon="Edit" type="primary" :loading="editLoading" @click="handleEdit(item)">编辑</el-button>
                <el-button icon="Delete" type="danger" :loading="removeLoading" @click="handleRemove(item.id)">
                  删除
                </el-button>
              </div>
            </div>
          </template>
        </el-card>
      </template>
    </el-card>
  </el-scrollbar>

  <el-dialog
    v-model="dialogState.visible"
    :title="dialogState.title"
    :close-on-click-modal="false"
    width="500px"
    align-center
    :show-close="false"
  >
    <el-form ref="dialogRef" :model="dialogState.form" :rules="dialogState.rules" label-width="100px">
      <el-form-item prop="consignee" label="收货人">
        <el-input v-model="dialogState.form.consignee" placeholder="请填写收货人姓名" />
      </el-form-item>
      <el-form-item prop="phoneNumber" label="手机号">
        <el-input v-model="dialogState.form.phoneNumber" placeholder="请填写收货人手机号" />
      </el-form-item>
      <el-form-item prop="regionCode" label="省市区" :rules="regionCodeRules">
        <el-cascader
          v-model="regionCode"
          :options="regionData"
          placeholder="请选择收货地址"
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
      <el-form-item prop="tag" label="标签">
        <el-radio-group v-model="dialogState.form.tag" @change="customTag = false">
          <el-radio-button value="家">家</el-radio-button>
          <el-radio-button value="公司">公司</el-radio-button>
          <el-radio-button value="学校">学校</el-radio-button>
        </el-radio-group>
        <el-button @click="handleCustom">自定义</el-button>
        <el-input
          v-show="customTag"
          v-model="dialogState.form.tag"
          placeholder="请输入自定义标签"
          maxlength="5"
          show-word-limit
        />
      </el-form-item>
      <el-form-item label="设为默认地址">
        <el-checkbox v-model="dialogState.form.isDefault" :true-label="1" :false-label="0" />
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
  listAddressApi,
  addAddressApi,
  removeAddressApi,
  editAddressApi,
  queryAddressApi,
} from '@/api/customer/address'
import { ElMessageBox, ElMessage } from 'element-plus'
import { useAuthStore } from '@/stores'

const authStore = useAuthStore()
const addressList = ref([])

const customTag = ref(false)
const removeLoading = ref(false)
const editLoading = ref(false)
const saveLoading = ref(false)
const setDefaultLoading = ref(false)

const dialogRef = ref(null)

const consigneeRules = [
  {
    required: true,
    message: '请输入收货人',
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
const detailAddressRules = [
  {
    required: true,
    message: '请输入详细地址',
    trigger: ['blur', 'change'],
  },
]
const regionCodeRules = [
  {
    required: true,
    validator: (rule, value, callback) => {
      if (!regionCode.value || regionCode.value.length !== 3 || regionCode.value.some((v) => !v)) {
        callback(new Error('请选择省市区'))
      } else {
        callback()
      }
    },
    trigger: ['blur', 'change'],
  },
]

const dialogState = ref({
  visible: false,
  form: {},
  title: '',
  rules: {
    consignee: consigneeRules,
    phoneNumber: phoneRules,
    detailAddress: detailAddressRules,
  },
})

const regionCode = ref([])
const handleRegionChange = (val) => {
  dialogState.value.form.provinceCode = val[0]
  dialogState.value.form.cityCode = val[1]
  dialogState.value.form.districtCode = val[2]
}

const handleCustom = () => {
  customTag.value = true
  dialogState.value.form.tag = ''
}

const handleAdd = () => {
  dialogState.value.title = '新增收货地址'
  dialogRef.value?.resetFields?.()
  dialogState.value.form = {}
  dialogState.value.form.userId = authStore.authMsg.id
  dialogState.value.form.isDefault = 0
  customTag.value = false
  regionCode.value = []
  dialogState.value.visible = true
}

const handleSetDefault = async (item) => {
  if (setDefaultLoading.value) return
  setDefaultLoading.value = true
  try {
    const res = await editAddressApi(item)
    if (res.code === 200) {
      listAddress()
    }
  } finally {
    setDefaultLoading.value = false
  }
}

const handleEdit = async (item) => {
  if (editLoading.value || dialogState.value.visible) return
  editLoading.value = true
  try {
    dialogState.value.title = '编辑收货地址'
    customTag.value = false
    dialogRef.value?.resetFields?.()
    const res = await queryAddressApi(item.id)
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
        const res = await removeAddressApi(id)
        if (res.code === 200) {
          ElMessage.success('删除成功')
          listAddress()
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
    await dialogRef.value?.validateField?.('regionCode')
    await dialogRef.value?.validate?.()
    let res
    if (dialogState.value.form.id) {
      res = await editAddressApi(dialogState.value.form)
    } else {
      res = await addAddressApi(dialogState.value.form)
    }
    if (res.code === 200) {
      ElMessage.success('保存成功')
      dialogState.value.visible = false
      listAddress()
    }
  } catch {
    ElMessage.warning('请检查输入')
  } finally {
    saveLoading.value = false
  }
}

const listAddress = async () => {
  const res = await listAddressApi()
  if (res.code === 200) {
    addressList.value = res.data
  }
}

onMounted(() => {
  listAddress()
})
</script>

<style scoped>
.address-card {
  margin: 0 300px;
  border-radius: var(--border-radius-lg);
}
.two-side {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.card-title {
  font-size: var(--font-size-xl);
  font-weight: bold;
}
.address-item {
  border-radius: var(--border-radius-md);
  margin-bottom: var(--spacing-md);
}
.item-header {
  font-size: var(--font-size-lg);
  display: flex;
  gap: var(--spacing-md);
}
</style>
