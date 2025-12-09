import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import pinia from '@/stores/index'
import '@/assets/style/global.css'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import znCn from 'element-plus/es/locale/lang/zh-cn'
import VueDOMPurifyHTML from 'vue-dompurify-html'

const app = createApp(App)
app.use(pinia)
app.use(router)
app.use(ElementPlus, { locale: znCn })
app.use(VueDOMPurifyHTML)
app.mount('#app')

import * as ElementPlusIconsVue from '@element-plus/icons-vue'

for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
  app.component(key, component)
}
