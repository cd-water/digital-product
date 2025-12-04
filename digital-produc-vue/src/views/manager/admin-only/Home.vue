<template>
  <div class="overview">
    <div class="item-card">
      <img src="@/assets/image/material1.png" alt="" class="item-img" />
      <div>
        <div class="item-title">入驻店铺</div>
        <div class="settled-shop">{{ allShopData.settledShopCount }}&nbsp;家</div>
      </div>
    </div>
    <div class="item-card">
      <img src="@/assets/image/material2.png" alt="" class="item-img" />
      <div>
        <div class="item-title">在售产品</div>
        <div class="sale-product">{{ allShopData.saleProductCount }}&nbsp;种</div>
      </div>
    </div>
    <div class="item-card">
      <img src="@/assets/image/material3.png" alt="" class="item-img" />
      <div>
        <div class="item-title">在售配件</div>
        <div class="sale-accessory">{{ allShopData.saleAccessoryCount }}&nbsp;种</div>
      </div>
    </div>
    <div class="item-card">
      <img src="@/assets/image/material4.png" alt="" class="item-img" />
      <div>
        <div class="item-title">产品销售额</div>
        <div class="price">￥{{ formatAmount(allShopData.productOrdersAmount) }}</div>
      </div>
    </div>
    <div class="item-card">
      <img src="@/assets/image/material5.png" alt="" class="item-img" />
      <div>
        <div class="item-title">配件销售额</div>
        <div class="price">￥{{ formatAmount(allShopData.accessoryOrdersAmount) }}</div>
      </div>
    </div>
  </div>

  <div class="chart">
    <div id="productPie"></div>
    <div id="productOrdersBar"></div>
  </div>
  <div class="chart">
    <div id="accessoryPie"></div>
    <div id="accessoryOrdersBar"></div>
  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import { adminCountApi, productPieApi, productOrdersBarApi, accessoryPieApi, accessoryOrdersBarApi } from '@/api/common'
import * as echarts from 'echarts'

const productPieOption = {
  title: {
    text: '数码产品类型种类分布',
    subtext: '统计每种数码产品类型下的种类个数',
    left: 'center',
  },
  grid: {
    left: '3%',
    right: '3%',
    top: '15%',
    bottom: '10%',
    containLabel: true,
  },
  tooltip: {
    trigger: 'item',
    backgroundColor: '#fff',
    borderColor: '#eee',
    borderWidth: 1,
    textStyle: { color: '#333', fontSize: 14 },
    formatter: '{b}<br/>{c} ({d}%)',
    extraCssText: 'box-shadow: 0 2px 8px rgba(0,0,0,0.15);',
  },
  legend: {
    orient: 'vertical',
    left: 'right',
    top: 'center',
    itemWidth: 32,
    itemHeight: 14,
    textStyle: { fontSize: 14, color: '#666', padding: [2, 0, 2, 0] },
  },
  series: [
    {
      type: 'pie',
      radius: '65%',
      center: ['40%', '60%'],
      data: [],
      label: {
        show: true,
        position: 'outside',
        formatter: '{b|{b}}\n{c} ({d}%)',
        rich: { b: { fontSize: 14, color: '#333', fontWeight: 'bold', lineHeight: 18 } },
      },
      labelLine: {
        length: 18,
        length2: 12,
        smooth: true,
        lineStyle: { color: '#aaa' },
      },
      itemStyle: {
        borderRadius: 8,
        borderColor: '#fff',
        borderWidth: 2,
        shadowBlur: 12,
        shadowColor: 'rgba(50, 50, 50, 0.08)',
      },
      emphasis: {
        scale: true,
        scaleSize: 10,
        itemStyle: {
          shadowBlur: 20,
          shadowColor: 'rgba(0, 0, 0, 0.18)',
          borderColor: '#eee',
          borderWidth: 3,
        },
        label: { fontSize: 16, fontWeight: 'bold', color: '#1976D2' },
      },
      color: [
        '#1976D2',
        '#43A047',
        '#FFA000',
        '#8E24AA',
        '#E53935',
        '#00ACC1',
        '#FDD835',
        '#3949AB',
        '#D81B60',
        '#00897B',
      ],
    },
  ],
}

const productOrdersBarOption = {
  title: {
    text: '店铺数码产品订单金额统计',
    subtext: '统计每个店铺有效数码产品订单金额',
    left: 'center',
  },
  tooltip: {
    trigger: 'axis',
    axisPointer: { type: 'shadow' },
  },
  grid: {
    top: '15%',
    left: '3%',
    right: '3%',
    bottom: '10%',
    containLabel: true,
  },
  dataZoom: [
    {
      type: 'slider',
      show: true,
      xAxisIndex: 0,
      start: 0,
      end: 100,
      height: 16,
      bottom: 8,
      showDetail: false,
    },
    {
      type: 'inside',
      xAxisIndex: 0,
      start: 0,
      end: 100,
    },
  ],
  xAxis: {
    type: 'category',
    data: [],
    axisTick: { alignWithLabel: true },
    axisLabel: {
      formatter: function (value) {
        return value && value.length > 6 ? value.slice(0, 6) + '…' : value
      },
    },
  },
  yAxis: {
    type: 'value',
    name: '金额(元)',
    splitLine: {
      lineStyle: { type: 'dashed', color: '#e0e6f1' },
    },
  },
  series: [
    {
      data: [],
      type: 'bar',
      barWidth: '55%',
      itemStyle: {
        borderRadius: [8, 8, 0, 0],
        color: function (params) {
          const vividColors = [
            ['#1976D2', '#42A5F5'],
            ['#43A047', '#66BB6A'],
            ['#FFA000', '#FFD54F'],
            ['#8E24AA', '#BA68C8'],
            ['#E53935', '#FF8A65'],
            ['#00ACC1', '#4DD0E1'],
            ['#FDD835', '#FFF176'],
            ['#3949AB', '#7986CB'],
            ['#D81B60', '#F06292'],
            ['#00897B', '#4DB6AC'],
          ]
          const colorPair = vividColors[params.dataIndex % vividColors.length]
          return new echarts.graphic.LinearGradient(0, 0, 0, 1, [
            { offset: 0, color: colorPair[0] },
            { offset: 1, color: colorPair[1] },
          ])
        },
        shadowColor: 'rgba(25, 118, 210, 0.15)',
        shadowBlur: 12,
      },
      emphasis: {
        itemStyle: {
          borderColor: '#fff',
          borderWidth: 2,
          shadowColor: 'rgba(25, 118, 210, 0.25)',
          shadowBlur: 20,
        },
      },
    },
  ],
}

const accessoryPieOption = {
  title: {
    text: '数码配件类型种类分布',
    subtext: '统计每种数码配件类型下的种类个数',
    left: 'center',
  },
  grid: {
    left: '3%',
    right: '3%',
    top: '15%',
    bottom: '10%',
    containLabel: true,
  },
  tooltip: {
    trigger: 'item',
    backgroundColor: '#fff',
    borderColor: '#eee',
    borderWidth: 1,
    textStyle: { color: '#333', fontSize: 14 },
    formatter: '{b}<br/>{c} ({d}%)',
    extraCssText: 'box-shadow: 0 2px 8px rgba(0,0,0,0.15);',
  },
  legend: {
    orient: 'vertical',
    left: 'right',
    top: 'center',
    itemWidth: 32,
    itemHeight: 14,
    textStyle: { fontSize: 14, color: '#666', padding: [2, 0, 2, 0] },
  },
  series: [
    {
      type: 'pie',
      radius: '65%',
      center: ['40%', '60%'],
      data: [],
      label: {
        show: true,
        position: 'outside',
        formatter: '{b|{b}}\n{c} ({d}%)',
        rich: { b: { fontSize: 14, color: '#333', fontWeight: 'bold', lineHeight: 18 } },
      },
      labelLine: {
        length: 18,
        length2: 12,
        smooth: true,
        lineStyle: { color: '#aaa' },
      },
      itemStyle: {
        borderRadius: 8,
        borderColor: '#fff',
        borderWidth: 2,
        shadowBlur: 12,
        shadowColor: 'rgba(50, 50, 50, 0.08)',
      },
      emphasis: {
        scale: true,
        scaleSize: 10,
        itemStyle: {
          shadowBlur: 20,
          shadowColor: 'rgba(0, 0, 0, 0.18)',
          borderColor: '#eee',
          borderWidth: 3,
        },
        label: { fontSize: 16, fontWeight: 'bold', color: '#1976D2' },
      },
      color: [
        '#1976D2',
        '#43A047',
        '#FFA000',
        '#8E24AA',
        '#E53935',
        '#00ACC1',
        '#FDD835',
        '#3949AB',
        '#D81B60',
        '#00897B',
      ],
    },
  ],
}

const accessoryOrdersBarOption = {
  title: {
    text: '店铺数码配件订单金额统计',
    subtext: '统计每个店铺有效数码配件订单金额',
    left: 'center',
  },
  tooltip: {
    trigger: 'axis',
    axisPointer: { type: 'shadow' },
  },
  grid: {
    top: '15%',
    left: '3%',
    right: '3%',
    bottom: '10%',
    containLabel: true,
  },
  dataZoom: [
    {
      type: 'slider',
      show: true,
      xAxisIndex: 0,
      start: 0,
      end: 100,
      height: 16,
      bottom: 8,
      showDetail: false,
    },
    {
      type: 'inside',
      xAxisIndex: 0,
      start: 0,
      end: 100,
    },
  ],
  xAxis: {
    type: 'category',
    data: [],
    axisTick: { alignWithLabel: true },
    axisLabel: {
      formatter: function (value) {
        return value && value.length > 6 ? value.slice(0, 6) + '…' : value
      },
    },
  },
  yAxis: {
    type: 'value',
    name: '金额(元)',
    splitLine: {
      lineStyle: { type: 'dashed', color: '#e0e6f1' },
    },
  },
  series: [
    {
      data: [],
      type: 'bar',
      barWidth: '55%',
      itemStyle: {
        borderRadius: [8, 8, 0, 0],
        color: function (params) {
          const vividColors = [
            ['#ffaa2e', '#ffd180'],
            ['#32C5E9', '#81d4fa'],
            ['#fa4c4c', '#ff8a80'],
            ['#08b448', '#69f0ae'],
            ['#FFDB5C', '#fff59d'],
            ['#ff9f7f', '#ffccbc'],
            ['#fb7293', '#f8bbd0'],
            ['#E062AE', '#f48fb1'],
            ['#E690D1', '#ce93d8'],
            ['#e7bcf3', '#e1bee7'],
          ]
          const colorPair = vividColors[params.dataIndex % vividColors.length]
          return new echarts.graphic.LinearGradient(0, 0, 0, 1, [
            { offset: 0, color: colorPair[0] },
            { offset: 1, color: colorPair[1] },
          ])
        },
        shadowColor: 'rgba(255, 170, 46, 0.15)',
        shadowBlur: 12,
      },
      emphasis: {
        itemStyle: {
          borderColor: '#fff',
          borderWidth: 2,
          shadowColor: 'rgba(255, 170, 46, 0.25)',
          shadowBlur: 20,
        },
      },
    },
  ],
}

const getProductPie = async () => {
  const dom = document.getElementById('productPie')
  let productPieChart = echarts.init(dom)
  const res = await productPieApi()
  if (res.code === 200) {
    productPieOption.series[0].data = res.data
    productPieChart.setOption(productPieOption)
  }
}

const getProductOrdersBar = async () => {
  const dom = document.getElementById('productOrdersBar')
  let productOrdersBarChart = echarts.init(dom)
  const res = await productOrdersBarApi()
  if (res.code === 200) {
    productOrdersBarOption.xAxis.data = res.data.shopNameList
    productOrdersBarOption.series[0].data = res.data.amountList
    productOrdersBarChart.setOption(productOrdersBarOption)
  }
}

const getAccessoryPie = async () => {
  const dom = document.getElementById('accessoryPie')
  let accessoryPieChart = echarts.init(dom)
  const res = await accessoryPieApi()
  if (res.code === 200) {
    accessoryPieOption.series[0].data = res.data
    accessoryPieChart.setOption(accessoryPieOption)
  }
}

const getAccessoryOrdersBar = async () => {
  const dom = document.getElementById('accessoryOrdersBar')
  let accessoryOrdersBarChart = echarts.init(dom)
  const res = await accessoryOrdersBarApi()
  if (res.code === 200) {
    accessoryOrdersBarOption.xAxis.data = res.data.shopNameList
    accessoryOrdersBarOption.series[0].data = res.data.amountList
    accessoryOrdersBarChart.setOption(accessoryOrdersBarOption)
  }
}

const allShopData = ref({})

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

const getAllShopData = async () => {
  const res = await adminCountApi()
  if (res.code === 200) {
    allShopData.value = res.data
  }
}

onMounted(() => {
  getAllShopData()
  getProductPie()
  getProductOrdersBar()
  getAccessoryPie()
  getAccessoryOrdersBar()
})
</script>

<style scoped>
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
  align-items: center;
  gap: var(--spacing-md);
}
.item-img {
  width: 70px;
  height: 70px;
}
.item-title {
  font-size: var(--font-size-lg);
}
.price {
  color: var(--danger-color);
  font-size: var(--font-size-xl);
  font-weight: bold;
  white-space: nowrap;
}
.settled-shop {
  color: var(--primary-color);
  font-size: var(--font-size-lg);
  font-weight: bold;
}
.sale-product {
  color: var(--success-color);
  font-size: var(--font-size-lg);
  font-weight: bold;
}
.sale-accessory {
  color: var(--success-color);
  font-size: var(--font-size-lg);
  font-weight: bold;
}
.chart {
  height: 400px;
  display: flex;
  gap: var(--spacing-lg);
}

#productPie,
#productOrdersBar,
#accessoryPie,
#accessoryOrdersBar {
  width: 100%;
  height: 100%;
  background: var(--text-white);
  border-radius: var(--border-radius-md);
}
</style>
