<template>
  <el-card shadow="hover">
    <template #header>
      <div class="header-content">
        <el-icon class="expert-icon"><ChatDotRound /></el-icon>
        <span class="expert-title">数码专家 AI 助手</span>
        <el-tag type="success" effect="dark" size="small">在线</el-tag>
      </div>
    </template>

    <!-- 对话消息区域 -->
    <div ref="messagesContainer" class="messages-container">
      <div v-if="messages.length === 0" class="empty-state">
        <el-icon class="empty-icon"><ChatLineRound /></el-icon>
        <p>您好！我是数码专家 AI 助手，可以为您解答关于数码产品的任何问题。</p>
        <p>请随时向我提问，我会尽力帮助您！</p>
      </div>
      <div
        v-for="(msg, index) in messages"
        :key="index"
        :class="['message-item', msg.role === 'user' ? 'user-message' : 'ai-message']"
      >
        <div class="message-avatar">
          <el-avatar v-if="msg.role === 'user'" :src="userAvatar" :size="40">
            <el-icon><User /></el-icon>
          </el-avatar>
          <el-avatar v-else :size="40" :style="{ background: 'var(--primary-color)' }">
            <el-icon><Robot /></el-icon>
          </el-avatar>
        </div>
        <div class="message-content">
          <div class="message-bubble">
            <div v-if="msg.role === 'ai' && msg.loading" class="loading-dots">
              <span></span>
              <span></span>
              <span></span>
            </div>
            <div v-else v-dompurify-html="formatMessage(msg.content)" class="message-text"></div>
          </div>
          <div class="message-time">{{ formatTime(msg.timestamp) }}</div>
        </div>
      </div>
    </div>

    <!-- 输入区域 -->
    <div class="input-area">
      <el-input
        v-model="inputMessage"
        type="textarea"
        :rows="3"
        placeholder="请输入您的问题..."
        :disabled="isLoading"
        @keydown.enter.exact.prevent="handleSend"
        @keydown.enter.shift.exact="inputMessage += '\n'"
      />
      <div class="input-actions">
        <el-button type="primary" :loading="isLoading" :disabled="!inputMessage.trim()" @click="handleSend">
          <el-icon><Promotion /></el-icon>
          发送
        </el-button>
        <el-button :disabled="isLoading" @click="handleClear">
          <el-icon><Delete /></el-icon>
          清空对话
        </el-button>
      </div>
    </div>
  </el-card>
</template>

<script setup>
import { ref, nextTick, onMounted, onUnmounted } from 'vue'
import { useAuthStore } from '@/stores'
import { chatApi } from '@/api/expert'
import { ElMessage } from 'element-plus'

const authStore = useAuthStore()
const messages = ref([])
const inputMessage = ref('')
const isLoading = ref(false)
const messagesContainer = ref(null)
let cancelChat = null

const userAvatar = ref(authStore.authMsg.avatar || '')

// 格式化消息内容（支持换行）
const formatMessage = (text) => {
  if (!text) return ''
  return text.replace(/\n/g, '<br>').replace(/ /g, '&nbsp;')
}

// 格式化时间
const formatTime = (timestamp) => {
  if (!timestamp) return ''
  const date = new Date(timestamp)
  const hours = String(date.getHours()).padStart(2, '0')
  const minutes = String(date.getMinutes()).padStart(2, '0')
  return `${hours}:${minutes}`
}

// 滚动到底部
const scrollToBottom = () => {
  nextTick(() => {
    if (messagesContainer.value) {
      messagesContainer.value.scrollTop = messagesContainer.value.scrollHeight
    }
  })
}

// 发送消息
const handleSend = async () => {
  const message = inputMessage.value.trim()
  if (!message || isLoading.value) return

  // 检查用户是否登录
  if (!authStore.authMsg.id) {
    ElMessage.warning('请先登录后再使用 AI 助手')
    return
  }

  // 添加用户消息
  messages.value.push({
    role: 'user',
    content: message,
    timestamp: Date.now(),
  })

  inputMessage.value = ''
  scrollToBottom()

  // 添加 AI 消息占位符
  const aiMessageIndex = messages.value.length
  messages.value.push({
    role: 'ai',
    content: '',
    loading: true,
    timestamp: Date.now(),
  })

  isLoading.value = true
  scrollToBottom()

  // 调用聊天 API
  cancelChat = chatApi(
    message,
    // onMessage: 接收流式数据
    (chunk) => {
      if (messages.value[aiMessageIndex]) {
        messages.value[aiMessageIndex].content += chunk
        messages.value[aiMessageIndex].loading = false
        scrollToBottom()
      }
    },
    // onError: 错误处理
    (error) => {
      ElMessage.error(error || '发送消息失败，请稍后重试')
      if (messages.value[aiMessageIndex]) {
        messages.value[aiMessageIndex].content = '抱歉，发生了错误，请稍后重试。'
        messages.value[aiMessageIndex].loading = false
      }
      isLoading.value = false
    },
    // onComplete: 完成回调
    () => {
      if (messages.value[aiMessageIndex]) {
        messages.value[aiMessageIndex].loading = false
      }
      isLoading.value = false
      scrollToBottom()
    },
  )
}

// 清空对话
const handleClear = () => {
  if (isLoading.value) {
    ElMessage.warning('正在发送消息，请稍候...')
    return
  }
  messages.value = []
  if (cancelChat) {
    cancelChat()
    cancelChat = null
  }
  ElMessage.success('对话已清空')
}

// 组件卸载时取消请求
onUnmounted(() => {
  if (cancelChat) {
    cancelChat()
  }
})

// 监听用户头像变化
onMounted(() => {
  userAvatar.value = authStore.authMsg.avatar || ''
})
</script>

<style scoped>
.expert-container {
  min-height: calc(100vh - 200px);
  padding: var(--spacing-lg);
  display: flex;
  justify-content: center;
}

.header-content {
  display: flex;
  align-items: center;
  gap: var(--spacing-md);
}

.expert-icon {
  font-size: 24px;
  color: var(--primary-color);
}

.expert-title {
  font-size: var(--font-size-xl);
  font-weight: bold;
  flex: 1;
}

.messages-container {
  flex: 1;
  overflow-y: auto;
  padding: var(--spacing-md);
  background: var(--item-color);
  border-radius: var(--border-radius-md);
  margin-bottom: var(--spacing-md);
  min-height: 400px;
}

.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 100%;
  color: var(--info-color);
  text-align: center;
  padding: var(--spacing-xl);
}

.empty-icon {
  font-size: 64px;
  margin-bottom: var(--spacing-md);
  color: var(--primary-color);
}

.empty-state p {
  margin: var(--spacing-sm) 0;
  font-size: var(--font-size-md);
}

.message-item {
  display: flex;
  margin-bottom: var(--spacing-lg);
  animation: fadeIn 0.3s ease-in;
}

@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(10px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.user-message {
  flex-direction: row-reverse;
}

.message-avatar {
  flex-shrink: 0;
  margin: 0 var(--spacing-md);
}

.message-content {
  max-width: 70%;
  display: flex;
  flex-direction: column;
}

.user-message .message-content {
  align-items: flex-end;
}

.ai-message .message-content {
  align-items: flex-start;
}

.message-bubble {
  padding: var(--spacing-md);
  border-radius: var(--border-radius-lg);
  word-wrap: break-word;
  line-height: 1.6;
}

.user-message .message-bubble {
  background: var(--primary-color);
  color: var(--text-white);
  border-bottom-right-radius: var(--border-radius-xs);
}

.ai-message .message-bubble {
  background: var(--text-white);
  color: var(--text-black);
  border: 1px solid var(--info-color);
  border-bottom-left-radius: var(--border-radius-xs);
}

.message-text {
  font-size: var(--font-size-md);
}

.loading-dots {
  display: flex;
  gap: var(--spacing-xs);
  padding: var(--spacing-sm) 0;
}

.loading-dots span {
  width: 8px;
  height: 8px;
  border-radius: 50%;
  background: var(--primary-color);
  animation: bounce 1.4s infinite ease-in-out both;
}

.loading-dots span:nth-child(1) {
  animation-delay: -0.32s;
}

.loading-dots span:nth-child(2) {
  animation-delay: -0.16s;
}

@keyframes bounce {
  0%,
  80%,
  100% {
    transform: scale(0);
  }
  40% {
    transform: scale(1);
  }
}

.message-time {
  font-size: var(--font-size-sm);
  color: var(--info-color);
  margin-top: var(--spacing-xs);
  padding: 0 var(--spacing-sm);
}

.input-area {
  border-top: 1px solid var(--item-color);
  padding-top: var(--spacing-md);
}

.input-actions {
  display: flex;
  justify-content: flex-end;
  gap: var(--spacing-md);
  margin-top: var(--spacing-md);
}

/* 滚动条样式 */
.messages-container::-webkit-scrollbar {
  width: 6px;
}

.messages-container::-webkit-scrollbar-track {
  background: transparent;
}

.messages-container::-webkit-scrollbar-thumb {
  background: var(--info-color);
  border-radius: 3px;
}

.messages-container::-webkit-scrollbar-thumb:hover {
  background: var(--primary-color);
}
</style>
