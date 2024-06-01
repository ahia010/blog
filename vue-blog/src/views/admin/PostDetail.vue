<template>
  <n-form
      ref="formRef"
      :model="model"
      label-placement="left"
      label-width="auto"
      :style="{
      maxWidth: '1000px'
      }"
  >
    <n-form-item path="title" label="标题">
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

</template>

<script setup>
import '@wangeditor/editor/dist/css/style.css' // 引入 css
import {onBeforeUnmount, ref, shallowRef, onMounted, reactive} from 'vue'
import {Editor, Toolbar} from '@wangeditor/editor-for-vue'
import {useMessage} from "naive-ui";
// 编辑器实例，必须用 shallowRef
const editorRef = shallowRef()

// 内容 HTML
const valueHtml = ref('<p>hello</p>')

// 模拟 ajax 异步获取内容
onMounted(() => {
  setTimeout(() => {
    valueHtml.value = '<p>模拟 Ajax 异步设置内容</p>'
  }, 1500)
})

const mode = ref('default')

const toolbarConfig = {}
const editorConfig = {
  placeholder: '请输入内容...',
  MENU_CONF: {
    uploadImage: {
      server: 'api/img/upload',
      fieldName: 'file',
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
  title: null,
  kind: null
});


</script>

<style scoped lang="scss">

</style>