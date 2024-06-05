<template>
  <n-layout has-sider>
    <n-layout-sider content-style="padding: 10px;minHeight:100vh;" width="250" bordered>
      <n-flex justify="center" style="margin-bottom: 30px">
        <n-image width="35" preview-disabled :src="logo"/>
        <n-gradient-text :size="30" type="success">
          Ahia-Blog
        </n-gradient-text>
      </n-flex>
      <!--      <n-divider >-->
      <!--        菜单-->
      <!--      </n-divider>-->
      <n-menu :options="menuOptions" v-model:value="activeKey"/>
    </n-layout-sider>
    <n-layout>
      <n-layout-header style="padding: 10px 30px 10px 10px" bordered>
        <n-flex justify="space-between" align="center">
          <div>面包屑</div>
          <n-dropdown trigger="hover" :options="options" @select="handleSelect">
            <n-image :src="avatar" preview-disabled width="50"></n-image>
          </n-dropdown>
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
import {h, onMounted, ref} from "vue";
import {userStore} from "@/stores/user.js";
import {useMessage} from "naive-ui";
import {getUserName} from "@/utils/request.js";

const message = useMessage();

const userInfo = userStore().getUserInfo()

const options = ref([
  {
    label: "你好,管理员",
    key: "username",
    disabled: true
  },
  {
    label: "个人中心",
    key: "mine"
  },
  {
    label: "设置",
    key: "setting"
  },
  {
    label: "前往主页",
    key: "home"
  },
  {
    label: "退出登录",
    key: "logout"
  }
])

async function getUser() {
  const headers = {
    Token: userInfo.token,
    "Content-Type": "application/json"
  }
  await getUserName(headers)
    .then(res => {
      if (res.data.code !== 200) {
        router.push({name: "login"})
        // options.value[0].label = "你好:" + res.data.username
      }
    })
    .catch(err => {
      console.log(err)
      router.push({name: "login"})
    })
}

onMounted(() => {
  options.value[0].label = "你好:" + userInfo.username
  getUser()
})

function handleSelect(key) {
  switch (key) {
    case "mine":
      router.push("/mine")
      break
    case "setting":
      router.push("/setting")
      break
    case "home":
      router.push("/")
      break
    case "logout":
      userStore().logout()
      message.success("退出登录成功")
      setTimeout(() => {
        router.push("/")
      }, 1000)
      break

  }
}

const avatar = userInfo.avatar ? userInfo.avatar : "api/avatar/default.jpg"

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
  }, {
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