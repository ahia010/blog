

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
        <n-input size="medium" round placeholder="搜索" class="header-input">
<!--          <template #suffix>-->
<!--                      <n-icon :component="FlashOutline" />-->
<!--          </template>-->
        </n-input>
      </div>
      <div class="nav-right">
        <n-button type="text" @click="goLogin()">登录</n-button>
        <n-button type="text" @click="goReg()">注册</n-button>
      </div>
    </n-layout-header>
    <n-layout-content>
      <slot name="default">
      </slot>
    </n-layout-content>
    <BaseFooter/>
  </n-layout>
</template>

<script setup>
import {h, ref} from "vue";
import {useRouter, useRoute,RouterLink} from "vue-router";
import logo from "@/assets/logo.svg";
import BaseFooter from "@/components/BaseFooter.vue";

const router = useRouter()

const route = useRoute()

const activeKey = ref(route.path)


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
