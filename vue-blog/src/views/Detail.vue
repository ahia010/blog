<template>
  <base-layout>
    <template #default>
      <n-layout content-style="padding:20px 10vw;">
        <n-card>
          <n-flex justify="start" style="font-weight: bold;font-size: 40px">我是文章标题</n-flex>
          <n-layout content-style="padding:20px">
            {{page.author}} {{page.time}} <n-tag>{{page.kind}}</n-tag>
            <n-divider />
          </n-layout>
          <n-layout  v-html="page.content"></n-layout>
        </n-card>
      </n-layout>
    </template>
  </base-layout>
</template>

<script setup>

import BaseLayout from "@/components/BaseLayout.vue";
import {useRoute, useRouter} from "vue-router";
import {reactive, ref} from "vue";

const route = useRoute();

const id = ref(route.params.id);

const page = reactive({
  title: "文章标题",
  author: "作者",
  time: "2021-10-10",
  kind: "分类",
  content: "<div>\n" +
      "    <h1>欢迎访问我们的官方网站</h1>\n" +
      "    <p>我们致力于提供最高质量的服务。下面是我们提供的一些主要服务:</p>\n" +
      "    <ul>\n" +
      "        <li><strong>网站设计</strong> - 采用最新技术打造响应式网站。</li>\n" +
      "        <li><strong>移动应用开发</strong> - 开发跨平台的移动应用。</li>\n" +
      "        <li><strong>数据分析</strong> - 提供企业级的数据分析服务。</li>\n" +
      "    </ul>\n" +
      "<p>使用v-html会使您的应用面临跨站脚本（XSS）的风险，因为它可以渲染用户提供的任何HTML内容。如果您要渲染的HTML内容来自不可靠的来源，很重要的一点是要确保内容在插入之前被清理，以避免XSS攻击。常见的做法是使用库如DOMPurify来清理HTML内容。</p>"+
      "    <p>如果您有任何疑问或者合作意向，请不要犹豫，联系我们！</p>\n" +
      "    <p>联系方式: <a href=\"mailto:info@ourcompany.com\">info@ourcompany.com</a></p>\n" +
      "    <img src=\"https://example.com/path_to_your_image.jpg\" alt=\"公司图片\" style=\"width:100%; height:auto;\">\n" +
      "</div>"
});


</script>

<style scoped lang="scss">

</style>