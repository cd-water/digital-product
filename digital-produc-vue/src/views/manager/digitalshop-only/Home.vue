<template>
  <div class="today-date">今日数据（{{ formatDate(new Date()) }}）</div>
  <div class="overview">
    <div class="item-card">
      <div class="item-title">营业额</div>
      <div class="turnover">￥{{ formatAmount(shopTodayData.turnover) }}</div>
    </div>
    <div class="item-card">
      <div class="item-title">今日订单数</div>
      <div class="today-orders">{{ shopTodayData.todayOrders }}</div>
    </div>
    <div class="item-card">
      <div class="item-title">待付款</div>
      <div class="pending-payment">{{ shopTodayData.pendingPayment }}</div>
    </div>
    <div class="item-card">
      <div class="item-title">待接单</div>
      <div class="pending-accept">{{ shopTodayData.pendingAccept }}</div>
    </div>
    <div class="item-card">
      <div class="item-title">派送中</div>
      <div class="delivering">{{ shopTodayData.delivering }}</div>
    </div>
    <div class="item-card">
      <div class="item-title">已送达</div>
      <div class="delivered">{{ shopTodayData.delivered }}</div>
    </div>
    <div class="item-card">
      <div class="item-title">已完成</div>
      <div class="completed">{{ shopTodayData.completed }}</div>
    </div>
    <div class="item-card">
      <div class="item-title">已取消</div>
      <div class="cancelled">{{ shopTodayData.cancelled }}</div>
    </div>
  </div>

  <div class="period-range">
    <el-segmented v-model="searchPeriod" :options="['昨日', '近7日', '近30日']" size="large" @change="periodChange" />
    <span>{{ periodRange }}</span>
  </div>

  <div class="chart">
    <div id="amountLine"></div>
    <div id="numberLine"></div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import * as echarts from 'echarts'
import { shopTodayApi, amountLineApi, numberLineApi } from '@/api/common'
import { useAuthStore } from '@/stores'

const authStore = useAuthStore()

const shopTodayData = ref({})
const searchPeriod = ref('近7日')
const periodRange = ref('')
const beginToEnd = ref({ begin: '', end: '' })

const formatDate = (date) => {
  const y = date.getFullYear()
  const m = String(date.getMonth() + 1).padStart(2, '0')
  const d = String(date.getDate()).padStart(2, '0')
  return `${y}-${m}-${d}`
}

const formatAmount = (amount) => {
  if (!amount) return '0'
  const num = Number(amount)
  if (num >= 100000000) {
    return (num / 100000000).toFixed(1) + '亿'
  } else if (num >= 10000) {
    return (num / 10000).toFixed(1) + '万'
  } else if (num >= 1000) {
    return (num / 1000).toFixed(1) + '千'
  }
  return num.toLocaleString()
}

const amountLineOption = {
  title: {
    text: '营业额趋势',
    subtext: '统计周期内每日营业额变化（不包含已取消订单）',
    left: 'center',
    textStyle: {
      fontSize: 20,
      fontWeight: 'bold',
      color: '#333',
    },
  },
  tooltip: {
    trigger: 'axis',
    backgroundColor: 'rgba(255, 255, 255, 0.95)',
    borderColor: '#ddd',
    borderWidth: 1,
    textStyle: {
      color: '#333',
    },
    formatter: function (params) {
      const data = params[0]
      return `${data.axisValue}<br/>营业额: ￥${data.value.toLocaleString()}`
    },
  },
  grid: {
    left: '3%',
    right: '4%',
    bottom: '3%',
    containLabel: true,
  },
  xAxis: {
    type: 'category',
    data: [],
    axisLine: {
      lineStyle: {
        color: '#ddd',
      },
    },
    axisTick: {
      alignWithLabel: true,
    },
    axisLabel: {
      color: '#666',
    },
  },
  yAxis: {
    type: 'value',
    axisLine: {
      lineStyle: {
        color: '#ddd',
      },
    },
    axisLabel: {
      color: '#666',
      formatter: function (value) {
        return '￥' + value.toLocaleString()
      },
    },
    splitLine: {
      lineStyle: {
        color: '#f0f0f0',
        type: 'dashed',
      },
    },
  },
  series: [
    {
      data: [],
      type: 'line',
      symbol: 'circle',
      symbolSize: 6,
      lineStyle: {
        width: 3,
        color: '#FFA500',
      },
      itemStyle: {
        color: '#ff6b6b',
        borderColor: '#fff',
        borderWidth: 2,
      },
      areaStyle: {
        color: {
          type: 'linear',
          x: 0,
          y: 0,
          x2: 0,
          y2: 1,
          colorStops: [
            { offset: 0, color: 'rgba(255, 165, 0, 0.25)' },
            { offset: 1, color: 'rgba(255, 223, 128, 0.05)' },
          ],
        },
      },
      emphasis: {
        focus: 'series',
        itemStyle: {
          color: '#ff4757',
          shadowBlur: 10,
          shadowColor: 'rgba(255, 71, 87, 0.5)',
        },
      },
    },
  ],
  animation: true,
  animationDuration: 1000,
  animationEasing: 'cubicOut',
}

const numberLineOption = {
  title: {
    text: '订单数趋势',
    subtext: '统计周期内每日订单数变化（总订单数与有效订单数）',
    left: 'center',
    textStyle: {
      fontSize: 20,
      fontWeight: 'bold',
      color: '#333',
    },
  },
  tooltip: {
    trigger: 'axis',
    backgroundColor: 'rgba(255, 255, 255, 0.95)',
    borderColor: '#ddd',
    borderWidth: 1,
    textStyle: {
      color: '#333',
    },
    formatter: function (params) {
      let result = `${params[0].axisValue}<br/>`
      params.forEach((param) => {
        result += `${param.seriesName}: ${param.value}单<br/>`
      })
      return result
    },
  },
  legend: {
    top: 'bottom',
    left: 'center',
    textStyle: {
      color: '#666',
      fontSize: 12,
    },
    itemGap: 20,
  },
  grid: {
    left: '3%',
    right: '4%',
    bottom: '8%',
    containLabel: true,
  },
  xAxis: {
    type: 'category',
    data: [],
    axisLine: {
      lineStyle: {
        color: '#ddd',
      },
    },
    axisTick: {
      alignWithLabel: true,
    },
    axisLabel: {
      color: '#666',
    },
  },
  yAxis: {
    type: 'value',
    minInterval: 1,
    axisLine: {
      lineStyle: {
        color: '#ddd',
      },
    },
    axisLabel: {
      color: '#666',
      formatter: function (value) {
        if (Number.isInteger(value)) {
          return value + '单'
        }
        return ''
      },
    },
    splitLine: {
      lineStyle: {
        color: '#f0f0f0',
        type: 'dashed',
      },
    },
  },
  series: [
    {
      name: '总订单数',
      data: [],
      type: 'line',
      symbol: 'circle',
      symbolSize: 6,
      lineStyle: {
        width: 3,
        color: '#5470c6',
      },
      itemStyle: {
        color: '#5470c6',
        borderColor: '#fff',
        borderWidth: 2,
      },
      areaStyle: {
        color: {
          type: 'linear',
          x: 0,
          y: 0,
          x2: 0,
          y2: 1,
          colorStops: [
            { offset: 0, color: 'rgba(84, 112, 198, 0.3)' },
            { offset: 1, color: 'rgba(84, 112, 198, 0.1)' },
          ],
        },
      },
      emphasis: {
        focus: 'series',
        itemStyle: {
          color: '#3b4d8c',
          shadowBlur: 10,
          shadowColor: 'rgba(59, 77, 140, 0.5)',
        },
      },
    },
    {
      name: '有效订单数',
      data: [],
      type: 'line',
      symbol: 'circle',
      symbolSize: 6,
      lineStyle: {
        width: 3,
        color: '#91cc75',
      },
      itemStyle: {
        color: '#91cc75',
        borderColor: '#fff',
        borderWidth: 2,
      },
      areaStyle: {
        color: {
          type: 'linear',
          x: 0,
          y: 0,
          x2: 0,
          y2: 1,
          colorStops: [
            { offset: 0, color: 'rgba(145, 204, 117, 0.3)' },
            { offset: 1, color: 'rgba(145, 204, 117, 0.1)' },
          ],
        },
      },
      emphasis: {
        focus: 'series',
        itemStyle: {
          color: '#6ba85a',
          shadowBlur: 10,
          shadowColor: 'rgba(107, 168, 90, 0.5)',
        },
      },
    },
  ],
  animation: true,
  animationDuration: 1000,
  animationEasing: 'cubicOut',
}

const getAmountLine = async () => {
  const dom = document.getElementById('amountLine')
  let amountLineChart = echarts.getInstanceByDom(dom)
  if (!amountLineChart) {
    amountLineChart = echarts.init(dom)
  }
  const shopId = authStore.authMsg.id
  const res = await amountLineApi(shopId, beginToEnd.value.begin, beginToEnd.value.end)
  if (res.code === 200) {
    amountLineOption.xAxis.data = res.data.dateList
    amountLineOption.series[0].data = res.data.amountList
    amountLineChart.setOption(amountLineOption)
  }
}

const getNumberLine = async () => {
  const dom = document.getElementById('numberLine')
  let numberLineChart = echarts.getInstanceByDom(dom)
  if (!numberLineChart) {
    numberLineChart = echarts.init(dom)
  }
  const shopId = authStore.authMsg.id
  const res = await numberLineApi(shopId, beginToEnd.value.begin, beginToEnd.value.end)
  if (res.code === 200) {
    numberLineOption.xAxis.data = res.data.dateList
    numberLineOption.series[0].data = res.data.totalNumberList
    numberLineOption.series[1].data = res.data.validNumberList
    numberLineChart.setOption(numberLineOption)
  }
}

const getShopTodayData = async () => {
  const shopId = authStore.authMsg.id
  const res = await shopTodayApi(shopId)
  if (res.code === 200) {
    shopTodayData.value = res.data
  }
}

const periodChange = () => {
  const today = new Date()
  let start, end
  if (searchPeriod.value === '昨日') {
    const yesterday = new Date(today)
    yesterday.setDate(today.getDate() - 1)
    periodRange.value = formatDate(yesterday)
    beginToEnd.value.begin = formatDate(yesterday)
    beginToEnd.value.end = formatDate(yesterday)
  } else if (searchPeriod.value === '近7日') {
    end = new Date(today)
    end.setDate(today.getDate() - 1)
    start = new Date(end)
    start.setDate(end.getDate() - 6)
    periodRange.value = `${formatDate(start)}至${formatDate(end)}`
    beginToEnd.value.begin = formatDate(start)
    beginToEnd.value.end = formatDate(end)
  } else if (searchPeriod.value === '近30日') {
    end = new Date(today)
    end.setDate(today.getDate() - 1)
    start = new Date(end)
    start.setDate(end.getDate() - 29)
    periodRange.value = `${formatDate(start)}至${formatDate(end)}`
    beginToEnd.value.begin = formatDate(start)
    beginToEnd.value.end = formatDate(end)
  } else {
    periodRange.value = ''
    beginToEnd.value.begin = ''
    beginToEnd.value.end = ''
  }
  getAmountLine()
  getNumberLine()
}

onMounted(() => {
  periodChange()
  getShopTodayData()
})
</script>

<style scoped>
.today-date {
  font-size: var(--font-size-lg);
}
.overview {
  display: flex;
  gap: var(--spacing-lg);
}
.item-card {
  flex: 1;
  background: var(--text-white);
  border-radius: var(--border-radius-lg);
  padding: var(--spacing-md);
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: var(--spacing-md);
}
.item-title {
  font-size: var(--font-size-lg);
}
.turnover {
  font-size: var(--font-size-xl);
  color: var(--danger-color);
  font-weight: bold;
  white-space: nowrap;
}
.today-orders {
  font-size: var(--font-size-lg);
  color: var(--primary-color);
  font-weight: bold;
}
.pending-payment {
  font-size: var(--font-size-lg);
  color: var(--warning-color);
  font-weight: bold;
}
.pending-accept {
  font-size: var(--font-size-lg);
  color: var(--warning-color);
  font-weight: bold;
}
.delivering {
  font-size: var(--font-size-lg);
  color: var(--primary-color);
  font-weight: bold;
}
.delivered {
  font-size: var(--font-size-lg);
  color: var(--success-color);
  font-weight: bold;
}
.completed {
  font-size: var(--font-size-lg);
  color: var(--success-color);
  font-weight: bold;
}
.cancelled {
  color: var(--danger-color);
  font-size: var(--font-size-lg);
  font-weight: bold;
}
.period-range {
  display: flex;
  align-items: center;
  gap: var(--spacing-lg);
}
.chart {
  height: 400px;
  display: flex;
  gap: var(--spacing-lg);
  margin-top: var(--spacing-lg);
}

#amountLine,
#numberLine {
  width: 100%;
  height: 100%;
  background: var(--text-white);
  border-radius: var(--border-radius-md);
}
</style>
