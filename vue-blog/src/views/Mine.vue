<template>
  <base-layout>
    <template #default>
      <n-layout content-style="padding:20px 10vw;display:flex;gap:20px;minHeight:80vh">
        <n-card>
          <n-grid :x-gap="12">
            <n-gi :span="6">
              <div>
                <n-gradient-text type="info" size="36">
                  个人中心
                </n-gradient-text>
              </div>
              <div>
                <n-image width="300" height="300"
                         :src="user.getUserInfo().avatar==null?'api/avatar/default.jpg':user.getUserInfo().avatar"/>
              </div>

            </n-gi>
            <n-gi :span="18">
              <n-card content-style="padding: 0;">
                <n-tabs
                    type="line"
                    size="large"
                    :tabs-padding="20"
                    pane-style="padding: 20px;height: 300px;"
                >
                  <n-tab-pane name="个人信息">
                    PARKLIFE
                  </n-tab-pane>
                  <n-tab-pane name="我的评论">
                    ROCKLIFE
                  </n-tab-pane>
                </n-tabs>
              </n-card>
            </n-gi>
          </n-grid>

        </n-card>
      </n-layout>
    </template>
  </base-layout>
</template>

<script setup>
import BaseLayout from "@/components/BaseLayout.vue";
import {userStore} from "@/stores/user.js";
import bg from "@/assets/bg.jpg"

const user = userStore()
console.log(user.getUserInfo().avatar)

</script>

<style scoped lang="scss">

</style>