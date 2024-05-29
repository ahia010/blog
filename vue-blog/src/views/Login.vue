<template>
  <div class="login">
    <div class="login-box">
      <n-card style="width: 100%;height: 100%">
        <n-flex justify="center" align="center" style="margin-bottom: 50px;">
          <n-gradient-text :size="50" type="success">
            登录
          </n-gradient-text>
        </n-flex>
        <n-form
            ref="formRef"
            :model="loginForm"
            require-mark-placement="right-hanging"
            :show-feedback="false"
            size="large">
          <n-form-item path="username">
            <n-input v-model:value="loginForm.username" placeholder="请输入用户名"/>
          </n-form-item>
          <n-form-item path="password">
            <n-input v-model:value="loginForm.password" placeholder="请输入密码" type="password"/>
          </n-form-item>
          <n-form-item>
            <n-button type="primary" @click="goLogin()" block>登录</n-button>
          </n-form-item>
          <n-form-item>
            <n-button block>注册</n-button>
          </n-form-item>
        </n-form>
        <n-flex justify="end">
          <n-button :bordered="false" size="large" text>忘记密码？</n-button>
        </n-flex>
        <n-divider>
          第三方登录
        </n-divider>
        <n-button block color="#1BD66C" size="large" style="margin-bottom: 20px;">微信登录</n-button>
        <n-button block color="#159BFA" size="large">QQ登录</n-button>
      </n-card>
    </div>

  </div>
</template>
<script setup>
import {reactive, ref} from 'vue'
import {useMessage} from 'naive-ui'
import {login} from "@/utils/request.js";

const loginForm = reactive({
  username: '',
  password: '',
})

let msgReactive = null;
const message = useMessage();

const isLogin = ref(false)

function goLogin() {
  if (loginForm.username == "" || loginForm.password == "") {
    msgReactive = message.create("用户名和密码不能为空", {
      type: "error",
      duration: 2000
    });
  } else {
    if (!isLogin.value) {
      isLogin.value = true;
      msgReactive = message.create("登录中", {
        type: "loading",
        duration: 5000
      });
      login(loginForm).then(res => {
        console.log(res)
        if (res.data.code === 200) {
          msgReactive.content = "登录成功";
          msgReactive.type = "success";
          isLogin.value = false;

          // setTimeout(() => {
          //   isLogin.value = false;
          //   router.push("/admin");
          // }, 2000)
        } else {
          msgReactive.content = res.data.msg;
          msgReactive.type = "error";
          isLogin.value = false;
        }
      }).catch(err => {
        msgReactive.content = "登录失败";
        msgReactive.type = "error";
        isLogin.value = false;
      })
    }
  }
}

</script>
<style scoped lang="scss">
.login-box {
  width: 600px;

}

.login {
  width: 100vw;
  height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
}
</style>