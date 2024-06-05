

<template>
  <n-layout>
    <n-layout-header bordered class="nav">
      <div class="nav-left">
        <n-image width="35" preview-disabled :src="logo" @click="goHome()"/>
        <n-gradient-text :size="30" type="success"  @click="goHome()">
          Ahia-Blog
        </n-gradient-text>
        <n-menu   v-model:value="activeKey" mode="horizontal" :options="menuOptions" responsive/>
      </div>
      <div class="header-center">
        <n-input v-model:value="search" size="medium" round placeholder="搜索" class="header-input">
          <template #suffix>
            <n-button text  >
                      <n-icon @click="goList()" size="20" :component="MdSearch">

                      </n-icon>
            </n-button>
          </template>
        </n-input>
      </div>
      <div class="nav-right" v-if="user.userInfo.token===''">
        <n-button type="text" @click="goLogin()">登录</n-button>
        <n-button type="text" @click="goReg()">注册</n-button>
      </div>
      <div class="nav-right" v-else>
        <n-dropdown trigger="hover" :options="options"  @select="handleSelect">
          <n-image :src="avatar" preview-disabled  width="50" height="50" object-fit="cover"></n-image>
        </n-dropdown>
      </div>
    </n-layout-header>
    <n-layout-content class="aaa">
      <slot name="default"/>
    </n-layout-content>
    <BaseFooter/>
  </n-layout>
</template>

<script setup>
import {h, onMounted, ref} from "vue";
import {useRouter, useRoute,RouterLink} from "vue-router";
import logo from "@/assets/logo.svg";
import BaseFooter from "@/components/BaseFooter.vue";
import {userStore} from "@/stores/user.js";
import {useMessage} from "naive-ui";
import {MdSearch} from "@vicons/ionicons4";


const search = ref("")
function goList() {
  if (search.value ===undefined||search.value === "") {
    message.info("请输入搜索内容")
    return
  }
  router.push({
    name: "list",
    query: {
      search: search.value
    }
  })
}

const message = useMessage();

const user = userStore()

const router = useRouter()

const route = useRoute()

const activeKey = ref(route.path)

const options =ref([
  {
    label: "你好,用户",
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
    label: "前往管理员后台",
    key: "admin",
    show: user.userInfo.role === 2
  },
  {
    label: "退出登录",
    key: "logout"
  }
])
onMounted(() => {
  options.value[0].label = "你好:"+user.userInfo.username
  console.log(route.query)
  if (route.query.serach!==null) {
    search.value = route.query.search
  }
})

const userInfo = user.getUserInfo()


const avatar = userInfo.avatar ? userInfo.avatar : "api/avatar/default.jpg"


function handleSelect(key) {
  if (key === "logout") {
    userStore().logout()
    message.success("退出登录成功")
    router.push({
      name: "home"
    })
  }
  if (key === "mine") {
    router.push({
      name: "mine"
    })
  }
  if (key === "setting") {
    // router.push({
    //   name: "setting"
    // })
  }
  if (key === "admin") {
    router.push({
      name: "dashboard"
    })
  }
}

const goLogin = () => {
  router.push({
    name: "login"
  })
}
const goReg = () => {
  router.push({
    name: "register"
  })
}
const goHome = () => {
  router.push({
    name: "home"
  })
}

const menuOptions = [
  {
    label: () => h(
        RouterLink,
        {
          to:{
            name: "home"
          }
        },
        { default: () => "首页" }
    ),
    key: "/",
  },
  {
    label: () => h(
        RouterLink,
        {
          to:{
            name: "list"
          }
        },
        { default: () => "分类" }
    ),
    key: "/list",
  },
];


</script>


<style scoped>
.header-center{
  flex: 1;
}
.n-layout-header {
  padding: 0 10vw;
  height: 64px;
  display: flex;
  align-items: center;
}

.nav-left {
  gap: 10px;
  display: flex;
  align-items: center;

}

.header-input {
  margin: 0 20px;
  width: 500px;
}

.header-input {

}

.nav-right {
  white-space: pre;
}
:deep(.n-menu) {
  margin-left: 50px;
}
/**
:deep(.n-menu) {

}



:deep(.v-overflow) {
  height: 100%;
  align-items: center;
}

:deep(.n-menu-item) {
  height: 100%;
  display: flex;
  align-items: center;
}


:deep(.n-menu.n-menu--horizontal .n-menu-item-content::before){
  display: flex;
}
:deep(.n-menu.n-menu--horizontal .n-menu-item-content){
  border: 0;
}
:deep(.n-menu.n-menu--horizontal .n-menu-item-content:not(.n-menu-item-content--disabled):hover){
  border: none;
}

 */
</style>
