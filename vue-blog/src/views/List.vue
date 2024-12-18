<template>
  <base-layout>
    <template #default>
      <n-layout content-style="padding:20px 10vw">
        <n-card>
          <n-space>
            <!--            <n-text>排序方式</n-text>-->
            <n-button @click="searchSoft(0)">
              <n-icon v-if="sort[0].visible==='1'" size="18" :component="sort[0].asc==='1'?MdArrowUp:MdArrowDown">
              </n-icon>
              类别排序
            </n-button>
            <n-button @click="searchSoft(1)">
             <n-icon v-if="sort[1].visible==='1'" size="18" :component="sort[1].asc==='1'?MdArrowUp:MdArrowDown">
              </n-icon>
              更新时间
            </n-button>
            <n-button @click="searchSoft(2)">
              <n-icon v-if="sort[2].visible==='1'" size="18" :component="sort[2].asc==='1'?MdArrowUp:MdArrowDown">
              </n-icon>
              浏览量
            </n-button>
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
import {useRouter, useRoute} from "vue-router";
import {MdArrowUp, MdArrowDown, MdSearch} from "@vicons/ionicons4";

const router = useRouter();
const route = useRoute();

const searchForm = reactive({})

function extractTextFromHtml(html) {
  const parser = new DOMParser();
  const doc = parser.parseFromString(html, 'text/html');
  return doc.body.textContent || '';
}

const postList = ref([]);

onBeforeMount(() => {
  getList()
})

function searchSoft(e) {
  switch (e) {
    case 0:
      sort[0].visible = "1";
      sort[1].visible = sort[2].visible = "0";
      sort[0].asc = sort[0].asc === '1' ? '0' : '1'
      break;
    case 1:
      sort[1].visible = "1";
      sort[0].visible = sort[2].visible = "0";
      sort[1].asc = sort[1].asc === '1' ? '0' : '1'
      break;
    case 2:
      sort[2].visible = "1";
      sort[1].visible = sort[0].visible = "0";
      sort[2].asc = sort[2].asc === '1' ? '0' : '1'
      break;
  }
  searchForm.soft=sort[e].name
  searchForm.asc=sort[e].asc
  getList()
}

const sort = reactive([{
  name: "pageView",
  asc: "1",
  visible: "0"
}, {
  name: "updateTime",
  asc: "1",
  visible: "0"
}, {
  name: "kind",
  asc: "1",
  visible: "0"
}])


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