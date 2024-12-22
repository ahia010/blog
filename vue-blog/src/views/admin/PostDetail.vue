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
          @customPaste="customPaste"
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
import {addPost, getPostDetailAdmin, updatePost, uploadListImg} from "@/utils/request.js";
import axios from "axios";

const router = useRouter();
const route = useRoute();

const user = userStore();

function _convertHexToBase64(hexString) {
  return btoa(hexString.match(/\w{2}/g).map(char => {
    return String.fromCharCode(parseInt(char, 16));
  }).join(''));
}

function    base64ToFile(base64Data, filename) {
  // 解码Base64字符串
  const byteString = atob(base64Data.split(',')[1]);

  // 获取MIME类型
  const mimeString = base64Data.split(',')[0].split(':')[1].split(';')[0];

  // 将解码后的字符串转换为Uint8Array
  const ia = new Uint8Array(byteString.length);
  for (let i = 0; i < byteString.length; i++) {
    ia[i] = byteString.charCodeAt(i);
  }

  // 创建Blob对象
  const blob = new Blob([ia], {type: mimeString});

  // 创建File对象
  return new File([blob], filename, {type: mimeString});
}
function extractImageDataFromRtf(rtfData, ignoreHeadersFooters = true) {
  if (!rtfData) {
    return [];
  }
  const regexPictureHeader = /{\\pict[\s\S]+?({\\\*\\blipuid\s?[\da-fA-F]+)[\s}]*/
  const regexPicture = new RegExp('(?:(' + regexPictureHeader.source + '))([\\da-fA-F\\s]+)\\}', 'g');
  const images = rtfData.match(regexPicture);
  const result = [];

  if (images) {
    for (const image of images) {
      let imageType = false;

      if (image.includes('\\pngblip')) {
        imageType = 'image/png';
      } else if (image.includes('\\jpegblip')) {
        imageType = 'image/jpeg';
      }

      if (imageType) {
        //是否跳过页眉页脚
        if (ignoreHeadersFooters) {
          const headerFooterRegex = /{\\header[\s\S]+?}\\par|{\\footer[\s\S]+?}\\par/g;
          if (headerFooterRegex.test(image)) {
            continue;
          }
        }
        result.push({
          hex: image.replace(regexPictureHeader, '').replace(/[^\da-fA-F]/g, ''),
          type: imageType
        });
      }
    }
  }

  return result;
}

const customPaste = (editor, event, callback) => {
  // 获取粘贴的html部分（？？没错粘贴word时候，一部分内容就是html），该部分包含了图片img标签
  let html = event.clipboardData.getData('text/html');
  // 获取rtf数据（从word、wps复制粘贴时有），复制粘贴过程中图片的数据就保存在rtf中
  let rtf = event.clipboardData.getData('text/rtf');
  // 该条件分支即表示要自定义word粘贴
  // 列表缩进会超出边框，直接过滤掉
  if (html && rtf) {
    let rtfImageData = extractImageDataFromRtf(rtf)
    if (rtfImageData.length > 0) {
      // 上传图片
      const imgUrls = []
      let formData = new FormData();
      for (let i = 0; i < rtfImageData.length; i++) {
        let file = `data:${rtfImageData[i].type};base64,${_convertHexToBase64(rtfImageData[i].hex)}`
        let file1 = base64ToFile(file, i + 'test.png')
        formData.append('files[]', file1);
      }

      let headers = {
        'Content-Type': 'multipart/form-data',
        'Token': user.userInfo.token
      }
      uploadListImg(formData,headers).then(res => {
        console.log(res.data)
        res.data.data.forEach(e => {
          imgUrls.push(e)
        })
        html = html.replace(/text\-indent:\-(.*?)pt/gi, '')

        let index = 0;
        let urls = [...imgUrls];

        html = html.replace(/src="[^"]*"/g, 'src=""');

        html = html.replace(/(<v:imagedata src="[^"]*"(\s+o:title="[^"]*")?\/>)([\s\S]*?<\/p>)/g, function (match, p1, p2, p3) {
          return p1 + p3 + "<img src=\"" + urls[index++] + "\"> <br>";
        });
        editor.dangerouslyInsertHtml(html)
        // 阻止默认的粘贴行为
        event.preventDefault();
        callback(false);
      }).catch(err => {
        console.log(err)
      })
    }
    // // 阻止默认的粘贴行为
    event.preventDefault();
    callback(false);
  } else {
    callback(true);
  }
}


async function submit() {
  if (!model.title || !model.kind || valueHtml.value.length === 0) {
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
  } else {
    model.id = route.params.id
    await updatePost(model, headers).then(res => {
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
  await getPostDetailAdmin(route.params.id).then(res => {
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