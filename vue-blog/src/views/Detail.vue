<template>
  <base-layout>
    <template #default>
      <n-layout content-style="padding:20px 10vw">
        <n-grid :x-gap="16">
          <n-gi :span="18">
            <n-card style="min-height: 80vh">
              <n-flex justify="start" style="font-weight: bold;font-size: 40px;margin-left: 40px;">{{ post.title }}
              </n-flex>
              <n-layout content-style="padding:20px">
                <n-space align="center">
                  <div> 作者 ：{{ post.username }}</div>
                  <div> {{ getTimeString(post.createTime, post.updateTime) }}</div>
                  <n-tag>{{ post.kind }}</n-tag>
                </n-space>
                <n-divider/>
              </n-layout>
              <div v-html="highlightedContent"></div>
            </n-card>
          </n-gi>
          <n-gi :span="6">
            <n-card class="right-card">
              <n-list clickable>
                <template #header>
                  <n-text style="font-size: 25px;margin-left: 20px;" strong>
                    评论区
                  </n-text>
                </template>
                <template #footer>
                  <div style="padding: 0 20px">
                    <n-input
                        v-model:value="commentForm.content"
                        type="textarea"
                        placeholder="请输入评论内容"
                        style="margin-bottom: 20px;"
                    />
                    <n-button block @click="comment()">发表评论</n-button>
                  </div>
                </template>
                <n-scrollbar style="height: 60vh">
                  <template v-if="commentList.length>0" v-for="item in commentList">
                    <n-list-item>
                      <!--                      <n-grid style="margin-left: 20px;">-->
                      <!--                        <n-grid-item :span="3">-->
                      <!--                          <n-image-->
                      <!--                              width="40"-->
                      <!--                              height="40"-->
                      <!--                              object-fit="cover"-->
                      <!--                              preview-disabled-->
                      <!--                              style="margin-top: 5px"-->
                      <!--                              :src="item.avatar"/>-->
                      <!--                        </n-grid-item>-->
                      <!--                        <n-grid-item :span="21" style="margin-right: 40px;margin-left: 5px;">-->
                      <!--                          <n-space justify="start">-->
                      <!--                            <n-text>用户名:{{ item.username }}</n-text>-->
                      <!--                            <n-text>时间 {{ item.time }}</n-text>-->
                      <!--                          </n-space>-->
                      <!--                          <n-space style="margin-top: 5px">-->
                      <!--                            <n-text>{{ item.content }}</n-text>-->
                      <!--                          </n-space>-->

                      <!--                        </n-grid-item>-->
                      <!--                      </n-grid>-->
                      <n-flex justify="start" style="flex-flow:nowrap;padding: 0 20px">
                        <n-image
                            width="40"
                            height="40"
                            object-fit="cover"
                            preview-disabled
                            style="margin-top: 5px"
                            :src="item.avatar"/>
                        <div>
                          <n-space justify="start">
                            <n-text>用户名:{{ item.username }}</n-text>
                            <n-text>时间 {{ item.createTime }}</n-text>
                          </n-space>
                          <n-space style="margin-top: 5px">
                            <n-text>{{ item.content }}</n-text>
                          </n-space>
                        </div>

                      </n-flex>
                    </n-list-item>
                  </template>
                  <template v-else>
                    <div style="width: 100%;height: 60vh;display: flex;align-items: center;justify-content: center">
                      <span style="font-size: 20px">暂无评论</span>
                    </div>
                  </template>
                </n-scrollbar>
              </n-list>
            </n-card>
          </n-gi>
        </n-grid>
      </n-layout>
    </template>
  </base-layout>
</template>

<script setup>

import BaseLayout from "@/components/BaseLayout.vue";
import {useRoute, useRouter} from "vue-router";
import {onMounted, reactive, ref} from "vue";
import {addPostComment, getPostDetail} from "@/utils/request.js";
import hljs from 'highlight.js';
import 'highlight.js/styles/github-dark.css';
import {useMessage} from "naive-ui";
import {userStore} from "@/stores/user.js";


const message = useMessage();

// const commentList = ref(Array.from({length: 12}, () => ({
//   username: "1",
//   time: "2024-06-23 18:49:49",
//   content: "我觉得这个文章还有很多不完善的地方，比如这里的这个和那里的那个，有明显的错误",
//   avatar: "https://07akioni.oss-cn-beijing.aliyuncs.com/07akioni.jpeg"
//
// })));
const commentList = ref([]);

const commentForm = reactive({
  postId: "",
  content: ""
})

const route = useRoute();
const router = useRouter();

const id = ref(route.params.id);
const user = userStore();

function getTimeString(time, time2) {
  if (time === time2) {
    return "创建于 " + time
  } else {
    return "更新于 " + time2
  }
}

async function comment() {
  if (!user.getUserInfo().token) {
    message.create("请先登录", {
      type: "warning",
      duration: 3000
    });
    return
  }
  if (commentForm.content.trim() === "") {
    message.create("回复不能为空", {
      type: "warning",
      duration: 3000
    });
    return
  }
  let headers = {
    'Content-Type': 'application/json',
    'Token': user.userInfo.token
  }
  commentForm.postId = route.params.id
  await addPostComment(commentForm, headers).then(res => {
    if (res.data.code === 200) {
      message.create("评论成功", {
        type: "success",
        duration: 3000
      });
      commentList.value = res.data.data
      commentForm.content = ""
    } else {
      message.create(res.data.msg, {
        type: "error",
        duration: 3000
      });
    }

  })

}

const post = ref({
  title: "文章标题",
  username: "作者",
  createTime: "2021-10-10",
  kind: "分类",
  content: "<div>\n" +
      "    <h1>欢迎访问我们的官方网站</h1>\n" +
      "    <p>我们致力于提供最高质量的服务。下面是我们提供的一些主要服务:</p>\n" +
      "    <ul>\n" +
      "        <li><strong>网站设计</strong> - 采用最新技术打造响应式网站。</li>\n" +
      "        <li><strong>移动应用开发</strong> - 开发跨平台的移动应用。</li>\n" +
      "        <li><strong>数据分析</strong> - 提供企业级的数据分析服务。</li>\n" +
      "    </ul>\n" +
      "<p>使用v-html会使您的应用面临跨站脚本（XSS）的风险，因为它可以渲染用户提供的任何HTML内容。如果您要渲染的HTML内容来自不可靠的来源，很重要的一点是要确保内容在插入之前被清理，以避免XSS攻击。常见的做法是使用库如DOMPurify来清理HTML内容。</p>" +
      "    <p>如果您有任何疑问或者合作意向，请不要犹豫，联系我们！</p>\n" +
      "    <p>联系方式: <a href=\"mailto:info@ourcompany.com\">info@ourcompany.com</a></p>\n" +
      "    <img src=\"\" alt=\"公司图片\" style=\"width:100%; height:auto;\">\n" +
      "</div>"
});

const highlightedContent = ref('');

onMounted(async () => {
  await getPostDetail(route.params.id).then(res => {
    if (res.data.code !== 200) {
      router.push({path: '/'})
    } else {
      post.value = res.data.data.post
      commentList.value = res.data.data.comments
      getHighLighted()
    }
  })


})

function getHighLighted() {
  highlightedContent.value = post.value.content.replace(/<pre><code class="(.*?)">([\s\S]*?)<\/code><\/pre>/g, (match, lang, code) => {
    const highlightedCode = hljs.highlightAuto(code).value;
    return `<pre><code class="hljs" style="width: 98%">${highlightedCode}</code></pre>`;
    // console.log(lang, code)
    // return `<pre><code class="hljs ${lang}"></code></pre>`;
  });
}

</script>

<style scoped lang="scss">
:deep(.right-card .n-card__content) {
  padding: 0 0 20px 0;
}
</style>