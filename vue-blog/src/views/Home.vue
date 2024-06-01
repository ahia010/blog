<template>
  <base-layout>
    <template #default>
      <n-layout>
        <n-layout content-style="padding:20px 10vw;display:flex;gap:20px">
          <n-grid :x-gap="12">
            <n-grid-item :span="18">
              <n-card>
                <n-list hoverable clickable>
                  <template v-for="item in homeList">
                    <n-list-item @click="goDetail(item.id)">
                      <n-thing :title="item.title" :content-style="{marginTop: '10px'}">
                        <template #description>
                          <n-space size="small" style="margin-top: 4px">
                            <template v-for="kind in item.kind">
                              <n-tag :bordered="false" type="info" size="small">
                                {{ kind }}
                              </n-tag>
                            </template>
                            <!--                            <n-tag :bordered="false" type="info" size="small">-->
                            <!--                              java-->
                            <!--                            </n-tag>-->
                            <!--                            <n-tag :bordered="false" type="info" size="small">-->
                            <!--                              vue-->
                            <!--                            </n-tag>-->
                          </n-space>
                        </template>
                        {{ item.content }}
                      </n-thing>
                    </n-list-item>
                  </template>

                  <!--                    <n-thing title="相见恨晚" content-style="margin-top: 10px;">-->
                  <!--                      <template #description>-->
                  <!--                        <n-space size="small" style="margin-top: 4px">-->
                  <!--                          <n-tag :bordered="false" type="info" size="small">-->
                  <!--                            java-->
                  <!--                          </n-tag>-->
                  <!--                          <n-tag :bordered="false" type="info" size="small">-->
                  <!--                            vue-->
                  <!--                          </n-tag>-->
                  <!--                        </n-space>-->
                  <!--                      </template>-->
                  <!--                      奋勇呀然后休息呀 住在我心里孤独的 孤独的海怪 痛苦之王 开始厌倦 深海的光 停滞的海浪住在我心里孤独的 孤独的海怪 痛苦之王-->
                  <!--                      开始厌倦 深海的光 停滞的海浪住在我心里孤独的 孤独的海怪 痛苦之王 开始厌倦 深海的光 停滞的海浪住在我心里孤独的-->
                  <!--                      孤独的海怪 痛苦之王 开始厌倦 深海的光 停滞的海浪住在我心里孤独的 孤独的海怪 痛苦之王 开始厌倦 深海的光-->
                  <!--                      停滞的海浪-->
                  <!--                    </n-thing>-->

                </n-list>
              </n-card>
            </n-grid-item>
            <n-grid-item :span="6">
              <n-card>
                <n-flex justify="center">
                  <n-image
                      style="margin-bottom: 20px;"
                      width="100"
                      src="https://07akioni.oss-cn-beijing.aliyuncs.com/07akioni.jpeg"
                      preview-disabled
                  />
                </n-flex>
                <n-flex justify="space-around">
                  <n-flex vertical>
                    <n-text>文章数</n-text>
                    <n-text>300</n-text>
                  </n-flex>
                  <n-flex vertical>
                    <n-text>更新时间</n-text>
                    <n-text>2021-10-10</n-text>
                  </n-flex>
                  <n-flex vertical>
                    <n-text>评论数</n-text>
                    <n-text>300</n-text>
                  </n-flex>
                </n-flex>
              </n-card>
              <n-card style="margin-top:20px">
                <n-text>浏览排行</n-text>
                <n-list hoverable clickable>
                  <template v-for="item in viewRankingList">
                  <n-list-item class="home-top" @click="goDetail(item.id)">
                    <n-thing :title="item.title" content-style="margin-top: 10px;">
                      <div class="home-top-content" :style="{maxWidth:topMaxWidth}">
                        {{item.content}}
                      </div>
                    </n-thing>
                  </n-list-item>
                  </template>


                </n-list>
              </n-card>
            </n-grid-item>
          </n-grid>
        </n-layout>
      </n-layout>
    </template>
  </base-layout>

</template>

<script setup>
import {useRoute, useRouter} from "vue-router";
import {getCurrentInstance, nextTick, onBeforeUnmount, onMounted, reactive, ref} from "vue";
import {debounce} from "@/utils/debonce.js";
import BaseLayout from "@/components/BaseLayout.vue";


const router = useRouter();
const route = useRoute();
const topMaxWidth = ref("");


const homeList = reactive(Array.from({length: 10}, () => ({
  id: '1',
  title: "相见恨晚",
  content: "奋勇呀然后休息呀 住在我心里孤独的 孤独的海怪 痛苦之王 开始厌倦 深海的光 停滞的海浪住在我心里孤独的 孤独的海怪 痛苦之王 开始厌倦 深海的光 停滞的海浪住在我心里孤独的 孤独的海怪 痛苦之王 开始厌倦 深海的光 停滞的海浪住在我心里孤独的 孤独的海怪 痛苦之王 开始厌倦 深海的光 停滞的海浪住在我心里孤独的 孤独的海怪 痛苦之王 开始厌倦 深海的光 停滞的海浪",
  kind: ["java", "vue"]
})));

const viewRankingList = reactive(Array.from({length:5 },()=>({
  id:'1',
  title:'文章标题',
  content:'住在我心里孤独的 孤独的海怪 痛苦之王 开始厌倦 深海的光 停滞的海浪住在我心里孤独的 孤独的海怪\n' +
      '                        痛苦之王\n' +
      '                        开始厌倦 深海的光 停滞的海浪住在我心里孤独的 孤独的海怪 痛苦之王 开始厌倦 深海的光 停滞的海浪住在我心里孤独的\n' +
      '                        孤独的海怪 痛苦之王 开始厌倦 深海的光 停滞的海浪住在我心里孤独的 孤独的海怪 痛苦之王 开始厌倦\n' +
      '                        深海的光\n' +
      '                        停滞的海浪'
})))

function goDetail(id) {
  router.push({
    name: "detail",
    params: {
      id: id
    }
  })
}

// const homeList = reactive([{
//   title: "相见恨晚",
//   content: "奋勇呀然后休息呀 住在我心里孤独的 孤独的海怪 痛苦之王 开始厌倦 深海的光 停滞的海浪住在我心里孤独的 孤独的海怪 痛苦之王 开始厌倦 深海的光 停滞的海浪住在我心里孤独的 孤独的海怪 痛苦之王 开始厌倦 深海的光 停滞的海浪住在我心里孤独的 孤独的海怪 痛苦之王 开始厌倦 深海的光 停滞的海浪住在我心里孤独的 孤独的海怪 痛苦之王 开始厌倦 深海的光 停滞的海浪",
//   kind: ["java", "vue"]
// }
// ]);


const screenWidth = ref(window.innerWidth);

onMounted(() => {
  const width = document.querySelector('.home-top').clientWidth;
  // document.querySelector('.home-top-content').style.maxWidth = `${width-30}px`;
  topMaxWidth.value = width - 30 + "px";

})


const handleResize = debounce(() => {
  screenWidth.value = window.innerWidth;
  console.log(`Screen width changed to: ${screenWidth.value}px`);
  // 这里可以添加其他响应屏幕尺寸变化的逻辑
  const width = document.querySelector('.home-top').clientWidth;
  // document.querySelector('.home-top-content').style.maxWidth = `${width-30}px`;
  topMaxWidth.value = width - 30 + "px";

}, 200); // 200毫秒内如果有新的resize事件，之前的调用会被取消，仅执行最后一次

// 在组件挂载时添加监听器
onMounted(() => {
  window.addEventListener('resize', handleResize);
});

// 在组件卸载时移除监听器，避免内存泄漏
onBeforeUnmount(() => {
  window.removeEventListener('resize', handleResize);
});


</script>

<style scoped>

.carousel-img {
  width: 100%;
  height: 400px;
  object-fit: cover;
}

.home-top-content {
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}
</style>