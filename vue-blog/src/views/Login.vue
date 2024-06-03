<template>
  <div class="login">
    <div class="login-box">
      <n-card style="width: 100%;height: 100%" v-if="route.name==='login'">
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
            <n-button block @click="goRegView()">注册</n-button>
          </n-form-item>
        </n-form>
        <n-flex justify="end" style="padding: 10px">
          <n-button :bordered="false" size="large" text @click="message.info('请联系管理员处理')">忘记密码？</n-button>
        </n-flex>
        <n-divider>
          第三方登录
        </n-divider>
        <n-button block color="#1BD66C" size="large" style="margin-bottom: 20px;" @click="message.info('敬请期待')">微信登录</n-button>
        <n-button block color="#159BFA" size="large" @click="message.info('敬请期待')">QQ登录</n-button>
      </n-card>
      <n-card style="width: 100%;height: 100%" v-else>
        <n-flex justify="center" align="center" style="">
          <n-gradient-text :size="50" type="success">
            注册账号
          </n-gradient-text>
        </n-flex>
        <n-form
            ref="formRef"
            :model="registerForm"
            require-mark-placement="right-hanging"
            :show-feedback="false"
            size="large">
          <n-form-item path="username">
            <n-input v-model:value="registerForm.username" placeholder="请输入用户名"/>
          </n-form-item>
          <n-form-item path="password">
            <n-input v-model:value="registerForm.password" placeholder="请输入密码" type="password"/>
          </n-form-item>
          <n-form-item path="repassword">
            <n-input v-model:value="registerForm.repassword" placeholder="请再次输入密码" type="password"/>
          </n-form-item>
          <n-form-item path="email">
            <n-input v-model:value="registerForm.email" placeholder="请输入邮箱"/>
          </n-form-item>
          <n-form-item path="password">
            <n-input v-model:value="registerForm.phone" placeholder="请输入手机号"/>
          </n-form-item>
          <n-form-item>
            <n-button block type="primary" @click="goReg()">注册</n-button>
          </n-form-item>
          <n-form-item>
            <n-button @click="router.push({name:'login'})" block>返回登录</n-button>
          </n-form-item>
        </n-form>

      </n-card>
    </div>

  </div>
</template>
<script setup>
import {reactive, ref} from 'vue'
import {useMessage} from 'naive-ui'
import {login, register} from "@/utils/request.js";
import {userStore} from "@/stores/user.js";
import {useRouter, useRoute} from "vue-router";

const router = useRouter();
const route = useRoute();

const user = userStore();

function goRegView() {
  router.push({name: 'register'})
  resetLoginForm()
}

const registerForm = reactive({
  username: '',
  password: '',
  repassword: '',
  email: '',
  phone: ''
})

const loginForm = reactive({
  username: '',
  password: '',
})


function resetLoginForm() {
  loginForm.username =
      loginForm.password = '';
}

function resetRegisterForm() {
  // 重置表单的每个字段到初始状态
  registerForm.username =
      registerForm.password =
          registerForm.repassword =
              registerForm.email =
                  registerForm.phone = '';
}

async function goReg() {
  if (registerForm.username === '' || registerForm.password === '') {
    message.error("用户名和密码不能为空")
    return;
  }
  if (registerForm.password !== registerForm.repassword) {
    message.error("新旧密码不一致")
    return;
  }

  if (registerForm.email === '' && registerForm.phone === '') {
    message.error("邮箱或手机号不能为空")
    return;
  }
  await register(registerForm).then(res => {
    console.log(res.data)
    if (res.data.code === 200) {

      msgReactive = message.create("注册成功！3秒后跳转登录", {
        type: 'success',
        duration: 3000
      })
      setTimeout(() => {
        resetRegisterForm()
        router.push({name: 'login'})
      }, 3000)
    } else {
      message.error(res.data.msg)
    }
  })

}


let msgReactive = null;
const message = useMessage();

const isLogin = ref(false)

async function goLogin() {
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
      await login(loginForm).then(res => {
        if (res.data.code == 200) {
          msgReactive.content = "登录成功";
          msgReactive.type = "success";
          const data = res.data.data
          resetLoginForm()
          user.setUserInfo(data.username, data.token, data.role, data.avatar)
          // console.log(user.getUserInfo());
          router.push('/')
        } else {
          msgReactive.content = res.data.msg;
          msgReactive.type = "error";
        }
        isLogin.value = false;
      }).catch(err => {
        console.log(err);
      })
      isLogin.value = false;
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
  background: linear-gradient(
          144deg, #af40ff, #5b42f3 50%, #00ddeb);
}
</style>