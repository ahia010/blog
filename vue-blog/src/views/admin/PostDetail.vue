<template>
  <n-form
      ref="formRef"
      :model="model"
      label-placement="left"
      label-width="auto"
      :style="{maxWidth: '1000px'}"
  >
    <n-form-item path="title" label="标题" size="large">
      <n-input v-model:value="model.title"/>
    </n-form-item>
    <n-form-item path="kind" label="类型">
      <n-input v-model:value="model.kind"/>
    </n-form-item>
  </n-form>
  <n-flex>
    内容
    <div style="border: 1px solid #ccc">
      <Toolbar
          style="border-bottom: 1px solid #ccc"
          :editor="editorRef"
          :defaultConfig="toolbarConfig"
          :mode="mode"
      />
      <Editor
          style="height: 500px; overflow-y: hidden;"
          v-model="valueHtml"
          :defaultConfig="editorConfig"
          :mode="mode"
          @onCreated="handleCreated"
      />
    </div>
  </n-flex>
  <n-space style="padding: 20px">
    <n-button type="primary" @click="submit">提交</n-button>
    <n-button @click="cancel">取消</n-button>
  </n-space>

</template>

<script setup>
import '@wangeditor/editor/dist/css/style.css' // 引入 css
import {onBeforeUnmount, ref, shallowRef, onMounted, reactive} from 'vue'
import {Editor, Toolbar} from '@wangeditor/editor-for-vue'
import {useMessage} from "naive-ui";
import {userStore} from "@/stores/user.js";
import {useRouter, useRoute} from "vue-router";
import {addPost, getPostDetail, updatePost} from "@/utils/request.js";

const router = useRouter();
const route = useRoute();

const user = userStore();

async function submit() {
  if (!model.title || !model.kind || !valueHtml.value) {
    message.error('填写内容不能为空');
    return;
  }
  let headers = {
    'Content-Type': 'application/json',
    'Token': user.userInfo.token
  }
  model.content = valueHtml.value
  if (route.path === '/admin/postAdd') {
    await addPost(model, headers).then(res => {
      if (res.data.code === 200) {
        message.success('新增成功')
      } else if (res.data.code === 401) {
        message.error('登录过期，请重新登录')
        router.push('/login')
        user.logout()
      } else {
        message.error(res.data.msg)
      }
    }).catch(err => {
      console.log(err)
    })
    // message.success('新增成功')
  } else {
    model.id = route.params.id
    await updatePost(model,headers).then(res => {
      if (res.data.code === 200) {
        message.success('修改成功')
        router.go(-1)
      } else if (res.data.code === 401) {
        message.error('登录过期，请重新登录')
        router.push('/login')
        user.logout()
      } else {
        message.error(res.data.msg)
      }
    }).catch(err => {
      console.log(err)
    })
  }
}

function cancel() {
  valueHtml.value = ''
  model.title = ''
  model.kind = ''
  router.go(-1)
}

// 编辑器实例，必须用 shallowRef
const editorRef = shallowRef()

// 内容 HTML
const valueHtml = ref('<p>hello，在这里输入内容哦</p>')

// 模拟 ajax 异步获取内容
// onMounted(() => {
//   setTimeout(() => {
//     valueHtml.value = '<p>模拟 Ajax 异步设置内容</p>'
//   }, 1500)
// })

onMounted(() => {
  console.log(route.path)
  if (route.path === '/admin/postAdd') {
    model.title = ''
    model.kind = ''
    valueHtml.value = "<p>hello，在这里输入内容哦</p>"
  } else {
    getDetailById()
  }
})

async function getDetailById() {
  await getPostDetail(route.params.id).then(res => {
    if (res.data.code === 200) {
      model.title = res.data.data.title
      model.kind = res.data.data.kind
      valueHtml.value = res.data.data.content
    } else {
      message.error(res.data.msg)
    }
  }).catch(err => {
    console.log(err)
  })
}


const mode = ref('default')

const toolbarConfig = {}
const editorConfig = {
  placeholder: '请输入内容...',
  MENU_CONF: {
    uploadImage: {
      server: 'api/upload/img',
      fieldName: 'file',
      headers: {
        Token: user.userInfo.token,
      },
      withCredentials: true,
    },
  },
}

// 组件销毁时，也及时销毁编辑器
onBeforeUnmount(() => {
  const editor = editorRef.value
  if (editor == null) return
  editor.destroy()
})

const handleCreated = (editor) => {
  editorRef.value = editor // 记录 editor 实例，重要！
}

const formRef = ref(null);
const message = useMessage();
const model = reactive({
  title: "",
  kind: "",
  content: valueHtml.value,
});


</script>

<style scoped lang="scss">

</style>