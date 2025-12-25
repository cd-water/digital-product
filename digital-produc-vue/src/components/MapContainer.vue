<template>
  <div id="container"></div>
</template>

<script setup>
import { onMounted, onUnmounted } from 'vue'
import AMapLoader from '@amap/amap-jsapi-loader'

let map = null

const props = defineProps({
  code: {
    type: Array,
    default: () => [],
  },
})

// 获取目标adcode，优先区 > 市 > 省
const getTargetAdcode = (codeArr) => {
  if (codeArr && codeArr.length === 3) {
    if (codeArr[2]) return codeArr[2]
    if (codeArr[1]) return codeArr[1]
    if (codeArr[0]) return codeArr[0]
  }
  return null
}

onMounted(() => {
  window._AMapSecurityConfig = {
    securityJsCode: '「你申请的安全密钥」',
  }
  AMapLoader.load({
    key: '「你申请的应用Key」', // 申请好的Web端开发者Key，首次调用 load 时必填
    version: '2.0', // 指定要加载的 JSAPI 的版本，缺省时默认为 1.4.15
    plugins: ['AMap.Scale', 'AMap.DistrictSearch'], //需要使用的的插件列表，如比例尺'AMap.Scale'，支持添加多个如：['...','...']
  })
    .then((AMap) => {
      map = new AMap.Map('container', {
        // 设置地图容器id
        viewMode: '3D', // 是否为3D地图模式
        zoom: 13, // 初始化地图级别
      })

      //比例尺
      let scale = new AMap.Scale()
      map.addControl(scale)

      //根据props.code进行定位
      const adcode = getTargetAdcode(props.code)
      let districtSearch = new AMap.DistrictSearch({
        level: 'district',
        subdistrict: 0,
        extensions: 'all',
      })
      districtSearch.search(adcode, (status, result) => {
        if (status === 'complete' && result.districtList && result.districtList.length > 0) {
          const district = result.districtList[0]
          // 定位到行政区中心
          if (district && district.center) {
            map.setCenter(district.center)
          }
        } else {
          // fallback: 定位到中国
          map.setCity('中国')
        }
      })
    })
    .catch((e) => {
      console.log(e)
    })
})

onUnmounted(() => {
  map?.destroy()
})
</script>

<style scoped>
#container {
  padding: 0px;
  margin: 0px;
  width: 100%;
  height: 100%;
}
</style>
