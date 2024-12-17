<template>
  <base-layout>
    <template #default>
      <n-layout content-style="padding:20px 10vw">
        <n-card>
          <n-space>
<!--            <n-text>排序方式</n-text>-->
            <n-button>类别排序</n-button>
            <n-button>更新时间</n-button>
            <n-button>浏览量</n-button>
          </n-space>
        </n-card>
        <n-card>
          <n-list hoverable clickable>
            <template v-for="item in postList">
              <n-list-item @click="goDetail(item.id)">
                <n-thing :title="item.title" :content-style="{marginTop: '10px'}">
                  <template #description>
                    <n-space size="small" style="margin-top: 4px">
                      <n-tag :bordered="false" type="info" size="small">
                        {{ item.kind }}
                      </n-tag>
                    </n-space>
                  </template>
                  <n-ellipsis :line-clamp="2" :tooltip="false">
                  {{ extractTextFromHtml(item.content) }}
                  </n-ellipsis>
                </n-thing>
              </n-list-item>
            </template>
          </n-list>
        </n-card>
      </n-layout>
    </template>
  </base-layout>
</template>

<script setup>
import BaseLayout from "@/components/BaseLayout.vue";
import {onBeforeMount, reactive, ref, watch} from "vue";
import {getPostList} from "@/utils/request.js";
import {useRouter,useRoute} from "vue-router";

const router = useRouter();
const route = useRoute();

const searchForm = reactive({

})
function extractTextFromHtml(html) {
  const parser = new DOMParser();
  const doc = parser.parseFromString(html, 'text/html');
  return doc.body.textContent || '';
}

const postList = ref([]);

onBeforeMount(() => {
  getList()
})


async function getList() {
  if (!route.query.pageSize)
    searchForm.pageSize = 10
  else searchForm.pageSize = route.query.pageSize

  if (route.query.search)
    searchForm.search = route.query.search
  await getPostList(searchForm).then(res => {
    if (res.data.code === 200)
      postList.value = res.data.data.records
  })
}

function goDetail(id) {
  router.push({
    name: "detail",
    params: {
      id: id
    }
  })
}
watch(() => route.query, () => {
  console.log("1234564964654")
  getList()
})

</script>

<style scoped lang="scss">

</style>