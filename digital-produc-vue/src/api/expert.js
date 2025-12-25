import { useAuthStore } from '@/stores'

/**
 * 处理 SSE 格式的数据，提取实际内容
 * @param {string} line - SSE 格式的数据行
 * @returns {string|null} 处理后的数据内容，如果是注释行则返回 null
 */
const processSSEData = (line) => {
  // 移除 SSE 注释行
  if (line.startsWith(':')) {
    return null
  }

  // 如果整行就是 "data:" 或 "data: "，返回空字符串
  const trimmed = line.trim()
  if (trimmed === 'data:' || trimmed === 'data:') {
    return ''
  }

  // 处理包含 data: 标记的情况
  // 直接替换所有的 "data: " 和 "data:" 前缀
  // 例如: "data:你好data:！data:我是" -> "你好！我是"
  if (line.includes('data:')) {
    // 先替换 "data: " (带空格)
    let result = line.replace(/data:\s+/g, '')
    // 再替换 "data:" (不带空格，防止遗漏)
    result = result.replace(/data:/g, '')
    return result
  }

  // 处理标准的 data: 前缀（单行格式）
  if (line.startsWith('data: ')) {
    return line.slice(6) // 移除 "data: " 前缀（6个字符）
  }

  if (line.startsWith('data:')) {
    return line.slice(5) // 移除 "data:" 前缀（5个字符）
  }

  // 纯文本流，直接返回
  return line
}

/**
 * 发送聊天消息（流式响应）
 * @param {string} message - 用户消息
 * @param {Function} onMessage - 接收消息的回调函数
 * @param {Function} onError - 错误处理回调
 * @param {Function} onComplete - 完成回调
 * @returns {Function} 取消请求的函数
 */
export const chatApi = (message, onMessage, onError, onComplete) => {
  const authStore = useAuthStore()
  const memoryId = authStore.authMsg.id
  const token = authStore.authMsg.token

  if (!memoryId) {
    onError?.('用户未登录')
    return () => {}
  }

  // 构建请求参数
  const params = new URLSearchParams({
    memoryId,
    message,
  })

  // 使用 fetch 进行流式请求
  const controller = new AbortController()
  const signal = controller.signal

  fetch(`/api/chat?${params.toString()}`, {
    method: 'POST',
    headers: {
      Authorization: token || '',
      Accept: 'text/event-stream',
    },
    signal,
  })
    .then(async (response) => {
      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`)
      }

      const reader = response.body.getReader()
      const decoder = new TextDecoder('utf-8')
      let buffer = ''

      while (true) {
        const { done, value } = await reader.read()
        if (done) {
          // 处理剩余的 buffer
          if (buffer.trim()) {
            const processed = processSSEData(buffer)
            if (processed !== null && processed !== '') {
              onMessage?.(processed)
            }
          }
          onComplete?.()
          break
        }

        buffer += decoder.decode(value, { stream: true })
        const lines = buffer.split('\n')
        buffer = lines.pop() || ''

        for (const line of lines) {
          if (line.trim()) {
            const processed = processSSEData(line)
            if (processed !== null && processed !== '') {
              onMessage?.(processed)
            }
          }
        }
      }
    })
    .catch((error) => {
      if (error.name !== 'AbortError') {
        onError?.(error.message || '请求失败')
      }
    })

  // 返回取消函数
  return () => {
    controller.abort()
  }
}
