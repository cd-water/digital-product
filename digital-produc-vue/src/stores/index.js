import { createPinia } from 'pinia'
import piniaPluginPersistedstate from 'pinia-plugin-persistedstate'

const pinia = createPinia().use(piniaPluginPersistedstate)
pinia.use(piniaPluginPersistedstate)

export default pinia

export * from './auth'
export * from './remember'
