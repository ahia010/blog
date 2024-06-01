<template>
  <n-form
      ref="formRef"
      inline
      label-placement="left"
      label-width="auto"
      :model="formValue">
    <n-form-item label="标题:" path="title">
      <n-input v-model:value="formValue.title" placeholder="输入姓名" />
    </n-form-item>
    <n-form-item label="内容:" path="content">
      <n-input v-model:value="formValue.content" placeholder="输入年龄" />
    </n-form-item>
    <n-form-item>
      <n-button attr-type="button" @click="handleValidateClick">
        搜索
      </n-button>
    </n-form-item>
    <n-form-item>
      <n-button attr-type="button" @click="handleValidateClick">
        重置
      </n-button>
    </n-form-item>
  </n-form>
  <n-space style="margin-bottom: 10px">
    <n-button>新增</n-button>
    <n-button>删除</n-button>
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
import {h, reactive, ref} from 'vue'
import {NButton, useMessage} from "naive-ui";


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
  },
  {
    title: '创建时间',
    key: 'createTime',
    sorter: {
      compare: (a, b) => a.createTime - b.createTime,
      multiple: 1
    }
  },{
    title: '更新时间',
    key: 'updateTime',
    sorter: {
      compare: (a, b) => a.updateTime - b.updateTime,
      multiple: 2
    }
  },{
    title: '操作',
    key: 'operation',
    render(row){
     const edit = h(NButton,{
       type: 'primary',
     },{
        default: () => '编辑'

     })
      const del = h(
          NButton,{
       type: 'error',
        style: {
          marginLeft: '10px'
        }
     },{
        default: () => '删除'
      })
      const  viewComment = h(
          NButton,{
            type: 'info',
            style: {
              marginLeft: '10px'
            }
          },{
            default: () => '查看评论'
          })
      return [edit,del,viewComment]
    }
  }
]
const data = reactive(Array.from({length: 10}, (_, i) => ({
  id: i,
  title: '标题标题标题标题标题标题标题标题标题标题标题',
  kind: '分类',
  content: '内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容内容',
  createTime: '2024-12-12 12:12:12',
  updateTime: '2024-12-12 12:12:12',
})))
const checkedRowKeysRef = ref([]);


const formRef = ref(null);
const message = useMessage();
const formValue = ref({
  title: "",
  content: "",
});


function handleValidateClick(e) {

}

</script>
<style scoped lang="scss">

</style>