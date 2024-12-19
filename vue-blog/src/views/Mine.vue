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

              <n-upload
                  default-upload
                  action="/api/user/update"
                  @before-upload="beforeUpload"
                  :headers="{ 'Token': userInfo.token}"
                  :data="userInfo"
                  @finish="getUserMy"
              >
                <n-button type="success">修改头像</n-button>
              </n-upload>
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
                          <n-button @click="updatePasswordShow=!updatePasswordShow">修改密码</n-button>
                          <n-modal
                              v-model:show="updatePasswordShow"
                              preset="card"
                              style="width: 600px; position: fixed; top: 100px;left: 35%"
                              title="修改密码"
                              size="huge"
                          >

                            <n-form
                                ref="formRef"
                                :model="updatePasswordFromValue"
                                label-placement="left"
                                label-width="auto">
                              <n-form-item label="旧密码:" path="password">
                                <n-input v-model:value="updatePasswordFromValue.password" placeholder="输入旧密码"/>
                              </n-form-item>
                              <n-form-item label="新密码:" path="newPassword">
                                <n-input v-model:value="updatePasswordFromValue.newPassword" placeholder="输入新密码"/>
                              </n-form-item>
                              <n-form-item label="确认新密码:" path="confirmPassword">
                                <n-input v-model:value="updatePasswordFromValue.confirmPassword"
                                         placeholder="再次输入新密码"/>
                              </n-form-item>
                              <n-form-item>
                                <n-button attr-type="button" type="success" @click="updatePassword()">
                                  修改
                                </n-button>
                              </n-form-item>
                            </n-form>
                          </n-modal>
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
import {getUserInfo, getUserInfoMy, getUserName, updatePost, updateUserInfo} from "@/utils/request.js";
import {useRouter} from "vue-router";
import {NButton, useMessage} from "naive-ui";


const message = useMessage();
const router = useRouter()
const user = userStore()
const formRef = ref(null);
const editUser = ref(false)


async function beforeUpload(data) {
  const allowedTypes = ["image/png", "image/jpeg", "image/jpg"];
  if (!allowedTypes.includes(data.file.file?.type)) {
    message.error("只能上传图片文件（png, jpeg, jpg），请重新上传");
    return false;
  }
  return true;
}

const emit = defineEmits(["avatarUpdate"]);

let updatePasswordFromValue = reactive({
  password: '',
  newPassword: '',
  confirmPassword: ''
})

async function updatePassword() {
  if (updatePasswordFromValue.password === '' || updatePasswordFromValue.newPassword === '' || updatePasswordFromValue.confirmPassword === '') {
    message.error("密码不能为空")
    return
  }
  if (updatePasswordFromValue.newPassword !== updatePasswordFromValue.confirmPassword) {
    message.error("两次密码不一致")
    return
  }
  const headers = {
    'Token': user.getUserInfo().token,
    'Content-Type': 'application/json'
  }
  updatePasswordFromValue.token = userInfo.token
  updatePasswordFromValue.username = userInfo.username
  await updateUserInfo(updatePasswordFromValue, headers).then(res => {

    if (res.data.code === 200) {
      updatePasswordFromValue.password = updatePasswordFromValue.newPassword = updatePasswordFromValue.confirmPassword = ''
      message.success("修改成功")
      updatePasswordShow.value = !updatePasswordShow.value
    } else {
      message.error(res.data.msg)
    }

  }).catch(err => {
    message.error("修改失败")
    console.log(err)
  })
}


// console.log(user.getUserInfo().avatar)
onBeforeMount(() => {
  getUserMy()
})
const updatePasswordShow = ref(false)

async function updateInfo() {
  const header = {
    'Token': user.getUserInfo().token
  }
  console.log(userInfo)
  await updateUserInfo(userInfo, header).then(res => {
    if (res.data.code === 200) {
      message.success('修改成功')
      console.log('修改成功')
      getUserMy()
      editUser.value = !editUser.value
    } else {
      message.error(res.data.msg)
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
      user.serUserAvatar(res.data.data.avatar)
      emit("avatarUpdate", res.data.data.avatar);

    }
  }).catch(err => {
    console.log(err)
    router.push({name: 'login'})
  })
}


</script>

<style scoped lang="scss">

</style>