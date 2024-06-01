<template>
  <n-layout has-sider>
    <n-layout-sider content-style="padding: 10px;minHeight:100vh;" width="250" bordered>
      <n-flex justify="center" style="margin-bottom: 20px;">
        <n-image width="35" preview-disabled :src="logo"/>
        <n-gradient-text :size="30" type="success">
          Ahia-Blog
        </n-gradient-text>
      </n-flex>
      <n-menu :options="menuOptions" v-model:value="activeKey" />
    </n-layout-sider>
    <n-layout>
      <n-layout-header style="padding: 24px" bordered>
        <n-flex justify="space-between">
          <div>11</div>
          <div>22</div>
        </n-flex>
      </n-layout-header>
      <n-layout-content content-style="padding: 24px;minHeight: 85vh;" style="">
        <router-view/>
      </n-layout-content>
      <BaseFooter/>
    </n-layout>
  </n-layout>
</template>

<script setup>
import logo from "@/assets/logo.svg";
import BaseFooter from "@/components/BaseFooter.vue";
import {useRouter, useRoute, RouterLink} from "vue-router";
import {h, ref} from "vue";


const router = useRouter()
const route = useRoute()
const activeKey = ref(route.path)

const menuOptions = [
  {
    label: () => h(
        RouterLink,
        {
          to: {
            name: "dashboard",
          }
        },
        {default: () => "首页"}
    ),
    key: "/admin",
  },
  {
    label: () => h(
        RouterLink,
        {
          to: {
            name: "admin-user",
          }
        },
        {default: () => "用户管理"}
    ),
    key: "/admin/user",
  },{
    label: () => h(
        RouterLink,
        {
          to: {
            name: "post",
          }
        },
        {default: () => "文章管理"}
    ),
    key: "/admin/post",
  },
  ]

</script>

<style scoped lang="scss">

</style>