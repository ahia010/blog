<template>
  <n-form
      ref="formRef"
      inline
      label-placement="left"
      label-width="auto"
      :model="formValue">
    <n-form-item label="标题:" path="title">
      <n-input v-model:value="formValue.title" placeholder="输入标题"/>
    </n-form-item>
    <n-form-item label="内容:" path="content">
      <n-input v-model:value="formValue.content" placeholder="输入内容"/>
    </n-form-item>
    <n-form-item>
      <n-button attr-type="button" @click="handleValidateClick">
        搜索
      </n-button>
    </n-form-item>
    <n-form-item>
      <n-button attr-type="button" @click="resetForm">
        重置
      </n-button>
    </n-form-item>
  </n-form>
  <n-space style="margin-bottom: 10px">
    <n-button @click="router.push({name:'postAdd'})">新增</n-button>
    <n-button @click="delPost(null)">删除</n-button>
  </n-space>
  <n-space vertical :size="12">
    <n-data-table
        ref="dataTableInst"
        :columns="columns"
        :data="data"
        :row-key="rowKey"
        @update:checked-row-keys="handleCheck"
    />
  </n-space>
</template>

<script setup>
import {h, onBeforeMount, reactive, ref} from 'vue'
import {NButton, NEllipsis, useMessage} from "naive-ui";
import {useRouter} from "vue-router";
import {deletePostRequest, deleteUserRequest, getPostList, getPostListAdmin} from "@/utils/request.js";
import {userStore} from "@/stores/user.js";

const router = useRouter();
const user=userStore();

const rowKey = (row) => row.id;

function handleCheck(rowKeys) {
  checkedRowKeysRef.value = rowKeys;
}

const columns = [
  {
    "type": "selection"
  },
  {
    title: 'id',
    key: 'id',
    width: 50
  },
  {
    title: '标题',
    key: 'title',
    ellipsis: {
      tooltip: true
    }
  },
  {
    title: '分类',
    key: 'kind',
  },
  {
    title: '内容',
    key: 'content',
    ellipsis: true,
    render(row) {
      return h(NEllipsis, {tooltip: false}, {default: () => extractTextFromHtml(row.content)})
    }
  },
  {
    title: '创建时间',
    key: 'createTime',
    sorter: {
      compare: (a, b) => a.createTime - b.createTime,
      multiple: 1
    }
  }, {
    title: '更新时间',
    key: 'updateTime',
    sorter: {
      compare: (a, b) => a.updateTime - b.updateTime,
      multiple: 2
    }
  }, {
    title: '操作',
    key: 'operation',
    render(row) {
      const edit = h(NButton, {
        type: 'primary',
        onClick: () => {
          router.push({name: 'postDetail', params: {id: row.id}})
        }
      }, {
        default: () => '编辑'

      })
      const del = h(
          NButton, {
            type: 'error',
            style: {
              marginLeft: '10px'
            },
            onClick:()=>{
              delPost(row.id)
            }
          }, {
            default: () => '删除'
          })
      const viewComment = h(
          NButton, {
            type: 'info',
            style: {
              marginLeft: '10px'
            },
            onClick:()=>{
              router.push({name:'comment', params: {id: row.id}})
            }
          }, {
            default: () => '查看评论'
          })
      return [edit, del, viewComment]
    }
  }
]
// const data = reactive(Array.from({length: 10}, (_, i) => ({
//   id: i,
//   title: '标题标题标题标题标题标题标题标题标题标题标题',
//   kind: '分类',
//   content: '内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容',
//   createTime: '2024-12-12 12:12:12',
//   updateTime: '2024-12-12 12:12:12',
// })))

const data = ref([]);
const checkedRowKeysRef = ref([]);

async function getList() {

  await getPostListAdmin(formValue).then(res => {
    if (res.data.code === 200)
      data.value = res.data.data.records;
    else
      message.error(res.data.msg)
  })
}

onBeforeMount(() => {
  getList()
})

function extractTextFromHtml(html) {
  const parser = new DOMParser();
  const doc = parser.parseFromString(html, 'text/html');
  return doc.body.textContent || '';
}

const formRef = ref(null);
const message = useMessage();
const formValue = reactive({
  title: "",
  content: "",
});

function resetForm() {
  formValue.title=formValue.content=''
  getList()
}

function handleValidateClick() {
  getList()
}

async function delPost(param) {
  const ids = new URLSearchParams();
  if (param == null) {
    if (checkedRowKeysRef.value.length === 0) {
      message.error('请选择要删除的数据')
      return
    }
    checkedRowKeysRef.value.forEach(e => {
      ids.append('ids', e)
    })
  } else
    ids.append('ids', param)
  await deletePostRequest(ids, {
    'Token': user.getUserInfo().token,
    'Content-Type': 'application/json'
  }).then(res => {
    if (res.data.code === 200) {
      message.success('删除成功')
      getList()
    } else {
      message.error(res.data.msg);
    }
  })
}


</script>
<style scoped lang="scss">
</style>