// import './assets/main.css'

import { createApp } from 'vue'
import { createPinia } from 'pinia'
import piniaPluginPersistedstate from 'pinia-plugin-persistedstate'

import NConfigProvider from 'naive-ui'

import App from './App.vue'
import router from './router'


const pinia = createPinia() //创建pinia实例
pinia.use(piniaPluginPersistedstate) //将插件添加到 pinia 实例上

const app = createApp(App)

app.use(pinia)
app.use(router)
app.use(NConfigProvider)
app.mount('#app')
