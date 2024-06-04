<template>
  <base-layout>
    <template #default>
      <n-layout content-style="padding:20px 10vw;display:flex;gap:20px;minHeight:80vh">
        <n-card>
          <n-grid :x-gap="12">
            <n-gi :span="6">
              <div style="margin-bottom: 20px;">
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
                    pane-style="padding: 20px;height: 500px;"
                >
                  <n-tab-pane name="个人信息">
                    <n-form label-placement="left" label-width="auto" label-align="left">
                      <n-form-item label="用户名:">
                        <n-text>{{ userInfo.username }}</n-text>
                      </n-form-item>
                      <n-form-item label="邮箱:">
                        <n-input v-if="editUser" style="width: 300px" v-model:value="userInfo.email"/>
                        <n-text v-else>{{ userInfo.email }}</n-text>
                      </n-form-item>
                      <n-form-item label="电话:">
                        <n-input v-if="editUser" style="width: 300px" v-model:value="userInfo.phone"/>
                        <n-text v-else>{{ userInfo.phone }}</n-text>

                      </n-form-item>
                      <n-form-item label="创建时间：">
                        <n-text>{{ userInfo.createTime }}</n-text>
                      </n-form-item>
                      <n-form-item>
                        <n-space v-if="editUser">
                          <n-button type="primary" @click="updateInfo">确定</n-button>
                          <n-button type="error" @click="editUser=!editUser">取消</n-button>
                        </n-space>
                        <n-space v-else>
                          <n-button type="primary" @click="editUser=!editUser">修改信息</n-button>
                          <n-button>修改密码</n-button>
                        </n-space>
                      </n-form-item>
                    </n-form>
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
import {onBeforeMount, reactive, ref} from "vue";
import {getUserInfo, getUserInfoMy, getUserName, updateUserInfo} from "@/utils/request.js";
import {useRouter} from "vue-router";

const router = useRouter()
const user = userStore()

const editUser = ref(false)


// console.log(user.getUserInfo().avatar)
onBeforeMount(() => {
  getUserMy()
})


async function updateInfo() {
   const header = {
      'Token': user.getUserInfo().token
    }
  console.log(userInfo)
    await updateUserInfo(userInfo,header).then(res => {
      if (res.data.code === 200) {
        console.log('修改成功')
        getUserMy()
        editUser.value = !editUser.value
      } else {
        console.log(res.data.msg)
      }
    }).catch(err => {
      console.log(err)
    })
}

let userInfo = reactive({
  username: '',
  email: '',
  phone: '',
  avatar: '',
  token: '',
  createTime: '',
})

async function getUserMy() {
  const header = {
    'Token': user.getUserInfo().token
  }
  await getUserInfoMy(header).then(res => {
    // console.log(res)
    if (res.data.code !== 200)
      router.push({name: 'login'})
    else {
      userInfo.username = res.data.data.username
      userInfo.email = res.data.data.email
      userInfo.phone = res.data.data.phone
      userInfo.avatar = res.data.data.avatar
      userInfo.createTime = res.data.data.createTime
      userInfo.token = user.getUserInfo().token

    }
  }).catch(err => {
    console.log(err)
    router.push({name: 'login'})
  })
}


</script>

<style scoped lang="scss">

</style>