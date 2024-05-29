<script setup>

import logo from "@/assets/logo.svg";
</script>

<template>
  <n-layout has-sider>
    <n-layout-sider content-style="padding: 10px;minHeight:100vh;" style="" bordered>
      <n-flex justify="center" style="margin-bottom: 20px;">
        <n-image width="35" preview-disabled :src="logo"/>
        <n-gradient-text :size="30" type="success">
          Ahia-Blog
        </n-gradient-text>
      </n-flex>
    </n-layout-sider>
    <n-layout>
      <n-layout-header style="padding: 24px" bordered>颐和园路</n-layout-header>
      <n-layout-content content-style="padding: 24px;">
        平山道
      </n-layout-content>
      <n-layout-footer bordered>成府路</n-layout-footer>
    </n-layout>
  </n-layout>
</template>

<style scoped lang="scss">

</style>